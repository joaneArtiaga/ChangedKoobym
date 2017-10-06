package com.example.joane14.myapplication.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Joane14 on 06/10/2017.
 */

public class SwapDetail implements Serializable {

    @SerializedName("swap_detailId")
    private int swapDetailId;

    @SerializedName("bookOwner")
    private BookOwnerModel bookOwner;

    @SerializedName("swapDescription")
    private String swapDescription;

    @SerializedName("swapTimeStamp")
    private String swapTimeStamp;

    @SerializedName("price")
    private float swapPrice;

    @SerializedName("swapComments")
    private List<SwapComment> swapComments;

    public List<SwapComment> getSwapComments() {
        return swapComments;
    }

    public void setSwapComments(List<SwapComment> swapComments) {
        this.swapComments = swapComments;
    }

    public BookOwnerModel getBookOwner() {
        return bookOwner;
    }

    public void setBookOwner(BookOwnerModel bookOwner) {
        this.bookOwner = bookOwner;
    }

    public String getSwapDescription() {
        return swapDescription;
    }

    public void setSwapDescription(String swapDescription) {
        this.swapDescription = swapDescription;
    }

    public String getSwapTimeStamp() {
        return swapTimeStamp;
    }

    public void setSwapTimeStamp(String swapTimeStamp) {
        this.swapTimeStamp = swapTimeStamp;
    }

    public float getSwapPrice() {
        return swapPrice;
    }

    public void setSwapPrice(float swapPrice) {
        this.swapPrice = swapPrice;
    }

    public int getSwapDetailId() {
        return swapDetailId;
    }

    public void setSwapDetailId(int swapDetailId) {
        this.swapDetailId = swapDetailId;
    }
}
