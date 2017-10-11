package com.example.joane14.myapplication.Model;

import java.io.Serializable;

/**
 * Created by Joane14 on 11/10/2017.
 */

public class BookOwnerRating implements Serializable {

    private int bookOwnerRatingId;
    private BookOwnerModel bookOwner;
    private User user;
    private Rate rate;


    public int getBookOwnerRatingId() {
        return bookOwnerRatingId;
    }

    public void setBookOwnerRatingId(int bookOwnerRatingId) {
        this.bookOwnerRatingId = bookOwnerRatingId;
    }

    public BookOwnerModel getBookOwner() {
        return bookOwner;
    }

    public void setBookOwner(BookOwnerModel bookOwner) {
        this.bookOwner = bookOwner;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Rate getRate() {
        return rate;
    }

    public void setRate(Rate rate) {
        this.rate = rate;
    }
}
