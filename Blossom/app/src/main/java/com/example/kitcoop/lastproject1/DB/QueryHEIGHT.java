package com.example.kitcoop.lastproject1.DB;

/**
 * Created by kitcoop on 2017-04-14.
 */

public class QueryHEIGHT extends DataForQuery {
    private float Height ;

    public QueryHEIGHT(Integer Seq, Integer Usernumber, String useraccount, String Date, float height) {
        DefalutDATA(Seq, Usernumber, useraccount, null, Date);
        this.Height = height;
    }

    public float getHeight() {
        return Height;
    }

    public void setHeight(float height) {
        Height = height;
    }
}
