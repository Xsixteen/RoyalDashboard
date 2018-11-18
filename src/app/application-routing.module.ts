import { NgModule }             from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent }        from './home-page/home-page.component';
import { MonthlyComponent }     from './monthly-page/monthly-page.component';
import { CameraComponent }      from './camera-page/camera-page.component';

const applicationRoutes: Routes = [
  { path: '',           component: HomeComponent },
  { path: 'monthly',    component: MonthlyComponent },
  { path: 'camera',     component: CameraComponent }
];

@NgModule({
  imports: [
   RouterModule.forRoot(
        applicationRoutes,
        {
          enableTracing: true, // <-- debugging purposes only
        }
      )
  ],
  exports: [
    RouterModule
  ]
})
export class ApplicationRoutingModule { }