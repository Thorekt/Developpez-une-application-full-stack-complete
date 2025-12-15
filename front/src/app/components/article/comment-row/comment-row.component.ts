import { Component, Input, OnInit } from '@angular/core';
import { CommentResponse } from 'src/app/core/models/responses/article/comment-response.model';
import { ErrorResponse } from 'src/app/core/models/responses/error-response.model';
import { UserResponse } from 'src/app/core/models/responses/user/user-response.model';
import { UserService } from 'src/app/core/services/user/user.service';

@Component({
  selector: 'app-comment-row',
  templateUrl: './comment-row.component.html',
  styleUrls: ['./comment-row.component.scss']
})
export class CommentRowComponent implements OnInit {
  @Input() comment?: CommentResponse;

  userName: string = 'Unknown User';
  userLoading: boolean = true;
  userError: string | null = null;

  constructor(
    private userService: UserService
  ) { }

  ngOnInit(): void {
  }

  fetchUserName(): void {
    if (!this.comment) {
      this.userError = 'Invalid comment data.';
      return;
    }

    this.userLoading = true;
    this.userService.getUserByUuid(this.comment.userUuid).subscribe({
      next: (data: UserResponse | ErrorResponse) => {
        if ('username' in data) {
          this.userName = data.username;
        } else {
          this.userError = 'User not found.';
        }
        this.userLoading = false;
      },
      error: (err: any) => {
        this.userError = 'Failed to load user data.';
        this.userLoading = false;
      }
    });
  }
}
