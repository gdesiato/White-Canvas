import { bootstrapApplication } from '@angular/platform-browser';
import { provideRouter, Routes } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { AppComponent } from './app/app.component';
import { UserComponent } from './app/user/user.component';

const routes: Routes = [
  { path: 'users', component: UserComponent },
  { path: '', redirectTo: '/users', pathMatch: 'full' }
];

bootstrapApplication(AppComponent, {
  providers: [
    provideRouter(routes),
    provideHttpClient()  // Ensure HttpClientModule is provided
  ]
}).catch(err => console.error(err));