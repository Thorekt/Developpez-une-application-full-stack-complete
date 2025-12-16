import { Component, OnInit } from '@angular/core';

/**
 * ThemeSubscriptionListComponent represents a list of themes to which the user is subscribed.
 * 
 * @component ThemeSubscriptionListComponent
 * @implements OnInit
 * @description This component displays the user's theme subscriptions.
 * 
 * @author Thorekt
 */
@Component({
  selector: 'app-theme-subscription-list',
  templateUrl: './theme-subscription-list.component.html',
  styleUrls: ['./theme-subscription-list.component.scss']
})
export class ThemeSubscriptionListComponent implements OnInit {

  /**
   * Constructor for ThemeSubscriptionListComponent.
   */
  constructor() { }

  /**
   * Initializes the component.
   */
  ngOnInit(): void {
  }

}
