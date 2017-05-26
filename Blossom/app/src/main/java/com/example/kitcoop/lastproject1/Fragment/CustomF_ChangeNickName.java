package com.example.kitcoop.lastproject1.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kitcoop.lastproject1.DATA.SharedObjct;
import com.example.kitcoop.lastproject1.DB.DBHelper;
import com.example.kitcoop.lastproject1.JClass.returnFragment;
import com.example.kitcoop.lastproject1.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kitcoop on 2017-04-11.
 */

public class CustomF_ChangeNickName extends Fragment implements SharedObjct {

    returnFragment rf = new returnFragment();   //프레그먼트 세팅으로 돌리기 위한 클래스
    EditText forChangeNickName;
    String result ="";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //final View view = inflater.inflate(R.layout.fragment, container, false);   //맨 앞자리는 디자인자리
        View view = inflater.inflate(R.layout.fragment_change_nickname, container, false);   //맨 앞자리는 디자인자리
        setImageAndText(view);


        return view;
    }

    public void setImageAndText (View v) {
        ((TextView)v.findViewById(R.id.f_change_tv_nickname)).setText(loginUserData.getNickname());
        forChangeNickName = (EditText)(v.findViewById(R.id.f_change_et_nickname));

        final postDataChangeNN pdn= new postDataChangeNN(handler);
        pdn.setDaemon(true);
        pdn.start();


        v.findViewById(R.id.btn_changeNN_Ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String changedNick = forChangeNickName.getText().toString();

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
            }
        });

        v.findViewById(R.id.btn_changeNN_No).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rf.returnToSetting(CustomF_ChangeNickName.this);
            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            result= ((String)msg.obj).trim();
            System.out.println(result);

            if(result.equals("fail")){
                Toast.makeText(getActivity(), "닉네임 변경에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }else if(result.equals("success")){
                Toast.makeText(getActivity(), "성공적으로 수정되었습니다.", Toast.LENGTH_SHORT).show();

                rf.returnToSetting(CustomF_ChangeNickName.this);
            }
        }
    };
}
