package com.eulicny.homedashboard.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MonthlyTemperature {
    private List<HashMap<Integer,Double>>dailyHigh      = new ArrayList<HashMap<Integer,Double>>();
    private List<HashMap<Integer,Double>>dailyLow       = new ArrayList<HashMap<Integer,Double>>();
    private List<HashMap<Integer,Double>>dailyAvg       = new ArrayList<HashMap<Integer,Double>>();


    public void addDailyHigh(HashMap<Integer,Double> value) {
        this.dailyHigh.add(value);
    }

    public void addDailyLow(HashMap<Integer,Double> value) {
        this.dailyLow.add(value);
    }

    public void addDailyAvg(HashMap<Integer,Double> value) {
        this.dailyAvg.add(value);
    }




}
