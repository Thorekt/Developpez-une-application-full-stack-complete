import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ErrorResponse } from 'src/app/core/models/responses/error-response.model';
import { ThemeListResponse } from 'src/app/core/models/responses/theme/theme-list-response.model';
import { ThemeResponse } from 'src/app/core/models/responses/theme/theme-response.model';
import { ThemeService } from 'src/app/core/services/theme/theme.service';

@Component({
  selector: 'app-theme-list',
  templateUrl: './theme-list.component.html',
  styleUrls: ['./theme-list.component.scss']
})
export class ThemeListComponent implements OnInit {
  loading: boolean = false;
  error: string | null = null;

  themes: ThemeResponse[] = [];
  subscribedThemeUuids: string[] = [];

  constructor(private router: Router,
    private themeService: ThemeService
  ) {
  }

  ngOnInit(): void {
    this.fetchThemes();
  }

  fetchThemes(): void {
    this.loading = true;
    this.themeService.getAllThemes().subscribe({
      next: (response: ThemeListResponse | ErrorResponse) => {
        if ('themes' in response) {
          this.themes = response.themes;
          this.error = null;
          this.fetchSubscribedThemes();
        } else {
          this.error = response.error || 'UNEXPECTED_ERROR';
          this.loading = false;
        }
      },
      error: (e: ErrorResponse) => {
        this.error = e.error || 'UNEXPECTED_ERROR';
        this.loading = false;
      }
    });
  }

  fetchSubscribedThemes(): void {
    this.themeService.getSubscribedThemes().subscribe({
      next: (response: ThemeListResponse | ErrorResponse) => {
        if ('themes' in response) {
          this.subscribedThemeUuids = response.themes.map(theme => theme.uuid);
          this.error = null;
        } else {
          this.error = response.error || 'UNEXPECTED_ERROR';
        }
        this.loading = false;
      },
      error: (e: ErrorResponse) => {
        this.error = e.error || 'UNEXPECTED_ERROR';
        this.loading = false;
      }
    });
  }
}
