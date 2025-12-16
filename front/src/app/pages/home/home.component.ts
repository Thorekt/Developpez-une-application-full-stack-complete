import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

/**
 * Home component that serves as the landing page of the application.
 * 
 * @component HomeComponent
 * @implements OnInit
 * @description This component displays the home page and handles navigation to login and registration pages.
 * 
 * @author Thorekt
 */
@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit {

  loginExpired: boolean = false;

  /**
   * Constructor for HomeComponent.
   * 
   * @param router 
   * @param route 
   */
  constructor(private router: Router,
    private route: ActivatedRoute
  ) { }

  /**
   * Initializes the component and checks for login expiration query parameter.
   */
  ngOnInit(): void {
    const expired = this.route.snapshot.queryParamMap.get('expired');
    this.loginExpired = expired === 'true';
  }

  /**
   * Navigates to the login page.
   */
  navigateToLoginPage() {
    this.router.navigate(['/login']);
  }

  /**
   * Navigates to the registration page.
   */
  navigateToRegisterPage() {
    this.router.navigate(['/register']);
  }
}
