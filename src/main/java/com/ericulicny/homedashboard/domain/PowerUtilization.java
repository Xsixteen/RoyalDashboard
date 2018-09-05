package com.ericulicny.homedashboard.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "PowerUtilization")
@Entity
public class PowerUtilization {
    Double powerKW;
    
    @Id
    private Long epochTime;



    public Double getPowerKW() {
        return powerKW;
    }

    public void setPowerKW(Double powerKW) {
        this.powerKW = powerKW;
    }

    public Long getEpochTime() {
        return epochTime;
    }

    public void setEpochTime(Long epochTime) {
        this.epochTime = epochTime;
    }
    
    @Override
    public String toString() {
        return String.format(
                "TempHumidity[epoch=%s, Power='%s]",
                epochTime, powerKW);
    }
}
