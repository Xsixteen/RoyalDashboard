package com.eulicny.homedashboard.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.eulicny.homedashboard.dao.TempHumidity;

public interface TempHumidMongoRepo extends MongoRepository<TempHumidity, Long> {
    List<TempHumidity> findByEpochTimeBetweenOrderByEpochTimeAsc(long epochStart, long epochEnd);

}
