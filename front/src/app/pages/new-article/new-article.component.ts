import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { CreateArticleRequest } from 'src/app/core/models/requests/article/create-article-request.model';
import { ErrorResponse } from 'src/app/core/models/responses/error-response.model';
import { ThemeListResponse } from 'src/app/core/models/responses/theme/theme-list-response.model';
import { ThemeResponse } from 'src/app/core/models/responses/theme/theme-response.model';
import { ArticleService } from 'src/app/core/services/article/article.service';
import { ThemeService } from 'src/app/core/services/theme/theme.service';

@Component({
  selector: 'app-new-article',
  templateUrl: './new-article.component.html',
  styleUrls: ['./new-article.component.scss']
})
export class NewArticleComponent implements OnInit {
  loading: boolean = false;
  loadError: string | null = null;

  themes: ThemeResponse[] = [];



  form: FormGroup;
  submitted = false;
  submitError: string | null = null;

  constructor(
    private articleService: ArticleService,
    private themeService: ThemeService,
    private router: Router,
    private fb: FormBuilder
  ) {
    this.form = this.fb.group({
      themeUuid: ['', Validators.required],
      title: ['', Validators.required],
      content: ['', Validators.required]
    });
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
          this.loadError = null;
        } else {
          this.loadError = response.error || 'UNEXPECTED_ERROR';
        }
        this.loading = false;
      },
      error: (e: ErrorResponse) => {
        this.loadError = e.error || 'UNEXPECTED_ERROR';
        this.loading = false;
      }
    });
  }

  submitArticle(): void {
    this.submitted = true;
    this.submitError = null;

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const payload: CreateArticleRequest = this.form.value;

    this.articleService.createArticle(payload).subscribe({
      next: (response) => {
        this.navigateToFeed();
      },
      error: (err) => {
        const apiError: ErrorResponse = err.error;
        this.submitError = apiError.error || 'UNEXPECTED_ERROR';
      }
    });
  }

  navigateToFeed() {
    this.router.navigate(['/feed']);
  }
}
