package com.example.choiww.getstyle_1.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.choiww.getstyle_1.R;
import com.example.choiww.getstyle_1.dataclass_1;
import com.example.choiww.getstyle_1.mallUpdateDate;
import com.example.choiww.getstyle_1.news_ex1;

import java.util.List;
import java.util.zip.Inflater;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("ValidFragment")
public class Main_MallProducts_Fragment extends Fragment {

    Context mcontext;
    String mall1;
    String TAG = "find";

    @SuppressLint("ValidFragment")
    public Main_MallProducts_Fragment(Context context){
        this.mcontext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: !!!!!!!!!!!!!!!!!");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            ConstraintLayout layout = (ConstraintLayout)inflater.inflate(R.layout.fragment_main_mallproducts,container,false);
            news_ex1 news_ex1 = com.example.choiww.getstyle_1.news_ex1.retrofit.create(com.example.choiww.getstyle_1.news_ex1.class);
            Call<List<mallUpdateDate>> call = news_ex1.getMallUpdateInfo();
//            final TextView mall1Info_tx = layout.findViewById(R.id.mall1Info_tx);
//            final TextView textView = layout.findViewById(R.id.textView);
//            mall1Info_tx.setText("whatthehell");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mcontext);
        final RecyclerView mallUpdateRecyclerview_frag = layout.findViewById(R.id.mallUpdateRecyclerview_frag);

        mallUpdateRecyclerview_frag.setLayoutManager(linearLayoutManager);


            Log.d(TAG, "onCreateView: #######################");

    //        mallUpdateDate mallUpdateDate = new mallUpdateDate();
//           final String mall1_;

            call.enqueue(new Callback<List<mallUpdateDate>>() {
                @Override
                public void onResponse(Call<List<mallUpdateDate>> call, Response<List<mallUpdateDate>> response) {
                    Log.d(TAG, "onResponse: @@@@@@@@@@@@@@@@");

                    mallUpdateRecyclerviewAdapter adapter = new mallUpdateRecyclerviewAdapter(mcontext, response);
                    mallUpdateRecyclerview_frag.setAdapter(adapter);



                }

                @Override
                public void onFailure(Call<List<mallUpdateDate>> call, Throwable t) {
                    Log.d(TAG, "onFailure: xxxxxxxxxxxxxxxxxxxxxxx");
                    Log.d(TAG, "onFailure: "+t);
                    mall1 = "....";
//                    mall1Info_tx.setText(mall1);

                }
            });

        Log.d(TAG, "onCreateView: setText 전에");



            /*목적 : 쇼핑몰 별로 */


            return layout;
    }
}
