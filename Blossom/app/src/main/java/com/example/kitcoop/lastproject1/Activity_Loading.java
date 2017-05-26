package com.example.kitcoop.lastproject1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kitcoop.lastproject1.DATA.SharedConstant;
import com.example.kitcoop.lastproject1.DATA.SharedObjct;
import com.example.kitcoop.lastproject1.DB.DBHelper;
import com.example.kitcoop.lastproject1.DB.DataForQuery;
import com.example.kitcoop.lastproject1.JClass.GoogleLogin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class Activity_Loading extends FragmentActivity implements SharedObjct, SharedConstant {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        System.out.println("나오나?");
        //sync1.sync_data_from_db(HEIGHT);
        //while(!sync1.get_finish){

        //}
        Intent intent = getIntent(); // 이 액티비티를 시작하게 한 인텐트를 호출
        //intent.putExtra("loginResult", "loginSuccess"); //드로어로 넘어가도록
        setResult(RESULT_OK, intent); // 추가 정보를 넣은 후 다시 인텐트를 반환합니다.
        finish();
    }

}
