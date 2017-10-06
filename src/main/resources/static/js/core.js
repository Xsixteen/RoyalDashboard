var app = angular.module('royaldashboard', []);
app.run(function($rootScope) {
});

app.controller('tempHumid', function($scope, $interval, $http) {
    $scope.refreshData = function() {
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
            var dayTemps           = [];
            var timeArray          = [];
            
            tempDayArray.forEach(function(entry) {
                var tempF = entry.temperature * 9 / 5 + 32;
                dayTemps.push(Math.round(tempF));
            });
            
            tempDayArray.forEach(function(entry) {
                var date = new Date(entry.epochTime);
                var hh = date.getHours();
                var mm = date.getMinutes();
                timeArray.push(hh + ":" + mm);
            });
                  
                        
            Highcharts.chart('chart', {

                    xAxis: {
                        categories: timeArray
                    },

                    series: [{
                        data: dayTemps
                    }]
            });
        });
     }
     
     $interval(function() {
        $scope.refreshData();        
     }, 120000);
     
     $scope.refreshData();
});

