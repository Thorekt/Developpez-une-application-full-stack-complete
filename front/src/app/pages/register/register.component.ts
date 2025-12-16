import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/core/services/user/auth.service';
import { RegisterRequest } from 'src/app/core/models/requests/user/register-request.model';
import { AuthResponse } from 'src/app/core/models/responses/user/auth-response.model';
import { ErrorResponse } from 'src/app/core/models/responses/error-response.model';
import { passwordValidator } from 'src/app/core/validators/password.validator';

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
      password: ['', [Validators.required, passwordValidator]]
    });
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
        if ('token' in response) {
          this.authService.saveToken(response.token);

          this.router.navigate(['/feed']);
        } else {
          this.serverError = 'Une erreur inattendue est survenue.';
        }
      },
      error: (err) => {
        const apiError: ErrorResponse = err.error;
        this.serverError = apiError?.error || 'Erreur lors de l’inscription.';
      }
    });
  }
}
