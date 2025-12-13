import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ArticleListResponse } from 'src/app/core/models/responses/article/article-list-response.model';
import { ErrorResponse } from 'src/app/core/models/responses/error-response.model';
import { ThemeListResponse } from 'src/app/core/models/responses/theme/theme-list-response.model';
import { ArticleService } from 'src/app/core/services/article/article.service';
import { ThemeService } from 'src/app/core/services/theme/theme.service';
import { ArticleListByThemeUuidsInOrder } from 'src/app/core/models/requests/theme/article-list-by-theme-uiids-in-order.model';
import { OrderType } from 'src/app/core/type/order.type';

@Component({
  selector: 'app-feed',
  templateUrl: './feed.component.html',
  styleUrls: ['./feed.component.scss']
})
export class FeedComponent implements OnInit {
  curentOrder: OrderType = 'DESC';

  themeUuids: string[] = [];
  articleList: ArticleListResponse | null = null;

  error: string | null = null;

  loading: boolean = false;

  constructor(
    private router: Router,
    private articleService: ArticleService,
    private themeService: ThemeService
  ) { }

  ngOnInit(): void {
    this.fetchSubscribedThemes();
  }

  toggleOrder(): void {
    this.curentOrder = this.curentOrder === 'DESC' ? 'ASC' : 'DESC';
  }

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

    this.articleService.getArticlesByThemeUuidsInOrder(payload).subscribe({
      next: (response: ArticleListResponse | ErrorResponse) => {
        if ('articles' in response) {
          this.articleList = response;
          this.error = null;
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

  fetchSubscribedThemes(): void {
    this.loading = true;
    this.themeService.getSubscribedThemes().subscribe({
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

  navigateToArticle(articleUuid: string): void {
    this.router.navigate(['/article', articleUuid]);
  }

  navigateToNewArticle(): void {
    this.router.navigate(['/article/new']);
  }


}
