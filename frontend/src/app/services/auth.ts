import { AuthResponse, RegisterRequest, User } from "@/models/auth";
import { HttpClient, HttpErrorResponse } from "@angular/common/http";
import { inject, Injectable, signal } from "@angular/core";
import { Router } from "@angular/router";
import { environment } from "@environments/environment";
import { Observable, tap } from "rxjs";

@Injectable({ providedIn: "root" })
export class AuthService {
    private readonly apiUrl = environment.apiUrl;

    public currentUser = signal<User | null>(null);

    http = inject(HttpClient);
    router = inject(Router);

    register(registerData: RegisterRequest): Observable<AuthResponse> {
        return this.http.post<AuthResponse>(`${this.apiUrl}/auth/register`, registerData).pipe(
            tap(this.handleAuthSuccess)
        );
    }

    getCurrentUser(): Observable<User> {
        return this.http.get<User>(`${this.apiUrl}/auth/me`).pipe(
            tap(user => {
                this.currentUser.set(user);
            })
        );
    }

    handleAuthSuccess(response: AuthResponse) {
        localStorage.setItem("jwt_token", response.accessToken);
    }
}