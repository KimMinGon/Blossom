package com.example.kitcoop.lastproject1.DB;

/**
 * Created by kitcoop on 2017-04-14.
 */

public class QueryWALK extends DataForQuery {
    private int Walk ;

    public QueryWALK(Integer Seq, Integer Usernumber, String useraccount, String Date, int walk) {
        DefalutDATA(Seq, Usernumber, useraccount, null, Date);
        this.Walk = walk;
    }

    public int getWalk() {
        return Walk;
    }

    public void setWalk(int walk) {
        Walk = walk;
    }
}
