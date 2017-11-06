package com.example.joane14.myapplication.Model;

import java.io.Serializable;

/**
 * Created by Joane14 on 29/10/2017.
 */

public class UserNotification implements Serializable {

    private int userNotificationId;
    private User user;
    private int actionId;
    private String actionName;
    private String actionStatus;
    private User userPerformer;
    private BookOwnerModel bookActionPerformedOn;
    private Boolean read;


    public int getUserNotificationId() {
        return userNotificationId;
    }

    public void setUserNotificationId(int userNotificationId) {
        this.userNotificationId = userNotificationId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getActionId() {
        return actionId;
    }

    public void setActionId(int actionId) {
        this.actionId = actionId;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(String actionStatus) {
        this.actionStatus = actionStatus;
    }

    public User getUserPerformer() {
        return userPerformer;
    }

    public void setUserPerformer(User userPerformer) {
        this.userPerformer = userPerformer;
    }

    public BookOwnerModel getBookActionPerformedOn() {
        return bookActionPerformedOn;
    }

    public void setBookActionPerformedOn(BookOwnerModel bookActionPerformedOn) {
        this.bookActionPerformedOn = bookActionPerformedOn;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }
}
