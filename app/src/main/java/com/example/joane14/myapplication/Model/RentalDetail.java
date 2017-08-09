package com.example.joane14.myapplication.Model;

/**
 * Created by Joane14 on 08/08/2017.
 */

public class RentalDetail {
    private int rental_detailId;

    private int daysForRent;

    private double calculatedPrice;

    private BookOwnerModel bookOwner;

    public int getRental_detailId() {
        return rental_detailId;
    }

    public void setRental_detailId(int rental_detailId) {
        this.rental_detailId = rental_detailId;
    }

    public int getDaysForRent() {
        return daysForRent;
    }

    public void setDaysForRent(int daysForRent) {
        this.daysForRent = daysForRent;
    }

    public double getCalculatedPrice() {
        return calculatedPrice;
    }

    public void setCalculatedPrice(double calculatedPrice) {
        this.calculatedPrice = calculatedPrice;
    }

    public BookOwnerModel getBookOwner() {
        return bookOwner;
    }

    public void setBookOwner(BookOwnerModel bookOwner) {
        this.bookOwner = bookOwner;
    }
}
