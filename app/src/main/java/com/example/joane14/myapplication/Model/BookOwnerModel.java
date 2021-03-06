package com.example.joane14.myapplication.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Joane14 on 05/08/2017.
 */

public class BookOwnerModel implements Serializable{

    String dateBought, statusDescription, status, bookStat;

    int noRenters;

    @SerializedName("book_ownerId")
    int bookOwnerId;

    @SerializedName("book")
    Book bookObj;

    @SerializedName("user")
    User userObj;

    @SerializedName("rate")
    Double rate;

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public BookOwnerModel(){

    }

    public BookOwnerModel(String dateBought, String statusDescription, Book bookObj, User userObj){
        this.dateBought = dateBought;
        this.statusDescription = statusDescription;
        this.bookObj = bookObj;
        this.userObj = userObj;
    }

    public String getBookStat() {
        return bookStat;
    }

    public void setBookStat(String bookStat) {
        this.bookStat = bookStat;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getBookOwnerId() {
        return bookOwnerId;
    }

    public void setBookOwnerId(int bookOwnerId) {
        this.bookOwnerId = bookOwnerId;
    }

    public int getNoRenters() {
        return noRenters;
    }

    public void setNoRenters(int noRenters) {
        this.noRenters = noRenters;
    }

    public void setDateBought(String dateBought) {
        this.dateBought = dateBought;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    public void setBookObj(Book bookObj) {
        this.bookObj = bookObj;
    }

    public void setUserObj(User userObj) {
        this.userObj = userObj;
    }

    public String getDateBought() {
        return dateBought;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public Book getBookObj() {
        return bookObj;
    }

    public User getUserObj() {
        return userObj;
    }
}
