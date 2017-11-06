package com.example.joane14.myapplication.Model;

import java.io.Serializable;

/**
 * Created by Joane14 on 06/11/2017.
 */

public class MeetUp implements Serializable {

    Long meetUpId;
    LocationModel location;
    UserDayTime userDayTime;

    public Long getMeetUpId() {
        return meetUpId;
    }

    public void setMeetUpId(Long meetUpId) {
        this.meetUpId = meetUpId;
    }

    public LocationModel getLocation() {
        return location;
    }

    public void setLocation(LocationModel location) {
        this.location = location;
    }

    public UserDayTime getUserDayTime() {
        return userDayTime;
    }

    public void setUserDayTime(UserDayTime userDayTime) {
        this.userDayTime = userDayTime;
    }
}
