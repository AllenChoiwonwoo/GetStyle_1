package com.example.choiww.getstyle_1.AdminMode;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.choiww.getstyle_1.DataClass.MallOrderListDate;
import com.example.choiww.getstyle_1.R;
import com.example.choiww.getstyle_1.news_ex1;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HideAffiliateMallAdminActivity extends AppCompatActivity {

    RecyclerView hideAffiliateMallAdmin_recyclerview;
    LinearLayoutManager linearLayoutManager;
    Adapter_MallAdminEditor adapter;
    ArrayList<MallOrderListDate> arrayList = new ArrayList<>();
    ArrayList<MallOrderListDate> visibleArray;
    Gson gson;
    news_ex1 newsex1;
    int visibleArray_size;
    String TAG = "find";
    news_ex1 retrofitConn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hide_affiliate_mall_admin);
        Intent intent = getIntent();
        String str_mallOrderList = intent.getStringExtra("mallOrderList");
        gson = new Gson();
        Type type = new TypeToken<ArrayList<MallOrderListDate>>(){}.getType();
        visibleArray = gson.fromJson(str_mallOrderList, type);

        hideAffiliateMallAdmin_recyclerview = findViewById(R.id.hideAffiliateMallAdmin_recyclerview);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        hideAffiliateMallAdmin_recyclerview.setLayoutManager(linearLayoutManager);
        arrayList.add(new MallOrderListDate());
        adapter = new Adapter_MallAdminEditor(getApplicationContext(), arrayList);
        adapter.isClickHideMalls =true;
        hideAffiliateMallAdmin_recyclerview.setAdapter(adapter);

        newsex1 = news_ex1.retrofit.create(news_ex1.class);
        final Call<ArrayList<MallOrderListDate>> call = newsex1.getAdminHideMallOrderList();
        call.enqueue(new Callback<ArrayList<MallOrderListDate>>() {
            @Override
            public void onResponse(Call<ArrayList<MallOrderListDate>> call, Response<ArrayList<MallOrderListDate>> response) {
//                Log.d(TAG, "onResponse: "+response.body().get(0).getKor_name());

                arrayList = response.body();
                adapter = new Adapter_MallAdminEditor(getApplicationContext(), arrayList);
                adapter.isClickHideMalls =true;
                hideAffiliateMallAdmin_recyclerview.setAdapter(adapter);
//                adapter.no
            }

            @Override
            public void onFailure(Call<ArrayList<MallOrderListDate>> call, Throwable t) {

            }
        });

        visibleArray_size = visibleArray.size()+1;
        // 저장버튼을 누르면
        Button hideAffiliateMallAdmin_save_btn = findViewById(R.id.hideAffiliateMallAdmin_save_btn);
        hideAffiliateMallAdmin_save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i=0;i<adapter.isShowBtnClick.size();i++){
                    if (adapter.isShowBtnClick.get(i)){//버튼을 누른적이 있다면
                        // 버튼을 누른 것을 옮
                        arrayList.get(i).setMall_order(visibleArray_size+"");// 화면에 보여지는 쇼핑몰 순번의 최대값 +1 한 순번값을 넣어준다.
                        visibleArray.add(arrayList.get(i));// 기존 쇼핑몰순번 array 뒤에다 보이게 하기로한 쇼핑몰의 값을 추가한다..
                        visibleArray_size = visibleArray_size + 1; // 다음번에 넣을때 순번이 겹치면 안되기에 지금 넣은 순번값에 +1 해 놓는다.
                    }else {

                    }
                }
                for (int i=0;visibleArray.size()>i;i++){
                    Log.d(TAG, "onClick: 쇼핑몰 이름 : "+visibleArray.get(i).getKor_name()+", 순번 : "+visibleArray.get(i).getMall_order());
                }


                String str_visibleArray = gson.toJson(visibleArray);
                // 서버에 저장하는 메서드를 넣는다.
                //
                retrofitConn = news_ex1.retrofit.create(news_ex1.class);
                Call<ResponseBody> call1 = retrofitConn.sendAdminMallOrderList(str_visibleArray);
                call1.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Log.d(TAG, "onResponse: response numb : "+response.code());
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.d(TAG, "onResponse: error : "+t);
                    }
                });
                finish();

            }

        });




    }

}
