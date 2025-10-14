import { User } from './user';

export interface CommentRequest {
  content: string;
}

export interface CommentResponse {
  id: number;
  content: string;
  author: User;
  createdAt: string;
}
