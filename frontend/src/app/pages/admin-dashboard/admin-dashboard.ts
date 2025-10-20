import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { MatTabsModule } from '@angular/material/tabs';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { MatChipsModule } from '@angular/material/chips';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { HttpErrorResponse } from '@angular/common/http';

import { AdminService } from '@/services/admin.service';
import { ModerationService } from '@/services/moderation.service';
import { AuthService } from '@/services/auth.service';
import {
  DashboardStatsResponse,
  AdminUser,
  AdminPost,
  UserFilters,
  PostFilters,
  ReportFilters,
} from '@/models/admin';
import { ReportResponse } from '@/models/report';
import { StatsCard } from '@/components/stats-card/stats-card';
import { BanUserDialog } from '@/components/ban-user-dialog/ban-user-dialog';
import { HidePostDialog } from '@/components/hide-post-dialog/hide-post-dialog';
import { ReportDetailsDialog } from '@/components/report-details-dialog/report-details-dialog';
import { UserDetailsDialog } from '@/components/user-details-dialog/user-details-dialog';
import { LoadingSpinnerComponent } from '@/components/loading-spinner/loading-spinner.component';
import { ErrorStateComponent } from '@/components/error-state/error-state.component';
import { EmptyStateComponent } from '@/components/empty-state/empty-state.component';

@Component({
  selector: 'app-admin-dashboard',
  imports: [
    CommonModule,
    FormsModule,
    MatTabsModule,
    MatTableModule,
    MatPaginatorModule,
    MatButtonModule,
    MatIconModule,
    MatMenuModule,
    MatChipsModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatDialogModule,
    MatSnackBarModule,
    StatsCard,
    LoadingSpinnerComponent,
    ErrorStateComponent,
    EmptyStateComponent,
  ],
  templateUrl: './admin-dashboard.html',
  styleUrl: './admin-dashboard.scss',
})
export class AdminDashboard implements OnInit {
  private adminService = inject(AdminService);
  private moderationService = inject(ModerationService);
  authService = inject(AuthService);
  private dialog = inject(MatDialog);
  private snackBar = inject(MatSnackBar);

  // Stats
  stats = signal<DashboardStatsResponse | null>(null);
  statsLoading = signal(false);
  statsError = signal('');

  // Users
  users = signal<AdminUser[]>([]);
  usersLoading = signal(false);
  usersError = signal('');
  usersPage = signal(0);
  usersPageSize = signal(20);
  usersTotalElements = signal(0);
  usersSearchQuery = signal('');
  usersRoleFilter = signal<'USER' | 'ADMIN' | ''>('');
  usersBannedFilter = signal<boolean | ''>('');
  usersDisplayedColumns = ['user', 'email', 'joinedAt', 'role', 'status', 'actions'];

  // Posts
  posts = signal<AdminPost[]>([]);
  postsLoading = signal(false);
  postsError = signal('');
  postsPage = signal(0);
  postsPageSize = signal(20);
  postsTotalElements = signal(0);
  postsSearchQuery = signal('');
  postsHiddenFilter = signal<boolean | ''>('');
  postsDisplayedColumns = ['author', 'title', 'createdAt', 'status', 'actions'];

  // Reports
  reports = signal<ReportResponse[]>([]);
  reportsLoading = signal(false);
  reportsError = signal('');
  reportsPage = signal(0);
  reportsPageSize = signal(20);
  reportsTotalElements = signal(0);
  reportsStatusFilter = signal<'PENDING' | 'RESOLVED' | 'DISMISSED' | ''>('');
  reportsTypeFilter = signal<'user' | 'post' | ''>('');
  reportsDisplayedColumns = ['type', 'reason', 'reportedBy', 'description', 'status', 'actions'];

  ngOnInit() {
    this.loadStats();
    this.loadUsers();
    this.loadPosts();
    this.loadReports();
  }

  // Stats Methods
  loadStats() {
    this.statsLoading.set(true);
    this.statsError.set('');

    this.adminService.getDashboardStats().subscribe({
      next: (data) => {
        this.stats.set(data);
        this.statsLoading.set(false);
      },
      error: (error: HttpErrorResponse) => {
        this.statsError.set('Failed to load statistics');
        this.statsLoading.set(false);
      },
    });
  }

  // Users Methods
  loadUsers() {
    this.usersLoading.set(true);
    this.usersError.set('');

    const filters: UserFilters = {
      page: this.usersPage(),
      size: this.usersPageSize(),
    };

    if (this.usersSearchQuery()) {
      filters.search = this.usersSearchQuery();
    }
    if (this.usersRoleFilter()) {
      filters.role = this.usersRoleFilter() as 'USER' | 'ADMIN';
    }
    if (this.usersBannedFilter() !== '') {
      filters.banned = this.usersBannedFilter() as boolean;
    }

    this.adminService.getAllUsers(filters).subscribe({
      next: (data) => {
        this.users.set(data.content);
        this.usersTotalElements.set(data.totalElements);
        this.usersLoading.set(false);
      },
      error: (error: HttpErrorResponse) => {
        this.usersError.set('Failed to load users');
        this.usersLoading.set(false);
      },
    });
  }

  onUsersPageChange(event: PageEvent) {
    this.usersPage.set(event.pageIndex);
    this.usersPageSize.set(event.pageSize);
    this.loadUsers();
  }

  onUsersSearch() {
    this.usersPage.set(0);
    this.loadUsers();
  }

  onUsersFilterChange() {
    this.usersPage.set(0);
    this.loadUsers();
  }

  onViewUserProfile(username: string) {
    window.open(`/profile/${username}`, '_blank');
  }

  onUserRowClick(user: AdminUser, event: MouseEvent) {
    const target = event.target as HTMLElement;
    if (target.closest('button') || target.closest('.mat-mdc-menu-trigger')) {
      return;
    }

    this.dialog.open(UserDetailsDialog, {
      data: { user },
      width: '700px',
    });
  }

  onBanUser(user: AdminUser, event?: MouseEvent) {
    event?.stopPropagation();
    const dialogRef = this.dialog.open(BanUserDialog, {
      data: { userId: user.id, username: user.username },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result?.success) {
        this.showSuccess('User banned successfully');
        this.loadUsers();
        this.loadStats();
      }
    });
  }

  onUnbanUser(user: AdminUser, event?: MouseEvent) {
    event?.stopPropagation();
    if (confirm(`Are you sure you want to unban @${user.username}?`)) {
      this.moderationService.unbanUser(user.id).subscribe({
        next: () => {
          this.showSuccess('User unbanned successfully');
          this.loadUsers();
          this.loadStats();
        },
        error: (error: HttpErrorResponse) => {
          this.showError('Failed to unban user');
        },
      });
    }
  }

  onDeleteUser(user: AdminUser, event?: MouseEvent) {
    event?.stopPropagation();
    if (
      confirm(`Are you sure you want to delete @${user.username}? This action cannot be undone.`)
    ) {
      this.moderationService.deleteUser(user.id).subscribe({
        next: () => {
          this.showSuccess('User deleted successfully');
          this.loadUsers();
          this.loadStats();
        },
        error: (error: HttpErrorResponse) => {
          this.showError('Failed to delete user');
        },
      });
    }
  }

  onChangeUserRole(user: AdminUser, event?: MouseEvent) {
    event?.stopPropagation();
    if (confirm(`Are you sure you want to make @${user.username} an admin?`)) {
      this.moderationService.changeUserRole(user.id).subscribe({
        next: () => {
          this.showSuccess('User role updated successfully');
          this.loadUsers();
        },
        error: (error: HttpErrorResponse) => {
          this.showError('Failed to update user role');
        },
      });
    }
  }

  // Posts Methods
  loadPosts() {
    this.postsLoading.set(true);
    this.postsError.set('');

    const filters: PostFilters = {
      page: this.postsPage(),
      size: this.postsPageSize(),
    };

    if (this.postsSearchQuery()) {
      filters.search = this.postsSearchQuery();
    }
    if (this.postsHiddenFilter() !== '') {
      filters.hidden = this.postsHiddenFilter() as boolean;
    }

    this.adminService.getAllPosts(filters).subscribe({
      next: (data) => {
        this.posts.set(data.content);
        this.postsTotalElements.set(data.totalElements);
        this.postsLoading.set(false);
      },
      error: (error: HttpErrorResponse) => {
        this.postsError.set('Failed to load posts');
        this.postsLoading.set(false);
      },
    });
  }

  onPostsPageChange(event: PageEvent) {
    this.postsPage.set(event.pageIndex);
    this.postsPageSize.set(event.pageSize);
    this.loadPosts();
  }

  onPostsSearch() {
    this.postsPage.set(0);
    this.loadPosts();
  }

  onPostsFilterChange() {
    this.postsPage.set(0);
    this.loadPosts();
  }

  onViewPost(postId: number) {
    window.open(`/post/${postId}`, '_blank');
  }

  onPostRowClick(post: AdminPost, event: MouseEvent) {
    const target = event.target as HTMLElement;
    if (
      target.closest('button') ||
      target.closest('.mat-mdc-menu-trigger') ||
      target.closest('a')
    ) {
      return;
    }

    window.open(`/post/${post.id}`, '_blank');
  }

  onHidePost(post: AdminPost, event?: MouseEvent) {
    event?.stopPropagation();
    const dialogRef = this.dialog.open(HidePostDialog, {
      data: { postId: post.id, postTitle: post.title },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result?.success) {
        this.showSuccess('Post hidden successfully');
        this.loadPosts();
        this.loadStats();
      }
    });
  }

  onRestorePost(post: AdminPost, event?: MouseEvent) {
    event?.stopPropagation();
    if (confirm(`Are you sure you want to restore the post "${post.title}"?`)) {
      this.moderationService.restorePost(post.id).subscribe({
        next: () => {
          this.showSuccess('Post restored successfully');
          this.loadPosts();
          this.loadStats();
        },
        error: (error: HttpErrorResponse) => {
          this.showError('Failed to restore post');
        },
      });
    }
  }

  onDeletePost(post: AdminPost, event?: MouseEvent) {
    event?.stopPropagation();
    if (
      confirm(
        `Are you sure you want to delete the post "${post.title}"? This action cannot be undone.`
      )
    ) {
      this.moderationService.deletePost(post.id).subscribe({
        next: () => {
          this.showSuccess('Post deleted successfully');
          this.loadPosts();
          this.loadStats();
        },
        error: (error: HttpErrorResponse) => {
          this.showError('Failed to delete post');
        },
      });
    }
  }

  // Reports Methods
  loadReports() {
    this.reportsLoading.set(true);
    this.reportsError.set('');

    const filters: ReportFilters = {
      page: this.reportsPage(),
      size: this.reportsPageSize(),
    };

    if (this.reportsStatusFilter()) {
      filters.status = this.reportsStatusFilter() as 'PENDING' | 'RESOLVED' | 'DISMISSED';
    }
    if (this.reportsTypeFilter()) {
      filters.type = this.reportsTypeFilter() as 'user' | 'post';
    }

    this.adminService.getAllReports(filters).subscribe({
      next: (data) => {
        this.reports.set(data.content);
        this.reportsTotalElements.set(data.totalElements);
        this.reportsLoading.set(false);
      },
      error: (error: HttpErrorResponse) => {
        this.reportsError.set('Failed to load reports');
        this.reportsLoading.set(false);
      },
    });
  }

  onReportsPageChange(event: PageEvent) {
    this.reportsPage.set(event.pageIndex);
    this.reportsPageSize.set(event.pageSize);
    this.loadReports();
  }

  onReportsFilterChange() {
    this.reportsPage.set(0);
    this.loadReports();
  }

  onViewReportDetails(report: ReportResponse) {
    const dialogRef = this.dialog.open(ReportDetailsDialog, {
      data: { report },
      width: '900px',
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result?.success) {
        this.showSuccess(`Report ${result.action} successfully`);
        this.loadReports();
        this.loadStats();
        if (result.moderationAction) {
          this.loadUsers();
          this.loadPosts();
        }
      }
    });
  }

  getReportType(report: ReportResponse): string {
    return report.reportedUser ? 'User' : 'Post';
  }

  getReportedEntityName(report: ReportResponse): string {
    if (report.reportedUser) {
      return `@${report.reportedUser.username}`;
    } else if (report.reportedPost) {
      return report.reportedPost.title;
    }
    return 'Unknown';
  }

  truncateText(text: string | undefined, maxLength: number): string {
    if (!text) return 'N/A';
    return text.length > maxLength ? text.substring(0, maxLength) + '...' : text;
  }

  private showSuccess(message: string) {
    this.snackBar.open(message, 'Close', {
      duration: 3000,
      horizontalPosition: 'end',
      verticalPosition: 'top',
    });
  }

  private showError(message: string) {
    this.snackBar.open(message, 'Close', {
      duration: 5000,
      horizontalPosition: 'end',
      verticalPosition: 'top',
      panelClass: ['error-snackbar'],
    });
  }
}
