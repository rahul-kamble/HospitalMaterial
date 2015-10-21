package com.example.root.myapplication.ModelClass;

/**
 * Created by root on 16/10/15.
 */
public class LatLong {
    private double lattitude;
    private double longitude;
    private int userId;

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getLattitude() {

        return lattitude;
    }

    public void setLattitude(double lattitude) {
        this.lattitude = lattitude;
    }
}