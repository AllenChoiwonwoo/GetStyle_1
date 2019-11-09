package com.example.choiww.getstyle_1.messenger;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Adapter_ChattingRecyclerview extends RecyclerView.Adapter<Adapter_ChattingRecyclerview.chatMessageViewholder> {

    public ArrayList<Messages_dataClass> arrayList;
    public Context mcontext;
    String TAG = "find";
    Date sendTime;
    SimpleDateFormat msgDateFormat;
    String msgDataTime;

    class chatMessageViewholder extends RecyclerView.ViewHolder{
        ConstraintLayout other_item;
        ImageView other_chatItem_profileImg_img;
        TextView other_chatItem_nickname_tv;
        TextView other_chatItem_message_tv;// 상대방의 채팅
//        TextView chatItem_message2_tv;// 내가 보낸 채팅(우측정렬)
        ImageView other_chatItem_image_img;
        TextView other_chatItem_time_tv;

        ConstraintLayout my_item;
        TextView my_chatItem_message_tv;
        ImageView my_chatItem_image_img;
        TextView my_chatItem_time_tv;

        ConstraintLayout alert_item;
        TextView alert_message_tx;


        public chatMessageViewholder(@NonNull View itemView) {
            super(itemView);
            this.other_item = itemView.findViewById(R.id.other_item);
            this.other_chatItem_profileImg_img = itemView.findViewById(R.id.other_chatItem_profileImg_img);
            this.other_chatItem_nickname_tv = itemView.findViewById(R.id.other_chatItem_nickname_tv);
            this.other_chatItem_message_tv = itemView.findViewById(R.id.other_chatItem_message_tv);
            this.other_chatItem_image_img = itemView.findViewById(R.id.other_chatItem_image_img);
            this.other_chatItem_time_tv = itemView.findViewById(R.id.other_chatItem_time_tv);

            this.my_item = itemView.findViewById(R.id.my_item);
            this.my_chatItem_message_tv = itemView.findViewById(R.id.my_chatItem_message_tv);
            this.my_chatItem_image_img = itemView.findViewById(R.id.my_chatItem_image_img);
            this.my_chatItem_time_tv = itemView.findViewById(R.id.my_chatItem_time_tv);

            this.alert_item = itemView.findViewById(R.id.alert_item);
            this.alert_message_tx = itemView.findViewById(R.id.alert_message_tx);
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
        }else if(messageType.equals("3")){ // 이미지일 경우

        }
        else {
            Log.d(TAG, "onBindViewHolder: "+i+"번째 messageType = "+messageType);
        }
        int getUserId = Integer.parseInt(arrayList.get(i).getUserId());
        if (Integer.parseInt(arrayList.get(i).getUserId()) > 5){// 이미지 임시 변경
            viewholder.other_chatItem_profileImg_img.setImageResource(R.drawable.grouptalkprofile1);
        }else {
            viewholder.other_chatItem_profileImg_img.setImageResource(R.drawable.grouptalkprofile2);
        }
        // 메시지 시간(sendTime) 쓰기 쉽게 변환
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Log.d(TAG, "onBindViewHolder: 메시지의 getSendTime : "+arrayList.get(i).getSendTime());
        try {
            sendTime = dateFormat.parse(arrayList.get(i).getSendTime());
            msgDateFormat= new SimpleDateFormat("MM월dd일 HH시mm분");
            msgDataTime = msgDateFormat.format(sendTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (main.userId != getUserId){// 타인이 보낸 메시지

            if (messageType.equals("0")){//일반 메시지
                viewholder.my_item.setVisibility(View.GONE);
                viewholder.other_item.setVisibility(View.VISIBLE);
                viewholder.alert_item.setVisibility(View.GONE);
                viewholder.other_chatItem_nickname_tv.setText(arrayList.get(i).getUserEmail());
                viewholder.other_chatItem_message_tv.setVisibility(View.VISIBLE);
                viewholder.other_chatItem_message_tv.setText(arrayList.get(i).getMessage());
                Log.d(TAG, "adapter - onBindViewHolder: 상대가 보낸 메시지 : "+arrayList.get(i).getMessage());
//                viewholder.other_chatItem_image_img.setImageBitmap(arrayList.get(i).getBitmap());
                viewholder.other_chatItem_image_img.setVisibility(View.GONE);
                viewholder.other_chatItem_time_tv.setText(msgDataTime);

            }else if(messageType.equals("1")){//알림
                viewholder.my_item.setVisibility(View.GONE);
                viewholder.other_item.setVisibility(View.GONE);
                viewholder.alert_item.setVisibility(View.VISIBLE);
                viewholder.alert_message_tx.setText(arrayList.get(i).getMessage());
                Log.d(TAG, "adapter - onBindViewHolder: 상대가 보낸 메시지 : "+arrayList.get(i).getMessage());


            }else if(messageType.equals("2")){

            }else if(messageType.equals("3")){//이미지일 경우
                viewholder.my_item.setVisibility(View.GONE);
                viewholder.other_item.setVisibility(View.VISIBLE);
                viewholder.alert_item.setVisibility(View.GONE);
//                viewholder.other_chatItem_profileImg_img.setImageBitmap(arrayList.get(i).getBitmap());
                viewholder.other_chatItem_nickname_tv.setText(arrayList.get(i).getUserEmail());
                viewholder.other_chatItem_message_tv.setVisibility(View.GONE);
                viewholder.other_chatItem_image_img.setVisibility(View.VISIBLE);
//                viewholder.other_chatItem_message_tv.setText(arrayList.get(i).getMessage());
                viewholder.other_chatItem_image_img.setImageBitmap(arrayList.get(i).getBitmap());
                viewholder.other_chatItem_time_tv.setText(msgDataTime);

                Log.d(TAG, "onBindViewHolder: bitmap Name =  "+arrayList.get(i).getBitmapName());

            }
            /*만약
            이전 메시지가 1(알림)이 아니고,
            이전 메시지가 같은 userid가 보낸 메시지 이고,
            이전 메시지와 같은 시간에 보낸 메시지 이면

             */


        }else {//내가 보낸 메시지
            if (messageType.equals("0")){//일반 메시지
                viewholder.my_item.setVisibility(View.VISIBLE);
                viewholder.other_item.setVisibility(View.GONE);
                viewholder.alert_item.setVisibility(View.GONE);
                viewholder.my_chatItem_image_img.setVisibility(View.GONE);
                Log.d(TAG, "adapter - onBindViewHolder: 내가 보낸 메시지가 서버에 갔다가 돌아옴 : "+arrayList.get(i).getMessage());
                viewholder.my_chatItem_message_tv.setText(arrayList.get(i).getMessage());
                viewholder.my_chatItem_time_tv.setText(msgDataTime);

            }else if(messageType.equals("1")){
                // 1은 알림 메시지시 ( 채팅방 입장 or 퇴장)
                viewholder.my_item.setVisibility(View.GONE);
                viewholder.other_item.setVisibility(View.GONE);
                viewholder.alert_item.setVisibility(View.VISIBLE);
                viewholder.alert_message_tx.setText(arrayList.get(i).getMessage());
                Log.d(TAG, "adapter - onBindViewHolder: 내가 보낸 메시지가 서버에 갔다가 돌아옴 : "+arrayList.get(i).getMessage());

//                viewholder.my_chatItem_image_img.setVisibility(View.GONE);
//                viewholder.my_chatItem_time_tv.setVisibility(View.GONE);

//                viewholder.chatItem_profileImg_img.setVisibility(View.GONE);
//                viewholder.chatItem_nickname_tv.setVisibility(View.GONE);
//                viewholder.chatItem_time_tv.setVisibility(View.GONE);
//                viewholder.chatItem_message2_tv.setVisibility(View.GONE);
//                viewholder.chatItem_message_tv.setText(arrayList.get(i).getMessage());
//                viewholder.chatItem_message_tv.setBackgroundColor(Color.GRAY);// 안내문은 회색으로

            }else if(messageType.equals("2")){//사진

            }else if(messageType.equals("3")){
                // 이미지일 경우
                viewholder.my_item.setVisibility(View.VISIBLE);
                viewholder.other_item.setVisibility(View.GONE);
                viewholder.alert_item.setVisibility(View.GONE);
                viewholder.my_chatItem_message_tv.setVisibility(View.GONE);
                viewholder.my_chatItem_image_img.setVisibility(View.VISIBLE);
                viewholder.my_chatItem_image_img.setImageBitmap(arrayList.get(i).getBitmap());
                viewholder.my_chatItem_time_tv.setText(msgDataTime);

//                viewholder.chatItem_profileImg_img.setVisibility(View.GONE);
//                viewholder.chatItem_nickname_tv.setVisibility(View.GONE);
//                viewholder.chatItem_time_tv.setText((String)arrayList.get(i).getSendTime());
//                viewholder.chatItem_message_tv.setVisibility(View.GONE);
//                viewholder.chatItem_message2_tv.setVisibility(View.GONE);
//                viewholder.chatItem_image_img.setVisibility(View.VISIBLE);
//                viewholder.chatItem_image_img.setImageBitmap(arrayList.get(i).getBitmap());
                Log.d(TAG, "onBindViewHolder: bitmap Name =  "+arrayList.get(i).getBitmapName());
            }
        }


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
