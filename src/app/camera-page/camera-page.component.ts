import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { NavbarService } from '../navbar.service';



@Component({
  selector: 'app-root',
  templateUrl: './camera-page.component.html',
  styleUrls: ['./camera-page.component.css']
})

export class CameraComponent {
  cameraImg = "/api/camera/last";
  title = 'Stock Insights';
  data = {};


  takePicture() {
          this.http.get("api/camera/snap").subscribe((data: any) => {
                   var random = (new Date()).toString();
                   this.cameraImg = "/api/camera/last?cb=" + random;
            });
  };

  constructor(private http: HttpClient,  public nav : NavbarService) {
  }

  ngOnInit() {
    this.nav.show();
  }
}