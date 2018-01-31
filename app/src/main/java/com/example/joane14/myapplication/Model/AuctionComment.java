package com.example.joane14.myapplication.Model;

import java.io.Serializable;

/**
 * Created by Joane14 on 18/12/2017.
 */

public class AuctionComment implements Serializable {

    private int auctionCommentId;
    private int auctionComment;
    private User user;


    public int getAuctionCommentId() {
        return auctionCommentId;
    }

    public void setAuctionCommentId(int auctionCommentId) {
        this.auctionCommentId = auctionCommentId;
    }

    public Integer getAuctionComment() {
        return auctionComment;
    }

    public void setAuctionComment(int auctionComment) {
        this.auctionComment = auctionComment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
