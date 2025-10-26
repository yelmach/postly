import { Component, input, output, signal, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { FormsModule } from '@angular/forms';
import { CommentResponse } from '@/models/comment';
import { Router } from '@angular/router';
import { AuthService } from '@/services/auth.service';
import { CommentService } from '@/services/comment.service';

@Component({
  selector: 'app-comment-card',
  imports: [
    CommonModule,
    MatCardModule,
    MatIconModule,
    MatButtonModule,
    MatMenuModule,
    MatFormFieldModule,
    MatInputModule,
    FormsModule,
  ],
  templateUrl: './comment-card.component.html',
  styleUrl: './comment-card.component.scss',
})
export class CommentCardComponent {
  comment = input.required<CommentResponse>();
  postId = input.required<number>();

  commentUpdated = output<CommentResponse>();
  commentDeleted = output<number>();

  isEditing = signal(false);
  editContent = signal('');
  isSubmitting = signal(false);

  private router = inject(Router);
  private authService = inject(AuthService);
  private commentService = inject(CommentService);

  onAuthorClick(event: Event) {
    event.stopPropagation();
    const author = this.comment().author;
    this.router.navigate(['/profile', author.username]);
  }

  isCommentOwner(): boolean {
    const currentUser = this.authService.currentUser();
    return currentUser?.id === this.comment().author.id;
  }

  onEditClick(): void {
    this.editContent.set(this.comment().content);
    this.isEditing.set(true);
  }

  onCancelEdit(): void {
    this.isEditing.set(false);
    this.editContent.set('');
  }

  onSaveEdit(): void {
    const trimmedContent = this.editContent().trim();
    if (!trimmedContent || this.isSubmitting()) return;

    this.isSubmitting.set(true);
    this.commentService
      .updateComment(this.postId(), this.comment().id, { content: trimmedContent })
      .subscribe({
        next: (updatedComment) => {
          this.commentUpdated.emit(updatedComment);
          this.isEditing.set(false);
          this.isSubmitting.set(false);
        },
        error: (error) => {
          console.error('Failed to update comment:', error);
          this.isSubmitting.set(false);
        },
      });
  }

  onDeleteClick(): void {
    if (confirm('Are you sure you want to delete this comment? This action cannot be undone.')) {
      this.commentService.deleteComment(this.postId(), this.comment().id).subscribe({
        next: () => {
          this.commentDeleted.emit(this.comment().id);
        },
        error: (error) => {
          console.error('Failed to delete comment:', error);
        },
      });
    }
  }

  getRelativeTime(dateString: string): string {
    const date = new Date(dateString);
    const now = new Date();
    const seconds = Math.floor((now.getTime() - date.getTime()) / 1000);

    if (seconds < 60) return 'just now';
    if (seconds < 3600) return `${Math.floor(seconds / 60)}m ago`;
    if (seconds < 86400) return `${Math.floor(seconds / 3600)}h ago`;
    if (seconds < 604800) return `${Math.floor(seconds / 86400)}d ago`;
    if (seconds < 2592000) return `${Math.floor(seconds / 604800)}w ago`;

    return date.toLocaleDateString();
  }
}
