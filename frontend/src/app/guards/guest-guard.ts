import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '@/services/auth.service';

export const guestGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const token = localStorage.getItem('jwt_token');

  if (!token) {
    return true;
  }

  if (authService.isTokenExpired(token)) {
    authService.logout();
    return true;
  }

  router.navigate(['/home']);
  return false;
};
