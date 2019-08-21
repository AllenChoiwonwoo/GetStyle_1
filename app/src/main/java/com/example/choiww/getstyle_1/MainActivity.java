package com.example.choiww.getstyle_1;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageView = findViewById(R.id.img_loading);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),login.class);
                startActivity(intent);
                // $추후추가 : 이 엑티비티를 벗어나기전에 자동로그인을 체크해서 login창을 거치치 않고
                //메인으로 갈 수 있게 해야할 것이다.
            }
        });

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(MainActivity.this , new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {

                String newToken= instanceIdResult.getToken();
                Log.e("fcm", "onSuccess: newToken : "+newToken );
                Log.e("fcm", "onSuccess: ");

                sendRegistrationToServer(newToken);
            }
        });

    }
    private void sendRegistrationToServer(String token){
        //디바이스 토큰이 생성되거나 재생성 될 시 동작할 코드 작성
    }
}

// 클릭시 다음 메인으로 넘어간다.