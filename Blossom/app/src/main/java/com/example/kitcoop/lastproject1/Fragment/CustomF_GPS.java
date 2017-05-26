package com.example.kitcoop.lastproject1.Fragment;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.example.kitcoop.lastproject1.DATA.SharedConstant;
import com.example.kitcoop.lastproject1.DATA.SharedObjct;
import com.example.kitcoop.lastproject1.DB.DBHelper;
import com.example.kitcoop.lastproject1.DB.QueryGPS;
import com.example.kitcoop.lastproject1.Gps.GpsThread;
import com.example.kitcoop.lastproject1.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by kitcoop on 2017-04-11.
 */

public class CustomF_GPS extends Fragment implements OnMapReadyCallback, SharedConstant, SharedObjct{
    private GpsThread gpsThread;
    private TextView tv1;
    private TextView tv2;
    private ImageButton gpsBtn;
    private ImageButton gpsBtn2;
    private Button mapBtn;
    private Button btn;
    private int mainTime;
    private MapView mapView;
    private GoogleMap mMap;
    private PolylineOptions polylineOptions;
    private CameraPosition cp;
    private DBHelper dbHelper;
    private String filePath;
    private String strTime;
    private int distance;
    private float distance2;
    private String sTime;

    postImageSend pdmapimg;

    View view;
    ImageView iv;


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        polylineOptions = new PolylineOptions();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        System.out.println("onCreateView() 호출");

        //final View view = inflater.inflate(R.layout.fragment, container, false);   //맨 앞자리는 디자인자리
        view = inflater.inflate(R.layout.fragment_gps, container, false);   //맨 앞자리는 디자인자리


        iv = ((ImageView)(view.findViewById(R.id.imageView6)));
        iv.setImageResource(R.drawable.walk);

        tv1 = (TextView)view.findViewById(R.id.textView18);
        tv2 = (TextView)view.findViewById(R.id.textView19);
        btn = (Button)view.findViewById(R.id.button);
        gpsBtn = (ImageButton)view.findViewById(R.id.gpsBtn);
        gpsBtn2 = (ImageButton)view.findViewById(R.id.gpsBtn2);
        mapBtn = (Button)view.findViewById(R.id.mapBtn);
        mapView = (MapView)view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        try
        {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        /*mapBtn.setVisibility(View.GONE);*/
        mapView.getMapAsync(this);
        mapView.setVisibility(View.INVISIBLE);
        setImage(view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        System.out.println("onActivityCreated() 호출");

        dbHelper = SHARD_DB.getDbHelper();

        gpsThread = new GpsThread(getActivity(), handler);
        Thread thread = new Thread(gpsThread);
        thread.setDaemon(true);
        thread.start();

        pdmapimg = new postImageSend();
        pdmapimg.setDaemon(true);
        pdmapimg.start();

        gpsBtn.setImageResource(R.drawable.start);
        gpsBtn.setTag("stop");
        gpsBtn2.setVisibility(View.GONE);
        gpsBtn2.setImageResource(R.drawable.stop);

        gpsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (gpsBtn.getTag().toString()) {
                    case "stop":

                        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(iv);
                        Glide.with(CustomF_GPS.this).load(R.raw.walk).into(imageViewTarget);

                        gpsThread.backHandler.sendEmptyMessage(0);
                        handler.sendEmptyMessage(0);
                        gpsBtn.setImageResource(R.drawable.pause);
                        gpsBtn.setTag("start");
                        gpsBtn2.setVisibility(View.GONE);
                        break;

                    case "start":

                        iv.setImageResource(R.drawable.walk);

                        gpsThread.backHandler.sendEmptyMessage(1);
                        handler.sendEmptyMessage(1);
                        gpsBtn.setImageResource(R.drawable.start);
                        gpsBtn.setTag("stop");
                        gpsBtn2.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        gpsBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(distance < 100) {
                    System.out.println("이동거리 변화가 짧아서 저장할 수 없습니다");
                } else {
                    try {
                        CaptureMapScreen();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapView.setVisibility(View.VISIBLE);
                mapBtn.setVisibility(View.VISIBLE);
            }
        });

        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapView.setVisibility(View.INVISIBLE);
                mapBtn.setVisibility(View.GONE);
            }
        });
    }

    public void CaptureMapScreen() {

        GoogleMap.SnapshotReadyCallback callback = new GoogleMap.SnapshotReadyCallback() {
            Bitmap bitmap;
            @Override
            public void onSnapshotReady(Bitmap snapshot) {
                System.out.println("맵캡쳐 실행2");
                // TODO Auto-generated method stub
                bitmap = snapshot;
                try {
                    filePath = "MyMapScreen" + today_hms_sdf.format(new Date()).replace(":","-").replace(" ","_")
                            + ".png";
                    FileOutputStream out = new FileOutputStream(getActivity().getFilesDir() + profilePic_dir + "/" + filePath);

                    // above "/mnt ..... png" => is a storage path (where image will be stored) + name of image you can customize as per your Requirement

                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                    System.out.println("맵캡쳐 완료");
                    gpsThread.backHandler.sendEmptyMessage(2);


                    //////////////////


                    Message msg3 = Message.obtain();
                    msg3.what = 600;
                    msg3.obj = getActivity().getFilesDir() + profilePic_dir + "/"+filePath;

                    pdmapimg.backHandler.sendMessage(msg3);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        mMap.snapshot(callback);
        System.out.println("맵캡쳐 실행3");

    }



    public void setImage (View v) {


        ((ImageView)(v.findViewById(R.id.txt_gps))).setImageResource(R.drawable.txt_gps);


       /* ImageView iv = ((ImageView)(v.findViewById(R.id.imageView6)));
        iv.setImageResource(R.drawable.walk);*/
       /* ImageView iv = ((ImageView)(v.findViewById(R.id.imageView6)));

        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(iv);
        Glide.with(CustomF_GPS.this).load(R.raw.walk).into(imageViewTarget);*/

       //((ImageView)v.findViewById(R.id.imageView2)).setImageResource(R.drawable.rewardline);
        ((ImageView)v.findViewById(R.id.line)).setImageResource(R.drawable.line);
    }

    Handler handler = new Handler(){
        public void handleMessage(Message msg){
            super.handleMessage(msg);

            int div = msg.what;

            switch (div) {
                case 0:
                    int min = mainTime / 60;
                    int sec = mainTime % 60;
                    strTime = String.format("%02d : %02d", min, sec);
                    this.sendEmptyMessageDelayed(0, 1000);
                    tv2.setText(strTime);
                    tv2.invalidate();
                    mainTime++;
                    break;
                case 1:
                    this.removeMessages(0);
                    break;
                case 2:
                    distance = msg.arg1;
                    distance2 = (float)(distance / 1000.0);
                    LatLng pt = (LatLng)msg.obj;

                    tv1.setText(distance2 + "Km");
                    polylineOptions.add(pt);

                    Polyline polyline = mMap.addPolyline(polylineOptions);
                    polyline.setColor(Color.BLUE);

                    CameraPosition cp = new CameraPosition.Builder().target((pt)).zoom(18).build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cp));
                    break;
                case 3:
                    sTime = (String)msg.obj;
                    break;
                case 4:
                    String gpsPath = (String)msg.obj;
                    dbHelper.insertGPSdata(distance2, sTime, strTime, filePath, gpsPath);
                    ArrayList<QueryGPS> datas = dbHelper.getGPSwalkDate(loginUserData.getUsernumber());
                    System.out.println(datas.size());


            }

        }
    };


}
