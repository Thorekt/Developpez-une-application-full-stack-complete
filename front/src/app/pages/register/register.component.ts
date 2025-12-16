import { Component, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/core/services/user/auth.service';
import { RegisterRequest } from 'src/app/core/models/requests/user/register-request.model';
import { AuthResponse } from 'src/app/core/models/responses/user/auth-response.model';
import { ErrorResponse } from 'src/app/core/models/responses/error-response.model';
import { passwordValidator } from 'src/app/core/validators/password.validator';
import { Subscription } from 'rxjs';

/**
 * Register component that handles user registration. 
 * @component RegisterComponent
 * @description This component provides a registration form for new users to create an account.
 * @implements OnDestroy
 * 
 * @author Thorekt
 */
@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnDestroy {

  form: FormGroup;
  submitted = false;
  serverError?: string;

  authServiceSubscription?: Subscription;

  /**
   * Constructor for RegisterComponent.
   * @param fb 
   * @param router 
   * @param authService 
   */
  constructor(
    private fb: FormBuilder,
    private router: Router,
    private authService: AuthService
  ) {
    this.form = this.fb.group({
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, passwordValidator]]
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
   * Submits the registration form and handles user registration.
   */
  submit() {
    this.submitted = true;
    this.serverError = undefined;

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const payload: RegisterRequest = this.form.value;

    this.authServiceSubscription = this.authService.register(payload).subscribe({
      next: (response: AuthResponse | ErrorResponse) => {
        if ('token' in response) {
          this.authService.saveToken(response.token);
          this.router.navigate(['/feed']);
        } else {
          this.serverError = response.error || 'Erreur lors de l’inscription.';
        }
      },
      error: (err) => {
        const apiError: ErrorResponse = err.error;
        this.serverError = apiError?.error || 'Erreur lors de l’inscription.';
      }
    });
  }
}
