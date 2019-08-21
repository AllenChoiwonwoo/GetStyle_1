package com.example.choiww.getstyle_1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Adapter_OrderConfirm extends RecyclerView.Adapter<Adapter_OrderConfirm.itemViewHolder> {

    ArrayList<itemInfo> arrayList;
    Context mcontext;
    Adapter_OrderConfirm adapter;



    public class itemViewHolder extends RecyclerView.ViewHolder{
        CheckBox selectCheckBox_forBasket;
        TextView mall_name;
        ImageView thumb_img;
        TextView product_name;
        TextView product_price;
        TextView product_count;
        TextView delete_forBasket;
        TextView order_forBasket;
        TextView likes_forBasket;
        TextView soldOut;
        TextView sendItemToMallbasket;

        public itemViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mall_name = itemView.findViewById(R.id.mallName_forBasket);
            this.thumb_img = itemView.findViewById(R.id.img_forBasket);
            this.product_name = itemView.findViewById(R.id.prodName_forBasket);
            this.product_price = itemView.findViewById(R.id.price_forBasket);
            this.product_count = itemView.findViewById(R.id.count_forBasket);
            this.delete_forBasket = itemView.findViewById(R.id.delete_forBasket);
                this.delete_forBasket.setVisibility(View.INVISIBLE);
            this.order_forBasket = itemView.findViewById(R.id.order_forBasket);
                this.order_forBasket.setVisibility(View.INVISIBLE);
            this.likes_forBasket = itemView.findViewById(R.id.likes_forBasket);
                this.likes_forBasket.setVisibility(View.INVISIBLE);
            this.selectCheckBox_forBasket = itemView.findViewById(R.id.selectCheckBox__forBasket);
                this.selectCheckBox_forBasket.setVisibility(View.INVISIBLE);
            this.soldOut = itemView.findViewById(R.id.text_soldOut);
                this.soldOut.setVisibility(View.INVISIBLE);
            this.sendItemToMallbasket = itemView.findViewById(R.id.sendItem_text);
                this.sendItemToMallbasket.setVisibility(View.INVISIBLE);
        }
    }
    public Adapter_OrderConfirm(Context context, ArrayList list){
        this.mcontext =context;
        this.arrayList = list;
        this.adapter = this;
    }
    @NonNull
    @Override
    public itemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.basketviewholder,viewGroup, false);
        itemViewHolder viewHolder = new itemViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull itemViewHolder viewHolder, int i) {
        viewHolder.mall_name.setText(arrayList.get(i).getMallName());
        Picasso.with(mcontext).load(arrayList.get(i).getImg_src()).into(viewHolder.thumb_img);
        viewHolder.product_name.setText(arrayList.get(i).getProdName());
        viewHolder.product_price.setText(arrayList.get(i).getPrice());
        viewHolder.product_count.setText(i+"");

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

}
