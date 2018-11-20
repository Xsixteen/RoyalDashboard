import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { NavbarService } from '../navbar.service';


@Component({
  selector: 'app-root',
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.css']
})

export class LoginComponent {

  constructor(private http: HttpClient, public nav : NavbarService) {
  }

  ngOnInit() {
    this.nav.hide();
  }
}
