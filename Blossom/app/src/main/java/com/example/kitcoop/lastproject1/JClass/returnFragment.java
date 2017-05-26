package com.example.kitcoop.lastproject1.JClass;

import android.app.Fragment;
import android.app.FragmentManager;

import com.example.kitcoop.lastproject1.Fragment.CustomF_Setting;
import com.example.kitcoop.lastproject1.R;

/**
 * Created by kitcoop on 2017-04-12.
 */

public class returnFragment {
    public void returnToSetting(Fragment a) {
        FragmentManager fragmentManager = a.getFragmentManager();
        //fragmentManager.beginTransaction().remove(CustomF_ChangePW.this).commit();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new CustomF_Setting()).commit();
    }
}
