package com.example.joane14.myapplication.Model;

/**
 * Created by Joane14 on 27/02/2018.
 */

public class Place {

    private String description;
    private String id;
    private double longitude;
    private double latitude;

    public Place() {
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public Place(String description, String id) {
        this.description = description;
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(String id) {
        this.id = id;
    }

}
