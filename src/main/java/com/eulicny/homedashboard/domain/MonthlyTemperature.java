package com.eulicny.homedashboard.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MonthlyTemperature {
    private List<HashMap<Integer,Double>>dailyHigh      = new ArrayList<HashMap<Integer,Double>>();
    private List<HashMap<Integer,Double>>dailyLow       = new ArrayList<HashMap<Integer,Double>>();
    private List<HashMap<Integer,Double>>dailyAvg       = new ArrayList<HashMap<Integer,Double>>();
    private HashMap<String, Double>monthlyStats         = new HashMap<>();

    public void addDailyHigh(HashMap<Integer,Double> value) {
        this.dailyHigh.add(value);
    }

    public void addDailyLow(HashMap<Integer,Double> value) {
        this.dailyLow.add(value);
    }

    public void addDailyAvg(HashMap<Integer,Double> value) {
        this.dailyAvg.add(value);
    }

    public HashMap<String, Double> getMonthlyStats() {
        Double dailyHighMaxValue = new Double("0");
        Double dailyHighMinValue = new Double("100");
        for(HashMap<Integer,Double>dailyHighValue : dailyHigh) {
            for (Integer value : dailyHighValue.keySet()) {
                if(dailyHighValue.get(value) > dailyHighMaxValue) {
                    dailyHighMaxValue = dailyHighValue.get(value);
                }
                if(dailyHighValue.get(value) < dailyHighMinValue) {
                    dailyHighMinValue = dailyHighValue.get(value);
                }
            }
        }

        monthlyStats.put("MaxMonthlyHigh", dailyHighMaxValue);
        monthlyStats.put("MaxMonthlyLow",  dailyHighMinValue);

        return monthlyStats;
    }

    public void setMonthlyStats(HashMap<String, Double> monthlyStats) {
        this.monthlyStats = monthlyStats;
    }

    public List<HashMap<Integer, Double>> getDailyHigh() {
        return dailyHigh;

    }

    public void setDailyHigh(List<HashMap<Integer, Double>> dailyHigh) {
        this.dailyHigh = dailyHigh;
    }

    public List<HashMap<Integer, Double>> getDailyLow() {
        return dailyLow;
    }

    public void setDailyLow(List<HashMap<Integer, Double>> dailyLow) {
        this.dailyLow = dailyLow;
    }

    public List<HashMap<Integer, Double>> getDailyAvg() {
        return dailyAvg;
    }

    public void setDailyAvg(List<HashMap<Integer, Double>> dailyAvg) {
        this.dailyAvg = dailyAvg;
    }
}
