import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { FormsModule } from '@angular/forms';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { marked } from 'marked';
import { PostService } from '@/services/post.service';
import { AuthService } from '@/services/auth.service';
import { CommentService, PageResponse } from '@/services/comment.service';
import { PostResponse } from '@/models/post';
import { CommentResponse } from '@/models/comment';
import { LoadingSpinnerComponent } from '@/components/loading-spinner/loading-spinner.component';
import { ErrorStateComponent } from '@/components/error-state/error-state.component';
import { CommentCardComponent } from '@/components/comment-card/comment-card.component';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-post-detail',
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatMenuModule,
    MatProgressSpinnerModule,
    MatFormFieldModule,
    MatInputModule,
    FormsModule,
    LoadingSpinnerComponent,
    ErrorStateComponent,
    CommentCardComponent,
  ],
  templateUrl: './post-detail.html',
  styleUrl: './post-detail.scss',
})
export class PostDetail implements OnInit {
  private postService = inject(PostService);
  private commentService = inject(CommentService);
  private authService = inject(AuthService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private sanitizer = inject(DomSanitizer);

  post = signal<PostResponse | null>(null);
  isLoading = signal(true);
  errorMessage = signal<string>('');
  renderedContent = signal<SafeHtml>('');

  // Like state
  likesCount = signal<number>(0);
  isLiked = signal<boolean>(false);

  // Comments state
  comments = signal<CommentResponse[]>([]);
  commentsLoading = signal<boolean>(false);
  commentText = signal<string>('');
  isSubmittingComment = signal<boolean>(false);

  ngOnInit() {
    const postId = this.route.snapshot.paramMap.get('id');
    if (postId) {
      this.loadPost(Number(postId));
      this.loadComments(Number(postId));
    } else {
      this.errorMessage.set('Invalid post ID');
      this.isLoading.set(false);
    }
  }

  loadPost(id: number) {
    this.isLoading.set(true);
    this.errorMessage.set('');

    this.postService.getPost(id).subscribe({
      next: (post: PostResponse) => {
        this.post.set(post);
        this.likesCount.set(post.likesCount);
        this.isLiked.set(post.isLikedByCurrentUser);
        this.renderMarkdown(post.content);
        this.isLoading.set(false);
      },
      error: (error: HttpErrorResponse) => {
        console.error('Failed to load post:', error);
        this.errorMessage.set(
          'Failed to load post. It may have been deleted or you may not have permission to view it.'
        );
        this.isLoading.set(false);
      },
    });
  }

  loadComments(postId: number, page: number = 0) {
    this.commentsLoading.set(true);

    this.commentService.getComments(postId, page).subscribe({
      next: (response: PageResponse<CommentResponse>) => {
        this.comments.set(response.content);
        this.commentsLoading.set(false);
      },
      error: (error: HttpErrorResponse) => {
        console.error('Failed to load comments:', error);
        this.commentsLoading.set(false);
      },
    });
  }

  async renderMarkdown(content: string) {
    try {
      const html = await marked.parse(content);
      this.renderedContent.set(this.sanitizer.sanitize(1, html) || '');
    } catch (error) {
      console.error('Failed to render markdown:', error);
      this.renderedContent.set(content);
    }
  }

  onAuthorClick(event: Event) {
    event.stopPropagation();
    const post = this.post();
    if (post) {
      this.router.navigate(['/profile', post.author.username]);
    }
  }

  onLikeClick() {
    if (!this.post()) return;

    const wasLiked = this.isLiked();
    this.isLiked.set(!wasLiked);
    this.likesCount.update((count) => (wasLiked ? count - 1 : count + 1));

    this.postService.toggleLike(this.post()!.id).subscribe({
      next: (response) => {
        this.isLiked.set(response.liked);
      },
      error: (error) => {
        console.error('Failed to toggle like:', error);
        this.isLiked.set(wasLiked);
        this.likesCount.update((count) => (wasLiked ? count + 1 : count - 1));
      },
    });
  }

  onSubmitComment() {
    const post = this.post();
    const text = this.commentText().trim();

    if (!post || !text || this.isSubmittingComment()) return;

    this.isSubmittingComment.set(true);

    this.commentService.createComment(post.id, { content: text }).subscribe({
      next: (newComment) => {
        this.comments.update((current) => [newComment, ...current]);
        this.commentText.set('');
        this.isSubmittingComment.set(false);
        if (post) {
          this.post.update((p) => (p ? { ...p, commentsCount: p.commentsCount + 1 } : p));
        }
      },
      error: (error) => {
        console.error('Failed to submit comment:', error);
        this.isSubmittingComment.set(false);
      },
    });
  }

  onEditClick() {
    const post = this.post();
    if (post) {
      this.router.navigate(['/edit-post'], { state: { post: post } });
    }
  }

  onDeleteClick() {
    const post = this.post();
    if (!post) return;

    if (confirm('Are you sure you want to delete this post? This action cannot be undone.')) {
      this.postService.deletePost(post.id).subscribe({
        next: () => {
          this.router.navigate(['/home']);
        },
        error: (error) => {
          console.error('Failed to delete post:', error);
          alert('Failed to delete post. Please try again.');
        },
      });
    }
  }

  onReportClick() {
    // TODO
    console.log('Report clicked');
  }

  isPostOwner(): boolean {
    const post = this.post();
    const currentUser = this.authService.currentUser();
    return currentUser !== null && post !== null && currentUser.id === post.author.id;
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

  goBack() {
    this.router.navigate(['/home']);
  }

  retry() {
    const postId = this.route.snapshot.paramMap.get('id');
    if (postId) {
      this.loadPost(Number(postId));
    }
  }
}
