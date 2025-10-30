export type NotificationType = 'NEW_SUBSCRIBER' | 'NEW_POST';

export interface AppNotification {
  id: number;
  type: NotificationType;
  message: string;
  isRead: boolean;
  createdAt: string;
  senderUsername: string;
  postId?: number;
}
