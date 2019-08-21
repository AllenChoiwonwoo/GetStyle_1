package com.example.choiww.getstyle_1.AdminMode;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.choiww.getstyle_1.DataClass.SimpleOrderInfoData;
import com.example.choiww.getstyle_1.OrderHistroyActivity;
import com.example.choiww.getstyle_1.R;
import com.example.choiww.getstyle_1.Adapter_orderHistory;
import com.example.choiww.getstyle_1.news_ex1;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**     관리자 모드 - 주문관리 페이지
 *
 *      목적 : 관리자가 주문을 관리 할 수 있는 페이지
 *
 *      기능 : 관리자가 주문을 모두 볼 수 있다.
 *              주문관리 페이지에 들어오게 되면 기본은 전체주문이 보여지게 되어있다.
 *
 *            관리자가 주문을 필터링해 볼 수 있다.
 *              '전체,입금전,결제완료'로 필터링해 볼 수 있다.
 *              전체 : 결제상태가 '전체'인 주문만 볼 수 있다.
 *              // 전체 : 모든 주문을 볼 수 있다.
 *              입금전 : 결제상태가 '입금전'인 주문만 필터링해 볼 수 있다.
 *              결제완료 : 결제상태가 '결제완료'인 주문만 필터링해 볼 수 있다.
 *
 *              결제상태를 변경하고 싶은 주문 우측의 화살표를 누르면 주문상세 페이지로 넘어간다.
 *              // 각 아이탬의 우측의 화살표버튼을 주문상세페이지로 이동한다.
 *              // 화살표 버튼 (결제상태를 변경하고 싶을때)
 *
 *
 *
* */
public class OrderManagingActivity extends AppCompatActivity {

    RecyclerView recyclerView; //
    LinearLayoutManager linearLayoutManager;
    Adapter_orderHistory mAdapter;
    ArrayList arraylist;
    int filterValue;
    Call<ArrayList<SimpleOrderInfoData>> call;
    Callback<ArrayList<SimpleOrderInfoData>> callback;
    View.OnClickListener onClickListener;

    Button orderManaging_filteringAll_btn;
    Button orderManaging_filteringNotPayed_btn;
    Button orderManaging_filteringPayComplite_btn;

    String TAG = "find";

    @Override
    protected void onRestart() {
        super.onRestart();
        recreate();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_managing);


        /*
        * 서버db-all_orders table 에서 [orderNumb, orderDate, orderState, finalBill, numb, 운송장번호 ] 데이터를 받아온다.
        *   받아올때 전체를 받아올지, 미결제, 결제완료를 받아올지를 조건문으로 가져와야한다.
        * 받아온 데이터(arraylist)를 recyclerview에 넣는다.
        * 하나의 아이템을 눌렀을때 상세페이지로 이동시킨다. ( intent를 통해 numb를 보내주어 해당 특정 주문의 정보만 가져올 수 있게 한다.)
        * */
        callback = new Callback<ArrayList<SimpleOrderInfoData>>() {
            @Override
            public void onResponse(Call<ArrayList<SimpleOrderInfoData>> call, Response<ArrayList<SimpleOrderInfoData>> response) {
                mAdapter.arrayList = response.body();
                mAdapter.notifyDataSetChanged();
                Log.d(TAG, "onResponse: 관리자 주문 리스트 보기 - 필터링 완료");
            }

            @Override
            public void onFailure(Call<ArrayList<SimpleOrderInfoData>> call, Throwable t) {
                Log.d(TAG, "onFailure: 관리자 주문 리스트 보기 - 필터링 - 에러 : "+t);
            }
        };



        final news_ex1 connector = news_ex1.retrofit.create(news_ex1.class);

        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.orderManaging_filteringAll_btn://전체버튼을 누르면
                        if (filterValue == 0){
                            return; // 이미 전체를 보고 있는데 또 전체를 누르면 동작하지 않게한다.
                        }
                        Log.d(TAG, "onClick: 전체 버튼 눌림");
                        filterValue = 0;
                        call = connector.getAdminOrderList();
                        call.enqueue(callback);
                        return;

                    case R.id.orderManaging_filteringNotPayed_btn:
                        if (filterValue == 1){
                            return; // 이미 미결제를 보고 있는데 또 미결제를 누르면 동작하지 않게한다.
                        }
                        Log.d(TAG, "onClick: 미결제 버튼 눌림");
                        filterValue = 1;
                        call = connector.getAdminOrderList(1);
                        call.enqueue(callback);
                        return;
                    case R.id.orderManaging_filteringPayComplite_btn:
                        if (filterValue == 2){
                            return; // 이미 결제완료를 보고 있는데 또 결제완료를 누르면 동작하지 않게한다.
                        }
                        Log.d(TAG, "onClick: 결제완료 버튼 눌림");
                        filterValue = 2;
                        call = connector.getAdminOrderList(2);
                        call.enqueue(callback);
                        return;
                }
            }
        };
        orderManaging_filteringAll_btn = findViewById(R.id.orderManaging_filteringAll_btn);
        orderManaging_filteringAll_btn.setOnClickListener(onClickListener);
        orderManaging_filteringNotPayed_btn = findViewById(R.id.orderManaging_filteringNotPayed_btn);
        orderManaging_filteringNotPayed_btn.setOnClickListener(onClickListener);
        orderManaging_filteringPayComplite_btn = findViewById(R.id.orderManaging_filteringPayComplite_btn);
        orderManaging_filteringPayComplite_btn.setOnClickListener(onClickListener);



        // 조건문을 통해서 필터링 조건을 걸었는지를 확인한다.
//        if(){
//
//        }
// 일단은 필터없이 전체를 가져오는거 부터
        call = connector.getAdminOrderList();
        call.enqueue(new Callback<ArrayList<SimpleOrderInfoData>>() {
            @Override
            public void onResponse(Call<ArrayList<SimpleOrderInfoData>> call, Response<ArrayList<SimpleOrderInfoData>> response) {
                Log.d(TAG, "onResponse: 데이터 잘 담겨오는지 확인 :"+response.body().get(0).getDeliveryNumb());
                arraylist = response.body();

                recyclerView = findViewById(R.id.orderManaging_recyclerview);
                linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(linearLayoutManager);
                mAdapter= new Adapter_orderHistory(getApplicationContext(), arraylist, true);// 관리자일시 3번째 인자값으로 true를 넣어줘야 주문정보를 수정할 수 있다.
                recyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<ArrayList<SimpleOrderInfoData>> call, Throwable t) {
                Log.d(TAG, "onFailure: 관리자 주문 리스트 보기 서버서 데이터 가져오기 - 에러 : "+t);
            }
        });






    }

}
