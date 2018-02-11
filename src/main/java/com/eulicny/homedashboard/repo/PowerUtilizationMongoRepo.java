package com.eulicny.homedashboard.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.eulicny.homedashboard.dao.PowerUtilization;

public interface PowerUtilizationMongoRepo extends MongoRepository<PowerUtilization, Long> {
        List<PowerUtilization> findByEpochTimeBetweenOrderByEpochTimeAsc(long epochStart, long epochEnd);

}
