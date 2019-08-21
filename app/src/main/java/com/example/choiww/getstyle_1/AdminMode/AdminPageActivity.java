package com.example.choiww.getstyle_1.AdminMode;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.choiww.getstyle_1.R;

public class AdminPageActivity extends AppCompatActivity {
    String TAG = "find";

    ImageView adminPage_goBasicAdmin_img;
    ImageView adminPage_goMallAdmin_img;
    ImageView adminPage_goOrderAdmin_img;
    ImageView adminPage_goUserAdmin_img;
    


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                switch (v.getId()){
                    case R.id.adminPage_goBasicAdmin_img:
                        Log.d(TAG, "onClick: adminPage_goBasicAdmin_img 눌림");
                        intent = new Intent(getApplicationContext(),MallAdminActivity.class);
                        startActivity(intent);
                        return;
                    case  R.id.adminPage_goMallAdmin_img:
                        Log.d(TAG, "onClick: adminPage_goMallAdmin_img 쇼핑몰관리 가기 버튼 눌림");
                        intent = new Intent(getApplicationContext(), MallAdminActivity.class);
                        startActivity(intent);
                        return;
                    case R.id.adminPage_goOrderAdmin_img:
                        Log.d(TAG, "onClick: adminPage_goOrderAdmin_img 주문관리가기 버튼 눌림");
                        intent = new Intent(getApplicationContext(), OrderManagingActivity.class);
                        startActivity(intent);
                        return;
                    case R.id.adminPage_goUserAdmin_img:
                        Log.d(TAG, "onClick: adminPage_goUserAdmin_img 회원관리가기 버튼 눌리");
                        intent = new Intent(getApplicationContext(), UserManagingActivity.class);
                        startActivity(intent);
                        return;
                        
                }
            }
        };

        adminPage_goBasicAdmin_img = findViewById(R.id.adminPage_goBasicAdmin_img);
        adminPage_goBasicAdmin_img.setOnClickListener(onClickListener);
        adminPage_goMallAdmin_img = findViewById(R.id.adminPage_goMallAdmin_img);
        adminPage_goMallAdmin_img.setOnClickListener(onClickListener);
        adminPage_goOrderAdmin_img = findViewById(R.id.adminPage_goOrderAdmin_img);
        adminPage_goOrderAdmin_img.setOnClickListener(onClickListener);
        adminPage_goUserAdmin_img = findViewById(R.id.adminPage_goUserAdmin_img);
        adminPage_goUserAdmin_img.setOnClickListener(onClickListener);


    }
}
