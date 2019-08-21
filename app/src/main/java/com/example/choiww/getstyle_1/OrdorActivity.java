package com.example.choiww.getstyle_1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.choiww.getstyle_1.DataClass.orderData;
//import com.example.choiww.getstyle_1.DataClass.orderDate;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
    * 목적 : 찜 목록에서 선택한 상품을 결제하기 위한 페이지이다.
    *
    * 시나리오 :
    *       1. 찜 목록에서 구매할 상품리스트를 받아온다.(왜: 선택한 상품을 확인시켜주고, 총 결제금액을 계산하여 보여주려먼 상품들의 리스트가 필요하기 때문에)
    *       2. 사용자가 구매할 상품을 확인하고, 주문자/배송지 등을 입력하고, 결제방법을 선택한다.
    *        2. 사용자가 구매할 상품을 확인하고, 구매를 위한 모든 데이터를 입력한다.(주문자,배송지,결제방법, 등등..)
    *       //3. 사용자가 주문자,배송지,결제방법 등을 입력한다.
    *       4. 구매할 상품 목록과 사용자가 입력한 정보를 내 서버로 전송한다.
    *       ( 왜: 서버에서 사용자가 고른 상품들을 쇼핑몰 별로 구분해서 주문을 넣어줘야하기 때문에
    *           + 서버에서 주문내역을 db에 저장해야 하기 때문에 )
    *
    *       // 5. 내 서버에서 사용자가 입력한 정보를 통해 각각의 쇼핑몰에 주문을 넣는다.
    *       6. 모든 쇼핑몰에 주문이 완료되면 사용자에게 주문이 완료되었다고 알려준다.
    *           (계좌이체는 입금계좌를 같이 알려준다. 현재는 계좌이체 프로세스로 진행한다.)
    *       7. 완료된 주문내용을 앱의 db에 저장한다.
    *
    * */

public class OrdorActivity extends AppCompatActivity {
    String TAG = "find";
    JSONArray jArray;
    JSONObject jsonObject;


    RadioButton.OnClickListener RBOnclickListener = new RadioButton.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordor);

        final TextView buyer_name_tx = findViewById(R.id.buyer_name_tx);
        buyer_name_tx.setText(main.userName);
        final TextView buyer_postNumb_tx = findViewById(R.id.buyer_postNumb_tx);
        final TextView buyer_address1_tx = findViewById(R.id.buyer_address1_tx);
        final TextView buyer_address2_tx = findViewById(R.id.buyer_address2_tx);
        final TextView buyer_callNumb_tx = findViewById(R.id.buyer_callNumb_tx);
        final TextView buyer_cellphoneNumb_tx = findViewById(R.id.buyer_cellphoneNumb_tx);
        final TextView buyer_email_tx = findViewById(R.id.buyer_email_tx);
        buyer_email_tx.setText(main.userEmail);
        final TextView receiver_name_tx = findViewById(R.id.receiver_name_tx);
        receiver_name_tx.setText(main.userName);
        final TextView receiver_postNumb_tx = findViewById(R.id.receiver_postNumb_tx);
        final TextView receiver_address1_tx = findViewById(R.id.receiver_address1_tx);
        final TextView receiver_address2_tx = findViewById(R.id.receiver_address2_tx);
        final TextView receiver_callNumb_tx = findViewById(R.id.receiver_callNumb_tx);
        final TextView receiver_cellphone_tx = findViewById(R.id.receiver_cellphone_tx);
        final TextView receiver_message_tx = findViewById(R.id.receiver_message_tx);
        final TextView billingInfo_coast_tx = findViewById(R.id.billingInfo_coast_tx);
        final RadioButton sendCash_rb =findViewById(R.id.sendCash_radioButton);
        final TextView sendCash_accountNumb_tx = findViewById(R.id.sendCash_accountNumb_tx);

        sendCash_rb.setOnClickListener(RBOnclickListener);
//        sendCash_rb.isChecked();
        final RadioButton card_rb = findViewById(R.id.card_radioButton);
        card_rb.setOnClickListener(RBOnclickListener);
        final RadioButton mobile_rb = findViewById(R.id.mobile_radioButton);
        Button sendPayment_btn = findViewById(R.id.sendPayment_btn);


//        Intent intent = getIntent();
//        Bundle arrayList = intent.getBundleExtra("selectedItems");
//        arrayList.get
//        Log.d(TAG, "onCreate: "+arrayList);

//        ArrayList selectedItems = BasketActivity.selectedItems;
        ArrayList selectedItemsIndex_list = BasketActivity.adapter.checkedIndex;
        ArrayList<itemInfo> items = BasketActivity.adapter.arrayList;
//        System.out.print(selectedItems);
        Log.d(TAG, "onCreate: "+selectedItemsIndex_list.toString());

        final ArrayList<itemInfo> selectedItemInfo_list = new ArrayList(); // 여기에 선택한 상품의 데이터 클래스가 담긴다.
        int totalCoast = 0;

        for (int i=0; i<selectedItemsIndex_list.size();i++){
            int index = Integer.parseInt(selectedItemsIndex_list.get(i).toString());
            selectedItemInfo_list.add(items.get(index));
            selectedItemInfo_list.get(i).getMallName();

            // 각각의 상품에서 가격을 추출해서 총 지불가를 구해주기 위한 코드
            String str_coast = items.get(index).getPrice();
//            price 값은 "30,000원 (80%할인)" 이런 식으로 적혀있기에 가격만 추출해서 숫자로 바꿔야한다.
            String[] array = str_coast.split("원");
            Log.d(TAG, "onCreate: "+array[0]);
//            Log.d(TAG, "onCreate: "+array[1]);
            String str_coast2 = array[0].replace(",","");
            totalCoast = totalCoast+Integer.parseInt(str_coast2);

//            totalCoast = totalCoast + Integer.parseInt(items.get(index).getPrice());

        }
        Log.d(TAG, "onCreate: 결제금액 : "+totalCoast);
        billingInfo_coast_tx.setText((totalCoast+""));



        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        RecyclerView orderConfirm_recyclerView = findViewById(R.id.orderConfirm_recyclerView);
        orderConfirm_recyclerView.setLayoutManager(linearLayoutManager);
        Adapter_OrderConfirm adapter = new Adapter_OrderConfirm(getApplicationContext(), selectedItemInfo_list);
        orderConfirm_recyclerView.setAdapter(adapter);

        final String buyer_name = buyer_name_tx.getText().toString();

        //결제하기 버튼을 눌렀을 시
        final int finalTotalCoast = totalCoast;
        sendPayment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: buyer_name = "+buyer_name);
                final orderData orderData = new orderData();
                orderData.setBuyer_name(buyer_name_tx.getText().toString());
                orderData.setBuyer_postNumb(buyer_postNumb_tx.getText().toString());
                orderData.setBuyer_address1(buyer_address1_tx.getText().toString());
                orderData.setBuyer_address2(buyer_address2_tx.getText().toString());
                orderData.setBuyer_callNumb(buyer_callNumb_tx.getText().toString());
                orderData.setBuyer_cellphoneNumb(buyer_cellphoneNumb_tx.getText().toString());
                orderData.setBuyer_email(buyer_email_tx.getText().toString());
                orderData.setReceiver_name(receiver_name_tx.getText().toString());
                orderData.setReceiver_postNumb(receiver_postNumb_tx.getText().toString());
                orderData.setReceiver_address1(receiver_address1_tx.getText().toString());
                orderData.setReceiver_address2(receiver_address2_tx.getText().toString());
                orderData.setReceiver_callNumb(receiver_callNumb_tx.getText().toString());
                orderData.setReceiver_cellphone(receiver_cellphone_tx.getText().toString());
                orderData.setReceiver_message(receiver_message_tx.getText().toString());
                orderData.setSendCash(sendCash_accountNumb_tx.getText().toString()); //$$계좌도 현재는 하드코딩되어있다 .추후에 선택할 수 있게 해주자
                orderData.setSendPayment("무통장 입금"); // $$이부분은 원래 라디오 버튼 체크여부에 따라 다른값이 들어가게 해야한다.
                orderData.setCard(card_rb.isChecked());
                orderData.setMobile(mobile_rb.isChecked());
                orderData.setCoast(finalTotalCoast);

                orderData.setSelectedItemInfo_list(selectedItemInfo_list);
                Log.d(TAG, "onClick: 선택한 상품들 : "+selectedItemInfo_list.size()+"개 ;  "+selectedItemInfo_list);
//                json

                try {
                jArray = new JSONArray();
                jsonObject = new JSONObject();
                for (int i=0;i<selectedItemInfo_list.size();i++){
                    JSONObject sObject = new JSONObject();
                    sObject.put("mallName", selectedItemInfo_list.get(i).getMallName());
                    sObject.put("img_src", selectedItemInfo_list.get(i).getImg_src());
                    sObject.put("prodName",selectedItemInfo_list.get(i).getProdName());
                    sObject.put("prodNumb",selectedItemInfo_list.get(i).getProdNumb());
                    sObject.put("price",selectedItemInfo_list.get(i).getPrice());
                    sObject.put("prodHref",selectedItemInfo_list.get(i).getProdHref());
                    jArray.put(sObject);


//                    selectedItemInfo_list.get(i).getMallName();
                }
                    jsonObject.put("selectedItems",jArray);
//                    JSONObject jsonObject1 = new JSONObject();
//                    jsonObject1.put(n)
//                    jArray.put(new JSONObject().put("buyer_name",buyer_name_tx.getText().toString()));
                    jsonObject.put("buyer_name",buyer_name_tx.getText().toString());
                    // 이 jsonObject에 모든 주문정보를 담아 서버에 보낸다.


                    Log.d(TAG, "onClick: ");
                    Log.d(TAG, "onClick: JSON Test : "+jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // 서버에 주문정보를 보내고 주문일자, 주문번호를 받아온다.
                news_ex1 connector = news_ex1.retrofit.create(news_ex1.class);
                Call<ResponseBody> call = connector.sendOrder(jsonObject.toString());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Log.d(TAG, "onResponse: response : "+response.code());

                        try {
//                            response.toString()

                            String response_info = response.body().string();
                            Log.d(TAG, "onResponse: 객체에 담음 : "+response_info);
                            String[] response_infoArray = response_info.split(",");


//                            Log.d(TAG, "onResponse: "+response.body().string());

                            Intent intent = new Intent(getApplicationContext(),OrderCompleteActivity.class);
                            Gson gson = new Gson();
                            String str_orderDate = gson.toJson(orderData);
                            intent.putExtra("orderProdList", jArray.toString());
//                            intent.putStringArrayListExtra("orderProdList",selectedItemInfo_list);
                            //현재 서버에서는 주문날짜, 주문번호 를 준다. 이 데이터를 담아 주문확인 화면으로 보낸다.
                            // 추가적으로 서버에서 줘야할 건, ..
                            intent.putExtra("orderData",str_orderDate);
                            intent.putExtra("orderNumb",response_infoArray[0]);
                            intent.putExtra("orderDate",response_infoArray[1]);

                            startActivity(intent);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e(TAG, "onFailure: ",t );
                    }
                });

            }
        });


    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
