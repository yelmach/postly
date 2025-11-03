import { HttpClient } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';
import { type AppNotification } from '@/models/notification';
import { Page } from '@/models/pagination';

interface UnreadCountResponse {
  count: number;
}

@Injectable({
  providedIn: 'root',
})
export class NotificationService {
  private readonly apiUrl = environment.apiUrl;
  private http = inject(HttpClient);

  public notifications = signal<AppNotification[]>([]);
  public unreadCount = signal<number>(0);
  public connected = signal<boolean>(false);
  public loading = signal<boolean>(false);
  public hasMore = signal<boolean>(true);

  private currentPage = 0;
  private pageSize = 20;

  private eventSource?: EventSource;
  private reconnectAttempts = 0;
  private readonly maxReconnectAttempts = 10;
  private readonly initialReconnectDelay = 1000; // 1 second
  private readonly maxReconnectDelay = 30000; // 30 seconds
  private reconnectTimeout?: number;

  getNotifications(page: number = 0, size: number = 20): Observable<Page<AppNotification>> {
    return this.http.get<Page<AppNotification>>(`${this.apiUrl}/notifications`, {
      params: { page: page.toString(), size: size.toString() },
    });
  }

  getUnreadCount(): Observable<UnreadCountResponse> {
    return this.http.get<UnreadCountResponse>(`${this.apiUrl}/notifications/unread/count`);
  }

  markAsRead(id: number): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/notifications/${id}/read`, {});
  }

  markAllAsRead(): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/notifications/read-all`, {});
  }

  connectSSE(): void {
    if (this.eventSource) {
      console.log('SSE already connected');
      return;
    }

    const token = localStorage.getItem('jwt_token');
    if (!token) {
      console.error('No JWT token found, cannot connect to SSE');
      return;
    }

    const url = `${this.apiUrl}/notifications/stream?token=${encodeURIComponent(token)}`;

    try {
      this.eventSource = new EventSource(url, { withCredentials: true });

      this.eventSource.addEventListener('connected', (event: MessageEvent) => {
        console.log('SSE connected:', event.data);
        this.connected.set(true);
        this.reconnectAttempts = 0;

        this.loadInitialData();
      });

      this.eventSource.addEventListener('notification', (event: MessageEvent) => {
        try {
          const notification: AppNotification = JSON.parse(event.data);
          this.notifications.update((notifs) => [notification, ...notifs]);
          this.unreadCount.update((count) => count + 1);

          this.showNotification(notification);
        } catch (error) {
          console.error('Failed to parse notification:', error);
        }
      });

      this.eventSource.onerror = (error) => {
        console.error('SSE error:', error);
        this.connected.set(false);
        this.handleReconnect();
      };
    } catch (error) {
      console.error('Failed to create EventSource:', error);
      this.connected.set(false);
      this.handleReconnect();
    }
  }

  disconnectSSE(): void {
    if (this.eventSource) {
      this.eventSource.close();
      this.eventSource = undefined;
      this.connected.set(false);
      console.log('SSE disconnected');
    }

    if (this.reconnectTimeout) {
      clearTimeout(this.reconnectTimeout);
      this.reconnectTimeout = undefined;
    }

    this.reconnectAttempts = 0;
  }

  private handleReconnect(): void {
    if (this.eventSource) {
      this.eventSource.close();
      this.eventSource = undefined;
    }

    if (this.reconnectAttempts >= this.maxReconnectAttempts) {
      console.error('Max reconnection attempts reached. Please refresh the page.');
      return;
    }

    const delay = Math.min(
      this.initialReconnectDelay * Math.pow(2, this.reconnectAttempts),
      this.maxReconnectDelay
    );

    this.reconnectAttempts++;
    console.log(
      `Reconnecting to SSE in ${delay}ms (attempt ${this.reconnectAttempts}/${this.maxReconnectAttempts})`
    );

    this.reconnectTimeout = window.setTimeout(() => {
      this.connectSSE();
    }, delay);
  }

  private loadInitialData(): void {
    this.getUnreadCount().subscribe({
      next: (response) => {
        this.unreadCount.set(response.count);
      },
      error: (error) => {
        console.error('Failed to fetch unread count:', error);
      },
    });
  }

  loadNotifications(): void {
    this.currentPage = 0;
    this.hasMore.set(true);
    this.loading.set(true);

    this.getNotifications(this.currentPage, this.pageSize).subscribe({
      next: (response) => {
        this.notifications.set(response.content);
        this.hasMore.set(!response.last);
        this.loading.set(false);
      },
      error: (error) => {
        console.error('Failed to fetch notifications:', error);
        this.loading.set(false);
      },
    });
  }

  loadMoreNotifications(): void {
    if (this.loading() || !this.hasMore()) {
      return;
    }

    this.loading.set(true);
    this.currentPage++;

    this.getNotifications(this.currentPage, this.pageSize).subscribe({
      next: (response) => {
        this.notifications.update((notifs) => [...notifs, ...response.content]);
        this.hasMore.set(!response.last);
        this.loading.set(false);
      },
      error: (error) => {
        console.error('Failed to fetch more notifications:', error);
        this.loading.set(false);
        this.currentPage--;
      },
    });
  }

  markNotificationAsRead(id: number): void {
    const notification = this.notifications().find((n) => n.id === id);
    const wasUnread = notification && !notification.isRead;

    this.markAsRead(id).subscribe({
      next: () => {
        this.notifications.update((notifs) =>
          notifs.map((n) => (n.id === id ? { ...n, isRead: true } : n))
        );

        if (wasUnread) {
          this.unreadCount.update((count) => Math.max(0, count - 1));
        }
      },
      error: (error) => {
        console.error('Failed to mark notification as read:', error);
      },
    });
  }

  markAllNotificationsAsRead(): void {
    this.markAllAsRead().subscribe({
      next: () => {
        this.notifications.update((notifs) => notifs.map((n) => ({ ...n, isRead: true })));
        this.unreadCount.set(0);
      },
      error: (error) => {
        console.error('Failed to mark all notifications as read:', error);
      },
    });
  }

  private showNotification(notification: AppNotification): void {
    new Notification('Postly', {
      body: notification.message,
      icon: '/favicon.ico',
    });
  }
}
