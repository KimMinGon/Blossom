package com.example.kitcoop.lastproject1.JClass;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.kitcoop.lastproject1.DB.DataForQuery;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

/**
 * Created by kitcoop on 2017-04-19.
 */

public class GoogleLogin implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "GoogleLogin";    //로그 찍기 위한 태그

    private FragmentActivity parentActivity;
    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;
    private TextView mStatusTextView;
    private ProgressDialog mProgressDialog;

    public GoogleLogin(FragmentActivity parentActivity, int ButtonID) {
        this.parentActivity = parentActivity;

        //접근범위 세팅
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().requestId().requestProfile()
                .build();
        /*
         * GoogleApiClient.Builder : Google Play 서비스 접근승인 요청
         * addConnectionCallbacks(this) //Google Client Connection Callback 클래스
         * addApi(LocationServices.API); //Fused Location Provider API 사용 요청
         */

        //GoogleApi를 사용할 수 있는지 체크
        mGoogleApiClient = new GoogleApiClient.Builder(parentActivity)
                .enableAutoManage(parentActivity /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        //버튼에 해당 구글 기능을 달아준다

        SignInButton signInButton = (SignInButton) parentActivity.findViewById(ButtonID);
        signInButton.setScopes(gso.getScopeArray());


    }

    //커넥션 연결이 실패할때 불려오는 메서드
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }



    //액티비티가 실행될때 돌아가는 함수(생명주기 관련) onStart에 넣어줄 부분
    public void outoLogin() {
        //인증 권한을 획득 했을때
        //자동 로그인 기능을 주는 부분
        //revoke를 하면 권한을 뺏고 자동로그인이 안되는것 같다.
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);

        if (opr.isDone()) { //인증이 이미 되어있으면
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);     //해당결과 석세스로 돌려주면서 성공시 동작 수행
        } else {    //인증이 안되어있을경우에
            showProgressDialog();
            //로딩 다이얼로그를 보여주면서
            //사인인 시도
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();   //로딩 다이얼로그를 없앤다
                    //해당 결과를 돌려주면서 그에따른 동작 수행
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }



    //다른 액티비티가 실행 된 이후에 돌아온 결과로 인해 실행되는 코드 onActivityResult에 넣어주던 부분
    public DataForQuery for_onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == RC_SIGN_IN) {
            //구글 로그인 인텐트를 새로 열어서 로그인 시도를 했을경우에
            //로그인이 성공인지 실패인지를 받아서 처리하는 부분
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            return handleSignInResult(result);
        }
        return null;
    }


    private DataForQuery handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());

        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            // 사인드인 성공시 행동해줄 코드
            Log.d(TAG, "Sign in 성공");

            DataForQuery userData = new DataForQuery();
            userData.DefalutDATA(null, null, acct.getEmail(), acct.getDisplayName(), null);
            return userData;
        } else {
            // Signed out, show unauthenticated UI.
            //실패시

            Log.d(TAG, "Sign in 실패");
            return null;
        }
    }



    /*---------------------------------------------------------------------*/



    //로그인 또는 가입 시도
    public void signIn(FragmentActivity activity) {
        //로그인 시도를 할 경우에 새로운 인텐트를 열어서 구글로긴 기능에 엑세스!
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        activity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    //로그아웃
    public void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Log.d(TAG, "Sign Out 성공");
                    }
                });
    }

    public void disconnect (){
        mGoogleApiClient.stopAutoManage(parentActivity);
        Log.d(TAG, "디스커넥트");
    }

    //탈퇴기능
    public void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                    }
                });
    }



    /*-----------------------
    ----------------------------------------------*/



    // 로그인 시도 중에 표시되는 진행 다이얼로그 표시.. show / hide
    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(parentActivity);   //<--원래코드 this
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }
}
