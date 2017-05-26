package com.example.kitcoop.lastproject1.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kitcoop.lastproject1.Activity_Drawer;
import com.example.kitcoop.lastproject1.DATA.SharedConstant;
import com.example.kitcoop.lastproject1.DATA.SharedObjct;
import com.example.kitcoop.lastproject1.DB.DBHelper;
import com.example.kitcoop.lastproject1.R;
import com.example.kitcoop.lastproject1.pedometerService.BusProvider;
import com.example.kitcoop.lastproject1.pedometerService.MyService;
import com.squareup.otto.Subscribe;

/**
 * Created by kitcoop on 2017-03-27.
 */

//생명주기 체크
public class CustomF_Measure extends Fragment implements SharedObjct, SharedConstant{

    DBHelper dbHelper = SHARD_DB.getDbHelper();

    private int step;
    private int dbStep;
    private TextView stepCountTextView;
    private Button stepServiceOnOff;
    //private ImageButton stepServiceOnOff;
    private View view;

    int setting_water = 100;    //한번에 올릴 물양 설정값 받아올수 있도록 조취

    SharedPreferences setting;
    SharedPreferences.Editor editor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        setting = getActivity().getSharedPreferences("setting", 0);
        editor = setting.edit();


        //final View view = inflater.inflate(R.layout.fragment, container, false);   //맨 앞자리는 디자인자리
        View view = inflater.inflate(R.layout.fragment_measure, container, false);   //맨 앞자리는 디자인자리

        setImageANDText(view);
        AddAllButtonClickListener(view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        System.out.println("onActivityCreated 호출");

        dbStep = dbHelper.selectTodayWalk(loginUserData.getUsernumber());

        stepCountTextView = (TextView)view.findViewById(R.id.stepCountTextView);
        stepCountTextView.setText(dbStep + "");

        //stepServiceOnOff = (ImageButton)view.findViewById(R.id.stepServiceOnOff);
        stepServiceOnOff = (Button)view.findViewById(R.id.stepServiceOnOff);



        if(Activity_Drawer.isServiceCheck() == true) {
            BusProvider.getInstance().register(CustomF_Measure.this);
            Intent intent = new Intent(getActivity(), MyService.class);
            intent.setFlags(2);
            getActivity().startService(intent);
//            stepServiceOnOff.setImageResource(R.drawable.stop);
            stepServiceOnOff.setText("■");
            stepServiceOnOff.setTag("start");
        } else {
//            stepServiceOnOff.setImageResource(R.drawable.start);
            stepServiceOnOff.setText("▶");
            stepServiceOnOff.setTag("stop");
        }

        stepServiceOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 서비스 시작 / 정지
                System.out.println("걸음 측정 버튼 클릭");
                if(((String)stepServiceOnOff.getTag()).equals("stop")) {
                    System.out.println("걸음 측정 시작");
                    dbStep = dbHelper.selectTodayWalk(loginUserData.getUsernumber());
                    BusProvider.getInstance().register(CustomF_Measure.this);
                    Intent intent = new Intent(getActivity(), MyService.class);
                    getActivity().startService(intent);
                    //stepServiceOnOff.setImageResource(R.drawable.stop);
                    stepServiceOnOff.setText("■");
                    stepServiceOnOff.setTag("start");
                    Activity_Drawer.setServiceCheck(true);


                } else {
                    System.out.println("걸음 측정 정지");
                    BusProvider.getInstance().unregister(CustomF_Measure.this);
                    Intent intent = new Intent(getActivity(), MyService.class);
                    getActivity().stopService(intent);
                    //stepServiceOnOff.setImageResource(R.drawable.start);
                    stepServiceOnOff.setText("▶");
                    stepServiceOnOff.setTag("stop");
                    Activity_Drawer.setServiceCheck(false);
                }
            }
        });

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(Activity_Drawer.isServiceCheck() == true) {
            Intent intent = new Intent(getActivity(), MyService.class);
            intent.setFlags(1);
            getActivity().startService(intent);
        }
    }

    private void setImageANDText (View v) {

        ((ImageView)(v.findViewById(R.id.txt_measure))).setImageResource(R.drawable.txt_measure);

        ((ImageView)(v.findViewById(R.id.challengeImage1))).setImageResource(R.drawable.shoes);
        ((ImageView)v.findViewById(R.id.challengeImage2)).setImageResource(R.drawable.drop);

        TextView waterCount =  (TextView)v.findViewById(R.id.f_measure_tv_waterCount);
        DBHelper dbHelper = SHARD_DB.getDbHelper();
        waterCount.setText(dbHelper.selectTodayWater(loginUserData.getUsernumber()).toString());

        TextView weightWrite =  (TextView)v.findViewById(R.id.f_measure_tv_weight);
        weightWrite.setText(dbHelper.selectTodayWeight(loginUserData.getUsernumber())+"");

        TextView heightWrite =  (TextView)v.findViewById(R.id.f_measure_tv_height);
        heightWrite.setText(dbHelper.selectTodayHeight(loginUserData.getUsernumber())+"");

        ((TextView)v.findViewById(R.id.challengeResult1)).setText(" / " + setting.getString("TODAY_C_WALK", "10000") + " 걸음");
        ((TextView)v.findViewById(R.id.challengeResult2)).setText(" / " + setting.getString("TODAY_C_WATER", "1000") + " mL");
        //((TextView)v.findViewById(R.id.today_total_walk)).setText();  //서비스쪽에서
        ((TextView)v.findViewById(R.id.today_total_water)).setText(dbHelper.selectTodayWater(loginUserData.getUsernumber()).toString());
        ((TextView)v.findViewById(R.id.f_measure_tv_weight)).setText( setting.getFloat("TODAY_WEIGHT", 0.0f) + "");
        ((TextView)v.findViewById(R.id.f_measure_tv_height)).setText( setting.getFloat("TODAY_HEIGHT", 0.0f) + "");

        setting_water = Integer.parseInt(setting.getString("DAILY_WATER","100"));
        ((TextView)v.findViewById(R.id.addWater)).setText( setting.getString("DAILY_WATER", "0"));
        ((Button)v.findViewById(R.id.measure_btn_Pwater)).setText( setting.getString("DAILY_WATER", "100") + "mL +");
    }


    private void AddAllButtonClickListener (View view) {
        /*view.findViewById(R.id.measure_btn_Mwater).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView waterCount = (TextView)getView().findViewById(R.id.f_measure_tv_waterCount);

                String water = (String) waterCount.getText();
                Integer iwater = Integer.parseInt(water);

                setting_water = Integer.parseInt(setting.getString("DAILY_WATER","100"));

                if(iwater-setting_water >= 0) {
                    iwater -= setting_water;
                    waterCount.setText(iwater.toString());

                    DBHelper dbHelper = SHARD_DB.getDbHelper();
                    dbHelper.updateTodayDATA(WATER ,loginUserData.getUsernumber(), iwater, 0.0f, 0.0f, 0);
                }
                else
                    Toast.makeText(getActivity(), "물 섭취량은 0이하로 할 수 없습니다.", Toast.LENGTH_SHORT).show();

            }
        });*/

        view.findViewById(R.id.measure_btn_Pwater).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView waterCount = (TextView)getView().findViewById(R.id.f_measure_tv_waterCount);
                TextView today_total_water = (TextView)getView().findViewById(R.id.today_total_water);

                String water = (String) waterCount.getText();
                Integer iwater = Integer.parseInt(water);

                setting_water = Integer.parseInt(setting.getString("DAILY_WATER","100"));

                iwater += setting_water;
                waterCount.setText(iwater.toString());
                today_total_water.setText(iwater.toString());

                dbHelper.updateTodayDATA(WATER ,loginUserData.getUsernumber(), iwater, 0.0f, 0.0f, 0);
            }
        });

        view.findViewById(R.id.setting_challenge).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog cd = new CustomDialog(getActivity());
                cd.setCanceledOnTouchOutside(false);
                cd.ShowDialog_setChallange(getActivity());

            }
        });

        view.findViewById(R.id.measure_set_adjust_water).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog cd = new CustomDialog(getActivity());
                cd.ShowDialog_set_AdjustWater(getActivity());

            }
        });

        view.findViewById(R.id.f_measure_tv_weight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog cd = new CustomDialog(getActivity());
                cd.ShowDialog_set_weightheight(getActivity(), 0);
            }
        });

        view.findViewById(R.id.f_measure_tv_height).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog cd = new CustomDialog(getActivity());
                cd.ShowDialog_set_weightheight(getActivity(), 1);
            }
        });
    }

    @Subscribe
    public void getEvent(MyService.StepEvent stepEvent) {
        step = stepEvent.getMainStep();

        stepCountTextView.setText(dbStep + step + "");
    }


    // 서비스 실행중인지 아닌지 판단

}

