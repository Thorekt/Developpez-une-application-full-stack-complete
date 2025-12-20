import { HttpErrorResponse } from '@angular/common/http';
import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { SubscriptionRequest } from 'src/app/core/models/requests/theme/subscription-request.model';
import { ErrorResponse } from 'src/app/core/models/responses/error-response.model';
import { SuccessResponse } from 'src/app/core/models/responses/success-response.model';
import { ThemeResponse } from 'src/app/core/models/responses/theme/theme-response.model';
import { ThemeService } from 'src/app/core/services/theme/theme.service';
import { ErrorProcessor } from 'src/app/core/utils/error-processor';

/**
 * ThemeCardComponent represents a card displaying theme information and allows users to subscribe to themes.
 * 
 * @implements OnInit
 * 
 * @author Thorekt
 */
@Component({
  selector: 'app-theme-card',
  templateUrl: './theme-card.component.html',
  styleUrls: ['./theme-card.component.scss']
})
export class ThemeCardComponent implements OnInit, OnDestroy {
  @Input() theme!: ThemeResponse;
  @Input() isSubscribed: boolean = false;
  @Input() canUnsubscribe: boolean = false;

  error?: string;
  success?: string;
  posting?: boolean = false;

  themeServiceSubscription?: Subscription;

  /**
   * Constructs an instance of ThemeCardComponent.
   * 
   * @param themeService ThemeService for theme-related operations.
   */
  constructor(private themeService: ThemeService,
    private errorProcessor: ErrorProcessor
  ) { }

  /**
   * Initializes the component.
   */
  ngOnInit(): void {
  }

  /**
   * Cleans up resources when the component is destroyed.
   */
  ngOnDestroy(): void {
    this.themeServiceSubscription?.unsubscribe();
  }

  /**
   * Subscribes the user to the theme represented by this card.
   */
  subscribe(): void {
    this.isSubscribed = true;

    this.postSubscribeAction();
  }

  /**
   * Handles the action of subscribing to a theme.
   */
  postSubscribeAction(): void {
    if (this.posting) {
      return;
    }

    this.posting = true;

    const data: SubscriptionRequest = {
      themeUuid: this.theme.uuid
    };

    this.themeServiceSubscription = this.themeService.subscribeToTheme(data).subscribe({
      next: (response: SuccessResponse | ErrorResponse) => {
        if ('message' in response) {
          this.success = response.message;
          this.error = undefined;
        } else {
          this.error = this.errorProcessor.processError(response.error || '');
          this.isSubscribed = false;
          this.success = undefined;
        }
      },
      error: (err: HttpErrorResponse) => {
        const apiError: ErrorResponse = err.error;
        this.isSubscribed = false;
        this.error = this.errorProcessor.processError(apiError.error || '');
      },
      complete: () => {
        this.posting = false;
      }
    });

  }

  /**
   * Unsubscribes the user from the theme represented by this card.
   */
  unsubscribe(): void {
    this.isSubscribed = false;

    this.postUnsubscribeAction();
  }

  /**
   * Handles the action of unsubscribing from a theme.
   */
  postUnsubscribeAction(): void {
    if (this.posting) {
      return;
    }

    this.posting = true;

    const data: SubscriptionRequest = {
      themeUuid: this.theme.uuid
    };

    this.themeServiceSubscription = this.themeService.unsubscribeFromTheme(data).subscribe({
      next: (response: SuccessResponse | ErrorResponse) => {
        if ('message' in response) {
          this.success = response.message;
          this.error = undefined;
        } else {
          this.error = this.errorProcessor.processError(response.error || '');
          this.isSubscribed = true;
          this.success = undefined;
        }
      },
      error: (err: HttpErrorResponse) => {
        const apiError: ErrorResponse = err.error;
        this.isSubscribed = true;
        this.error = this.errorProcessor.processError(apiError.error || '');
      },
      complete: () => {
        this.posting = false;
      }
    });
  }
}
