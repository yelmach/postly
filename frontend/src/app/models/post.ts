import { User } from './user';

export enum MediaType {
  IMAGE = 'IMAGE',
  VIDEO = 'VIDEO',
}

export interface PostRequest {
  title: string;
  content: string;
  mediaUrls?: string[];
}

export interface PostResponse {
  id: number;
  title: string;
  content: string;
  author: User;
  createdAt: string;
  updatedAt: string;
}

export interface PostMediaResponse {
  id: number;
  mediaUrl: string;
  mediaType: MediaType;
}
