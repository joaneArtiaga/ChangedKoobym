package com.example.joane14.myapplication.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Joane14 on 09/10/2017.
 */

public class SwapHeader implements Serializable {

    @SerializedName("swapHeaderId")
    private int swapHeaderId;

    @SerializedName("user")
    private User user;

    @SerializedName("swapDetail")
    private SwapDetail swapDetail;

    @SerializedName("dateTimeStamp")
    private String dateTimeStamp;

    @SerializedName("userDayTime")
    private UserDayTime userDayTime;

    @SerializedName("location")
    private LocationModel location;

    @SerializedName("status")
    private String status;


    public int getSwapHeaderId() {
        return swapHeaderId;
    }

    public void setSwapHeaderId(int swapHeaderId) {
        this.swapHeaderId = swapHeaderId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public SwapDetail getSwapDetail() {
        return swapDetail;
    }

    public void setSwapDetail(SwapDetail swapDetail) {
        this.swapDetail = swapDetail;
    }

    public String getDateTimeStamp() {
        return dateTimeStamp;
    }

    public void setDateTimeStamp(String dateTimeStamp) {
        this.dateTimeStamp = dateTimeStamp;
    }

    public UserDayTime getUserDayTime() {
        return userDayTime;
    }

    public void setUserDayTime(UserDayTime userDayTime) {
        this.userDayTime = userDayTime;
    }

    public LocationModel getLocation() {
        return location;
    }

    public void setLocation(LocationModel location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
