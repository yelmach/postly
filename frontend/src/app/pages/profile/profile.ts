import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTabsModule } from '@angular/material/tabs';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { AuthService } from '@/services/auth.service';
import { UserService } from '@/services/user.service';
import { User } from '@/models/user';
import { HttpErrorResponse } from '@angular/common/http';
import { EditProfileDialog } from '@/components/edit-profile-dialog/edit-profile-dialog';
import { UserCardComponent } from '@/components/user-card/user-card.component';
import { LoadingSpinnerComponent } from '@/components/loading-spinner/loading-spinner.component';
import { ErrorStateComponent } from '@/components/error-state/error-state.component';
import { EmptyStateComponent } from '@/components/empty-state/empty-state.component';

@Component({
  selector: 'app-profile',
  imports: [
    CommonModule,
    MatButtonModule,
    MatIconModule,
    MatTabsModule,
    MatDialogModule,
    MatProgressSpinnerModule,
    UserCardComponent,
    LoadingSpinnerComponent,
    ErrorStateComponent,
    EmptyStateComponent,
  ],
  templateUrl: './profile.html',
  styleUrl: './profile.scss',
})
export class Profile implements OnInit {
  authService = inject(AuthService);
  userService = inject(UserService);
  route = inject(ActivatedRoute);
  router = inject(Router);
  dialog = inject(MatDialog);

  user = signal<User | null>(null);
  isMyProfile = signal(false);
  isSubscribed = signal(false);
  isLoading = signal(true);
  errorMessage = signal<string>('');

  selectedTabIndex = signal(0);

  postsCount = signal(0);
  subscribersCount = signal(0);
  subscribedCount = signal(0);

  posts = signal<[]>([]);
  subscribers = signal<User[]>([]);
  subscriptions = signal<User[]>([]);

  subscribersLoading = signal(false);
  subscriptionsLoading = signal(false);

  ngOnInit() {
    this.route.params.subscribe((params) => {
      const username = params['username'];
      this.loadUserProfile(username);
    });
  }

  loadUserProfile(username: string) {
    this.isLoading.set(true);
    this.errorMessage.set('');

    // Reset tab data when navigating to a different profile
    this.selectedTabIndex.set(0);
    this.subscribers.set([]);
    this.subscriptions.set([]);
    this.posts.set([]);

    const currentUser = this.authService.currentUser();

    if (!username || (currentUser && currentUser.username === username)) {
      this.loadMyProfile();
    } else {
      this.isMyProfile.set(false);

      this.userService.getUserProfile(username).subscribe({
        next: (user: User) => {
          this.user.set(user);
          this.postsCount.set(user.postsCount || 0);
          this.subscribersCount.set(user.subscribersCount || 0);
          this.subscribedCount.set(user.subscribedCount || 0);
          this.isSubscribed.set(user.isSubscribed || false);
          this.isLoading.set(false);
        },
        error: (error: HttpErrorResponse) => {
          this.isLoading.set(false);
          if (error.status === 404) {
            this.errorMessage.set('User not found');
          } else {
            this.errorMessage.set('Failed to load user profile');
          }
        },
      });
    }
  }

  loadMyProfile() {
    this.isLoading.set(true);
    const currentUser = this.authService.currentUser();

    if (currentUser) {
      this.user.set(currentUser);
      this.isMyProfile.set(true);

      this.postsCount.set(currentUser.postsCount || 0);
      this.subscribersCount.set(currentUser.subscribersCount || 0);
      this.subscribedCount.set(currentUser.subscribedCount || 0);
      this.isLoading.set(false);
    } else {
      this.router.navigate(['/login']);
    }
  }

  toggleSubscribe() {
    const profileUser = this.user();
    if (!profileUser) return;

    const wasSubscribed = this.isSubscribed();

    if (wasSubscribed) {
      // Unsubscribe
      this.userService.unsubscribe(profileUser.id).subscribe({
        next: () => {
          this.isSubscribed.set(false);
          this.subscribersCount.update((v) => Math.max(0, v - 1));
          this.authService.getCurrentUser().subscribe();
        },
        error: (error: HttpErrorResponse) => {
          console.error('Failed to unsubscribe:', error);
          this.errorMessage.set('Failed to unsubscribe user. Please try again.');
        },
      });
    } else {
      // Subscribe
      this.userService.subscribe(profileUser.id).subscribe({
        next: () => {
          this.isSubscribed.set(true);
          this.subscribersCount.update((v) => v + 1);
          this.authService.getCurrentUser().subscribe();
        },
        error: (error: HttpErrorResponse) => {
          console.error('Failed to subscribe:', error);
          if (error.status === 400) {
            this.errorMessage.set('You cannot subscribe to yourself.');
          } else if (error.status === 409) {
            this.errorMessage.set('You are already subscribed to this user.');
          } else {
            this.errorMessage.set('Failed to subscribe to user. Please try again.');
          }
        },
      });
    }
  }

  onTabChange(index: number) {
    this.selectedTabIndex.set(index);
    const currentUser = this.user();
    if (!currentUser) return;

    if (index === 1) {
      this.loadSubscribers(currentUser.id);
    } else if (index === 2) {
      this.loadSubscriptions(currentUser.id);
    }
  }

  loadSubscribers(userId: number) {
    this.subscribersLoading.set(true);
    this.userService.getSubscribers(userId).subscribe({
      next: (users: User[]) => {
        this.subscribers.set(users);
        this.subscribersLoading.set(false);
      },
      error: (error: HttpErrorResponse) => {
        console.error('Failed to load subscribers:', error);
        this.subscribersLoading.set(false);
      },
    });
  }

  loadSubscriptions(userId: number) {
    this.subscriptionsLoading.set(true);
    this.userService.getSubscriptions(userId).subscribe({
      next: (users: User[]) => {
        this.subscriptions.set(users);
        this.subscriptionsLoading.set(false);
      },
      error: (error: HttpErrorResponse) => {
        console.error('Failed to load subscriptions:', error);
        this.subscriptionsLoading.set(false);
      },
    });
  }

  navigateToProfile(username: string) {
    this.router.navigate(['/profile', username]);
  }

  toggleUserSubscription(targetUser: User) {
    const wasSubscribed = targetUser.isSubscribed;

    if (wasSubscribed) {
      this.userService.unsubscribe(targetUser.id).subscribe({
        next: () => {
          this.updateUserInList(targetUser.id, false);
          this.authService.getCurrentUser().subscribe();
        },
        error: (error: HttpErrorResponse) => {
          console.error('Failed to unsubscribe:', error);
        },
      });
    } else {
      this.userService.subscribe(targetUser.id).subscribe({
        next: () => {
          this.updateUserInList(targetUser.id, true);
          this.authService.getCurrentUser().subscribe();
        },
        error: (error: HttpErrorResponse) => {
          console.error('Failed to subscribe:', error);
        },
      });
    }
  }

  updateUserInList(userId: number, isSubscribed: boolean) {
    const updatedSubscribers = this.subscribers().map((user) =>
      user.id === userId ? { ...user, isSubscribed } : user
    );
    this.subscribers.set(updatedSubscribers);

    const updatedSubscriptions = this.subscriptions().map((user) =>
      user.id === userId ? { ...user, isSubscribed } : user
    );
    this.subscriptions.set(updatedSubscriptions);
  }

  openEditProfileDialog() {
    const currentUser = this.user();
    if (!currentUser) return;

    const dialogRef = this.dialog.open(EditProfileDialog, {
      width: '100%',
      maxWidth: '550px',
      height: 'auto',
      data: { user: currentUser },
      disableClose: false,
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result?.success) {
        const username = this.route.snapshot.params['username'];
        this.loadUserProfile(username);
      }
    });
  }
}
