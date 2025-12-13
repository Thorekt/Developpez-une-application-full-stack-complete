import { Injectable } from '@angular/core';
import { ApiService } from '../api.service';
import { ThemeListResponse } from '../../models/responses/theme/theme-list-response.model';
import { ErrorResponse } from '../../models/responses/error-response.model';
import { Observable, Subscription } from 'rxjs';
import { ThemeResponse } from '../../models/responses/theme/theme-response.model';
import { SuccessResponse } from '../../models/responses/success-response.model';
import { SubscriptionRequest } from '../../models/requests/theme/subscription-request.model';

@Injectable({
  providedIn: 'root'
})
export class ThemeService extends ApiService {

  private prefix = '/theme';

  private secondPrefix = '/subscription';

  getAllThemes(): Observable<ThemeListResponse | ErrorResponse> {
    return this.http.get<ThemeListResponse | ErrorResponse>(`${this.baseUrl}${this.prefix}/`);
  }

  getThemeByUuid(themeUuid: string): Observable<ThemeResponse | ErrorResponse> {
    return this.http.get<ThemeResponse | ErrorResponse>(`${this.baseUrl}${this.prefix}/${themeUuid}`);
  }

  getSubscribedThemes(): Observable<ThemeListResponse | ErrorResponse> {
    return this.http.get<ThemeListResponse | ErrorResponse>(`${this.baseUrl}${this.prefix}${this.secondPrefix}/`);
  }

  subscribeToTheme(data: SubscriptionRequest): Observable<SuccessResponse | ErrorResponse> {
    return this.http.post<SuccessResponse | ErrorResponse>(`${this.baseUrl}${this.prefix}${this.secondPrefix}/`, data);
  }

  unsubscribeFromTheme(data: SubscriptionRequest): Observable<SuccessResponse | ErrorResponse> {
    return this.http.delete<SuccessResponse | ErrorResponse>(`${this.baseUrl}${this.prefix}${this.secondPrefix}/`, { body: data });
  }
}
