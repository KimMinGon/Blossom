package com.example.kitcoop.lastproject1;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kitcoop.lastproject1.DATA.SharedObjct;
import com.example.kitcoop.lastproject1.DB.DBHelper;
import com.example.kitcoop.lastproject1.DB.DataForQuery;
import com.example.kitcoop.lastproject1.JClass.GoogleLogin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import static com.example.kitcoop.lastproject1.DATA.SharedConstant.today_hms_sdf;

public class Activity_Join extends FragmentActivity implements SharedObjct{

    private EditText id;
    private EditText pw;
    private EditText pwCheck;
    private EditText nickName;
    String result;

    GoogleLogin googleJoin;
    DataForQuery googleJoinData;

    final postData pd = new postData(new Handler() {
        @Override
        public void handleMessage(Message msg) {

            result= ((String)msg.obj).trim();
            System.out.println(result);

            if (result.equals("dupli")) {
                //System.out.println(result);
                Toast.makeText(Activity_Join.this, "현재 아이디는 사용중입니다.", Toast.LENGTH_SHORT).show();
            } else if (result.equals("fail")) {
                Toast.makeText(Activity_Join.this, "입력에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            } else if ((result.split("/"))[0].equals("success")) {

                Toast.makeText(Activity_Join.this, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();

                //가입시키고 난 다음에 바로 sqlight의 정보로 넣는다.
                //////////////////
                DBHelper dbHelper = SHARD_DB.getDbHelper();

                Integer usernumber = Integer.parseInt(result.split("/")[1]);
                dbHelper.insert_A_useraccount(usernumber, id.getText().toString()
                        ,nickName.getText().toString(), today_hms_sdf.format(new Date()));

                //잘들어갔는지 출력 test
               /* ArrayList<DataForQuery> alluseraccount = dbHelper.selectAlluserAccount(true, null);

                for(int i = 0 ; i < alluseraccount.size() ; i++) {
                    System.out.println(i + " : " + alluseraccount.get(i).getUsernumber() + " : " + alluseraccount.get(i).getUserAccount());
                }*/
                ////////////////////


                /*Intent intent = new Intent(Activity_Join.this, Activity_Login.class);
                startActivity(intent);
                */
            }
            finish();
        }

    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);


        id = (EditText)findViewById(R.id.join_Id);
        pw = (EditText)findViewById(R.id.join_Password);
        pwCheck = (EditText)findViewById(R.id.join_PasswordCheck);
        nickName = (EditText)findViewById(R.id.join_Nickname);


        pd.setDaemon(true);
        pd.start();

        findViewById(R.id.join_Join).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONObject jsonObject = new JSONObject();
                JSONArray jsonArray = new JSONArray();
                try {

                    jsonObject.put("id", id.getText().toString());
                    jsonObject.put("pw", pw.getText().toString());
                    jsonObject.put("pwCheck", pwCheck.getText().toString());
                    jsonObject.put("nickName", nickName.getText().toString());

                    if (jsonObject.get("id").equals("")) {
                        Toast.makeText(Activity_Join.this, "아이디를 입력하세요.", Toast.LENGTH_SHORT).show();
                    } else if (jsonObject.get("pw").equals("")) {
                        Toast.makeText(Activity_Join.this, "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                    } else if (jsonObject.get("pwCheck").equals("")) {
                        Toast.makeText(Activity_Join.this, "비밀번호 확인을 입력하세요.", Toast.LENGTH_SHORT).show();
                    } else if (jsonObject.get("nickName").equals("")) {
                        Toast.makeText(Activity_Join.this, "닉네임을 입력하세요.", Toast.LENGTH_SHORT).show();
                    } else if (jsonObject.get("pw").toString().length() < 8) {
                        Toast.makeText(Activity_Join.this, "비밀번호를 8자 이상 입력하세요.", Toast.LENGTH_SHORT).show();
                    } else if (!jsonObject.get("pw").equals(jsonObject.get("pwCheck"))) {
                        Toast.makeText(Activity_Join.this, "비밀번호와 비밀번호 확인이 다릅니다", Toast.LENGTH_SHORT).show();
                    } else {

                        jsonArray.put(jsonObject);

                        Message msg = Message.obtain();
                        msg.what = 1;
                        msg.obj = jsonArray.toString();

                        pd.backHandler.sendMessage(msg);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Activity_Join.this, "에러", Toast.LENGTH_SHORT).show();
                }

            }
        });


        findViewById(R.id.join_SignGoogle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleJoin = new GoogleLogin(Activity_Join.this, R.id.join_SignGoogle);
                googleJoin.signIn(Activity_Join.this);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(googleJoin != null){
            googleJoinData = googleJoin.for_onActivityResult(requestCode, resultCode, data);
            if(googleJoinData != null) {
                Log.d("GoogleJoin", "Sign in 성공");

                ///////////
                if( googleJoinData != null) {
                    Log.d("GoogleJoin : ", googleJoinData.getUserAccount() + " / " + googleJoinData.getNickname());
                    googleJoin.signOut();

                    JSONObject jsonObject = new JSONObject();
                    JSONArray jsonArray = new JSONArray();

                    try {
                        jsonObject.put("id", googleJoinData.getUserAccount());
                        jsonObject.put("pw", "google");
                        jsonObject.put("pwCheck", "google");
                        jsonObject.put("nickName", googleJoinData.getNickname());
                        jsonArray.put(jsonObject);

                        Message msg = Message.obtain();
                        msg.what = 1;
                        msg.obj = jsonArray.toString();

                        pd.backHandler.sendMessage(msg);

                    } catch (JSONException e1) {
                        e1.printStackTrace();
                        Toast.makeText(Activity_Join.this, "에러", Toast.LENGTH_SHORT).show();
                    }
                }
                ///////////////
            }
            else
                Log.d("GoogleJoin", "읽어오기 실패");
        }
        else {
            Log.d("GoogleJoin", "계정으로 가입 실패");
        }
    }

    /*Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            result= ((String)msg.obj).trim();
            System.out.println(result);

            if (result.equals("dupli")) {
                //System.out.println(result);
                Toast.makeText(Activity_Join.this, "현재 아이디는 사용중입니다.", Toast.LENGTH_SHORT).show();
            } else if (result.equals("fail")) {
                Toast.makeText(Activity_Join.this, "입력에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            } else if ((result.split("/"))[0].equals("success")) {

                Toast.makeText(Activity_Join.this, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();


                //가입시키고 난 다음에 바로 sqlight의 정보로 넣는다.
                //////////////////
                DBHelper dbHelper = SHARD_DB.getDbHelper();

                Integer usernumber = Integer.parseInt(result.split("/")[1]);
                dbHelper.insert_A_useraccount(usernumber, id.getText().toString()
                                                 ,nickName.getText().toString(), today_hms_sdf.format(new Date()));

                //잘들어갔는지 출력 test
               *//* ArrayList<DataForQuery> alluseraccount = dbHelper.selectAlluserAccount(true, null);

                for(int i = 0 ; i < alluseraccount.size() ; i++) {
                    System.out.println(i + " : " + alluseraccount.get(i).getUsernumber() + " : " + alluseraccount.get(i).getUserAccount());
                }*//*
                ////////////////////

                finish();
                *//*Intent intent = new Intent(Activity_Join.this, Activity_Login.class);
                startActivity(intent);
                *//*
            }
        }

    };*/


}
