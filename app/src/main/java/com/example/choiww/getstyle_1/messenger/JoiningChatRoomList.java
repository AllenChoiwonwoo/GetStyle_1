package com.example.choiww.getstyle_1.messenger;

import android.app.Activity;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.choiww.getstyle_1.DataClass.Messages_dataClass;
import com.example.choiww.getstyle_1.DataClass.roomInfoDataClass;
import com.example.choiww.getstyle_1.R;
import com.example.choiww.getstyle_1.main;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JoiningChatRoomList extends AppCompatActivity {

    String TAG = "find";
    public TCP_ClientChatting_service tcpConn;
    ArrayList<roomInfoDataClass> joinChatRoomArray=new ArrayList<>();
    RecyclerView joinChatRoomList_recyclerview;
    Adapter_ChatRoomRecyclerview joinChatRoomListAdapter;
    LinearLayoutManager linearLayoutManager;
    String staticUserEmail = main.userEmail;
    int staticUserId = main.userId;
    String staticUserNickname = main.userNickname;
    String staticUserName = main.userName;

    ImageView joinChatRoomList_makeNewChatRoom_img;// 새 채팅방 만들기 버튼
    ImageView joinChatRoomList_findNewChatRoom_img; // 채팅방 찾기 버튼
    View.OnClickListener onClickListener;
    Intent goSelectedChatRoom_intent;

    @Override
    protected void onResume() {
        super.onResume();
        Intent service = new Intent(this, TCP_ClientChatting_service.class);
        bindService(service, mConnection, Context.BIND_AUTO_CREATE);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        goSelectedChatRoom_intent = new Intent(getApplicationContext(), ChatRoom.class);

        setContentView(R.layout.activity_joining_chat_room_list);

        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.joinChatRoomList_makeNewChatRoom_img:
                        Log.d(TAG, "onClick: make new chatting room 버튼 클림됨");
                        Intent intent = new Intent(getApplicationContext(), MakeNewChatRoom_Activity.class);
                        startActivity(intent);
                        break;
                    case R.id.joinChatRoomList_goAllChatRoomList_tx:
                        Log.d(TAG, "onClick: chatRoomList 로 이동");
                        Intent intent1 = new Intent(getApplicationContext(), ChatRoomList.class);
//                        onRestoreInstanceState();
                        intent1.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        unbindService(mConnection);
                        startActivity(intent1);
                }
            }
        };

        joinChatRoomList_makeNewChatRoom_img = findViewById(R.id.joinChatRoomList_makeNewChatRoom_img);
        joinChatRoomList_makeNewChatRoom_img.setOnClickListener(onClickListener);

        joinChatRoomList_findNewChatRoom_img = findViewById(R.id.joinChatRoomList_findNewChatRoom_img);
        joinChatRoomList_findNewChatRoom_img.setOnClickListener(onClickListener);

        TextView joinChatRoomList_goAllChatRoomList_tx = findViewById(R.id.joinChatRoomList_goAllChatRoomList_tx);
        joinChatRoomList_goAllChatRoomList_tx.setOnClickListener(onClickListener);

//        // 채팅방리스트 보기를 들어가면
//        JSONObject request_json = new JSONObject();
//        try {
//            request_json.put("request", "joinChatRoomList");
//            request_json.put("data", main.userId);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        // 서버와 소통할 수 있는 서비스와 연결(bind)시킨다.
        Intent service = new Intent(this, TCP_ClientChatting_service.class);
        bindService(service, mConnection, Context.BIND_AUTO_CREATE);

        joinChatRoomList_recyclerview = findViewById(R.id.joinChatRoomList_recyclerview);
        joinChatRoomListAdapter = new Adapter_ChatRoomRecyclerview(this,joinChatRoomArray,this, goSelectedChatRoom_intent);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        joinChatRoomList_recyclerview.setLayoutManager(linearLayoutManager);
        joinChatRoomList_recyclerview.setAdapter(joinChatRoomListAdapter);




    }
    private ServiceConnection mConnection = new ServiceConnection() {
        // 서비스와 연결될시 작동하는 메서드
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            TCP_ClientChatting_service.TCP_chatting_serv_binder binder
                    = (TCP_ClientChatting_service.TCP_chatting_serv_binder) service;
            tcpConn = binder.getService();// 이 객체를 통해서 서비스클래스의 메서드에 접근 할 수 있다.
            tcpConn.registerCallback("joiningChatRoomList_activity", mCallback);
            Log.d(TAG, "onServiceConnected: 서비스가 엑티비티와 연결됨");
            // 채팅방리스트 보기를 들어가면
            JSONObject request_json = new JSONObject();
            try {
                request_json.put("request", "joinChatRoomList");
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
                    Log.d(TAG, "remoteCall: remoteCall(msg) 발동");
//                    Log.d(TAG, "remoteCall: msg = "+message.obj.toString());
                    // 여기서 이제 새채팅방 엑티비티를 만들어야한다.

//                    Intent intent = new Intent(getApplicationContext(), ChatRoom.class);
//                    startActivity(intent);
                    if (message.what == 4){
                        Log.d(TAG, "joiningChatRoomList-remoteCall: 서비스를 통해 서버에서 보낸 참여중인채팅방리스트를 엑티비티가 받음");
                        // 서버에서 참여중인 채팅방의 리스트 데이터가 왔을때
                        List<roomInfoDataClass> joinChatRoomList = (List<roomInfoDataClass>)message.obj;
                        joinChatRoomListAdapter.chatRoomList.addAll(joinChatRoomList);
                        joinChatRoomListAdapter.notifyDataSetChanged();
                        // 이 어레이를 이제 리사이클러뷰에 넣어줘야한다.
                    }else if(message.what == 5){
                        Log.d(TAG, "remoteCall: 이건 모든 메시지 불러오기 전코드라 작동하면 안됌");
//                        Log.d(TAG, "joiningChatRoomList-remoteCall: 서비스를 통해 서버에서 보낸 유저가 클릭한 채팅방의 전체 채팅메시지를 받음");
                        List<Messages_dataClass> messageList = (List<Messages_dataClass>)message.obj;
                        ArrayList<Messages_dataClass> arrayList = new ArrayList<>();
                        arrayList.addAll(messageList);
//                        Bundle bundle = new Bundle();
//                        bundle.putParcelable("messageList",message);
//                        goSelectedChatRoom_intent.putExtra("messageList", message);
//                        goSelectedChatRoom_intent.putExtras(bundle);
//
//                        goSelectedChatRoom_intent.putParcelableArrayListExtra(this, "messageList", arrayList);
//                        goSelectedChatRoom_intent.putExtra("roomNumb", message.arg1+"");
//                        startActivity(goSelectedChatRoom_intent);
                    }


                    // 채팅방을 클릭했을때 채팅방의 채팅내용을 여기서 받아와야한다.
//                    if(false){
//                        intent = new Intent(getApplicationContext(), ChatRoom.class);
////                        intent.putExtra("roomNumb", )// 방번호를 어디서 받아오지?
//                        intent.putExtra("message", message);
////                        roomInfoDataClass ridc = new roomInfoDataClass();
////                        intent.putExtra("send",ridc);
//                        startActivity(intent);
//                    }


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
