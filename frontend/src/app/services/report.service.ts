import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';
import { CreateReportRequest, ReportResponse } from '@/models/report';

@Injectable({ providedIn: 'root' })
export class ReportService {
  private readonly apiUrl = environment.apiUrl;

  http = inject(HttpClient);

  reportPost(postId: number, request: CreateReportRequest): Observable<ReportResponse> {
    return this.http.post<ReportResponse>(`${this.apiUrl}/reports/posts/${postId}`, request);
  }

  reportUser(userId: number, request: CreateReportRequest): Observable<ReportResponse> {
    return this.http.post<ReportResponse>(`${this.apiUrl}/reports/users/${userId}`, request);
  }
}
