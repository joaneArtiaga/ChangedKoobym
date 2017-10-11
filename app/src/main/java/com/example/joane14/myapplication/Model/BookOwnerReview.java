package com.example.joane14.myapplication.Model;

import java.io.Serializable;

/**
 * Created by Joane14 on 11/10/2017.
 */

public class BookOwnerReview implements Serializable {

    private int bookOwnerReviewId;
    private BookOwnerModel bookOwner;
    private Review review;
    private User user;
    private String comment;


    public int getBookOwnerReviewId() {
        return bookOwnerReviewId;
    }

    public void setBookOwnerReviewId(int bookOwnerReviewId) {
        this.bookOwnerReviewId = bookOwnerReviewId;
    }

    public BookOwnerModel getBookOwner() {
        return bookOwner;
    }

    public void setBookOwner(BookOwnerModel bookOwner) {
        this.bookOwner = bookOwner;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
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
