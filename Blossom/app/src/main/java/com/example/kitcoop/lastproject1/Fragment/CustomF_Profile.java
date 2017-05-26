package com.example.kitcoop.lastproject1.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kitcoop.lastproject1.DB.DBHelper;
import com.example.kitcoop.lastproject1.Img.MakePicRoundClass;
import com.example.kitcoop.lastproject1.JClass.MakeData;
import com.example.kitcoop.lastproject1.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;
import static com.example.kitcoop.lastproject1.DATA.SharedConstant.profilePic_dir;
import static com.example.kitcoop.lastproject1.DATA.SharedObjct.SHARD_DB;
import static com.example.kitcoop.lastproject1.DATA.SharedObjct.loginUserData;
import static com.example.kitcoop.lastproject1.DATA.SharedObjct.myItem1;
import static com.example.kitcoop.lastproject1.DATA.SharedObjct.myListadapter;
import static java.lang.System.out;

/**
 * Created by kitcoop on 2017-03-27.
 */

//생명주기 체크
public class CustomF_Profile extends Fragment {

    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_IMAGE = 2;

    Uri mImageCaptureUri;
    String absolutePath;

    /*final postImageSend pdimg = new postImageSend(new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //핸들러
        }
    });*/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {



        //final View view = inflater.inflate(R.layout.fragment, container, false);   //맨 앞자리는 디자인자리
        final View view = inflater.inflate(R.layout.fragment_profile, container, false);   //맨 앞자리는 디자인자리


        //pdimg.set_imgpath(getActivity().getFilesDir() + profilePic_dir +"/"+ loginUserData.getUsernumber()+".jpg");




        setImageAndText(view);
        AddAllButtonClickListener(view);

        return view;
    }


    public void setImageAndText (View v) {
        MakePicRoundClass profile = new MakePicRoundClass();

        //만약 해당 filePath 경로에 사진이 존재하면 그것을 가져와서 쓰고, 아니면 기본 이미지로 한다
        String filePath = getActivity().getFilesDir() + profilePic_dir +"/"+ loginUserData.getUsernumber()+".jpg";
        File profileFile = new File(filePath);
        Bitmap croppedIcon;


        if(profileFile.exists()){
            Bitmap bm = BitmapFactory.decodeFile(filePath);
            croppedIcon = profile.getRoundedBitmap(bm);
        }
        else {
            croppedIcon = profile.getRoundedBitmap(v, R.drawable.profile);
        }

        //유저 프로필 정보 글로벌하게 저장. 기본정보 혹은 이미 저장되어있는 데이터
        loginUserData.setCropped_profile(croppedIcon);

        ((ImageView) (v.findViewById(R.id.profile_pic))).setImageBitmap(croppedIcon);

        //메뉴리스트의 이미지도 교체해 준다.
        myItem1.setBicon(loginUserData.getCropped_profile());
        myItem1.setIcon(0);
        myListadapter.notifyDataSetInvalidated();

        ((TextView)v.findViewById(R.id.f_profile_tv_nickname)).setText(loginUserData.getNickname());

        ((ImageView)v.findViewById(R.id.ssak)).setImageResource(R.drawable.ssak_p);
        ((ImageView)v.findViewById(R.id.reward1)).setImageResource(R.drawable.tro1);
        ((ImageView)v.findViewById(R.id.reward2)).setImageResource(R.drawable.tro2);
        ((ImageView)v.findViewById(R.id.reward3)).setImageResource(R.drawable.tro3);

        ((TextView)v.findViewById(R.id.f_profile_tv_account)).setText(loginUserData.getUserAccount());

        DBHelper dbHelper = SHARD_DB.getDbHelper();

        int level = dbHelper.getSSAKlevel(loginUserData.getUsernumber());
        if(level != 1)
            level -= 1;
        ((TextView)v.findViewById(R.id.ssakLV)).setText("Lv. " + level);

        /*Integer now_exp = dbHelper.getSSAKexp(loginUserData.getUsernumber());
        Integer level_max_exp = 0;
        ProgressBar exp_progress = (ProgressBar)v.findViewById(R.id.EXPprogressBar);
        for(int i = 0 ; i < level_exp.length ; i++) {
            if(level_exp[i] <= now_exp && now_exp <= level_exp[i+1]) {
                exp_progress.setMax(level_exp[i + 1]);
                level_max_exp=level_exp[i + 1];
            }
        }
        }
        exp_progress.setProgress(now_exp);
        if(level_exp[level_exp.length-1] <= now_exp) {
            exp_progress.setMax(1);
            exp_progress.setProgress(1);
            level_max_exp = 0;
            now_exp = 0;
        }*/

        //((TextView)v.findViewById(R.id.leftEXP)).setText("다음 레벨까지 남은 경험치 " + (level_max_exp-now_exp));

        ((ImageView)v.findViewById(R.id.rewardLine)).setImageResource(R.drawable.reward_line);

        //리워드 이미지 세팅
        int rewards[] = dbHelper.getReward(loginUserData.getUsernumber());

        switch (rewards[0]) {
            case 1: ((ImageView)v.findViewById(R.id.reward1)).setImageResource(R.drawable.r1); break;
            case 2: ((ImageView)v.findViewById(R.id.reward1)).setImageResource(R.drawable.r11); break;
            case 3: ((ImageView)v.findViewById(R.id.reward1)).setImageResource(R.drawable.r12); break;
            case 4: ((ImageView)v.findViewById(R.id.reward1)).setImageResource(R.drawable.r13); break;
        }
        switch (rewards[1]) {
            case 1: ((ImageView)v.findViewById(R.id.reward2)).setImageResource(R.drawable.r2); break;
            case 2: ((ImageView)v.findViewById(R.id.reward2)).setImageResource(R.drawable.r21); break;
            case 3: ((ImageView)v.findViewById(R.id.reward2)).setImageResource(R.drawable.r22); break;
            case 4: ((ImageView)v.findViewById(R.id.reward2)).setImageResource(R.drawable.r23); break;
        }
        switch (rewards[2]) {
            case 1: ((ImageView)v.findViewById(R.id.reward3)).setImageResource(R.drawable.r3); break;
            case 2: ((ImageView)v.findViewById(R.id.reward3)).setImageResource(R.drawable.r31); break;
            case 3: ((ImageView)v.findViewById(R.id.reward3)).setImageResource(R.drawable.r32); break;
            case 4: ((ImageView)v.findViewById(R.id.reward3)).setImageResource(R.drawable.r33); break;
        }

        //닉네임 표시 부분을 누르면 변경 가능
        v.findViewById(R.id.f_profile_tv_nickname).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog cd = new CustomDialog(getActivity());
                cd.ChangeNickDialog(getActivity());
            }
        });

        ((ImageView)v.findViewById(R.id.ssak)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MakeData();
            }
        });


    }

    private void AddAllButtonClickListener (View view) {
        view.findViewById(R.id.profile_pic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //이미지 갤러리 호출..하는 부분 짤것..
                doTakeAlbumAction();


            }
        });


    }

    //////////////////////////////////////////////////////////////////////////////////
    ///아래 앨범에서 이미지 받아오는 부분

    public void doTakeAlbumAction() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent,PICK_FROM_ALBUM);
        //위 startActivityForResult() 의 결과로써
        //onActivityResult() 가 실행되고
        //두번째 인자를 넣는것으로 switch case 분류


    }

    //갤러리에서 이미지를 선택한 이후에 처리되는 부분..
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //결과없음
        if(resultCode != RESULT_OK)
            return;

        switch (requestCode) {
            case PICK_FROM_ALBUM:
                //이미지 가져오기
                mImageCaptureUri = data.getData();

                //이미지 가져온 이후 리사이즈할 이미지 크기 결정
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mImageCaptureUri, "image/*");

                //크롭한 이미지의 x/y축 크기
                intent.putExtra("outputX", 400);
                intent.putExtra("outputY", 400);

                //크롭 박스의 x/y축 비율
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);

                intent.putExtra("scale", true);
                intent.putExtra("return-data", true);

                //CROP_FROM_CAMERA CASE문 이동?? 이게머지
                //아 어떤 액티비티 결과를..
                startActivityForResult(intent, CROP_FROM_IMAGE);

                break;    //앨범에서 선택하면 바로 이미지 크롭
            case CROP_FROM_IMAGE:
                //크롭 된 이후의 이미지를 넘겨받음

                if(resultCode != RESULT_OK)
                    return;

                final Bundle extras = data.getExtras();

                //크롭된 이미지를 저장하기 위한 파일 경로
                //String filePath = Environment.getExternalStorageDirectory().getAbsolutePath()
                //        +"/SmartWheel/"+System.currentTimeMillis()+"jpg";
                String filePath = getActivity().getFilesDir() + profilePic_dir +"/"+ loginUserData.getUsernumber()+".jpg";

                if(extras != null) {
                    //크롭된 비트맵
                    Bitmap photo = extras.getParcelable("data");

                    //크롭된 이미지를 외부저장소, 앨범에 저장
                    storeCropImage(photo, filePath);
                    absolutePath = filePath;


                    //레이아웃의 이미지칸에 크롭된 비트맵을 보여줌
                    /*ImageView iv = (ImageView)getView().findViewById(R.id.profile_pic);
                    iv.setImageBitmap(photo);*/

                    ImageView iv = (ImageView)getView().findViewById(R.id.profile_pic);
                    MakePicRoundClass profile = new MakePicRoundClass();
                    Bitmap croppedIcon = profile.getRoundedBitmap(photo);
                    iv.setImageBitmap(croppedIcon);

                    //메뉴리스트에 이미지 바꾸는 부분
                    myItem1.setBicon(croppedIcon);
                    myListadapter.notifyDataSetInvalidated();

                    break;
                }

                //임시 파일 삭제
                File f = new File(mImageCaptureUri.getPath());
                if(f.exists())
                    f.delete();

                break;
        }
    }

    //크롭한 이미지 저장하는 함수
    private void storeCropImage(Bitmap bitmap, String filePath) {

        final postImageSend pdimg = new postImageSend();
        pdimg.setDaemon(true);
        pdimg.start();



        //String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SmartWheel";
        String dirPath = getActivity().getFilesDir() +profilePic_dir;

        File directory_SmartWheel = new File(dirPath);

        if(!directory_SmartWheel.exists()) {
            out.println(directory_SmartWheel.mkdir());
        }

        File copyFile = new File(filePath);


        //만약에 profile.jpg 파일이 이미 존재하면 지우고 다시 저장하자.
        if(copyFile.exists()){
            copyFile.delete();
        }

        BufferedOutputStream out = null;

        try {
            copyFile.createNewFile();
            out= new BufferedOutputStream(new FileOutputStream(copyFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

            getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.fromFile(copyFile)));

            //서버에 파일 올리기


            ////////////////////
            Message msg2 = Message.obtain();
            msg2.what = 600;
            msg2.obj = filePath;

            pdimg.backHandler.sendMessage(msg2);
            /////////////////////////////////////

            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
