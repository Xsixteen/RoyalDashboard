package com.eulicny.homedashboard.repo;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.eulicny.homedashboard.dao.TempHumidity;

public interface TempHumidityRepo extends CrudRepository<TempHumidity, Long> {
}
