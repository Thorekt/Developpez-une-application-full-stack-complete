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

  getUserByUuid(userUuid: string): Observable<UserResponse | ErrorResponse> {
    return this.http.get<UserResponse | ErrorResponse>(`${this.baseUrl}${this.prefix}/${userUuid}`);
  }

  updateUser(data: any): Observable<SuccessResponse | ErrorResponse> {
    return this.http.put<SuccessResponse | ErrorResponse>(`${this.baseUrl}${this.prefix}/`, data);
  }
}
