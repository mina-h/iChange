package com.example.ichange;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Upload {

    private String mName;
    private String mUser;
    private String mXchange;
    private String mImageUrl;
    private GeoPoint mGeopoint;
    private @ServerTimestamp Date timeStamp;
    @Exclude private String mId;


    public Upload() {
        //empty constructor needed
    }

    public Upload(String name, String user, String imageUrl, String xchange, GeoPoint geoPoint, String id) {

        if (name.trim().equals("")) {
            name = "No Name";
        }
        mName = name;
        mImageUrl = imageUrl;
        mXchange = xchange;
        mGeopoint = geoPoint;
        mId = id;
        mUser = user;
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

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }
    public String getmUser() {
        return mUser;
    }

    public void setmUser(String mUser) {
        this.mUser = mUser;
    }

}