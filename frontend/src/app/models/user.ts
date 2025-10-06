export interface User {
  id: number;
  firstName: string;
  lastName: string;
  fullName: string;
  username: string;
  email: string;
  role: 'USER' | 'ADMIN';
  bio?: string;
  profileUrl?: string;
  postsCount?: number;
  subscribersCount?: number;
  subscribedCount?: number;
  isSubscribed?: boolean;
}
