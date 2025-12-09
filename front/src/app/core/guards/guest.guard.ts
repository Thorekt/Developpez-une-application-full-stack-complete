import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class GuestGuard implements CanActivate {

  constructor(private auth: AuthService, private router: Router) { }

  /**
   * Redirects logged-in users away from guest-only routes.
   * 
   * @returns boolean that indicates whether the route can be activated.
   */
  canActivate(): boolean {

    if (this.auth.isLoggedIn()) {
      this.router.navigate(['/feed']);
      return false;
    }

    return true;
  }
}
