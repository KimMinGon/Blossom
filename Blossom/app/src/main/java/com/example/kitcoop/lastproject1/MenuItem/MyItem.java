package com.example.kitcoop.lastproject1.MenuItem;

import android.graphics.Bitmap;

/**
 * Created by kitcoop on 2017-03-09.
 */

// 한 행의 데이터에 관련된 bean
public class MyItem {
    private int icon = 0;
    private Bitmap Bicon;

    // 단축키 Alt + Insert

    public MyItem(int icon) {
        this.icon = icon;
    }

    public MyItem(Bitmap icon2) {
        this.Bicon = Bicon;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public Bitmap getBicon() {
        return Bicon;
    }

    public void setBicon(Bitmap Bicon) {
        this.Bicon = Bicon;
    }


}
