import { Component } from '@angular/core';



@Component({
  selector: 'app-root',
  templateUrl: './monthly-page.component.html',
  styleUrls: ['./monthly-page.component.css']
})

export class MonthlyComponent {
  stocks = [];
  title = 'Stock Insights';
  data = {};

  constructor() {
  }

}