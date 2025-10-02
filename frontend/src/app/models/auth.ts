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
    accessToken: string
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
