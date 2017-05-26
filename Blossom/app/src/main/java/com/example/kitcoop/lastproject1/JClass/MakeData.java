package com.example.kitcoop.lastproject1.JClass;

import com.example.kitcoop.lastproject1.DATA.SharedConstant;
import com.example.kitcoop.lastproject1.DATA.SharedObjct;
import com.example.kitcoop.lastproject1.DB.DBHelper;

/**
 * Created by kitcoop on 2017-05-01.
 */

public class MakeData implements SharedObjct, SharedConstant {
    public MakeData(){
        DBHelper dbHelper = SHARD_DB.getDbHelper();

        dbHelper.CreateTestData_walk(100);
        dbHelper.CreateTestData_water(100);
        dbHelper.CreateTestData_weight(100, 50);
        dbHelper.CreateTestData_height(100, 164);

        dbHelper.CreateTestData_SSAK_Level();
        dbHelper.CreateTestData_REWARD_Level();
    }
}