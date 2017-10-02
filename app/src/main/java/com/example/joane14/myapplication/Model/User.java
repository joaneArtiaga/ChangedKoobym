package com.example.joane14.myapplication.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Joane14 on 26/07/2017.
 */

public class User implements Serializable{

    @SerializedName("genres")
    private List<GenreModel> genreArray;

    @SerializedName("locations")
    private List<LocationModel> locationArray;

    @SerializedName("userDayTimes")
    private List<UserDayTime> dayTimeModel;

    private Integer userId;
    private String userFname;
    private String userLname;
    private String username;
    private String password;
    private String phoneNumber;
    private String address;
    private Date birthdate;
    private String email;
    private String imageFilename;


    public List<UserDayTime> getDayTimeModel() {
        return dayTimeModel;
    }

    public void setDayTimeModel(List<UserDayTime> dayTimeModel) {
        this.dayTimeModel = dayTimeModel;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setLocationArray(List<LocationModel> locationArray) {
        this.locationArray = locationArray;
    }

    public List<LocationModel> getLocationArray() {
        return locationArray;
    }


    public void setGenreArray(List<GenreModel> genreArray) {
        this.genreArray = genreArray;
    }

    public List<GenreModel> getGenreArray() {
        return genreArray;
    }



    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserFname() {
        return userFname;
    }

    public void setUserFname(String userFname) {
        this.userFname = userFname;
    }

    public String getUserLname() {
        return userLname;
    }

    public void setUserLname(String userLname) {
        this.userLname = userLname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageFilename() {
        return imageFilename;
    }

    public void setImageFilename(String imageFilename) {
        this.imageFilename = imageFilename;
    }

    @Override
    public String toString() {
        return "User{" +
                "genreArray=" + genreArray +
                ", dayTimeArray='" + dayTimeModel + '\'' +
                ", userId=" + userId +
                ", userFname='" + userFname + '\'' +
                ", userLname='" + userLname + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", birthdate=" + birthdate +
                ", email='" + email + '\'' +
                ", imageFilename='" + imageFilename + '\'' +
                '}';
    }
}
