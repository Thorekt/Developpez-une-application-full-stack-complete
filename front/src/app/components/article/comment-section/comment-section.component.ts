import { HttpErrorResponse } from '@angular/common/http';
import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { CommentListResponse } from 'src/app/core/models/responses/article/comment-list-response.model';
import { CommentResponse } from 'src/app/core/models/responses/article/comment-response.model';
import { ErrorResponse } from 'src/app/core/models/responses/error-response.model';
import { ArticleService } from 'src/app/core/services/article/article.service';
import { ErrorProcessor } from 'src/app/core/utils/error-processor';

/**
 * CommentSectionComponent represents the comment section of an article, allowing users to view comments.
 * 
 * @implements OnInit, OnDestroy
 * 
 * @author Thorekt
 */
@Component({
  selector: 'app-comment-section',
  templateUrl: './comment-section.component.html',
  styleUrls: ['./comment-section.component.scss']
})
export class CommentSectionComponent implements OnInit, OnDestroy {
  @Input() articleUuid?: string;


  comments: CommentResponse[] = [];
  loadingComments: boolean = false;
  error?: string;

  articleServiceSubscription?: Subscription;

  /**
   * Constructs an instance of CommentSectionComponent.
   * 
   * @param articleService ArticleService for managing article-related operations.
   */
  constructor(private articleService: ArticleService,
    private errorProcessor: ErrorProcessor
  ) { }

  /**
   * Initializes the component and fetches comments for the article.
   */
  ngOnInit(): void {
    this.fetchComments();
  }

  /**
   * Cleans up resources when the component is destroyed.
   */
  ngOnDestroy(): void {
    this.articleServiceSubscription?.unsubscribe();
  }

  /**
   * Fetches comments for the article identified by articleUuid.
   */
  fetchComments(): void {
    if (!this.articleUuid) {
      this.error = 'Invalid article UUID.';
      return;
    }

    this.loadingComments = true;
    this.articleServiceSubscription = this.articleService.getCommentFromArticle(this.articleUuid).subscribe({
      next: (response: CommentListResponse | ErrorResponse) => {
        if ('comments' in response) {
          this.comments = response.comments;
          this.error = undefined;
        } else {
          this.error = this.errorProcessor.processError(response.error || '');
        }
      },
      error: (err: HttpErrorResponse) => {
        const apiError: ErrorResponse = err.error;
        this.error = this.errorProcessor.processError(apiError.error || '');
      },
      complete: () => {
        this.loadingComments = false;
      }
    });
  }
}