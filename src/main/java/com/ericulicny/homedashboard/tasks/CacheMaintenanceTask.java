package com.ericulicny.homedashboard.tasks;

import com.ericulicny.homedashboard.service.TemperatureHumidityCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class CacheMaintenanceTask {
    private static final Logger log                                     = LoggerFactory.getLogger(CacheMaintenanceTask.class);

    @Autowired
    private TemperatureHumidityCacheService temperatureHumidityCacheService;

    private final long CONST_ONEDAY     = 1440;
    private final long CONST_ONEMONTH   = 43200;
    @Scheduled(fixedRate = CONST_ONEDAY*60000L)
    public void updateCache() {
        log.info("Building Cache ...");
        Long start = Instant.now().toEpochMilli();
        temperatureHumidityCacheService.getCachedTemperatureHumidityData(start - (1000L * 60L * 60L * 24L * 364L), start);
        log.info("Cache Building Operation took " + ((Instant.now().toEpochMilli()-start)/1000) + " seconds");

    }

    @Scheduled(fixedRate = CONST_ONEMONTH*60000L)
    public void cleanCache() {
        log.info("Cleaning Cache ...");
        Long start = Instant.now().toEpochMilli();
        temperatureHumidityCacheService.cleanCache(start - (1000L * 60L * 60L * 24L * 365L));
        log.info("Cache Cleaning Operation took " + ((Instant.now().toEpochMilli()-start)/1000) + " seconds");
    }
}
