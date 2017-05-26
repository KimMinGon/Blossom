package com.example.kitcoop.lastproject1.DB;

/**
 * Created by kitcoop on 2017-04-14.
 */

public class QueryGPS extends DataForQuery {
    private float Distance;
    private String Stime;
    private String Ttime;
    private String MAP_imgfile_name;
    private String GPSpath;

    public QueryGPS(Integer Seq, Integer Usernumber, String useraccount, String Date, float distance, String stime, String ttime, String MAP_imgfile_name, String GPSpath) {
        DefalutDATA(Seq, Usernumber, useraccount, null, Date);

        this.Distance = distance;
        this.Stime = stime;
        this.Ttime = ttime;
        this.MAP_imgfile_name = MAP_imgfile_name;
        this.GPSpath = GPSpath;
    }

    public String getGPSpath() {
        return GPSpath;
    }

    public void setGPSpath(String GPSpath) {
        this.GPSpath = GPSpath;
    }

    public String getMAP_imgfile_name() {
        return MAP_imgfile_name;
    }

    public void setMAP_imgfile_name(String MAP_imgfile_name) {
        this.MAP_imgfile_name = MAP_imgfile_name;
    }

    public float getDistance() {
        return Distance;
    }

    public void setDistance(float distance) {
        Distance = distance;
    }

    public String getStime() {
        return Stime;
    }

    public void setStime(String stime) {
        Stime = stime;
    }

    public String getTtime() {
        return Ttime;
    }

    public void setTtime(String ttime) {
        Ttime = ttime;
    }
}
