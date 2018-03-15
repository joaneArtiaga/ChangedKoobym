package com.example.joane14.myapplication.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Joane14 on 02/10/2017.
 */

public class UserDayTime implements Serializable {

    @SerializedName("userDayTimeId")
    Long userDayTimeId;

    @SerializedName("userId")
    Long userId;

    @SerializedName("days")
    DayModel day;

    @SerializedName("times")
    TimeModel time;


    public Long getUserDayTimeId() {
        return userDayTimeId;
    }

    public void setUserDayTimeId(Long userDayTimeId) {
        this.userDayTimeId = userDayTimeId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public DayModel getDay() {
        return day;
    }

    public void setDay(DayModel day) {
        this.day = day;
    }

    public TimeModel getTime() {
        return time;
    }

    public void setTime(TimeModel time) {
        this.time = time;
    }


//    @Override
//    public String toString() {
//        return "UserDayTimeModel{" +
//                "userId=" + userId +
//                ", day='" + day.getStrDay() + '\'' +
//                ", time=" + time.getStrTime() +
//                '}';
//    }

}




