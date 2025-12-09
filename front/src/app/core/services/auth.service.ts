import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { Observable } from 'rxjs';
import { RegisterRequest } from '../models/requests/register-request.model';
import { AuthResponse } from '../models/responses/auth-response.model';
import { LoginRequest } from '../models/requests/login-request.model';
import { UserResponse } from '../models/responses/user-response.model';
import { ErrorResponse } from '../models/responses/error-response.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService extends ApiService {

  private prefix = '/auth';

  register(data: RegisterRequest): Observable<AuthResponse | ErrorResponse> {
    return this.http.post<AuthResponse | ErrorResponse>(`${this.baseUrl}${this.prefix}/register`, data);
  }

  login(data: LoginRequest): Observable<AuthResponse | ErrorResponse> {
    return this.http.post<AuthResponse | ErrorResponse>(`${this.baseUrl}${this.prefix}/login`, data);
  }

  me(): Observable<UserResponse> {
    return this.http.get<UserResponse>(`${this.baseUrl}${this.prefix}/me`);
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
  }
}
