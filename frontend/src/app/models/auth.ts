import { User } from './user';

export interface RegisterRequest {
  firstName: string;
  lastName: string;
  username: string;
  email: string;
  password: string;
  bio?: string;
}

export interface LoginRequest {
  credentials: string;
  password: string;
}

export interface AuthResponse {
  accessToken: string;
  currentUser: User;
}
