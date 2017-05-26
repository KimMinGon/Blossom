package com.example.kitcoop.lastproject1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;

import com.example.kitcoop.lastproject1.DATA.SharedConstant;
import com.example.kitcoop.lastproject1.DATA.SharedObjct;
import com.example.kitcoop.lastproject1.Fragment.CustomDialog;

public class Activity_Settings extends PreferenceActivity implements SharedObjct, SharedConstant {

    PreferenceScreen preferenceScreen;
    Activity activity = this;
    Boolean IsChangeNick = false;
    Boolean IsChangePickture = false;

    String result;

    sync sync = new sync();


    @Override
    public void onCreate(Bundle savedIstanceState) {
        super.onCreate(savedIstanceState);



        addPreferencesFromResource(R.xml.settings);

        preferenceScreen = getPreferenceScreen();
        Preference useraccount = preferenceScreen.findPreference("setting_userAccount");
        useraccount.setSummary(loginUserData.getUserAccount());
        useraccount.setOnPreferenceClickListener(null);

        final Preference userNick = preferenceScreen.findPreference("setting_userNick");
        userNick.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                CustomDialog cd = new CustomDialog(activity);
                cd.ChangeNickDialog(activity);
                IsChangeNick = true;
                return true;
            }
        });

        final Preference userPW = preferenceScreen.findPreference("setting_userPW");
        userPW.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                CustomDialog cd = new CustomDialog(activity);
                cd.ChangePWDialog(activity);
                return true;
            }
        });

        final Preference logout2 = preferenceScreen.findPreference("setting_btn_logout");
        logout2.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                CustomDialog cd = new CustomDialog(activity);
                cd.EXIT(activity);
                return true;
            }
        });

        final Preference sycn = preferenceScreen.findPreference("setting_synchronization");
        sycn.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                System.out.println("test1 동기화 버튼");

                sync.sync_data_into_db(WATER);
                sync.sync_data_into_db(WALK);
                sync.sync_data_into_db(WEIGHT);
                sync.sync_data_into_db(HEIGHT);
                sync.sync_data_into_db(REWARD);
                sync.sync_data_into_db(GPS);
                sync.sync_data_into_db(SSAK);

                return true;
            }
        });

        final Preference sycn2 = preferenceScreen.findPreference("setting_synchronization_get");
        sycn2.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                System.out.println("test2 동기화 버튼");
                sync.sync_data_from_db(WEIGHT);
                return true;
            }
        });

    }

    @Override
    public void onBackPressed() {
        //설정 저장 후 피니시

        //Intent intent = getIntent(); // 이 액티비티를 시작하게 한 인텐트를 호출
        Intent intent = new Intent();
        if (intent != null) {
            intent.putExtra("IsChangeNick", IsChangeNick); //드로어로 넘어가도록
            intent.putExtra("IsChangePicture", IsChangePickture); //드로어로 넘어가도록
        }

        setResult(RESULT_OK, intent); // 추가 정보를 넣은 후 다시 인텐트를 반환합니다.
        finish();
    }
}