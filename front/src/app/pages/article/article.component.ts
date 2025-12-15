import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ArticleResponse } from 'src/app/core/models/responses/article/article-response.model';
import { ErrorResponse } from 'src/app/core/models/responses/error-response.model';
import { ThemeResponse } from 'src/app/core/models/responses/theme/theme-response.model';
import { UserResponse } from 'src/app/core/models/responses/user/user-response.model';
import { ArticleService } from 'src/app/core/services/article/article.service';
import { ThemeService } from 'src/app/core/services/theme/theme.service';
import { UserService } from 'src/app/core/services/user/user.service';

@Component({
  selector: 'app-article',
  templateUrl: './article.component.html',
  styleUrls: ['./article.component.scss']
})
export class ArticleComponent implements OnInit {
  articleUuid: string | null = null;
  article: ArticleResponse | null = null;
  loadingArticle: boolean = true;

  user: UserResponse | null = null;
  loadingUser: boolean = true;

  theme: ThemeResponse | null = null;
  loadingTheme: boolean = true;

  error: string | null = null;

  constructor(
    private articleService: ArticleService,
    private themeService: ThemeService,
    private userService: UserService,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.articleUuid = this.route.snapshot.paramMap.get('uuid');
    if (this.articleUuid) {
      this.fetchArticle();
    } else {
      this.error = 'Invalid article UUID.';
    }
  }

  fetchArticle(): void {
    this.loadingArticle = true;
    this.articleService.getArticleByUuid(this.articleUuid!)
      .subscribe({
        next: (data: ArticleResponse | ErrorResponse) => {
          if ('uuid' in data) {
            this.article = data as ArticleResponse;
            this.fetchUser();
            this.fetchTheme();
          } else {
            this.error = (data as ErrorResponse).error || 'An error occurred while fetching the article.';
          }
          this.loadingArticle = false;
        },
        error: (err) => {
          this.error = 'Failed to load article.';
          this.loadingArticle = false;
        }
      });
  }

  fetchUser() {
    this.loadingUser = true;
    this.userService.getUserByUuid(this.article!.userUuid)
      .subscribe({
        next: (data: UserResponse | ErrorResponse) => {
          if ('uuid' in data) {
            this.user = data as UserResponse;
          } else {
            this.error = (data as ErrorResponse).error || 'An error occurred while fetching the user.';
          }
          this.loadingUser = false;
        },
        error: (err: any) => {
          this.error += err ? ' Failed to load user.' : '';
          this.loadingUser = false;
        }
      });
  }

  fetchTheme() {
    this.loadingTheme = true;
    this.themeService.getThemeByUuid(this.article!.themeUuid)
      .subscribe({
        next: (data: ThemeResponse | ErrorResponse) => {
          if ('uuid' in data) {
            this.theme = data;
          } else {
            this.error = (data as ErrorResponse).error || 'An error occurred while fetching the theme.';
          }
          this.loadingTheme = false;
        },
        error: (err: any) => {
          this.error += err ? ' Failed to load theme.' : '';
          this.loadingTheme = false;
        }
      });

  }

}
