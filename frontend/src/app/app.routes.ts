import { Routes } from '@angular/router';
import { guestGuard } from '@/guards/guest-guard';

export const routes: Routes = [
  {
    path: '',
    canActivate: [guestGuard],
    loadComponent: () => import('./pages/login/login').then(m => m.Login)
  },
  {
    path: '**',
    redirectTo: '/home'
  }
];