package com.example.kitcoop.lastproject1;

import android.os.Handler;
import android.os.Message;

import com.example.kitcoop.lastproject1.DATA.SharedConstant;
import com.example.kitcoop.lastproject1.DATA.SharedObjct;
import com.example.kitcoop.lastproject1.DB.DBHelper;
import com.example.kitcoop.lastproject1.DB.DataForQuery;
import com.example.kitcoop.lastproject1.DB.QueryGPS;
import com.example.kitcoop.lastproject1.DB.QueryHEIGHT;
import com.example.kitcoop.lastproject1.DB.QueryREWARD;
import com.example.kitcoop.lastproject1.DB.QuerySSAK;
import com.example.kitcoop.lastproject1.DB.QueryWALK;
import com.example.kitcoop.lastproject1.DB.QueryWATER;
import com.example.kitcoop.lastproject1.DB.QueryWEIGHT;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by kitcoop on 2017-04-27.
 */

public class sync implements SharedConstant, SharedObjct{

    String result = "";
    boolean sync_get = false;
    String sync_table;
    boolean get_finish = false;

    DBHelper dbHelper = SHARD_DB.getDbHelper();

    final postSyncData pd = new postSyncData(new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(sync_get){   //받을때
            }
            else {      //보낼때
                result = ((String) msg.obj).trim();
                if (result.equals("fail")) {
                } else if (result.equals("success")) {
                }
            }
        }
    });

    final postSyncDataGet pdg = new postSyncDataGet(new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(sync_get){   //받을때
                //작업..
                result= ((String)msg.obj).trim();


                System.out.println("받을 데이터 결과 " + result.split("//").length);


                JSONObject jsonObject;

                try {
                    dbHelper.delete(WEIGHT, loginUserData.getUsernumber());
                    dbHelper.delete(HEIGHT, loginUserData.getUsernumber());
                    dbHelper.delete(WALK, loginUserData.getUsernumber());
                    dbHelper.delete(WATER, loginUserData.getUsernumber());
                    dbHelper.delete(REWARD, loginUserData.getUsernumber());
                    dbHelper.delete(GPS, loginUserData.getUsernumber());
                    dbHelper.delete(SSAK, loginUserData.getUsernumber());

                    for(int j= 0 ; j < result.split("//").length ; j++) {
                        String data = result.split("//")[j];
                        JSONArray jsonArray = new JSONArray(data.split("==")[1]);
                        switch (data.split("==")[0]) {
                            case WEIGHT: System.out.println(data.split("==")[1]);
                                QueryWEIGHT weight;
                                ArrayList<DataForQuery> weightes = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    jsonObject = jsonArray.getJSONObject(i);
                                    weight = new QueryWEIGHT(null, loginUserData.getUsernumber(), null, jsonObject.getString("date"), Float.parseFloat(jsonObject.getString("weight")));
                                    weightes.add(weight);
                                }
                                 dbHelper.insertALL(WEIGHT, weightes);
                                break;
                            case HEIGHT: System.out.println(data.split("==")[1]);
                                QueryHEIGHT height;

                                ArrayList<DataForQuery> heights = new ArrayList<>();
                                for (int i2 = 0; i2 < jsonArray.length(); i2++) {
                                        jsonObject = jsonArray.getJSONObject(i2);
                                        height = new QueryHEIGHT(null, loginUserData.getUsernumber(), null, jsonObject.getString("date"), Float.parseFloat(jsonObject.getString("height")));
                                        heights.add(height);
                                }
                                dbHelper.insertALL(HEIGHT, heights);

                                break;
                            case WALK: System.out.println(data.split("==")[1]);
                                QueryWALK walk;
                                ArrayList<DataForQuery> walks = new ArrayList<>();
                                for (int i2 = 0; i2 < jsonArray.length(); i2++) {
                                        jsonObject = jsonArray.getJSONObject(i2);
                                        walk = new QueryWALK(null, loginUserData.getUsernumber(), null, jsonObject.getString("date"), Integer.parseInt(jsonObject.getString("walk")));
                                        walks.add(walk);
                                }
                                dbHelper.insertALL(WALK, walks);

                                break;
                            case WATER: System.out.println(data.split("==")[1]);
                                QueryWATER water;
                                ArrayList<DataForQuery> waters = new ArrayList<>();
                                for (int i2 = 0; i2 < jsonArray.length(); i2++) {
                                    jsonObject = jsonArray.getJSONObject(i2);
                                    water = new QueryWATER(null, loginUserData.getUsernumber(), null, jsonObject.getString("date"), Integer.parseInt(jsonObject.getString("water")));
                                    waters.add(water);
                                }
                                dbHelper.insertALL(WATER, waters);
                                break;
                            case REWARD: System.out.println(data.split("==")[1]);
                                QueryREWARD reward;
                                ArrayList<DataForQuery> rewards = new ArrayList<>();
                                for (int i2 = 0; i2 < jsonArray.length(); i2++) {
                                    jsonObject = jsonArray.getJSONObject(i2);
                                    reward = new QueryREWARD(null, loginUserData.getUsernumber(), null, jsonObject.getString("date")
                                            , Integer.parseInt(jsonObject.getString("reward")), jsonObject.getInt("currlevel"), jsonObject.getInt("tpoint"));
                                    rewards.add(reward);
                                }
                                dbHelper.insertALL(REWARD, rewards);
                                break;
                            case GPS: System.out.println(data.split("==")[1]);
                                QueryGPS gps;
                                ArrayList<DataForQuery> gpss = new ArrayList<>();
                                for (int i2 = 0; i2 < jsonArray.length(); i2++) {
                                    jsonObject = jsonArray.getJSONObject(i2);
                                    gps = new QueryGPS(null, loginUserData.getUsernumber(), null, jsonObject.getString("date")
                                            , Float.parseFloat(jsonObject.getString("distance")), jsonObject.getString("stime"), jsonObject.getString("ttime"),
                                            jsonObject.getString("mapimg"), jsonObject.getString("path"));
                                    gpss.add(gps);
                                }
                                dbHelper.insertALL(GPS, gpss);
                                break;
                            case SSAK: System.out.println(data.split("==")[1]);
                                QuerySSAK ssak;
                                ArrayList<DataForQuery> ssaks = new ArrayList<>();
                                for (int i2 = 0; i2 < jsonArray.length(); i2++) {
                                    jsonObject = jsonArray.getJSONObject(i2);
                                    ssak = new QuerySSAK(null, loginUserData.getUsernumber(), null, jsonObject.getString("date")
                                            , jsonObject.getInt("exp_today_water"), jsonObject.getInt("exp_today_walk"), jsonObject.getInt("total_exp"),
                                            jsonObject.getInt("level"));
                                    ssaks.add(ssak);
                                }
                                dbHelper.insertALL(SSAK, ssaks);
                                break;
                        }


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                }


            }
            else {      //보낼때

            }
            get_finish = true;
        }
    });


    public sync() {
        pd.setDaemon(true);
        pd.start();

        pdg.setDaemon(true);
        pdg.start();
    }

    public boolean sync_data_into_db(String Table) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        try {
            ArrayList<DataForQuery> datas = dbHelper.select(Table, loginUserData.getUsernumber());
            DataForQuery data;

            for (int i = 0; i < datas.size(); i++) {
                data = datas.get(i);
                System.out.println(i);
                jsonObject = new JSONObject();
                jsonObject.put("usernumber", data.getUsernumber());
                jsonObject.put("date", data.getDate());
                switch (Table) {
                    case WALK :
                        jsonObject.put("walk", ((QueryWALK)data).getWalk());
                        break;
                    case WATER : jsonObject.put("water", ((QueryWATER)data).getWater());
                        break;
                    case HEIGHT : jsonObject.put("height",  String.format("%.2f",((QueryHEIGHT)data).getHeight()));
                        break;
                    case WEIGHT : jsonObject.put("weight",  String.format("%.2f",((QueryWEIGHT)data).getWeight()));
                        break;
                    case REWARD  : //reward INTEGER, currlevel INTEGER, tpoint INTEGER
                        jsonObject.put("reward", ((QueryREWARD)data).getReward());
                        jsonObject.put("currlevel", ((QueryREWARD)data).getCurrLevel());
                        jsonObject.put("tpoint", ((QueryREWARD)data).getTPoint());
                        break;
                    case GPS : //distance REAL, stime TEXT, ttime TEXT, mapimg TEXT, path TEXT
                        jsonObject.put("distance", String.format("%.2f",((QueryGPS)data).getDistance()));
                        jsonObject.put("stime", ((QueryGPS)data).getStime());
                        jsonObject.put("ttime", ((QueryGPS)data).getTtime());
                        jsonObject.put("mapimg", ((QueryGPS)data).getMAP_imgfile_name());
                        jsonObject.put("path", ((QueryGPS)data).getGPSpath());
                        break;
                    case SSAK : //exp_today_water INTEGER, exp_today_walk INTEGER, total_exp INTEGER, level INTEGER
                        jsonObject.put("exp_today_water", ((QuerySSAK)data).getExp_today_water());
                        jsonObject.put("exp_today_walk", ((QuerySSAK)data).getExp_today_walk());
                        jsonObject.put("total_exp", ((QuerySSAK)data).getTotal_exp());
                        jsonObject.put("level", ((QuerySSAK)data).getLevel());
                        break;
                }

                jsonArray.put(jsonObject);
            }

            Message msg = Message.obtain();
            switch (Table) {
                case WEIGHT : msg.what = 10001;
                    break;
                case HEIGHT : msg.what = 10002;
                    break;
                case WALK : msg.what = 10003;
                    break;
                case WATER : msg.what = 10004;
                    break;
                case REWARD : msg.what = 10005;
                    break;
                case GPS : msg.what = 10006;
                    break;
                case SSAK : msg.what = 10007;
                    break;
            }

            msg.obj = jsonArray.toString();

            pd.backHandler.sendMessage(msg);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean sync_data_from_db(String Table) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        sync_get = true;

        try {
            jsonObject.put("usernumber", loginUserData.getUsernumber());


            Message msg = Message.obtain();
            switch (Table) {
                case WEIGHT: msg.what = 20001; sync_table = WEIGHT; break;
                case HEIGHT: msg.what = 20002; sync_table = HEIGHT; break;
                case WALK:   msg.what = 20003; sync_table = WALK; break;
                case WATER:  msg.what = 20004; sync_table = WATER; break;
                case REWARD: msg.what = 20005; sync_table = REWARD; break;
                case GPS:    msg.what = 20006; sync_table = GPS; break;
                case SSAK:   msg.what = 20007; sync_table = SSAK; break;
            }
            jsonArray.put(jsonObject);

            //msg.obj = jsonArray.toString();
            msg.obj = loginUserData.getUsernumber() + "";
            pdg.backHandler.sendMessage(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        } /*catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        return true;
    }
}
