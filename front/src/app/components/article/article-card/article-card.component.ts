import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { ArticleResponse } from 'src/app/core/models/responses/article/article-response.model';
import { ErrorResponse } from 'src/app/core/models/responses/error-response.model';
import { UserResponse } from 'src/app/core/models/responses/user/user-response.model';
import { UserService } from 'src/app/core/services/user/user.service';

/**
 * ArticleCardComponent represents a card view of an article, displaying basic information and allowing navigation to the full article.
 * 
 * @implements OnInit, OnDestroy
 * 
 * @author Thorekt
 */
@Component({
  selector: 'app-article-card',
  templateUrl: './article-card.component.html',
  styleUrls: ['./article-card.component.scss']
})
export class ArticleCardComponent implements OnInit, OnDestroy {
  @Input() article!: ArticleResponse;

  user?: UserResponse;
  loadingUser: boolean = false;
  error?: string;

  userServiceSubscription?: Subscription

  /**
   * Constructs an instance of ArticleCardComponent.
   * 
   * @param userService UserService for fetching user information.
   * @param router Router for navigation.
   */
  constructor(
    private userService: UserService,
    private router: Router
  ) { }

  /**
   * Initializes the component and fetches the user information for the article's author.
   */
  ngOnInit(): void {
    this.fetchUser();
  }

  /**
   * Cleans up resources when the component is destroyed.
   */
  ngOnDestroy(): void {
    this.userServiceSubscription?.unsubscribe();
  }

  /**
   * Fetches the user information for the article's author.
   */
  fetchUser(): void {
    this.loadingUser = true;
    this.error = undefined;

    this.userService.getUserByUuid(this.article.userUuid).subscribe({
      next: (response: UserResponse | ErrorResponse) => {
        if ('username' in response) {
          const user = response as UserResponse;
          this.user = user;
          this.error = undefined;
        } else {
          const errorResponse = response as ErrorResponse;
          this.error = errorResponse.error || 'Unknown error occurred.';
        }
        this.loadingUser = false;
      },
      error: (err: any) => {
        this.error = err || 'Error fetching user data.';
        this.loadingUser = false;
      }
    });
  }

  /**
   * Navigates to the full article view.
   */
  navigateToArticle(): void {
    this.router.navigate(['/article', this.article.uuid]);
  }
}
