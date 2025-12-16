import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { CommentResponse } from 'src/app/core/models/responses/article/comment-response.model';
import { ErrorResponse } from 'src/app/core/models/responses/error-response.model';
import { UserResponse } from 'src/app/core/models/responses/user/user-response.model';
import { UserService } from 'src/app/core/services/user/user.service';

/**
 * CommentRowComponent represents a single comment row in the comment section, displaying comment details and user information.
 * 
 * @implements OnInit, OnDestroy
 * 
 * @author Thorekt
 */
@Component({
  selector: 'app-comment-row',
  templateUrl: './comment-row.component.html',
  styleUrls: ['./comment-row.component.scss']
})
export class CommentRowComponent implements OnInit, OnDestroy {
  @Input() comment?: CommentResponse;

  userName: string = 'Unknown User';
  userLoading: boolean = true;
  userError?: string;

  userServiceSubscription?: Subscription;

  /**
   * Constructs an instance of CommentRowComponent.
   * 
   * @param userService UserService for fetching user information.
   */
  constructor(
    private userService: UserService
  ) { }

  /**
   * Initializes the component and fetches the user name associated with the comment.
   */
  ngOnInit(): void {
    this.fetchUserName();
  }

  /**
   * Cleans up resources when the component is destroyed.
   */
  ngOnDestroy(): void {
    this.userServiceSubscription?.unsubscribe();
  }

  /**
   * Fetches the user name associated with the comment's user UUID.
   */
  fetchUserName(): void {
    if (!this.comment) {
      this.userError = 'Invalid comment data.';
      return;
    }

    this.userLoading = true;
    this.userServiceSubscription = this.userService.getUserByUuid(this.comment.userUuid).subscribe({
      next: (data: UserResponse | ErrorResponse) => {
        if ('username' in data) {
          this.userName = data.username;
          this.userError = undefined;
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
