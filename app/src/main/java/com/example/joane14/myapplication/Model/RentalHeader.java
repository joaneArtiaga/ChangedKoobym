package com.example.joane14.myapplication.Model;

import android.location.Location;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Joane14 on 03/10/2017.
 */

public class RentalHeader implements Serializable {

    @SerializedName("rentalHeaderId")
    Long rentalHeaderId;

    @SerializedName("user")
    User user;

    @SerializedName("rentalTimeStamp")
    String rentalTimeStamp;

    @SerializedName("totalPrice")
    float totalPrice;

    @SerializedName("location")
    LocationModel location;

    public Long getRentalHeaderId() {
        return rentalHeaderId;
    }

    public void setRentalHeaderId(Long rentalHeaderId) {
        this.rentalHeaderId = rentalHeaderId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRentalTimeStamp() {
        return rentalTimeStamp;
    }

    public void setRentalTimeStamp(String rentalTimeStamp) {
        this.rentalTimeStamp = rentalTimeStamp;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocationModel getLocation() {
        return location;
    }

    public void setLocation(LocationModel location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "RentalHeader{" +
                "rentalHeaderId=" + rentalHeaderId +
                ", user='" + user + '\'' +
                ", rentalTimeStamp=" + rentalTimeStamp +
                ", totalPrice='" + totalPrice + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
