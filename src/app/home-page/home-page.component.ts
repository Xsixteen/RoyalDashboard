import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import * as Highcharts from 'highcharts';
import { NavbarService } from '../navbar.service';

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
   rxNetworkUsage;
   txNetworkUsage;
   currPower;
   Highcharts = Highcharts; // required
   chartConstructor = 'chart'; // optional string, defaults to 'chart'
   chartOptions = { }; // required
   chartCallback = function (chart) {  } // optional function, defaults to null
   updateFlag = false; // optional boolean
   oneToOneFlag = true; // optional boolean, defaults to false
   powerChartOptions;
   tempChartOptions;
   dataLoaded         = false;
   intialNetText      = true;
   networkLoading     = false;

   constructor(private http: HttpClient, public nav : NavbarService) {
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

        this.http.get("api/powerutilization/current").subscribe((data: any) => {
                let powerKW            = data.powerKW;
                this.currPower         = Number(powerKW);
        });

        this.http.get("/api/network/current").subscribe((data: any) => {
                    let rxbps                = data.rxrateBytes;
                    let txbps                = data.txrateBytes;
                    this.rxNetworkUsage      = Math.round(rxbps)/1000;
                    this.txNetworkUsage      = Math.round(txbps)/1000;
                    this.networkLoading      = true;
                    this.intialNetText       = false;
          });

        this.http.get("/api/temphumid/dayhistorical").subscribe((data: any) => {
                    let tempDayArray       = data.dayhistorical;
                    let dayHumid           = [];
                    let dayTemps           = [];
                    let timeArray          = [];

                    tempDayArray.forEach(function(entry) {
                        var tempF = entry.temperature * 9 / 5 + 32;
                        dayTemps.push(Math.round(tempF));
                        dayHumid.push(Math.round(entry.humidity));
                    });

                    tempDayArray.forEach(function(entry) {
                        var date = new Date(entry.epochTime);
                        var hh = date.getHours();
                        var mm = date.getMinutes();
                        timeArray.push(hh + ":" + mm);
                    });


                    this.tempChartOptions =  {
                            title: {
                                text: 'Daily Temperature Chart'
                            },
                            xAxis: {
                                categories: timeArray
                            },
                            yAxis:[{
                                lineWidth: 1,
                                title: {
                                       text: 'Temperature (F)'
                                }
                            }, {
                                lineWidth: 1,
                                opposite: true,
                                title: {
                                    text: 'Humidity (%)'
                                }

                            }],

                            series: [{
                                name: 'Temperature',
                                data: dayTemps
                            }, {
                                name: 'Humidity',
                                data: dayHumid,
                                yAxis: 1
                            }]
                    }


        });


        this.http.get("/api/powerutilization/dayhistorical").subscribe((data: any) => {
                    let powerDayArray      = data.dayhistorical;
                    let powerArray         = [];
                    let timeArray          = [];

                    powerDayArray.forEach(function(entry) {
                        powerArray.push(Number(entry.powerKW));
                        let date = new Date(entry.epochTime);
                        let hh = date.getHours();
                        let mm = date.getMinutes();
                        timeArray.push(hh + ":" + mm);
                    });



                  this.powerChartOptions = {
                        chart: {
                            zoomType: 'x'
                        },
                        title: {
                            text: '24-Hour Home Power Usage'
                        },
                        subtitle: {
                            text: document.ontouchstart === undefined ?
                                    'Click and drag in the plot area to zoom in' : 'Pinch the chart to zoom in'
                        },
                        xAxis: {
                            categories: timeArray
                        },
                        yAxis: {
                            title: {
                                text: 'Power Usage in kW'
                            },
                            min: 0
                        },
                        legend: {
                            enabled: false
                        },
                        plotOptions: {
                            area: {
                                fillColor: {
                                    linearGradient: {
                                        x1: 0,
                                        y1: 0,
                                        x2: 0,
                                        y2: 1
                                    },
                                    stops: [
                                        [0, Highcharts.getOptions().colors[0]],
                                        [1, Highcharts.Color(Highcharts.getOptions().colors[0]).setOpacity(0).get('rgba')]
                                    ]
                                },
                                marker: {
                                    radius: 2
                                },
                                lineWidth: 1,
                                states: {
                                    hover: {
                                        lineWidth: 1
                                    }
                                },
                                threshold: null
                            }
                        },

                        series: [{
                            type: 'area',
                            name: 'Power Usage',
                            data: powerArray
                        }]
                  }

                    this.dataLoaded     = true;
        });

  }

  ngOnInit() {
    this.nav.show();
  }
}