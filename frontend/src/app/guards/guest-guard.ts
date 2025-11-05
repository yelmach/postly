import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '@/services/auth.service';
import { catchError, map, of } from 'rxjs';

export const guestGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const token = localStorage.getItem('jwt_token');

  if (!token) {
    return true;
  }

  return authService.getCurrentUser().pipe(
    map(() => {
      router.navigate(['/home']);
      return false;
    }),
    catchError(() => {
      return of(true);
    })
  );
};
