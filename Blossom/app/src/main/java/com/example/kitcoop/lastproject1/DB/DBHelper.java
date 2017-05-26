package com.example.kitcoop.lastproject1.DB;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.kitcoop.lastproject1.DATA.SharedConstant;
import com.example.kitcoop.lastproject1.DATA.SharedObjct;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by kitcoop on 2017-04-14.
 */

public final class DBHelper extends SQLiteOpenHelper implements SharedConstant, SharedObjct {
        ///수우우우어저어엉하아아알것


    // DBHelper 생성자로 관리할 DB 이름과 버전 정보를 받음
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    // DB를 새로 생성할 때 호출되는 함수
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 새로운 테이블 생성
        /* 이름은 MONEYBOOK이고, 자동으로 값이 증가하는 _id 정수형 기본키 컬럼과
        item 문자열 컬럼, price 정수형 컬럼, create_at 문자열 컬럼으로 구성된 테이블을 생성. */
        //db.execSQL("CREATE TABLE MONEYBOOK (_id INTEGER PRIMARY KEY AUTOINCREMENT, item TEXT, price INTEGER, create_at TEXT);");
        System.out.println("테이블 생성");
        db.execSQL("CREATE TABLE USERACCOUNT (_seq INTEGER PRIMARY KEY AUTOINCREMENT, usernumberFromServer INTEGER, account TEXT, nickname TEXT, last_acc_date TEXT);");
        db.execSQL("CREATE TABLE WEIGHT (_seq INTEGER PRIMARY KEY AUTOINCREMENT, usernumber INTEGER, date TEXT, weight REAL);");
        db.execSQL("CREATE TABLE HEIGHT (_seq INTEGER PRIMARY KEY AUTOINCREMENT, usernumber INTEGER, date TEXT, height REAL);");
        db.execSQL("CREATE TABLE WALK (_seq INTEGER PRIMARY KEY AUTOINCREMENT, usernumber INTEGER, date TEXT, walk INTEGER);");
        db.execSQL("CREATE TABLE WATER (_seq INTEGER PRIMARY KEY AUTOINCREMENT, usernumber INTEGER, date TEXT, water REAL);");
        db.execSQL("CREATE TABLE REWARD (_seq INTEGER PRIMARY KEY AUTOINCREMENT, usernumber INTEGER, date TEXT, reward INTEGER, currlevel INTEGER, tpoint INTEGER);");
        db.execSQL("CREATE TABLE GPS (_seq INTEGER PRIMARY KEY AUTOINCREMENT, usernumber INTEGER, date TEXT, distance REAL, stime TEXT, ttime TEXT, mapimg TEXT, path TEXT);");
        db.execSQL("CREATE TABLE SSAK (_seq INTEGER PRIMARY KEY AUTOINCREMENT, usernumber INTEGER, date TEXT, exp_today_water INTEGER, exp_today_walk INTEGER, total_exp INTEGER, level INTEGER);");
    }

    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean insert_A_useraccount(int usernumberFromServer, String useraccount, String nickname, String joinday){
        SQLiteDatabase db = getWritableDatabase();

        String sql = "INSERT INTO "+USERACCOUNT+" VALUES(null, " + usernumberFromServer + ", '" + useraccount + "', '" +nickname+"', '"+ joinday +"');";
        db.execSQL(sql);
        System.out.println(sql);
        db.close();
        return true;
    }

    public ArrayList<DataForQuery> selectUserAccount(boolean GetAllUser, Integer usernumber){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<DataForQuery> arrayList = new ArrayList<>();

        String sql ;
        if(GetAllUser == true)
            sql = "SELECT * FROM " + USERACCOUNT;
        else
            sql = "SELECT * FROM " + USERACCOUNT + " WHERE usernumberFromServer = " + usernumber;
        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            //에러나는지 아닌지 확인해볼것
            DataForQuery d = new DataForQuery();
            d.DefalutDATA(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
            arrayList.add(d);
        }

        for(int i = 0 ; i < arrayList.size() ; i++) {
            System.out.println("유저정보 테스트 출력 : " + arrayList.get(0)
                            + " / " + arrayList.get(i).getSeq()
                            + " / " + arrayList.get(i).getUsernumber()
                            + " / " + arrayList.get(i).getUserAccount()
                            + " / " + arrayList.get(i).getNickname()
                            + " / " + arrayList.get(i).getDate()
            );
        }

        db.close();
        return arrayList;
    }

    public boolean updateUserLastAccess(Integer usernumber, String date){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "UPDATE USERACCOUNT SET last_acc_date = '" + date +"' WHERE usernumberFromServer = "+usernumber+";";
        System.out.println(sql);
        db.execSQL(sql);
        db.close();

        return true;
    }

    public boolean updateUserNickname(Integer usernumber, String NickName){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "UPDATE USERACCOUNT SET nickname = '" + NickName +"' WHERE usernumberFromServer = "+usernumber+";";
        System.out.println(sql);
        db.execSQL(sql);
        db.close();

        return true;
    }

    public void deleteUserAccount(Integer usernumber){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "DELETE FROM USERACCOUNT WHERE usernumberFromServer=" + usernumber+";";
        System.out.println(sql);
        db.execSQL(sql);
        db.close();
    }

    public void updateEXP(Integer usernumber, int today_walk, int today_water) {

        ////////////////////////////////////////////////////////////////////////////////////////
        /////수정 사항.. 하루에 입력할 수 있는 exp 정보를 가지고와서 리미트를 가지도록 수정하자..
        ////////////////////////////////////////////////////////////////////////////////////////

        String sql = "SELECT COUNT(*) FROM SSAK WHERE usernumber=" + usernumber
                + " AND date like '" + today_sdf.format(new Date()) +"'";
        System.out.println("경험치 업데이트 : " + sql);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToNext();
        Integer count = cursor.getInt(0);

        sql = "SELECT total_exp FROM SSAK WHERE usernumber=" + usernumber
                + " AND NOT date like '" + today_sdf.format(new Date()) +"' order by _seq desc";
        System.out.println("경험치 업데이트 - 전날 total_exp 가져오기 : " + sql);

        cursor = db.rawQuery(sql, null);
        Integer fore_total_exp =0;      //전날 데이터가 존재하지 않으면
        if(cursor.moveToNext())        //전날 데이터가 존재하면..
            fore_total_exp = cursor.getInt(0);

        //수정된 코드 ------------------------------------------------------------------------------

        //Integer today_total_exp = fore_total_exp+today_walk+today_water;
        Integer today_total_exp = fore_total_exp;
        //오늘걸음 오늘 물 중 하나는 0임, 그래서 다른정보 가져와서 밑에서 더해줌


        //만약 싹 테이블에 오늘의 데이터가 없으면 생성
        //단 이전(직전) 다른날 에 저장된 데이터를 가져와서 토탈경험치를 결정하고 + 오늘의 다른데이터도 불러와야지만 완벽한 토탈,..OTL
        //레벨 분류에 따라 레벨 작업을 한다


        //하루에 올릴수 있는 경험치 이상으로 올리지 못하게 리미트를 걸어준다.
        if( NEED_TODAY_WALK < today_walk) {
            System.out.println(" 걸음 리미트 ");
            today_walk = NEED_TODAY_WALK;
        }
        if( NEED_TODAY_WATER < today_water) {
            System.out.println(" 물 리미트 ");
            today_water = NEED_TODAY_WATER;
        }
        today_total_exp += today_walk + today_water;

        Integer level = 0;


        for(int i = 0 ; i<level_exp.length-1 ; i++) {
            if (level_exp[i] <= today_total_exp && today_total_exp < level_exp[i + 1]) {
                level = i + 1;
                break;
            }
        }
        //------------------------------------------------------------------------------수정된 코드


        if(level_exp[level_exp.length-1] <= today_total_exp)
            level = level_exp.length;
        System.out.println("레벨 저장 단계 " + level);

        if(count == 0){
            sql = "INSERT INTO SSAK VALUES(null, " + usernumber + ", "
                    + "'" + today_sdf.format(new Date()) + "' ,"
                    + today_water + ", "
                    + today_walk + ", "
                    + today_total_exp + ", "
                    + level +");";
        }
        //존재하면 업데이트 시킨다
        else {
            sql = "SELECT exp_today_water, exp_today_walk FROM SSAK WHERE usernumber=" + usernumber
                    + " AND date like '" + today_sdf.format(new Date()) +"'";
            System.out.println("경험치 업데이트 - 오늘 다른 데이터 가져오기 : " + sql);
            cursor = db.rawQuery(sql, null);

            cursor.moveToNext();
            if(today_water != 0 && today_walk == 0) {   //물을 입력할때
                today_total_exp += cursor.getInt(1);    //오늘의 walk 정보를 가져옴
                sql = "UPDATE SSAK SET exp_today_water = " + today_water + ", level = " + level + ", total_exp = "+ today_total_exp + " WHERE usernumber = " + usernumber + " AND date = " + "'" + today_sdf.format(new Date()) + "';";

            } else if(today_water == 0 && today_walk != 0) {    //걸음을 입력할때
                today_total_exp += cursor.getInt(0);    //오늘의 물 정보를 가져옴
                sql = "UPDATE SSAK SET exp_today_walk = " + today_walk +", level = " + level + ", total_exp = "+ today_total_exp + " WHERE usernumber = "+usernumber+" AND date = " + "'" + today_sdf.format(new Date()) + "';";
            }
            else
                System.out.println("둘중 한 데이터만 업데이트용으로 입력하세요");
        }

        update_Reward_SSAK(usernumber, level); //리워드 업데이트

        System.out.println("경험치 업데이트 : " + sql);
        db = getWritableDatabase();
        db.execSQL(sql);

        db.close();
        cursor.close();
    }

    public int getSSAKlevel(Integer usernumber){
        String sql = "SELECT level FROM SSAK WHERE usernumber=" + usernumber + " order by level desc";

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        int result = 1;
        if(cursor.moveToNext()) {
            result =  cursor.getInt(0) + 1;
        }

        db.close(); cursor.close();
        return result;
    }

    public int getSSAKexp(Integer usernumber){
        String sql = "SELECT total_exp FROM SSAK WHERE usernumber=" + usernumber + " order by total_exp desc";

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        int result = 0;
        if(cursor.moveToNext()) {
            result =  cursor.getInt(0);
        }

        db.close(); cursor.close();
        return result;
    }

    public int[] getReward(Integer usernumber) {
        int return_rewards[] = {1,1,1};
        String sql = "SELECT currlevel FROM REWARD WHERE usernumber=" + usernumber + " AND reward = "+ FIRST_REWARD_WALK+" order by currlevel desc";

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToNext())
            return_rewards[0] =  cursor.getInt(0);

        System.out.println("겟리워드 : " + sql);

        sql = "SELECT currlevel FROM REWARD WHERE usernumber=" + usernumber + " AND reward = "+ SECOND_REWARD_CHALLENGE+" order by currlevel desc";
        cursor = db.rawQuery(sql, null);
        if(cursor.moveToNext())
            return_rewards[1] =  cursor.getInt(0);

        System.out.println("겟리워드 : " + sql);

        sql = "SELECT currlevel FROM REWARD WHERE usernumber=" + usernumber + " AND reward = "+ THIRD_REWARD_SSAK_LEVER+" order by currlevel desc";
        cursor = db.rawQuery(sql, null);
        if(cursor.moveToNext())
            return_rewards[2] =  cursor.getInt(0);


        System.out.println("겟리워드 : " + sql);

        db.close(); cursor.close();

        return  return_rewards;
    }

    public void update_Reward_SSAK(int usernumber, int ssakLevel) {

        System.out.println("리워드 업데이트 싹");

        int saakREWARDlevel = 0;
        for(int i = 0 ; i<THIRD_REWARD_SSAK_LEVER_STEP.length-1 ; i++)
            if(THIRD_REWARD_SSAK_LEVER_STEP[i] <= ssakLevel && ssakLevel < THIRD_REWARD_SSAK_LEVER_STEP[i+1])
                saakREWARDlevel = i + 1;
        if(THIRD_REWARD_SSAK_LEVER_STEP[FIRST_REWARD_WALK_STEP.length-1] <= ssakLevel)
            saakREWARDlevel = FIRST_REWARD_WALK_STEP.length;

        String sql = "SELECT COUNT(*) FROM REWARD WHERE usernumber=" + usernumber
                + " AND reward=" + THIRD_REWARD_SSAK_LEVER
                //+ " AND currlevel=" + saakREWARDlevel
                + " AND tpoint=" + ssakLevel;//currlevel INTEGER, tpoint INTEGER

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToNext();

        Integer count = cursor.getInt(0);

        if(count >= 1) { //아무것도 안함.. 같은 싹레벨이 존재하면 궂이 리워드 업데이트 할 피룡없음
        } else {
            db = getWritableDatabase();
            sql = "INSERT INTO "+REWARD+" VALUES(null, " + usernumber + ", '" + today_hms_sdf.format(new Date()) + "', " +THIRD_REWARD_SSAK_LEVER+", "+saakREWARDlevel+", "+ssakLevel+");";
            db.execSQL(sql);
            System.out.println("싹 리워드 인서트 : " + sql);
        }

        db.close(); cursor.close();
    }

    public void update_Reward_TodayCHALLENGE(int usernumber) {

        String sql = "SELECT COUNT(*) FROM REWARD WHERE usernumber=" + usernumber + " AND reward=" + SECOND_REWARD_CHALLENGE;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToNext();

        Integer count = cursor.getInt(0);

        db.close(); cursor.close();

        System.out.println("챌린지");
        Integer currlevel = 1;
        Integer tpoint = 1;
        if(count == 0) {  //만약 기존 챌린지 리워드 정보가 존재하지 않는다면 1로 집어넣음
            System.out.println("챌린지 정보 입력\n" + sql);
            currlevel = 1;
            tpoint = 1;
        } else {    //입력되어 있다면 마지막 값을 가져와서 +1 로 인서트
            sql = "SELECT currlevel, tpoint FROM REWARD WHERE usernumber=" + usernumber + " AND reward=" + SECOND_REWARD_CHALLENGE + " order by _seq desc";

            System.out.println("챌린지 정보 입력 전 데이터 가져오기\n" + sql);

            SQLiteDatabase db3 = getReadableDatabase();
            Cursor cursor3 = db3.rawQuery(sql, null);
            cursor3.moveToNext();

            //티포인트에 따른 커런트레벨도 확정해서 넣어줄것
            tpoint = cursor3.getInt(1) + 1;
            currlevel = cursor3.getInt(0);
            for(int i = 0 ; i<SECOND_REWARD_CHALLENGE_STEP.length-1 ; i++)
                if(SECOND_REWARD_CHALLENGE_STEP[i] <= tpoint && tpoint < SECOND_REWARD_CHALLENGE_STEP[i+1])
                    currlevel = i + 1;
            if(SECOND_REWARD_CHALLENGE_STEP[SECOND_REWARD_CHALLENGE_STEP.length-1] <= tpoint)
                currlevel = SECOND_REWARD_CHALLENGE_STEP.length;

            //System.out.println(currlevel +" "+ tpoint);
            db3.close(); cursor3.close();
        }

        SQLiteDatabase wdb = getWritableDatabase();
        sql = "INSERT INTO "+REWARD+" VALUES(null, " + usernumber + ", '" + today_hms_sdf.format(new Date()) + "', " +SECOND_REWARD_CHALLENGE+", "+currlevel+", "+tpoint+");";
        wdb.execSQL(sql);
        System.out.println(sql);
        wdb.close();
    }

   public void test_walk(){
       SQLiteDatabase wdb = getWritableDatabase();
       String sql = "INSERT INTO SSAK VALUES(null, " + 1162 + ", "
               + "'17-04-19' ,"
               + 10000 + ", "
               + 10000 + ", "
               + 20000 + ", "
               + 1 +");";
       wdb.execSQL(sql);
       wdb.close();
       System.out.println("가 데이터 : " + sql);
       /*
        int usernumber = 1162;
        SQLiteDatabase wdb = getWritableDatabase();
        String sql = "INSERT INTO "+REWARD
                +" VALUES(null, " + usernumber + ", '17-04-19 11:04:57', " +FIRST_REWARD_WALK+", "+0+", "+10+");";
        wdb.execSQL(sql);
        System.out.println("test_insert_walk_reward : " + sql);
        sql = "INSERT INTO "+REWARD
                +" VALUES(null, " + usernumber + ", '17-04-20 11:04:57', " +FIRST_REWARD_WALK+", "+0+", "+1000+");";
        wdb.execSQL(sql);
        System.out.println("test_insert_walk_reward : " + sql);
        sql = "INSERT INTO "+REWARD
                +" VALUES(null, " + usernumber + ", '17-04-21 11:04:57', " +FIRST_REWARD_WALK+", "+0+", "+2000+");";
        wdb.execSQL(sql);
        System.out.println("test_insert_walk_reward : " + sql);
        sql = "INSERT INTO "+REWARD
                +" VALUES(null, " + usernumber + ", '17-04-22 11:04:57', " +FIRST_REWARD_WALK+", "+0+", "+10000+");";
        wdb.execSQL(sql);
        System.out.println("test_insert_walk_reward : " + sql);
        wdb.close();*/
    }

   //어차피 walk는 올라가는거 에스큐엘 라이트에 넣고빼면서 토탈값 관리되니까 토탈값넣으면 전에 데이터랑 합쳐줌. 당일날 저장된 다른 데이터는 무시함.
    public void update_Reward_WALK(int usernumber, int totayTotalwalk) {

        String sql = "SELECT COUNT(*) FROM REWARD WHERE usernumber=" + usernumber
                + " AND reward=" + FIRST_REWARD_WALK
                + " AND NOT date like '" + today_sdf.format(new Date()) +"%'";

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToNext();

        Integer count = cursor.getInt(0);

        db.close(); cursor.close();

        Integer currlevel = 1;
        Integer tpoint = 1;
        if(count == 0) {  //만약 기존 챌린지 리워드 정보가 존재하지 않는다면 1로 집어넣음
            System.out.println("걷기 챌린지 정보 입력\n" + sql);
            currlevel = 1;
            tpoint = totayTotalwalk;
        } else {    //입력되어 있다면 마지막 값을 가져와서 +1 로 업데이트
            sql = "SELECT currlevel, tpoint FROM REWARD WHERE usernumber=" + usernumber
                    + " AND reward=" + FIRST_REWARD_WALK
                    + " AND NOT date like '" + today_sdf.format(new Date()) +"%'"
                    + " order by _seq desc";

            System.out.println("걷기 챌린지 정보 입력 전 데이터 가져오기\n" + sql);

            SQLiteDatabase db3 = getReadableDatabase();
            Cursor cursor3 = db3.rawQuery(sql, null);
            cursor3.moveToNext();

            //티포인트에 따른 커런트레벨도 확정해서 넣어줄것
            tpoint = cursor3.getInt(1) + totayTotalwalk;
            int prelevel = cursor3.getInt(0);
            currlevel = cursor3.getInt(0);
            for(int i = 0 ; i<FIRST_REWARD_WALK_STEP.length-1 ; i++)
                if(FIRST_REWARD_WALK_STEP[i] <= tpoint && tpoint < FIRST_REWARD_WALK_STEP[i+1])
                    currlevel = i + 1;
            if(FIRST_REWARD_WALK_STEP[FIRST_REWARD_WALK_STEP.length-1] <= tpoint)
                currlevel = FIRST_REWARD_WALK_STEP.length;

            if(prelevel == currlevel + 1){
              /*  NotificationCompat.Builder builder = new NotificationCompat.Builder(activity);
                Resources res = activity.getResources();
                notificationIntent.putExtra("notificationId", 9999); //전달할 값
                PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                builder.setContentTitle("상태바 드래그시 보이는 타이틀")
                        .setContentText("상태바 드래그시 보이는 서브타이틀")
                        .setTicker("상태바 한줄 메시지")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher))
                        .setContentIntent(contentIntent)
                        .setAutoCancel(true)
                        .setWhen(System.currentTimeMillis())
                        .setDefaults(Notification.DEFAULT_ALL).setCategory(Notification.CATEGORY_MESSAGE)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setVisibility(Notification.VISIBILITY_PUBLIC);*/
            }

            db3.close(); cursor3.close();
        }

        SQLiteDatabase wdb = getWritableDatabase();
        sql = "INSERT INTO "+REWARD+" VALUES(null, " + usernumber + ", '" + today_hms_sdf.format(new Date()) + "', " +FIRST_REWARD_WALK+", "+currlevel+", "+tpoint+");";
        wdb.execSQL(sql);
        System.out.println("test_insert_walk_reward : " + sql);
        wdb.close();
    }

    public void updateTodayDATA(String TableName, Integer usernumber, int water, float weight, float height, int walk) {

        String sql = "SELECT COUNT(*) FROM "+TableName+" WHERE usernumber=" + usernumber + " AND date='" + today_sdf.format(new Date())+"'";

        DataForQuery data = null;
        switch (TableName) {
            case WATER:
                data = new QueryWATER(null, usernumber, null, today_sdf.format(new Date()), water);
                break;
            case WEIGHT:
                data = new QueryWEIGHT(null, usernumber, null, today_sdf.format(new Date()), weight);
                break;
            case HEIGHT:
                data = new QueryHEIGHT(null, usernumber, null, today_sdf.format(new Date()), height);
                break;
            case WALK:
                data = new QueryWALK(null, usernumber, null, today_sdf.format(new Date()), walk);
                update_Reward_WALK(usernumber, walk); //리워드 업데이트(내부에서 레벨 및 총tpoint 체크 후 저장)
                break;
        }

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToNext();

        Integer count = cursor.getInt(0);

        db.close(); cursor.close();

        if(count == 0) {  //만약에 오늘의 물이 입력되어 있지 않다면, 행을 추가하고
            insert(TableName, data);
        } else if(count == 1) {    //입력되어 있다면 업데이트
            update(TableName, data);
        }

        //만약 투데이 챌린지(물/걸음)이 성공하지 않았다면
        //성공여부를 판별해서
        //성공으로 하고 챌린지 정보 update로

        SharedPreferences setting = SHARE_SETTING.getSetting();
        SharedPreferences.Editor editor = setting.edit();

        //에스큐엘 라이트에 챌린지 업데이트 하는 부분 추가해주기.
        switch (TableName) {
            case WATER:
                if(!setting.getBoolean("CHALLENGE_WATER_TODAY_"+today_sdf.format(new Date()), false)){
                    //false 일 경우에만  //오늘일자의 챌린지 1 셋 -> 성공
                    int challenge_WATER = Integer.parseInt(setting.getString("TODAY_C_WATER", "0"));       //챌린지 목표를 가져와서 비교
                    if(challenge_WATER <= water){
                        editor.putBoolean("CHALLENGE_WATER_TODAY_"+today_sdf.format(new Date()), true);
                        editor.commit();
                        System.out.println("챌린지1 성공 " + setting.getBoolean("CHALLENGE_WATER_TODAY_"+today_sdf.format(new Date()), false));

                        update_Reward_TodayCHALLENGE(usernumber);
                    }
                }
                break;
            case WALK:
                if(!setting.getBoolean("CHALLENGE_WALK_TODAY_"+today_sdf.format(new Date()), false)){
                    //false 일 경우에만  //오늘일자의 챌린지 2 셋 -> 성공
                    int challenge_WALK = Integer.parseInt(setting.getString("TODAY_C_WALK", "0"));  //챌린지 목표를 가져와서 비교
                    if(challenge_WALK <= walk){
                        editor.putBoolean("CHALLENGE_WALK_TODAY_"+today_sdf.format(new Date()), true);
                        editor.commit();
                        System.out.println("챌린지2 성공 " + setting.getBoolean("CHALLENGE_WALK_TODAY_"+today_sdf.format(new Date()), false));

                        update_Reward_TodayCHALLENGE(usernumber);
                    }
                }
                break;
        }

    }

    public Integer selectTodayWater(Integer usernumber) {

        String sql = "SELECT water FROM WATER WHERE usernumber=" + usernumber + " AND date='" + today_sdf.format(new Date())+"'";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        if(cursor.moveToNext()){
            Integer count = cursor.getInt(0);
            db.close(); cursor.close();
            return count;
        }
        db.close(); cursor.close();
        return 0;
    }

    public float selectTodayHeight(Integer usernumber) {

        String sql = "SELECT height FROM HEIGHT WHERE usernumber=" + usernumber + " AND date='" + today_sdf.format(new Date())+"'";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        if(cursor.moveToNext()){
            Float count = cursor.getFloat(0);
            db.close(); cursor.close();
            return count;
        }
        db.close(); cursor.close();
        return 0.0f;
    }

    public float selectTodayWeight(Integer usernumber) {

        String sql = "SELECT weight FROM WEIGHT WHERE usernumber=" + usernumber + " AND date='" + today_sdf.format(new Date())+"'";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        if(cursor.moveToNext()){
            Float count = cursor.getFloat(0);
            db.close(); cursor.close();
            return count;
        }
        db.close(); cursor.close();
        return 0.0f;
    }

    public int selectTodayWalk(Integer usernumber) {

        String sql = "SELECT walk FROM WALK WHERE usernumber=" + usernumber + " AND date='" + today_sdf.format(new Date())+"'";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        if(cursor.moveToNext()){
            Integer count = cursor.getInt(0);
            db.close(); cursor.close();
            return count;
        }
        db.close(); cursor.close();
        return 0;
    }

    public void insertGPSdata(float distance, String stime, String ttime, String map_img_filename, String path) {
        String sql = "INSERT INTO GPS VALUES(null, " + loginUserData.getUsernumber() + ", '" + today_sdf.format(new Date()) + "', "

                + distance + ", '" + stime + "', '" + ttime + "', '" +map_img_filename+ "', '" +path+ "');";
        System.out.println("insertGPSdata() 함수 쿼리문 체크 : " + sql);

        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(sql);
        System.out.println(sql);
        db.close();

    }

    public ArrayList<QueryGPS> getGPSwalkDate(Integer usernumber) {
      ArrayList<QueryGPS> returnGPSes = new ArrayList<QueryGPS> ();

        String sql = "SELECT _seq, date, distance, stime, ttime, mapimg, path FROM GPS WHERE usernumber=" + usernumber + " order by _seq";
        System.out.println("getGPSwalkDate 쿼리문 체크 : " + sql);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        while(cursor.moveToNext()){
            QueryGPS add = new QueryGPS(cursor.getInt(0)    //Integer seq
                    , loginUserData.getUsernumber()         //String Usernumber
                    , loginUserData.getUserAccount()        //String useraccount
                    , cursor.getString(1)                   //String Date
                    , cursor.getFloat(2)                    //float distance
                    , cursor.getString(3)                   //String stime
                    , cursor.getString(4)                   //String ttime
                    , cursor.getString(5)                 //String Map_imgfile_name
                    , cursor.getString(6));

            returnGPSes.add(add);
        }

        db.close(); cursor.close();

        return returnGPSes;
    }

    public boolean insert(String TableName, DataForQuery data) {

        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        String sql = "INSERT INTO "+ TableName +" VALUES(null, " + data.getUsernumber() + ", '" + data.getDate() + "', ";

        switch (TableName) {

            // DB에 입력한 값으로 행 추가
            // db.execSQL("INSERT INTO MONEYBOOK VALUES(null, '" + item + "', " + price + ", '" + create_at + "');");

            case WEIGHT :
                QueryWEIGHT weight = (QueryWEIGHT)data;
                sql += String.format("%.2f",weight.getWeight());
                break;
            case HEIGHT: QueryHEIGHT height = (QueryHEIGHT)data;
                sql +=  String.format("%.2f",height.getHeight());
                break;
            case WALK : QueryWALK walk = (QueryWALK)data;
                sql += walk.getWalk();
                //updateEXP(walk.getUsernumber(), walk.getWalk(), 0);
                break;
            case WATER : QueryWATER water = (QueryWATER)data;
                sql += water.getWater();
                //updateEXP(water.getUsernumber(), 0, water.getWater());
                break;
            case REWARD : QueryREWARD reward = (QueryREWARD)data;
                sql += reward.getReward() +  ", " +reward.getCurrLevel()+  ", " + reward.getTPoint();
                break;
            case GPS : QueryGPS gps = (QueryGPS)data;
                sql += gps.getDistance() +  ", '" +gps.getStime()+  "', '" + gps.getTtime()+"', '" + gps.getMAP_imgfile_name()+"'"  ;
                break;
            default:
                return false;
        }
        sql += ");";

        //sql 문 테스트해서 확인 후에 excSQL(sql) 문 주석 풀어서 사용할것
        System.out.println("테스트 : " + sql);
        db.execSQL(sql);
        db.close();

        switch (TableName) {
            case WALK:
                updateEXP(data.getUsernumber(), ((QueryWALK)data).getWalk(), 0);
                break;
            case WATER:
                updateEXP(data.getUsernumber(), 0, ((QueryWATER)data).getWater());
                break;
        }
        return true;
    }

    public boolean insertALL(String TableName, ArrayList<DataForQuery> datas) {

        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();

        String sql = "";

        System.out.println(TableName);
        switch (TableName) {

            // DB에 입력한 값으로 행 추가
            // db.execSQL("INSERT INTO MONEYBOOK VALUES(null, '" + item + "', " + price + ", '" + create_at + "');");
                case WEIGHT:
                    for(int i = 0 ; i < datas.size() ; i++ ) {
                       QueryWEIGHT weight = (QueryWEIGHT) datas.get(i);
                        sql = "INSERT INTO "+ TableName +" VALUES(null, " + weight.getUsernumber() + ", '" + weight.getDate() + "', ";
                        sql += String.format("%.2f", weight.getWeight()) + ");";
                        //System.out.println("동기화 : " +sql);
                        db.execSQL(sql);
                    }
                    break;
                case HEIGHT:
                    for(int i = 0 ; i < datas.size() ; i++ ) {
                        QueryHEIGHT height = (QueryHEIGHT) datas.get(i);
                        sql = "INSERT INTO "+ TableName +" VALUES(null, " + height.getUsernumber() + ", '" + height.getDate() + "', ";
                        sql += String.format("%.2f", height.getHeight()) + ");";
                       // System.out.println("동기화 : " +sql);
                        if(!db.isOpen())
                            db = getWritableDatabase();
                        db.execSQL(sql);
                    }
                    break;
                case WALK:
                    for(int i = 0 ; i < datas.size() ; i++ ) {
                        QueryWALK walk = (QueryWALK) datas.get(i);
                        sql = "INSERT INTO "+ TableName +" VALUES(null, " + walk.getUsernumber() + ", '" + walk.getDate() + "', ";
                        sql += walk.getWalk() + ");";
                       // System.out.println("동기화 : " +sql);
                        if(!db.isOpen())
                            db = getWritableDatabase();
                        db.execSQL(sql);
                    }
                    break;
                case WATER:
                    for(int i = 0 ; i < datas.size() ; i++ ) {
                        QueryWATER water = (QueryWATER) datas.get(i);
                        sql = "INSERT INTO "+ TableName +" VALUES(null, " + water.getUsernumber() + ", '" + water.getDate() + "', ";
                        sql += water.getWater() + ");";
                       // System.out.println("동기화 : " +sql);
                        if(!db.isOpen())
                            db = getWritableDatabase();
                        db.execSQL(sql);
                    }
                    break;
                case REWARD:
                    for(int i = 0 ; i < datas.size() ; i++ ) {
                        QueryREWARD reward = (QueryREWARD) datas.get(i);
                        sql = "INSERT INTO "+ TableName +" VALUES(null, " + reward.getUsernumber() + ", '" + reward.getDate() + "', ";
                        sql += reward.getReward() +", " + reward.getCurrLevel()+ "," + reward.getTPoint()+ ");";
                       // System.out.println("동기화 : " +sql);
                        if(!db.isOpen())
                            db = getWritableDatabase();
                        db.execSQL(sql);
                    }
                    break;
                case GPS:
                    for(int i = 0 ; i < datas.size() ; i++ ) {
                        QueryGPS gps = (QueryGPS) datas.get(i);
                        sql = "INSERT INTO "+ TableName +" VALUES(null, " + gps.getUsernumber() + ", '" + gps.getDate() + "', '";
                        sql += gps.getDistance()+"', '" + gps.getStime()+ "','" + gps.getTtime().trim()+  "', '" +gps.getMAP_imgfile_name()+"', '" +gps.getGPSpath() +"');";

                       // System.out.println("동기화 : " +sql);
                        if(!db.isOpen())
                            db = getWritableDatabase();
                        db.execSQL(sql);
                    }
                    break;
                case SSAK:
                    for(int i = 0 ; i < datas.size() ; i++ ) {
                        QuerySSAK ssak = (QuerySSAK) datas.get(i);
                        sql = "INSERT INTO "+ TableName +" VALUES(null, " + ssak.getUsernumber() + ", '" + ssak.getDate() + "', ";
                        sql += ssak.getExp_today_water()+", " + ssak.getExp_today_walk()+ "," + ssak.getTotal_exp()+  "," +ssak.getLevel() +");";

                      //  System.out.println("동기화 : " +sql);
                        if(!db.isOpen())
                            db = getWritableDatabase();
                        db.execSQL(sql);
                    }
                default:
                    return false;

            }


        //sql 문 테스트해서 확인 후에 excSQL(sql) 문 주석 풀어서 사용할것
        //System.out.println("테스트 : " + sql);

        db.close();


        return true;
    }

    public boolean update(String TableName, DataForQuery data) {
        //유저 넘버와 date로 기본 데이터 수정하는 메서드

        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행의 가격 정보 수정
        //db.execSQL("UPDATE MONEYBOOK SET price=" + price + " WHERE item='" + item + "';");
        String sql = "UPDATE " + TableName + " SET ";

        switch (TableName) {
            case WEIGHT :
                QueryWEIGHT weight = (QueryWEIGHT)data;
                sql += "weight=" +  String.format("%.2f",weight.getWeight());
                break;
            case HEIGHT: QueryHEIGHT height = (QueryHEIGHT)data;
                sql += "height=" +  String.format("%.2f",height.getHeight());
                break;
            case WALK : QueryWALK walk = (QueryWALK)data;
                sql += "walk=" + walk.getWalk();
                //updateEXP(walk.getUsernumber(), walk.getWalk(), 0);
                break;
            case WATER : QueryWATER water = (QueryWATER)data;
                sql += "water=" + water.getWater();
                //updateEXP(water.getUsernumber(), 0, water.getWater());
                break;
            case REWARD : QueryREWARD reward = (QueryREWARD)data;
                sql += "reward=" + reward.getReward() +  ", currlevel=" +reward.getCurrLevel()+  ", tpoint=" + reward.getTPoint();
                break;
            case GPS : QueryGPS gps = (QueryGPS)data;
                sql += "distance="+ gps.getDistance() +  ", stime='" +gps.getStime()+  "', ttime='" + gps.getTtime()+"', '" + gps.getMAP_imgfile_name()+"'" ;
                break;
            default:
                return false;
        }

        sql += " WHERE usernumber="+data.getUsernumber()+" and date='"+data.getDate()+"';";

        //테스트 먼저 해보기
        System.out.println(sql);

        db.execSQL(sql);
        db.close();

        switch (TableName) {
            case WALK:
                updateEXP(data.getUsernumber(), ((QueryWALK)data).getWalk(), 0);
                break;
            case WATER:
                updateEXP(data.getUsernumber(), 0, ((QueryWATER)data).getWater());
                break;
        }

        return true;
    }

    public void delete(String TableName, int usernumber) {
        //유저 넘버로 홀라당 삭제할것
        SQLiteDatabase db = getWritableDatabase();

        String sql = "DELETE FROM " + TableName + " WHERE usernumber=" + usernumber + ";";

        System.out.println(sql);
        db.execSQL(sql);
        db.close();
    }

    public ArrayList<DataForQuery> select(String TableName, int usernumber) {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();

        ArrayList<DataForQuery> resultset = new ArrayList<> ();
        DataForQuery result;
        String sql = "SELECT * FROM " + TableName + " WHERE usernumber=" + usernumber + " order by _seq";

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery(sql, null);

        //이거 각 케이스마다 설정해서 변수로 돌리도록 할것

        switch (TableName){
               case WALK :
                   while (cursor.moveToNext()) {
                       result = new QueryWALK(cursor.getInt(0),
                               cursor.getInt(1),
                               null,
                               cursor.getString(2),
                               cursor.getInt(3));
                       resultset.add(result);
                   }
                   break;
               case WATER :
                   while (cursor.moveToNext()) {
                       result = new QueryWATER(cursor.getInt(0),
                               cursor.getInt(1),
                               null,
                               cursor.getString(2),
                               cursor.getInt(3));
                       resultset.add(result);
                   }
                   break;
               case HEIGHT :
                   while (cursor.moveToNext()) {
                       result = new QueryHEIGHT(cursor.getInt(0),
                               cursor.getInt(1),
                               null,
                               cursor.getString(2),
                               cursor.getFloat(3));
                       resultset.add(result);
                   }
                   break;
               case WEIGHT :
                while (cursor.moveToNext()) {
                    result = new QueryWEIGHT(cursor.getInt(0),
                            cursor.getInt(1),
                            null,
                            cursor.getString(2),
                            cursor.getFloat(3));
                    resultset.add(result);
                }
                break;
            case GPS :  //
                while (cursor.moveToNext()) {
                    result = new QueryGPS(cursor.getInt(0),
                            cursor.getInt(1),
                            null,
                            cursor.getString(2),
                            cursor.getFloat(3),
                            cursor.getString(4),
                            cursor.getString(5),
                            cursor.getString(6),
                            cursor.getString(7));
                    resultset.add(result);
                }
                break;
            case SSAK :  //
                while (cursor.moveToNext()) {
                    result = new QuerySSAK(cursor.getInt(0),
                            cursor.getInt(1),
                            null,
                            cursor.getString(2),
                            cursor.getInt(3),
                            cursor.getInt(4),
                            cursor.getInt(5),
                            cursor.getInt(6));
                    resultset.add(result);
                }
                break;
            case REWARD :
                //(_seq INTEGER PRIMARY KEY AUTOINCREMENT,
                // usernumber INTEGER,
                // date TEXT,
                // reward INTEGER,
                // currlevel INTEGER,
                // tpoint INTEGER)
                while (cursor.moveToNext()) {
                    result = new QueryREWARD(cursor.getInt(0),
                            cursor.getInt(1),
                            null,
                            cursor.getString(2),
                            cursor.getInt(3),
                            cursor.getInt(4),
                            cursor.getInt(5));
                    resultset.add(result);
                }
                break;
        }

        db.close();
        cursor.close();
        return resultset;
    }



    public void CreateTestData_walk (int howMuch) {

        SimpleDateFormat fm1 = new SimpleDateFormat("yy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        int random_walk;
        String sql;

        SQLiteDatabase db = getWritableDatabase();


       /* calendar.add(Calendar.DATE, -1);
        Date date = calendar.getTime();
        System.out.println(fm1.format(date));
        calendar.add(Calendar.DATE, -1);
        date = calendar.getTime();
        System.out.println(fm1.format(date));*/

        calendar.add(Calendar.DATE, -howMuch);
        for(int i = 0 ; i < howMuch ; i++) {

            Date date = calendar.getTime();
            random_walk = (int)(Math.random()*10000)+1; //1~10000 걸음사이

            sql = "INSERT INTO WALK VALUES(null, "+loginUserData.getUsernumber()+", '" + fm1.format(date) + "', " + random_walk +")";

            System.out.println("걸음 가데이터 : " + sql);
            db.execSQL(sql);

            calendar.add(Calendar.DATE, +1);
        }

        db.close();
    }

    public void CreateTestData_water (int howMuch) {

        SimpleDateFormat fm1 = new SimpleDateFormat("yy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        int random_water;
        String sql;

        SQLiteDatabase db = getWritableDatabase();

        calendar.add(Calendar.DATE, -howMuch);
        for(int i = 0 ; i < howMuch ; i++) {

            Date date = calendar.getTime();
            random_water = (int)(Math.random()*3000); //1~3000 ml 사이

            sql = "INSERT INTO WATER VALUES(null, "+loginUserData.getUsernumber()+", '" + fm1.format(date) + "', " + random_water +")";

            System.out.println("물 가데이터 : " + sql);
            db.execSQL(sql);

            calendar.add(Calendar.DATE, 1);
        }

        db.close();
    }

    public void CreateTestData_height (int howMuch, float start_height) {

        SimpleDateFormat fm1 = new SimpleDateFormat("yy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        float random_plus = 0.0f;
        String sql;

        DecimalFormat format = new DecimalFormat(".##");

        SQLiteDatabase db = getWritableDatabase();

        calendar.add(Calendar.DATE, -howMuch);
        for(int i = 0 ; i < howMuch ; i++) {

            Date date = calendar.getTime();
            random_plus += (float)(Math.random()*10)/1000; //0~0.009 사이 랜덤하게 더해짐

            sql = "INSERT INTO HEIGHT VALUES(null, "+loginUserData.getUsernumber()+", '" + fm1.format(date) + "', " +  String.format("%.2f",(start_height+random_plus)) +")";

            System.out.println("키 가데이터 : " + sql);
            db.execSQL(sql);

            calendar.add(Calendar.DATE, 1);
        }

        db.close();
    }

    public void CreateTestData_weight (int howMuch, float start_weight) {

        SimpleDateFormat fm1 = new SimpleDateFormat("yy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        float random_plus = 0.0f;
        String sql;

        DecimalFormat format = new DecimalFormat(".##");
        // String str = format.format(f);

        SQLiteDatabase db = getWritableDatabase();


        calendar.add(Calendar.DATE, -howMuch);
        for(int i = 0 ; i < howMuch ; i++) {

            Date date = calendar.getTime();
            random_plus += (float)(Math.random()*10)/100; //0~0.9 사이 랜덤하게 더해짐

            sql = "INSERT INTO WEIGHT VALUES(null, "+loginUserData.getUsernumber()+", '" + fm1.format(date) + "', " +  String.format("%.2f",(start_weight+random_plus)) +")";

            System.out.println("몸무게 가데이터 : " + sql);
            db.execSQL(sql);

            calendar.add(Calendar.DATE, 1);
        }

        db.close();
    }

    public void CreateTestData_SSAK_Level() {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "INSERT INTO SSAK VALUES(null, "+loginUserData.getUsernumber()
                +", '17-04-30', 0, 0, 1000000, 10)";

        System.out.println("CreateTestData_SSAK_Level 가데이터 : " + sql);
        db.execSQL(sql);

        db.close();

    }

    public void CreateTestData_REWARD_Level() {
        SQLiteDatabase db = getWritableDatabase();

        String sql = "INSERT INTO REWARD VALUES(null, " + loginUserData.getUsernumber()
                + ", '17-04-30', 0, 4, 1000000)";
        System.out.println("CreateTestData_SSAK_Level 가데이터 : " + sql);
        db.execSQL(sql);

        sql = "INSERT INTO REWARD VALUES(null, " + loginUserData.getUsernumber()
                + ", '17-04-30', 1, 4, 1000000)";
        System.out.println("CreateTestData_SSAK_Level 가데이터 : " + sql);
        db.execSQL(sql);

        sql = "INSERT INTO REWARD VALUES(null, " + loginUserData.getUsernumber()
                + ", '17-04-30', 2, 4, 1000000)";
        System.out.println("CreateTestData_SSAK_Level 가데이터 : " + sql);
        db.execSQL(sql);

        db.close();
    }


        // public ArrayList<QueryWALK> getWalkMonth(Integer usernumber, String Y_Month) {
   public int getWalkMonth(Integer usernumber, String Y_Month) {
        ArrayList<QueryWALK> returns = new ArrayList<>();
        int add_all_walk = 0;
        String sql = "SELECT _seq, date, walk FROM WALK WHERE usernumber=" + usernumber + " AND date like '%" + Y_Month+"%'";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        QueryWALK walk;
        while(cursor.moveToNext()){
            walk = new QueryWALK(cursor.getInt(0), null, null, cursor.getString(1), cursor.getInt(2));
            add_all_walk += cursor.getInt(2);
            //returns.add(walk);
        }
        db.close(); cursor.close();
        //return returns;
       return add_all_walk;
    }

    public int getWaterMonth(Integer usernumber, String Y_Month) {
        ArrayList<QueryWALK> returns = new ArrayList<>();
        int add_all_water = 0;
        String sql = "SELECT _seq, date, water FROM WATER WHERE usernumber=" + usernumber + " AND date like '%" + Y_Month+"%'";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        QueryWALK walk;
        while(cursor.moveToNext()){
            walk = new QueryWALK(cursor.getInt(0), null, null, cursor.getString(1), cursor.getInt(2));
            add_all_water += cursor.getInt(2);
            //returns.add(walk);
        }
        db.close(); cursor.close();
        //return returns;
        return add_all_water;
    }


}

