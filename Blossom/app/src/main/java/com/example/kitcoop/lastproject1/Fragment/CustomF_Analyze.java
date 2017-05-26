package com.example.kitcoop.lastproject1.Fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;

import com.example.kitcoop.lastproject1.DATA.SharedConstant;
import com.example.kitcoop.lastproject1.DATA.SharedObjct;
import com.example.kitcoop.lastproject1.DB.DBHelper;
import com.example.kitcoop.lastproject1.DB.DataForQuery;
import com.example.kitcoop.lastproject1.DB.QueryGPS;
import com.example.kitcoop.lastproject1.DB.QueryHEIGHT;
import com.example.kitcoop.lastproject1.DB.QueryWALK;
import com.example.kitcoop.lastproject1.DB.QueryWATER;
import com.example.kitcoop.lastproject1.DB.QueryWEIGHT;
import com.example.kitcoop.lastproject1.R;
import com.example.kitcoop.lastproject1.Tab4Adapter;
import com.example.kitcoop.lastproject1.Tab4CustomMap;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.example.kitcoop.lastproject1.R.id.chart;
import static com.example.kitcoop.lastproject1.R.id.chart3;

/**
 * Created by kitcoop on 2017-04-07.
 */

public class CustomF_Analyze extends Fragment implements SharedConstant, SharedObjct {
    TabHost tabHost;
    DBHelper dbHelper;
    ArrayList<QueryGPS> datas;
    ListView listView;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {


        //final View view = inflater.inflate(R.layout.fragment, container, false);   //맨 앞자리는 디자인자리
        View view = inflater.inflate(R.layout.fragment_anlaystab, container, false);   //맨 앞자리는 디자인자리
        ((ImageView)(view.findViewById(R.id.txt_anlysis))).setImageResource(R.drawable.txt_anlysis);

        listView = (ListView)view.findViewById(R.id.mapListView);
        //"Tab Space" 태그를 가진 TabSpec 객체 생성


        TabHost tabHost = (TabHost)view.findViewById(R.id.tabhost);
        tabHost.setup();


        //첫번째 탭
        TabHost.TabSpec ts = tabHost.newTabSpec("Tab Space");

        //탭에 표시될 문자열 지정
        ts.setIndicator("걸음수");
        //탭이 눌려졌을때 프레임 레이아웃에 표시될 Content 뷰에 대한 리소스 id 지정
        ts.setContent(R.id.tab1);
        //탭호스트에 탭추가
        tabHost.addTab(ts);


        TabHost.TabSpec ts2 = tabHost.newTabSpec("Tab Space2");
        ts2.setIndicator("물 섭취량");
        ts2.setContent(R.id.tab2);
        tabHost.addTab(ts2);


        TabHost.TabSpec ts3 = tabHost.newTabSpec("Tab Space3");
        ts3.setIndicator("몸무게/키");
        ts3.setContent(R.id.tab3);
        tabHost.addTab(ts3);


        TabHost.TabSpec ts4 = tabHost.newTabSpec("Tab Space4");
        ts4.setIndicator("GPS+");
        ts4.setContent(R.id.tab4);
        tabHost.addTab(ts4);

        btn_set(view);
        Chart_walk_day(view);
        Chart_water_day(view);
        Chart_Weight_day(view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                QueryGPS gpsInfo = datas.get(position);
                String gpsPath = gpsInfo.getGPSpath();
                Intent intent = new Intent(getActivity(), Tab4CustomMap.class);
                intent.putExtra("gpsInfo", gpsPath);
                startActivity(intent);
            }
        });

        dbHelper = SHARD_DB.getDbHelper();
        datas = dbHelper.getGPSwalkDate(loginUserData.getUsernumber());
        if(datas.size() != 0) {

            Button goGPSplus = (Button)getActivity().findViewById(R.id.go_GPSplus);
            goGPSplus.setVisibility(View.GONE);
            Tab4Adapter adapter = new Tab4Adapter(getActivity(), datas);
            listView.setAdapter(adapter);
        }
        else {
            Button goGPSplus = (Button)getActivity().findViewById(R.id.go_GPSplus);
            goGPSplus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fragment = new CustomF_GPS();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                }
            });
        }
    }

    private void btn_set (final View view) {
        ((Button)view.findViewById(R.id.btn_walk_day)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Chart_walk_day(view);
            }
        });
        ((Button)view.findViewById(R.id.btn_walk_month)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Chart_walk_month(view);
            }
        });
        ((Button)view.findViewById(R.id.btn_water_day)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Chart_water_day(view);
            }
        });
        ((Button)view.findViewById(R.id.btn_water_month)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Chart_water_month(view);
            }
        });
        ((Button)view.findViewById(R.id.btn_weight)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Chart_Weight_day(view);
            }
        });
        ((Button)view.findViewById(R.id.btn_height)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Chart_Height_day(view);
            }
        });
    }

    private void Chart_walk_day(View view){
        final BarChart barChart = (BarChart)view.findViewById(chart);

        barChart.clear();

        barChart.zoom(0, 0 ,0, 0);
        barChart.zoom(1,0,0,0);

        dbHelper = SHARD_DB.getDbHelper();
        ArrayList<DataForQuery> walks = dbHelper.select(WALK, loginUserData.getUsernumber());

        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<String>();



        ////---- 입력된 첫날짜 및 날짜 차이 구하기
        Calendar cal_today= Calendar.getInstance();
        Date today = cal_today.getTime();

        if(walks.size() == 0){
            BarDataSet dataset = new BarDataSet(entries, "걸음 수");
            BarData data = new BarData(labels, dataset);
            barChart.setData(data);
            return;
        }

        Calendar cal_firstday= Calendar.getInstance();
        String first_date_cal= (walks.get(0)).getDate();
        Integer year;
        if(first_date_cal.split("-")[0].contains("20")){
            year = Integer.parseInt(first_date_cal.split("-")[0]);
        }
        else
            year = Integer.parseInt("20"+first_date_cal.split("-")[0]);
        Integer month = Integer.parseInt(first_date_cal.split("-")[1]) -1;
        Integer day = Integer.parseInt(first_date_cal.split("-")[2]);

        cal_firstday.set(Calendar.YEAR,  year);
        cal_firstday.set(Calendar.MONTH,  month);
        cal_firstday.set(Calendar.DATE,  day);

        SimpleDateFormat fm1 = new SimpleDateFormat("yy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        Date first_date = cal_firstday.getTime();

        System.out.println(first_date_cal + " / " + sdf.format(first_date).toString() + " / " + sdf.format(today).toString());

        //SimpleDateFormat 을 이용하여 startDate와 endDate의 Date 객체를 생성한다.
        //두날짜 사이의 시간 차이(ms)를 하루 동안의 ms(24시*60분*60초*1000밀리초) 로 나눈다.
        long diffDay = (today.getTime() - first_date.getTime()) / (24*60*60*1000);
        System.out.println(diffDay+"일");
        ////----

        QueryWALK walk;
        Date date;
        //int for_index = walks.size()-1;
        int for_index = 0;
        String checkday;
        String dataday;
        for(int i = 0 ; i < (int)diffDay ; i++) {
            date = cal_firstday.getTime();
            checkday = fm1.format(date).toString();
            dataday = walks.get(for_index).getDate();
            System.out.println(checkday + " / " + dataday);
            labels.add(checkday);
            if( dataday.contains(checkday) && for_index < walks.size()) {   //비교해서 일자 같으면 데이터 빼고 증가시킬것
                //System.out.println("매치 " + checkday + " / " + dataday);
                walk = (QueryWALK) walks.get(for_index);
                entries.add(new BarEntry(walk.getWalk(), for_index));
                for_index++;
            }
            cal_firstday.add(Calendar.DATE, +1);
        }

        BarDataSet dataset = new BarDataSet(entries, "걸음 수");


        BarData data = new BarData(labels, dataset);
        // dataset.setColors(ColorTemplate.COLORFUL_COLORS); //

        int y=1;
        for(int i = 0 ; i < walks.size() ; i+=14)   //대충 10개 정도 보이도록..
            y++;

        barChart.zoom(y, 0 ,0, 0);

        barChart.setPinchZoom(true);    //확대 막기
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.moveViewToX(1000); // 이거찾느라 3시간.....>_<...

        //barChart.z
        //barChart.set
        barChart.setData(data);
        barChart.animateY(1500);
    }

    private void Chart_walk_month(View view){
        final BarChart barChart2 = (BarChart)view.findViewById(chart);

        barChart2.clear();
        barChart2.zoom(0,0,0,0);

        dbHelper = SHARD_DB.getDbHelper();
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<String>();

        ////---- 출력해야하는 달 및 데이터 구하기
        Calendar cal_today= Calendar.getInstance();
        String y_m ;
        Integer year;
        Integer month;

        cal_today.add(Calendar.MONTH, -11);
        for(int i = 0 ; i < 12 ; i++ ) {
            year = cal_today.get(Calendar.YEAR);
            month = cal_today.get(Calendar.MONTH)+1 ;
            y_m = year.toString().substring(2,4) + "-" + (month < 10? "0"+month : month);
            cal_today.add(Calendar.MONTH, +1);
            System.out.println(y_m);
            labels.add(y_m.replace("-", "년 ") + "월");
            entries.add(new BarEntry((dbHelper.getWalkMonth(loginUserData.getUsernumber(), y_m)), i));

            //System.out.println((dbHelper.getWalkMonth(loginUserData.getUsernumber(), y_m)).size());
        }

        //12개월간의 데이터를 출력

        BarDataSet dataset = new BarDataSet(entries, "걸음 수");
        BarData data = new BarData(labels, dataset);
        //dataset.setColors(ColorTemplate.COLORFUL_COLORS); //

        barChart2.setPinchZoom(true);    //확대 막기
        barChart2.setDoubleTapToZoomEnabled(false);
        barChart2.moveViewToX(1000); // 이거찾느라 3시간.....>_<...

        barChart2.setData(data);
        barChart2.animateY(1500);
    }

    private void Chart_water_day(View view){
        final BarChart barChart = (BarChart)view.findViewById(R.id.chart2);

        barChart.clear();

        barChart.zoom(0, 0 ,0, 0);
        barChart.zoom(1,0,0,0);

        dbHelper = SHARD_DB.getDbHelper();
        ArrayList<DataForQuery> waters = dbHelper.select(WATER, loginUserData.getUsernumber());

        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<String>();

        if(waters.size() == 0){
            BarDataSet dataset = new BarDataSet(entries, "걸음 수");
            BarData data = new BarData(labels, dataset);
            barChart.setData(data);
            return;
        }
        System.out.println("야얍" + waters.get(0).getDate());

        ////---- 입력된 첫날짜 및 날짜 차이 구하기
        Calendar cal_today= Calendar.getInstance();
        Date today = cal_today.getTime();

        Calendar cal_firstday= Calendar.getInstance();
        String first_date_cal = (waters.get(0)).getDate();
        Integer year ;
        if(first_date_cal.split("-")[0].contains("20")){
            year = Integer.parseInt(first_date_cal.split("-")[0]);
        }
        else
            year = Integer.parseInt("20"+first_date_cal.split("-")[0]);
        Integer month = Integer.parseInt(first_date_cal.split("-")[1]) -1;
        Integer day = Integer.parseInt(first_date_cal.split("-")[2]);

        cal_firstday.set(Calendar.YEAR,  year);
        cal_firstday.set(Calendar.MONTH,  month);
        cal_firstday.set(Calendar.DATE,  day);

        SimpleDateFormat fm1 = new SimpleDateFormat("yy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        Date first_date = cal_firstday.getTime();

        System.out.println(first_date_cal + " / " + sdf.format(first_date).toString() + " / " + sdf.format(today).toString());

        //SimpleDateFormat 을 이용하여 startDate와 endDate의 Date 객체를 생성한다.
        //두날짜 사이의 시간 차이(ms)를 하루 동안의 ms(24시*60분*60초*1000밀리초) 로 나눈다.
        long diffDay = (today.getTime() - first_date.getTime()) / (24*60*60*1000);
        System.out.println(diffDay+"일");
        ////----

        QueryWATER water;
        Date date;
        //int for_index = waters.size()-1;
        int for_index = 0;
        String checkday;
        String dataday;
        for(int i = 0 ; i < (int)diffDay ; i++) {
            date = cal_firstday.getTime();
            checkday = fm1.format(date).toString();
            dataday = waters.get(for_index).getDate();
            System.out.println(i+ " / water 기록 데이 " + dataday);
            System.out.println(checkday + " / " + dataday);
            labels.add(checkday);
            if( dataday.contains(checkday) && for_index < waters.size()) {   //비교해서 일자 같으면 데이터 빼고 증가시킬것
                System.out.println("매치 " + checkday + " / " + dataday);
                water = (QueryWATER) waters.get(for_index);
                entries.add(new BarEntry(water.getWater(), for_index));
                for_index++;
            }
            cal_firstday.add(Calendar.DATE, +1);
        }


        final BarDataSet dataset = new BarDataSet(entries, " 물 섭취량");

        // ArrayList<String> labels = new ArrayList<String>();
       /* labels.add("January");
        labels.add("February");
        labels.add("March");
        labels.add("April");
        labels.add("May");
        labels.add("June");*/

        BarData data = new BarData(labels, dataset);
        // dataset.setColors(ColorTemplate.COLORFUL_COLORS); //

        int y=1;
        for(int i = 0 ; i < waters.size() ; i+=14)   //대충 10개 정도 보이도록..
            y++;

        barChart.zoom(y, 0 ,0, 0);

        barChart.setPinchZoom(true);    //확대 막기
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.moveViewToX(1000); // 이거찾느라 3시간.....>_<...

        //barChart.z
        //barChart.set
        barChart.setData(data);
        barChart.animateY(1500);
    }

    private void Chart_water_month(View view){
        final BarChart barChart2 = (BarChart)view.findViewById(R.id.chart2);

        barChart2.clear();
        barChart2.zoom(0,0,0,0);

        dbHelper = SHARD_DB.getDbHelper();
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<String>();

        ////---- 출력해야하는 달 및 데이터 구하기
        Calendar cal_today= Calendar.getInstance();
        String y_m ;
        Integer year;
        Integer month;

        cal_today.add(Calendar.MONTH, -11);
        for(int i = 0 ; i < 12 ; i++ ) {
            year = cal_today.get(Calendar.YEAR);
            month = cal_today.get(Calendar.MONTH)+1 ;
            y_m = year.toString().substring(2,4) + "-" + (month < 10? "0"+month : month);
            cal_today.add(Calendar.MONTH, +1);
            System.out.println(y_m);
            labels.add(y_m.replace("-", "년 ") + "월");
            entries.add(new BarEntry((dbHelper.getWaterMonth(loginUserData.getUsernumber(), y_m)), i));

            //System.out.println((dbHelper.getWalkMonth(loginUserData.getUsernumber(), y_m)).size());
        }

        //12개월간의 데이터를 출력

        final BarDataSet dataset = new BarDataSet(entries, "물 섭취량");
        BarData data = new BarData(labels, dataset);
        //dataset.setColors(ColorTemplate.COLORFUL_COLORS); //

        barChart2.setPinchZoom(true);    //확대 막기
        barChart2.setDoubleTapToZoomEnabled(false);
        barChart2.moveViewToX(1000); // 이거찾느라 3시간.....>_<...

        barChart2.setData(data);
        barChart2.animateY(1500);

    }

    private void Chart_Weight_day(View view){
        final BarChart barChart = (BarChart)view.findViewById(chart3);

        barChart.clear();

        barChart.zoom(0, 0 ,0, 0);
        barChart.zoom(1,0,0,0);

        dbHelper = SHARD_DB.getDbHelper();
        ArrayList<DataForQuery> weights = dbHelper.select(WEIGHT, loginUserData.getUsernumber());


        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<String>();

        if(weights.size() == 0){
            BarDataSet dataset = new BarDataSet(entries, "걸음 수");
            BarData data = new BarData(labels, dataset);
            barChart.setData(data);
            return;
        }


        ////---- 입력된 첫날짜 및 날짜 차이 구하기
        Calendar cal_today= Calendar.getInstance();
        Date today = cal_today.getTime();

        Calendar cal_firstday= Calendar.getInstance();
        String first_date_cal = (weights.get(0)).getDate();
        Integer year;
        if(first_date_cal.split("-")[0].contains("20")){
            year = Integer.parseInt(first_date_cal.split("-")[0]);
        }
        else
             year = Integer.parseInt("20"+first_date_cal.split("-")[0]);
        Integer month = Integer.parseInt(first_date_cal.split("-")[1]) -1;
        Integer day = Integer.parseInt(first_date_cal.split("-")[2]);

        cal_firstday.set(Calendar.YEAR,  year);
        cal_firstday.set(Calendar.MONTH,  month);
        cal_firstday.set(Calendar.DATE,  day);

        SimpleDateFormat fm1 = new SimpleDateFormat("yy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        Date first_date = cal_firstday.getTime();

        System.out.println(first_date_cal + " / " + sdf.format(first_date).toString() + " / " + sdf.format(today).toString());

        //SimpleDateFormat 을 이용하여 startDate와 endDate의 Date 객체를 생성한다.
        //두날짜 사이의 시간 차이(ms)를 하루 동안의 ms(24시*60분*60초*1000밀리초) 로 나눈다.
        long diffDay = (today.getTime() - first_date.getTime()) / (24*60*60*1000);
        System.out.println(diffDay+"일");
        ////----

        QueryWEIGHT weight = (QueryWEIGHT) weights.get(0);
        float first_weight = weight.getWeight();
        Date date;
        int for_index = 0;
        String checkday;
        String dataday;
        for(int i = 0 ; i < (int)diffDay ; i++) {
            date = cal_firstday.getTime();
            checkday = fm1.format(date).toString();
            dataday = weights.get(for_index).getDate();
            System.out.println(checkday + " / " + dataday);
            labels.add(checkday);
            if( dataday.contains(checkday) && for_index < weights.size()) {   //비교해서 일자 같으면 데이터 빼고 증가시킬것
                System.out.println("매치 " + checkday + " / " + dataday);
                weight = (QueryWEIGHT) weights.get(for_index);
                entries.add(new BarEntry((weight.getWeight() - first_weight)*10, for_index));
                for_index++;
            }
            cal_firstday.add(Calendar.DATE, +1);
        }
        //ArrayList<BarEntry> entries = new ArrayList<>();
        /*entries.add(new BarEntry(4f, 0));
        entries.add(new BarEntry(8f, 1));
        entries.add(new BarEntry(6f, 2));
        entries.add(new BarEntry(12f, 3));
        entries.add(new BarEntry(18f, 4));
        entries.add(new BarEntry(9f, 5));*/

        final BarDataSet dataset = new BarDataSet(entries, " 초기값으로부터의 몸무게 변화량");

        // ArrayList<String> labels = new ArrayList<String>();
       /* labels.add("January");
        labels.add("February");
        labels.add("March");
        labels.add("April");
        labels.add("May");
        labels.add("June");*/

        BarData data = new BarData(labels, dataset);
        // dataset.setColors(ColorTemplate.COLORFUL_COLORS); //

        int y=1;
        for(int i = 0 ; i < weights.size() ; i+=14)   //대충 10개 정도 보이도록..
            y++;

        barChart.zoom(y, 0 ,0, 0);

        barChart.setPinchZoom(true);    //확대 막기
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.moveViewToX(1000); // 이거찾느라 3시간.....>_<...

        barChart.setData(data);
        barChart.animateY(1500);
    }

    private void Chart_Height_day(View view){
        final BarChart barChart = (BarChart)view.findViewById(chart3);

        barChart.clear();

        barChart.zoom(0, 0 ,0, 0);
        barChart.zoom(1,0,0,0);

        dbHelper = SHARD_DB.getDbHelper();
        ArrayList<DataForQuery> heights = dbHelper.select(HEIGHT, loginUserData.getUsernumber());


        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<String>();

        if(heights.size() == 0){

            BarDataSet dataset = new BarDataSet(entries, "걸음 수");
            BarData data = new BarData(labels, dataset);
            barChart.setData(data);
            return;
        }

        ////---- 입력된 첫날짜 및 날짜 차이 구하기
        Calendar cal_today= Calendar.getInstance();
        Date today = cal_today.getTime();

        Calendar cal_firstday= Calendar.getInstance();
        String first_date_cal = (heights.get(0)).getDate();
        Integer year;
        if(first_date_cal.split("-")[0].contains("20")){
            year = Integer.parseInt(first_date_cal.split("-")[0]);
        }
        else
            year = Integer.parseInt("20"+first_date_cal.split("-")[0]);
        Integer month = Integer.parseInt(first_date_cal.split("-")[1]) -1;
        Integer day = Integer.parseInt(first_date_cal.split("-")[2]);

        cal_firstday.set(Calendar.YEAR,  year);
        cal_firstday.set(Calendar.MONTH,  month);
        cal_firstday.set(Calendar.DATE,  day);

        SimpleDateFormat fm1 = new SimpleDateFormat("yy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        Date first_date = cal_firstday.getTime();

        System.out.println(first_date_cal + " / " + sdf.format(first_date).toString() + " / " + sdf.format(today).toString());

        //SimpleDateFormat 을 이용하여 startDate와 endDate의 Date 객체를 생성한다.
        //두날짜 사이의 시간 차이(ms)를 하루 동안의 ms(24시*60분*60초*1000밀리초) 로 나눈다.
        long diffDay = (today.getTime() - first_date.getTime()) / (24*60*60*1000);
        System.out.println(diffDay+"일");
        ////----


        QueryHEIGHT height = (QueryHEIGHT) heights.get(0);
        float first_height = height.getHeight();
        System.out.println(first_height);
        Date date;
        int for_index = 0;
        String checkday;
        String dataday;
        for(int i = 0 ; i < (int)diffDay ; i++) {

            date = cal_firstday.getTime();
            checkday = fm1.format(date).toString();
            dataday = heights.get(for_index).getDate();
            System.out.println(checkday + " / " + dataday);
            labels.add(checkday);
            if( dataday.contains(checkday) && for_index < heights.size()) {   //비교해서 일자 같으면 데이터 빼고 증가시킬것

                System.out.println("매치 " + checkday + " / " + dataday);
                height = (QueryHEIGHT) heights.get(for_index);

                entries.add(new BarEntry(height.getHeight() - first_height , for_index));
                for_index++;
            }
            cal_firstday.add(Calendar.DATE, +1);
        }
        //ArrayList<BarEntry> entries = new ArrayList<>();
        /*entries.add(new BarEntry(4f, 0));
        entries.add(new BarEntry(8f, 1));
        entries.add(new BarEntry(6f, 2));
        entries.add(new BarEntry(12f, 3));
        entries.add(new BarEntry(18f, 4));
        entries.add(new BarEntry(9f, 5));*/

        final BarDataSet dataset = new BarDataSet(entries, " 초기 값으로부터의 키 변화량");

        // ArrayList<String> labels = new ArrayList<String>();
       /* labels.add("January");
        labels.add("February");
        labels.add("March");
        labels.add("April");
        labels.add("May");
        labels.add("June");*/

        BarData data = new BarData(labels, dataset);
        // dataset.setColors(ColorTemplate.COLORFUL_COLORS); //

        int y=1;
        for(int i = 0 ; i < heights.size() ; i+=14)   //대충 10개 정도 보이도록..
            y++;

        barChart.zoom(y, 0 ,0, 0);

        barChart.setPinchZoom(true);    //확대 막기
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.moveViewToX(1000); // 이거찾느라 3시간.....>_<...

        /*XAxis xAxis = barChart.getXAxis();
        xAxis.setTextSize(20f);

        YAxis yAxis = barChart.getAxisRight();
        yAxis.setTextSize(20); // set the text size
*/
        barChart.setData(data);
        barChart.animateY(1500);
    }
}
