import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

import { environment } from '../../../environments/environment';
import { LoginRequest } from '../models/login-request';
import { LoginResponse } from '../models/login-response';
import { RegisterRequest } from '../models/register-request';


@Injectable({
  providedIn: 'root',
})
export class Auth {

  private http = inject(HttpClient);

  private readonly authUrl = `${environment.apiUrl}/v1/auth`;

  login(request: LoginRequest): Observable<LoginResponse> {
    return this.http
      .post<LoginResponse>(`${this.authUrl}/login`, request)
      .pipe(
        tap(response => {
          localStorage.setItem('accessToken', response.accessToken);
          localStorage.setItem('refreshToken', response.refreshToken);
        })
      );
  }

  register(request: RegisterRequest): Observable<void> {
    return this.http.post<void>(
      `${this.authUrl}/register`,
      request
    );
  }

  logout(): Observable<void> {
    const refreshToken = localStorage.getItem('refreshToken');

    return this.http
      .post<void>(
        `${this.authUrl}/logout`,
        { refreshToken }
      )
      .pipe(
        tap(() => {
          localStorage.removeItem('accessToken');
          localStorage.removeItem('refreshToken');
        })
      );
  }

  getAccessToken(): string | null {
    return localStorage.getItem('accessToken');
  }

  refreshToken(): Observable<LoginResponse> {
  const refreshToken = localStorage.getItem('refreshToken');

  return this.http
    .post<LoginResponse>(
      `${this.authUrl}/refresh`,
      { refreshToken }
    )
    .pipe(
      tap(response => {
        localStorage.setItem(
          'accessToken',
          response.accessToken
        );

        localStorage.setItem(
          'refreshToken',
          response.refreshToken
        );
      })
    );
}

clearTokens(): void {
  localStorage.removeItem('accessToken');
  localStorage.removeItem('refreshToken');
}
}