package com.example.joane14.myapplication.Model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Joane14 on 17/09/2017.
 */

public class MeetUpLocObj implements Serializable {

    List<LocationModel> locationModelList;

    public List<LocationModel> getLocationModelList() {
        return locationModelList;
    }

    public void setLocationModelList(List<LocationModel> locationModelList) {
        this.locationModelList = locationModelList;
    }
}
