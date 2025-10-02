// frontend/src/app/interceptors/jwt.interceptor.ts
import { AuthService } from '@/services/auth';
import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';

export const jwtInterceptor: HttpInterceptorFn = (req, next) => {
    const authService = inject(AuthService);
    const token = localStorage.getItem('jwt_token');

    if (token) {
        req = req.clone({
            setHeaders: { Authorization: `Bearer ${token}` }
        });
    }

    return next(req).pipe(
        catchError((error) => {
            if (error.status === 401) {
                authService.logout();
            }
            return throwError(() => error);
        })
    );
};