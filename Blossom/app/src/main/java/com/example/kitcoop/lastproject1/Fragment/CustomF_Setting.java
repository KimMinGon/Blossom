package com.example.kitcoop.lastproject1.Fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kitcoop.lastproject1.R;

/**
 * Created by kitcoop on 2017-04-11.
 */

public class CustomF_Setting extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        System.out.println("onCreateView() 호출");

        //final View view = inflater.inflate(R.layout.fragment, container, false);   //맨 앞자리는 디자인자리
        View view = inflater.inflate(R.layout.fragment_setting, container, false);   //맨 앞자리는 디자인자리

        setImage(view);

        view.findViewById(R.id.setting_btn_changePw).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment =new CustomF_ChangePW();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
            }
        });

        view.findViewById(R.id.setting_btn_changeNickname).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment =new CustomF_ChangeNickName();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
            }
        });

        view.findViewById(R.id.setting_btn_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //동기화 코드 넣어줄것
              // System.exit(0);
            }
        });

        view.findViewById(R.id.setting_btn_synchronization).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //동기화 코드 넣어줄것
            }
        });

        return view;
    }


    public void setImage (View v) {

    }
}
