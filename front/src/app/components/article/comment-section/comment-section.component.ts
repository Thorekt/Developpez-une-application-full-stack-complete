import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { CommentListResponse } from 'src/app/core/models/responses/article/comment-list-response.model';
import { CommentResponse } from 'src/app/core/models/responses/article/comment-response.model';
import { ErrorResponse } from 'src/app/core/models/responses/error-response.model';
import { ArticleService } from 'src/app/core/services/article/article.service';

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
  constructor(private articleService: ArticleService) { }

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
          this.error = response.error || 'An error occurred while fetching comments.';
        }
        this.loadingComments = false;
      },
      error: (err) => {
        this.error = 'Failed to load comments.';
        this.loadingComments = false;
      }
    });
  }
}