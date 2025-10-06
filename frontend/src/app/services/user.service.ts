import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';
import { User } from '@/models/user';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private readonly apiUrl = environment.apiUrl;
  private http = inject(HttpClient);

  getUserProfile(username: string): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/users/${username}`);
  }

  updateProfile(profileData: any): Observable<User> {
    return this.http.put<User>(`${this.apiUrl}/users`, profileData);
  }

  updateProfilePicture(formData: FormData): Observable<User> {
    return this.http.put<User>(`${this.apiUrl}/users/picture`, formData);
  }

  subscribe(userId: number): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/users/${userId}/subscriptions`, {});
  }

  unsubscribe(userId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/users/${userId}/subscriptions`);
  }

  getSubscribers(userId: number): Observable<User[]> {
    return this.http.get<User[]>(`${this.apiUrl}/users/${userId}/subscribers`);
  }

  getSubscriptions(userId: number): Observable<User[]> {
    return this.http.get<User[]>(`${this.apiUrl}/users/${userId}/subscriptions`);
  }
}
