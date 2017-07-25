package com.example.joane14.myapplication.Model;

/**
 * Created by Joane14 on 25/07/2017.
 */

public class FbUSer {
    public String email, name, userId, gender;

    public FbUSer(){

    }

    public FbUSer(String email, String name, String userId, String gender){
        this.email = email;
        this.name = name;
        this.userId = userId;
        this.gender = gender;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getUserId() {
        return userId;
    }

    public String getGender() {
        return gender;
    }
}
