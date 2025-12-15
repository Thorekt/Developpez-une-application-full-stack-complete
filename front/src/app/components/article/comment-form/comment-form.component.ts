import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CreateCommentRequest } from 'src/app/core/models/requests/article/create-comment-request.model';
import { ErrorResponse } from 'src/app/core/models/responses/error-response.model';
import { SuccessResponse } from 'src/app/core/models/responses/success-response.model';
import { ArticleService } from 'src/app/core/services/article/article.service';

@Component({
  selector: 'app-comment-form',
  templateUrl: './comment-form.component.html',
  styleUrls: ['./comment-form.component.scss']
})
export class CommentFormComponent implements OnInit {
  @Input() articleUuid?: string;

  @Output() commentCreated = new EventEmitter<void>();

  form: FormGroup;

  error: string | null = null;

  submitting: boolean = false;

  constructor(
    private fb: FormBuilder,
    private articleService: ArticleService
  ) {
    this.form = this.fb.group({
      content: ['', Validators.required]
    });
  }

  ngOnInit(): void {
  }

  submit(): void {
    this.submitting = true;
    this.error = null;
    if (!this.articleUuid) {
      this.error = 'Invalid article UUID.';
      return;
    }
    const data: CreateCommentRequest = {
      articleUuid: this.articleUuid,
      content: this.form.value.content
    };

    this.articleService.createComment(data).subscribe({
      next: (response: SuccessResponse | ErrorResponse) => {
        if ('message' in response) {
          this.commentCreated.emit();
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
