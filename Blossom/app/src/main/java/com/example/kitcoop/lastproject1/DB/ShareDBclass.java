package com.example.kitcoop.lastproject1.DB;

import android.content.Context;
import static com.example.kitcoop.lastproject1.DATA.SharedConstant.DB_NAME;

/**
 * Created by kitcoop on 2017-04-14.
 */

public class ShareDBclass {

    private DBHelper dbHelper;

    public void makeDBHelper(Context context){
       // dbHelper = dbHelper = new DBHelper(getApplicationContext(), "MoneyBook.db", null, 1);
        this.dbHelper = new DBHelper(context, DB_NAME, null, 1);
    }

    public DBHelper getDbHelper() {
        if (dbHelper!= null)
            return dbHelper;
        System.out.println("makeDBHelper 함수를 반드시 1회 사용해주세요...");
        return null;
    }

}
