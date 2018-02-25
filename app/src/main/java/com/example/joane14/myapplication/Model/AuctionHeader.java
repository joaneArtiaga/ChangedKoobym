package com.example.joane14.myapplication.Model;

import java.io.Serializable;

/**
 * Created by Joane14 on 18/12/2017.
 */

public class AuctionHeader implements Serializable {

    private int auctionHeaderId;
    private User user;
    private AuctionDetailModel auctionDetail;
    private String auctionHeaderDateStamp, auctionExtraMessage;
    private MeetUp meetUp;
    private String dateDelivered;
    private String status;

    public String getAuctionExtraMessage() {
        return auctionExtraMessage;
    }

    public void setAuctionExtraMessage(String auctionExtraMessage) {
        this.auctionExtraMessage = auctionExtraMessage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDateDelivered() {
        return dateDelivered;
    }

    public void setDateDelivered(String dateDelivered) {
        this.dateDelivered = dateDelivered;
    }

    public int getAuctionHeaderId() {
        return auctionHeaderId;
    }

    public void setAuctionHeaderId(int auctionHeaderId) {
        this.auctionHeaderId = auctionHeaderId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public AuctionDetailModel getAuctionDetail() {
        return auctionDetail;
    }

    public void setAuctionDetail(AuctionDetailModel auctionDetail) {
        this.auctionDetail = auctionDetail;
    }

    public String getAuctionHeaderDateStamp() {
        return auctionHeaderDateStamp;
    }

    public void setAuctionHeaderDateStamp(String auctionHeaderDateStamp) {
        this.auctionHeaderDateStamp = auctionHeaderDateStamp;
    }

    public MeetUp getMeetUp() {
        return meetUp;
    }

    public void setMeetUp(MeetUp meetUp) {
        this.meetUp = meetUp;
    }
}
