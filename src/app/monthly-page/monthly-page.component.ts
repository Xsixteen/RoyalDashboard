import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import * as Highcharts from 'highcharts';


@Component({
  selector: 'app-root',
  templateUrl: './monthly-page.component.html',
  styleUrls: ['./monthly-page.component.css']
})

export class MonthlyComponent {
  timestamp;
  data = {};
  monthlyTempChart;
  monthlyDailyTempChart;
  Highcharts = Highcharts; // required
  chartConstructor = 'chart'; // optional string, defaults to 'chart'
  chartOptions = { }; // required
  chartCallback = function (chart) {  } // optional function, defaults to null
  updateFlag = false; // optional boolean
  oneToOneFlag = true; // optional boolean, defaults to false

  constructor(private http: HttpClient) {
    this.http.get("api/temphumid/monthlystatistics").subscribe((data: any) => {
          var monthlystatistics      = data.monthlystatistics;

          var dailyHighArray     = [];
          var dailyLowArray      = [];
          var dailyAbsHighArray  = [];
          var dailyAbsLowArray   = [];
          var timeArray          = [];
          var monthAbsHighArray  = [];
          var monthAbsLowArray   = [];
          var monthHighArray     = [];
          var monthLowArray      = [];
          var monthTimeArray     = [];
          var d                  = new Date(0);
          var utcSeconds         = data.timestamp;
          d.setUTCSeconds((utcSeconds/1000));
          this.timestamp       = d.toString();

          for (var monthKey in monthlystatistics) {
              if (monthlystatistics.hasOwnProperty(monthKey)) {
                  monthTimeArray.push(monthKey);
                  var count = 0;
                  var sum   = 0;
                  for (var dayKey in monthlystatistics[monthKey].dailyHigh) {
                      if (monthlystatistics[monthKey].dailyHigh.hasOwnProperty(dayKey)) {
                          for (var specificDayKey in monthlystatistics[monthKey].dailyHigh[dayKey]) {
                              if (monthlystatistics[monthKey].dailyHigh[dayKey].hasOwnProperty(specificDayKey)) {

                                 timeArray.push(monthKey + "-" + specificDayKey);
                                 var tempC              = monthlystatistics[monthKey].dailyHigh[dayKey][specificDayKey];
                                 var tempF              = tempC * 9 / 5 + 32;
                                 count ++;
                                 sum                    = sum+tempF;

                                 var tempFString        = tempF.toFixed(3);
                                 var tempFFloatF        = parseFloat(tempFString);

                                 var maxTempC           = monthlystatistics[monthKey].monthlyStats.MonthlyHigh
                                 var maxTempF           = maxTempC * 9 / 5 + 32;
                                 var maxTempFString     = maxTempF.toFixed(3);
                                 var maxTempFFloat      = parseFloat(maxTempFString);

                                 dailyHighArray.push(tempFFloatF);
                                 dailyAbsHighArray.push(maxTempFFloat);
                              }
                          }
                      }


                  }
                  var monthlyTempDecF      = (sum/count);
                  var monthlyTempDecFixedF = monthlyTempDecF.toFixed(3);
                  var monthlyNumF          = parseFloat(monthlyTempDecFixedF);
                  monthHighArray.push(monthlyNumF);

                  count = 0;
                  sum   = 0;
                  for (var dayKey in monthlystatistics[monthKey].dailyLow) {
                      if (monthlystatistics[monthKey].dailyLow.hasOwnProperty(dayKey)) {
                          for (var specificDayKey in monthlystatistics[monthKey].dailyLow[dayKey]) {
                              if (monthlystatistics[monthKey].dailyLow[dayKey].hasOwnProperty(specificDayKey)) {
                                  var tempC              = monthlystatistics[monthKey].dailyLow[dayKey][specificDayKey];
                                  var tempF              = tempC * 9 / 5 + 32;
                                  count ++;
                                  sum                    = sum+tempF;

                                  var tempFString        = tempF.toFixed(3);
                                  var tempFFloatF        = parseFloat(tempFString);

                                  var minTempC           = monthlystatistics[monthKey].monthlyStats.MonthlyLow
                                  var minTempF           = minTempC * 9 / 5 + 32;
                                  var minTempFString     = minTempF.toFixed(3);
                                  var minTempFFloat      = parseFloat(minTempFString);


                                  dailyLowArray.push(tempFFloatF);
                                  dailyAbsLowArray.push(minTempFFloat);
                              }
                          }
                      }

                  }
                  var monthlyTempDecF      = (sum/count);
                  var monthlyTempDecFixedF = monthlyTempDecF.toFixed(3);
                  var monthlyNumF          = parseFloat(monthlyTempDecFixedF);
                  monthLowArray.push(monthlyNumF);

                  //FullMonth High Lows
                  monthAbsHighArray.push(maxTempFFloat);
                  monthAbsLowArray.push(minTempFFloat);
              }
          }



          this.monthlyTempChart = {
                           chart: {
                              type: 'bar'
                           },
                           title: {
                               text: 'Monthly Temperature Chart'
                           },
                           xAxis: {
                               categories: monthTimeArray
                           },
                           yAxis:[{
                               lineWidth: 1,
                               min:50,
                               title: {
                                      text: 'Temperature (F)'
                               }
                           }],

                           series: [{
                              name: 'High Temperature',
                              data: monthAbsHighArray
                           },
                           {
                               name: 'Avg High Temperature',
                               data: monthHighArray
                           },{
                               name: 'Avg Low Temperature',
                               data: monthLowArray
                           },{
                               name: 'Low Temperature',
                               data: monthAbsLowArray
                           }]
           }


           this.monthlyDailyTempChart = {
                           title: {
                               text: 'Monthly Daily Average Temperature Chart'
                           },
                           xAxis: {
                               categories: timeArray
                           },
                           yAxis:[{
                               lineWidth: 1,
                               min:50,
                               title: {
                                      text: 'Temperature (F)'
                               }
                           }],

                           series: [{
                               name: 'Monthly High Temperature',
                               data: dailyAbsHighArray,
                               marker: { enabled: false },
                               dashStyle: 'shortdot'
                           },{
                               name: 'Daily High Temperature',
                               data: dailyHighArray
                           },{
                               name: 'Daily Low Temperature',
                               data: dailyLowArray
                           },{
                                name: 'Monthly Low Temperature',
                                data: dailyAbsLowArray,
                                marker: { enabled: false },
                                dashStyle: 'shortdot'
                           }]
           }



      });


    }
}
