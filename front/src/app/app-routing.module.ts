import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { LoginComponent } from './pages/login/login.component';
import { RegisterComponent } from './pages/register/register.component';
import { GuestGuard } from './core/guards/guest.guard';
import { AuthGuard } from './core/guards/auth.guard';
import { FeedComponent } from './pages/feed/feed.component';
import { ThemeListComponent } from './pages/theme-list/theme-list.component';
import { NewArticleComponent } from './pages/new-article/new-article.component';
import { ArticleComponent } from './pages/article/article.component';
import { UserProfileComponent } from './pages/user-profile/user-profile.component';

const routes: Routes = [
  // Routes accessibles uniquement quand NON connecté
  { path: '', component: HomeComponent, canActivate: [GuestGuard] },
  { path: 'login', component: LoginComponent, canActivate: [GuestGuard] },
  { path: 'register', component: RegisterComponent, canActivate: [GuestGuard] },

  // Routes accessibles uniquement quand connecté
  { path: 'feed', component: FeedComponent, canActivate: [AuthGuard] },
  { path: 'theme', component: ThemeListComponent, canActivate: [AuthGuard] },
  { path: 'article/new', component: NewArticleComponent, canActivate: [AuthGuard] },
  { path: 'article/:uuid', component: ArticleComponent, canActivate: [AuthGuard] },
  { path: 'profile', component: UserProfileComponent, canActivate: [AuthGuard] },

  // Fallback
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule { }
