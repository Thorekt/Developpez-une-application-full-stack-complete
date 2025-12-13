import { Component, Input, OnInit } from '@angular/core';
import { SubscriptionRequest } from 'src/app/core/models/requests/theme/subscription-request.model';
import { ErrorResponse } from 'src/app/core/models/responses/error-response.model';
import { SuccessResponse } from 'src/app/core/models/responses/success-response.model';
import { ThemeResponse } from 'src/app/core/models/responses/theme/theme-response.model';
import { ThemeService } from 'src/app/core/services/theme/theme.service';

@Component({
  selector: 'app-theme-card',
  templateUrl: './theme-card.component.html',
  styleUrls: ['./theme-card.component.scss']
})
export class ThemeCardComponent implements OnInit {
  @Input() theme!: ThemeResponse;
  @Input() isSubscribed: boolean = false;

  error: string | null = null;
  success: string | null = null;

  constructor(private themeService: ThemeService) { }

  ngOnInit(): void {
  }

  subscribe(): void {
    this.isSubscribed = true;

    this.postSubscribeAction();
  }

  postSubscribeAction(): void {
    const data: SubscriptionRequest = {
      themeUuid: this.theme.uuid
    };

    this.themeService.subscribeToTheme(data).subscribe({
      next: (response: SuccessResponse | ErrorResponse) => {
        if ('message' in response) {
          this.success = response.message;
          this.error = null;
        } else {
          this.error = response.error || "An error occurred while subscribing to the theme.";
          this.isSubscribed = false;
          this.success = null;
        }
      },
      error: (e) => {
        console.error(e);
        this.isSubscribed = false;
        this.error = "Failed to subscribe to the theme. Please try again.";
      }
    });

  }
}
