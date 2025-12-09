import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/core/services/auth.service';
import { LoginRequest } from 'src/app/core/models/requests/login-request.model';
import { AuthResponse } from 'src/app/core/models/responses/auth-response.model';
import { ErrorResponse } from 'src/app/core/models/responses/error-response.model';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {

  form: FormGroup;
  submitted = false;
  serverError: string | null = null;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private authService: AuthService
  ) {

    this.form = this.fb.group({
      login: ['', Validators.required],     // email OU username
      password: ['', Validators.required]
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

    const payload: LoginRequest = this.form.value;

    this.authService.login(payload).subscribe({
      next: (response) => {
        if ('token' in response) {
          this.authService.saveToken(response.token);
          this.router.navigate(['/feed']);
        } else {
          this.serverError = "Une erreur inattendue est survenue.";
        }
      },
      error: (err) => {
        const apiErr: ErrorResponse = err.error;
        this.serverError = apiErr?.error || "Identifiants incorrects.";
      }
    });
  }
}
