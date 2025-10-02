import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { MatBadgeModule } from '@angular/material/badge';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatDividerModule } from '@angular/material/divider';
import { MatTooltipModule } from '@angular/material/tooltip';
import { AuthService } from '@/services/auth';
import { Router } from '@angular/router';

@Component({
  selector: 'app-main-layout',
  imports: [
    CommonModule,
    RouterOutlet,
    FormsModule,
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    MatMenuModule,
    MatBadgeModule,
    MatFormFieldModule,
    MatInputModule,
    MatSlideToggleModule,
    MatDividerModule,
    MatTooltipModule
  ],
  templateUrl: './layout.html',
  styleUrl: './layout.scss'
})
export class MainLayout {
  authService = inject(AuthService);
  router = inject(Router);

  isDarkMode = signal(false);
  searchQuery = signal('');
  notificationCount = signal(5);

  get currentUser() {
    return this.authService.currentUser();
  }


  toggleTheme() {
    this.isDarkMode.set(!this.isDarkMode());
    document.body.classList.toggle('dark-theme');
  }
}