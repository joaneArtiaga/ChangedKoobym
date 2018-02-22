package com.example.joane14.myapplication.Model;

import java.io.Serializable;

/**
 * Created by Joane14 on 11/10/2017.
 */

public class Rate implements Serializable {

    private int rateId;
    private float rateNumber;
    private String rateTimeStamp;

    public int getRateId() {
        return rateId;
    }

    public void setRateId(int rateId) {
        this.rateId = rateId;
    }

    public float getRateNumber() {
        return rateNumber;
    }

    public void setRateNumber(float rateNumber) {
        this.rateNumber = rateNumber;
    }

    public String getRateTimeStamp() {
        return rateTimeStamp;
    }

    public void setRateTimeStamp(String rateTimeStamp) {
        this.rateTimeStamp = rateTimeStamp;
    }

    @Override
    public String toString() {
        return "Rate{" +
                "RateId=" + rateId+
                ", rateNumber ='" + rateNumber + '\'' +
                ", rateTimeStamp =" + rateTimeStamp +
                '}';
    }
}
