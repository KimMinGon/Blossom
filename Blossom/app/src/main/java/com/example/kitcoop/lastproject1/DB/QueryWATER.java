package com.example.kitcoop.lastproject1.DB;

/**
 * Created by kitcoop on 2017-04-14.
 */

public class QueryWATER extends DataForQuery {
    private int Water;

    public QueryWATER(Integer Seq, Integer Usernumber, String useraccount, String Date, int water) {
        DefalutDATA(Seq, Usernumber, useraccount, null, Date);
        this.Water = water;
    }

    public int getWater() {
        return Water;
    }

    public void setWater(int water) {
        Water = water;
    }
}