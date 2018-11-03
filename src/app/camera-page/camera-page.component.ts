import { Component } from '@angular/core';



@Component({
  selector: 'app-root',
  templateUrl: './camera-page.component.html',
  styleUrls: ['./camera-page.component.css']
})

export class CameraComponent {
  stocks = [];
  title = 'Stock Insights';
  data = {};

  constructor() {
  }

}