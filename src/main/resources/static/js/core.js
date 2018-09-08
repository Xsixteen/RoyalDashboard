var app = angular.module('royaldashboard', []);
app.run(function($rootScope) {

});

app.controller('menucontroller', function($scope, $interval, $http, $location) {
    $scope.isActive = function( path ) {
          if(!$location.absUrl().includes(".html")) {
            if(path == "index.html") {
                return true;
            }
          }
          return $location.absUrl().includes(path);
    };
});

app.controller('camera', function($scope, $interval, $http, $location) {
    $scope.cameraImg = "/api/camera/last";
    $scope.takePicture = function() {
        $http.get("api/camera/snap")
             .then(function(response) {
                 var random = (new Date()).toString();
                 $scope.cameraImg = "/api/camera/last?cb=" + random;
          });
    };
});

app.controller('tempHumid', function($scope, $interval, $http, $location) {
     $scope.dataLoaded         = false;
     $scope.intialNetText      = true;

     $scope.refreshNetworkData = function() {

          $scope.networkLoading = false;

          $http.get("api/network/current")
             .then(function(response) {
                 var rxbps                = response.data.rxrateBytes;
                 var txbps                = response.data.txrateBytes;
                 $scope.rxNetworkUsage    = Math.round(rxbps)/1000;
                 $scope.txNetworkUsage    = Math.round(txbps)/1000;
                 $scope.networkLoading    = true;
                 $scope.intialNetText     = false;
          });
     }

     $scope.refreshData = function() {
    
     $http.get("api/powerutilization/current")
        .then(function(response) {
            var powerKW            = response.data.powerKW;
            $scope.currPower       = Number(powerKW);
        });
        
       $http.get("/api/powerutilization/dayhistorical")
        .then(function(response) {
            var powerDayArray      = response.data.dayhistorical;
            var powerArray         = [];
            var timeArray          = [];
            
            powerDayArray.forEach(function(entry) {
                powerArray.push(Number(entry.powerKW));
                var date = new Date(entry.epochTime);
                var hh = date.getHours();
                var mm = date.getMinutes();
                timeArray.push(hh + ":" + mm);
            });
            
                        
           Highcharts.chart('powerChart', {
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
          });
       
            $scope.dataLoaded     = true;
        });
        
        
        $http.get("/api/temphumid/current")
        .then(function(response) {
            var tempCFull          = response.data.tempC;
            var tempC              = tempCFull.toFixed(3);
            var tempF              = tempC * 9 / 5 + 32;
            var d                  = new Date(0);
            var utcSeconds         = response.data.timestamp;
            d.setUTCSeconds((utcSeconds/1000));
            
            $scope.humidity        = response.data.rhPct.toFixed(2);
            $scope.timestamp       = d.toString();
            $scope.temperatureC    = tempC;
            $scope.temperatureF    = tempF.toFixed(2);
        });
        
     
        $http.get("/api/temphumid/dayhistorical")
        .then(function(response) {
            var tempDayArray       = response.data.dayhistorical;
            var dayHumid           = [];
            var dayTemps           = [];
            var timeArray          = [];
            
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
                  
                        
            Highcharts.chart('tempChart', {
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
            });
            
            
        });
     }
     
     $interval(function() {
        $scope.refreshData();        
     }, 120000);

     $interval(function() {
        $scope.refreshNetworkData();
     }, 300000);
     
     $scope.refreshData();
     $scope.refreshNetworkData();
});

app.controller('monthly', function($scope, $interval, $http, $location) {
    $scope.dataLoaded=false;

    $scope.monthlyData = function() {

     $http.get("api/temphumid/monthlystatistics")
        .then(function(response) {
            var monthlystatistics      = response.data.monthlystatistics;

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
            var utcSeconds         = response.data.timestamp;
            d.setUTCSeconds((utcSeconds/1000));
            $scope.timestamp       = d.toString();

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



            Highcharts.chart('monthlyTempChart', {
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
             });


             Highcharts.chart('monthlyDailyTempChart', {
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
             });


            $scope.dataLoaded=true;

        });


     }

     $scope.monthlyData();
});

