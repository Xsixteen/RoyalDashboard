package com.ericulicny.homedashboard.repo;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.ericulicny.homedashboard.domain.TempHumidity;

public interface TempHumidMongoRepo extends MongoRepository<TempHumidity, Long> {
    @Cacheable("epochasc")
    List<TempHumidity> findByEpochTimeBetweenOrderByEpochTimeAsc(long epochStart, long epochEnd);

}
