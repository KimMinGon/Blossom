package com.example.kitcoop.lastproject1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.kitcoop.lastproject1.DATA.SharedConstant;
import com.example.kitcoop.lastproject1.DATA.SharedObjct;
import com.example.kitcoop.lastproject1.DB.DBHelper;

public class Activity_FirstMain extends Activity implements SharedConstant, SharedObjct {

    final int requestCode_Activity_Login = 123;
    final int requestCode_Activity_Loding = 321;

    sync sync1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SHARD_DB.makeDBHelper(getApplicationContext());
        final DBHelper dbHelper = SHARD_DB.getDbHelper();

        sync1 = new sync();


        findViewById(R.id.main_LoginBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_FirstMain.this, Activity_Login.class);
                startActivityForResult(intent, requestCode_Activity_Login);
            }
        });

        /*findViewById(R.id.main_GuestBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.CreateTestData_walk(100);  //100일치 넣기
                dbHelper.CreateTestData_water(100);
                dbHelper.CreateTestData_height(100, 164);
                dbHelper.CreateTestData_weight(100, 55);
                //test
                //dbHelper.delete(WALK, 1162);
            }
        });*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        System.out.println("오케이" + requestCode);
        if(resultCode==RESULT_OK && requestCode == requestCode_Activity_Login) {

           /* sync1.sync_data_from_db(HEIGHT);
            while(!sync1.get_finish){

            }*/

            Intent intent = new Intent(Activity_FirstMain.this, Activity_Drawer.class);
            startActivity(intent);
        }
         /*{   Intent intent = new Intent(Activity_FirstMain.this, Activity_Loading.class);
            startActivityForResult(intent, requestCode_Activity_Loding);



        }
        else if(resultCode==RESULT_OK && requestCode == requestCode_Activity_Loding){   //로딩이 끝나면
            System.out.println("시작");
            Intent intent = new Intent(Activity_FirstMain.this, Activity_Drawer.class);
            startActivity(intent);
        }*/
    }
}