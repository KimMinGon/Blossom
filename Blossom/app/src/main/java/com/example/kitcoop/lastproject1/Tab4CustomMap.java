package com.example.kitcoop.lastproject1;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Created by kitcoop on 2017-03-28.
 */

public class Tab4CustomMap extends FragmentActivity {
    private GoogleMap googleMap;
    private PolylineOptions polylineOptions;
    private Polyline polyline;
    private LatLng pt;
    private String[] gpsInfo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab4custommap);

        polylineOptions = new PolylineOptions();

        Intent intent = getIntent();
        gpsInfo = intent.getStringExtra("gpsInfo").split("/");


        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment)fragmentManager.findFragmentById(R.id.tab4mapFragment);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                Tab4CustomMap.this.googleMap = googleMap;
                Tab4CustomMap.this.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                System.out.println(gpsInfo.length);
                for(int i=0 ; i<gpsInfo.length / 2 ; i++) {
                    float latitude = Float.parseFloat(gpsInfo[i * 2]);
                    float longitude = Float.parseFloat(gpsInfo[i*2 + 1]);
                    pt = new LatLng(latitude, longitude);
                    polylineOptions.add(pt);
                }




                Polyline polyline = Tab4CustomMap.this.googleMap.addPolyline(polylineOptions);
                polyline.setColor(Color.BLUE);

                CameraPosition cp = new CameraPosition.Builder().target((pt)).zoom(16).build();
                Tab4CustomMap.this.googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cp));
            }
        });

    }
}
