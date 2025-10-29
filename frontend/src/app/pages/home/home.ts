import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PostService } from '@/services/post.service';
import { AuthService } from '@/services/auth.service';
import { PostResponse } from '@/models/post';
import { PostCard } from '@/components/post-card/post-card';
import { ProfileInfoCard } from '@/components/profile-info-card/profile-info-card';
import { SuggestedUsersCard } from '@/components/suggested-users-card/suggested-users-card';
import { LoadingSpinnerComponent } from '@/components/loading-spinner/loading-spinner.component';
import { EmptyStateComponent } from '@/components/empty-state/empty-state.component';
import { ErrorStateComponent } from '@/components/error-state/error-state.component';
import { HttpErrorResponse } from '@angular/common/http';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Page } from '@/models/pagination';
import { InfiniteScrollDirective } from '@/directives/infinite-scroll.directive';

@Component({
  selector: 'app-home',
  imports: [
    CommonModule,
    PostCard,
    ProfileInfoCard,
    SuggestedUsersCard,
    LoadingSpinnerComponent,
    EmptyStateComponent,
    ErrorStateComponent,
    InfiniteScrollDirective,
  ],
  templateUrl: './home.html',
  styleUrl: './home.scss',
})
export class Home implements OnInit {
  postService = inject(PostService);
  authService = inject(AuthService);
  snackBar = inject(MatSnackBar);

  posts = signal<PostResponse[]>([]);
  isLoading = signal(true);
  errorMessage = signal<string>('');

  currentPage = signal(0);
  pageSize = 20;
  hasMorePosts = signal(true);
  isLoadingMore = signal(false);

  ngOnInit() {
    this.loadPosts();
  }

  loadPosts() {
    this.isLoading.set(true);
    this.errorMessage.set('');

    this.postService.getAllPosts(0, this.pageSize).subscribe({
      next: (response: Page<PostResponse>) => {
        this.posts.set(response.content || []);
        this.hasMorePosts.set(!response.last);
        this.currentPage.set(0);
        this.isLoading.set(false);
      },
      error: (error: HttpErrorResponse) => {
        console.error('Failed to load posts:', error);
        this.errorMessage.set('Failed to load posts. Please try again.');
        this.isLoading.set(false);
      },
    });
  }

  loadMorePosts() {
    if (!this.hasMorePosts() || this.isLoadingMore()) return;

    this.isLoadingMore.set(true);
    const nextPage = this.currentPage() + 1;

    this.postService.getAllPosts(nextPage, this.pageSize).subscribe({
      next: (response: Page<PostResponse>) => {
        const newPosts = response.content || [];
        this.posts.update((current) => [...current, ...newPosts]);
        this.hasMorePosts.set(!response.last);
        this.currentPage.set(nextPage);
        this.isLoadingMore.set(false);
      },
      error: (error: HttpErrorResponse) => {
        this.snackBar.open('Failed to load more posts. Please try again.', 'Close', {
          duration: 4000,
          horizontalPosition: 'center',
          verticalPosition: 'bottom',
        });
        this.isLoadingMore.set(false);
      },
    });
  }

  retry() {
    this.loadPosts();
  }
}
