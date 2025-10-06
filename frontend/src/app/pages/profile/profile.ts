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

@Component({
  selector: 'app-profile',
  imports: [
    CommonModule,
    MatButtonModule,
    MatIconModule,
    MatTabsModule,
    MatDialogModule,
    MatProgressSpinnerModule,
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
  subscribers = signal<[]>([]);
  subscribed = signal<[]>([]);

  ngOnInit() {
    this.route.params.subscribe((params) => {
      const username = params['username'];
      this.loadUserProfile(username);
    });
  }

  loadUserProfile(username: string) {
    this.isLoading.set(true);
    this.errorMessage.set('');

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
    this.isSubscribed.update((v) => !v);

    if (this.isSubscribed()) {
      this.subscribersCount.update((v) => v + 1);
    } else {
      this.subscribedCount.update((v) => v - 1);
    }
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
