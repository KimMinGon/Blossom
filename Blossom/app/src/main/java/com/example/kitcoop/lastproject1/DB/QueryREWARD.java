package com.example.kitcoop.lastproject1.DB;

/**
 * Created by kitcoop on 2017-04-14.
 */

public class QueryREWARD extends DataForQuery {
    private int Reward;
    private int CurrLevel;
    private int TPoint;

    public QueryREWARD(Integer Seq, Integer Usernumber, String useraccount, String Date, int reward, int currLevel, int TPoint) {
        DefalutDATA(Seq, Usernumber, useraccount, null, Date);

        this.Reward = reward;
        this.CurrLevel = currLevel;
        this.TPoint = TPoint;
    }

    public int getReward() {
        return Reward;
    }

    public void setReward(int reward) {
        Reward = reward;
    }

    public int getCurrLevel() {
        return CurrLevel;
    }

    public void setCurrLevel(int currLevel) {
        CurrLevel = currLevel;
    }

    public int getTPoint() {
        return TPoint;
    }

    public void setTPoint(int TPoint) {
        this.TPoint = TPoint;
    }
}
