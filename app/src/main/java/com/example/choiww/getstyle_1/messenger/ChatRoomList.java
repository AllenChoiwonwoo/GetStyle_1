package com.example.choiww.getstyle_1.messenger;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.choiww.getstyle_1.DataClass.Messages_dataClass;
import com.example.choiww.getstyle_1.DataClass.roomInfoDataClass;
import com.example.choiww.getstyle_1.R;
import com.example.choiww.getstyle_1.main;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/*
    *   목적 : 채팅기능의 메인화면으로 참여중인 채팅방 목록을 볼 수 있는 화면이다.
    * */

public class ChatRoomList extends AppCompatActivity {
    String TAG = "find";
    TCP_ClientChatting_service tcpConn;
    ArrayList<roomInfoDataClass> chatRoomArray=new ArrayList<>();
    RecyclerView chatRoomList_recyclerview;
    Adapter_ChatRoomRecyclerview chatRoomListAdapter;
    LinearLayoutManager linearLayoutManager;
    String staticUserEmail = main.userEmail;
    int staticUserId = main.userId;
    String staticUserNickname = main.userNickname;
    String staticUserName = main.userName;

    View.OnClickListener onClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room_list);

        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.chatRoomList_goJoinChatRoomList_tv:
                        Log.d(TAG, "onClick: chatRoomList_goJoinChatRoomList_tv 버튼 클림됨");
                        Intent intent = new Intent(getApplicationContext(), JoiningChatRoomList.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        unbindService(mConnection);

                        startActivity(intent);
                        break;
                }
            }
        };
        TextView chatRoomList_goJoinChatRoomList_tv = findViewById(R.id.chatRoomList_goJoinChatRoomList_tv);
        chatRoomList_goJoinChatRoomList_tv.setOnClickListener(onClickListener);
        // 주석하나 추가

        // 서버와 소통할 수 있는 서비스와 연결(bind)시킨다.
        Intent service = new Intent(this, TCP_ClientChatting_service.class);
        bindService(service, mConnection, Context.BIND_AUTO_CREATE);
//        Re
        // 전채 채팅방의 리스트를 보여주는 recyclerview
        chatRoomList_recyclerview = findViewById(R.id.chatRoomList_recyclerview);
        chatRoomListAdapter = new Adapter_ChatRoomRecyclerview(getApplicationContext(),chatRoomArray,this);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        chatRoomList_recyclerview.setLayoutManager(linearLayoutManager);
        chatRoomList_recyclerview.setAdapter(chatRoomListAdapter);

    }
    @Override
    public void onBackPressed() {// 백버튼이 눌렸을 경우
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), main.class);
        intent.putExtra("userId", staticUserId);
        intent.putExtra("userEmail", staticUserEmail);
        intent.putExtra("userNickname", staticUserNickname);
        intent.putExtra("userName", staticUserName);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// 호출한 엑티비티보다 위의 스텍을 다 지운다.
        startActivity(intent);
        finish();
    }
    private ServiceConnection mConnection = new ServiceConnection() {
        // 서비스와 연결될시 작동하는 메서드
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            TCP_ClientChatting_service.TCP_chatting_serv_binder binder
                    = (TCP_ClientChatting_service.TCP_chatting_serv_binder) service;
            tcpConn = binder.getService();// 이 객체를 통해서 서비스클래스의 메서드에 접근 할 수 있다.
            tcpConn.registerCallback("chatRoomList_activity", mCallback);
            Log.d(TAG, "onServiceConnected: 서비스가 엑티비티와 연결됨");
            // 채팅방리스트 보기를 들어가면
            JSONObject request_json = new JSONObject();
            try {
                request_json.put("request", "chatRoomList");
                request_json.put("data", main.userId);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "onServiceConnected: joinchatRooomList엑티비티에서 myserviceFunc 실행시킴");
            tcpConn.myServiceFunc(request_json.toString());// 연결된 서비스를 통해서 서버로 데이터를 보낸다.
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    private TCP_ClientChatting_service.ICallback mCallback =
            new TCP_ClientChatting_service.ICallback(){
                // 콜백 인터페이스(연결자)를 통해서 서비스에서 엑티비티의 메서드를 동작시킬 수 있게 한다.
                @Override
                public void remoteCall() {
                    Log.d(TAG, "remoteCall: 새로운 사용자가 서버에 연결됨 : ");
                }

                @Override
                public void remoteCall(Message message) {
                    if (message.what == 6){
                        Log.d(TAG, "chatRoomList - remoteCall: remoteCall(msg) 발동");
                        List list = (List<roomInfoDataClass>)message.obj;
                        chatRoomListAdapter.chatRoomList.addAll(list);
                        chatRoomListAdapter.notifyDataSetChanged();

                    }else if(message.what == 7){
//                        roomInfoDataClass roomDetail = (roomInfoDataClass)message.obj;
                        String str_jObject_roomInfoDataClass = (String)message.obj;
                        Intent goChatRoomProfile = new Intent(getApplicationContext(), ChatRoomProfile.class);
                        goChatRoomProfile.putExtra("str_jObject_roomInfoDataClass",str_jObject_roomInfoDataClass);
                        startActivity(goChatRoomProfile);
                    }

                }

                // 메시지를 받아오는 부분
                //  메시지를 받았을 시 recyclerview에 데이터를 넣고 리프레쉬를 해줘야한다.
                @Override
                public void sendMessage(HashMap hashMap) {
//                    Log.d(TAG, "sendMessage: chatRoom 엑티비티에서 sendMessage 발동 . 메시지데이터 : "+hashMap.toString());
//                    String received_userid = (String)hashMap.get("userId");
//                    String received_userEmail = (String)hashMap.get("userEmail");
//                    String received_roomNumb = (String)hashMap.get("roomNumb");
//                    String received_message = (String)hashMap.get("message");
//                    String received_createdDate = (String)hashMap.get("createdDate");
//                    chattingAdapter.arrayList.add(hashMap);// 서비스에서 받은 메시지를 화면에 더해서 보여준다.
//                    chattingAdapter.notifyDataSetChanged();
                }
            };
}
