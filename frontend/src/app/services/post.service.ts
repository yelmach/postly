import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';
import { PostMediaResponse, PostRequest, PostResponse } from '@/models/post';
import { Page } from '@/models/pagination';

@Injectable({ providedIn: 'root' })
export class PostService {
  private readonly apiUrl = environment.apiUrl;

  http = inject(HttpClient);

  createPost(postRequest: PostRequest): Observable<PostResponse> {
    return this.http.post<PostResponse>(`${this.apiUrl}/posts`, postRequest);
  }

  uploadMedia(file: File): Observable<PostMediaResponse> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<PostMediaResponse>(`${this.apiUrl}/media/upload`, formData);
  }

  getPost(id: number): Observable<PostResponse> {
    return this.http.get<PostResponse>(`${this.apiUrl}/posts/${id}`);
  }

  getAllPosts(page: number = 0, size: number = 20): Observable<Page<PostResponse>> {
    return this.http.get<Page<PostResponse>>(`${this.apiUrl}/posts`, {
      params: { page: page.toString(), size: size.toString() },
    });
  }

  getUserPosts(
    userId: number,
    page: number = 0,
    size: number = 20
  ): Observable<Page<PostResponse>> {
    return this.http.get<Page<PostResponse>>(`${this.apiUrl}/posts/user/${userId}`, {
      params: { page: page.toString(), size: size.toString() },
    });
  }

  updatePost(id: number, postRequest: PostRequest): Observable<PostResponse> {
    return this.http.put<PostResponse>(`${this.apiUrl}/posts/${id}`, postRequest);
  }

  deletePost(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/posts/${id}`);
  }

  toggleLike(postId: number): Observable<{ liked: boolean }> {
    return this.http.post<{ liked: boolean }>(`${this.apiUrl}/posts/${postId}/likes`, {});
  }
}
