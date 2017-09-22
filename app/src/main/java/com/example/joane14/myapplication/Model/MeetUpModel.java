package com.example.joane14.myapplication.Model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Joane14 on 18/09/2017.
 */

public class MeetUpModel implements Serializable{
    List<LocationModel> locationModelList;
    List<DayTimeModel> dayTimeModelList;

    public List<LocationModel> getLocationModelList() {
        return locationModelList;
    }

    public void setLocationModelList(List<LocationModel> locationModelList) {
        this.locationModelList = locationModelList;
    }

    public List<DayTimeModel> getDayTimeModelList() {
        return dayTimeModelList;
    }

    public void setDayTimeModelList(List<DayTimeModel> dayTimeModelList) {
        this.dayTimeModelList = dayTimeModelList;
    }
}
