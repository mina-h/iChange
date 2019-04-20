package com.example.ichange;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Upload {

    private String mName;
    private String mXchange;
    private String mImageUrl;
    private GeoPoint mGeopoint;
    private @ServerTimestamp Date timeStamp;
    @Exclude private String id;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public Upload() {
        //empty constructor needed

    }

    public Upload(String name, String imageUrl, String xchange, GeoPoint geoPoint) {

        if (name.trim().equals("")) {
            name = "No Name";
        }

        mName = name;
        mImageUrl = imageUrl;
        mXchange = xchange;
        mGeopoint = geoPoint;



    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public String getmXchange() {
        return mXchange;
    }

    public void setmXchange(String xchange) {
        this.mXchange = xchange;
    }


    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public GeoPoint getmGeopoint() {
        return mGeopoint;
    }

    public void setmGeopoint(GeoPoint mGeopoint) {
        this.mGeopoint = mGeopoint;
    }
}