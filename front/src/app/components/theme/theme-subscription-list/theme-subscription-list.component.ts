import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { ErrorResponse } from 'src/app/core/models/responses/error-response.model';
import { ThemeListResponse } from 'src/app/core/models/responses/theme/theme-list-response.model';
import { ThemeResponse } from 'src/app/core/models/responses/theme/theme-response.model';
import { ThemeService } from 'src/app/core/services/theme/theme.service';

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
    private themeService: ThemeService
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
      next: (data: ThemeListResponse | ErrorResponse) => {
        if ('themes' in data) {
          this.subscribedThemes = data.themes;
        } else {
          this.loadingError = data.error || 'Failed to load subscribed themes.';
        }
      },
      error: (error: any) => {
        this.loadingError = error.error || 'Failed to load subscribed themes.';
      },
      complete: () => {
        this.loading = false;
      }
    });
  }
}
