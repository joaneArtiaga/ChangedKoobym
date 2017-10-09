package com.example.joane14.myapplication.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Joane14 on 06/10/2017.
 */

public class SwapComment implements Serializable {

    @SerializedName("swapCommentId")
    private int swapCommentId;

    @SerializedName("user")
    private User user;

    @SerializedName("swapComment")
    private String swapComment;


    public int getSwapCommentId() {
        return swapCommentId;
    }

    public void setSwapCommentId(int swapCommentId) {
        this.swapCommentId = swapCommentId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSwapComment() {
        return swapComment;
    }

    public void setSwapComment(String swapComment) {
        this.swapComment = swapComment;
    }


    @Override
    public String toString() {
        return "SwapComment{" +
                "User=" + user.toString() +
                ", swapComment='" + swapComment + '\'' +
                '}';
    }

}
