package com.example.kitcoop.lastproject1.DB;

/**
 * Created by kitcoop on 2017-04-14.
 */

public class QuerySSAK extends DataForQuery {
    private int exp_today_water;
    private int exp_today_walk;
    private int total_exp;
    private int level;

    public QuerySSAK(Integer Seq, Integer Usernumber, String useraccount, String Date
            , int exp_today_water , int exp_today_walk , int total_exp , int level) {
        DefalutDATA(Seq, Usernumber, useraccount, null, Date);

        this.exp_today_water = exp_today_water;
        this.exp_today_walk = exp_today_walk;
        this.total_exp = total_exp;
        this.level = level;
    }

    public int getExp_today_water() {
        return exp_today_water;
    }

    public void setExp_today_water(int exp_today_water) {
        this.exp_today_water = exp_today_water;
    }

    public int getExp_today_walk() {
        return exp_today_walk;
    }

    public void setExp_today_walk(int exp_today_walk) {
        this.exp_today_walk = exp_today_walk;
    }

    public int getTotal_exp() {
        return total_exp;
    }

    public void setTotal_exp(int total_exp) {
        this.total_exp = total_exp;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
