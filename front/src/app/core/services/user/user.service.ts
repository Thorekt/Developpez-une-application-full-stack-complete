import { Injectable } from '@angular/core';
import { ApiService } from '../api.service';
import { Observable } from 'rxjs';
import { UserResponse } from '../../models/responses/user/user-response.model';
import { ErrorResponse } from '../../models/responses/error-response.model';
import { SuccessResponse } from '../../models/responses/success-response.model';

/**
 * UserService handles user-related operations such as fetching and updating user information.
 * 
 * @extends ApiService
 * @providedIn root
 * 
 * @author Thorekt
 */
@Injectable({
  providedIn: 'root'
})
export class UserService extends ApiService {

  private prefix = '/user';

  /**
   * Fetches user information by UUID.
   * 
   * @param userUuid string representing the UUID of the user to be fetched.
   * @returns Observable<UserResponse | ErrorResponse> containing the user information or an error response.
   */
  getUserByUuid(userUuid: string): Observable<UserResponse | ErrorResponse> {
    return this.http.get<UserResponse | ErrorResponse>(`${this.baseUrl}${this.prefix}/${userUuid}`);
  }

  /**
   * Updates the current user's information.
   * 
   * @param data object containing the updated user information.
   * @returns Observable<SuccessResponse | ErrorResponse> indicating the success or failure of the update operation.
   */
  updateUser(data: any): Observable<SuccessResponse | ErrorResponse> {
    return this.http.put<SuccessResponse | ErrorResponse>(`${this.baseUrl}${this.prefix}/`, data);
  }
}
