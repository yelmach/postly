import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { PostService } from '@/services/post.service';
import { PostRequest } from '@/models/post';
import { HttpErrorResponse } from '@angular/common/http';
import { PostFormComponent } from '@/components/post-form/post-form.component';

@Component({
  selector: 'app-new-post',
  imports: [CommonModule, PostFormComponent],
  templateUrl: './new-post.html',
  styleUrl: './new-post.scss',
})
export class NewPost {
  private postService = inject(PostService);
  private router = inject(Router);

  isSubmitting = signal(false);

  onFormSubmit(formData: { title: string; content: string; mediaUrls?: string[] }) {
    this.isSubmitting.set(true);

    const postRequest: PostRequest = {
      title: formData.title,
      content: formData.content,
      mediaUrls: formData.mediaUrls,
    };

    this.postService.createPost(postRequest).subscribe({
      next: (response) => {
        this.isSubmitting.set(false);
        this.router.navigate(['/profile']);
      },
      error: (error: HttpErrorResponse) => {
        this.isSubmitting.set(false);
        // Error handling is done in the form component
        console.error('Failed to create post:', error);
      },
    });
  }

  onFormCancel() {
    this.router.navigate(['/home']);
  }
}
