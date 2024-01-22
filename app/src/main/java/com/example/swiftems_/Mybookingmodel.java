package com.example.swiftems_;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;

public class Mybookingmodel implements Serializable {
    GeoPoint address,ambulancelocation;
    Timestamp timestamp;
    String ambulancearea,ambulancename,docId;


    public Mybookingmodel() {
    }

    public Mybookingmodel(GeoPoint address, GeoPoint ambulancelocation, Timestamp timestamp, String ambulancearea, String ambulancename, String docId) {
        this.address = address;
        this.ambulancelocation = ambulancelocation;
        this.timestamp = timestamp;
        this.ambulancearea = ambulancearea;
        this.ambulancename = ambulancename;
        this.docId = docId;
    }


    public GeoPoint getAddress() {
        return address;
    }

    public void setAddress(GeoPoint address) {
        this.address = address;
    }

    public GeoPoint getAmbulancelocation() {
        return ambulancelocation;
    }

    public void setAmbulancelocation(GeoPoint ambulancelocation) {
        this.ambulancelocation = ambulancelocation;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getAmbulancearea() {
        return ambulancearea;
    }

    public void setAmbulancearea(String ambulancearea) {
        this.ambulancearea = ambulancearea;
    }

    public String getAmbulancename() {
        return ambulancename;
    }

    public void setAmbulancename(String ambulancename) {
        this.ambulancename = ambulancename;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }
}
