package com.example.choiww.getstyle_1.Fragments;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.choiww.getstyle_1.R;
import com.example.choiww.getstyle_1.mallUpdateDate;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

    /**
    *
    * */

public class mallUpdateRecyclerviewAdapter extends RecyclerView.Adapter<mallUpdateRecyclerviewAdapter.mallUpdateViewholder> {
    ArrayList arrayList;
    Response<List<mallUpdateDate>> response;
    Context context;

    public class mallUpdateViewholder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;

        public mallUpdateViewholder(@NonNull View view) {
            super(view);
            this.imageView = view.findViewById(R.id.mallLogo_img);
            this.textView = view.findViewById(R.id.mallUpdateInfo_tx);
        }
    }
    public mallUpdateRecyclerviewAdapter(Context context, Response response){
        this.response =response;
        this.context = context;
    }

    @NonNull
    @Override
    public mallUpdateRecyclerviewAdapter.mallUpdateViewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mallupdate_item,viewGroup,false);
        mallUpdateRecyclerviewAdapter.mallUpdateViewholder viewholder = new mallUpdateRecyclerviewAdapter.mallUpdateViewholder(view);

        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull mallUpdateRecyclerviewAdapter.mallUpdateViewholder viewholder, int i) {

        switch (response.body().get(i).getMallNumb()){
            case 1:
                viewholder.imageView.setImageResource(R.drawable.vintagetalklogo);break;
            case 2:
                viewholder.imageView.setImageResource(R.drawable.vintagesisterlogo);break;
            case 3:
                viewholder.imageView.setImageResource(R.drawable.xecond_logo);break;
        }


        String updateinfo = (response.body().get(i).getNewProdCount())+"개 업데이트! "+response.body().get(i).getNewProdDate();
        viewholder.textView.setText(updateinfo);
    }

    @Override
    public int getItemCount() {
        return response.body().size();
    }
}
