import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MatTableModule } from '@angular/material/table';
import { HttpClientModule } from '@angular/common/http';
import { AppComponent } from './app.component';
import { HomeComponent } from './home-page/home-page.component';
import { MonthlyComponent } from './monthly-page/monthly-page.component';
import { CameraComponent } from './camera-page/camera-page.component';
import { ApplicationRoutingModule } from './application-routing.module';


@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    MonthlyComponent,
    CameraComponent
  ],
  imports: [
    BrowserModule,
    ApplicationRoutingModule,
    MatTableModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }