package com.example.kitcoop.lastproject1.Fragment;

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
 * Created by kitcoop on 2017-04-18.
 */

public class postDataChangePW extends Thread {
    Handler backHandler;
    Handler mainHandler;
    String jsonData;
    String line = "";


    public postDataChangePW(Handler mainHandler)
    {
        this.mainHandler = mainHandler;
    }

    @Override
    public void run() {
        Looper.prepare();
        backHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                if (msg.what == 400) {
                    jsonData = (String) msg.obj;
                    HttpURLConnection conn = null;
                    OutputStream os = null;
                    BufferedReader br = null;

                    try {
                        String data = "data=" + jsonData;

                        String getURL = "http://"+ SharedConstant.IP+"/Tragramer/changepw_json.jsp";

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
                            mainHandler.handleMessage(mainMsg);

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (os != null) try {os.close();} catch (IOException e) {}
                        if (br != null) try {br.close();} catch (IOException e) {}
                        if (conn != null) conn.disconnect();
                    }
                }
            }
        };

        Looper.loop();
    }
}