package com.mrswagbhinav.trapapp;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

public class Trap {

    private String title;
    private String host;
    private String locationName;
    private String locationAddress;
    private Timestamp timestamp;
    private GeoPoint geoPoint;
    private String id;


    public Trap (String title, String host, String locationName, String locationAddress, Timestamp timestamp, GeoPoint geoPoint, String id) {
        this.title = title;
        this.host = host;
        this.locationName = locationName;
        this.locationAddress = locationAddress;
        this.timestamp = timestamp;
        this.geoPoint = geoPoint;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getHost() {
        return host;
    }

    public String getLocationName() {
        return locationName;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public Double getLat() {
        return geoPoint.getLatitude();
    }

    public Double getLng() {
        return  geoPoint.getLongitude();
    }

    public String getId() {
        return id;
    }

}
