import { Component, effect, inject, OnDestroy, OnInit, signal } from '@angular/core';
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
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { AuthService } from '@/services/auth.service';
import { NotificationService } from '@/services/notification.service';
import { Router } from '@angular/router';
import { HeaderSearchComponent } from '@/components/header-search/header-search';
import { NotificationItem } from '@/components/notification-item/notification-item';
import { InfiniteScrollDirective } from '@/directives/infinite-scroll.directive';

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
    MatProgressSpinnerModule,
    HeaderSearchComponent,
    NotificationItem,
    InfiniteScrollDirective,
  ],
  templateUrl: './layout.html',
  styleUrl: './layout.scss',
})
export class MainLayout implements OnInit, OnDestroy {
  authService = inject(AuthService);
  notificationService = inject(NotificationService);
  router = inject(Router);

  isDarkMode = signal(false);
  notificationsLoaded = signal(false);

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

  get notificationCount() {
    return this.notificationService.unreadCount();
  }

  get notifications() {
    return this.notificationService.notifications();
  }

  get isSSEConnected() {
    return this.notificationService.connected();
  }

  ngOnInit() {
    this.notificationService.connectSSE();
  }

  ngOnDestroy() {
    this.notificationService.disconnectSSE();
  }

  onNotificationMenuOpened() {
    if (!this.notificationsLoaded()) {
      this.notificationService.loadNotifications();
      this.notificationsLoaded.set(true);
    }
  }

  markAllAsRead() {
    this.notificationService.markAllNotificationsAsRead();
  }

  refreshNotifications() {
    this.notificationService.loadNotifications();
  }

  loadMoreNotifications() {
    this.notificationService.loadMoreNotifications();
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
