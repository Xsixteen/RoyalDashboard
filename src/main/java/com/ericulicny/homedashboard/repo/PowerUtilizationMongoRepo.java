package com.ericulicny.homedashboard.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ericulicny.homedashboard.domain.PowerUtilization;

public interface PowerUtilizationMongoRepo extends MongoRepository<PowerUtilization, Long> {
        List<PowerUtilization> findByEpochTimeBetweenOrderByEpochTimeAsc(long epochStart, long epochEnd);

}
