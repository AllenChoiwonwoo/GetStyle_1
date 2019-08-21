package com.example.choiww.getstyle_1;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.choiww.getstyle_1.AdminMode.UserManagingDetailActivity;
import com.example.choiww.getstyle_1.DataClass.userInfoData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class Adapter_userList extends  RecyclerView.Adapter<Adapter_userList.viewHoler>{

    public ArrayList<userInfoData> arrayList;
    public Context mcontext;

    class viewHoler extends RecyclerView.ViewHolder{
        TextView userListItem_name_tv;
        TextView userListItem_userId_tv;
        TextView userListItem_nickName_tv;
        ImageView userListItem_goUserDetail_img;

        public viewHoler(@NonNull View itemView) {
            super(itemView);
            this.userListItem_name_tv = itemView.findViewById(R.id.userListItem_name_tv);
            this.userListItem_userId_tv = itemView.findViewById(R.id.userListItem_userId_tv);
            this.userListItem_nickName_tv = itemView.findViewById(R.id.userListItem_nickName_tv);
            this.userListItem_goUserDetail_img = itemView.findViewById(R.id.userListItem_goUserDetail_img);
        }
    }

    public Adapter_userList(Context context, ArrayList arrayList){
        this.mcontext = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public viewHoler onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_user_list_item,viewGroup,false);
//        View view = LayoutInflater.from(viewGroup.getContext())inflate(R.layout.orderhistoryitemlayout,viewGroup, false);
        viewHoler viewHoler = new viewHoler(view);

        return viewHoler;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHoler viewHoler, final int i) {
        viewHoler.userListItem_name_tv.setText(arrayList.get(i).getUserName());
        viewHoler.userListItem_userId_tv.setText(arrayList.get(i).getUserEmail());
        viewHoler.userListItem_nickName_tv.setText(arrayList.get(i).getUserNickname());
        // 회원상세 페이지로 넘어갈 수 있게 해주는 버튼
        // 회원번호를 같이 넘겨줘야 서버에서 한명의 회원정보만 받아올 수 있다.
        viewHoler.userListItem_goUserDetail_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext,UserManagingDetailActivity.class);
                intent.putExtra("userNumb", arrayList.get(i).getId());
                mcontext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
