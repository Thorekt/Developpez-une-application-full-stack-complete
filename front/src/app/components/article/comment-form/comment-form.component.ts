import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Subscription } from 'rxjs';
import { CreateCommentRequest } from 'src/app/core/models/requests/article/create-comment-request.model';
import { ErrorResponse } from 'src/app/core/models/responses/error-response.model';
import { SuccessResponse } from 'src/app/core/models/responses/success-response.model';
import { ArticleService } from 'src/app/core/services/article/article.service';

/**
 * CommentFormComponent represents a form for submitting comments on an article.
 */
@Component({
  selector: 'app-comment-form',
  templateUrl: './comment-form.component.html',
  styleUrls: ['./comment-form.component.scss']
})
export class CommentFormComponent implements OnInit, OnDestroy {
  @Input() articleUuid?: string;

  @Output() commentCreated = new EventEmitter<void>();

  form: FormGroup;

  error?: string;

  submitting: boolean = false;

  articleServiceSubscription?: Subscription;

  /**
   * Constructs an instance of CommentFormComponent.
   * 
   * @param fb form builder for creating the comment form.
   * @param articleService ArticleService for managing article-related operations.
   */
  constructor(
    private fb: FormBuilder,
    private articleService: ArticleService
  ) {
    this.form = this.fb.group({
      content: ['', Validators.required]
    });
  }

  /**
   * Initializes the component.
   */
  ngOnInit(): void {
  }

  /**
   * Cleans up resources when the component is destroyed.
   */
  ngOnDestroy(): void {
    this.articleServiceSubscription?.unsubscribe();
  }

  /**
   * Submits the comment form. 
   */
  submit(): void {
    this.submitting = true;
    this.error = undefined;
    if (!this.articleUuid) {
      this.error = 'Invalid article UUID.';
      return;
    }
    const data: CreateCommentRequest = {
      articleUuid: this.articleUuid,
      content: this.form.value.content
    };

    this.articleServiceSubscription = this.articleService.createComment(data).subscribe({
      next: (response: SuccessResponse | ErrorResponse) => {
        if ('message' in response) {
          this.commentCreated.emit();
          this.error = undefined;
        } else {
          this.error = response.error || 'An error occurred while submitting the comment.';
        }

        this.form.reset();
        this.submitting = false;
      },
      error: (err: any) => {
        this.error = 'Failed to submit comment.';
        this.submitting = false;
      }
    });



  }
}
