package com.example.kitcoop.lastproject1.Fragment;
/*

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
*/

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.example.kitcoop.lastproject1.DATA.SharedConstant;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/* Created by kitcoop on 2017-04-18.
*/
/*
public class postImageSend extends Thread {
    Handler backHandler;
    Handler mainHandler;
    String jsonData;
    String filepath;
    Bitmap bitmap;
    File profile;
    String returnString = "";
    String boundary = "*";

    HttpURLConnection connection = null;
    OutputStream os = null;


    @Override
    public void run() {
        Looper.prepare();
        backHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                if (msg.what == 600) {

                    filepath = (String) msg.obj;

                    HttpClient client = new DefaultHttpClient(); // httpClient 생성
                    String url = "http://192.168.0.76:8080/Tragramer/upload_ok.jsp";
                    HttpPost post = new HttpPost(url); // httpPost 생성

                    // FileBody 객체를 이용해서 파일을 받아옴
                    File glee = new File(filepath); // file 객체 생성
                    FileBody bin = new FileBody(glee); // FileBody 생성

                    MultipartEntity multipart = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

                    multipart.addPart("images", bin); //실제 파일을 multipart에 넣는다.

                    post.setEntity(multipart); // Multipart를 post 형식에 담음
                    try {
                        client.execute(post);   // post 형식의 데이터를 서버로 전달
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            }
        };
        Looper.loop();
    }
} */
/**
 * Created by kitcoop on 2017-04-18.
 */


public class postImageSend extends Thread {
    public Handler backHandler;
    Handler mainHandler;
    String imgdata;
    String line = "";

    String filepath; //= getActivity().getFilesDir() + profilePic_dir +"/"+ loginUserData.getUsernumber()+".jpg";
    StringBuffer fileData = new StringBuffer(1000);
    BufferedReader reader; //= new BufferedReader( new InputStreamReader(new FileInputStream("C:/Test.xml"),"utf-8"));



    @Override
    public void run() {
        Looper.prepare();
        backHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                    System.out.println("웹서버에 파일 저장 시도1");
                if (msg.what == 600) {
                    System.out.println("웹서버에 파일 저장 시도2");

                    HttpURLConnection conn = null;
                    OutputStream os = null;
                    DataOutputStream dos = null;
                    BufferedReader br = null;
                    FileInputStream mFileInputStream = null;

                    try {
                        String getURL = "http://"+ SharedConstant.IP+"/Tragramer/upload_ok2.jsp";

                        URL url = new URL(getURL);
                        conn = (HttpURLConnection) url.openConnection();
                        if (conn != null) {

                            conn.setReadTimeout(10000);
                            conn.setConnectTimeout(15000);
                            conn.setRequestMethod("POST");//
                            conn.setDoOutput(true);//
                            conn.setDoInput(true);

                            //////////////////////////////////////////////
                            filepath = (String) msg.obj;
                            File sourceFile = new File(filepath);

                            String lineEnd = "\r\n";
                            String twoHyphens = "--";
                            String boundary = "*****";

                            conn.setRequestProperty("Connection", "Keep-Alive");
                            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                            conn.setRequestProperty("uploaded_file", filepath);

                            dos = new DataOutputStream(conn.getOutputStream());
                            dos.writeBytes(twoHyphens + boundary + lineEnd);
                            dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + filepath + "\"" + lineEnd);
                            dos.writeBytes(lineEnd);

                            System.out.println("파일패스 확인한당 :" +  filepath) ;

                            mFileInputStream = new FileInputStream(sourceFile);

                            int bytesAvailable = mFileInputStream.available();
                            int maxBufferSize = 1024 * 1024 * 10;
                            int bufferSize = Math.min(bytesAvailable, maxBufferSize);


                            byte[] buffer = new byte[bufferSize];
                            int bytesRead = mFileInputStream.read(buffer, 0, bufferSize);


                            while (bytesRead > 0) {
                                dos.write(buffer, 0, bufferSize);
                                bytesAvailable = mFileInputStream.available();
                                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                                bytesRead = mFileInputStream.read(buffer, 0, bufferSize);
                                dos.flush();
                            }

                            dos.writeBytes(lineEnd);
                            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                            dos.flush();


                            /////////////////////////


                            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));

                            String result = new String();

                            while ((line = br.readLine()) != null) {
                                result += (line + "\n");
                            }

                            //Message mainMsg = Message.obtain();
                            //mainMsg.obj = result;
                            //mainHandler.handleMessage(mainMsg);

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (os != null) try {os.close();} catch (IOException e) {}
                        if (mFileInputStream != null) try {mFileInputStream.close();} catch (IOException e) {}
                        //if (dos != null) try {dos.close();} catch (IOException e) {}
                        if (br != null) try {br.close();} catch (IOException e) {}
                        if (conn != null) conn.disconnect();
                    }
                }
            }
        };

        Looper.loop();
    }
}
