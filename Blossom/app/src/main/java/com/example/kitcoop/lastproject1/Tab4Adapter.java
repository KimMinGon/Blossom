package com.example.kitcoop.lastproject1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kitcoop.lastproject1.DB.QueryGPS;

import java.io.File;
import java.util.ArrayList;

import static com.example.kitcoop.lastproject1.DATA.SharedConstant.profilePic_dir;

/**
 * Created by kitcoop on 2017-04-25.
 */

public class Tab4Adapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private Context context;
    private ArrayList<QueryGPS> datas;

    public Tab4Adapter(Context context, ArrayList<QueryGPS> datas) {
        this.context = context;
        this.datas = datas;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        QueryGPS gpsInfo = datas.get(position);

        float distance = gpsInfo.getDistance();
        String filePath = gpsInfo.getMAP_imgfile_name();
        String sTime = gpsInfo.getStime();
        String tTime = gpsInfo.getTtime();
        String gpsPath = gpsInfo.getGPSpath();

        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.tab4, parent, false);
        }

        ImageView imageView = (ImageView)convertView.findViewById(R.id.tab4ImageView);
        TextView textView1 = (TextView)convertView.findViewById(R.id.tab4TextView1);
        TextView textView2 = (TextView)convertView.findViewById(R.id.tab4TextView2);
        TextView textView3 = (TextView)convertView.findViewById(R.id.tab4TextView3);

        /*File file = new File(context.getFilesDir() + profilePic_dir + "/" + filePath);
        MakePicRoundClass profile = new MakePicRoundClass();
        Bitmap croppedIcon;
        Bitmap bm = BitmapFactory.decodeFile(filePath);
        croppedIcon = profile.getRoundedBitmap(bm);
        imageView.setImageBitmap(croppedIcon);*/

        File imgFile = new  File(context.getFilesDir() + profilePic_dir + "/" + filePath);

        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            imageView.setImageBitmap(myBitmap);

        }

        textView1.setText(sTime);
        textView2.setText(distance + "KM");
        textView3.setText("운동시간 : " + tTime);

        return convertView;
    }
}

