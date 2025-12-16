import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from '../services/user/auth.service';

/**
 * GuestGuard prevents logged-in users from accessing guest-only routes.
 * 
 * @implements CanActivate
 * @providedIn root
 * 
 * @author Thorekt
 */
@Injectable({
  providedIn: 'root'
})
export class GuestGuard implements CanActivate {

  /**
   * Constructs an instance of GuestGuard.
   * 
   * @param auth AuthService for checking authentication status.
   * @param router Router for navigation.
   */
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
