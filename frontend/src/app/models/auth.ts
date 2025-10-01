export interface RegisterRequest {
    firstName: string,
    lastName: string,
    username: string,
    email: string,
    password: string,
    bio?: string,
}

export interface LoginRequest {
    credentials: string,
    password: string,
}

export interface AuthResponse {
    token: string
}

export interface User {
    firstName: string,
    lastName: string,
    username: string,
    email: string,
    role: 'USER' | 'ADMIN',
    bio: string,
    profileUrl: string,
}

export interface ApiError {
    status: number;
    error: string;
    message: string;
    details?: any;
}