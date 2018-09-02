package com.eulicny.homedashboard.repo;

import org.springframework.data.repository.CrudRepository;
import com.eulicny.homedashboard.dao.TempHumidity;

public interface TempHumidityRepo extends CrudRepository<TempHumidity, Long> {
}
