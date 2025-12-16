import { Injectable } from '@angular/core';
import { ApiService } from '../api.service';
import { Observable } from 'rxjs';
import { RegisterRequest } from '../../models/requests/user/register-request.model';
import { AuthResponse } from '../../models/responses/user/auth-response.model';
import { LoginRequest } from '../../models/requests/user/login-request.model';
import { UserResponse } from '../../models/responses/user/user-response.model';
import { ErrorResponse } from '../../models/responses/error-response.model';

/**
 * AuthService handles user authentication operations such as registration, login, and session management.
 * 
 * @extends ApiService
 * @providedIn root
 * 
 * @author Thorekt
 */
@Injectable({
  providedIn: 'root'
})
export class AuthService extends ApiService {

  private prefix = '/auth';


  /**
   * Registers a new user with the provided registration data.
   * 
   * @param data RegisterRequest that contains user registration information.
   * @returns Observable<AuthResponse | ErrorResponse> containing the authentication response or an error response.
   */
  register(data: RegisterRequest): Observable<AuthResponse | ErrorResponse> {
    return this.http.post<AuthResponse | ErrorResponse>(`${this.baseUrl}${this.prefix}/register`, data);
  }

  /**
   * Logs in a user with the provided login data.
   * 
   * @param data LoginRequest that contains user login information.
   * @returns Observable<AuthResponse | ErrorResponse> containing the authentication response or an error response.
   */
  login(data: LoginRequest): Observable<AuthResponse | ErrorResponse> {
    return this.http.post<AuthResponse | ErrorResponse>(`${this.baseUrl}${this.prefix}/login`, data);
  }

  /**
   * Fetches the currently authenticated user's information.
   * 
   * @returns Observable<UserResponse> containing the user's information.
   */
  me(): Observable<UserResponse> {
    return this.http.get<UserResponse>(`${this.baseUrl}${this.prefix}/me`);
  }

  /**
   * Checks if a user is currently logged in by verifying the presence of a token in local storage.
   * 
   * @returns boolean indicating whether a user is currently logged in.
   */
  isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
  }

  /**
   * Saves the authentication token to local storage.
   * 
   * @param token string representing the authentication token to be saved.
   */
  saveToken(token: string): void {
    localStorage.setItem('token', token);
  }

  /**
   * Logs out the current user by removing the authentication token from local storage.
   */
  logout(): void {
    localStorage.removeItem('token');
  }
}
