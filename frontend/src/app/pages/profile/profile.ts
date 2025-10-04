import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTabsModule } from '@angular/material/tabs';
import { MatDialogModule } from '@angular/material/dialog';
import { AuthService } from '@/services/auth';
import { User } from '@/models/auth';

@Component({
  selector: 'app-profile',
  imports: [CommonModule, MatButtonModule, MatIconModule, MatTabsModule, MatDialogModule],
  templateUrl: './profile.html',
  styleUrl: './profile.scss',
})
export class Profile implements OnInit {
  authService = inject(AuthService);
  route = inject(ActivatedRoute);
  router = inject(Router);

  user = signal<User | null>(null);
  isOwnProfile = signal(false);
  isFollowing = signal(false);

  selectedTabIndex = signal(0);

  postsCount = signal(0);
  followersCount = signal(0);
  followingCount = signal(0);

  posts = signal<[]>([]);
  followers = signal<[]>([]);
  following = signal<[]>([]);

  ngOnInit() {
    this.route.params.subscribe((params) => {
      const username = params['username'];
      if (username) {
        this.loadUserProfile(username);
      } else {
        this.loadOwnProfile();
      }
    });
  }

  loadOwnProfile() {
    const currentUser = this.authService.currentUser();
    if (currentUser) {
      this.user.set(currentUser);
      this.isOwnProfile.set(true);
      this.loadUserData();
    }
  }

  loadUserProfile(username: string) {
    const currentUser = this.authService.currentUser();

    // Check if viewing own profile
    if (currentUser?.username === username) {
      this.user.set(currentUser);
      this.isOwnProfile.set(true);
    } else {
      // TODO: Fetch user profile from API
      this.isOwnProfile.set(false);
      // Temporary mock data
      this.user.set({
        firstName: 'John',
        lastName: 'Doe',
        username: username,
        email: 'john@example.com',
        role: 'USER',
        bio: 'This is a sample bio',
        profileUrl: '',
      });
    }

    this.loadUserData();
  }

  loadUserData() {}

  toggleFollow() {
    this.isFollowing.update((v) => !v);

    if (this.isFollowing()) {
      this.followersCount.update((v) => v + 1);
    } else {
      this.followersCount.update((v) => v - 1);
    }
  }
}
