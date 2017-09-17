package com.example.joane14.myapplication.Model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Joane14 on 16/09/2017.
 */

public class DayTimeModel implements Serializable {

    String day;
    List<String> time;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public List<String> getTime() {
        return time;
    }

    public void setTime(List<String> time) {
        this.time = time;
    }
}
