import { Component, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/core/services/user/auth.service';
import { LoginRequest } from 'src/app/core/models/requests/user/login-request.model';
import { AuthResponse } from 'src/app/core/models/responses/user/auth-response.model';
import { ErrorResponse } from 'src/app/core/models/responses/error-response.model';
import { Subscription } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';
import { ErrorProcessor } from 'src/app/core/utils/error-processor';

/**
 * Login component that handles user authentication.
 * 
 * @component LoginComponent
 * @description This component provides a login form for users to authenticate themselves.
 * @implements OnDestroy
 * 
 * @author Thorekt
 */
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnDestroy {

  form: FormGroup;
  submitted = false;
  error?: string;

  authServiceSubscription?: Subscription;

  /**
   * Constructor for LoginComponent.
   * 
   * @param fb 
   * @param router 
   * @param authService 
   */
  constructor(
    private fb: FormBuilder,
    private router: Router,
    private authService: AuthService,
    private errorProcessor: ErrorProcessor
  ) {

    this.form = this.fb.group({
      login: ['', Validators.required],     // email OU username
      password: ['', Validators.required]
    });
  }

  /**
   * Cleans up subscriptions when the component is destroyed.
   */
  ngOnDestroy(): void {
    this.authServiceSubscription?.unsubscribe();
  }

  /**
   * Navigates back to the home page.
   */
  goBack() {
    this.router.navigate(['/']);
  }

  /**
   * Submits the login form and handles authentication.
   */
  submit() {
    this.submitted = true;
    this.error = undefined;

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const payload: LoginRequest = this.form.value;

    this.authServiceSubscription = this.authService.login(payload).subscribe({
      next: (response: AuthResponse | ErrorResponse) => {
        if ('token' in response) {
          this.authService.saveToken(response.token);
          this.router.navigate(['/feed']);
        } else {
          this.error = this.errorProcessor.processError(response.error || '');
        }
      },
      error: (err: HttpErrorResponse) => {
        const apiError: ErrorResponse = err.error;
        this.error = this.errorProcessor.processError(apiError.error || '');
      },
      complete: () => {
        this.submitted = false;
      }
    });
  }
}
