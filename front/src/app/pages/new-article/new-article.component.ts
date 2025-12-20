import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { CreateArticleRequest } from 'src/app/core/models/requests/article/create-article-request.model';
import { ErrorResponse } from 'src/app/core/models/responses/error-response.model';
import { SuccessResponse } from 'src/app/core/models/responses/success-response.model';
import { ThemeListResponse } from 'src/app/core/models/responses/theme/theme-list-response.model';
import { ThemeResponse } from 'src/app/core/models/responses/theme/theme-response.model';
import { ArticleService } from 'src/app/core/services/article/article.service';
import { ThemeService } from 'src/app/core/services/theme/theme.service';
import { ErrorProcessor } from 'src/app/core/utils/error-processor';

/**
 * NewArticle component that allows users to create a new article.
 * 
 * @component NewArticleComponent
 * @implements OnInit, OnDestroy
 * @description This component provides a form for users to create and submit a new article.
 * 
 * @author Thorekt
 */
@Component({
  selector: 'app-new-article',
  templateUrl: './new-article.component.html',
  styleUrls: ['./new-article.component.scss']
})
export class NewArticleComponent implements OnInit, OnDestroy {
  loading: boolean = false;
  loadingError?: string;

  themes: ThemeResponse[] = [];

  form: FormGroup;
  submitted = false;
  submitError?: string;

  themeServiceSubscription?: Subscription;
  articleServiceSubscription?: Subscription;

  /**
   * Constructor for NewArticleComponent.
   * 
   * @param articleService 
   * @param themeService 
   * @param router 
   * @param fb 
   */
  constructor(
    private articleService: ArticleService,
    private themeService: ThemeService,
    private router: Router,
    private fb: FormBuilder,
    private errorProcessor: ErrorProcessor
  ) {
    this.form = this.fb.group({
      themeUuid: ['', Validators.required],
      title: ['', Validators.required],
      content: ['', Validators.required]
    });
  }

  /**
   * Initializes the component and fetches available themes.
   */
  ngOnInit(): void {
    this.fetchThemes();
  }

  /**
   * Cleans up subscriptions when the component is destroyed.
   */
  ngOnDestroy(): void {
    this.themeServiceSubscription?.unsubscribe();
    this.articleServiceSubscription?.unsubscribe();
  }

  /**
   * Fetches available themes from the ThemeService.
   */
  fetchThemes(): void {
    this.loading = true;
    this.themeServiceSubscription = this.themeService.getAllThemes().subscribe({
      next: (response: ThemeListResponse | ErrorResponse) => {
        if ('themes' in response) {
          this.themes = response.themes;
          this.loadingError = undefined;
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

  /**
   * Submits the new article form and handles article creation.
   */
  submitArticle(): void {
    this.submitted = true;
    this.submitError = undefined;

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const payload: CreateArticleRequest = this.form.value;

    this.articleServiceSubscription = this.articleService.createArticle(payload).subscribe({
      next: (response: SuccessResponse | ErrorResponse) => {
        if ('message' in response) {
          this.submitError = undefined;
          this.navigateToFeed();

        } else {
          this.submitError = this.errorProcessor.processError(response.error || '');
        }
      },
      error: (err: HttpErrorResponse) => {
        const apiError: ErrorResponse = err.error;
        this.submitError = this.errorProcessor.processError(apiError.error || '');
      },
      complete: () => {
        this.submitted = false;
      }
    });
  }

  /**
   * Navigates to the feed page.
   */
  navigateToFeed() {
    this.router.navigate(['/feed']);
  }
}
