package com.eulicny.homedashboard.dao;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

@Table(name = "IndoorTempHumid")
@Entity
public class TempHumidity {

    Double temperature;
    Double humidity;
    String created;
    
    @Id
    private Long epochTime;

    public Long getEpochTime() {
        return epochTime;
    }

    public void setEpochTime(Long epochTime) {
        this.epochTime = epochTime;
    }

    public TempHumidity() { // jpa only
    }

    public TempHumidity(Double temperature, Double humidity) {
        this.temperature    = temperature;
        this.humidity       = humidity;
    }
    
    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

  
    
    @Override
    public String toString() {
        return String.format(
                "TempHumidity[epoch=%s, Temperature='%s', Humidity='%s']",
                epochTime, temperature, humidity);
    }


}


