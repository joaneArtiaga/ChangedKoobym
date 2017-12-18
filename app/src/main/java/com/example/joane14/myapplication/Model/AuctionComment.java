package com.example.joane14.myapplication.Model;

import java.io.Serializable;

/**
 * Created by Joane14 on 18/12/2017.
 */

public class AuctionComment implements Serializable {

    private int auctionCommentId;
    private String auctionComment;
    private User user;


    public int getAuctionCommentId() {
        return auctionCommentId;
    }

    public void setAuctionCommentId(int auctionCommentId) {
        this.auctionCommentId = auctionCommentId;
    }

    public String getAuctionComment() {
        return auctionComment;
    }

    public void setAuctionComment(String auctionComment) {
        this.auctionComment = auctionComment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
