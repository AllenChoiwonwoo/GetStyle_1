package com.example.choiww.getstyle_1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.choiww.getstyle_1.AdminMode.AdminPageActivity;

public class MyPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        /*
        * 관리자 모드 버튼을 관리자 아이디로 접속했을 시에만 누를 수 있게 한다.
        *   만약 회원번호(id)가 3이면 '관리자 모드'버튼을 활성화 시킨다.
        * */

        Button myPage_goAdminMode_btn = findViewById(R.id.myPage_goAdminMode_btn);
        if (main.userId !=3){
            myPage_goAdminMode_btn.setVisibility(View.GONE);
        }
        myPage_goAdminMode_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AdminPageActivity.class);
                startActivity(intent);
            }
        });

        TextView myPage_goOrderHistory_tx = findViewById(R.id.myPage_goOrderHistory_tx);
        myPage_goOrderHistory_tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OrderHistroyActivity.class);
                startActivity(intent);
            }
        });

    }
}
