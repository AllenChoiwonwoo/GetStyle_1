package com.example.choiww.getstyle_1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.choiww.getstyle_1.DataClass.orderData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

//import com.example.choiww.getstyle_1.DataClass.orderDate;
import java.lang.reflect.Type;
import java.util.ArrayList;

/*
    * 목적 : 주문이 완료되면 주문내역을 사용자에게 확인시켜주기 위한 화면이다.
    *
    * 시나리오 :
    *       (주문 화면에서 결제하기 버튼을 눌러 결제가 완료되면 이 화면으로 넘어온다.)
    *       1. 자신이 입력한 결제정보들이 보여진다.
    *          (+ 계좌이체시 입금자명, 입금할 계좌번호 가 표시된다.)
    *       2. 주문내역 확인버튼을 누르면
    * */

public class OrderCompleteActivity extends AppCompatActivity {

    String TAG = "find";

    TextView orderConfirm_orderNumb_tx;
    TextView orderConfirm_payment_tx;
    TextView orderConfirm_payAccount_tx;
    TextView orderConfirm_payDay_tx;
    TextView orderConfirm_productsPrice_tx;
    TextView orderConfirm_billedPoint_tx;
    TextView orderConfirm_finalBill_tx;
    TextView receiverConfirm_name_tx;
    TextView receiverConfirm_address_tx;
    TextView receiverConfirm_cellphone_tx;
    TextView receiverreceiverConfirm_deliveryRequest_tx;

//    TextView orderHistroy_Receiver_name_tx;

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_complete);

        orderConfirm_orderNumb_tx=findViewById(R.id.orderConfirm_orderNumb_tx);
        //주문번호
        orderConfirm_payment_tx=findViewById(R.id.orderConfirm_payment_tx);
        //결제방식
        orderConfirm_payAccount_tx=findViewById(R.id.orderConfirm_payAccount_tx);
        //입금계좌
        orderConfirm_payDay_tx=findViewById(R.id.orderHistoryDetail_payDay_tx);
        orderConfirm_productsPrice_tx=findViewById(R.id.orderHistoryDetail_productsPrice_tx);
        orderConfirm_billedPoint_tx=findViewById(R.id.orderHistroyDetail_billedPoint_tx);
        orderConfirm_finalBill_tx=findViewById(R.id.orderConfirm_finalBill_tx);
        receiverConfirm_name_tx=findViewById(R.id.receiverConfirm_name_tx);
        receiverConfirm_address_tx=findViewById(R.id.receiverConfirm_address_tx);
        receiverConfirm_cellphone_tx=findViewById(R.id.receiverConfirm_cellphone_tx);
        receiverreceiverConfirm_deliveryRequest_tx=findViewById(R.id.receiverreceiverConfirm_deliveryRequest_tx);
//        orderHistroy_Receiver_name_tx = findViewById(R.id.receiverConfirm_name_tx);

        final Intent intent = getIntent();
        String str_orderData = intent.getStringExtra("orderData");

//        ArrayList orderProdList = intent.getStringArrayListExtra("orderProdList");
//        Log.d(TAG, "onCreate:받은 orderPordList = "+orderProdList.toString());
        String orderProdList = intent.getStringExtra("orderProdList"); // str된 주문상품리스트가 담겨 있다.
        // 이건 화면에 띄워주고 앱에 저장하려고 받아왔다.
        String orderNumb = intent.getStringExtra("orderNumb"); // orderNumb엔 내 서버에서 준 주문번호가 담겨있다.
        // 서버에 주문정보를 보내고 받은 주문번호이다. 아직은 서버에서 주문은 넣어주고 보내는게 아닌 그냥 임의의 번호이다. 앱db에 저장하기 위해 받아왔다.
        String orderDate = intent.getStringExtra("orderDate"); // 주문일자
        // 서버에서 보내준 주문일자이다.앱db에 저장하기 위해 받아왔다.

        Gson gson = new Gson();
        // orderProdList를 다시 array 형태로 바꾸기 위해 사용한다.
        // gson 사용방법 (특히 fromJosn : http://1004lucifer.blogspot.com/2015/04/javagson-gson-java-json.html )
//        Type type = new TypeToken<orderDate>().getType();
//        gson.fromJson(str_orderDate,type);

        Type listType = new TypeToken<ArrayList<itemInfo>>(){}.getType();//json.string 이된 데이터를 다시 arraylist<itemInfo>로 바꾸기 위해
        // gson과 함께 사용한다. string을 바꿀 형태(타입)을 정을할 수 있다.
        ArrayList<ArrayList<itemInfo>> orderProdList_array = gson.fromJson(orderProdList, listType);
        // orderProdList_array 는 주문한 상품이 담겨있다.
        //원래는 이 화면에서도 이 데이터를 어떤 상품이 담겨있는지 모두 보여주려 했지만 .. 하지 않았다.

        orderData orderData = gson.fromJson(str_orderData, com.example.choiww.getstyle_1.DataClass.orderData.class);
        //orderDate 는 주문정보(주문자,배송지 정보 등)가 담겨있따.
        Log.d(TAG, "onCreate: "+orderData.getReceiver_name());
        Log.d(TAG, "onCreate: "+orderNumb);

        orderConfirm_orderNumb_tx.setText(orderNumb);//주문번호
        orderConfirm_payment_tx.setText(orderData.getSendPayment()); //결제수단 (현재 무통장입금)//$$ 여기
        orderConfirm_payAccount_tx.setText(orderData.getSendCash()); //입금계좌 예금주 // $$여기 나중에 수정해야한다.
        orderConfirm_productsPrice_tx.setText(orderData.getCoast()+""); //상품금액합계
        orderConfirm_finalBill_tx.setText(orderData.getCoast()+""); // 총 결제금액
        receiverConfirm_name_tx.setText(orderData.getReceiver_name()); // 받는이 이름

//        orderHistroy_Receiver_name_tx.setText(orderData.getReceiver_name());
        receiverConfirm_address_tx.setText(orderData.getReceiver_address1()+" "+orderData.getReceiver_address2()); //받는 주소
        receiverConfirm_cellphone_tx.setText(orderData.getReceiver_cellphone());// 받는이 폰번호
        receiverreceiverConfirm_deliveryRequest_tx.setText(orderData.getReceiver_message()); //배송메시지 //$$ orderHistoryTable에 col이 없다.

        //dbHelper 가 선언된적이 없으면 초기화 시킨다.
        if (dbHelper == null){
            dbHelper = new DBHelper(OrderCompleteActivity.this, "basket", null, DBHelper.DB_VERSION);
            Log.d(DBHelper.TAG, "onCreate: 초기화 됨");
        }

//        Log.d(TAG, "onCreate: orderProdlist_array 길이 = "+orderProdList_array.size());
//        Log.d(TAG, "onCreate: ");

        /**
         * 코드리뷰 가기전 여기까지 만들었다. 이제 DBHelper의 inserNewOrder를 정의해야한다.
         * 여기에 생성자로 넣어줄값도 정의하고
         *  주문정보는 orderDate에 다 담겨있다.
         *  주문번호는 orderNumb에 담겨있다.
         * */

        dbHelper.InsertNewOrder(orderNumb, orderDate, orderProdList, orderData);
        // 완료된 주문의 정보를 db에 저장하기 위한 코드이다.

        ArrayList selectedItemsIndex_list = BasketActivity.adapter.checkedIndex;
        ArrayList<itemInfo> items = BasketActivity.adapter.arrayList;
//        System.out.print(selectedItems);
        Log.d(TAG, "onCreate: "+selectedItemsIndex_list.toString());

        final ArrayList<itemInfo> selectedItemInfo_list = new ArrayList(); // 여기에 선택한 상품의 데이터 클래스가 담긴다.
//        int totalCoast = 0;

        //구매한 상품을 찜 목록에서 삭제하기 위한 메소드
        for (int i=0; i<selectedItemsIndex_list.size();i++){
            int index = Integer.parseInt(selectedItemsIndex_list.get(i).toString());
            selectedItemInfo_list.add(items.get(index));
            String mallName = items.get(index).getMallName();
            String prodNumb = items.get(index).getProdNumb();
            dbHelper.deleteItems(prodNumb, mallName);

            // 각각의 상품에서 가격을 추출해서 총 지불가를 구해주기 위한 코드
//            String str_coast = items.get(index).getPrice();
//            price 값은 "30,000원 (80%할인)" 이런 식으로 적혀있기에 가격만 추출해서 숫자로 바꿔야한다.
//            String[] array = str_coast.split("원");
//            Log.d(TAG, "onCreate: "+array[0]);
//            Log.d(TAG, "onCreate: "+array[1]);
//            String str_coast2 = array[0].replace(",","");
//            totalCoast = totalCoast+Integer.parseInt(str_coast2);

//            totalCoast = totalCoast + Integer.parseInt(items.get(index).getPrice());

        }



        Button goOrderHistoryActivity_btn = findViewById(R.id.goOrderHistoryActivity_btn);
        goOrderHistoryActivity_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                Intent intent1 = new Intent(getApplicationContext(), OrderHistroyActivity.class);
                startActivity(intent1);
            }
        });


    }
}
