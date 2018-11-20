import { NgModule }             from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent }        from './home-page/home-page.component';
import { MonthlyComponent }     from './monthly-page/monthly-page.component';
import { CameraComponent }      from './camera-page/camera-page.component';
import { LoginComponent }      from './login-page/login-page.component';

const applicationRoutes: Routes = [
  { path: '',           component: HomeComponent },
  { path: 'monthly',    component: MonthlyComponent },
  { path: 'camera',     component: CameraComponent },
  { path: 'login',      component: LoginComponent }

];

@NgModule({
  imports: [
   RouterModule.forRoot(
        applicationRoutes,
        {
          enableTracing: true, // <-- debugging purposes only
          useHash: true
        }
      )
  ],
  exports: [
    RouterModule
  ]
})
export class ApplicationRoutingModule { }