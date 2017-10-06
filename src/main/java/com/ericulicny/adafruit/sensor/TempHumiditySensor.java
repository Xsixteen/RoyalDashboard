package com.ericulicny.adafruit.sensor;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;

public class TempHumiditySensor {

    private I2CBus bus;
    private I2CDevice i2cDevice;
    
    private static final Logger log = LoggerFactory.getLogger(TempHumiditySensor.class);

    public TempHumiditySensor() {
 
    }
    
    public void initialize() {
        try {
            bus = I2CFactory.getInstance(I2CBus.BUS_1);
            i2cDevice = bus.getDevice(TempHumidConstants.I2CADDRESS);
            resetDevice();
        } catch (UnsupportedBusNumberException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public Double getTemperature() throws IOException {
           sleep();
           i2cDevice.write((byte)TempHumidConstants.TEMP_MEAS_NOHOLD_CMD);
           sleep();
           int read1        = i2cDevice.read();
           sleep();
           int read2        = i2cDevice.read();
           sleep(); 
           int checksum     = i2cDevice.read();
           
           int tempRead     = (read1 << 8);
           log.info("Temperature Raw Read=" + tempRead);
           resetDevice();
           Double temperature = tempRead * 175.72;
           temperature = temperature / 65536;
           return temperature - 46.85;
           
    }
    
    public Double getHumidity() throws IOException {
        sleep();
        i2cDevice.write((byte)TempHumidConstants.HUMIDITY_MEAS_NOHOLD_CMD);
        sleep();
        int read1        = i2cDevice.read();
        sleep();
        int read2        = i2cDevice.read();
        sleep(); 
        int checksum     = i2cDevice.read();
        
        int tempRead     = (read1 << 8);
        log.info("Humidity Raw Read=" + tempRead);
    
        Double temperature = (double) (tempRead * 125);
        temperature = temperature / 65536;
        return temperature - 6;
    }
    
    public void resetDevice() throws IOException {
        i2cDevice.write((byte)TempHumidConstants.RESET_CMD);
    }
    
    private void sleep() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
