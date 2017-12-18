package com.example.joane14.myapplication.Model;

import java.io.Serializable;

/**
 * Created by Joane14 on 18/12/2017.
 */

public class AuctionCommentDetail implements Serializable {

    private int auctionCommentDetailId;
    private AuctionDetailModel auctionDetail;
    private AuctionComment auctionComment;


    public int getAuctionCommentDetailId() {
        return auctionCommentDetailId;
    }

    public void setAuctionCommentDetailId(int auctionCommentDetailId) {
        this.auctionCommentDetailId = auctionCommentDetailId;
    }

    public AuctionDetailModel getAuctionDetail() {
        return auctionDetail;
    }

    public void setAuctionDetail(AuctionDetailModel auctionDetail) {
        this.auctionDetail = auctionDetail;
    }

    public AuctionComment getAuctionComment() {
        return auctionComment;
    }

    public void setAuctionComment(AuctionComment auctionComment) {
        this.auctionComment = auctionComment;
    }
}
