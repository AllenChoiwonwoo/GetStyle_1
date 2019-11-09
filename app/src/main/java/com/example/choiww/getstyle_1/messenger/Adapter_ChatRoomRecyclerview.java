package com.example.choiww.getstyle_1.messenger;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.choiww.getstyle_1.DataClass.roomInfoDataClass;
import com.example.choiww.getstyle_1.R;
import com.example.choiww.getstyle_1.main;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class Adapter_ChatRoomRecyclerview extends RecyclerView.Adapter<Adapter_ChatRoomRecyclerview.ChatRoomViewholder> {
//    ArrayList<roomInfoDataClass> joinChatRoomList;
    ArrayList<roomInfoDataClass> chatRoomList;
    JoiningChatRoomList jcrl;
    ChatRoomList crl;
    Intent intent;
    String TAG = "find";
    boolean isChatRoomList = false;
    Context mcontext;


    public Adapter_ChatRoomRecyclerview(Context mcontext, ArrayList list, JoiningChatRoomList jcrl, Intent intent){
        this.chatRoomList = list;
        this.jcrl = jcrl;
        this.intent = intent;
        this.mcontext = mcontext;
    }
    public Adapter_ChatRoomRecyclerview(Context mcontext, ArrayList list, ChatRoomList crl){
        this.chatRoomList = list;
        this.crl = crl;
        this.intent = intent;
        isChatRoomList = true;
        this.mcontext = mcontext;
    }

    public class ChatRoomViewholder extends RecyclerView.ViewHolder{
        ConstraintLayout chatRoomListItem_constraintLayout;
        TextView chatRoomListItem_roomName_tv; // 방이름
        TextView chatRoomListItem_joinUserVolume_tv; // 참여인원
        ImageView chatRoomListItem_profile_img; // 프로필이미지
        TextView chatRoomListItem_lastMessage_tv; // 마지막 메시지
        TextView chatRoomListItem_lastMessageTime_tv; // 마지막 메시지 온 시간
        TextView chatRoomListItem_notReadMessageCount_tv; // 내가 안읽은 메시지 개수


        public ChatRoomViewholder(@NonNull View v) {
            super(v);
            chatRoomListItem_constraintLayout = v.findViewById(R.id.chatRoomListItem_constraintLayout);
            chatRoomListItem_roomName_tv = v.findViewById(R.id.chatRoomListItem_roomName_tv);
            chatRoomListItem_joinUserVolume_tv = v.findViewById(R.id.chatRoomListItem_joinUserVolume_tv);
            chatRoomListItem_profile_img = v.findViewById(R.id.chatRoomListItem_profile_img);

            chatRoomListItem_lastMessage_tv = v.findViewById(R.id.chatRoomListItem_lastMessage_tv);
            // chatRoomList 시에 마지막메시지view를 태그를 보이는 뷰로 활용
            chatRoomListItem_lastMessageTime_tv = v.findViewById(R.id.chatRoomListItem_lastMessageTime_tv);
            chatRoomListItem_notReadMessageCount_tv = v.findViewById(R.id.chatRoomListItem_notReadMessageCount_tv);
            if (isChatRoomList){
//                chatRoomListItem_lastMessage_tv.setVisibility(View.INVISIBLE);
                chatRoomListItem_lastMessageTime_tv.setVisibility(View.INVISIBLE);
                chatRoomListItem_notReadMessageCount_tv.setVisibility(View.INVISIBLE);
            }

        }
    }

    @NonNull
    @Override
    public ChatRoomViewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_chat_room_list_item,viewGroup,false);
        ChatRoomViewholder viewholder = new ChatRoomViewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ChatRoomViewholder viewHolder, final int i) {
        viewHolder.chatRoomListItem_roomName_tv.setText(chatRoomList.get(i).getRoomName());
        Log.d(TAG, "adapter_chatRoomRV-onBindViewHolder: 이 어댑터를 통해 이미지가들어간다는것?");
        
        Log.d(TAG, "Adapter_allchatRommList_adapter - onBindViewHolder: get_numb ="+chatRoomList.get(i).getRoomNumb());
        Log.d(TAG, "onBindViewHolder: getRoomName = "+chatRoomList.get(i).getRoomName());
        Log.d(TAG, "onBindViewHolder: ");
//        viewHolder.chatRoomListItem_joinUserVolume_tv
//        viewHolder.chatRoomListItem_profile_img
//        viewHolder.chatRoomListItem_lastMessage_tv
        if(isChatRoomList){
            viewHolder.chatRoomListItem_lastMessage_tv.setText(chatRoomList.get(i).getRoomTages());
        }
//        viewHolder.chatRoomListItem_lastMessageTime_tv
//        viewHolder.chatRoomListItem_notReadMessageCount_tv
        viewHolder.chatRoomListItem_constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isChatRoomList){
                    Log.d(TAG, "onClick: 전체 채팅방 리스트 보기 recyclerview에서 하나의 채팅방이 클릭됨 -> 채팅방 프로필화면으로 가야함");
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("request", "getOneChatRoomProfile");
                        jsonObject.put("data", chatRoomList.get(i).getRoomNumb());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    crl.tcpConn.myServiceFunc(jsonObject.toString());

                }else {
                    /*// 이코드는 기존의 joiningChatRoomList 에서 메시지들을 받아서 chatRoom으로 넘겨주는 방식이다.
                    Log.d(TAG, "onClick: 참여중채팅방리스트 보기 recyclerview에서 하나의 채팅방(아이탬)이 클릭됨");
//                intent.putExtra("roomNumb", joinChatRoomList.get(i).get_numb());
                    JSONObject jsonObject = new JSONObject();
                    JSONObject data_jObject = new JSONObject();
                    try {
                        data_jObject.put("roomNumb",chatRoomList.get(i).getRoomNumb()+"");
                        Log.d(TAG, "onClick: 클릭된 방번호 : "+chatRoomList.get(i).getRoomNumb());
                        data_jObject.put("userId", main.userId+"");
                        //
//                    data_jObject.put("",)

                        jsonObject.put("request","sendAllChatMessageInThisRoom");
                        jsonObject.put("data", data_jObject.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jcrl.tcpConn.myServiceFunc(jsonObject.toString());
*/
                    // 이건 선택한 방번호만 chatRoom으로 넘겨서 거기서 메시지를 받아오게 하는 코드이다.
                    Intent goChatRoom_intent = new Intent(mcontext,ChatRoom.class);
                    goChatRoom_intent.putExtra("joiningRoomNumb", chatRoomList.get(i).getRoomNumb());
                    mcontext.startActivity(goChatRoom_intent);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return chatRoomList.size();
    }

}
