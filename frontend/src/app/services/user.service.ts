import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '@environments/environment';
import { Observable, tap } from 'rxjs';
import { User } from '@/models/auth';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private readonly apiUrl = environment.apiUrl;

  private http = inject(HttpClient);

  getUserProfile(username: string): Observable<User> {
    return this.http
      .get<User>(`${this.apiUrl}/users/${username}`)
      .pipe(tap((user) => console.log(user)));
  }
}
