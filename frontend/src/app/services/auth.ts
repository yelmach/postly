import { AuthResponse, RegisterRequest, User } from "@/models/auth";
import { HttpClient, HttpErrorResponse } from "@angular/common/http";
import { inject, Injectable, signal } from "@angular/core";
import { Router } from "@angular/router";
import { environment } from "@environments/environment";
import { catchError, Observable, tap, throwError } from "rxjs";

@Injectable({ providedIn: "root" })
export class AuthService {
    private readonly apiUrl = environment.apiUrl;

    public currentUser = signal<User | null>(null);

    http = inject(HttpClient);
    router = inject(Router);

    constructor() {

    }

    register(registerData: RegisterRequest): Observable<AuthResponse> {
        return this.http.post<AuthResponse>(`${this.apiUrl}/auth/register`, registerData).pipe(
            tap(this.handleAuthSuccess),
            catchError(this.handleAuthError)
        );
    }

    initializeAuthService() {

    }

    handleAuthSuccess(response: AuthResponse) {

    }

    handleAuthError(error: HttpErrorResponse): Observable<never> {
        return throwError(() => "err");
    }
}