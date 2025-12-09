import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/core/services/auth.service';
import { RegisterRequest } from 'src/app/core/models/requests/register-request.model';
import { AuthResponse } from 'src/app/core/models/responses/auth-response.model';
import { ErrorResponse } from 'src/app/core/models/responses/error-response.model';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {

  form: FormGroup;
  submitted = false;
  serverError: string | null = null;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private authService: AuthService
  ) {
    this.form = this.fb.group({
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, this.passwordValidator]]
    });
  }

  passwordValidator(control: AbstractControl): ValidationErrors | null {
    const value = control.value;
    if (!value) return null;

    const hasMinLength = value.length >= 8;
    const hasUpper = /[A-Z]/.test(value);
    const hasLower = /[a-z]/.test(value);
    const hasDigit = /[0-9]/.test(value);
    const hasSpecial = /[^A-Za-z0-9]/.test(value);

    return hasMinLength && hasUpper && hasLower && hasDigit && hasSpecial
      ? null
      : { passwordInvalid: true };
  }

  goBack() {
    this.router.navigate(['/']);
  }

  submit() {
    this.submitted = true;
    this.serverError = null;

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const payload: RegisterRequest = this.form.value;

    this.authService.register(payload).subscribe({
      next: (response) => {
        // SUCCESS ?
        if ('token' in response) {
          // Stocker le token pour persister la session
          localStorage.setItem('token', response.token);

          // Rediriger l'utilisateur
          this.router.navigate(['/']);
        } else {
          // Cas improbable où success != AuthResponse
          this.serverError = 'Une erreur inattendue est survenue.';
        }
      },
      error: (err) => {
        // Erreur HTTP (ex: 400, 409, 500)
        const apiError: ErrorResponse = err.error;
        this.serverError = apiError?.error || 'Erreur lors de l’inscription.';
      }
    });
  }
}
