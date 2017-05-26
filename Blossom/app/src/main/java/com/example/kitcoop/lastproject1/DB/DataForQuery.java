package com.example.kitcoop.lastproject1.DB;

import android.graphics.Bitmap;

/**
 * Created by kitcoop on 2017-04-14.
 */

public class DataForQuery {
    private Bitmap cropped_profile;
    private Integer Seq;
    private Integer Usernumber;
    private String userAccount;
    private String nickname;
    private String Date;
    private boolean IsGoogleLogin = false;



    public void DefalutDATA(Integer Seq, Integer Usernumber, String userAccount, String nickname, String Date){
        this.Seq = Seq;
        this.Usernumber = Usernumber;
        this.userAccount = userAccount;
        this.nickname = nickname;
        this.Date = Date;
    }

    public Bitmap getCropped_profile() {
        return cropped_profile;
    }

    public void setCropped_profile(Bitmap cropped_profile) {
        this.cropped_profile = cropped_profile;

    }

    public Integer getSeq() {
        return Seq;
    }

    public void setSeq(Integer seq) {
        Seq = seq;
    }

    public Integer getUsernumber() {
        return Usernumber;
    }

    public void setUsernumber(Integer usernumber) {
        Usernumber = usernumber;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean isGoogleLogin() { return IsGoogleLogin; }

    public void setGoogleLogin(boolean IsGoogleLogin) { this.IsGoogleLogin = IsGoogleLogin; }
}
