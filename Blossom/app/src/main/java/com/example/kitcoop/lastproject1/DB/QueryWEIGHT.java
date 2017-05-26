package com.example.kitcoop.lastproject1.DB;

/**
 * Created by kitcoop on 2017-04-14.
 */

public class QueryWEIGHT extends DataForQuery {
    private float Weight;

    public QueryWEIGHT(Integer Seq, Integer Usernumber, String useraccount, String Date, float weight) {
        DefalutDATA(Seq, Usernumber, useraccount, null, Date);
        this.Weight = weight;
    }

    public float getWeight() {
        return Weight;
    }

    public void setWeight(float weight) {
        Weight = weight;
    }
}
