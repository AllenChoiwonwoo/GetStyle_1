package com.example.choiww.getstyle_1.messenger;

import android.Manifest;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.choiww.getstyle_1.DataClass.Messages_dataClass;
import com.example.choiww.getstyle_1.R;
import com.example.choiww.getstyle_1.main;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ChatRoom extends AppCompatActivity {

    String TAG = "find";
    EditText chatRoom_userInputMessage_et;
    ImageView chatRoom_sendButton_img;
    View.OnClickListener onClickListener;
    TCP_ClientChatting_service tcpConn;
    String roomNumb;
    boolean isNewChatRoom;
    int messageType = 0;// 메시지 종류 . 일반채팅:0, 알림:1, 사진:3, 동영상:4
//    boolean anounceNewUser;// 방에 참가시 다른이들에게 새유저 참가 알림을 보내고. 클라를 사용자로 등록하기 위한 변수

    ArrayList<Messages_dataClass> ChatMessage_array = new ArrayList();
    RecyclerView chatRoom_chattingRecyclerview;
    Adapter_ChattingRecyclerview chattingAdapter;
    LinearLayoutManager linearLayoutManager;

    int PICTURE_REQUEST_CODE = 100;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        intent = getIntent();
        roomNumb = intent.getStringExtra("roomNumb");
        if (roomNumb == null){
            Log.d(TAG, "onCreate: 참여 채팅목록에서 온경우 roomNumb가 안오기에 서버에서 받아온 int RoomNumb를 넣는다.");
            roomNumb = intent.getIntExtra("roomNumb",0)+"";
        }
        Log.d(TAG, "onCreate: 사용자가 들어간 방번호 - roomNumb : "+roomNumb);
        isNewChatRoom = intent.getBooleanExtra("newChatRoom", false);
        Log.d(TAG, "onCreate:  roomNumb :"+roomNumb+" , isNewChatRoom : "+isNewChatRoom);
        Log.d(TAG, "onCreate: isNewChatRoom은 새로생성한 방일 경우 true이다");
        Log.d(TAG, "onCreate: 참여중인 채팅방 목록에서 왔다면 isNewChatRoom이 없음으로 기본값 false여야한다."+isNewChatRoom);
        if (intent.getBooleanExtra("firstJoin",false)){
            Log.d(TAG, "onCreate: 처음으로 참여한 채팅방이라면(최초생성X) 서버에 참여자로 등록하는 요청을 보낼 수 있게 isNewChatRoom을 true 바꾼다.");
            isNewChatRoom = true;
            messageType = 1;
//            anounceNewUser = true;
        }


        if (!isNewChatRoom){// 이값이 false라는건 최초로 생성되어 입장한 채팅방이 아니란 것이다. 그러면 이미 있던 채팅을 받아와야할것
            //받아와야할 이전 채팅이 있다면 intent를 통해 message객체에 채팅이 담겨 올 것이다.
//            Log.d(TAG, "chatRoom - remoteCall: Message객체에 이방의 모든 메시지가 들어있다.");
//            Message holeChatMessage_msg = (Message)intent.getExtras().getParcelable("messageList");
//            Bundle bundle = intent.getExtras();
//            Message holeChatMessage_msg = bundle.getParcelable("messageList");
//            ChatMessage_array.addAll((ArrayList<Messages_dataClass>)holeChatMessage_msg.obj);
//            ArrayList<Messages_dataClass> array = intent.getParcelableArrayListExtra("messageList");
//            if (array == null){
//                Log.d(TAG, "onCreate: 가져올 채팅내용이 없음");
//            }else{
//                ChatMessage_array.addAll(array);
//                Log.d(TAG, "onCreate: 참여중 채팅방 목록에서 chatRoom으로 bundle을 통해 msg 객체를 옮기는데 성공함");
//            }




            
//           chattingAdapter.notifyDataSetChanged();

            // joinChatRoomActiivty 에서 intent에 방번호(roomNumb)를 넣지 안고 실행시켜
            // 채팅을 보내면 roomNumb가 서버로 보내지지 않아서 서버서 에러가 난다.
            //

        }




        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.chatRoom_sendButton_img:
                        Log.d(TAG, "onClick: 메시지 보내기 버튼 눌려짐");
                        String userInput = chatRoom_userInputMessage_et.getText().toString();
                        String Json_messageData = convertMessageToJson(userInput , messageType);
                        tcpConn.myServiceFunc(Json_messageData);
                        chatRoom_userInputMessage_et.setText("");
                        break;
                    case R.id.chatRoom_sendImgBtm_img:
                        Log.d(TAG, "onClick: 이미지 보내기 버튼 클릭됨");
                        Intent getImage_intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                        getImage_intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                        getImage_intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(getImage_intent, "Select Picture"),  PICTURE_REQUEST_CODE);
                        break;
                }
            }
        };

        chatRoom_userInputMessage_et = findViewById(R.id.charRoom_userInputMessage_et);
        chatRoom_sendButton_img = findViewById(R.id.chatRoom_sendButton_img);
        chatRoom_sendButton_img.setOnClickListener(onClickListener);
        ImageView chatRoom_sendImgBtm_img = findViewById(R.id.chatRoom_sendImgBtm_img);
        chatRoom_sendImgBtm_img.setOnClickListener(onClickListener);

        // 서버와 소통할 수 있는 서비스와 연결(bind)시킨다.
        Intent service = new Intent(this, TCP_ClientChatting_service.class);
        bindService(service, mConnection, Context.BIND_AUTO_CREATE);

        chatRoom_chattingRecyclerview = findViewById(R.id.chatRoom_chattingRecyclerview);
        chattingAdapter = new Adapter_ChattingRecyclerview(getApplicationContext(), ChatMessage_array);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        chatRoom_chattingRecyclerview.setLayoutManager(linearLayoutManager);
        chatRoom_chattingRecyclerview.setAdapter(chattingAdapter);

        // 권한을 요청한다.
        ActivityCompat.requestPermissions(ChatRoom.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

    }

    private ServiceConnection mConnection = new ServiceConnection() {
        // 서비스와 연결될시 작동하는 메서드
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            TCP_ClientChatting_service.TCP_chatting_serv_binder binder
                    = (TCP_ClientChatting_service.TCP_chatting_serv_binder) service;
            tcpConn = binder.getService();// 이 객체를 통해서 서비스클래스의 메서드에 접근 할 수 있다.
            tcpConn.registerCallback("chatRoom_activity", mCallback);
            Log.d(TAG, "onServiceConnected: 서비스가 엑티비티와 연결됨");
            // 채팅방에 참가시 '000이 참여했다는 메시지를 보낸다.'
            // 서버에서 첫참가자가 첫메시지를 보낼때 참여자로써 DB에 저장한다.
            if (isNewChatRoom){
                Log.d(TAG, "onServiceConnected: 서버로 새 참가자 알림 보내기");
                String anounceNewUser = main.userId+"이 참여하였습니다.";
//                messageType = 1;
                String Json_messageData = convertMessageToJson(anounceNewUser, messageType);
                tcpConn.myServiceFunc(Json_messageData);
//                chatRoom_userInputMessage_et.setText("");
                messageType = 0;// 참여했다는 메시지를 보냈으니 이제 일반 메시지를 보낼 것이기에 messagetype를 0으로 바꾼다.
                
            }
            int joiningChatRoomNumb = intent.getIntExtra("joiningRoomNumb",0);
            if (joiningChatRoomNumb != 0){
                roomNumb = joiningChatRoomNumb+"";
                Log.d(TAG, "chatRoom -onCreate: 참여중인 채팅방으로 들어옴, 채팅방 번호 : "+joiningChatRoomNumb);
                Log.d(TAG, "chatRoom -onServiceConnected: 서버로 기존채팅내용을  요청함");
                JSONObject jsonObject = new JSONObject();
                JSONObject data_jObject = new JSONObject();
                // 여기가 joiningChatRoom에서 왔을대 실행되는 메스드니까 여기서 서비스를 통해 메시지 내용들을 가져오게한다.
                JSONObject request_json = new JSONObject();
                try {
                    data_jObject.put("roomNumb",joiningChatRoomNumb+"");
                    Log.d(TAG, "onClick: 클릭된 방번호 : "+joiningChatRoomNumb);
                    data_jObject.put("userId", main.userId+"");

                    request_json.put("request", "sendAllChatMessageInThisRoom");
                    request_json.put("data", data_jObject);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "onServiceConnected: joinchatRooomList엑티비티에서 myserviceFunc 실행시킴");
                tcpConn.myServiceFunc(request_json.toString());// 연결된 서비스를 통해서 서버로 데이터를 보낸다.
//            tcpConn.myServiceFunc();

            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICTURE_REQUEST_CODE){
            ClipData clipData = data.getClipData();
            ArrayList<Uri> imgUri_array = new ArrayList<>();

            for (int i=0;clipData.getItemCount()>i;i++){
                Uri getUri = clipData.getItemAt(i).getUri();
                Log.d(TAG, "onActivityResult: 이미지 url"+i+" = "+getUri);

                imgUri_array.add(getUri);
//
//                tcpConn.myServiceFunc("");
            }
//            roomNumb = Join
            JSONObject json_data = convertImgMessageToJson(imgUri_array.size());
            tcpConn.sendImage(imgUri_array, json_data);
        }

    }

    private TCP_ClientChatting_service.ICallback mCallback =
            new TCP_ClientChatting_service.ICallback(){
                // 콜백 인터페이스(연결자)를 통해서 서비스에서 엑티비티의 메서드를 동작시킬 수 있게 한다.
                @Override
                public void remoteCall() {
                    Log.d(TAG, "remoteCall: 새로운 사용자가 서버에 연결됨 : ");
                }

                @Override
                public void remoteCall(Message message) {
//                    Log.d(TAG, "remoteCall: 이채팅방의 모든 메시지를 받아오기 위해 remoteCall(msg) 발동");
////                    Log.d(TAG, "remoteCall: msg = "+message.obj.toString());
//                    Log.d(TAG, "chatRoom - remoteCall: Message객체에 이방의 모든 메시지가 들어있다.");
//                    chattingAdapter.arrayList.addAll((ArrayList<Messages_dataClass>)message.obj);
//                    chattingAdapter.notifyDataSetChanged();

                    // joiningChatRoomList에서 왔을때( 기존에 받았던 메시지 전부를 서버에서 가져올때)
                    if(message.what == 5){
                        int joiningRoomNumb = message.arg1; // 참여중 방번호
                        List<Messages_dataClass> messageArray = (List<Messages_dataClass>) message.obj; // 메시지들
                        chattingAdapter.arrayList.addAll(messageArray);
                        chattingAdapter.notifyDataSetChanged();

                    }
                    // 이미지가 들어있는 메시지를 바았을때 == message.what= 8
                    if (message.what == 8){
                        Log.d(TAG, "chatRoom - remoteCall: 엑티비티에서 message 객체를 받음");
//                        HashMap msgDataHash = (HashMap) message.obj;
//                        JSONObject json_data = (JSONObject) msgDataHash.get("json_data"); // 메시지의 데이터
//                        ArrayList<Bitmap> bitmapArray = (ArrayList) msgDataHash.get("bitmapArray"); // 이미지 데이터 배열
//                        ArrayList<byte[]> byteArray_array = (ArrayList) msgDataHash.get("byteArray_array");
                        //
                        ArrayList<Messages_dataClass> messageArray = (ArrayList<Messages_dataClass>) message.obj;
                        for (int i=0;messageArray.size()>i;i++){
                            Log.d(TAG, "remoteCall: 이미지 이름 : "+messageArray.get(i).getBitmapName());
                            Bitmap bitmap = messageArray.get(i).getBitmap();

                            // https://www.youtube.com/watch?v=74O81YwVqWg 이 튜토리얼 보고 다시시도
                            FileOutputStream fileOutputStream = null;
                            File sdCard = Environment.getExternalStorageDirectory();
//                                        File sdCard2 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                            File directory = new File(sdCard.getAbsolutePath() + "/YourFolderName");
                            directory.mkdir();
                            String fileName = String.format("%d.png", System.currentTimeMillis());
                            File outFile = null;
                            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                                Log.d(TAG, "run: Media_mounted 라는디?");
                                outFile = new File(directory, fileName);
                            }

                            try{
                                fileOutputStream = new FileOutputStream(outFile);
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                                fileOutputStream.flush();
                                fileOutputStream.close();

                                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                                intent.setData(Uri.fromFile(outFile));
                                sendBroadcast(intent);
                            }catch (FileNotFoundException e){
                                e.printStackTrace();
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        chattingAdapter.arrayList.addAll(messageArray);
                        Log.d(TAG, "remoteCall: messageArray 길이 : "+messageArray.size());

                        chattingAdapter.notifyDataSetChanged();


                    }

                }

                // 메시지를 받아오는 부분
                //  메시지를 받았을 시 recyclerview에 데이터를 넣고 리프레쉬를 해줘야한다.
                @Override
                public void sendMessage(HashMap hashMap) {
                    Log.d(TAG, "sendMessage: chatRoom 엑티비티에서 sendMessage 발동 . 메시지데이터 : "+hashMap.toString());
                    String received_userid = (String)hashMap.get("userId");
                    String received_userEmail = (String)hashMap.get("userEmail");
                    String received_roomNumb = (String)hashMap.get("roomNumb");
                    String received_message = (String)hashMap.get("message");
                    String received_sendTime = (String)hashMap.get("sendTime");
                    String received_messageType = (String)hashMap.get("messageType");
                    Log.d(TAG, "ChatRoom - sendMessage: received_messageType :" +messageType);
                    Messages_dataClass messageData = new Messages_dataClass(received_roomNumb,received_userid,received_userEmail, received_sendTime, received_message, received_messageType, null);
                    chattingAdapter.arrayList.add(messageData);// 서비스에서 받은 메시지를 화면에 더해서 보여준다.
                    chattingAdapter.notifyDataSetChanged();
                }

//                @Override
//                public void sendImage(ArrayList<Uri> arrayList, JSONObject jsonObject) {
//
//                }

            };
    public String convertMessageToJson(String userinputMessage, int messageType){
        // 메시지 보내기 요청보낼 데이터를 직렬화한다.
        Log.d(TAG, "convertMessageToJson: 메시지 직려렬화 시작");
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        JSONObject jObject_request = new JSONObject();
        try {
            jsonObject.put("userId",main.userId);
            jsonObject.put("userEmail", main.userEmail);
            jsonObject.put("roomNumb",roomNumb);
            jsonObject.put("message",userinputMessage);
            jsonObject.put("isNewChatRoom", isNewChatRoom);
//            jsonObject.put("anounceNewUser", anounceNewUser);

            long now = System.currentTimeMillis();
            Date date = new Date(now);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String getTime = sdf.format(date);
            Log.d(TAG, "convertMessageToJson: getTime : "+getTime);
            jsonObject.put("sendTime",getTime);
            jsonObject.put("messageType", messageType);// 일반 메시지

            jObject_request.put("request","sendMessage");//한번더 json으로 감싼 후 구분자를 넣어준다. request=어떤요청인지 구분할 수 있게한다.
            jObject_request.put("data",jsonObject.toString());


        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "sendChatMessageRequest : "+e.toString());

        }
        Log.d(TAG, "convertMessageToJson: 메시지 직력화 완료");
        if (isNewChatRoom == true){// 이 불린값은 방생성후 최초 메시지를 구분하위함이다.
            //왜냐하면 방을 생성하고 메시지를 보내지 않고 나간경우 채팅방을 만들지 않고, 참여자로 등록도 하지 않게 하려하기 때문이다.
            // 방을 생성하고 최초로 메시지를 보내면 보낸이가 채팅방의 참가자로 서버db에 등록된다.
            // 다음음 또 보냈을때 서버에서 또 참가자로 등록하지 않게하기위해 이 구분자를 false로 바꿔 보낸다.
            isNewChatRoom = false;
//            anounceNewUser = false;
        }
        return jObject_request.toString();

    }
    public JSONObject convertImgMessageToJson(int imgCount){

        // 메시지 보내기 요청보낼 데이터를 직렬화한다.
        Log.d(TAG, "convertMessageToJson: 메시지 직려렬화 시작");
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        JSONObject jObject_request = new JSONObject();
        try {
            jsonObject.put("userId",main.userId);
            jsonObject.put("userEmail", main.userEmail);
            jsonObject.put("roomNumb",roomNumb);
//            jsonObject.put("message",userinputMessage);
//            jsonObject.put("image", )
            jsonObject.put("isNewChatRoom", isNewChatRoom);
//            jsonObject.put("anounceNewUser", anounceNewUser);

            long now = System.currentTimeMillis();
            Date date = new Date(now);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String getTime = sdf.format(date);
            Log.d(TAG, "convertMessageToJson: getTime : "+getTime);
            jsonObject.put("sendTime",getTime);
            jsonObject.put("messageType", 3);//메시지타입이 3이면 사진
            jsonObject.put("imgCount",imgCount);

            jObject_request.put("request","sendImage");//한번더 json으로 감싼 후 구분자를 넣어준다. request=어떤요청인지 구분할 수 있게한다.
            jObject_request.put("data",jsonObject.toString());
            Log.d(TAG, "chatRoom - convertImgMessageToJson: request = sendImage");


        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "sendChatMessageRequest : "+e.toString());

        }
        Log.d(TAG, "convertMessageToJson: 메시지 직력화 완료");
        if (isNewChatRoom == true){// 이 불린값은 방생성후 최초 메시지를 구분하위함이다.
            //왜냐하면 방을 생성하고 메시지를 보내지 않고 나간경우 채팅방을 만들지 않고, 참여자로 등록도 하지 않게 하려하기 때문이다.
            // 방을 생성하고 최초로 메시지를 보내면 보낸이가 채팅방의 참가자로 서버db에 등록된다.
            // 다음음 또 보냈을때 서버에서 또 참가자로 등록하지 않게하기위해 이 구분자를 false로 바꿔 보낸다.
            isNewChatRoom = false;
//            anounceNewUser = false;
        }
        return jObject_request;
    }
}
