import { Component, Input, OnInit } from '@angular/core';
import { CommentListResponse } from 'src/app/core/models/responses/article/comment-list-response.model';
import { CommentResponse } from 'src/app/core/models/responses/article/comment-response.model';
import { ErrorResponse } from 'src/app/core/models/responses/error-response.model';
import { ArticleService } from 'src/app/core/services/article/article.service';

@Component({
  selector: 'app-comment-section',
  templateUrl: './comment-section.component.html',
  styleUrls: ['./comment-section.component.scss']
})
export class CommentSectionComponent implements OnInit {
  @Input() articleUuid?: string;


  comments: CommentResponse[] = [];
  loadingComments: boolean = false;
  error: string | null = null;

  constructor(private articleService: ArticleService) { }

  ngOnInit(): void {
    this.fetchComments();
  }

  fetchComments(): void {
    if (!this.articleUuid) {
      this.error = 'Invalid article UUID.';
      return;
    }

    this.loadingComments = true;
    this.articleService.getCommentFromArticle(this.articleUuid).subscribe({
      next: (response: CommentListResponse | ErrorResponse) => {
        if ('comments' in response) {
          this.comments = response.comments;
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