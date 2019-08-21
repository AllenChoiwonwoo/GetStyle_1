package com.example.choiww.getstyle_1.messenger;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.choiww.getstyle_1.MainActivity;
import com.example.choiww.getstyle_1.R;
import com.example.choiww.getstyle_1.main;
import com.example.choiww.getstyle_1.news_ex1;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MakeNewChatRoom_Activity extends AppCompatActivity {
    String TAG = "find";

    EditText makeNewChatRoom_inputRoomName_et; // 새로 생성할 채팅방 이름을 입력받는 et
    EditText makeNewChatRoom_InputHashtags_et; // 해쉬태그 입력 et
    EditText makeNewChatRoom_inputjoinLimitNumb_et; // 참여가능 최대 인원 수
    TextView makeNewChatRoom_selectImgInGallery_tx; // 커버이미지를 갤러리에서 선택하는 버튼
    TextView makeNewChatRoom_defaultImg_tx; // 커버이미지를 기본으로 선택하는 버튼
    Button makeNewChatRoom_makeChatRoom_btn; // 채팅방 생성 하기 버튼
    View.OnClickListener onClickListener;

    String inputRoomName;
    String inputHashTages;
    int inputJoinLimitNumb;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_new_chat_room_);

        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.makeNewChatRoom_selectImgInGallery_tx:
                        Log.d(TAG, "onClick: 겔러리에서 사진 선택");
                        break;
                    case R.id.makeNewChatRoom_defaultImg_tx:
                        Log.d(TAG, "onClick: 기본이미지 설정 ");
                        break;
                    case R.id.makeNewChatRoom_makeChatRoom_btn:
                        Log.d(TAG, "onClick: 채팅방 생성 버튼 클릭");
                        // 사용자가 입력한 값들을 받아온다.
                        inputRoomName = makeNewChatRoom_inputRoomName_et.getText().toString();
                        inputHashTages = makeNewChatRoom_InputHashtags_et.getText().toString();
                        inputJoinLimitNumb = Integer.parseInt(makeNewChatRoom_inputjoinLimitNumb_et.getText().toString());


                        checkUserInput();// 사용자가 입력한 값이 제한길이 이하인지 체크하는 메서드
//                        sendNewChatRoomRequest(); // 아파치서버로 새 채팅방을 만드는 요청을 보내는 메서드
                        sendNewChatRoomRequestByService(); // 자바서버(tcp)서버로 새채팅방을 만드는 요청을 보내는 메서드
                        break;
                }
            }
        };

        makeNewChatRoom_inputRoomName_et = findViewById(R.id.makeNewChatRoom_inputRoomName_et);
        makeNewChatRoom_InputHashtags_et = findViewById(R.id.makeNewChatRoom_InputHashtags_et);
        makeNewChatRoom_inputjoinLimitNumb_et = findViewById(R.id.makeNewChatRoom_inputjoinLimitNumb_et);
        makeNewChatRoom_inputjoinLimitNumb_et.setOnClickListener(onClickListener);
        makeNewChatRoom_selectImgInGallery_tx = findViewById(R.id.makeNewChatRoom_selectImgInGallery_tx);
        makeNewChatRoom_selectImgInGallery_tx.setOnClickListener(onClickListener);
        makeNewChatRoom_defaultImg_tx = findViewById(R.id.makeNewChatRoom_defaultImg_tx);
        makeNewChatRoom_defaultImg_tx.setOnClickListener(onClickListener);
        makeNewChatRoom_makeChatRoom_btn = findViewById(R.id.makeNewChatRoom_makeChatRoom_btn);
        makeNewChatRoom_makeChatRoom_btn.setOnClickListener(onClickListener);

        // 서버와 소통할 수 있는 서비스와 연결(bind)시킨다.
        Intent intent = new Intent(this, TCP_ClientChatting_service.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);


    }
    public void checkUserInput(){// 추후 추가하기
        Log.d(TAG, "checkUserInput: input 체크 시작");
    }
    public void sendNewChatRoomRequest(){
        // 레트로핏을 활용해 서버로 새로운 방을 만드는 요청을 보낸다.
        // 요청에 [채팅방이름, 채팅방태그, 채팅방종류 ,참여가능인원, 커버사진url]을 직력화한 데이터도 함께 보낸다.

        news_ex1 retrofit = com.example.choiww.getstyle_1.news_ex1.retrofit.create(com.example.choiww.getstyle_1.news_ex1.class);
        // 서버와 연결해주는 연결자(news_ex1)를 만들고
//        Call<List<dataclass_1>> call = news_ex1.getNewProdLimit(limitRange, limitRange+50);
//        Log.d("findcall", "요청범위: "+(limitRange+1) +"부터 "+(limitRange+50));
//        limitRange = limitRange + 50;
//        Call<List<dataclass_1>> call = news_ex1.getNewProd();
        // 어떤 요청을 보낼지 선택하여 요청을 만든다.(Call)
//        call.enqueue(new Callback<List<dataclass_1>>() {

        // 새채팅방 만들기 요청보낼 데이터를 직렬화한다.
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("roomName",inputRoomName);
            jsonObject.put("roomTages",inputHashTages);
            jsonObject.put("roomCertains_email",main.userEmail);
            jsonObject.put("roomCertains_userId", main.userId);
            jsonObject.put("maxUserVolume",inputJoinLimitNumb);

            long now = System.currentTimeMillis();
            Date date = new Date(now);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
            String getTime = sdf.format(date);
            jsonObject.put("createdDate",getTime);
//            jsonArray.put(jsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "sendNewChatRoomRequest: "+e.toString());
        }
        // 서버로 채팅방을 만들어달라는 요청을 보낸다.
        Call<ResponseBody> call = retrofit.sendNewChatRoomRequest(jsonObject.toString());
        Log.d(TAG, "sendNewChatRoomRequest: newRoomData_json  = "+jsonObject.toString());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "onResponse: response code : "+response.code());

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "onFailure: t :" + t.toString());

            }
        });
        // 서비스로 서버로 보낼 새로만들 방의 데이터를 보낸다.
        //

    }
    void sendNewChatRoomRequestByService(){
        Log.d(TAG, "sendNewChatRoomRequestByService: 엑티비티에서 채팅방 생성 버튼이 눌림");
        // 서비스를  통해서 채팅서버로 새채팅방 생성 요청을 보낸다

        // 새채팅방 만들기 요청보낼 데이터를 직렬화한다.
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        JSONObject jObject_request = new JSONObject();
        try {
            jsonObject.put("roomName",inputRoomName);
            jsonObject.put("roomTages",inputHashTages);
            jsonObject.put("roomCertains_email",main.userEmail);
            jsonObject.put("roomCertains_userId", main.userId);
            jsonObject.put("maxUserVolume",inputJoinLimitNumb);

            long now = System.currentTimeMillis();
            Date date = new Date(now);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
            String getTime = sdf.format(date);
            jsonObject.put("createdDate",getTime);
            jObject_request.put("request","createNewGroupChat");//한번더 json으로 감싼 후 구분자를 넣어준다. request=어떤요청인지 구분할 수 있게한다.
            jObject_request.put("data",jsonObject.toString());

//            jsonArray.put(jsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "sendNewChatRoomRequest: "+e.toString());
        }
        // 서비스의 tcp서버로 새채팅방을 만드는 메서드를
        tcpConn.myServiceFunc(jObject_request.toString());

    }
    TCP_ClientChatting_service tcpConn;
    private ServiceConnection mConnection = new ServiceConnection() {
        // 서비스와 연결될시 작동하는 메서드
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            TCP_ClientChatting_service.TCP_chatting_serv_binder binder
                    = (TCP_ClientChatting_service.TCP_chatting_serv_binder) service;
            tcpConn = binder.getService();// 이 객체를 통해서 서비스클래스의 메서드에 접근 할 수 있다.
            tcpConn.registerCallback("makeNewChatRoom_activity", mCallback);
            Log.d(TAG, "onServiceConnected: 서비스가 엑티비티와 연결됨");
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
                    Log.d(TAG, "remoteCall: msg = "+message.obj.toString());
                    // 여기서 이제 새채팅방 엑티비티를 만들어야한다.
                    String newRoomNumb = message.obj.toString();
                    Intent intent = new Intent(getApplicationContext(), ChatRoom.class);
                    intent.putExtra("roomNumb", newRoomNumb);
                    intent.putExtra("newChatRoom", true);
                    Log.d(TAG, "remoteCall:  새로운  채팅방이 생성되어 이 채팅방으로 화면이동 , 채팅방번호 : "+newRoomNumb);
                    startActivity(intent);
                }

                @Override
                public void sendMessage(HashMap hashMap) {

                }
            };
}
