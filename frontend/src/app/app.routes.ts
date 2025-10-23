import { Routes } from '@angular/router';
import { guestGuard } from '@/guards/guest-guard';
import { authGuard } from '@/guards/auth-guard';
import { adminGuard } from './guards/admin.guard';

export const routes: Routes = [
  {
    path: '',
    redirectTo: '/home',
    pathMatch: 'full',
  },
  {
    path: 'login',
    canActivate: [guestGuard],
    loadComponent: () => import('./pages/login/login').then((m) => m.Login),
  },
  {
    path: 'register',
    canActivate: [guestGuard],
    loadComponent: () => import('./pages/register/register').then((m) => m.Register),
  },
  {
    path: '',
    canActivate: [authGuard],
    loadComponent: () => import('./components/layout/layout').then((m) => m.MainLayout),
    children: [
      {
        path: 'home',
        canActivate: [authGuard],
        loadComponent: () => import('./pages/home/home').then((m) => m.Home),
      },
      {
        path: 'profile',
        canActivate: [authGuard],
        loadComponent: () => import('./pages/profile/profile').then((m) => m.Profile),
      },
      {
        path: 'profile/:username',
        canActivate: [authGuard],
        loadComponent: () => import('./pages/profile/profile').then((m) => m.Profile),
      },
      {
        path: 'new-post',
        canActivate: [authGuard],
        loadComponent: () => import('./pages/new-post/new-post').then((m) => m.NewPost),
      },
      {
        path: 'post/:id',
        canActivate: [authGuard],
        loadComponent: () => import('./pages/post-detail/post-detail').then((m) => m.PostDetail),
      },
      {
        path: 'edit-post',
        canActivate: [authGuard],
        loadComponent: () => import('./pages/edit-post/edit-post').then((m) => m.EditPost),
      },
      {
        path: 'admin',
        canActivate: [adminGuard],
        loadComponent: () =>
          import('./pages/admin-dashboard/admin-dashboard').then((m) => m.AdminDashboard),
      },
    ],
  },
  {
    path: '**',
    redirectTo: '/home',
  },
];
