package com.example.kitcoop.lastproject1;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.example.kitcoop.lastproject1.DATA.SharedConstant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.kitcoop.lastproject1.DATA.SharedConstant.profilePic_dir;

/**
 * Created by kitcoop on 2017-04-12.
 */

public class postDataLogin extends Thread {
    Handler backHandler;
    Handler mainHandler;
    String jsonData;
    String line = "";
    Activity activity;

   public postDataLogin (Activity activity, Handler mainHandler) {

       this.mainHandler = mainHandler;
       this.activity = activity;
    }


    @Override
    public void run() {
        Looper.prepare();
            backHandler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    if (msg.what == 100) {
                        jsonData = (String) msg.obj;
                        HttpURLConnection conn = null;
                        OutputStream os = null;
                        BufferedReader br = null;

                        InputStream is2= null;
                        OutputStream os2= null;  //f 파일객체의 파일 경로에 쓰기위한 아웃풋 스트림

                        try {
                            String data = "data=" + jsonData;
                            System.out.println(data);

                            String getURL = "http://"+ SharedConstant.IP+"/Tragramer/login_json.jsp";

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

                                String useraccount = new String();

                                while ((line = br.readLine()) != null) {
                                    result += (line + "\n");
                                }

                                System.out.println(result);

                                if(result.trim().equals("noexist")){
                                        Message mainMsg = Message.obtain();
                                        mainMsg.obj = result;
                                        mainHandler.handleMessage(mainMsg);
                                }else if(result.trim().equals("diff")){
                                        Message mainMsg = Message.obtain();
                                        mainMsg.obj = result;
                                        mainHandler.handleMessage(mainMsg);
                                }else {

                                    Message mainMsg = Message.obtain();
                                    mainMsg.obj = result;
                                    mainHandler.handleMessage(mainMsg);

                                        String usernumber = (result.split("/"))[1];//유저넘버

                                        System.out.println("파일 로딩 시작");

                                        //File f=fileCache.getFile(url);
                                        File dir = new File(  activity.getFilesDir() + profilePic_dir);
                                        File f = new File(  activity.getFilesDir() + profilePic_dir +"/"+ usernumber+".jpg");

                                        String url2 = "http://"+ SharedConstant.IP+"/Tragramer/upload/"+usernumber+".jpg";
                                        //from web
                                        System.out.println(url2);

                                        Bitmap bitmap;
                                        URL imageUrl = new URL(url2);


                                            /////////////////
                                        HttpURLConnection conn2 = (HttpURLConnection)imageUrl.openConnection();
                                        conn.setConnectTimeout(30000);
                                        conn.setReadTimeout(30000);
                                        conn.setInstanceFollowRedirects(true);
                                        is2=conn2.getInputStream();

                                    //여기서 파일객체 경로에 파일이 없으면 생성 해 준 후 처리할것
                                        if(!dir.exists()){
                                            dir.mkdir();
                                            f.createNewFile();
                                        }

                                        os2 = new FileOutputStream(f);  //f 파일객체의 파일 경로에 쓰기위한 아웃풋 스트림


                                        final int buffer_size=1024;

                                        byte[] bytes=new byte[buffer_size];
                                        for(;;) {
                                            int count=is2.read(bytes, 0, buffer_size);
                                            if(count==-1)
                                                break;
                                            os2.write(bytes, 0, count);      //파일을 저장한다.
                                            System.out.println("save");
                                        }

                                        bitmap = decodeFile(f);


                                        /////////////////

                                }
                            }

                        }

                        catch (FileNotFoundException f) { System.out.println("없다");}
                        catch (IOException e) { e.printStackTrace(); }
                        catch(Exception ex) {}
                        finally {
                                if (os != null) try {os.close();} catch (IOException e) {}
                                if (os2 != null) try {os2.close();} catch (IOException e) {}
                                if (is2 != null) try {is2.close();} catch (IOException e) {}
                                if (br != null) try {br.close();} catch (IOException e) {}
                                if (conn != null) conn.disconnect();
                        }
                    }
                }
            };

        Looper.loop();
    }

        private Bitmap decodeFile(File f){
            try {
                //decode image size
                BitmapFactory.Options o = new BitmapFactory.Options();
                o.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(new FileInputStream(f),null,o);

                //Find the correct scale value. It should be the power of 2.
                final int REQUIRED_SIZE=70;
                int width_tmp=o.outWidth, height_tmp=o.outHeight;
                int scale=1;
                while(true){
                    if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
                        break;
                    width_tmp/=2;
                    height_tmp/=2;
                    scale*=2;
                }

                //decode with inSampleSize
                BitmapFactory.Options o2 = new BitmapFactory.Options();
                o2.inSampleSize=scale;
                return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
            } catch (FileNotFoundException e) {}
            return null;
        }
}
