import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ArticleListResponse } from 'src/app/core/models/responses/article/article-list-response.model';
import { ErrorResponse } from 'src/app/core/models/responses/error-response.model';
import { ThemeListResponse } from 'src/app/core/models/responses/theme/theme-list-response.model';
import { ArticleService } from 'src/app/core/services/article/article.service';
import { ThemeService } from 'src/app/core/services/theme/theme.service';
import { ArticleListByThemeUuidsInOrder } from 'src/app/core/models/requests/theme/article-list-by-theme-uiids-in-order.model';
import { OrderType } from 'src/app/core/type/order.type';
import { Subscription } from 'rxjs';

/**
 * Component for displaying the user's feed based on subscribed themes.
 * @component FeedComponent
 * @implements OnInit, OnDestroy
 * @description This component fetches and displays articles from themes the user is subscribed to,
 * allowing them to toggle the order of articles displayed.
 * 
 * @author Thorekt
 */
@Component({
  selector: 'app-feed',
  templateUrl: './feed.component.html',
  styleUrls: ['./feed.component.scss']
})
export class FeedComponent implements OnInit, OnDestroy {
  curentOrder: OrderType = 'DESC';

  themeUuids: string[] = [];
  articleList?: ArticleListResponse;

  articleServiceSubscription?: Subscription;
  themeServiceSubscription?: Subscription;

  error?: string;

  loading: boolean = false;

  /**
   * Constructor for FeedComponent.
   * 
   * @param router 
   * @param articleService 
   * @param themeService 
   */
  constructor(
    private router: Router,
    private articleService: ArticleService,
    private themeService: ThemeService
  ) { }

  /**
   * Initializes the component by fetching the subscribed themes and their articles.
   */
  ngOnInit(): void {
    this.fetchSubscribedThemes();
  }

  /**
   * Cleans up subscriptions when the component is destroyed.
   */
  ngOnDestroy(): void {
    this.articleServiceSubscription?.unsubscribe();
    this.themeServiceSubscription?.unsubscribe();
  }

  /**
   * Toggles the order of articles between ascending and descending.
   */
  toggleOrder(): void {
    this.curentOrder = this.curentOrder === 'DESC' ? 'ASC' : 'DESC';
  }

  /**
   * Fetches the feed articles based on subscribed theme UUIDs and the current order. 
   */
  fetchFeed(): void {
    if (this.themeUuids.length === 0) {
      this.error = "Commencez par vous abonner à des thèmes pour voir des articles ici.";
      this.loading = false;
      return;
    }

    const payload: ArticleListByThemeUuidsInOrder = {
      themeUuids: this.themeUuids,
      order: this.curentOrder
    };

    this.articleServiceSubscription = this.articleService.getArticlesByThemeUuidsInOrder(payload).subscribe({
      next: (response: ArticleListResponse | ErrorResponse) => {
        if ('articles' in response) {
          this.articleList = response;
          this.error = undefined;
          if (this.articleList.articles.length === 0) {
            this.error = "Aucun article disponible pour le moment dans vos thèmes abonnés.";
          }
        } else {
          this.error = response.error;
        }
        this.loading = false;
      },
      error: (e: ErrorResponse) => {
        this.error = e.error || 'UNEXPECTED_ERROR';
        this.loading = false;
      }
    });

  }

  /**
   * Fetches the themes the user is subscribed to.
   */
  fetchSubscribedThemes(): void {
    this.loading = true;
    this.themeServiceSubscription = this.themeService.getSubscribedThemes().subscribe({
      next: (response: ThemeListResponse | ErrorResponse) => {
        if ('themes' in response) {
          this.themeUuids = response.themes.map(theme => theme.uuid);
          this.fetchFeed();
        } else {
          this.error = response.error;
          this.loading = false;
          return;
        }
      },
      error: (e: ErrorResponse) => {
        this.error = e.error || 'UNEXPECTED_ERROR';
        this.loading = false;
      }
    });
  }

  /**
   * Navigates to the article detail page by providing the article UUID.
   * 
   * @param articleUuid 
   */
  navigateToArticle(articleUuid: string): void {
    this.router.navigate(['/article', articleUuid]);
  }

  /**
   * Navigates to the new article creation page.
   */
  navigateToNewArticle(): void {
    this.router.navigate(['/article/new']);
  }
}
