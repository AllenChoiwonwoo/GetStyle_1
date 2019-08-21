package com.example.choiww.getstyle_1;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
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

/*
    * 목적 : 주문내역을 보여주기 위한 페이지이다.
    *       주문내역을 간략하게 보여주고 "상세정보보기"를 누르면 상세주문정보를 볼 수 있는 페이지로 넘어가게 한다.
    *
    * 시나리오 :
    *       sqlite - orderhistory 테이블에 저장된 주문정보를 가져온다.
    *           "주문번호, 주문일, 주문상태, 운송장번호" 만 가져와 간략하게 보여준다.
    *               리사이클러뷰를 생성해서 가져온 주문정보를 넣어서 보여준다.
    *
    *       아이탬의 우측의 버튼을 누르면 주문상세페이지로 넘어간다.
    *
    * */
public class OrderHistroyActivity extends AppCompatActivity {

    DBHelper dbHelper;
    RecyclerView orderHistory_recyclerView;

    String TAG = "find";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_histroy);


        if (dbHelper == null){
            dbHelper = new DBHelper(OrderHistroyActivity.this, "basket", null, DBHelper.DB_VERSION);
        Log.d(DBHelper.TAG, "onCreate: 초기화 됨");
    }

//        ArrayList<HashMap> allOrderHistory = dbHelper.getAllOrderHistory();
//        Log.d(TAG, "onCreate: finalBill : "+allOrderHistory.get(0).get("finalBill"));
        ArrayList<SimpleOrderInfoData> allOrderHistory = dbHelper.getAllOrderHistory();

        /** 글쓰기 소모임 가기전 여기까지 했고. arraylist에 필요한 4가지 정보가 들어있으니 . 이것을 토대로 recyclerview에 넣으면된다.
         * 어텝터를 만들고 만들어놓은 layout 에 넣으면된다.*/

        orderHistory_recyclerView= findViewById(R.id.orderHistoryRecyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        orderHistory_recyclerView.setLayoutManager(linearLayoutManager);
        Adapter_orderHistory adapter_orderHistory = new Adapter_orderHistory(this, allOrderHistory);
        orderHistory_recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),1));
        orderHistory_recyclerView.setAdapter(adapter_orderHistory);


    }
}
