import { inject } from '@angular/core';
import { Router, CanActivateFn } from '@angular/router';
import { AuthService } from '@/services/auth.service';

export const adminGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  const currentUser = authService.currentUser();

  if (!currentUser) {
    authService.logout();
    return false;
  }

  if (currentUser.role !== 'ADMIN') {
    router.navigate(['/']);
    return false;
  }

  return true;
};
