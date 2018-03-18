package com.example.joane14.myapplication.Model;

/**
 * Created by Joane14 on 03/03/2018.
 */

public class SwapHeaderDetail {

    int swapHeaderDetailId;
    SwapDetail swapDetail;
    SwapHeader swapHeader;
    int swapHeaderId;
    String swapType;

    public int getSwapHeaderId() {
        return swapHeaderId;
    }

    public void setSwapHeaderId(int swapHeaderId) {
        this.swapHeaderId = swapHeaderId;
    }

    public int getSwapHeaderDetailId() {
        return swapHeaderDetailId;
    }

    public void setSwapHeaderDetailId(int swapHeaderDetailId) {
        this.swapHeaderDetailId = swapHeaderDetailId;
    }

    public SwapDetail getSwapDetail() {
        return swapDetail;
    }

    public void setSwapDetail(SwapDetail swapDetail) {
        this.swapDetail = swapDetail;
    }

    public SwapHeader getSwapHeader() {
        return swapHeader;
    }

    public void setSwapHeader(SwapHeader swapHeader) {
        this.swapHeader = swapHeader;
    }

    public String getSwapType() {
        return swapType;
    }

    public void setSwapType(String swapType) {
        this.swapType = swapType;
    }
}
