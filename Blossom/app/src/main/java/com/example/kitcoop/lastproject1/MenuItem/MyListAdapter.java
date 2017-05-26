package com.example.kitcoop.lastproject1.MenuItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.kitcoop.lastproject1.R;

import java.util.ArrayList;

/**
 * Created by kitcoop on 2017-03-09.
 */

public class MyListAdapter extends BaseAdapter {
    // 데이터가 저장될 공간
    private ArrayList<MyItem> myItems;

    // 레이아웃을 객체화 시키는 클래스
    private LayoutInflater layoutInflater;
    private Context context;


   /* public MyListAdapter(Context context, ArrayList<MyItem> myItems) {
        this.context=context;
        this.myItems = myItems;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }*/

    public void setAdapterData (Context context, ArrayList<MyItem> myItems) {
        this.context=context;
        this.myItems = myItems;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // 단축키 alt + insert
    @Override
    public int getCount() {
        System.out.println("getCount 호출");
        return myItems.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View converterView, ViewGroup parent) {
        System.out.println("getView 호출");

        final int pos = position;
        if (converterView == null) {
            converterView = layoutInflater.inflate(R.layout.icontext, parent, false);
        }

        ImageView imageView = (ImageView) converterView.findViewById(R.id.img);

        if(myItems.get(pos).getIcon() != 0)
            imageView.setImageResource(myItems.get(pos).getIcon());
        else
            imageView.setImageBitmap(myItems.get(pos).getBicon());

        return converterView;


    }


}
