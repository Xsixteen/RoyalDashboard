package com.ericulicny.homedashboard.service;

import com.ericulicny.homedashboard.domain.TempHumidity;
import com.ericulicny.homedashboard.repo.TempHumidMongoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TemperatureHumidityCacheService {

    @Autowired
    private TempHumidMongoRepo tempHumidMongoRepo;

    private static final Logger log                                     = LoggerFactory.getLogger(TemperatureHumidityCacheService.class);

    private static ConcurrentHashMap<Long,TempHumidity> cache           = new ConcurrentHashMap();

    public List<TempHumidity> getCachedTemperatureHumidityData(Long start, Long end) {
        ArrayList<TempHumidity> listOfRequestedData = new ArrayList<>();
        Long maxValue                               = 0L;
        for(Long epochKey : cache.keySet()) {
            if(epochKey >= start && epochKey <= end) {
                listOfRequestedData.add(cache.get(epochKey));
                if(epochKey > maxValue) {
                    maxValue = epochKey;
                }
            }
        }

        List<TempHumidity> result = tempHumidMongoRepo.findByEpochTimeBetweenOrderByEpochTimeAsc(maxValue, end);
        log.info("Updating Cache with values between " + maxValue + " and " + end + " Database returned rows="+result.size());

        for(TempHumidity tempHumidity : result) {
            cache.put(tempHumidity.getEpochTime(), tempHumidity);
            listOfRequestedData.add(tempHumidity);
        }

        return listOfRequestedData;

    }

    /**
     * function removes all records before earliest record
     * @param earliestRecord
     */
    public void cleanCache(Long earliestRecord) {
        Long maxValue                               = 0L;
        for(Long epochKey : cache.keySet()) {
            if(epochKey <= earliestRecord) {
                cache.remove(epochKey);
            }
        }
    }


}
