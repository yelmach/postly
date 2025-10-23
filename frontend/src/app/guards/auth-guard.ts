import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '@/services/auth.service';
import { catchError, map, of } from 'rxjs';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const token = localStorage.getItem('jwt_token');

  if (!token || authService.isTokenExpired(token)) {
    authService.logout();
    return false;
  }

  return authService.getCurrentUser().pipe(
    map(() => {
      return true;
    }),
    catchError((error) => {
      console.error('Token validation failed:', error);
      authService.logout();
      router.navigate(['/login']);
      return of(false);
    })
  );
};
