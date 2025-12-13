import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpErrorResponse } from '@angular/common/http';
import { catchError, Observable, throwError } from 'rxjs';
import { AuthService } from '../services/user/auth.service';
import { Router } from '@angular/router';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(private authService: AuthService, private router: Router) { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    const noAuthEndpoints = [
      '/api/auth/login',
      '/api/auth/register'
    ];

    // Si l'URL contient un endpoint sans token → on laisse passer
    if (noAuthEndpoints.some(e => req.url.includes(e))) {
      return next.handle(req);
    }

    // Sinon → on ajoute le token
    const token = localStorage.getItem('token'); // ou où tu le stockes

    const authReq = req.clone({
      setHeaders: {
        Authorization: token ? `Bearer ${token}` : ''
      }
    });

    return next.handle(authReq).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401) {
          this.authService.logout();
          this.router.navigate(['/?expired=true']);
        }

        return throwError(() => error);
      })
    );
  }
}
