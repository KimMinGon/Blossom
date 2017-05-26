package com.example.kitcoop.lastproject1.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kitcoop.lastproject1.Activity_Drawer;
import com.example.kitcoop.lastproject1.DATA.SharedConstant;
import com.example.kitcoop.lastproject1.DATA.SharedObjct;
import com.example.kitcoop.lastproject1.DB.DBHelper;
import com.example.kitcoop.lastproject1.R;
import com.example.kitcoop.lastproject1.pedometerService.MyService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import static android.app.Activity.RESULT_OK;

/**
 * Created by kitcoop on 2017-04-12.
 */

public class CustomDialog extends Dialog implements SharedConstant, SharedObjct{

    DBHelper dbHelper = SHARD_DB.getDbHelper();
    Activity activity;
    String nick;


     public CustomDialog(@NonNull Context context) {
        super(context);
     }

    public void ShowDialog_setChallange(Activity activity)
    {
        final Activity activity2 = activity;
        final SharedPreferences setting = activity.getSharedPreferences("setting", 0);
        final SharedPreferences.Editor editor = setting.edit();

        LayoutInflater dialog = LayoutInflater.from(activity);
        final View dialogLayout = dialog.inflate(R.layout.dialog_set_challenge, null);
        final Dialog myDialog = new Dialog(activity);

        myDialog.setTitle("오늘의 목표 설정");
        myDialog.setContentView(dialogLayout);
        myDialog.show();


        ///////////////////
        final EditText walk = ((EditText)dialogLayout.findViewById(R.id.set_challenge_tx_walk));
        //Integer walk_i = Integer.parseInt(setting.getString("TODAY_WALK"+today_sdf.format(new Date()), setting.getString("WAKL", "10000")));
        Integer walk_i = Integer.parseInt(setting.getString("TODAY_C_WALK", "10000"));
        walk.setText(walk_i+"");

        final EditText water = ((EditText)dialogLayout.findViewById(R.id.set_challenge_tx_water));
        //Integer water_i = Integer.parseInt(setting.getString("TODAY_WATER"+today_sdf.format(new Date()), setting.getString("WATER", "1000")));
        Integer water_i = Integer.parseInt(setting.getString("TODAY_C_WATER",  "1000"));
        water.setText(water_i+"");


        /////////////



        Button btn_ok = (Button)dialogLayout.findViewById(R.id.set_challenge_btn_ok);
        Button btn_cancel = (Button)dialogLayout.findViewById(R.id.set_challenge_btn_cancel);

        btn_ok.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                /*editor.putInt("TODAY_WALK"+today_sdf.format(new Date()), Integer.parseInt(walk.getText().toString()));
                editor.putInt("TODAY_WATER"+today_sdf.format(new Date()), Integer.parseInt(water.getText().toString()));*/

                if(Integer.parseInt(walk.getText().toString()) < 1000 || Integer.parseInt(water.getText().toString()) < 1000)
                {
                    Toast.makeText(activity2, "모든 도전은 1000이하로 설정할 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
                else if(setting.getBoolean("CHALLENGE_WATER_TODAY_"+today_sdf.format(new Date()), false) || setting.getBoolean("CHALLENGE_WALK_TODAY_"+today_sdf.format(new Date()), false)){
                    //오늘의 챌린지가 성공하였을때는 조건을 변경할 수 없음.
                    Toast.makeText(activity2, "오늘의 챌린지가 성공하였을때는 조건을 변경할 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
                else {
                    editor.putString("TODAY_C_WALK", walk.getText().toString());
                    editor.putString("TODAY_C_WATER", water.getText().toString());
                    editor.commit();

                    final TextView walk2 = ((TextView) activity2.findViewById(R.id.challengeResult1));
                    final TextView water2 = ((TextView) activity2.findViewById(R.id.challengeResult2));

                    walk2.setText(" / " + setting.getString("TODAY_C_WALK", "10000") + " 걸음");
                    water2.setText(" / " + setting.getString("TODAY_C_WATER", "1000") + " mL");

                    myDialog.cancel();
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                myDialog.cancel();
            }
        });
    }

    public void ShowDialog_set_AdjustWater(final Activity activity)
    {

        final SharedPreferences setting = activity.getSharedPreferences("setting", 0);
        final SharedPreferences.Editor editor = setting.edit();

        LayoutInflater dialog = LayoutInflater.from(activity);
        final View dialogLayout = dialog.inflate(R.layout.dialog_set_addwater, null);
        final Dialog myDialog = new Dialog(activity);

        myDialog.setTitle("물 양 설정");
        myDialog.setContentView(dialogLayout);
        myDialog.show();

        Button btn_ok = (Button)dialogLayout.findViewById(R.id.set_adjust_water_btn_ok);
        Button btn_cancel = (Button)dialogLayout.findViewById(R.id.set_adjust_water_btn_cancel);
        EditText setting_water = (EditText)dialogLayout.findViewById(R.id.set_adjust_water);

        setting_water.setText(setting.getString("DAILY_WATER", "100"));

        btn_ok.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                EditText setting_water = (EditText)dialogLayout.findViewById(R.id.set_adjust_water);

                editor.putString("DAILY_WATER", setting_water.getText().toString());
                editor.commit();

                ////////

                ((TextView)activity.findViewById(R.id.addWater)).setText( setting.getString("DAILY_WATER", "0"));
                ((Button)activity.findViewById(R.id.measure_btn_Pwater)).setText( setting.getString("DAILY_WATER", "100") + "mL +");


                myDialog.cancel();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                myDialog.cancel();
            }
        });
    }


    public void ShowDialog_set_weightheight(final Activity activity, int WorH)
    {
        final int WorHx = WorH;

        LayoutInflater dialog = LayoutInflater.from(activity);
        final View dialogLayout = dialog.inflate(R.layout.dialog_set_weightheight, null);
        final Dialog myDialog = new Dialog(activity);

        final SharedPreferences setting = activity.getSharedPreferences("setting", 0);
        final SharedPreferences.Editor editor = setting.edit();

        if(WorH == 0) {
            myDialog.setTitle("오늘의 몸무게 입력하기");
        }
        else {
            myDialog.setTitle("오늘의 키 입력하기");
        }

        myDialog.setContentView(dialogLayout);
        myDialog.show();

        Button btn_ok = (Button)dialogLayout.findViewById(R.id.set_daily_weightheight_ok);
        Button btn_cancel = (Button)dialogLayout.findViewById(R.id.set_daily_weightheight_cancel);

        btn_ok.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //OK 누르면 할거
                TextView tv;
                EditText dt = (EditText)dialogLayout.findViewById(R.id.set_daily_weightheight_et);
                Float wh = Float.parseFloat( dt.getText() + "" );

                if(WorHx == 0) {
                    tv = ((TextView) activity.findViewById(R.id.f_measure_tv_weight));
                    dbHelper.updateTodayDATA(WEIGHT ,loginUserData.getUsernumber(), 0, wh, 0.0f, 0);
                    editor.putFloat("TODAY_WEIGHT", wh);

                } else {
                    tv = ((TextView) activity.findViewById(R.id.f_measure_tv_height));
                    dbHelper.updateTodayDATA(HEIGHT ,loginUserData.getUsernumber(), 0, 0.0f, wh, 0);
                    editor.putFloat("TODAY_HEIGHT", wh);
                }
                editor.commit();

                tv.setText(wh + "");
                myDialog.cancel();

            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                myDialog.cancel();
            }
        });
    }

    public void ChangeNickDialog(final Activity activity)
    {
        this.activity = activity;

        LayoutInflater dialog = LayoutInflater.from(activity);
        final View dialogLayout = dialog.inflate(R.layout.dialog_set_change_nick, null);
        final Dialog myDialog = new Dialog(activity);

        myDialog.setContentView(dialogLayout);

        myDialog.setTitle("닉네임 변경");
        myDialog.show();

        final EditText changed_nick = (EditText)dialogLayout.findViewById(R.id.set_et_change_nick);
        changed_nick.setText(loginUserData.getNickname());

        Button btn_ok = (Button)dialogLayout.findViewById(R.id.set_bt_change_nick_ok);
        Button btn_cancel = (Button)dialogLayout.findViewById(R.id.set_bt_change_nick_cancle);

        //OK 누르면 할거
        final postDataChangeNN pdn= new postDataChangeNN(handler);
        pdn.setDaemon(true);
        pdn.start();

        btn_ok.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                /////////////////////////////
                String changedNick = changed_nick.getText().toString();
                nick = changedNick;

                if(!changedNick.equals("")) {
                    loginUserData.setNickname(changedNick);
                    DBHelper dbHelper = SHARD_DB.getDbHelper();
                    dbHelper.updateUserNickname(loginUserData.getUsernumber(), changedNick);

                    dbHelper.selectUserAccount(false, loginUserData.getUsernumber());

                    //rf.returnToSetting(CustomF_ChangeNickName.this);

                    JSONObject jsonObject = new JSONObject();
                    JSONArray jsonArray = new JSONArray();

                    try {
                        jsonObject.put("usernumber" , loginUserData.getUsernumber());
                        jsonObject.put("nickname", changedNick);

                        jsonArray.put(jsonObject);

                        Message msg = Message.obtain();
                        msg.what = 500;
                        msg.obj = jsonArray.toString();

                        pdn.backHandler.sendMessage(msg);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    //토스트
                }

                if(activity.getClass().getName().contains("Drawer")){
                    ((TextView)activity.findViewById(R.id.f_profile_tv_nickname)).setText(changedNick);
                    loginUserData.setNickname(changedNick);
                }

                myDialog.cancel();
                /////////////////////////////////////
            }

        });

        btn_cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                myDialog.cancel();
            }
        });
    }


    public void ChangePWDialog(final Activity activity)
    {
        this.activity = activity;

        LayoutInflater dialog = LayoutInflater.from(activity);
        final View dialogLayout = dialog.inflate(R.layout.dialog_set_change_pw, null);
        final Dialog myDialog = new Dialog(activity);

        myDialog.setContentView(dialogLayout);

        myDialog.setTitle("패스워드 변경");
        myDialog.show();

        Button btn_ok = (Button)dialogLayout.findViewById(R.id.set_bt_change_pw_ok);
        Button btn_cancel = (Button)dialogLayout.findViewById(R.id.set_bt_change_pw_cancle);

        final EditText pw = (EditText)dialogLayout.findViewById(R.id.set_et_curr_pw);
        final EditText changepw = (EditText)dialogLayout.findViewById(R.id.set_et_new_pw1);
        final EditText changepw2 = (EditText)dialogLayout.findViewById(R.id.set_et_new_pw2);

        final postDataChangePW pdc= new postDataChangePW(handler2);
        pdc.setDaemon(true);
        pdc.start();

        btn_ok.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //OK 누르면 할거
                JSONObject jsonObject = new JSONObject();
                JSONArray jsonArray = new JSONArray();

                try {
                    jsonObject.put("usernumber" , loginUserData.getUsernumber());
                    jsonObject.put("password", pw.getText().toString());
                    jsonObject.put("changepw", changepw.getText().toString());

                    if(!changepw.getText().toString().equals(changepw2.getText().toString())){
                        Toast.makeText(activity, "새 비밀번호와 새 비밀번호 확인이 다릅니다", Toast.LENGTH_SHORT).show();
                    } else if (changepw.getText().toString().length() < 8) {
                        Toast.makeText(activity, "비밀번호를 8자 이상 입력하세요.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        jsonArray.put(jsonObject);

                        Message msg = Message.obtain();
                        msg.what = 400;
                        msg.obj = jsonArray.toString();

                        pdc.backHandler.sendMessage(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                myDialog.cancel();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                myDialog.cancel();
            }
        });
    }

    public void EXIT(final Activity activity)
    {
        this.activity = activity;

        LayoutInflater dialog = LayoutInflater.from(activity);
        final View dialogLayout = dialog.inflate(R.layout.dialog_set_exit, null);
        final Dialog myDialog = new Dialog(activity);

        myDialog.setContentView(dialogLayout);

        myDialog.setTitle("로그아웃 경고");
        myDialog.show();

        Button btn_ok = (Button)dialogLayout.findViewById(R.id.set_bt_exit_ok);
        Button btn_cancel = (Button)dialogLayout.findViewById(R.id.set_bt_exit_cancle);


        btn_ok.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //OK 누르면 할거
                Activity_Drawer.setServiceCheck(false);
                Intent sIntent = new Intent(activity, MyService.class);
                activity.stopService(sIntent);


                SharedPreferences setting = activity.getSharedPreferences("setting", 0);
                SharedPreferences.Editor editor = setting.edit();

                editor.remove("ID");
                editor.remove("PW");
                editor.putBoolean("Auto_Login_enabled", false);
                editor.commit();


                Intent intent = new Intent();
                if(intent != null) {
                    intent.putExtra("exit", true); //드로어로 넘어가도록
                }
                activity.setResult(RESULT_OK, intent); // 추가 정보를 넣은 후 다시 인텐트를 반환합니다.
                activity.finish();
                myDialog.cancel();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                myDialog.cancel();
            }
        });
    }


    String result ="";

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            result= ((String)msg.obj).trim();
            System.out.println(result);

            if(result.equals("fail")){
                Toast.makeText(activity, "닉네임 변경에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }else if(result.equals("success")){
                Toast.makeText(activity, "성공적으로 수정되었습니다.", Toast.LENGTH_SHORT).show();

                loginUserData.setNickname(nick);

            }
        }
    };

    Handler handler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            result= ((String)msg.obj).trim();
            System.out.println(result);
            System.out.println(activity);


            if(result.equals("fail")){
                Toast.makeText(activity, "현재 비밀번호가 틀립니다.", Toast.LENGTH_SHORT).show();
            }else if(result.equals("success")){
                Toast.makeText(activity, "성공적으로 수정되었습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    };
}
