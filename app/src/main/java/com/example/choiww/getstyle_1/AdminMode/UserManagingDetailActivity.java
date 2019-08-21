package com.example.choiww.getstyle_1.AdminMode;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.choiww.getstyle_1.DataClass.userInfoData;
import com.example.choiww.getstyle_1.R;
import com.example.choiww.getstyle_1.news_ex1;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserManagingDetailActivity extends AppCompatActivity {

    String TAG = "find"; // log용 구분자

    userInfoData userInfoData_class = new userInfoData();
    int userNumb;// 서버db에서 한사람의 회원정보만 불러오기위한 구분자, 전 엑티비티('회원관리')에서 받아옴
    String userEmail;
    String userName;
    String userNickname;
//    String userPasswd;
//    String userPhone;
    String createdDate;
    int int_userStatus;


    TextView userManagingDetail_userEmail_tx;
    TextView userManagingDetail_userName_tx;
    TextView userManagingDetail_userNickname_tx;
    TextView userManagingDetail_joinDate_tx;
    TextView userManagingDetail_tatalNumberOfOrders_tx;
    TextView userManagingDetail_totalOrderAmount_tx;
    TextView userManagingDetail_userStatus_tx;
    Button userManagingDetail_accessRestriction_btn;
    Button userManagingDetail_doWithdrawal_btn;

    news_ex1 connector;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_managing_detail);

        Intent intent = getIntent();
        userNumb = intent.getIntExtra("userNumb",0);

        userManagingDetail_accessRestriction_btn = findViewById(R.id.userManagingDetail_accessRestriction_btn);

        connector = news_ex1.retrofit.create(news_ex1.class);
        retrofit2.Call<ArrayList<userInfoData>> call = connector.getAdminOneUserInfo(userNumb);
        call.enqueue(new Callback<ArrayList<userInfoData>>() {
            @Override
            public void onResponse(retrofit2.Call<ArrayList<userInfoData>> call, Response<ArrayList<userInfoData>> response) {
                Log.d(TAG, "onResponse: 회원상세 페이지에서 회원정보 받아오기 성공 : "+response.code());
                userInfoData_class = response.body().get(0);
                setDataToEachView(userInfoData_class);


            }

            @Override
            public void onFailure(retrofit2.Call<ArrayList<userInfoData>> call, Throwable t) {

            }
        });

    }

    public void setDataToEachView(userInfoData userInfoData_class){
        userInfoData mUserInfoData = userInfoData_class;

        userManagingDetail_userName_tx = findViewById(R.id.userManagingDetail_userName_tx);
        userManagingDetail_userName_tx.setText(mUserInfoData.getUserName());
        userManagingDetail_userEmail_tx = findViewById(R.id.userManagingDetail_userEmail_tx);
        userManagingDetail_userEmail_tx.setText(mUserInfoData.getUserEmail());
        userManagingDetail_userNickname_tx = findViewById(R.id.userManagingDetail_userNickname_tx);
        userManagingDetail_userNickname_tx.setText(mUserInfoData.getUserNickname());

        Log.d(TAG, "setDataToEachView: : "+mUserInfoData.getCreatedDate());
        String joinDate = mUserInfoData.getCreatedDate().substring(0,10);
        userManagingDetail_joinDate_tx = findViewById(R.id.userManagingDetail_joinDate_tx);
        userManagingDetail_joinDate_tx.setText(joinDate);
//        Log.d(TAG, "setDataToEachView: getTotalNumberOfOrders : "+mUserInfoData.getTotalNumberOfOrders());

        userManagingDetail_tatalNumberOfOrders_tx = findViewById(R.id.userManagingDetail_tatalNumberOfOrders_tx);
//        userManagingDetail_tatalNumberOfOrders_tx.setText("1231231");
        Log.d(TAG, "setDataToEachView: 총 주문 개수 : "+mUserInfoData.getTotalNumberOfOrders());
        userManagingDetail_tatalNumberOfOrders_tx.setText((mUserInfoData.getTotalNumberOfOrders())+"");
        userManagingDetail_totalOrderAmount_tx = findViewById(R.id.userManagingDetail_totalOrderAmount_tx);
        userManagingDetail_totalOrderAmount_tx.setText(mUserInfoData.getTotalOrderAmount()+"");
        userManagingDetail_userStatus_tx = findViewById(R.id.userManagingDetail_userStatus_tx);
        String userStatus = "";
        if (mUserInfoData.getUser_status() == 0){
            userStatus = "일반회원";
            userManagingDetail_accessRestriction_btn.setText("접속제한 등록");
            Log.d(TAG, "setDataToEachView: 접속제한xx");
//            int_userStatus = 0;
        }else if(mUserInfoData.getUser_status() == 1){
            userStatus = "접속제한 회원";
            userManagingDetail_accessRestriction_btn.setText("접속제한 취소");
            Log.d(TAG, "setDataToEachView: 접속제한oo");
//            int_userStatus = 1;
        }else if(mUserInfoData.getUser_status() == 2){
            userStatus = "탈퇴회원";
            userManagingDetail_accessRestriction_btn.setVisibility(View.INVISIBLE);
//            int_userStatus = 2;
        }
        userManagingDetail_userStatus_tx.setText(userStatus);
        int_userStatus = mUserInfoData.getUser_status();


        userManagingDetail_accessRestriction_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (int_userStatus == 0){
//                    userManagingDetail_accessRestriction_btn.setText("접속제한 유저 등록");
                    Call<ResponseBody> call = connector.setUserStatus( 1,userNumb);
                    Log.d(TAG, "onClick: id : "+userNumb+" , user_status : "+1+" ");
                    call.enqueue(callback);
                    int_userStatus = 1;
                    userManagingDetail_accessRestriction_btn.setText("접속제한 취소");
                    userManagingDetail_userStatus_tx.setText("접속제한 회원");

                }else if(int_userStatus == 1){
//                    userManagingDetail_accessRestriction_btn.setText("접속제한 유저 취소");
                    Call<ResponseBody> call = connector.setUserStatus(0, userNumb);
                    Log.d(TAG, "onClick: id : "+userNumb+" , user_status : "+0+" ");
                    call.enqueue(callback);
                    int_userStatus = 0;
                    userManagingDetail_accessRestriction_btn.setText("접속제한 등록");
                    userManagingDetail_userStatus_tx.setText("일반회원");
                }else {
//                    userManagingDetail_accessRestriction_btn.setVisibility(View.INVISIBLE);
                }
//                userManagingDetail_accessRestriction_btn.setText();

            }
        });
    }
    Callback callback = new Callback() {
        @Override
        public void onResponse(Call call, Response response) {
            Log.d(TAG, "onResponse: 성공 : "+response.code());
        }

        @Override
        public void onFailure(Call call, Throwable t) {
            Log.d(TAG, "onFailure: 실패 : "+t);
        }
    };
}
