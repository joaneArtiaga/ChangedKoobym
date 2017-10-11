package com.example.joane14.myapplication.Model;

import java.io.Serializable;

/**
 * Created by Joane14 on 11/10/2017.
 */

public class Review implements Serializable {

    private int reviewId;
    private String reviewTimeStamp;


    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public String getReviewTimeStamp() {
        return reviewTimeStamp;
    }

    public void setReviewTimeStamp(String reviewTimeStamp) {
        this.reviewTimeStamp = reviewTimeStamp;
    }
}
