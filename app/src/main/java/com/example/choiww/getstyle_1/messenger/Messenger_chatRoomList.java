package com.example.choiww.getstyle_1.messenger;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.choiww.getstyle_1.BasketActivity;
import com.example.choiww.getstyle_1.OrderHistroyActivity;
import com.example.choiww.getstyle_1.R;
import com.example.choiww.getstyle_1.main;

public class Messenger_chatRoomList extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
//                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
//                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
//                    mTextMessage.setText(R.string.title_notifications);
                    Toast.makeText(Messenger_chatRoomList.this, "장바구니 버튼 클릭됨", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Messenger_chatRoomList.this,BasketActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_mypage:
                    Intent intent1 = new Intent(Messenger_chatRoomList.this,OrderHistroyActivity.class);
                    startActivity(intent1);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger_chat_room_list);
    }
}
