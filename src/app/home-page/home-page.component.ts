import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';



@Component({
  selector: 'app-root',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css']
})

export class HomeComponent {
   humidity;
   timestamp;
   temperatureC;
   temperatureF;

  constructor(private http: HttpClient) {
    this.http.get("/api/temphumid/current").subscribe((data: any) => {

                let tempCFull          = data.tempC;
                let tempC              = tempCFull.toFixed(3);
                let tempF              = tempC * 9 / 5 + 32;
                let d                  = new Date(0);
                let utcSeconds         = data.timestamp;
                d.setUTCSeconds((utcSeconds/1000));

                this.humidity        = data.rhPct.toFixed(2);
                this.timestamp       = d.toString();
                this.temperatureC    = tempC;
                this.temperatureF    = tempF.toFixed(2);

       });
  }

}