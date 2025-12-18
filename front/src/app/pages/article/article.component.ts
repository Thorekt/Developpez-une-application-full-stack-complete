import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { ArticleResponse } from 'src/app/core/models/responses/article/article-response.model';
import { ErrorResponse } from 'src/app/core/models/responses/error-response.model';
import { ThemeResponse } from 'src/app/core/models/responses/theme/theme-response.model';
import { UserResponse } from 'src/app/core/models/responses/user/user-response.model';
import { ArticleService } from 'src/app/core/services/article/article.service';
import { ThemeService } from 'src/app/core/services/theme/theme.service';
import { UserService } from 'src/app/core/services/user/user.service';

/**
 * Component for displaying an article along with its associated user and theme information.
 * @component ArticleComponent
 * @implements OnInit, OnDestroy
 * @description This component fetches and displays an article based on the UUID provided in the route parameters.
 * It also retrieves the associated user and theme details for the article.
 * 
 * @author Thorekt
 */
@Component({
  selector: 'app-article',
  templateUrl: './article.component.html',
  styleUrls: ['./article.component.scss']
})
export class ArticleComponent implements OnInit, OnDestroy {
  articleUuid?: string;
  article?: ArticleResponse;
  loadingArticle: boolean = true;
  articleServiceSubscription?: Subscription;

  user?: UserResponse;
  loadingUser: boolean = true;
  userServiceSubscription?: Subscription;

  theme?: ThemeResponse;
  loadingTheme: boolean = true;
  themeServiceSubscription?: Subscription;

  error?: string;

  /**
   * Constructor for ArticleComponent.
   * 
   * @param articleService 
   * @param themeService 
   * @param userService 
   * @param route 
   */
  constructor(
    private articleService: ArticleService,
    private themeService: ThemeService,
    private userService: UserService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  /**
   * Initializes the component by fetching the article based on the UUID from the route parameters.
   */
  ngOnInit(): void {
    this.articleUuid = this.route.snapshot.paramMap.get('uuid') ?? undefined;
    if (this.articleUuid) {
      this.fetchArticle();
    } else {
      this.error = 'Invalid article UUID.';
    }
  }

  /**
   * Cleans up subscriptions when the component is destroyed.
   */
  ngOnDestroy(): void {
    this.userServiceSubscription?.unsubscribe();
    this.articleServiceSubscription?.unsubscribe();
    this.themeServiceSubscription?.unsubscribe();
  }

  /**
   * Fetches the article using the ArticleService.
   */
  fetchArticle(): void {
    this.loadingArticle = true;
    this.articleServiceSubscription = this.articleService.getArticleByUuid(this.articleUuid!)
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

  /**
   * Fetches the user associated with the article using the UserService.
   */
  fetchUser() {
    this.loadingUser = true;
    this.userServiceSubscription = this.userService.getUserByUuid(this.article!.userUuid)
      .subscribe({
        next: (data: UserResponse | ErrorResponse) => {
          if ('uuid' in data) {
            this.user = data as UserResponse;
          } else {
            this.error += (data as ErrorResponse).error || 'An error occurred while fetching the user.';
          }
          this.loadingUser = false;
        },
        error: (err: any) => {
          this.error += err ? ' Failed to load user.' : '';
          this.loadingUser = false;
        }
      });
  }

  /**
   * Fetches the theme associated with the article using the ThemeService.
   */
  fetchTheme() {
    this.loadingTheme = true;
    this.themeServiceSubscription = this.themeService.getThemeByUuid(this.article!.themeUuid)
      .subscribe({
        next: (data: ThemeResponse | ErrorResponse) => {
          if ('uuid' in data) {
            this.theme = data;
          } else {
            this.error += (data as ErrorResponse).error || 'An error occurred while fetching the theme.';
          }
          this.loadingTheme = false;
        },
        error: (err: any) => {
          this.error += err ? ' Failed to load theme.' : '';
          this.loadingTheme = false;
        }
      });

  }

  /**
   * Navigates back to the home page.
   */
  goBack() {
    this.router.navigate(['/']);
  }

}
