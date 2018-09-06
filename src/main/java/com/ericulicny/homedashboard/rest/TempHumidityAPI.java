package com.ericulicny.homedashboard.rest;

import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.ericulicny.homedashboard.domain.MonthlyTemperature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ericulicny.adafruit.sensor.TempHumiditySensor;
import com.ericulicny.homedashboard.domain.TempHumidity;
import com.ericulicny.homedashboard.repo.TempHumidMongoRepo;

@RestController
public class TempHumidityAPI {
    @Autowired
    private TempHumidMongoRepo tempHumidMongoRepo;
    
    TempHumiditySensor temperatureHudmiditySensor                       = new TempHumiditySensor();
    private static final Logger log                                     = LoggerFactory.getLogger(TempHumidityAPI.class);

    private final static String CONST_API_TEMPHUMID                     = "temphumid";
    private static ConcurrentHashMap<Long,TempHumidity> cache           = new ConcurrentHashMap();

    public List<TempHumidity> getCachedTemperatureHumidityData(Long start, Long end) {
        ArrayList<TempHumidity> listOfRequestedData = new ArrayList<>();
        ArrayList<Long> missingValues               = new ArrayList<>();
        for(Long epochKey : cache.keySet()) {
            if(epochKey >= start && epochKey <= end) {
                listOfRequestedData.add(cache.get(epochKey));
            } else {
                missingValues.add(epochKey);
            }
        }

        Collections.sort(missingValues);
        List<TempHumidity> result = tempHumidMongoRepo.findByEpochTimeBetweenOrderByEpochTimeAsc(missingValues.get(0), missingValues.get(missingValues.size()-1));
        log.info("Missing Value List Size=" + missingValues.size() + " Updating Cache with values between " + missingValues.get(0) + " and " + missingValues.get(missingValues.size()-1));

        for(TempHumidity tempHumidity : result) {
            cache.put(tempHumidity.getEpochTime(), tempHumidity);
            listOfRequestedData.add(tempHumidity);
        }

        return listOfRequestedData;

    }

    @RequestMapping("/api/"+CONST_API_TEMPHUMID+"/current")
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
    
    @RequestMapping("/api/"+CONST_API_TEMPHUMID+"/dayhistorical")
    public Map<String, List<TempHumidity>> dayHistorical() {
        Map<String,List<TempHumidity> > model = new HashMap<String,List<TempHumidity>>();
        
        Long now = Instant.now().toEpochMilli();
        List<TempHumidity> temperatureHumidityList = tempHumidMongoRepo.findByEpochTimeBetweenOrderByEpochTimeAsc(now - (1000 * 60 * 60 * 24), now);
        log.info("API - dayhistorical result="+temperatureHumidityList.size());

        model.put("dayhistorical", temperatureHumidityList);
  
        return model;
    }
    
    @RequestMapping("/api/"+CONST_API_TEMPHUMID+"/hourhistorical")
    public Map<String, List<TempHumidity>> hourHistorical() {
        Map<String,List<TempHumidity> > model = new HashMap<String,List<TempHumidity>>();
        
        Long now = Instant.now().toEpochMilli();
        List<TempHumidity> temperatureHumidityList = tempHumidMongoRepo.findByEpochTimeBetweenOrderByEpochTimeAsc(now - (1000 * 60 * 60), now);
        log.info("API - hourhistorical result="+temperatureHumidityList.size());
        model.put("hourhistorical", temperatureHumidityList);
  
        return model;
    }
    
    @RequestMapping("/api/"+CONST_API_TEMPHUMID+"/monthhistorical")
    public Map<String, List<TempHumidity>> monthHistorical() {
        Map<String,List<TempHumidity> > model = new HashMap<String,List<TempHumidity>>();
        
        long now = Instant.now().toEpochMilli();
        long monthMillis = 1000L * 60 * 60 * 24 * 31;
        List<TempHumidity> temperatureHumidityList = tempHumidMongoRepo.findByEpochTimeBetweenOrderByEpochTimeAsc(now - monthMillis, now);
        log.info("API - monthhistorical result="+temperatureHumidityList.size());

        model.put("monthhistorical", temperatureHumidityList);
  
        return model;
    }

    @RequestMapping("/api/"+CONST_API_TEMPHUMID+"/yearhistorical")
    public Map<String, List<TempHumidity>> yearHistorical() {
        Map<String,List<TempHumidity> > model = new HashMap<String,List<TempHumidity>>();

        long now = Instant.now().toEpochMilli();
        List<TempHumidity> temperatureHumidityList = getCachedTemperatureHumidityData(now - (1000L * 60L * 60L * 24L * 364L), now);
        //List<TempHumidity> temperatureHumidityList = tempHumidMongoRepo.findByEpochTimeBetweenOrderByEpochTimeAsc(now - (1000L * 60L * 60L * 24L * 364L), now);
        log.info("API - yearhistorical result="+temperatureHumidityList.size());

        model.put("yearhistorical", temperatureHumidityList);

        return model;
    }


    @RequestMapping("/api/"+CONST_API_TEMPHUMID+"/monthlystatistics")
    public Map<String, Object> monthStatistics() {
        long startTime                                              = System.currentTimeMillis();
        MonthlyTemperature monthlyTemperature                       = null;

        HashMap<Integer, MonthlyTemperature> monthlyTemperatures    = new HashMap<>();
        Map<String, Object> model                                   = new HashMap<>();
        boolean valueFound                                          = false;
        Long now = Instant.now().toEpochMilli();
        List<TempHumidity> temperatureHumidityList                  = getCachedTemperatureHumidityData(now - (1000L * 60L * 60L * 24L * 364L), now);

        //List<TempHumidity> temperatureHumidityList = tempHumidMongoRepo.findByEpochTimeBetweenOrderByEpochTimeAsc(now - (1000L * 60L * 60L * 24L * 364L), now);
        log.info("API - monthlystatistics result="+temperatureHumidityList.size());

        Calendar calendar = Calendar.getInstance();
        //Months loop
        for(int i = 0; i < 12; i++){

          if(i != 0) {
                calendar.add(Calendar.MONTH, -1);
          }

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
                      valueFound = true;
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
              if(valueFound) {
                  if(monthlyTemperatures.get(calendar.get(Calendar.MONTH) + 1) == null) {
                      monthlyTemperature                         = new MonthlyTemperature();
                  } else {
                      monthlyTemperature                         = monthlyTemperatures.get(calendar.get(Calendar.MONTH) + 1);
                  }

                  HashMap<Integer, Double> dailyAvgHash = new HashMap<Integer, Double>();
                  HashMap<Integer, Double> dailyLowHash = new HashMap<Integer, Double>();
                  HashMap<Integer, Double> dailyHighHash = new HashMap<Integer, Double>();
                  dailyAvgHash.put(j, (dailyAvg / count));
                  dailyLowHash.put(j, dailyMin);
                  dailyHighHash.put(j, dailyMax);

                  monthlyTemperature.addDailyAvg(dailyAvgHash);
                  monthlyTemperature.addDailyHigh(dailyHighHash);
                  monthlyTemperature.addDailyLow(dailyLowHash);
                  monthlyTemperatures.put(calendar.get(Calendar.MONTH) + 1, monthlyTemperature);
              }

              valueFound = false;
          }




        }
        long endTime = System.currentTimeMillis();
        log.info("API Operation took " + ((endTime-startTime)/1000) + " seconds");
        model.put("monthlystatistics", monthlyTemperatures);
        model.put("timestamp", System.currentTimeMillis());

        return model;
    }


}

