 package com.eulicny.homedashboard.rest;

import java.util.HashMap;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.eulicny.homedashboard.domain.TempHumidity;
import com.eulicny.homedashboard.repo.TempHumidityRepo;

@RestController
@RequestMapping(path="/api")
public class HomeRestController {

    @Autowired
    private TempHumidityRepo tempHumidityRepo;
    
    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> add(@RequestBody TempHumidity input) {
        if(input.getTemperature() == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unexpected Format");
        
        this.tempHumidityRepo.save(input);
        
        return new ResponseEntity(HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.GET)
    Map get() {
        Map results = new HashMap();
        results.put("results",this.tempHumidityRepo.findAll());
        //results.put("Test", "test");
        return results;
    }
    
    
}
