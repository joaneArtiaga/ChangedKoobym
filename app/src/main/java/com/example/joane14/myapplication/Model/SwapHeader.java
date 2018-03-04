package com.example.joane14.myapplication.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

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

    @SerializedName("requestedSwapDetail")
    private SwapDetail requestedSwapDetail;

    @SerializedName("dateRequest")
    private String dateRequest;

    @SerializedName("dateApproved")
    private String dateApproved;

    @SerializedName("dateRejected")
    private String dateRejected;

    @SerializedName("dateConfirmed")
    private String dateConfirmed;

    @SerializedName("dateDelivered")
    private String dateDelivered;

    @SerializedName("dateReceived")
    private String dateReceived;

    @SerializedName("swapExtraMessage")
    private String swapExtraMessage;

    List<SwapHeaderDetail> swapHeaderDetails;

    public List<SwapHeaderDetail> getSwapHeaderDetail() {
        return swapHeaderDetails;
    }

    public void setSwapHeaderDetail(List<SwapHeaderDetail> swapHeaderDetails) {
        this.swapHeaderDetails = swapHeaderDetails;
    }

    private MeetUp meetUp;

    public String getSwapExtraMessage() {
        return swapExtraMessage;
    }

    public void setSwapExtraMessage(String swapExtraMessage) {
        this.swapExtraMessage = swapExtraMessage;
    }

    public String getDateDelivered() {
        return dateDelivered;
    }

    public void setDateDelivered(String dateDelivered) {
        this.dateDelivered = dateDelivered;
    }

    public MeetUp getMeetUp() {
        return meetUp;
    }

    public void setMeetUp(MeetUp meetUp) {
        this.meetUp = meetUp;
    }

    public String getDateRequest() {
        return dateRequest;
    }

    public void setDateRequest(String dateRequest) {
        this.dateRequest = dateRequest;
    }

    public String getDateApproved() {
        return dateApproved;
    }

    public void setDateApproved(String dateApproved) {
        this.dateApproved = dateApproved;
    }

    public String getDateRejected() {
        return dateRejected;
    }

    public void setDateRejected(String dateRejected) {
        this.dateRejected = dateRejected;
    }

    public String getDateConfirmed() {
        return dateConfirmed;
    }

    public void setDateConfirmed(String dateConfirmed) {
        this.dateConfirmed = dateConfirmed;
    }

    public String getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(String dateReceived) {
        this.dateReceived = dateReceived;
    }

    public SwapDetail getRequestedSwapDetail() {
        return requestedSwapDetail;
    }

    public void setRequestedSwapDetail(SwapDetail requestedSwapDetail) {
        this.requestedSwapDetail = requestedSwapDetail;
    }

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
