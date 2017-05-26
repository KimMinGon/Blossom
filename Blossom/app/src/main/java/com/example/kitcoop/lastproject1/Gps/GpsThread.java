package com.example.kitcoop.lastproject1.Gps;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kitcoop on 2017-04-17.
 */

public class GpsThread implements Runnable {

    public Handler backHandler;
    private Handler mainHandler;
    private Context context;
    private LocationManager locationManager;
    private String provider;
    private LatLng pt;
    private double bef_latitude, bef_longitude;
    private long bef_time;
    private int flag;
    private int distance;
    private String sTime;
    private String line;

    public GpsThread(Context context, Handler mainHandler) {
        this.context = context;
        this.mainHandler = mainHandler;
        this.flag = 1;
    }

    @Override
    public void run() {
        Looper.prepare();
        backHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch(msg.what) {
                    case 0:
                        line = "";
                        locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
                        provider = locationManager.GPS_PROVIDER;
                        locationManager.requestLocationUpdates(provider, 3000, 10, locationListener);
                        break;
                    case 1:
                        locationManager.removeUpdates(locationListener);
                        break;
                    case 2:
                        Message mainMsg = Message.obtain();
                        mainMsg.what = 4;
                        mainMsg.obj = line;

                        mainHandler.sendMessage(mainMsg);
                        break;
                }

            }
        };
        Looper.loop();
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            double cur_latitude = location.getLatitude();
            double cur_longitude = location.getLongitude();
            long cur_time = System.currentTimeMillis();

            line += (cur_latitude + "/" + cur_longitude + "/");
            System.out.println(line);
            pt = new LatLng(cur_latitude, cur_longitude);

            // 2번째 호출 부터 실행
            if (flag >= 2) {
                GpsInfo gpsInfo = new GpsInfo(bef_latitude, bef_longitude, cur_latitude, cur_longitude, bef_time, cur_time);
                distance += (int) gpsInfo.getDistance();

                Message msg = Message.obtain();
                msg.what = 2;
                msg.arg1 = distance;
                msg.obj = pt;

                mainHandler.sendMessage(msg);
            } else {
                SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                sTime = dayTime.format(new Date(cur_time));

                Message msg = Message.obtain();
                msg.what = 3;
                msg.obj = sTime;
                mainHandler.sendMessage(msg);
            }

            // 이전 위도, 경도, 시간 저장
            bef_latitude = cur_latitude;
            bef_longitude = cur_longitude;
            bef_time = cur_time;

            flag++;
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
}


