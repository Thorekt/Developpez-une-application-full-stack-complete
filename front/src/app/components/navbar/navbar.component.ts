import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/core/services/user/auth.service';

/**
 * NavbarComponent represents the navigation bar of the application.
 * 
 * @author Thorekt
 */
@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent {

  isLogged = false;
  isMenuOpen = false;

  /**
   * Constructs an instance of NavbarComponent.
   * 
   * @param authService AuthService for managing authentication state.
   * @param router Router for navigation.
   */
  constructor(private authService: AuthService, private router: Router) { }

  /**
   * Initializes the component.
   * Sets the isLogged property based on the authentication status.
   * 
   */
  ngOnInit() {
    this.isLogged = this.authService.isLoggedIn();
  }

  /**
   * Navigates to the feed page.
   */
  navigateToFeed() {
    this.router.navigate(['/feed']);
  }

  /**
   * Navigates to the themes page.
   */
  navigateToThemes() {
    this.router.navigate(['/theme']);
  }

  /**
   * Navigates to the user's profile page.
   */
  navigateToProfile() {
    this.router.navigate(['/profile']);
  }

  /**
   * Logs out the current user and navigates to the login page.
   */
  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  /**
   * toggles the navigation menu visibility.
   */
  toggleMenu(): void {
    this.isMenuOpen = !this.isMenuOpen;
  }

  /**
   * closes the navigation menu.
   */
  closeMenu(): void {
    this.isMenuOpen = false;
  }
}
