import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from '../services/user/auth.service';

/**
 * AuthGuard ensures that only logged-in users can access certain routes.
 * 
 * @implements CanActivate
 * @providedIn root
 * 
 * @author Thorekt
 */
@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  /**
   * Constructs an instance of AuthGuard.
   * 
   * @param auth AuthService for checking authentication status.
   * @param router Router for navigation.
   */
  constructor(private auth: AuthService, private router: Router) { }

  /**
   * Ensures that only logged-in users can access certain routes.
   * 
   * @returns boolean that indicates whether the route can be activated.
   */
  canActivate(): boolean {
    if (!this.auth.isLoggedIn()) {
      this.router.navigate(['/']);
      return false;
    }
    return true;
  }
}
