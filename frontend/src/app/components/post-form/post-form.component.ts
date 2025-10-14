import { Component, inject, OnInit, OnDestroy, signal, input, output, effect } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatChipsModule } from '@angular/material/chips';
import { PostService } from '@/services/post.service';
import { PostMediaResponse, MediaType, PostResponse } from '@/models/post';
import { HttpErrorResponse } from '@angular/common/http';
import { ErrorBannerComponent } from '@/components/error-banner/error-banner.component';
import EasyMDE from 'easymde';

@Component({
  selector: 'app-post-form',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatChipsModule,
    ErrorBannerComponent,
  ],
  templateUrl: './post-form.component.html',
  styleUrl: './post-form.component.scss',
})
export class PostFormComponent implements OnInit, OnDestroy {
  private postService = inject(PostService);

  // Inputs
  mode = input<'create' | 'edit'>('create');
  postData = input<PostResponse | null>(null);
  isSubmitting = input<boolean>(false);

  // Outputs
  formSubmit = output<{ title: string; content: string; mediaUrls?: string[] }>();
  formCancel = output<void>();

  postForm = new FormGroup({
    title: new FormControl('', [
      Validators.required,
      Validators.minLength(3),
      Validators.maxLength(100),
    ]),
    content: new FormControl('', [Validators.required, Validators.minLength(10)]),
  });

  formError = signal('');
  uploadedMedia = signal<PostMediaResponse[]>([]);
  isUploadingMedia = signal(false);

  private easyMDE: EasyMDE | null = null;

  constructor() {
    effect(() => {
      const post = this.postData();
      if (post && this.mode() === 'edit') {
        this.postForm.patchValue({
          title: post.title,
          content: post.content,
        });

        if (this.easyMDE) {
          this.easyMDE.value(post.content);
        }

        if (post.mediaUrls && post.mediaUrls.length > 0) {
          this.uploadedMedia.set(post.mediaUrls);
        }
      }
    });
  }

  ngOnInit() {
    setTimeout(() => {
      this.initializeEditor();
    }, 0);
  }

  ngOnDestroy() {
    if (this.easyMDE) {
      this.easyMDE.toTextArea();
      this.easyMDE = null;
    }
  }

  private initializeEditor() {
    const textarea = document.getElementById('content-editor') as HTMLTextAreaElement;
    if (!textarea) return;

    this.easyMDE = new EasyMDE({
      element: textarea,
      placeholder: 'Share your learning experience, discoveries, or thoughts...',
      spellChecker: false,
      toolbar: [
        'bold',
        'italic',
        'heading',
        '|',
        'quote',
        'unordered-list',
        'ordered-list',
        '|',
        'link',
        {
          name: 'upload-image',
          action: () => this.triggerMediaUpload('image'),
          className: 'fa fa-image',
          title: 'Upload Image',
        },
        {
          name: 'upload-video',
          action: () => this.triggerMediaUpload('video'),
          className: 'fa fa-video',
          title: 'Upload Video',
        },
        '|',
        'preview',
      ],
      status: ['lines', 'words', 'cursor'],
      minHeight: '400px',
      maxHeight: '500px',
    });

    // Set initial value if in edit mode
    const post = this.postData();
    if (post && this.mode() === 'edit') {
      this.easyMDE.value(post.content);
    }

    this.easyMDE.codemirror.on('change', () => {
      const content = this.easyMDE?.value() || '';
      this.postForm.patchValue({ content }, { emitEvent: false });
    });
  }

  private triggerMediaUpload(type: 'image' | 'video') {
    const input = document.createElement('input');
    input.type = 'file';
    input.accept =
      type === 'image'
        ? 'image/jpeg,image/jpg,image/png,image/webp,image/gif'
        : 'video/mp4,video/webm,video/mov';

    input.onchange = (event: any) => {
      const file = event.target.files[0];
      if (file) {
        this.uploadMedia(file);
      }
    };

    input.click();
  }

  uploadMedia(file: File) {
    this.isUploadingMedia.set(true);
    this.formError.set('');

    this.postService.uploadMedia(file).subscribe({
      next: (response: PostMediaResponse) => {
        this.uploadedMedia.update((media) => [...media, response]);
        this.insertMediaIntoEditor(response);
        this.isUploadingMedia.set(false);
      },
      error: (error: HttpErrorResponse) => {
        this.isUploadingMedia.set(false);
        if (error.error?.message) {
          this.formError.set(error.error.message);
        } else {
          this.formError.set('Failed to upload media. Please try again.');
        }
      },
    });
  }

  private insertMediaIntoEditor(media: PostMediaResponse) {
    if (!this.easyMDE) return;

    const cm = this.easyMDE.codemirror;
    const cursor = cm.getCursor();

    let markdown = '';
    if (media.mediaType === MediaType.IMAGE) {
      markdown = `![Image](${media.mediaUrl})\n`;
    } else if (media.mediaType === MediaType.VIDEO) {
      markdown = `<video controls src="${media.mediaUrl}" style="max-width: 100%; border-radius: 6px;"></video>\n`;
    }

    cm.replaceRange(markdown, cursor);
  }

  onSubmit() {
    this.formError.set('');

    if (this.postForm.invalid) {
      Object.keys(this.postForm.controls).forEach((key) => {
        const control = this.postForm.get(key);
        if (control?.invalid) {
          control.markAsTouched();
        }
      });
      return;
    }

    const formValue = this.postForm.value;
    const mediaUrls = this.uploadedMedia().map((m) => m.mediaUrl);

    this.formSubmit.emit({
      title: formValue.title || '',
      content: formValue.content || '',
      mediaUrls: mediaUrls.length > 0 ? mediaUrls : undefined,
    });
  }

  onCancel() {
    const message =
      this.mode() === 'edit'
        ? 'Are you sure you want to discard your changes?'
        : 'Are you sure you want to discard this post?';

    if (confirm(message)) {
      this.formCancel.emit();
    }
  }

  getErrorMessage(fieldName: string): string {
    const field = this.postForm.get(fieldName);
    if (!field || field.valid || field.untouched) return '';

    const errors = field.errors;
    if (!errors) return '';

    if (errors['required']) return `${fieldName} is required`;
    if (errors['minlength'])
      return `Must be at least ${errors['minlength'].requiredLength} characters`;
    if (errors['maxlength'])
      return `Must not exceed ${errors['maxlength'].requiredLength} characters`;

    return '';
  }

  setError(error: string) {
    this.formError.set(error);
  }

  getTitle(): string {
    return this.mode() === 'edit' ? 'Edit Article' : 'Create New Article';
  }

  getSubmitButtonText(): string {
    if (this.isSubmitting()) {
      return this.mode() === 'edit' ? 'Updating...' : 'Publishing...';
    }
    return this.mode() === 'edit' ? 'Update Article' : 'Publish Article';
  }
}
