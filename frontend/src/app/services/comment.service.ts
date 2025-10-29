import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';
import { CommentRequest, CommentResponse } from '@/models/comment';
import { Page } from '@/models/pagination';

@Injectable({ providedIn: 'root' })
export class CommentService {
  private readonly apiUrl = environment.apiUrl;

  http = inject(HttpClient);

  createComment(postId: number, request: CommentRequest): Observable<CommentResponse> {
    return this.http.post<CommentResponse>(`${this.apiUrl}/posts/${postId}/comments`, request);
  }

  getComments(
    postId: number,
    page: number = 0,
    size: number = 20
  ): Observable<Page<CommentResponse>> {
    return this.http.get<Page<CommentResponse>>(`${this.apiUrl}/posts/${postId}/comments`, {
      params: { page: page.toString(), size: size.toString() },
    });
  }

  updateComment(
    postId: number,
    commentId: number,
    request: CommentRequest
  ): Observable<CommentResponse> {
    return this.http.put<CommentResponse>(
      `${this.apiUrl}/posts/${postId}/comments/${commentId}`,
      request
    );
  }

  deleteComment(postId: number, commentId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/posts/${postId}/comments/${commentId}`);
  }
}
