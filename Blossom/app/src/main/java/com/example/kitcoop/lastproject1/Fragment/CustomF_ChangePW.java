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
import android.widget.Toast;

import com.example.kitcoop.lastproject1.JClass.returnFragment;
import com.example.kitcoop.lastproject1.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.kitcoop.lastproject1.DATA.SharedObjct.loginUserData;

/**
 * Created by kitcoop on 2017-04-11.
 */

public class CustomF_ChangePW extends Fragment {

    returnFragment rf = new returnFragment();   //프레그먼트 세팅으로 돌리기 위한 클래스

    EditText pw;
    EditText changepw;
    EditText changepw2;

    String result = "";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        System.out.println("onCreateView() 호출");

        //final View view = inflater.inflate(R.layout.fragment, container, false);   //맨 앞자리는 디자인자리
        View view = inflater.inflate(R.layout.fragment_change_pw, container, false);   //맨 앞자리는 디자인자리

        pw = (EditText)view.findViewById(R.id.editText3);
        changepw = (EditText)view.findViewById(R.id.editText5);
        changepw2 = (EditText)view.findViewById(R.id.editText6);

        final postDataChangePW pdc= new postDataChangePW(handler);
        pdc.setDaemon(true);
        pdc.start();

        view.findViewById(R.id.btn_changePW_No).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rf.returnToSetting(CustomF_ChangePW.this);
            }
        });

        view.findViewById(R.id.btn_changePW_Ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject = new JSONObject();
                JSONArray jsonArray = new JSONArray();

                try {
                    jsonObject.put("usernumber" , loginUserData.getUsernumber());
                    jsonObject.put("password", pw.getText().toString());
                    jsonObject.put("changepw", changepw.getText().toString());

                    if(!changepw.getText().toString().equals(changepw2.getText().toString())){
                        Toast.makeText(getActivity(), "새 비밀번호와 새 비밀번호 확인이 다릅니다", Toast.LENGTH_SHORT).show();
                    } else if (changepw.getText().toString().length() < 8) {
                        Toast.makeText(getActivity(), "비밀번호를 8자 이상 입력하세요.", Toast.LENGTH_SHORT).show();
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
                //  rf.returnToSetting(CustomF_ChangePW.this);
            }
        });

        return view;
    }


    public void setImage (View v) {
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            result= ((String)msg.obj).trim();
            System.out.println(result);

            if(result.equals("fail")){
                Toast.makeText(getActivity(), "현재 비밀번호가 틀립니다.", Toast.LENGTH_SHORT).show();
            }else if(result.equals("success")){
                Toast.makeText(getActivity(), "성공적으로 수정되었습니다.", Toast.LENGTH_SHORT).show();
                rf.returnToSetting(CustomF_ChangePW.this);
            }
        }
    };

}
