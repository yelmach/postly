import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { marked } from 'marked';
import { PostService } from '@/services/post.service';
import { AuthService } from '@/services/auth.service';
import { PostResponse } from '@/models/post';
import { LoadingSpinnerComponent } from '@/components/loading-spinner/loading-spinner.component';
import { ErrorStateComponent } from '@/components/error-state/error-state.component';
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
    LoadingSpinnerComponent,
    ErrorStateComponent,
  ],
  templateUrl: './post-detail.html',
  styleUrl: './post-detail.scss',
})
export class PostDetail implements OnInit {
  private postService = inject(PostService);
  private authService = inject(AuthService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private sanitizer = inject(DomSanitizer);

  post = signal<PostResponse | null>(null);
  isLoading = signal(true);
  errorMessage = signal<string>('');
  renderedContent = signal<SafeHtml>('');

  ngOnInit() {
    const postId = this.route.snapshot.paramMap.get('id');
    if (postId) {
      this.loadPost(Number(postId));
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
    // TODO
    console.log('Like clicked');
  }

  onEditClick() {
    // TODO:
    console.log('Edit clicked');
  }

  onDeleteClick() {
    // TODO
    console.log('Delete clicked');
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
