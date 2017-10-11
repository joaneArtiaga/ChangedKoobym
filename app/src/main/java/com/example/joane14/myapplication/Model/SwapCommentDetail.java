package com.example.joane14.myapplication.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Joane14 on 11/10/2017.
 */

public class SwapCommentDetail implements Serializable {


    @SerializedName("swapCommentDetailId")
    private long swapCommentDetailId;

    @SerializedName("swapDetail")
    private SwapDetail swapDetail;

    @SerializedName("swapComment")
    private SwapComment swapComment;


    public SwapComment getSwapComment() {
        return swapComment;
    }

    public long getSwapCommentDetailId() {
        return swapCommentDetailId;
    }

    public SwapDetail getSwapDetail() {
        return swapDetail;
    }

    public void setSwapComment(SwapComment swapComment) {
        this.swapComment = swapComment;
    }

    public void setSwapCommentDetailId(long swapCommentDetailId) {
        this.swapCommentDetailId = swapCommentDetailId;
    }

    public void setSwapDetail(SwapDetail swapDetail) {
        this.swapDetail = swapDetail;
    }


}
