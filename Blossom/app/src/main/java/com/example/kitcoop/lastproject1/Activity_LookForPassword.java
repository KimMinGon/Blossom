package com.example.kitcoop.lastproject1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Activity_LookForPassword extends Activity {
    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lookforpassword);

        final postDataLookForPassword pdl = new postDataLookForPassword(handler);
        pdl.setDaemon(true);
        pdl.start();

        findViewById(R.id.lookforpassword_SendPassword).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                EditText et = (EditText)findViewById(R.id.lookforpassword_Email);
                String data = et.getText().toString();

                Message msg = Message.obtain();
                msg.what =300;
                msg.obj = data;

                pdl.backHandler.sendMessage(msg);
                // System.out.println(msg.obj);

            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            result= ((String)msg.obj).trim();

            if (result.equals("fail")) {
                //System.out.println(result);
                Toast.makeText(Activity_LookForPassword.this, "가입되지 않은 이메일을 입력하셨습니다.", Toast.LENGTH_SHORT).show();
            } else if (result.equals("success")) {
                Toast.makeText(Activity_LookForPassword.this, "이메일로 임시비밀번호를 전송하였습니다..", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Activity_LookForPassword.this, Activity_Login.class);
                startActivity(intent);

            }
        }
    };
}
