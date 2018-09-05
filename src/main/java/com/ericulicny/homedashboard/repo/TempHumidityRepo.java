package com.ericulicny.homedashboard.repo;

import org.springframework.data.repository.CrudRepository;
import com.ericulicny.homedashboard.domain.TempHumidity;

public interface TempHumidityRepo extends CrudRepository<TempHumidity, Long> {
}
