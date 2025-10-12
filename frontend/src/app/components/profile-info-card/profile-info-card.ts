import { Component, input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { Router } from '@angular/router';
import { User } from '@/models/user';

@Component({
  selector: 'app-profile-info-card',
  imports: [CommonModule, MatCardModule, MatIconModule],
  templateUrl: './profile-info-card.html',
  styleUrl: './profile-info-card.scss',
})
export class ProfileInfoCard {
  user = input.required<User>();

  constructor(private router: Router) {}

  navigateToProfile() {
    const user = this.user();
    if (user) {
      this.router.navigate(['/profile', user.username]);
    }
  }
}
