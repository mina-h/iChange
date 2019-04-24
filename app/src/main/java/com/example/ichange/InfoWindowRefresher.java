package com.example.ichange;

import android.util.Log;

import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Callback;

public class InfoWindowRefresher implements Callback {
    private Marker markerToRefresh;

    public InfoWindowRefresher(Marker markerToRefresh) {
        this.markerToRefresh = markerToRefresh;
    }

    @Override
    public void onSuccess() {

        Log.d("david", "rendoWindowText: success" + markerToRefresh.getZIndex());

        markerToRefresh.setZIndex(0); //
        markerToRefresh.showInfoWindow();
    }

    @Override
    public void onError() {
        Log.d("david", "rendoWindowText: error" + markerToRefresh.getZIndex());

    }
}