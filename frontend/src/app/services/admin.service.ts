import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';
import {
  DashboardStatsResponse,
  AdminUser,
  AdminPost,
  PaginatedResponse,
  UserFilters,
  PostFilters,
  ReportFilters,
} from '@/models/admin';
import { ReportResponse } from '@/models/report';

@Injectable({ providedIn: 'root' })
export class AdminService {
  private readonly apiUrl = environment.apiUrl;
  private http = inject(HttpClient);

  getDashboardStats(): Observable<DashboardStatsResponse> {
    return this.http.get<DashboardStatsResponse>(`${this.apiUrl}/admin/dashboard/stats`);
  }

  getAllUsers(filters: UserFilters = {}): Observable<PaginatedResponse<AdminUser>> {
    let params = new HttpParams();

    if (filters.page !== undefined) {
      params = params.set('page', filters.page.toString());
    }
    if (filters.size !== undefined) {
      params = params.set('size', filters.size.toString());
    }
    if (filters.role) {
      params = params.set('role', filters.role);
    }
    if (filters.banned !== undefined) {
      params = params.set('banned', filters.banned.toString());
    }
    if (filters.search) {
      params = params.set('search', filters.search);
    }

    return this.http.get<PaginatedResponse<AdminUser>>(`${this.apiUrl}/admin/users`, {
      params,
    });
  }

  getAllPosts(filters: PostFilters = {}): Observable<PaginatedResponse<AdminPost>> {
    let params = new HttpParams();

    if (filters.page !== undefined) {
      params = params.set('page', filters.page.toString());
    }
    if (filters.size !== undefined) {
      params = params.set('size', filters.size.toString());
    }
    if (filters.hidden !== undefined) {
      params = params.set('hidden', filters.hidden.toString());
    }
    if (filters.search) {
      params = params.set('search', filters.search);
    }

    return this.http.get<PaginatedResponse<AdminPost>>(`${this.apiUrl}/admin/posts`, {
      params,
    });
  }

  getAllReports(filters: ReportFilters = {}): Observable<PaginatedResponse<ReportResponse>> {
    let params = new HttpParams();

    if (filters.page !== undefined) {
      params = params.set('page', filters.page.toString());
    }
    if (filters.size !== undefined) {
      params = params.set('size', filters.size.toString());
    }
    if (filters.status) {
      params = params.set('status', filters.status);
    }
    if (filters.type) {
      params = params.set('type', filters.type);
    }

    return this.http.get<PaginatedResponse<ReportResponse>>(`${this.apiUrl}/admin/reports`, {
      params,
    });
  }

  getReportById(reportId: number): Observable<ReportResponse> {
    return this.http.get<ReportResponse>(`${this.apiUrl}/admin/reports/${reportId}`);
  }
}
