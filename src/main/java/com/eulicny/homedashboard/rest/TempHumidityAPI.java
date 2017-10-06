package com.eulicny.homedashboard.rest;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
}

