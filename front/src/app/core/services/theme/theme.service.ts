import { Injectable } from '@angular/core';
import { ApiService } from '../api.service';
import { ThemeListResponse } from '../../models/responses/theme/theme-list-response.model';
import { ErrorResponse } from '../../models/responses/error-response.model';
import { Observable, Subscription } from 'rxjs';
import { ThemeResponse } from '../../models/responses/theme/theme-response.model';
import { SuccessResponse } from '../../models/responses/success-response.model';
import { SubscriptionRequest } from '../../models/requests/theme/subscription-request.model';

/**
 * ThemeService handles theme-related operations such as fetching themes and managing subscriptions.
 * 
 * @extends ApiService
 * @providedIn root
 * 
 * @author Thorekt
 */
@Injectable({
  providedIn: 'root'
})
export class ThemeService extends ApiService {

  private prefix = '/theme';

  private secondPrefix = '/subscription';

  /**
   * Retrieves a list of all available themes.
   * 
   * @returns Observable<ThemeListResponse | ErrorResponse> containing the list of themes or an error response.
   */
  getAllThemes(): Observable<ThemeListResponse | ErrorResponse> {
    return this.http.get<ThemeListResponse | ErrorResponse>(`${this.baseUrl}${this.prefix}/`);
  }

  /**
   * Retrieves a specific theme by its UUID.
   * 
   * @param themeUuid string representing the UUID of the theme to be fetched.
   * @returns Observable<ThemeResponse | ErrorResponse> containing the theme details or an error response.
   */
  getThemeByUuid(themeUuid: string): Observable<ThemeResponse | ErrorResponse> {
    return this.http.get<ThemeResponse | ErrorResponse>(`${this.baseUrl}${this.prefix}/${themeUuid}`);
  }

  /**
   * Retrieves a list of themes to which the user is subscribed.
   * 
   * @returns Observable<ThemeListResponse | ErrorResponse> containing the list of subscribed themes or an error response.
   */
  getSubscribedThemes(): Observable<ThemeListResponse | ErrorResponse> {
    return this.http.get<ThemeListResponse | ErrorResponse>(`${this.baseUrl}${this.prefix}${this.secondPrefix}/`);
  }

  /**
   * Subscribes the user to a specific theme.
   * 
   * @param data SubscriptionRequest containing the subscription details.
   * @returns Observable<SuccessResponse | ErrorResponse> indicating the success or failure of the subscription operation.
   */
  subscribeToTheme(data: SubscriptionRequest): Observable<SuccessResponse | ErrorResponse> {
    return this.http.post<SuccessResponse | ErrorResponse>(`${this.baseUrl}${this.prefix}${this.secondPrefix}/`, data);
  }

  /**
   * Unsubscribes the user from a specific theme.
   * 
   * @param data SubscriptionRequest containing the unsubscription details.
   * @returns Observable<SuccessResponse | ErrorResponse> indicating the success or failure of the unsubscription operation.
   */
  unsubscribeFromTheme(data: SubscriptionRequest): Observable<SuccessResponse | ErrorResponse> {
    return this.http.delete<SuccessResponse | ErrorResponse>(`${this.baseUrl}${this.prefix}${this.secondPrefix}/`, { body: data });
  }
}
