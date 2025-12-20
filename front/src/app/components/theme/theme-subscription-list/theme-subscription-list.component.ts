import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { ErrorResponse } from 'src/app/core/models/responses/error-response.model';
import { ThemeListResponse } from 'src/app/core/models/responses/theme/theme-list-response.model';
import { ThemeResponse } from 'src/app/core/models/responses/theme/theme-response.model';
import { ThemeService } from 'src/app/core/services/theme/theme.service';
import { ErrorProcessor } from 'src/app/core/utils/error-processor';

/**
 * ThemeSubscriptionListComponent represents a list of themes to which the user is subscribed.
 * 
 * @component ThemeSubscriptionListComponent
 * @implements OnInit, OnDestroy
 * @description This component displays the user's theme subscriptions.
 * 
 * @author Thorekt
 */
@Component({
  selector: 'app-theme-subscription-list',
  templateUrl: './theme-subscription-list.component.html',
  styleUrls: ['./theme-subscription-list.component.scss']
})
export class ThemeSubscriptionListComponent implements OnInit, OnDestroy {
  subscribedThemes: ThemeResponse[] = [];
  loading: boolean = false;
  loadingError?: string;

  themeServiceSubscription?: Subscription;

  /**
   * Constructor for ThemeSubscriptionListComponent.
   */
  constructor(
    private themeService: ThemeService,
    private errorProcessor: ErrorProcessor
  ) { }

  /**
   * Initializes the component.
   */
  ngOnInit(): void {
    this.fetchSubscribedThemes();
  }

  /**
   * Cleans up subscriptions when the component is destroyed.
   */
  ngOnDestroy(): void {
    this.themeServiceSubscription?.unsubscribe();
  }

  /**
   * Fetches the list of subscribed themes.
   */
  fetchSubscribedThemes(): void {
    this.loading = true;
    this.loadingError = undefined;

    this.themeServiceSubscription = this.themeService.getSubscribedThemes().subscribe({
      next: (response: ThemeListResponse | ErrorResponse) => {
        if ('themes' in response) {
          this.subscribedThemes = response.themes;
        } else {
          this.loadingError = this.errorProcessor.processError(response.error || '');
        }
      },
      error: (err: HttpErrorResponse) => {
        const apiError: ErrorResponse = err.error;
        this.loadingError = this.errorProcessor.processError(apiError.error || '');
      },
      complete: () => {
        this.loading = false;
      }
    });
  }
}
