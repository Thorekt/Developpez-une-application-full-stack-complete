// Core Angular
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

// Angular Material
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

// Forms
import { ReactiveFormsModule } from '@angular/forms';

// HTTP & Interceptors
import { HttpClientModule } from '@angular/common/http';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { AuthInterceptor } from './core/interceptors/auth.interceptor';

// Routing
import { AppRoutingModule } from './app-routing.module';

// Pages
import { AppComponent } from './app.component';
import { HomeComponent } from './pages/home/home.component';
import { LoginComponent } from './pages/login/login.component';
import { RegisterComponent } from './pages/register/register.component';
import { FeedComponent } from './pages/feed/feed.component';
import { ArticleComponent } from './pages/article/article.component';
import { ThemeListComponent } from './pages/theme-list/theme-list.component';
import { NewArticleComponent } from './pages/new-article/new-article.component';
import { UserProfileComponent } from './pages/user-profile/user-profile.component';

// Components
import { NavbarComponent } from './components/navbar/navbar.component';
import { ArticleCardComponent } from './components/article/article-card/article-card.component';
import { CommentSectionComponent } from './components/article/comment-section/comment-section.component';
import { CommentRowComponent } from './components/article/comment-row/comment-row.component';
import { CommentFormComponent } from './components/article/comment-form/comment-form.component';
import { ThemeCardComponent } from './components/theme/theme-card/theme-card.component';
import { ThemeSubscriptionListComponent } from './components/theme/theme-subscription-list/theme-subscription-list.component';

/**
 * AppModule represents the root module of the Angular application.
 * It declares all components and imports all necessary modules for the application.
 * 
 * @author Thorekt
 */
@NgModule({
  declarations: [AppComponent, HomeComponent, LoginComponent, RegisterComponent, FeedComponent, ArticleComponent, ThemeListComponent, NewArticleComponent, UserProfileComponent, ArticleCardComponent, CommentSectionComponent, CommentRowComponent, CommentFormComponent, ThemeCardComponent, ThemeSubscriptionListComponent, NavbarComponent],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatButtonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    HttpClientModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent],
})
export class AppModule { }
