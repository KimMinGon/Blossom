package com.example.kitcoop.lastproject1.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.kitcoop.lastproject1.DB.DBHelper;
import com.example.kitcoop.lastproject1.R;

import static com.example.kitcoop.lastproject1.DATA.SharedConstant.NEED_TODAY_WALK;
import static com.example.kitcoop.lastproject1.DATA.SharedConstant.NEED_TODAY_WATER;
import static com.example.kitcoop.lastproject1.DATA.SharedConstant.level_exp;
import static com.example.kitcoop.lastproject1.DATA.SharedObjct.SHARD_DB;
import static com.example.kitcoop.lastproject1.DATA.SharedObjct.loginUserData;

/**
 * Created by kitcoop on 2017-03-27.
 */

//생명주기 체크
public class CustomF_Ssak extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        System.out.println("onCreateView() 호출");

        //final View view = inflater.inflate(R.layout.fragment, container, false);   //맨 앞자리는 디자인자리
        View view = inflater.inflate(R.layout.fragment_ssak, container, false);   //맨 앞자리는 디자인자리

        setImage(view);

        return view;
    }

    public void setImage (View v) {
        DBHelper dbHelper = SHARD_DB.getDbHelper();

        ((ImageView)(v.findViewById(R.id.SsakPageImage))).setImageResource(R.drawable.window);
        ((ImageView)(v.findViewById(R.id.txt_sprout))).setImageResource(R.drawable.txt_sprout);


        ProgressBar today_need_water = (ProgressBar)v.findViewById(R.id.ssak_progressBar_today_water);
        ProgressBar today_need_walk = (ProgressBar)v.findViewById(R.id.ssak_progressBar_today_walk);

        today_need_water.setMax(NEED_TODAY_WATER);
        today_need_water.setProgress(dbHelper.selectTodayWater(loginUserData.getUsernumber()));
        today_need_walk.setMax(NEED_TODAY_WALK);
        today_need_walk.setProgress(dbHelper.selectTodayWalk(loginUserData.getUsernumber()));

        ProgressBar ssak_exp_percent = (ProgressBar)v.findViewById(R.id.ssak_exp_percent_prog);

        Integer total_exp = dbHelper.getSSAKexp(loginUserData.getUsernumber());
        Integer level = dbHelper.getSSAKlevel(loginUserData.getUsernumber());

        if(1 < level)
            level -= 1;

        int[] plant = {R.drawable.plant1, R.drawable.plant2, R.drawable.plant3, R.drawable.plant4, R.drawable.plant5 ,
                R.drawable.plant6, R.drawable.plant7, R.drawable.plant8, R.drawable.plant9, R.drawable.plant10};

        ((TextView)v.findViewById(R.id.ssak_level)).setText("Lv. " + level);
        ((ImageView)(v.findViewById(R.id.plant))).setImageResource(plant[level-1]); //level-1



        /////////////
        if(level != 10) {
            ssak_exp_percent.setMax(level_exp[level] - level_exp[level - 1]);
            total_exp -= level_exp[level - 1];
            ssak_exp_percent.setProgress(total_exp);

            ((TextView)v.findViewById(R.id.ssak_exp_percent_tv))
                    .setText("남은 필요 경험치 " + (level_exp[level] - level_exp[level-1] - total_exp) );
        }
        else {
            ssak_exp_percent.setMax(1);
            ssak_exp_percent.setProgress(1);

            ((TextView)v.findViewById(R.id.ssak_exp_percent_tv))
                    .setText("남은 필요 경험치 0" );
        }
        /////////////////


        /*System.out.println("싹싹 토탈 : " +total_exp);
        System.out.println("싹싹 레벨 : " +level);
        System.out.println("싹싹 해당레벨 총필요 경험치 : " + (level_exp[level] - level_exp[level-1]));
        System.out.println("싹싹 해당레벨 채운 경험치 : " +total_exp);*/

    }

}
