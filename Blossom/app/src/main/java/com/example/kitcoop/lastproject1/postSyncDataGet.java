package com.example.kitcoop.lastproject1;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.example.kitcoop.lastproject1.DATA.SharedConstant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by kitcoop on 2017-04-08.
 */

public class postSyncDataGet extends Thread {
    Handler backHandler;
    Handler mainHandler;
    String jsonData;
    String line = "";

    public postSyncDataGet(Handler mainHandler) {
        this.mainHandler = mainHandler;
    }
    @Override
    public void run() {
        Looper.prepare();
        backHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                HttpURLConnection conn = null;
                OutputStream os = null;
                BufferedReader br = null;
                String data = "";
                String getURL = "";

                if (msg.what / 10 == 2000) {
                    jsonData = (String) msg.obj;

                    if (msg.what == 20001) {
                        jsonData = (String) msg.obj;
                        data = "data_weight=" + jsonData;
                    }
                    if (msg.what == 20002) {
                        jsonData = (String) msg.obj;
                        data = "data_height=" + jsonData;
                    }
                    if (msg.what == 20003) {
                        jsonData = (String) msg.obj;
                        data = "data_walk=" + jsonData;
                    }
                    if (msg.what == 20004) {
                        jsonData = (String) msg.obj;
                        data = "data_water=" + jsonData;
                    }
                    if (msg.what == 20005) {
                        jsonData = (String) msg.obj;
                        data = "data_reward=" + jsonData;
                    }
                    if (msg.what == 20006) {
                        jsonData = (String) msg.obj;
                        data = "data_gps=" + jsonData;
                    }if (msg.what == 20007) {
                        jsonData = (String) msg.obj;
                        data = "data_ssak=" + jsonData;

                    }



                    try {
                        getURL = "http://" + SharedConstant.IP + "/Tragramer/sync_getDate_json.jsp";

                        System.out.println("받을 데이터 " + data);
                        //System.out.println("받을 데이터 " + getURL);

                        if (!getURL.equals("") && !data.equals("")) {
                            URL url = new URL(getURL);
                            conn = (HttpURLConnection) url.openConnection();
                            if (conn != null) {

                                conn.setReadTimeout(10000);
                                conn.setConnectTimeout(15000);
                                conn.setRequestMethod("POST");
                                conn.setDoOutput(true);
                                conn.setDoInput(true);

                                os = conn.getOutputStream();
                                os.write(data.getBytes("utf-8"));
                                os.flush();

                                br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                                String result = new String();

                                while ((line = br.readLine()) != null) {
                                    result += (line + "\n");
                                }

                                Message mainMsg = Message.obtain();
                                mainMsg.obj = result;
                                System.out.println(result);
                                mainHandler.handleMessage(mainMsg);

                            }
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (os != null) try {
                            os.close();
                        } catch (IOException e) {
                        }
                        if (br != null) try {
                            br.close();
                        } catch (IOException e) {
                        }
                        if (conn != null) conn.disconnect();
                    }
                }

            }
        };
        Looper.loop();
    }
}