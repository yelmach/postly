import { Component, effect, inject, signal } from '@angular/core';
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
import { AuthService } from '@/services/auth.service';
import { Router } from '@angular/router';
import { HeaderSearchComponent } from '@/components/header-search/header-search';

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
    MatTooltipModule,
    HeaderSearchComponent,
  ],
  templateUrl: './layout.html',
  styleUrl: './layout.scss',
})
export class MainLayout {
  authService = inject(AuthService);
  router = inject(Router);

  isDarkMode = signal(false);
  notificationCount = signal(5);

  constructor() {
    const savedTheme = localStorage.getItem('theme');
    if (savedTheme === 'dark') {
      this.isDarkMode.set(true);
      document.documentElement.classList.add('dark-theme');
    }

    effect(() => {
      const darkMode = this.isDarkMode();
      if (darkMode) {
        document.documentElement.classList.add('dark-theme');
        localStorage.setItem('theme', 'dark');
      } else {
        document.documentElement.classList.remove('dark-theme');
        localStorage.setItem('theme', 'light');
      }
    });
  }

  get currentUser() {
    return this.authService.currentUser();
  }

  get isAdmin() {
    return this.currentUser?.role === 'ADMIN';
  }

  toggleTheme() {
    this.isDarkMode.update((v) => !v);
  }

  navigateToProfile() {
    if (this.currentUser?.username) {
      this.router.navigate(['/profile']);
    }
  }

  navigateToAdminPanel() {
    this.router.navigate(['/admin']);
  }

  logout() {
    this.authService.logout();
  }
}
