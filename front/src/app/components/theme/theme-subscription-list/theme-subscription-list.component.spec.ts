import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ThemeSubscriptionListComponent } from './theme-subscription-list.component';

describe('ThemeSubscriptionListComponent', () => {
  let component: ThemeSubscriptionListComponent;
  let fixture: ComponentFixture<ThemeSubscriptionListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ThemeSubscriptionListComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ThemeSubscriptionListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
