package com.example.choiww.getstyle_1.messenger;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.choiww.getstyle_1.DataClass.roomInfoDataClass;
import com.example.choiww.getstyle_1.R;
import com.google.gson.Gson;

public class ChatRoomProfile extends AppCompatActivity {
    String TAG = "find";

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room_profile);

        final Intent intent = getIntent();
        String str_jObject_roomInfoDataClass = intent.getStringExtra("str_jObject_roomInfoDataClass");
        Log.d(TAG, "ChatRoomProfile - onCreate: roomInfoDataClass ="+str_jObject_roomInfoDataClass);
        Gson gson = new Gson();
        final roomInfoDataClass roomDetail = gson.fromJson(str_jObject_roomInfoDataClass, roomInfoDataClass.class);

        ImageView chatRoomProfile_profileImg_img = findViewById(R.id.chatRoomProfile_profileImg_img);

        TextView chatRoomProfile_roomName_tv = findViewById(R.id.chatRoomProfile_roomName_tv);
        chatRoomProfile_roomName_tv.setText(roomDetail.getRoomName());
        ImageView chatRoomProfile_certainsProfileImg_img = findViewById(R.id.chatRoomProfile_certainsProfileImg_img);
//        chatRoom
        TextView chatRoomProfile_maxUserAndCreatedDate_tv = findViewById(R.id.chatRoomProfile_maxUserAndCreatedDate_tv);
        String createdDate = roomDetail.getCreatedDate();
        Log.d(TAG, "onCreate: 채팅방 생성 날짜 원본 : cratedDate = "+createdDate);
        String[] splited_createdDate = createdDate.split(" ");
        String edited_createdDate = splited_createdDate[0];
        Log.d(TAG, "chatRoomProfile- onCreate: 날짜만 분리한 createdDate = "+edited_createdDate);
        String maxUserAndCreatedDate = "최대인원: "+roomDetail.getMaxUserVolume()+" /생성일 : "+edited_createdDate;
        chatRoomProfile_maxUserAndCreatedDate_tv.setText(maxUserAndCreatedDate);
        TextView chatRoomProfile_chatRoomTages_tv = findViewById(R.id.chatRoomProfile_chatRoomTages_tv);
        chatRoomProfile_chatRoomTages_tv.setText(roomDetail.getRoomTages());
//        roomDetail.get
        Button chatRoomProfile_joinChatRoom_btn = findViewById(R.id.chatRoomProfile_joinChatRoom_btn);
        chatRoomProfile_joinChatRoom_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d(TAG, "onClick: 여기서 서비스로 참가 요청 보내야함");
                Log.d(TAG, "onClick: 채팅에 참가하여 chatRoom으로 넘간다.");
                Intent intent1 = new Intent(getApplicationContext(), ChatRoom.class);
                intent1.putExtra("roomNumb", roomDetail.getRoomNumb());
                intent1.putExtra("firstJoin", true);
                startActivity(intent1);
            }
        });
    }
}
