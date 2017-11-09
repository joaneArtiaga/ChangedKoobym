package com.example.joane14.myapplication.Model;

import java.io.Serializable;

/**
 * Created by Joane14 on 08/11/2017.
 */

public class BookActivity implements Serializable {

    private User user;
    private BookOwnerModel bookOwner;
    private String status, bookStatus, dateRequest;
    private long bookActivityId;

    public BookOwnerModel getBookOwner() {
        return bookOwner;
    }

    public void setBookOwner(BookOwnerModel bookOwner) {
        this.bookOwner = bookOwner;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBookStatus() {
        return bookStatus;
    }

    public void setBookStatus(String bookStatus) {
        this.bookStatus = bookStatus;
    }

    public String getDateRequest() {
        return dateRequest;
    }

    public void setDateRequest(String dateRequest) {
        this.dateRequest = dateRequest;
    }

    public long getBookActivityId() {
        return bookActivityId;
    }

    public void setBookActivityId(long bookActivityId) {
        this.bookActivityId = bookActivityId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
