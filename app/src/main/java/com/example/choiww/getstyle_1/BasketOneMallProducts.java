package com.example.choiww.getstyle_1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BasketOneMallProducts extends AppCompatActivity {

    RecyclerView basketRecyclerview2;
    BasketRecyclerviewAdapter adapter;
    DBHelper dbHelper;
    TextView selectItemDel_bnt;
    Response checkedResponse = null;
    String TAG = "find";
    WebView hiddenWebview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket_one_mall_products);

        Intent intent = getIntent();
        String selectedMallName = intent.getStringExtra("selectedMallName");

        TextView mallName_textview = findViewById(R.id.mallName_textView);
        mallName_textview.setText(selectedMallName);

        basketRecyclerview2 = findViewById(R.id.BasketFilteredByMall_recyclerview);
        hiddenWebview = findViewById(R.id.oneMallHiddenWebview);


        /*db에서 데이터를 불러온다.
         * recyclerview에 넣는다.*/

        //dbHelper 가 선언된적이 없으면 초기화 시킨다.
        if (dbHelper == null){
            dbHelper = new DBHelper(BasketOneMallProducts.this, "basket", null, DBHelper.DB_VERSION);
            Log.d(DBHelper.TAG, "onCreate: 초기화 됨");
        }
//        Log.d(DBHelper.TAG, "onCreate: dbHelper 초기화까진 문제 없음 ");
        final ArrayList<itemInfo> items = dbHelper.getItems(main.userId, selectedMallName);//db 에서 가져온 장바구니 아이탬 데이터들
//        Log.d(DBHelper.TAG, "onCreate: "+items.get(0).getProdName());

        /**
         * 여기에 items를 가지고 mallName, prodNumb를 추출해 서 서버에 보내서 체크된 리스트를 받아와 그걸 다시 recyclerview어댑터에 넣어줄 수 있는
         * 배열로 만들어야하는 코드가 들어와야한ㄷ.ㅏ*/
        ArrayList prodNumbArray=new ArrayList();
        ArrayList mallNameArray = new ArrayList();
        String prodNumbArrayString="";
        String mallNameArrayString="";
        // ","를 구분자로 배열을 string 화 시킨다.
        for (int i=0;i<items.size();i++){
            String prodNumb = items.get(i).getProdNumb();
            prodNumbArray.add(prodNumb);
            prodNumbArrayString = prodNumbArrayString+prodNumb+",";

            String mallName = items.get(i).getMallName();
            mallNameArray.add(mallName);
            mallNameArrayString = mallNameArrayString+mallName+",";


        }
        Log.d(TAG, "onCreate: 배열을 str화 prodNumbArrayString : "+prodNumbArrayString);
        Log.d(TAG, "onCreate: 배열을 str화 mallNameArrayString : "+mallNameArrayString);

        //recyclerview 선언
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getApplicationContext());
        basketRecyclerview2.setLayoutManager(linearLayoutManager2);
        adapter = new BasketRecyclerviewAdapter(getApplicationContext(), items, checkedResponse, hiddenWebview);
        //db에서 가져온 데이터를 넣는다.
        basketRecyclerview2.setAdapter(adapter);

        // 선택 상품 삭제
        //삭제 list('checkedMallName', 'checkedProdName')에 있는 모든상품을 삭제한다.
        /**선택상품 삭제 부분
         * selectItemDel_bnt = findViewById(R.id.selectItemDel_bnt);
        selectItemDel_bnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i=0; adapter.checkedMallname.size()>i; i++) {
                    dbHelper.deleteItems(adapter.checkedProdNumb.get(i).toString(), adapter.checkedMallname.get(i).toString());
//                    basketRecyclerview.getAdapter().notifyDataSetChanged();
//                    basketRecyclerview.not
//                    items.remove(Integer.parseInt(adapter.checkedIndex.get(i).toString()));
                    Log.d(DBHelper.TAG, "onClick: index = "+Integer.parseInt(adapter.checkedIndex.get(i).toString()));
                }
//                adapter.notifyDataSetChanged();
                recreate(); //뷰를 다시 만들어 수정된 db를 화면에 보여준다.
                // ** 현재는 recreate 를 통해 view를 다시 만들지만 , notifyDataSetChanged 를 사용하는 것이 엡퍼포먼스 측면에서 더 권장되어진다고 생각한다. 나중에 보완하자


            }
        });*/

        news_ex1 news_ex1 = com.example.choiww.getstyle_1.news_ex1.retrofit.create(com.example.choiww.getstyle_1.news_ex1.class);
        Call<List<checkedItemData>> call = news_ex1.checkBasketItems(prodNumbArrayString, mallNameArrayString);
        final ArrayList arrayList = new ArrayList();


        call.enqueue(new Callback<List<checkedItemData>>() {
            @Override
            public void onResponse(Call<List<checkedItemData>> call, Response<List<checkedItemData>> response) {
//                for (int i=0;response.body().size()>i;i++){
//
//                }
//                checkedResponse = response;
                int i=0;
                Log.d(TAG, "onResponse: 서버에서 체크한 상품 배열 길이 : "+response.body().size());
                Log.d(TAG, "onResponse: 앱 db 장바구니 배열 길이 : "+items.size());
//                String got0 = response.body().get(0).getProdNumb();
//                String got1 = response.body().get(1).getProdNumb();
//                String got2 = response.body().get(2).getProdNumb();
//                String got3 = response.body().get(3).getProdNumb();
//                for ()
                // 이게 nullpointException 나오는거 보니까.. php 에ㅓㅅ 문제가 있을 수 동 ㅣㅆ겠다.
//                Log.e(TAG, "onResponse: get1.getProdNumb() : "+got0+got1+got2+got3);
//                while (response.body().size()>i){
//                    Log.d(TAG, "onResponse: "+response.body().get(i).getProdName());
//                    i++;
//                }
//                items.clear();
                adapter.checkedResponse = response;
                adapter.notifyDataSetChanged();
                int length = response.body().size()-1;
//                adapter.notifyItemRangeChanged(0,length);
                TextView onResponseTest = findViewById(R.id.onResponseTest);
//                onResponseTest.setText(""+got0+got1+got2+got3);
//                Log.d(TAG, "onResponse: notifiDataSetChanged  가 발동됨");

/**---------------------이걸 여기에 넣으면 무조건 서버에서 체크를 하고 나서야만 장바구니를 볼 수 있는데 .. 그건 아니지 않을까?---  */
                /*//recyclerview 선언
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                basketRecyclerview.setLayoutManager(linearLayoutManager);
                adapter = new BasketRecyclerviewAdapter(getApplicationContext(), items, checkedResponse);
                //db에서 가져온 데이터를 넣는다.
                basketRecyclerview.setAdapter(adapter);

                // 선택 상품 삭제
                //삭제 list('checkedMallName', 'checkedProdName')에 있는 모든상품을 삭제한다.
                selectItemDel_bnt = findViewById(R.id.selectItemDel_bnt);
                selectItemDel_bnt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int i=0; adapter.checkedMallname.size()>i; i++) {
                            dbHelper.deleteItems(adapter.checkedProdNumb.get(i).toString(), adapter.checkedMallname.get(i).toString());
//                    basketRecyclerview.getAdapter().notifyDataSetChanged();
//                    basketRecyclerview.not
//                    items.remove(Integer.parseInt(adapter.checkedIndex.get(i).toString()));
                            Log.d(DBHelper.TAG, "onClick: index = "+Integer.parseInt(adapter.checkedIndex.get(i).toString()));
                        }
//                adapter.notifyDataSetChanged();
                        recreate(); //뷰를 다시 만들어 수정된 db를 화면에 보여준다.
                        // ** 현재는 recreate 를 통해 view를 다시 만들지만 , notifyDataSetChanged 를 사용하는 것이 엡퍼포먼스 측면에서 더 권장되어진다고 생각한다. 나중에 보완하자


                    }
                });*/
/**---------------------이-----------------------------------------------------------------------  */

            }

            @Override
            public void onFailure(Call<List<checkedItemData>> call, Throwable t) {
                checkedResponse = null;
                Log.e(TAG, "onFailure: 요청 실패 : "+t );
            }
        });

//        TextView goBasketFilteredByMall = findViewById(R.id.goBasketFilteredByMall);
//        goBasketFilteredByMall.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), BasketFilteredByMallActivity.class);
//                startActivity(intent);
//            }
//        });

    }
}
