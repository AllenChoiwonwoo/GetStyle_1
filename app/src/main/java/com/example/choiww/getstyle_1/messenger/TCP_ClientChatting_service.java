package com.example.choiww.getstyle_1.messenger;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.example.choiww.getstyle_1.DataClass.Messages_dataClass;
import com.example.choiww.getstyle_1.DataClass.roomInfoDataClass;
import com.example.choiww.getstyle_1.main;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static java.util.Arrays.asList;

public class TCP_ClientChatting_service extends Service {
    String TAG = "find";

    String ipAddress;
    static final int port=6000;
    SocketClient client=null;
    Socket socket = null;
    BufferedReader read;
    PrintWriter oos;
    BufferedReader ois;
    String sendData;
    String receiveData;

    ReceiveThread receive;
    SendThread send;

    String user_id;
    String nickName;
    ReceiveDataThread rt;
    boolean endflag=false;
    String server_ipAddress="192.168.152.60";
    ICallback callback_makeNewChatRoom;
    ICallback callback_chatRoom;
    ICallback callBack_joinChaRoomList;
    ICallback callback_chatRoomList;

    Handler handler;
    IBinder mBinder = new TCP_chatting_serv_binder();
    String sendMessage;

    public TCP_ClientChatting_service() {

    }

    @SuppressLint("HandlerLeak")
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: 서비스 시작됨");
        client = new SocketClient(main.userId,"yeji",server_ipAddress,"5001" );
        client.start();


        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Gson gson = new Gson();

                if(msg.what == 1){
                    callback_makeNewChatRoom.remoteCall(msg);
                }else if(msg.what == 2){
//                    callBack_joinChaRoomList.remoteCall();
                }else if(msg.what == 3){
//                    callback_chatRoom.remoteCall(msg);// msg.obj 에 채팅메시지 데이터가 들어있다.
                    // * db에 메시지 내용을 저장하는 코드 추가 필요
                    HashMap receivedChatData = new HashMap();
                    try {
                        JSONObject chatData_jObject = new JSONObject(msg.obj.toString());
                        String received_userId = chatData_jObject.get("userId").toString();
                        receivedChatData.put("userId",received_userId);
                        String received_userEmail = chatData_jObject.get("userEmail").toString();
                        receivedChatData.put("userEmail",received_userEmail);
                        String received_roomNumb = chatData_jObject.get("roomNumb").toString();
                        receivedChatData.put("roomNumb",received_roomNumb);
                        String received_message = chatData_jObject.get("message").toString();
                        receivedChatData.put("message",received_message);
                        String received_createdDate = chatData_jObject.get("sendTime").toString();
                        receivedChatData.put("sendTime",received_createdDate);
                        String received_messageType = chatData_jObject.get("messageType").toString();
                        receivedChatData.put("messageType",received_messageType);
//                        Boolean received_isNewChatRoom = (Boolean) chatData_jObject.get("isNewChatRoom");
                        callback_chatRoom.sendMessage(receivedChatData);
                        // * 이 데이터들을 활용해 데이터를 앱내부 sqlite에 저장하면된다.
                    } catch (JSONException e) {

                        e.printStackTrace();
                    }


                }else if(msg.what == 4){
                    String Str_jsonArray = (String)msg.obj;// 이걸 gson 으로
                    Log.d(TAG, "receivedThread - handleMessage: 서비스에서 참여중 채팅방 목록 받음 : ");

                    roomInfoDataClass[] array = gson.fromJson(Str_jsonArray, roomInfoDataClass[].class);
                    List<roomInfoDataClass> joinChatRoomList = Arrays.asList(array);
                    msg.obj = joinChatRoomList;
                    callBack_joinChaRoomList.remoteCall(msg);

                }
                else if(msg.what == 5){
                    Log.d(TAG, "receivedThread - handleMessage: 서비스에서 채팅방전채메시지 받음 : ");
                    String str_jsonArray = (String)msg.obj;
                    Log.d(TAG, "receivedThread - handleMessage: 서비스에서 채팅방전채메시지 받음 : "+str_jsonArray);

                    Messages_dataClass[] array = gson.fromJson(str_jsonArray, Messages_dataClass[].class);
                    List<Messages_dataClass> messageList = Arrays.asList(array);
                    msg.obj = messageList;
                    callBack_joinChaRoomList.remoteCall(msg);
                }else if(msg.what == 6){
                    Log.d(TAG, "ReceiveThread-handleMessage: 서비스에서 전체 채팅방목록을 받음");
                    String str_jsonArray = (String)msg.obj;

                    roomInfoDataClass[] array = gson.fromJson(str_jsonArray, roomInfoDataClass[].class);
                    List<roomInfoDataClass> allChatRoomList = Arrays.asList(array);
                    msg.obj = allChatRoomList;
                    callback_chatRoomList.remoteCall(msg);

                }else if(msg.what == 7){
                    Log.d(TAG, "ReceiveThread -handleMessage: 서비스에서 하나의 채팅방의 상세 정보를 받음");
//                    String str_jsonObject = (String) msg.obj;
//                    roomInfoDataClass roomDetail = gson.fromJson(str_jsonObject, roomInfoDataClass.class);
//                    msg.obj = roomDetail;
                    callback_chatRoomList.remoteCall(msg);

                }
            }
        };
    }

    @Override
    public IBinder onBind(Intent intent) {
//        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
        Log.d(TAG, "onBind:  바인드 실행");
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);

//        ChatClient chatClient = new ChatCliet();

//        Log.d(TAG, "onStartCommand: 클라이언트");
//        System.out.println("**** 클라이언트*****");

    }
    // bind한 엑티비티의 메서드에 접근할 수 있게하는 객채를 변수에 담아놓는다.
    public void registerCallback(String activityName, ICallback mCallback) {
        if (activityName.equals("makeNewChatRoom_activity")){
            Log.d(TAG, "registerCallback: mCallback 에 makeNewChatRoom Activity 등록됨");
            callback_makeNewChatRoom = mCallback;
        }else if(activityName.equals("chatRoom_activity")){
            Log.d(TAG, "registerCallback: mCallback 에 chatRoom Activity 등록됨");
            callback_chatRoom = mCallback;
        }else if(activityName.equals("joiningChatRoomList_activity")){
            Log.d(TAG, "registerCallback: mCallback 에 joiningChatRoomList_activity 등록됨");
            callBack_joinChaRoomList = mCallback;
        }else if(activityName.equals("chatRoomList_activity")){
            Log.d(TAG, "registerCallback: mCallback 에 ChatRoomList_activity 등록됨");
            callback_chatRoomList = mCallback;
        }

    }
    // 엑티비티에서 서비스를 바인드시, 엑티비티에서 메서드를 발동시킬 수 있다.
    public void myServiceFunc(String str){
        Log.d(TAG, "myServiceFunc: 엑티비티에서 서비스의 메서드 실행시킴 . str = "+str);
        // 여기에 쓰레드를통해서 tcp서버로 새채팅방 생성을 요청해야한다.
        sendMessage(str);
    }

    public void sendMessage(String str){
        Log.d(TAG, "sendMessage: "+str);
        this.sendMessage = str;
        send = new SendThread(socket);
        send.start();
    }

    public class TCP_chatting_serv_binder extends Binder {
        TCP_ClientChatting_service getService(){
            return TCP_ClientChatting_service.this;
        }
    }

    public interface  ICallback{
        void remoteCall();
        void remoteCall(Message message);
        void sendMessage(HashMap hashMap);
    }

    class SocketClient extends Thread{
        boolean threadAlive;

        String ip;
        String port;
        int userId;

        OutputStream outputStream = null;
        DataOutputStream output = null;
        public SocketClient(int userid, String nickname, String ip, String port){
            threadAlive = true;
            nickName = nickname;
            this.userId = userid;
            this.ip =ip;
            this.port = port;
            Log.d(TAG, "SocketClient: 소켓 연결 준비");
        }
        public void run(){
            try{
                socket = new Socket(ip, Integer.parseInt(port));
                Log.d(TAG, "run: 서버와 소켓연결 성공 : "+socket.toString());
                output = new DataOutputStream(socket.getOutputStream());
                receive = new ReceiveThread(socket); // 쓰레드에서 반복문을 통해서 서버가 보내는 메시지를 받아온다.
                receive.start();

//                WifiManager mng = (WifiManager) context.getSystemService( WIFI_SERVICE);
//                WifiInfo info = mng.getConnectionInfo();
//                mac = info.getMacAddress();
                output.writeUTF(userId+"");
                Log.d(TAG, "run: 서버로 클라의 회원번호 전송");
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    class ReceiveThread extends Thread {
        Socket socket = null;
        DataInputStream input = null;
        public ReceiveThread(Socket socket){
            this.socket = socket;
            try {
                input = new DataInputStream(socket.getInputStream());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        public void run(){
            try{
                while(input != null){
                    Log.d(TAG, "ReceiveThread- Run: inputStream을 통해 데이터 받기 스타트");
                    // 채팅 서버로부터 받은 메시지
                    String msg = input.readUTF();
                    Log.d(TAG, "ReceiveThread-run: 받은 메시지 : "+msg);
                    if(msg != null){
                        try {
                            JSONObject receivedData = new JSONObject(msg);
                            String delimiter = receivedData.getString("response");
                            Log.d(TAG, "ReceiveThread-run: TCP_ClientChatting_service 에서 response를 받음 response : "+delimiter);
                            int newRoomNumb;
                            if (delimiter.equals("response_newGroupChat")){
                                Log.d(TAG, "ReceiveThread-run: 서버에서 온 메시지 : 채팅 만들기 완료");
                                // 새로운 그룹채팅방을 생성하기에 대한 응답
                                // 데이터로 새로운 방의 방번호가 온다.
                                newRoomNumb = (int) receivedData.get("data");

//                                핸들러에게 전달할 메시지 객체호출(이미 새성되어 있는 객체 재활용)
                                Message hdmsg = handler.obtainMessage();
                                hdmsg.what = 1;//메시지의 식별자 : 새채팅방 만들기 요청이 처리되어 생성된 새 채팅방 번호가 돌아옴
                                hdmsg.obj = newRoomNumb; // 메시지의 본문
                                Log.d(TAG, "ReceiveThread-run: 방번호 : "+newRoomNumb);
                                handler.sendMessage(hdmsg);//이 메서드(handler.sendmessage)를 사용하면 handler의 handleMessage 메서드가 발동된다.

                            }else if(delimiter.equals("newConnection")){
                                Log.d(TAG, "ReceiveThread-run: 서버에서 온 메시지 : 새로운 유저의 소켓이 연결됨");
                                Message hdmsg = handler.obtainMessage();
                                hdmsg.what = 2;//메시지의 식별자 : 누군가 서버와 연결되었음
                                hdmsg.obj = receivedData.getString("nickName");
                                handler.sendMessage(hdmsg);//이 메서드(handler.sendmessage)를 사용하면 handler의 handleMessage 메서드가 발동된다.

                            }else if(delimiter.equals("sendMessage")){
                                Log.d(TAG, "ReceiveThread-run: 같은 채팅방의 참여자가 보낸 채팅을 서버에서 보내줌");
                                Message hdmsg = handler.obtainMessage();
                                hdmsg.what = 3;//메시지의 식별자 : 채팅메시지를 받음
                                hdmsg.obj = receivedData.getString("data");
                                handler.sendMessage(hdmsg);

                            }else if(delimiter.equals("joinChatRoomList")){
                                Log.d(TAG, "ReceiveThread - run: ");
                                Message hdmsg = handler.obtainMessage();
                                hdmsg.what = 4;
                                hdmsg.obj = receivedData.getString("data");// jsonArray 가 들어있다.(gson을 통해 array<dataClass>로 치환 가능
                                handler.sendMessage(hdmsg);

                            }
                            else if(delimiter.equals("chatRoomsMessages")){
                                Log.d(TAG, "ReceiveThread-run: (아직 앱db에 메시지를 저장하지 않아) 들어가려는 참여중인 채팅방의 지금까지 온 메시지를 받아온다.");
                                Message hdmsg = handler.obtainMessage();
                                hdmsg.what = 5;
                                hdmsg.obj = receivedData.getString("data");// jsonArray 형태로 메시지들이 담겨있다.
                                Log.d(TAG, "run: 메시지들 data = "+hdmsg.obj);
                                hdmsg.arg1 = Integer.parseInt(receivedData.getString("roomNumb"));// 방번호
                                handler.sendMessage(hdmsg);

                            }else if(delimiter.equals("allChatRoomList")){
                                Log.d(TAG, "ReceiveThread-run: 전채 채팅방 목록을 서버에서 받아옴");
                                Message hdmsg = handler.obtainMessage();
                                hdmsg.what = 6;
                                hdmsg.obj = receivedData.getString("data");// jsonArray 형태로 메시지들이 담겨있다.
                                Log.d(TAG, "ReceiveThread-run: 서버에서 준 전체 채팅방 목록 : "+hdmsg.obj);
                                handler.sendMessage(hdmsg);

                            }else if(delimiter.equals("getOneChatRoomProfile")) {
                                Log.d(TAG, "ReceiveThread-run: 하나의 채팅방 정보 받아옴");
                                Message hdmsg = handler.obtainMessage();
                                hdmsg.what = 7;
                                hdmsg.obj = receivedData.getString("data");
                                Log.d(TAG, "ReceiveThread-run: 서버에서 준 하나의 채팅방 프로필 정보를 받아옴" + hdmsg.obj);
                                handler.sendMessage(hdmsg);
                            }
                            else{
                                Log.d(TAG, "ReceiveThread-run: 메시지 받기 - 일치하는 response가 없음");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

//                        핸들러에게 전달할 메시지 객체호출(이미 새성되어 있는 객체 재활용)
//                        Message hdmsg = handler.obtainMessage();
//                        hdmsg.what = 111;//메시지의 식별자
//                        hdmsg.obj = msg; // 메시지의 본문
                        //핸들러에게 메시지 전달(화면 변경 요청)
//                        msgHandler.sendMessage(hdmsg);
//                        mCallback_chat1.remoteCall(hdmsg);// 서비스의 메시지 받는 쓰레드가 받은 메시지를 콜백 메서드를 통해서 엑티비티로 보내준다.
//                        if (mCallback_chat1 == null) {
////                            Log.d(TAG, "run: 채팅 서버의 환영 메시지 : "+msg);
////                        }else {
////                            msgHandler.sendMessage(hdmsg);
//////                            mCallback_chat1.remoteCall(hdmsg);
////                            // ###스레드에서 바로 콜백의 remoteCall 메서드로 화면의 변경하니 에러가 떴고(다른쓰레드에서 ui에 접근 못한다는 에러)
////                            // ###핸들러-handlemessage 를 통해 서비스로 데이터를 넘겨서 서비스가 콜백의 remoteCall 메서드를 실행하게 하니 화면에 보낸 메시지가 보였다.
////
////                        }
//                        handler.sendMessage(hdmsg);//이 메서드(handler.sendmessage)를 사용하면 handler의 handleMessage 메서드가 발동된다.
                        //그러므로 handleMessage를 통해 엑티비티 화면에 받은 메시지를 보여줄 수 있다.

                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    class SendThread extends Thread{
        Socket socket;
        //        String sendmsg = editMessage.getText().toString();
        String sendmsg = " test1 "; // 여기에 엑티비티에서 받아온 보낼 메시지를 넣어야한다.
        DataOutputStream output;
        public SendThread(Socket socket){
            this.socket = socket;
            try{
                //채팅서버로 메시지를 보내기 위한 스트림 생성
                output = new DataOutputStream(socket.getOutputStream());
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        public void run(){
            try{
                if(output != null){
                    if(sendMessage != null){
//                        output.writeUTF(mac+":"+sendmsg);
                        // 여기서 메시지 데이터를 json화해서 보내야것네
                        Log.d(TAG, "run: sendThread - tcp서버로 데이터를 전송한다.");
//                        output.writeUTF(nickName+">> :"+sendMessage);
                        output.writeUTF(sendMessage);
                    }else {
                        Log.d(TAG, "run: 보낼 메시지가 없습니다.");
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
