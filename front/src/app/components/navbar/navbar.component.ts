import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/core/services/user/auth.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent {

  isLogged = false;

  constructor(private authService: AuthService, private router: Router) { }

  ngOnInit() {
    this.isLogged = this.authService.isLoggedIn();
  }

  navigateToFeed() {
    this.router.navigate(['/feed']);
  }

  navigateToThemes() {
    this.router.navigate(['/theme']);
  }

  navigateToProfile() {
    this.router.navigate(['/profile']);
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
