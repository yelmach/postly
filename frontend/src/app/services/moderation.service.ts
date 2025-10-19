import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';
import { BanUserRequest, HidePostRequest, ResolveReportRequest } from '@/models/admin';
import { User } from '@/models/user';
import { ReportResponse } from '@/models/report';

@Injectable({ providedIn: 'root' })
export class ModerationService {
  private readonly apiUrl = environment.apiUrl;
  private http = inject(HttpClient);

  banUser(userId: number, request: BanUserRequest): Observable<User> {
    return this.http.post<User>(`${this.apiUrl}/moderation/users/${userId}/ban`, request);
  }

  unbanUser(userId: number): Observable<User> {
    return this.http.delete<User>(`${this.apiUrl}/moderation/users/${userId}/ban`);
  }

  deleteUser(userId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/moderation/users/${userId}`);
  }

  changeUserRole(userId: number): Observable<User> {
    return this.http.patch<User>(`${this.apiUrl}/moderation/users/${userId}/role`, {});
  }

  hidePost(postId: number, request: HidePostRequest): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/moderation/posts/${postId}/hide`, request);
  }

  restorePost(postId: number): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/moderation/posts/${postId}/restore`, {});
  }

  deletePost(postId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/moderation/posts/${postId}`);
  }

  resolveReport(reportId: number, request: ResolveReportRequest): Observable<ReportResponse> {
    return this.http.patch<ReportResponse>(`${this.apiUrl}/moderation/${reportId}`, request);
  }
}
