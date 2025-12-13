import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit {

  constructor(private router: Router,
    private route: ActivatedRoute
  ) { }

  loginExpired: boolean = false;

  ngOnInit(): void {
    const expired = this.route.snapshot.queryParamMap.get('expired');
    this.loginExpired = expired === 'true';
  }

  navigateToLoginPage() {
    this.router.navigate(['/login']);
  }

  navigateToRegisterPage() {
    this.router.navigate(['/register']);
  }
}
