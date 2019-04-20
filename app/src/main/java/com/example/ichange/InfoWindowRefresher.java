package com.example.ichange;

import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Callback;

public class InfoWindowRefresher implements Callback {
    private Marker markerToRefresh;

    public InfoWindowRefresher(Marker markerToRefresh) {
        this.markerToRefresh = markerToRefresh;
    }

    @Override
    public void onSuccess() {
        markerToRefresh.setZIndex(0); //
        markerToRefresh.showInfoWindow();
    }

    @Override
    public void onError() {}
}