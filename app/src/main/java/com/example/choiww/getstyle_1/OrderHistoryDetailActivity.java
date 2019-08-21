package com.example.choiww.getstyle_1;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.choiww.getstyle_1.DataClass.orderDataFromServer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

    /**
    * 목적 : 주문의 상세내용을 보여주기 위한 엑티비티이다.
     *  // 사용자와 관리자가 사용하는 기능을 분리해서 쓰자.
     *      사용자 모드일땐 내용을 볼 수만 있고
     *      관리자 모드일땐 결제상태를 변경할 수 있다.
    *
    * 기능설명 :
    *
    *   관리자모드일 경우 주문상태를 변경할 수 있다.
    *       관리자 모드에서 주문하나를 선택해 주문상세 페이지로 들어간다.
    *       결제상태 옆 dropDown 버튼을 클릭하면 spinner가 나오면서 결제상태를 변경할 수 있다.
    *       dropDown메뉴에서 변경하고자하는 결제상태를 클릭하면 결제상태가 변경되어 spinner에 보여진다.
    *       하단의 저장버튼을 누르면 변경된 결제상태가 서버db에 저장되게 된다.
     *      // 주석만 보고 개발할 수 있게 만들자.
     *      // 주석만 보고 개발은 어떻게 할 수 있는가?
     *      // 주석만 보고 개발을 어떻게 할 수 있는가?
     *      // 주석은
    *
    * */

public class OrderHistoryDetailActivity extends AppCompatActivity {

    String TAG = "find";

    RecyclerView orderHistoryDetail_products_recyclerview;
    DBHelper dbHelper;
    ArrayList<itemInfo> arrayList;//이거
    Gson gson;
    ArrayList<String> spinner_array;
    Spinner spinner;
    ArrayAdapter arrayAdapter;

    String orderNumb;
    String orderDate;
    String buyer;
    String orderState;
    int productsPrice;
    int saleDiscount;
    int billedPoint;
    int deliveryCharge;
    int finalBill;
    String paymentMethod;
    String depositor;
    String accountNumb;
    String payDay;
    String receiverName;
    String receiverAddress;
    String receiverCallNumb;
    String receiverCellphone;
    String products_json;

    String changedOrderState;

    Button orderHistoryDetail_save_btn;
    TextView orderHistroyDetail_orderState_tx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history_detail);

        /*
        * OrderActivity와 비슷한 화면을 만들어줘야한다.
        *
        *   주문했던 상품을 recyclerview로 보여줘야하는데 이는 orderActivity에서 사용하는 Adapter_orderConfirm을 사용하면 될 거 같다.
        *   이 어탭터는 생성자로 context와 arrraylist<item> 형을 받는다.
        *   그러므로 db에서 받아온 상품 정보를 item 객체에 담아 arraylist를 만들어 넣어줘야한다.
        *       dbHelper에 주문번호에 맞춰서 주문정보를 받아오는 메서드를 만들어야한다.
        *       orderHistory 테이블에서 raw를 구분하기 위해선.. 주문번호로 구분하면 되겠다.
        *       근데 지금은 서버에서 주문번호를 고정값으로 주고 있기때문에 구분이 안된다. 그럼 일단 index번호로 해야겠다.
        *       그럼 쿼리문에서 where 에 index번호로 조건을 주면 하나의 row만 가져올 수 있다.
        *
        *
        *   나머지 정보들은 orderActivity와 같이 textview에 하나하나 넣어주면된다.
        * */

        // 주문내역 페이에서 선택한 주문에 대한 정보를 intent로 받아온다.
        final Intent intent = getIntent();
        String str_numb = intent.getStringExtra("numb");
        // intent로 받아온 numb 는 db에서 해당주문을 구분해 가져오기 위해서이다.
        int numb = Integer.parseInt(str_numb);

        // adapter_orderConfirm 에 넣을 Arraylist<itemInfo> 를 만들기 위한 객체
        gson = new Gson();
        final Type type = new TypeToken<ArrayList<itemInfo>>(){}.getType();

        final String deliveryNumb = intent.getStringExtra("deliveryNumb");

        orderHistroyDetail_orderState_tx = findViewById(R.id.orderHistroyDetail_orderState_tx);//상용자가 접속했을때 보이는 결제상태
        spinner= findViewById(R.id.orderHistoryDetail_orderState_spinner);//관리자가 접속했을때 보이는 결제상태를 변경할 수 있는 spinner

        //관리자모드에서 주문상세로 접속한건지 구분하는 조건문이다.
        if (intent.getBooleanExtra("isAdmin", false)){ // true시 관리자-주문관리 에서 넘어온 것이다.
            Log.d(TAG, "onCreate: 관리자 모드에서 주문상세페이지로 이동해왔습니다.");
            // (앱db가 아닌) 서버에서 주문정보를 받아와야한다.
            news_ex1 connector = news_ex1.retrofit.create(news_ex1.class);
            Call<ArrayList<orderDataFromServer>> call = connector.getAdminOneOrder(numb);
            call.enqueue(new Callback<ArrayList<orderDataFromServer>>() {
                @Override
                public void onResponse(Call<ArrayList<orderDataFromServer>> call, Response<ArrayList<orderDataFromServer>> response) {

                        Log.d(TAG, "onResponse: 관리자모드에서 getAdminOneOrder 메서드 발동 : "+response.body().get(0).getUserEmail());
                        // @@@@@@@@@@@@@@@ 여기에 json 으로 변환된 데이터가 잘 들어오는것까진 확인했다
                        // 이제 이것을 데이터 클래스로 담아서 값을 파싱할지, 그냥 수동으로 파싱할지를 선택해야한다.

                    //서버에서 받아온 주문내역의 정보를 파싱해서 각각의 변수에 넣는다.
                    products_json = response.body().get(0).getProducts_json();
                    arrayList = gson.fromJson(products_json,type);

                    orderNumb = response.body().get(0).getOrderNumb();
                    Log.e(TAG, "onResponse: response받아온 orderNumb : "+orderNumb );
                    orderDate = response.body().get(0).getOrderDate();
                    buyer = response.body().get(0).getBuyer();
                    orderState = response.body().get(0).getOrderState();
                    productsPrice = response.body().get(0).getProductsPrice();
                    saleDiscount = response.body().get(0).getSaleDiscount();
                    billedPoint = response.body().get(0).getBilledPoint();
                    deliveryCharge = response.body().get(0).getDeliveryCharge();
                    finalBill = response.body().get(0).getFinalBill();
                    paymentMethod = response.body().get(0).getPaymentMethod();
                    depositor = response.body().get(0).getDepositor();
                    accountNumb = response.body().get(0).getAccountNumb();
                    payDay = response.body().get(0).getPayDay();
                    receiverName = response.body().get(0).getReceiverName();
                    receiverAddress = response.body().get(0).getReceiverAddress();
                    receiverCallNumb = response.body().get(0).getReceiverCallNumb();
                    receiverCellphone = response.body().get(0).getReceiverCellphone();

                    // 리사이클러뷰
                    // 주문한 상품을 보여주기 위한 recyclerview
                    orderHistoryDetail_products_recyclerview = findViewById(R.id.orderHistoryDetail_products_recyclerview);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                    orderHistoryDetail_products_recyclerview.setLayoutManager(linearLayoutManager);
                    Log.d(TAG, "onCreate: arraylist size : "+arrayList.size());
                    Adapter_OrderConfirm adapter = new Adapter_OrderConfirm(getApplicationContext(), arrayList);
                    orderHistoryDetail_products_recyclerview.setAdapter(adapter);

                    //(서버에서 받아온 각각의 주문 데이터를 가진) 변수 값을 각각의 뷰에 넣는다.
                    TextView orderHistoryDetail_orderNumb_tx = findViewById(R.id.orderHistoryDetail_orderNumb_tx);
                    orderHistoryDetail_orderNumb_tx.setText(orderNumb);
                    Log.e(TAG, "onResponse: 화면에 넣어줄때엔 orderNumb : "+orderNumb );
                    TextView orderHistoryDetail_payment_tx = findViewById(R.id.orderHistoryDetail_payment_tx);
                    orderHistoryDetail_payment_tx.setText(paymentMethod);
                    TextView orderConfirm_payAccount_tx = findViewById(R.id.orderConfirm_payAccount_tx);
                    orderConfirm_payAccount_tx.setText(accountNumb);
                    TextView orderHistoryDetail_payDay_tx = findViewById(R.id.orderHistoryDetail_payDay_tx);
                    orderHistoryDetail_payDay_tx.setText(payDay);
                    TextView orderHistoryDetail_productsPrice_tx = findViewById(R.id.orderHistoryDetail_productsPrice_tx);
//        orderHistoryDetail_productsPrice_tx.setText(productsPrice+"");
                    orderHistoryDetail_productsPrice_tx.setText(finalBill+"");// $$위에가 정상이지만. 지금은 일단 원가를 따로 저장해 놓지 않기에 구할수 없음으로 finalBill을 표시해주자.
                    TextView orderHistroyDetail_billedPoint_tx =findViewById(R.id.orderHistroyDetail_billedPoint_tx);
                    orderHistroyDetail_billedPoint_tx.setText(billedPoint+"");
                    TextView orderHistroyDetail_finalBill_tx = findViewById(R.id.orderConfirm_finalBill_tx);
                    orderHistroyDetail_finalBill_tx.setText(finalBill+"");
                    TextView orderHistroy_deliveryNumb_tx = findViewById(R.id.orderHistroy_deliveryNumb_tx);
                    orderHistroy_deliveryNumb_tx.setText(deliveryNumb);
                    TextView orderHistroy_Receiver_name_tx = findViewById(R.id.receiverConfirm_name_tx);
                    orderHistroy_Receiver_name_tx.setText(receiverName);
                    TextView orderHistroy_Receiver_address_tx = findViewById(R.id.receiverConfirm_address_tx);
                    orderHistroy_Receiver_address_tx.setText(receiverAddress);
                    TextView orderHistroy_Receiver_cellphone_tx = findViewById(R.id.receiverConfirm_cellphone_tx);
                    orderHistroy_Receiver_cellphone_tx.setText(receiverCellphone);
                    TextView orderHistory_Receiver_deliveryRequest_tx = findViewById(R.id.orderHistory_Receiver_deliveryRequest_tx);
                    orderHistory_Receiver_deliveryRequest_tx.setText("정상 입력됨");

                    orderHistroyDetail_orderState_tx.setText(orderState);//사용자한테만 보이는 결제상태 view

                    // 관리자가 결제상태를 변경할 수 있게 해주는 코드 (spinner 활용)
                    spinner_array = new ArrayList();
                    spinner_array.add("미결제");
                    spinner_array.add("결제완료");
                    spinner_array.add("결제취소");
                    Log.d(TAG, "onCreate: spinner_array 에는 : "+spinner_array.toString());
                    arrayAdapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, spinner_array);

                    spinner.setAdapter(arrayAdapter);
                    // 스피너 초기값 설정
                    if (orderState.equals("결제완료")){
                        spinner.setSelection(1);
                    }else if(orderState.equals("미결제")){
                        spinner.setSelection(0);
                    }else if(orderState.equals("결제취소")){
                        spinner.setSelection(2);
                    }else {
                        spinner.setSelection(0);
                    }
                    // 스피너 클릭시 이벤트 코드
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                            changedOrderState = spinner_array.get(i);
                            Log.d(TAG, "onItemSelected: 클릭이 됨 변경된 주문상태 : "+changedOrderState);
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    if (intent.getBooleanExtra("isAdmin", false)){//관리자 모드일때
                        orderHistoryDetail_save_btn.setVisibility(View.VISIBLE);
                        orderHistroyDetail_orderState_tx.setVisibility(View.INVISIBLE);
//                        orderHistroyDetail_orderState_tx.setVisibility(View.VISIBLE);
                        spinner.setVisibility(View.VISIBLE);

                    }else {//관리자 모드 아닐때
                        orderHistoryDetail_save_btn.setVisibility(View.INVISIBLE);
                        orderHistroyDetail_orderState_tx.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<orderDataFromServer>> call, Throwable t) {

                }
            });

        }else {

            if (dbHelper == null){
                dbHelper = new DBHelper(OrderHistoryDetailActivity.this, "basket", null, DBHelper.DB_VERSION);
                Log.d(DBHelper.TAG, "onCreate: 초기화 됨");
            }
            Cursor cursor = dbHelper.getOneOrders(numb);
//            Log.d(TAG, "onCreate: "+cursor.toString());
//            Log.d(TAG, "onCreate: "+cursor.moveToFirst());

            if (cursor.moveToFirst()){
                String cursor_zero = cursor.getString(0);
//                Log.d(TAG, "onCreate: cursor_zero : "+cursor_zero);
                products_json = cursor.getString(0);
                arrayList = gson.fromJson(products_json, type);
//                Log.d(TAG, "onCreate: cursor 의 if 문 안으로 들어오지 않는것임?");
            }

            cursor.moveToFirst();
            orderNumb = cursor.getString(1);//
            orderDate = cursor.getString(2);
            buyer = cursor.getString(3);
            orderState = cursor.getString(4);
            productsPrice = cursor.getInt(5);
            saleDiscount = cursor.getInt(6);
            billedPoint = cursor.getInt(7);
            deliveryCharge = cursor.getInt(8);
            finalBill = cursor.getInt(9);
            paymentMethod = cursor.getString(10);
            depositor = cursor.getString(11);
            accountNumb = cursor.getString(12);
            payDay = cursor.getString(13);
            receiverName = cursor.getString(14);
            receiverAddress = cursor.getString(15);
            receiverCallNumb = cursor.getString(16);
            receiverCellphone = cursor.getString(17);

            //사용자가 주문상세 페이지에 접속했을때 주문한 상품목록을 보여주기위한 리사이클러뷰
            orderHistoryDetail_products_recyclerview = findViewById(R.id.orderHistoryDetail_products_recyclerview);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
            orderHistoryDetail_products_recyclerview.setLayoutManager(linearLayoutManager);
            Log.d(TAG, "onCreate: arraylist size : "+arrayList.size());
            Adapter_OrderConfirm adapter = new Adapter_OrderConfirm(this, arrayList);
            orderHistoryDetail_products_recyclerview.setAdapter(adapter);

            orderHistroyDetail_orderState_tx.setText(orderState);
        }

//        // 리사이클러뷰
//        orderHistoryDetail_products_recyclerview = findViewById(R.id.orderHistoryDetail_products_recyclerview);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
//        orderHistoryDetail_products_recyclerview.setLayoutManager(linearLayoutManager);
//        Log.d(TAG, "onCreate: arraylist size : "+arrayList.size());
//        Adapter_OrderConfirm adapter = new Adapter_OrderConfirm(this, arrayList);
//        orderHistoryDetail_products_recyclerview.setAdapter(adapter);

//        Log.d(TAG, "onCreate:  이것도 동작을 안하는 것인가?");
        //기본 설정은 사용자용으로 view를 보이게 세팅한다.
        orderHistroyDetail_orderState_tx.setVisibility(View.VISIBLE);
        spinner.setVisibility(View.GONE);

        TextView orderHistoryDetail_orderNumb_tx = findViewById(R.id.orderHistoryDetail_orderNumb_tx);
        orderHistoryDetail_orderNumb_tx.setText(orderNumb);
        Log.e(TAG, "onResponse: 화면에 넣어줄때엔 orderNumb : "+orderNumb );
        TextView orderHistoryDetail_payment_tx = findViewById(R.id.orderHistoryDetail_payment_tx);
        orderHistoryDetail_payment_tx.setText(paymentMethod);
        TextView orderConfirm_payAccount_tx = findViewById(R.id.orderConfirm_payAccount_tx);
        orderConfirm_payAccount_tx.setText(accountNumb);
        TextView orderHistoryDetail_payDay_tx = findViewById(R.id.orderHistoryDetail_payDay_tx);
        orderHistoryDetail_payDay_tx.setText(payDay);
        TextView orderHistoryDetail_productsPrice_tx = findViewById(R.id.orderHistoryDetail_productsPrice_tx);
//        orderHistoryDetail_productsPrice_tx.setText(productsPrice+"");
          orderHistoryDetail_productsPrice_tx.setText(finalBill+"");// $$위에가 정상이지만. 지금은 일단 원가를 따로 저장해 놓지 않기에 구할수 없음으로 finalBill을 표시해주자.
        TextView orderHistroyDetail_billedPoint_tx =findViewById(R.id.orderHistroyDetail_billedPoint_tx);
        orderHistroyDetail_billedPoint_tx.setText(billedPoint+"");
        TextView orderHistroyDetail_finalBill_tx = findViewById(R.id.orderConfirm_finalBill_tx);
        orderHistroyDetail_finalBill_tx.setText(finalBill+"");
        TextView orderHistroy_deliveryNumb_tx = findViewById(R.id.orderHistroy_deliveryNumb_tx);
        orderHistroy_deliveryNumb_tx.setText(deliveryNumb);
        TextView orderHistroy_Receiver_name_tx = findViewById(R.id.receiverConfirm_name_tx);
        orderHistroy_Receiver_name_tx.setText(receiverName);
        TextView orderHistroy_Receiver_address_tx = findViewById(R.id.receiverConfirm_address_tx);
        orderHistroy_Receiver_address_tx.setText(receiverAddress);
        TextView orderHistroy_Receiver_cellphone_tx = findViewById(R.id.receiverConfirm_cellphone_tx);
        orderHistroy_Receiver_cellphone_tx.setText(receiverCellphone);
        TextView orderHistory_Receiver_deliveryRequest_tx = findViewById(R.id.orderHistory_Receiver_deliveryRequest_tx);
        orderHistory_Receiver_deliveryRequest_tx.setText("정상 입력됨");

        // 관리자가 결제상태를 변경할 수 있게 해주는 코드 (spinner 활용)
       spinner_array = new ArrayList();
        spinner_array.add("미결제");
        spinner_array.add("결제완료");
        spinner_array.add("결제취소");
        Log.d(TAG, "onCreate: spinner_array 에는 : "+spinner_array.toString());
        arrayAdapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, spinner_array);
//        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            spinner= findViewById(R.id.orderHistoryDetail_orderState_spinner);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                changedOrderState = spinner_array.get(i);
                Log.d(TAG, "onItemSelected: 클릭이 됨");

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // 저장버튼으로 결제상태를 변경하고 저장버튼을 누르면 서버에 저장된 결제상태가 변경된다.
        orderHistoryDetail_save_btn = findViewById(R.id.orderHistoryDetail_save_btn);
        if (intent.getBooleanExtra("isAdmin", false)){//관리자 모드일때
            orderHistoryDetail_save_btn.setVisibility(View.VISIBLE);
            orderHistroyDetail_finalBill_tx.setVisibility(View.VISIBLE);

        }else {//관리자 모드 아닐때
            orderHistoryDetail_save_btn.setVisibility(View.INVISIBLE);
            orderHistroyDetail_finalBill_tx.setVisibility(View.INVISIBLE);
        }
        orderHistoryDetail_save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 변경된 결제상태를 내 서버db에 업데이트 하는 메서드
                // 서버에 변경된 결제상태값과 orderNumb를 보내서 하나의 주문에만 update하게 해야한다.
                news_ex1 connector = news_ex1.retrofit.create(news_ex1.class);
                Call<ResponseBody> call = connector.updateOrderInfo(changedOrderState, orderNumb);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Log.d(TAG, "onResponse: 성공 : code "+response.code());
                        Toast.makeText(getApplicationContext(), "결제상태를 변경하였습니다.", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e(TAG, "onFailure: 실패 ",t );
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
