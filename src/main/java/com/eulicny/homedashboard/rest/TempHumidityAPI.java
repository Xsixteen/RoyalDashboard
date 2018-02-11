package com.eulicny.homedashboard.rest;

import java.io.IOException;
import java.time.Instant;
import java.util.*;

import com.eulicny.homedashboard.domain.MonthlyTemperature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ericulicny.adafruit.sensor.TempHumiditySensor;
import com.eulicny.homedashboard.dao.TempHumidity;
import com.eulicny.homedashboard.repo.TempHumidMongoRepo;

@RestController
public class TempHumidityAPI {
    @Autowired
    private TempHumidMongoRepo tempHumidMongoRepo;
    
    TempHumiditySensor temperatureHudmiditySensor = new TempHumiditySensor();
    
    @RequestMapping("/api/temphumid/current")
    public Map<String,Object> home() {
        Map<String,Object> model = new HashMap<String,Object>();
        temperatureHudmiditySensor.initialize();
        try {
            model.put("tempC", temperatureHudmiditySensor.getTemperature());
            model.put("rhPct", temperatureHudmiditySensor.getHumidity());
            model.put("timestamp", System.currentTimeMillis());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
  
        return model;
    }
    
    @RequestMapping("/api/temphumid/dayhistorical")
    public Map<String, List<TempHumidity>> dayHistorical() {
        Map<String,List<TempHumidity> > model = new HashMap<String,List<TempHumidity>>();
        
        Long now = Instant.now().toEpochMilli();
        List<TempHumidity> temperatureHumidityList = tempHumidMongoRepo.findByEpochTimeBetweenOrderByEpochTimeAsc(now - (1000 * 60 * 60 * 24), now);

        model.put("dayhistorical", temperatureHumidityList);
  
        return model;
    }
    
    @RequestMapping("/api/temphumid/hourhistorical")
    public Map<String, List<TempHumidity>> hourHistorical() {
        Map<String,List<TempHumidity> > model = new HashMap<String,List<TempHumidity>>();
        
        Long now = Instant.now().toEpochMilli();
        List<TempHumidity> temperatureHumidityList = tempHumidMongoRepo.findByEpochTimeBetweenOrderByEpochTimeAsc(now - (1000 * 60 * 60), now);

        model.put("hourhistorical", temperatureHumidityList);
  
        return model;
    }
    
    @RequestMapping("/api/temphumid/monthhistorical")
    public Map<String, List<TempHumidity>> monthHistorical() {
        Map<String,List<TempHumidity> > model = new HashMap<String,List<TempHumidity>>();
        
        Long now = Instant.now().toEpochMilli();
        List<TempHumidity> temperatureHumidityList = tempHumidMongoRepo.findByEpochTimeBetweenOrderByEpochTimeAsc(now - (1000 * 60 * 60 * 24 * 31), now);

        model.put("monthhistorical", temperatureHumidityList);
  
        return model;
    }


    @RequestMapping("/api/temphumid/monthlystatistics")
    public Map<String, MonthlyTemperature> monthStatistics() {
        MonthlyTemperature monthlyTemperature                       = new MonthlyTemperature();
        Map<String, MonthlyTemperature> model                       = new HashMap<>();

        Long now = Instant.now().toEpochMilli();
        List<TempHumidity> temperatureHumidityList = tempHumidMongoRepo.findByEpochTimeBetweenOrderByEpochTimeAsc(now - (1000 * 60 * 60 * 24 * 365), now);

        Calendar calendar = Calendar.getInstance();
        //Months loop
        for(int i = 0; i < 12; i++){
          calendar.add(Calendar.MONTH,-i);

          //Day loop
          for(int j = 1; j < calendar.getActualMaximum(Calendar.DAY_OF_MONTH); j++) {
              calendar.set(Calendar.DAY_OF_MONTH, j);

              calendar.set(Calendar.HOUR_OF_DAY, 0);
              calendar.set(Calendar.MINUTE, 0);

              Long start      = calendar.getTimeInMillis();

              calendar.set(Calendar.HOUR_OF_DAY, 23);
              calendar.set(Calendar.MINUTE, 59);

              Long end        = calendar.getTimeInMillis();
              Double dailyMin = 100.0;
              Double dailyMax = -273.0;
              Double dailyAvg = 0.0;
              int count       = 0;
              //Iterate through and generate data
              for(TempHumidity tempHumid : temperatureHumidityList) {
                  if(tempHumid.getEpochTime() > start && tempHumid.getEpochTime() < end) {
                      if (tempHumid.getTemperature() > dailyMax) {
                          dailyMax = tempHumid.getTemperature();
                      }
                      if (tempHumid.getTemperature() < dailyMin) {
                          dailyMin = tempHumid.getTemperature();
                      }
                      dailyAvg = dailyAvg + tempHumid.getTemperature();
                      count++;

                  }
              }
              HashMap<Integer,Double> dailyAvgHash  = new HashMap<Integer,Double>();
              HashMap<Integer,Double> dailyLowHash  = new HashMap<Integer,Double>();
              HashMap<Integer,Double> dailyHighHash = new HashMap<Integer,Double>();
              dailyAvgHash.put(j,  (dailyAvg/count));
              dailyLowHash.put(j,  dailyMin);
              dailyHighHash.put(j, dailyMax);

              monthlyTemperature.addDailyAvg(dailyAvgHash);
              monthlyTemperature.addDailyHigh(dailyHighHash);
              monthlyTemperature.addDailyLow(dailyLowHash);

          }




        }


        model.put("monthlystatsitics", monthlyTemperature);

        return model;
    }


}

