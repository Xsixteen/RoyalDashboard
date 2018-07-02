package com.eulicny.homedashboard.tasks;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ericulicny.adafruit.sensor.TempHumiditySensor;
import com.ericulicny.power.PowerRequest;
import com.ericulicny.power.PowerUtilizationConstants;
import com.eulicny.homedashboard.dao.PowerUtilization;
import com.eulicny.homedashboard.dao.TempHumidity;
import com.eulicny.homedashboard.repo.PowerUtilizationMongoRepo;
import com.eulicny.homedashboard.repo.TempHumidMongoRepo;

@Component
public class ScheduledTasks {
    private TempHumiditySensor tempHumidSensor  = new TempHumiditySensor();
    private static final Logger log             = LoggerFactory.getLogger(ScheduledTasks.class);

    @Autowired
    private TempHumidMongoRepo tempHumidMongoRepo;
    
    @Autowired
    private PowerUtilizationMongoRepo powerUtilizationMongoRepo;
    
    @Value("${powerHost}")
    private String powerHost;
    private final int CONST_TIMER_MINUTES   = 10;
    @Scheduled(fixedRate = CONST_TIMER_MINUTES*60000)
    public void reportTempAndHumidity() {
        tempHumidSensor.initialize();
        Date date = new Date();
        TempHumidity temperatureHumidityInsert = new TempHumidity();
        temperatureHumidityInsert.setEpochTime(Instant.now().toEpochMilli());
        try {
            temperatureHumidityInsert.setHumidity(tempHumidSensor.getHumidity());
            temperatureHumidityInsert.setTemperature(tempHumidSensor.getTemperature());
            temperatureHumidityInsert.setCreated(date.toString());
            temperatureHumidityInsert.setSensorId(1);
            tempHumidMongoRepo.insert(temperatureHumidityInsert);
        } catch (IOException e) {
            log.error("Error fetching temperature data=" + e.getMessage());
            e.printStackTrace();
        }
        
    }
    
    @Scheduled(fixedRate = CONST_TIMER_MINUTES*60000)
    public void reportPowerUtilization() {
        Date date = new Date();
        PowerUtilization powerUtilizationInsert = new PowerUtilization();
        powerUtilizationInsert.setEpochTime(Instant.now().toEpochMilli());
        try {
            powerUtilizationInsert.setPowerKW(Double.parseDouble(PowerRequest.getPowerKW("http://"+powerHost+PowerUtilizationConstants.CONST_POWERURL)));
            powerUtilizationMongoRepo.insert(powerUtilizationInsert);
        } catch (Exception e) {
            log.error("Error fetching Power data=" + e.getMessage());
            e.printStackTrace();
        }
        
    }
}
