import { User } from './user';
import { ReportResponse } from './report';

export interface UserStats {
  total: number;
  newLastMonth: number;
}

export interface PostStats {
  total: number;
  newLastMonth: number;
}

export interface ReportStats {
  total: number;
  activePending: number;
}

export interface DashboardStatsResponse {
  userStats: UserStats;
  postStats: PostStats;
  reportStats: ReportStats;
}

export interface AdminUser extends User {
  createdAt: string;
  isBanned: boolean;
  bannedUntil?: string;
  banReason?: string;
  postsCount: number;
  subscribersCount: number;
  subscriptionsCount: number;
  reportsCount: number;
}

export interface AdminPost {
  id: number;
  title: string;
  content: string;
  author: User;
  createdAt: string;
  updatedAt: string;
  isHidden: boolean;
  likesCount: number;
  commentsCount: number;
  reportsCount: number;
}

export interface PaginatedResponse<T> {
  content: T[];
  pageable: {
    pageNumber: number;
    pageSize: number;
    sort: {
      sorted: boolean;
      unsorted: boolean;
      empty: boolean;
    };
    offset: number;
    paged: boolean;
    unpaged: boolean;
  };
  totalPages: number;
  totalElements: number;
  last: boolean;
  first: boolean;
  size: number;
  number: number;
  sort: {
    sorted: boolean;
    unsorted: boolean;
    empty: boolean;
  };
  numberOfElements: number;
  empty: boolean;
}

export enum ModerationAction {
  NO_ACTION = 'NO_ACTION',
  BAN_USER = 'BAN_USER',
  DELETE_USER = 'DELETE_USER',
  HIDE_POST = 'HIDE_POST',
  DELETE_POST = 'DELETE_POST',
}

export interface BanUserRequest {
  reason: string;
  durationDays?: number;
  permanent?: boolean;
}

export interface HidePostRequest {
  reason: string;
}

export interface ResolveReportRequest {
  status: 'RESOLVED' | 'DISMISSED';
  adminNotes?: string;
  action?: ModerationAction;
}

export interface UserFilters {
  page?: number;
  size?: number;
  role?: 'USER' | 'ADMIN';
  banned?: boolean;
  search?: string;
}

export interface PostFilters {
  page?: number;
  size?: number;
  hidden?: boolean;
  search?: string;
}

export interface ReportFilters {
  page?: number;
  size?: number;
  status?: 'PENDING' | 'RESOLVED' | 'DISMISSED';
  type?: 'user' | 'post';
}

export function getModerationActionLabel(action: ModerationAction): string {
  const labels: Record<ModerationAction, string> = {
    [ModerationAction.NO_ACTION]: 'No Action',
    [ModerationAction.BAN_USER]: 'Ban User',
    [ModerationAction.DELETE_USER]: 'Delete User',
    [ModerationAction.HIDE_POST]: 'Hide Post',
    [ModerationAction.DELETE_POST]: 'Delete Post',
  };
  return labels[action];
}

export function getAvailableActionsForReport(report: ReportResponse): ModerationAction[] {
  if (report.reportedUser) {
    return [ModerationAction.NO_ACTION, ModerationAction.BAN_USER, ModerationAction.DELETE_USER];
  } else if (report.reportedPost) {
    return [ModerationAction.NO_ACTION, ModerationAction.HIDE_POST, ModerationAction.DELETE_POST];
  }
  return [ModerationAction.NO_ACTION];
}
