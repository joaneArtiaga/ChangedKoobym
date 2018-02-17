package com.example.joane14.myapplication.Model;

import android.location.Location;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Joane14 on 03/10/2017.
 */

public class RentalHeader implements Serializable {

    @SerializedName("rentalHeaderId")
    Long rentalHeaderId;

    @SerializedName("user")
    User userId;

    @SerializedName("rentalTimeStamp")
    String rentalTimeStamp;

    @SerializedName("totalPrice")
    float totalPrice;

    @SerializedName("location")
    LocationModel location;

    @SerializedName("status")
    String status;

    @SerializedName("rentalDetail")
    RentalDetail rentalDetail;

    UserDayTime userDayTime;

    String dateApproved, dateConfirmed, rentalEndDate, dateDeliver;

    MeetUp meetUp, returnMeetUp;


    public MeetUp getReturnMeetUp() {
        return returnMeetUp;
    }

    public void setReturnMeetUp(MeetUp returnMeetUp) {
        this.returnMeetUp = returnMeetUp;
    }

    public String getDateDeliver() {
        return dateDeliver;
    }

    public void setDateDeliver(String dateDeliver) {
        this.dateDeliver = dateDeliver;
    }

    public String getRentalEndDate() {
        return rentalEndDate;
    }

    public void setRentalEndDate(String rentalEndDate) {
        this.rentalEndDate = rentalEndDate;
    }

    public String getDateConfirmed() {
        return dateConfirmed;
    }

    public void setDateConfirmed(String dateConfirmed) {
        this.dateConfirmed = dateConfirmed;
    }

    public MeetUp getMeetUp() {
        return meetUp;
    }

    public void setMeetUp(MeetUp meetUp) {
        this.meetUp = meetUp;
    }

    public String getDateApproved() {
        return dateApproved;
    }

    public void setDateApproved(String dateApproved) {
        this.dateApproved = dateApproved;
    }

    public UserDayTime getUserDayTime() {
        return userDayTime;
    }

    public void setUserDayTime(UserDayTime userDayTime) {
        this.userDayTime = userDayTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public RentalDetail getRentalDetail() {
        return rentalDetail;
    }

    public void setRentalDetail(RentalDetail rentalDetail) {
        this.rentalDetail = rentalDetail;
    }

    public Long getRentalHeaderId() {
        return rentalHeaderId;
    }

    public void setRentalHeaderId(Long rentalHeaderId) {
        this.rentalHeaderId = rentalHeaderId;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User user) {
        this.userId = user;
    }

    public String getRentalTimeStamp() {
        return rentalTimeStamp;
    }

    public void setRentalTimeStamp(String rentalTimeStamp) {
        this.rentalTimeStamp = rentalTimeStamp;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocationModel getLocation() {
        return location;
    }

    public void setLocation(LocationModel location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "RentalHeader{" +
                "rentalHeaderId=" + rentalHeaderId +
                ", rentalDetail='" + rentalDetail.toString() + '\'' +
                ", status='" + status + '\'' +
                ", user='" + userId.toString() + '\'' +
                ", rentalTimeStamp=" + rentalTimeStamp +
                ", totalPrice='" + totalPrice + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
