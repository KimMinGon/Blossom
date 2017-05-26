package com.example.kitcoop.lastproject1.Gps;

/**
 * Created by kitcoop on 2017-04-13.
 */

public class GpsInfo {

    private double beforeLatitude;
    private double beforeLongitude;
    private double afterLatitude;
    private double afterLongitude;
    private long beforeTime;
    private long afterTime;
    private double theta;
    private double dist;

    public GpsInfo(double beforeLatitude, double beforeLongitude, double afterLatitude, double afterLongitude, long beforeTime, long afterTime) {
        this.beforeLatitude = beforeLatitude;
        this.beforeLongitude = beforeLongitude;
        this.afterLatitude = afterLatitude;
        this.afterLongitude = afterLongitude;
        this.beforeTime = beforeTime;
        this.afterTime = afterTime;
    }

    public double getDistance() {
        theta = beforeLongitude - afterLongitude;
        dist = Math.sin(deg2rad(beforeLatitude))
                * Math.sin(deg2rad(afterLatitude))
                + Math.cos(deg2rad(beforeLatitude))
                * Math.cos(deg2rad(afterLatitude))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);

        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;
        dist = dist * 1000.0;
        return dist;

    }

    private double deg2rad(double deg) {
        return (double)(deg * Math.PI / (double)180d);
    }

    private double rad2deg(double rad) {
        return (double)(rad * (double)180d / Math.PI);
    }

}
