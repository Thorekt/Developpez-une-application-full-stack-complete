import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ArticleResponse } from 'src/app/core/models/responses/article/article-response.model';
import { ErrorResponse } from 'src/app/core/models/responses/error-response.model';
import { UserResponse } from 'src/app/core/models/responses/user/user-response.model';
import { UserService } from 'src/app/core/services/user/user.service';

@Component({
  selector: 'app-article-card',
  templateUrl: './article-card.component.html',
  styleUrls: ['./article-card.component.scss']
})
export class ArticleCardComponent implements OnInit {
  @Input() article!: ArticleResponse;

  user: UserResponse | null = null;
  loadingUser: boolean = false;
  error: string | null = null;

  constructor(
    private userService: UserService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.fetchUser();
  }

  fetchUser(): void {
    this.loadingUser = true;
    this.error = null;

    this.userService.getUserByUuid(this.article.userUuid).subscribe({
      next: (response: UserResponse | ErrorResponse) => {
        if ('username' in response) {
          const user = response as UserResponse;
          this.user = user;
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

  navigateToArticle(): void {
    this.router.navigate(['/article', this.article.uuid]);
  }
}
