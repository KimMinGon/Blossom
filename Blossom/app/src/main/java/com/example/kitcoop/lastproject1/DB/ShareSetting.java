package com.example.kitcoop.lastproject1.DB;

/**
 * Created by kitcoop on 2017-04-24.
 */

import android.app.Activity;
import android.content.SharedPreferences;

/* Created by kitcoop on 2017-04-24.
*/

public class ShareSetting {
    SharedPreferences setting;
    SharedPreferences.Editor editor;

    public void makeSetting(Activity activity) {
        setting = activity.getSharedPreferences("setting", 0);
        editor = setting.edit();
    }

    public SharedPreferences getSetting() {
        if (setting != null)
            return setting;
        System.out.println("makeSetting 함수를 반드시 1회 사용해주세요...");
        return null;
    }

    public SharedPreferences.Editor getEditor() {
        if (editor != null)
            return editor;
        System.out.println("makeSetting 함수를 반드시 1회 사용해주세요...");
        return null;
    }
}