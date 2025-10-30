import { Component, inject, input, output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { Router } from '@angular/router';
import { type AppNotification } from '@/models/notification';
import { NotificationService } from '@/services/notification.service';
import { getRelativeTime } from '@/utils/date-utils';

@Component({
  selector: 'app-notification-item',
  imports: [CommonModule, MatIconModule],
  templateUrl: './notification-item.html',
  styleUrl: './notification-item.scss',
})
export class NotificationItem {
  private router = inject(Router);
  private notificationService = inject(NotificationService);

  notification = input.required<AppNotification>();

  onClick(): void {
    const notif = this.notification();

    if (!notif.isRead) {
      this.notificationService.markNotificationAsRead(notif.id);
    }

    if (notif.type === 'NEW_POST' && notif.postId) {
      this.router.navigate(['/post', notif.postId]);
    } else if (notif.type === 'NEW_SUBSCRIBER' && notif.senderUsername) {
      this.router.navigate(['/profile', notif.senderUsername]);
    }
  }

  getRelativeTime(): string {
    return getRelativeTime(this.notification().createdAt);
  }

  getIcon(): string {
    const type = this.notification().type;
    return type === 'NEW_SUBSCRIBER' ? 'person_add' : 'article';
  }

  getIconColor(): string {
    const type = this.notification().type;
    return type === 'NEW_SUBSCRIBER' ? 'primary' : 'accent';
  }
}
