import { Component, OnInit, signal, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { UserService } from '@/services/user.service';
import { AuthService } from '@/services/auth.service';
import { UserCardComponent } from '@/components/user-card/user-card.component';
import { User } from '@/models/user';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-suggested-users-card',
  imports: [CommonModule, MatCardModule, MatProgressSpinnerModule, UserCardComponent],
  templateUrl: './suggested-users-card.html',
  styleUrl: './suggested-users-card.scss',
})
export class SuggestedUsersCard implements OnInit {
  userService = inject(UserService);
  authService = inject(AuthService);
  router = inject(Router);

  suggestedUsers = signal<User[]>([]);
  isLoading = signal(false);

  ngOnInit() {
    this.loadSuggestedUsers();
  }

  loadSuggestedUsers() {
    this.isLoading.set(true);
    this.userService.getSuggestedUsers(5).subscribe({
      next: (users: User[]) => {
        this.suggestedUsers.set(users);
        this.isLoading.set(false);
      },
      error: (error: HttpErrorResponse) => {
        console.error('Failed to load suggested users:', error);
        this.isLoading.set(false);
      },
    });
  }

  navigateToProfile(username: string) {
    this.router.navigate(['/profile', username]);
  }
}
