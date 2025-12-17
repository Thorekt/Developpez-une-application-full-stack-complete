import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Subscription } from 'rxjs';
import { UpdateRequest } from 'src/app/core/models/requests/user/update-request.model';
import { ErrorResponse } from 'src/app/core/models/responses/error-response.model';
import { SuccessResponse } from 'src/app/core/models/responses/success-response.model';
import { UserResponse } from 'src/app/core/models/responses/user/user-response.model';
import { AuthService } from 'src/app/core/services/user/auth.service';
import { UserService } from 'src/app/core/services/user/user.service';
import { passwordValidator } from 'src/app/core/validators/password.validator';

/**
 * UserProfileComponent allows users to view and update their profile information.
 * 
 * @component UserProfileComponent
 * @implements OnInit
 * @description This component displays the current user's profile and provides a form to update user information.
 * 
 * @author Thorekt
 */
@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.scss']
})
export class UserProfileComponent implements OnInit, OnDestroy {
  user?: UserResponse;

  loading: boolean = false;
  loadingError?: string;

  submitted: boolean = false;
  submitError?: string;

  form: FormGroup;

  authServiceSubscription?: Subscription;
  userServiceSubscription?: Subscription;

  /**
   * Constructor for UserProfileComponent.
   * 
   * @param authService AuthService for authentication operations.
   * @param userService UserService for user-related operations.
   * @param fb FormBuilder for creating the profile update form.
   */
  constructor(
    private authService: AuthService,
    private userService: UserService,
    private fb: FormBuilder
  ) {
    this.form = this.fb.group({
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, passwordValidator]]
    });
  }

  /**
   * Initializes the component and fetches the current user's information.
   */
  ngOnInit(): void {
    this.fetchUser();
  }

  /**
   * Cleans up resources when the component is destroyed.
   */
  ngOnDestroy(): void {
    this.authServiceSubscription?.unsubscribe();
    this.userServiceSubscription?.unsubscribe();
  }

  /**
   * Fetches the current user's information from the AuthService.
   */
  fetchUser() {
    this.loading = true;
    this.loadingError = undefined;
    this.authServiceSubscription = this.authService.me().subscribe({
      next: (data: UserResponse | ErrorResponse) => {
        if ('username' in data) {
          this.user = data;
          this.prefillForm();
          this.loadingError = undefined;
        } else {
          this.loadingError = data.error || 'An error occurred while fetching the user profile.';
        }
        this.loading = false;
      },
      error: (err: any) => {
        this.loadingError = 'Failed to load user profile.';
        this.loading = false;
      }
    });
  }

  /**
   * Submits the profile update form.
   */
  submit(): void {
    this.submitted = true;
    this.submitError = undefined;

    if (this.form.invalid) {
      this.submitError = 'Please correct the errors in the form.';
      this.submitted = false;
      return;
    }

    const data: UpdateRequest = {
      username: this.form.value.username,
      email: this.form.value.email,
      password: this.form.value.password
    };

    this.userServiceSubscription = this.userService.updateUser(data).subscribe({
      next: (response: SuccessResponse | ErrorResponse) => {
        if ('message' in response) {
          this.submitError = undefined;
          this.form.reset();
          this.fetchUser();
        } else {
          this.submitError = response.error || 'An error occurred while updating the profile.';
        }
      },
      error: (err: any) => {
        this.submitError = 'Failed to update profile.';
      },
      complete: () => {
        this.submitted = false;
      }
    });
  }

  prefillForm(): void {
    if (this.user) {
      this.form.patchValue({
        username: this.user.username,
        email: this.user.email,
        password: ''
      });
    }
  }
}
