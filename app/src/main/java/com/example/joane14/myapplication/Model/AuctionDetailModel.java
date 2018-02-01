package com.example.joane14.myapplication.Model;

import java.io.Serializable;

/**
 * Created by Joane14 on 28/11/2017.
 */

public class AuctionDetailModel implements Serializable {

    private int auctionDetailId;

    private float startingPrice;

    private BookOwnerModel bookOwner;

    private String startDate;

    private String endDate;

    private String endTime;

    private String startTime;

    private String auctionDescription;

    private String auctionStatus;

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getAuctionStatus() {
        return auctionStatus;
    }

    public void setAuctionStatus(String auctionStatus) {
        this.auctionStatus = auctionStatus;
    }

    public int getAuctionDetailId() {
        return auctionDetailId;
    }

    public void setAuctionDetailId(int auctionDetailId) {
        this.auctionDetailId = auctionDetailId;
    }

    public float getStartingPrice() {
        return startingPrice;
    }

    public void setStartingPrice(float startingPrice) {
        this.startingPrice = startingPrice;
    }

    public BookOwnerModel getBookOwner() {
        return bookOwner;
    }

    public void setBookOwner(BookOwnerModel bookOwner) {
        this.bookOwner = bookOwner;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getAuctionDescription() {
        return auctionDescription;
    }

    public void setAuctionDescription(String auctionDescription) {
        this.auctionDescription = auctionDescription;
    }


    @Override
    public String toString() {
        return "AuctionDetail{" +
                "auctionDetailId=" + auctionDetailId +
                ", startingPrice ='" + startingPrice + '\'' +
                ", bookOwner =" + bookOwner.toString() +
                ", startDate =" + startDate +
                ", endDate =" + endDate +
                ", endTime =" + endTime +
                ", startTime =" + startTime +
                ", auctionDescription =" + auctionDescription +
                ", auctionStatus =" + auctionStatus +
                '}';
    }
}
