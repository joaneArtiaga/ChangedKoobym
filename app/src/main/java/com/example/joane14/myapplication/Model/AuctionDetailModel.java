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

    private String auctionDescription;


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
}
