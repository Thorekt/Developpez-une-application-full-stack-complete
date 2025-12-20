import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { ErrorResponse } from 'src/app/core/models/responses/error-response.model';
import { ThemeListResponse } from 'src/app/core/models/responses/theme/theme-list-response.model';
import { ThemeResponse } from 'src/app/core/models/responses/theme/theme-response.model';
import { ThemeService } from 'src/app/core/services/theme/theme.service';
import { ErrorProcessor } from 'src/app/core/utils/error-processor';

/**
 * ThemeList component that displays a list of themes.
 * 
 * @component ThemeListComponent
 * @implements OnInit, OnDestroy
 * @description This component fetches and displays a list of themes, along with the user's subscribed themes.
 * 
 * @author Thorekt
 */
@Component({
  selector: 'app-theme-list',
  templateUrl: './theme-list.component.html',
  styleUrls: ['./theme-list.component.scss']
})
export class ThemeListComponent implements OnInit, OnDestroy {
  loading: boolean = false;
  error: string | null = null;

  themes: ThemeResponse[] = [];
  subscribedThemeUuids: string[] = [];

  fetchThemesSubscription?: Subscription;
  fetchSubscribedThemesSubscription?: Subscription;

  /**
   * Constructor for ThemeListComponent.
   * 
   * @param router 
   * @param themeService 
   */
  constructor(private router: Router,
    private themeService: ThemeService,
    private errorProcessor: ErrorProcessor
  ) {
  }

  /**
   * Initializes the component and fetches themes.
   */
  ngOnInit(): void {
    this.fetchThemes();
  }

  /**
   * Cleans up subscriptions when the component is destroyed.
   */
  ngOnDestroy(): void {
    this.fetchThemesSubscription?.unsubscribe();
    this.fetchSubscribedThemesSubscription?.unsubscribe();
  }

  /**
   * Fetches all themes and the user's subscribed themes.
   */
  fetchThemes(): void {
    this.loading = true;
    this.fetchThemesSubscription = this.themeService.getAllThemes().subscribe({
      next: (response: ThemeListResponse | ErrorResponse) => {
        if ('themes' in response) {
          this.themes = response.themes;
          this.error = null;
          this.fetchSubscribedThemes();
        } else {
          this.error = this.errorProcessor.processError(response.error || '');
        }
      },
      error: (err: HttpErrorResponse) => {
        const apiError: ErrorResponse = err.error;
        this.error = this.errorProcessor.processError(apiError.error || '');
      },
      complete: () => {
        this.loading = false;
      }
    });
  }

  /**
   * Fetches the themes the user is subscribed to.
   */
  fetchSubscribedThemes(): void {
    this.fetchSubscribedThemesSubscription = this.themeService.getSubscribedThemes().subscribe({
      next: (response: ThemeListResponse | ErrorResponse) => {
        if ('themes' in response) {
          this.subscribedThemeUuids = response.themes.map(theme => theme.uuid);
          this.error = null;
        } else {
          this.error = this.errorProcessor.processError(response.error || '');
        }
      },
      error: (err: HttpErrorResponse) => {
        const apiError: ErrorResponse = err.error;
        this.error = this.errorProcessor.processError(apiError.error || '');
      },
      complete: () => {
        this.loading = false;
      }
    });
  }
}
