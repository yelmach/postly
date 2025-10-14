import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { PostService } from '@/services/post.service';
import { PostResponse, PostRequest } from '@/models/post';
import { HttpErrorResponse } from '@angular/common/http';
import { PostFormComponent } from '@/components/post-form/post-form.component';
import { ErrorStateComponent } from '@/components/error-state/error-state.component';

@Component({
  selector: 'app-edit-post',
  imports: [CommonModule, PostFormComponent, ErrorStateComponent],
  templateUrl: './edit-post.html',
  styleUrl: './edit-post.scss',
})
export class EditPost implements OnInit {
  private postService = inject(PostService);
  private router = inject(Router);

  post = signal<PostResponse | null>(null);
  errorMessage = signal<string>('');
  isSubmitting = signal(false);

  ngOnInit() {
    const navigation = this.router.getCurrentNavigation();
    const state = navigation?.extras?.state || this.router.lastSuccessfulNavigation?.extras?.state;

    if (state && state['post']) {
      this.post.set(state['post'] as PostResponse);
    } else {
      this.errorMessage.set('No post data provided. Please navigate from a post.');
    }
  }

  onFormSubmit(formData: { title: string; content: string; mediaUrls?: string[] }) {
    const postId = this.post()?.id;
    if (!postId) return;

    this.isSubmitting.set(true);

    const postRequest: PostRequest = {
      title: formData.title,
      content: formData.content,
      mediaUrls: formData.mediaUrls,
    };

    this.postService.updatePost(postId, postRequest).subscribe({
      next: (response) => {
        this.isSubmitting.set(false);
        this.router.navigate(['/post', postId]);
      },
      error: (error: HttpErrorResponse) => {
        this.isSubmitting.set(false);
        if (error.error?.message) {
          this.setFormError(error.error.message);
        } else if (error.error?.details) {
          this.setFormError('Please check your input and try again.');
        } else {
          this.setFormError('Failed to update post. Please try again.');
        }
      },
    });
  }

  onFormCancel() {
    const postId = this.post()?.id;
    if (postId) {
      this.router.navigate(['/post', postId]);
    } else {
      this.router.navigate(['/home']);
    }
  }

  setFormError(error: string) {
    console.error('Form error:', error);
  }

  goBack() {
    this.router.navigate(['/home']);
  }
}
