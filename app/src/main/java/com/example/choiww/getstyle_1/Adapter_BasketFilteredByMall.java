package com.example.choiww.getstyle_1;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class Adapter_BasketFilteredByMall extends RecyclerView.Adapter<Adapter_BasketFilteredByMall.itemViewHolder> {

    Context mcontext;
    ArrayList<BasketFilteredByMallActivity.MallCount> arrayList;


    public class itemViewHolder extends RecyclerView.ViewHolder {

        ImageView mallLogo;
        TextView mallName;
        TextView mallCount;
        View item;

        public itemViewHolder(@NonNull View view) {
            super(view);
            mallLogo = view.findViewById(R.id.basketFilterByMall_logo);
            mallName = view.findViewById(R.id.basketFilteredByMall_name);
            mallCount = view.findViewById(R.id.basketFilteredByMall_count);
            item = view.findViewById(R.id.basketFilteredByMall_viewgroup);

        }
    }
    public Adapter_BasketFilteredByMall(Context context, ArrayList arrayList){
        this.mcontext=context;
        this.arrayList=arrayList;

    }


    @NonNull
    @Override
    public Adapter_BasketFilteredByMall.itemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.basketfilteredbymall,viewGroup,false);
        itemViewHolder viewHolder = new itemViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_BasketFilteredByMall.itemViewHolder viewHolder, int i) {
        viewHolder.mallLogo.setImageResource(arrayList.get(i).getDrawable_numb());
        final String gotMallName = arrayList.get(i).getMallName();
        viewHolder.mallName.setText(gotMallName);
        Log.e("find", "onBindViewHolder: getCount = "+arrayList.get(i).getCount());
        viewHolder.mallCount.setText(String.valueOf(arrayList.get(i).getCount()));
        viewHolder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, BasketOneMallProducts.class);
                intent.putExtra("selectedMallName", gotMallName);
                mcontext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
