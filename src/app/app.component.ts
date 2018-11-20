import { Component } from '@angular/core';
import { NavbarService } from './navbar.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent {
  stocks = [];
  title = 'Stock Insights';
  data = {};

  constructor(public nav: NavbarService) {
  }

  ngOnInit() {
     this.nav.show();
  }

}