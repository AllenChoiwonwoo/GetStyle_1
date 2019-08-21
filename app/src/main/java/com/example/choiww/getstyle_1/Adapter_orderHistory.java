package com.example.choiww.getstyle_1;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.choiww.getstyle_1.DataClass.SimpleOrderInfoData;

import java.util.ArrayList;
import java.util.HashMap;

import static android.support.constraint.Constraints.TAG;

/** 어텝터 */
public class Adapter_orderHistory extends RecyclerView.Adapter<Adapter_orderHistory.itemViewHolder>{

    public Context mcontext;
//    ArrayList<HashMap> arrayList;
    public ArrayList<SimpleOrderInfoData> arrayList;
    public boolean isAdmin = false;


    class itemViewHolder extends RecyclerView.ViewHolder{
        TextView orderHistory_orderNumb_tx;
        TextView orderHistory_orderDate_tx;
        TextView orderHistory_finalBill_tx;
        TextView orderHistory_orderState_tx;
        TextView orderHistory_deliveryNumb_tx;
        Button orderHistory_deliveryTracking_tx;
        ImageView orderHistory_orderDetail_img;

        public itemViewHolder(View itemView){
            super(itemView);
            orderHistory_orderNumb_tx=itemView.findViewById(R.id.orderHistory_orderNumb_tx);
            orderHistory_orderDate_tx= itemView.findViewById(R.id.orderHistory_orderDate_tx);
            orderHistory_finalBill_tx = itemView.findViewById(R.id.orderHistory_finalBill_tx);
            orderHistory_orderState_tx=itemView.findViewById(R.id.orderHistory_orderState_tx);
            orderHistory_deliveryNumb_tx=itemView.findViewById(R.id.orderHistory_deliveryNumb_tx);
            orderHistory_orderDetail_img=itemView.findViewById(R.id.orderHistory_orderDetail_img);
            orderHistory_deliveryTracking_tx=itemView.findViewById(R.id.orderHistory_deliveryTracking_tx);
        }
    }
    public Adapter_orderHistory(Context context, ArrayList arrayList){
        this.mcontext = context;
        this.arrayList = arrayList;
    }
    public Adapter_orderHistory(Context context, ArrayList arrayList, boolean len){
        this.mcontext = context;
        this.arrayList = arrayList;
        this.isAdmin = len;

    }

    @NonNull
    @Override
    public itemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.orderhistoryitemlayout,viewGroup, false);
        itemViewHolder viewHolder = new itemViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final itemViewHolder itemViewHolder, final int i) {
        if(isAdmin){
            itemViewHolder.orderHistory_orderNumb_tx.setText(arrayList.get(i).getNumb());
        }else {
            itemViewHolder.orderHistory_orderNumb_tx.setText(arrayList.get(i).getOrderNumb());
        }

        itemViewHolder.orderHistory_orderDate_tx.setText(arrayList.get(i).getOrderDate());
        itemViewHolder.orderHistory_finalBill_tx.setText(arrayList.get(i).getFinalBill()+"");
        itemViewHolder.orderHistory_orderState_tx.setText(arrayList.get(i).getOrderState());
        //현재 운송장번호 하드코딩 -> 서버에서 발급해 db에 저장하면 '주문내역'볼때 가져오기 필요
//        if (arrayList.get(i).getDeliveryNumb() == null)
        itemViewHolder.orderHistory_deliveryNumb_tx.setText("622539451773");
//        Log.d(TAG, "onBindViewHolder: numb 가 다 달야야함 : "+arrayList.get(i).getNumb());
        itemViewHolder.orderHistory_orderDetail_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 주문상세 페이지로 넘어가는 버튼 메서드
                Intent intent = new Intent(mcontext, OrderHistoryDetailActivity.class);
                intent.putExtra("numb",arrayList.get(i).getNumb());
                intent.putExtra("deliveryNumb" , itemViewHolder.orderHistory_deliveryNumb_tx.getText());
                intent.putExtra("isAdmin", isAdmin);
                mcontext.startActivity(intent);
            }
        });
        itemViewHolder.orderHistory_deliveryTracking_tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 배송추적 페이지로 이동하게 해주는 메서드
                Intent intent = new Intent(mcontext,deliveryTarckingActivity.class);
                intent.putExtra("deliveryNumb",itemViewHolder.orderHistory_deliveryNumb_tx.getText());
                mcontext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
