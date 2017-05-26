package com.example.kitcoop.lastproject1;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.kitcoop.lastproject1.DATA.SharedConstant;
import com.example.kitcoop.lastproject1.DATA.SharedObjct;
import com.example.kitcoop.lastproject1.Fragment.CustomF_Analyze;
import com.example.kitcoop.lastproject1.Fragment.CustomF_GPS;
import com.example.kitcoop.lastproject1.Fragment.CustomF_Measure;
import com.example.kitcoop.lastproject1.Fragment.CustomF_Profile;
import com.example.kitcoop.lastproject1.Fragment.CustomF_Ssak;
import com.example.kitcoop.lastproject1.pedometerService.MyService;

public class Activity_Drawer extends Activity implements SharedConstant, SharedObjct{
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] MenuListTitle;
    private static boolean serviceCheck;

    //////////////////////
    Fragment fragment;
    int reqestcode_call_setting = 777;

    public static boolean isServiceCheck() {
        return serviceCheck;
    }

    public static void setServiceCheck(boolean serviceCheck) {
        Activity_Drawer.serviceCheck = serviceCheck;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_main);

        SHARE_SETTING.makeSetting(this);

        serviceCheck = isServiceRunning(getApplicationContext());

        SharedPreferences setting = getSharedPreferences("setting", 0);
        SharedPreferences.Editor editor = setting.edit();

        //만약 투데이 챌린지 정보가 없으면 기본값으로 세팅
        editor.putString("TODAY_C_WALK", setting.getString("TODAY_C_WALK", "10000"));
        editor.putString("TODAY_C_WATER", setting.getString("TODAY_C_WATER", "2000"));
        editor.commit();

        mTitle = mDrawerTitle = getTitle();
        MenuListTitle = getResources().getStringArray(R.array.menu_list);

        //드로어 레이아웃 그 자체를 연결시켜줌
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        //옆에 열리는거 자체는 ListView 임 그것을 activity_main에 세팅해놓고 그것을 연결
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        // 드로어가 열렸을때..
        //mDrawerLayout.setDrawerShadow(R.mipmap.ic_launcher, GravityCompat.START);
        mDrawerLayout.setDrawerShadow(R.drawable.ssak_menu, GravityCompat.START);

        // set up the drawer's list view with items and click listener
        //드로어에 리스트를 달고, 형태.. 기본형태 사용
        /*mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, MenuListTitle));*/

       /* ArrayList<MyItem> myItems = new ArrayList<>();*/

        //여기서 Profile을 생성한 다음에 MyItem에 set
        //profile.jpg가 존재하면 가져와서 쓰고 없으면 기본 이미지 파일로 대체할것.
        /*
        Profile profile = new Profile();
        profile.getRoundedBitmap(~~~~~~~~~~~~~~~~~);
        */

        //MakePicRoundClass profile = new MakePicRoundClass();
        //MyItem myItem1 = new MyItem(profile.getRoundedBitmap(this.getResources(), R.id.profile_pic));

        /*MyItem myItem1 = new MyItem(R.drawable.profile);
        MyItem myItem2 = new MyItem(R.drawable.ssak);
        MyItem myItem3 = new MyItem(R.drawable.trophe);
        MyItem myItem4 = new MyItem(R.drawable.walk);
        MyItem myItem5 = new MyItem(R.drawable.graph);*/

        //MakePicRoundClass profile = new MakePicRoundClass();

        if(myItems.size() == 0) {
            myItems.add(myItem1);
            myItems.add(myItem2);
            myItems.add(myItem3);
            myItems.add(myItem4);
            myItems.add(myItem5);
        }
        myListadapter.setAdapterData(this, myItems);


        /*mDrawerList.setAdapter(new MyListAdapter(this, myItems));*/
        mDrawerList.setAdapter(myListadapter);

        //클릭 리스너를 달아줌.
        //클릭리스너 수정하면 프레그먼트 교체 할수 있도록 할것.
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);


        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                //R.mipmap.ic_launcher_round,  /* nav drawer image to replace 'Up' caret */
                R.drawable.ssak_menu,
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                //getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                //getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);      //처음 열릴때 선택되는 프레그먼트(리스트position으로 선택)
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //궂이 안보이게 할 필요 없음.주석처리
        //menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch(item.getItemId()) {
            case R.id.action_setting:
                //상단 오른쪽의 버튼을 클릭했을경우.

                Intent intent = new Intent(Activity_Drawer.this, Activity_Settings.class);
                startActivityForResult(intent, reqestcode_call_setting);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        // update the main content by replacing fragments



        //메뉴버튼을 클릭하면 위치를 읽어서 프레그먼트를 교체.
        //Fragment fragment;

        switch (position) {
            case 0:
                fragment = new CustomF_Profile();
                break;
            case 1:
                fragment = new CustomF_Ssak();
                break;
            case 2:
                fragment = new CustomF_Measure();
                break;
            case 3:
                fragment = new CustomF_GPS();
                break;
            case 4:
                fragment = new CustomF_Analyze();
                break;
            default:
                fragment = new CustomF_Profile();
        }

        Bundle args = new Bundle();
        args.putInt("planet_number", position);
        fragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();


        // update selected item and title, then close the drawer

        mDrawerList.setItemChecked(position, true);
        //기존 코드에서는 메뉴를 눌렀을때, 상단바 타이틀을 변경. 하지만 우리는 안할고야! 때문에 주석처리??왜안됨..
        //setTitle(MenuListTitle[position]);

        //대신 이 위치에서 프레그먼트를 교체해주는 부분을 만들어야 함.
        //위에서 해줘따



        mDrawerLayout.closeDrawer(mDrawerList);
    }

    //상단 바에 타이틀.
    @Override
    public void setTitle(CharSequence title) {

        mTitle = "Blossom";
        getActionBar().setTitle(mTitle);

        //mTitle = title;
        //getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        System.exit(0);
    }

    /**
     * Fragment that appears in the "content_frame", shows a planet
     */


    /*
    //각 페이지에 필요한 프레그먼트 정보. 이거 떼서 만들면 될듯
    public static class PlanetFragment extends Fragment {
        public static final String ARG_PLANET_NUMBER = "planet_number";

        public PlanetFragment() {
            // Empty constructor required for fragment subclasses
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_planet, container, false);
            int i = getArguments().getInt(ARG_PLANET_NUMBER);
            *//*String planet = getResources().getStringArray(R.array.menu_list)[i];

            int imageId = getResources().getIdentifier(planet.toLowerCase(Locale.getDefault()),
                    "drawable", getActivity().getPackageName());
            ((ImageView) rootView.findViewById(R.id.image)).setImageResource(imageId);*//*
           // getActivity().setTitle(planet);

            return rootView;
        }
    }*/

    public static boolean isServiceRunning(Context context) {
        ActivityManager manager = (ActivityManager)context.getSystemService(context.ACTIVITY_SERVICE);

        for(ActivityManager.RunningServiceInfo rsi : manager.getRunningServices(Integer.MAX_VALUE)) {
            if("com.example.kitcoop.lastproject1.pedometerService.MyService".equals(rsi.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(serviceCheck == true) {
            Intent intent = new Intent(this, MyService.class);
            intent.setFlags(1);
            startService(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        System.out.println("오케이" + resultCode + "/" + RESULT_OK);

        if(requestCode == reqestcode_call_setting && resultCode==RESULT_OK) {
            if(data != null) {

                if (data.getBooleanExtra("IsChangeNick", false)) { //닉네임이 바뀌었으면
                    System.out.println("닉네임 변경" + fragment.getClass().getName());
                    if(fragment.getClass().getName().contains("CustomF_Profile")) {
                        fragment = new CustomF_Profile();

                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                    }
                }
                if(data.getBooleanExtra("IsChangePicture", false)) {    //사진이 바뀌었으면

                }
                if(data.getBooleanExtra("exit", false))
                    finish();
            }
        }
    }


}