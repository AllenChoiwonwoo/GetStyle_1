package com.example.choiww.getstyle_1.messenger;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.choiww.getstyle_1.DataClass.Messages_dataClass;
import com.example.choiww.getstyle_1.R;
import com.example.choiww.getstyle_1.main;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class Adapter_ChattingRecyclerview extends RecyclerView.Adapter<Adapter_ChattingRecyclerview.chatMessageViewholder> {

    public ArrayList<Messages_dataClass> arrayList;
    public Context mcontext;
    String TAG = "find";

    class chatMessageViewholder extends RecyclerView.ViewHolder{
        ImageView chatItem_profileImg_img;
        TextView chatItem_nickname_tv;
        TextView chatItem_message_tv;// 상대방의 채팅
        TextView chatItem_message2_tv;// 내가 보낸 채팅(우측정렬)
        ImageView chatItem_image_img;
        TextView chatItem_time_tv;

        public chatMessageViewholder(@NonNull View itemView) {
            super(itemView);
            this.chatItem_profileImg_img = itemView.findViewById(R.id.chatItem_profileImg_img);
            this.chatItem_nickname_tv = itemView.findViewById(R.id.chatItem_nickname_tv);
            this.chatItem_message_tv = itemView.findViewById(R.id.chatItem_message_tv);
            this.chatItem_message2_tv = itemView.findViewById(R.id.chatItem_message2_tv);
            this.chatItem_image_img = itemView.findViewById(R.id.chatItem_image_img);
            this.chatItem_time_tv = itemView.findViewById(R.id.chatItem_time_tv);
        }
    }
    public Adapter_ChattingRecyclerview(Context applicationContext, ArrayList chatMessage_array) {
        this.arrayList = chatMessage_array;
        this.mcontext = applicationContext;
    }

    @NonNull
    @Override
    public chatMessageViewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_chat_message_item,viewGroup,false);
        chatMessageViewholder viewholder = new chatMessageViewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull chatMessageViewholder viewholder, int i) {
//        viewholder.chatItem_image_img.set // 프로필사진은 나중에 추가, 피카소로 해야함
        Log.d(TAG, "onBindViewHolder: i = "+i);
        String messageType = arrayList.get(i).getMessageType();
        if(messageType == null){
            Log.d(TAG, "onBindViewHolder: "+i+"번째 messageType = "+messageType);
            messageType = "0";
        }else {
            Log.d(TAG, "onBindViewHolder: "+i+"번째 messageType = "+messageType);
        }
        int getUserId = Integer.parseInt(arrayList.get(i).getUserId());
        if (Integer.parseInt(arrayList.get(i).getUserId()) > 5){// 이미지 임시 변경
            viewholder.chatItem_profileImg_img.setImageResource(R.drawable.grouptalkprofile1);
        }else {
            viewholder.chatItem_profileImg_img.setImageResource(R.drawable.grouptalkprofile2);
        }

        if (main.userId != getUserId){// 타인이 보낸 메시지

            if (messageType.equals("0")){//일반 메시지
                viewholder.chatItem_profileImg_img.setVisibility(View.VISIBLE);
                viewholder.chatItem_nickname_tv.setVisibility(View.VISIBLE);
                viewholder.chatItem_time_tv.setText((String)arrayList.get(i).getSendTime());
                viewholder.chatItem_message2_tv.setVisibility(View.GONE);
                viewholder.chatItem_message_tv.setVisibility(View.VISIBLE);
                viewholder.chatItem_message_tv.setText(arrayList.get(i).getMessage());
            }else if(messageType.equals("1")){//알림
                viewholder.chatItem_profileImg_img.setVisibility(View.GONE);
                viewholder.chatItem_nickname_tv.setVisibility(View.GONE);
                viewholder.chatItem_time_tv.setVisibility(View.GONE);
                viewholder.chatItem_message2_tv.setVisibility(View.GONE);
                viewholder.chatItem_message_tv.setText(arrayList.get(i).getMessage());
                viewholder.chatItem_message_tv.setBackgroundColor(Color.GRAY);// 안내문은 회색으로

            }else if(messageType.equals("2")){

            }
        }else {//내가 보낸 메시지
            if (messageType.equals("0")){//일반 메시지
                viewholder.chatItem_profileImg_img.setVisibility(View.GONE);
                viewholder.chatItem_nickname_tv.setVisibility(View.GONE);
                viewholder.chatItem_time_tv.setText((String)arrayList.get(i).getSendTime());
                viewholder.chatItem_message_tv.setVisibility(View.GONE);
                viewholder.chatItem_message2_tv.setVisibility(View.VISIBLE);
                viewholder.chatItem_message2_tv.setText(arrayList.get(i).getMessage());

            }else if(messageType.equals("1")){
                // 1은 알림 메시지시 ( 채팅방 입장 or 퇴장)
                viewholder.chatItem_profileImg_img.setVisibility(View.GONE);
                viewholder.chatItem_nickname_tv.setVisibility(View.GONE);
                viewholder.chatItem_time_tv.setVisibility(View.GONE);
                viewholder.chatItem_message2_tv.setVisibility(View.GONE);
                viewholder.chatItem_message_tv.setText(arrayList.get(i).getMessage());
                viewholder.chatItem_message_tv.setBackgroundColor(Color.GRAY);// 안내문은 회색으로

            }else if(messageType.equals("2")){//사진

            }
        }


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
