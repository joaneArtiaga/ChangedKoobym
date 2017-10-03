package com.example.joane14.myapplication.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Joane14 on 03/10/2017.
 */

public class UserRental implements Serializable{

    @SerializedName("userRentalId")
    Long userRentalId;

    @SerializedName("user")
    User user;

    @SerializedName("rentalDetail")
    RentalDetail rentalDetail;

    @SerializedName("rentStatus")
    String rentStatus;

    @SerializedName("timeStamp")
    String timeStamp;


    public Long getUserRentalId() {
        return userRentalId;
    }

    public void setUserRentalId(Long userRentalId) {
        this.userRentalId = userRentalId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public RentalDetail getRentalDetail() {
        return rentalDetail;
    }

    public void setRentalDetail(RentalDetail rentalDetail) {
        this.rentalDetail = rentalDetail;
    }

    public String getRentStatus() {
        return rentStatus;
    }

    public void setRentStatus(String rentStatus) {
        this.rentStatus = rentStatus;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "UserRental{" +
                "userRentalId=" + userRentalId+
                ", user='" + user+ '\'' +
                ", rentalDetail=" + rentalDetail +
                ", rentStatus='" + rentStatus+ '\'' +
                ", timestamp='" + timeStamp + '\'' +
                '}';
    }
}
