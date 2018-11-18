package com.ericulicny.homedashboard.rest;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ericulicny.power.PowerRequest;
import com.ericulicny.power.PowerUtilizationConstants;
import com.ericulicny.homedashboard.domain.PowerUtilization;
import com.ericulicny.homedashboard.repo.PowerUtilizationMongoRepo;

@RestController
public class PowerUtilizationAPI {
    @Autowired
    private PowerUtilizationMongoRepo powerUtilizationMongoRepo;
    
    @Value("${powerHost}")
    private String powerHost;

        
    @RequestMapping("/api/powerutilization/current")
    public Map<String,Object> home() {
        Map<String,Object> model = new HashMap<String,Object>();
        try {
            model.put("powerKW", PowerRequest.getPowerKW("http://"+powerHost+PowerUtilizationConstants.CONST_POWERURL));
            model.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
  
        return model;
    }
    
    @RequestMapping("/api/powerutilization/dayhistorical")
    public Map<String, List<PowerUtilization>> dayHistorical() {
        Map<String,List<PowerUtilization> > model = new HashMap<String,List<PowerUtilization>>();
        
        Long now = Instant.now().toEpochMilli();
        List<PowerUtilization> temperatureHumidityList = powerUtilizationMongoRepo.findByEpochTimeBetweenOrderByEpochTimeAsc(now - (1000 * 60 * 60 * 24), now);

        model.put("dayhistorical", temperatureHumidityList);
  
        return model;
    }
    
    @RequestMapping("/api/powerutilization/hourhistorical")
    public Map<String, List<PowerUtilization>> hourHistorical() {
        Map<String,List<PowerUtilization> > model = new HashMap<String,List<PowerUtilization>>();
        
        Long now = Instant.now().toEpochMilli();
        List<PowerUtilization> temperatureHumidityList = powerUtilizationMongoRepo.findByEpochTimeBetweenOrderByEpochTimeAsc(now - (1000 * 60 * 60), now);

        model.put("hourhistorical", temperatureHumidityList);
  
        return model;
    }
    
    @RequestMapping("/api/powerutilization/monthhistorical")
    public Map<String, List<PowerUtilization>> monthHistorical() {
        Map<String,List<PowerUtilization> > model = new HashMap<String,List<PowerUtilization>>();
        
        Long now = Instant.now().toEpochMilli();
        List<PowerUtilization> temperatureHumidityList = powerUtilizationMongoRepo.findByEpochTimeBetweenOrderByEpochTimeAsc(now - (1000L * 60L * 60L * 24L * 31L), now);

        model.put("monthhistorical", temperatureHumidityList);
  
        return model;
    }
}

