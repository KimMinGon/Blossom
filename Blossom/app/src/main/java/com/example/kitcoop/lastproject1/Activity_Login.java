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

public class Activity_Login extends FragmentActivity implements SharedObjct, SharedConstant {

    private EditText id;
    private EditText pw;
    String result;
    boolean forauto = false;


    sync sync1 ;
    GoogleLogin googleLogin;
    DataForQuery googleLoginData;




    final postDataLogin pdl = new postDataLogin(Activity_Login.this, new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // System.out.println(result);
            result= ((String)msg.obj).trim();
            System.out.println("Test: " + result);
            if (result.equals("noexist")) {
                // System.out.println(result);
                Toast.makeText(Activity_Login.this, "입력한 아이디가 없습니다.", Toast.LENGTH_SHORT).show();
            } else if (result.equals("diff")) {
                Toast.makeText(Activity_Login.this, "비밀번호가 다릅니다.", Toast.LENGTH_SHORT).show();
            } else if ((result.split("/"))[0].equals("success")) {
                if(forauto)
                    Toast.makeText(Activity_Login.this, "자동 로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(Activity_Login.this, "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show();


                DBHelper dbHelper = SHARD_DB.getDbHelper();

                Integer usernumber = Integer.parseInt(result.split("/")[1]);
                String nickName = result.split("/")[2];
                ArrayList<DataForQuery> existUserData = dbHelper.selectUserAccount(false, usernumber);

                //만약 sqlight에 기존에 로그인한 정보갖 존재하면, 접속 일시를 업데이트하고
                //그렇지 않다면 계정 로그인 정보를 유저 테이블에 추가
                if (existUserData.size() == 1)
                    dbHelper.updateUserLastAccess(usernumber, today_hms_sdf.format(new Date()));
                else
                    dbHelper.insert_A_useraccount(usernumber, id.getText().toString(), nickName, today_hms_sdf.format(new Date()));

                loginUserData.DefalutDATA(null, usernumber, id.getText().toString(), nickName, null);

                //로그인을 성공하면
                //오토로그인 여부를 체크해서 저장
                SharedPreferences setting = getSharedPreferences("setting", 0);
                SharedPreferences.Editor editor = setting.edit();

                CheckBox autologin = (CheckBox)findViewById(R.id.login_AutoCheckBox);
                if(autologin.isChecked()) {
                    editor.putString("ID", id.getText().toString());
                    editor.putString("PW", pw.getText().toString());
                    editor.putBoolean("Auto_Login_enabled", true);
                    editor.commit();
                } else {
                    editor.remove("ID");
                    editor.remove("PW");
                    editor.putBoolean("Auto_Login_enabled", false);
                    editor.commit();
                }

                //데이터 백업해옴




                Intent intent = getIntent(); // 이 액티비티를 시작하게 한 인텐트를 호출
                //intent.putExtra("loginResult", "loginSuccess"); //드로어로 넘어가도록
                setResult(RESULT_OK, intent); // 추가 정보를 넣은 후 다시 인텐트를 반환합니다.

                finish(); // 액티비티 종료

            }
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        id = (EditText)findViewById(R.id.login_Email);
        pw = (EditText)findViewById(R.id.login_Password);

        pdl.setDaemon(true);
        pdl.start();


        sync1 = new sync();

        findViewById(R.id.login_Check).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject = new JSONObject();
                JSONArray jsonArray = new JSONArray();

                try {

                    jsonObject.put("google", "false");

                    jsonObject.put("id", id.getText().toString());
                    jsonObject.put("pw", pw.getText().toString());

                    System.out.println("로그인시도" + id.getText().toString());
                    System.out.println("로그인시도" +pw.getText().toString());

                    jsonArray.put(jsonObject);

                    Message msg = Message.obtain();
                    msg.what = 100;
                    msg.obj = jsonArray.toString();

                    while(pdl.backHandler == null){
                        System.out.println("백핸들러가 준비되기를 기다림.");
                    }
                    pdl.backHandler.sendMessage(msg);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });




        findViewById(R.id.login_GoogleLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleLogin = new GoogleLogin(Activity_Login.this, R.id.login_GoogleLogin);
                googleLogin.signIn(Activity_Login.this);
            }
        });

        findViewById(R.id.login_Join).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Login.this, Activity_Join.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.login_LookForPassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Login.this, Activity_LookForPassword.class);
                startActivity(intent);
            }
        });


        //자동로그인

        SharedPreferences setting = getSharedPreferences("setting", 0);
        SharedPreferences.Editor editor = setting.edit();

        CheckBox autologin = (CheckBox)findViewById(R.id.login_AutoCheckBox);
        if(setting.getBoolean("Auto_Login_enabled", false)) {       //뒤의 false는 기본값
            // . 설정이 true일경우에
            autologin.setChecked(true);
            id.setText(setting.getString("ID", "").toString());
            pw.setText(setting.getString("PW", "").toString());
            System.out.println("자동로그인 시도");

            forauto=true;

            ((Button)findViewById(R.id.login_Check)).performClick();
        } else {
            autologin.setChecked(false);
            //id.setText("");
            //pw.setText("");
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(googleLogin != null){
            googleLoginData = googleLogin.for_onActivityResult(requestCode, resultCode, data);
            if(googleLoginData != null) {
                Log.d("googleLogin", "Sign in 성공");

                ///////////
                if( googleLoginData != null) {
                    Log.d("googleLogin : ", googleLoginData.getUserAccount() + " / " + googleLoginData.getNickname());
                    googleLogin.signOut();

                    JSONObject jsonObject = new JSONObject();
                    JSONArray jsonArray = new JSONArray();

                    try {
                        jsonObject.put("google", "true");
                        jsonObject.put("id", googleLoginData.getUserAccount());
                        jsonObject.put("pw", "0000");          //패스워드로 특정문자가 들어오면

                        jsonArray.put(jsonObject);

                        Message msg = Message.obtain();
                        msg.what = 100;
                        msg.obj = jsonArray.toString();

                        pdl.backHandler.sendMessage(msg);

                    } catch (JSONException e1) {
                        e1.printStackTrace();
                        Toast.makeText(Activity_Login.this, "에러", Toast.LENGTH_SHORT).show();
                    }
                }
                ///////////////
            }
            else
                Log.d("googleLoginData", "읽어오기 실패");
        }
        else {
            Log.d("googleLogin", "계정연결실패");
        }
    }

   /* Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // System.out.println(result);
            result= ((String)msg.obj).trim();
            System.out.println("Test: " + result);
            if (result.equals("noexist")) {
               // System.out.println(result);
                Toast.makeText(Activity_Login.this, "입력한 아이디가 없습니다.", Toast.LENGTH_SHORT).show();
            } else if (result.equals("diff")) {
                Toast.makeText(Activity_Login.this, "비밀번호가 다릅니다.", Toast.LENGTH_SHORT).show();
            } else if ((result.split("/"))[0].equals("success")) {

                Toast.makeText(Activity_Login.this, "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show();


                DBHelper dbHelper = SHARD_DB.getDbHelper();

                Integer usernumber = Integer.parseInt(result.split("/")[1]);
                String nickName = result.split("/")[2];
                ArrayList<DataForQuery> existUserData = dbHelper.selectUserAccount(false, usernumber);

                //만약 sqlight에 기존에 로그인한 정보갖 존재하면, 접속 일시를 업데이트하고
                //그렇지 않다면 계정 로그인 정보를 유저 테이블에 추가
                if (existUserData.size() == 1)
                    dbHelper.updateUserLastAccess(usernumber, today_hms_sdf.format(new Date()));
                else
                    dbHelper.insert_A_useraccount(usernumber, id.getText().toString(), nickName, today_hms_sdf.format(new Date()));

                 loginUserData.DefalutDATA(null, usernumber, id.getText().toString(), nickName, null);

                //출력관련 테스트
              *//*  ArrayList<DataForQuery> alluseraccount = dbHelper.selectUserAccount(true, null);
                for(int i = 0 ; i < alluseraccount.size() ; i++) {

                }
                *//*

                *//*Intent intent = new Intent(Activity_Login.this, Activity_Drawer.class);
                startActivity(intent);*//*

                //버튼을 클릭하면
                Intent intent = getIntent(); // 이 액티비티를 시작하게 한 인텐트를 호출
                String loginResult = "login";
                intent.putExtra("loginResult", "loginSuccess");
                setResult(RESULT_OK, intent); // 추가 정보를 넣은 후 다시 인텐트를 반환합니다.

                finish(); // 액티비티 종료

            }
        }
    };*/
}
