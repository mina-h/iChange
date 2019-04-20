package com.example.ichange;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter{

    private final View mWindow;
    private Context mContext;




    public CustomInfoWindowAdapter(Context mContext) {
        this.mContext = mContext;
        mWindow = LayoutInflater.from(mContext).inflate(R.layout.custom_info_window, null);
    }

    private void rendoWindowText(Marker marker, View view){


        Upload upload = (Upload) marker.getTag(); //in bayad yeja Dge sakhte she fekr konam mmm
        String title = upload.getName(); //marker.getTitle();
        TextView tvTitle =  view.findViewById(R.id.info_marker_title);
        if(!title.equals("")){
            tvTitle.setText(title);
        }

        String exchange = upload.getmXchange();//marker.getTitle();
        TextView tvExchange =  view.findViewById(R.id.exchange);
        if(!exchange.equals("")){
            tvExchange.setText(exchange);


            ImageView imageView = view.findViewById(R.id.info_marker_image);

            if ( marker.getZIndex() >= 0 )
            Picasso.with(mContext)
                    .load(upload.getImageUrl())
                    .fit()
                    //   .centerCrop()
                    .into(imageView);
            else {
                Picasso.with(mContext)
                        .load(upload.getImageUrl())
                        .fit()
                        //   .centerCrop()
                        .into(imageView, new InfoWindowRefresher(marker));
            }
        }

    }

    @Override
    public View getInfoWindow(Marker marker) {


        rendoWindowText(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        rendoWindowText(marker, mWindow);

        return mWindow;
    }
}
