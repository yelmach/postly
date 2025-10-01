export interface RegisterRequest {
    firstName: String,
    lastName: String,
    username: String,
    email: String,
    password: String,
    bio?: String,
}

export interface LoginRequest {
    credentials: String,
    password: String,
}

export interface AuthResponse {
    token: String
}

export interface User {
    firstName: String,
    lastName: String,
    username: String,
    email: String,
    role: 'USER' | 'ADMIN',
    bio: String,
    profileUrl: String,
}

export interface ApiError {
    status: number;
    error: string;
    message: string;
    details?: any;
}