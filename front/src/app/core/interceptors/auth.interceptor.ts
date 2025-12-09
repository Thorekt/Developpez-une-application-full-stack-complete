import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

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

    return next.handle(authReq);
  }
}
