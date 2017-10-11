package com.example.joane14.myapplication.Model;

import java.io.Serializable;

/**
 * Created by Joane14 on 11/10/2017.
 */

public class UserRating implements Serializable {


    private int userRatingId;
    private Review review;
    private Rate rate;
    private User user, userRater;
    private String comment;


    public User getUserRater() {
        return userRater;
    }

    public void setUserRater(User userRater) {
        this.userRater = userRater;
    }

    public int getUserRatingId() {
        return userRatingId;
    }

    public void setUserRatingId(int userRatingId) {
        this.userRatingId = userRatingId;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    public Rate getRate() {
        return rate;
    }

    public void setRate(Rate rate) {
        this.rate = rate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
