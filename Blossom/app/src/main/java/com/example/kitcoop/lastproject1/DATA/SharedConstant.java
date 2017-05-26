package com.example.kitcoop.lastproject1.DATA;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by kitcoop on 2017-04-14.
 */

public interface SharedConstant {
    //String IP = "33:80";
    String IP = "192.168.0.76:8080";
    //String IP = "192.168.0.7:8080";
    static final String DB_NAME = "Blossom.db";

    static final String WEIGHT = "WEIGHT";
    static final String HEIGHT = "HEIGHT";
    static final String WALK = "WALK";
    static final String WATER = "WATER";
    static final String REWARD = "REWARD";
    static final String GPS = "GPS";
    static final String USERACCOUNT = "USERACCOUNT";
    static final String SSAK = "SSAK";

    static final int TEST_USERNUMBER = 1;
    static final String TEST_USERACCOUNT = "sbean0215@gmail.com";

    static final String dateFormat = "yy-MM-dd HH:mm:ss";
    String format = new String(dateFormat);
    SimpleDateFormat today_hms_sdf = new SimpleDateFormat(format, Locale.KOREA);

    static final String TodaydateFormat = "yy-MM-dd";
    String format2 = new String(TodaydateFormat);
    SimpleDateFormat today_sdf = new SimpleDateFormat(format2, Locale.KOREA);

    String profilePic_dir = "/profile_Pic";

    String SETTING = "setting";
    String SETTING_ID = "ID";
    String SETTING_PW = "PW";
    String SETTING_AUTO_LOGIN = "Auto_Login_enabled";

    Integer NEED_TODAY_WATER = 2000; //2L
    Integer NEED_TODAY_WALK = 5000; //오천걸음

    //경험치..
    int level_exp[] = { 0, 3000, 8000, 15000, 23000, 33000, 45000, 60000, 80000, 100000}; //1~10레벨(만렙 10)

    static final int FIRST_REWARD_WALK = 0;
    static final int SECOND_REWARD_CHALLENGE = 1;
    static final int THIRD_REWARD_SSAK_LEVER = 2;

    //각 리워드 단계
    int FIRST_REWARD_WALK_STEP[] = {0, 1000, 10000, 30000};   // 1, 2, 3, 4 레벨로 저장
    int SECOND_REWARD_CHALLENGE_STEP[] = {1, 5, 10, 15};
    int THIRD_REWARD_SSAK_LEVER_STEP[] = {1, 2, 5, 9};


}
