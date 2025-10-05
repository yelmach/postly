import { AuthResponse, LoginRequest, RegisterRequest, User } from '@/models/auth';
import { HttpClient } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
import { Router } from '@angular/router';
import { environment } from '@environments/environment';
import { Observable, tap } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly apiUrl = environment.apiUrl;

  public currentUser = signal<User | null>(null);

  http = inject(HttpClient);
  router = inject(Router);

  register(registerData: RegisterRequest): Observable<AuthResponse> {
    return this.http
      .post<AuthResponse>(`${this.apiUrl}/auth/register`, registerData)
      .pipe(tap(this.handleAuthSuccess));
  }

  login(loginData: LoginRequest): Observable<AuthResponse> {
    return this.http
      .post<AuthResponse>(`${this.apiUrl}/auth/login`, loginData)
      .pipe(tap(this.handleAuthSuccess));
  }

  logout() {
    localStorage.removeItem('jwt_token');
    this.currentUser.set(null);
    this.router.navigate(['/login']);
  }

  getCurrentUser(): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/auth/me`).pipe(
      tap((user) => {
        console.log('user: ');
        console.log(user);

        this.currentUser.set(user);
      })
    );
  }

  handleAuthSuccess(response: AuthResponse) {
    localStorage.setItem('jwt_token', response.accessToken);
    this.currentUser.set(response.currentUser);
  }

  isTokenExpired(token: string): boolean {
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      const expiryTime = payload.exp * 1000;
      return Date.now() >= expiryTime;
    } catch {
      return true;
    }
  }
}
