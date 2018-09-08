package com.ericulicny.homedashboard.tasks;

import java.time.Instant;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ericulicny.adafruit.sensor.TempHumiditySensor;
import com.ericulicny.power.PowerRequest;
import com.ericulicny.power.PowerUtilizationConstants;
import com.ericulicny.homedashboard.domain.PowerUtilization;
import com.ericulicny.homedashboard.domain.TempHumidity;
import com.ericulicny.homedashboard.repo.PowerUtilizationMongoRepo;
import com.ericulicny.homedashboard.repo.TempHumidMongoRepo;

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
        log.info("Fetching Temperature and Humidity Data");

        try {
            tempHumidSensor.initialize();
            Date date = new Date();
            TempHumidity temperatureHumidityInsert = new TempHumidity();
            temperatureHumidityInsert.setEpochTime(Instant.now().toEpochMilli());
            temperatureHumidityInsert.setHumidity(tempHumidSensor.getHumidity());
            temperatureHumidityInsert.setTemperature(tempHumidSensor.getTemperature());
            temperatureHumidityInsert.setCreated(date.toString());
            temperatureHumidityInsert.setSensorId(1);
            tempHumidMongoRepo.insert(temperatureHumidityInsert);
        } catch (Exception e) {
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
