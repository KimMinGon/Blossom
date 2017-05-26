package com.example.kitcoop.lastproject1.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kitcoop.lastproject1.DATA.SharedConstant;
import com.example.kitcoop.lastproject1.DATA.SharedObjct;
import com.example.kitcoop.lastproject1.DB.DBHelper;
import com.example.kitcoop.lastproject1.DB.QueryWEIGHT;
import com.example.kitcoop.lastproject1.R;

/**
 * Created by kitcoop on 2017-04-11.
 */

public class CustomF_TEST_SQLight extends Fragment implements SharedObjct, SharedConstant {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        System.out.println("onCreateView() 호출");

        //final View view = inflater.inflate(R.layout.fragment, container, false);   //맨 앞자리는 디자인자리
        View view = inflater.inflate(R.layout.fragment_test_sqlight, container, false);   //맨 앞자리는 디자인자리
        final DBHelper dbHelper = SHARD_DB.getDbHelper();

        view.findViewById(R.id.intsertTest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QueryWEIGHT weight = new QueryWEIGHT(0, 1, "sbean0215@gmail.com", "오늘", 20.2f);
                dbHelper.insert(WEIGHT, weight);
            }
        });

        view.findViewById(R.id.updateTest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        view.findViewById(R.id.deleteTest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        view.findViewById(R.id.selectTest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.select(WEIGHT, TEST_USERNUMBER);
            }
        });

        return view;
    }

}
