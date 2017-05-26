package com.example.kitcoop.lastproject1.Img;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;

/**
 * Created by kitcoop on 2017-04-11.
 */

public class MakePicRoundClass {

    // 받은 이미지를 라운드처리한다음에 이미지 바깥 부분을 투명 처리해서 다시 넘겨주는 함수
    public Bitmap getRoundedBitmap(Bitmap bitmap) {

        //직사각형 테스트코드
        // Bitmap bitmap = BitmapFactory.decodeResource(v.getResources(), R.drawable.test);

        //이미지를 넣기 위해서는 이부분에서 끝에 bitmap_id를 받은이미지로 수정하면 됨..

        //그런데 옆으로 퍼진 이미지나 ..
        // 사각형이 아닌 이미지를 넣으면 이상하게 쪼개짐
        //때문에 정사각형으로 만드는 처리를 해준 다음에 해주어야 함.

        bitmap = Bitmap_make_squre(bitmap); //정사각형 처리하는 부분

        return output(bitmap);
    }

    public Bitmap getRoundedBitmap(View v, int bitmap_id) {
        //public Bitmap getRoundedBitmap(Resources resources, int bitmap_id) {

        Bitmap bitmap = BitmapFactory.decodeResource(v.getResources(), bitmap_id);
        //Bitmap bitmap = BitmapFactory.decodeResource(resources, bitmap_id);
        bitmap = Bitmap_make_squre(bitmap); //정사각형 처리하는 부분

        return output(bitmap);
    }



    private Bitmap output(Bitmap bitmap){

        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = Color.GRAY;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();

        return output;
    }

    //이미지를 직사각형으로 만들어주는 함수. 긴 쪽 가운데를 기준으로 정사각형으로 뽑아낸다.
    private Bitmap Bitmap_make_squre(Bitmap original) {
        int size;
        Bitmap result;

        //만약에 가로보다 세로가 길면
        if(original.getWidth() > original.getHeight()){
            size = original.getHeight();

            result = Bitmap.createBitmap(original
                    , (original.getWidth() - size) / 2
                    , 0
                    , size
                    , size);

        }
        //만약에 세로보다 가로가 길면
        else if(original.getWidth() < original.getHeight()){
            size = original.getWidth();

            result = Bitmap.createBitmap(original
                    , 0
                    , (original.getHeight() - size) / 2
                    , size
                    , size);

        }
        //같을 경우엔 그냥 처리
        else { //(original.getWidth() == original.getHeight()){
            //아무처리도 하지 않음
            result = original;
        }

        if (result != original)
            original.recycle();

        return result;

        /*Bitmap result = Bitmap.createBitmap(original
                , original.getWidth() / 4 //X 시작위치 (원본의 4/1지점)
                , original.getHeight() / 4 //Y 시작위치 (원본의 4/1지점)
                , original.getWidth() / 2 // 넓이 (원본의 절반 크기)
                , original.getHeight() / 2); // 높이 (원본의 절반 크기)
        if (result != original) {
            original.recycle();
        }
        return result;*/
    }
}
