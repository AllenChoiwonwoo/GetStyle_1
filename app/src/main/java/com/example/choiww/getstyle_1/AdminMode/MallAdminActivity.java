package com.example.choiww.getstyle_1.AdminMode;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.choiww.getstyle_1.R;

    /**
     * 목적 : 쇼핑몰 관리 엑티비티
     * */

public class MallAdminActivity extends AppCompatActivity {

    ImageView goAffiliateMallAdmin_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mall_admin);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            Intent intent;
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.goAffiliateMallAdmin_img:
                        intent = new Intent(getApplicationContext(),AffiliateMallAdminActivity.class);
                        startActivity(intent);
                        return;
                }
            }
        };
        goAffiliateMallAdmin_img = findViewById(R.id.goAffiliateMallAdmin_img);
        goAffiliateMallAdmin_img.setOnClickListener(onClickListener);



    }
}
