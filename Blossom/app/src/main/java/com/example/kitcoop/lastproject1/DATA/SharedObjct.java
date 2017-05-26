package com.example.kitcoop.lastproject1.DATA;

import android.content.SharedPreferences;

import com.example.kitcoop.lastproject1.DB.DataForQuery;
import com.example.kitcoop.lastproject1.DB.ShareDBclass;
import com.example.kitcoop.lastproject1.DB.ShareSetting;
import com.example.kitcoop.lastproject1.MenuItem.MyItem;
import com.example.kitcoop.lastproject1.MenuItem.MyListAdapter;
import com.example.kitcoop.lastproject1.R;

import java.util.ArrayList;

/**
 * Created by kitcoop on 2017-04-14.
 */

public interface SharedObjct {
    ShareDBclass SHARD_DB = new ShareDBclass();
    ShareSetting SHARE_SETTING = new ShareSetting();

    ArrayList<MyItem> myItems = new ArrayList<>();

    MyItem myItem1 = new MyItem(R.drawable.profile);
    MyItem myItem2 = new MyItem(R.drawable.ssak_drawer);
    MyItem myItem3 = new MyItem(R.drawable.measure_drawer);
    MyItem myItem4 = new MyItem(R.drawable.gps_drawer);
    MyItem myItem5 = new MyItem(R.drawable.ana_drawer);

    MyListAdapter myListadapter = new MyListAdapter();

    public static final DataForQuery loginUserData = new DataForQuery();


}
