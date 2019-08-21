package com.example.choiww.getstyle_1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.choiww.getstyle_1.Fragments.Main_AllProducts_Fragment;
import com.example.choiww.getstyle_1.Fragments.Main_MallProducts_Fragment;
import com.example.choiww.getstyle_1.messenger.ChatRoomList;
import com.example.choiww.getstyle_1.messenger.JoiningChatRoomList;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/** 좀더풀어서 **/

    /*목적 : 메인화면으로써 여러 쇼핑몰의 신상품을 하나의 화면에서 보여준다.

     사용 라이브러리 : recyclerview
                     Retrofit

     전체 시나리오 : 로딩화면에서 main으로 넘어오면 서버에서 쇼핑몰들의 상품 데이터를 받아서 보여준다.
                    (데이터는 json 형태로 받아온다.)
                1. 신규상품데이터를 서버에 요청한다( retrofit 사용)
                2. 받아온데이터를 리스트형태로 화면에 보여준다.(recyclerview 사용) = "전체"페이지
                3. '쇼핑몰' 을 클릭하면 쇼핑몰 별로 신상품을 화면에 보여준다.

*/
public class main extends AppCompatActivity {
    //
    // 레트로 핏을 사용하여 크롤링데이터 요청한다. // @크롤링데이터가 어떤 데이터인지 써주기
    // 요청의 답을 textview 에 보인다.
    String TAG = "find";
    Retrofit retrofit;
    Bitmap bitmap;
    RecyclerView newProdRecyclerview;
    LinearLayoutManager linearLayoutManager;
    GridLayoutManager gridLayoutManager;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    Response<List<dataclass_1>> newProdList;
    boolean isGetData = true;
    public int limitRange = 0;
    public static int userId;
    public static String userEmail;
    public static String userName;
    public static String userNickname;
    ViewPager vp;



    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
//                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
//                    mTextMessage.setText(R.string.title_dashboard);
//                    intent = new Intent(main.this, JoiningChatRoomList.class);
                    intent = new Intent(main.this, JoiningChatRoomList.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_notifications:
//                    mTextMessage.setText(R.string.title_notifications);
                    Toast.makeText(main.this, "장바구니 버튼 클릭됨", Toast.LENGTH_SHORT).show();
                    intent = new Intent(main.this,BasketActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_mypage:
                    intent = new Intent(main.this,MyPageActivity.class);
                    startActivity(intent);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //로그인에 성공한 회원으 정보를 받아온다.
        Intent intent = getIntent();
        userId = intent.getIntExtra("userId",0);
        userEmail = intent.getExtras().getString("userEmail", "email 없음");
        userName = intent.getStringExtra("userName");
        userNickname = intent.getStringExtra("userNickname");
//        Log.d(TAG, "onCreate: userId = "+userId);
//        Log.d(TAG, "onCreate: userEmail = "+userEmail);
        Log.d(TAG, "onCreate: putExtra test : "+intent.getStringExtra("test"));


        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        final TextView textView = findViewById(R.id.textView4);
        textView.setMovementMethod(new ScrollingMovementMethod());

        vp = findViewById(R.id.mainViewpager);
        TextView main_AllProducts_tv = findViewById(R.id.main_AllProducts_tv);
        TextView main_mallProducts_tv = findViewById(R.id.main_MallProducts_tv);

        vp.setAdapter(new pagerAdapter(getSupportFragmentManager()));
        vp.setCurrentItem(0);

        main_AllProducts_tv.setOnClickListener(movePageListener);
        main_AllProducts_tv.setTag(0);
        main_mallProducts_tv.setOnClickListener(movePageListener);
        main_mallProducts_tv.setTag(1);

        /** 이 코드는 Viewpager를 사용하기의 최신상품을 보여주는 recyclerview를 구현하는 코드이다.
         *  현재는 이 코드는 Main_AllProducts_Fragment.java 에 포함되어있다.
         *
         * news_ex1 news_ex1 = com.example.choiww.getstyle_1.news_ex1.retrofit.create(com.example.choiww.getstyle_1.news_ex1.class);
        // 서버와 연결해주는 연결자(news_ex1)를 만들고
        Call<List<dataclass_1>> call = news_ex1.getNewProdLimit(limitRange, limitRange+50);
        Log.d("findcall", "요청범위: "+(limitRange+1) +"부터 "+(limitRange+50));
        limitRange = limitRange + 50;
//        Call<List<dataclass_1>> call = news_ex1.getNewProd();
        // 어떤 요청을 보낼지 선택하여 요청을 만든다.(Call)
        call.enqueue(new Callback<List<dataclass_1>>() {
            // 요청을 실행하고 응답(callback)을 받는다.
            @Override
            public void onResponse(Call<List<dataclass_1>> call, final Response<List<dataclass_1>> response) {
                //요청에 성공하여 응답을 받을시 발동 코드

                //@테스트용 코드라고 설명을 적자
                String a = "";
                for (int i=0; i<response.body().size(); i++){
                    a += response.body().get(i).getMall_name() + "\n";
                    a += response.body().get(i).getImg_src() + "\n";
                    a += response.body().get(i).getProdname() + "\n";
                    a += response.body().get(i).getPrice() + "\n";
                    a += response.body().get(i).getCount() +"\n";
                    a += response.body().get(i).getHref() +"\n";
                }
                textView.setText(a); // 데이터가 잘 왔는지 textView에서 확인한다.
                // 받아온 데이터 전부를 화면(textview)에 표시해 확인한다.

                newProdRecyclerview = findViewById(R.id.newProdRecyclerView);
                // recyclerview선언
//                linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
//                staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
                // 화면에 어떤 정렬로 상품을 보여줄지 선언
                newProdRecyclerview.setLayoutManager(gridLayoutManager);
                // 새상품(@워딩을 통일)을 보여줄 recyclerview에 적용
                newProdList = response;
                final productRecyclerviewAdapter adapter = new productRecyclerviewAdapter(getApplicationContext(), newProdList);
                // 어뎁터 선언
                newProdRecyclerview.setAdapter(adapter);
                //
                *//**test **//*
//                int position = ((LinearLayoutManager)newProdRecyclerview.getLayoutManager()).findLastCompletelyVisibleItemPosition();
//                //linearlayout으로 형변화를 줘서 강제로 메소드를 실행시키며 어떻게 될지 테스트  => position값 확인
//                Log.e("pos", "findLastCompletelyVisibleItemPosition : "+position);


                newProdRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    *//*( 경위 : 한번에 모든 데이터를 불러와 앱에 담아놓고 recyclerview를 만들면 시간도 오래걸리고, 용량도 많이 먹기에)
                       목적 : recyclerview의 스크롤이 최하단까지 오는것을 감지하여 다음에 보여줄 데이터를 로드하게 한다.
                       시나리오 : 1. 스크롤이 최하단에 갔다는 것을 감지
                                       마지막임을 알 수 있는 기준데이터 필요 - 리사이크러뷰 포지션값, 데이터의 (db의)id값, y좌표, 아니며 더보기 버튼을 누르게 한다.
                                 2. 그다음에 보여줄 데이터를 요청하여 받아오기(retrofit 사용)
                                 3. 받아온 데이터를 리싸이클러뷰 어뎁터에 들어가는 list에 추가한다.
                                 4. recyclerview를 리프레쉬(?) 시킨다.
                    *//*
                    @Override
                    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                    }

                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
//                        Log.e("find", "onScrolled: 스크롤한다.");

                        int lastVisibleItemPosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
//                        // 다 보이는 아이텝의 포시션 값
//                        int LastVisibleItemPosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                        // 조금이라도 보이는 포시션 값
//                        Log.e("pos", "findLastCompletelyVisibleItemPosition : "+lastVisibleItemPosition);
//                        Log.d("pos", "findLastVisibleItemPosition : "+LastVisibleItemPosition);
                        // 가능했다. 마지막 아이템의 position 값을 가져올 수 있었다.

                        int itemListSIze = response.body().size()-1;
//                        Log.d("last_pos", "response.body().size() : "+response.body().size());
                        // 데이터의 사이즈
                            // 요청한 데이터를 받아와 리스트에 추가하기 전까지는 itemListSIze <= LastVisibleItemPosition 가충족한다
                            // #그래서 그짧은 시간동안 스크롤로 일어난 이벤트에 isGetData는 계속 true이다.
                        if (itemListSIze > lastVisibleItemPosition){
                            isGetData = false;
                        }else if(itemListSIze <= lastVisibleItemPosition){
                            isGetData = true;
                        }

                        if (itemListSIze == lastVisibleItemPosition){ //스크롤을 마지막 아이템까지 하면 데이터를 더 불러와 보여준다.
                            //스크롤 이벤트를 감지하기 때문에 마지막 아이템 포지션인때 조금이라도 스크롤이 되면 또 코드가 발동되고
                            // 그럼 데이터가 마지막 아이템 포시션 값이 바뀐때까지 조금이라도 움직이면 계속 데이터가 불러와진다.
                            Log.d("find", "isGetData : "+isGetData);
                            if (isGetData){
                                news_ex1 news_ex2 = com.example.choiww.getstyle_1.news_ex1.retrofit.create(com.example.choiww.getstyle_1.news_ex1.class);
                                Call<List<dataclass_1>> call = news_ex2.getNewProdLimit(limitRange+1,50);
                                Log.d("findcall", "요청범위: "+(limitRange+1) +"부터 "+(limitRange+50));
//                                51 ~ 100 까지 요청한다고 재대로 보냈다.
                                limitRange = limitRange+50;
                                call.enqueue(new Callback<List<dataclass_1>>() {
                                    @Override
                                    public void onResponse(Call<List<dataclass_1>> call, Response<List<dataclass_1>> response) {
                                        Log.d("find", "50개를 새로 요청 : "+response.body().size());
                                        (newProdList.body()).addAll(response.body());//body가 list형의 데이터 이기에 addall을 통해서 response.body에 있는 모든 데이터를 newProdLIst.body에 넣는다.
                                        adapter.notifyDataSetChanged(); // 어뎁터에게 데어터가 바뀌었음을 알려준다.
                                        isGetData = false;
                                    }

                                    @Override
                                    public void onFailure(Call<List<dataclass_1>> call, Throwable t) {
                                        Log.e("CallBack error", "Throwable t : "+t);
                                    }
                                });
                            }
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<List<dataclass_1>> call, Throwable t) {
                // 요청 실패시 발동 코드
                Log.e("RetrofitError", "onFailure: "+t.toString());
                textView.setText(t.toString());
                // not
            }
        });*/
        /*Thread mThread = new Thread(){
            public void run(){
                URL url = null;
                try {
                    url = new URL("http:"+"\\/\\/xecond.co.kr\\/web\\/product\\/medium\\/j6536.jpg");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    Log.e("find", "MalformedURLException: "+e );
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("find", "IOException: "+e );
                }
            }
        };
        mThread.start();

        try {
            mThread.join();
            ImageView imageView = findViewById(R.id.imageView);
            imageView.setImageBitmap(bitmap);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

    /*    ImageView imageView2 = findViewById(R.id.imageView2);
//        Picasso.with(this).load().resize(250, 200).into(image1);
        Picasso
                .with(this)
                .load("http:"+"\\/\\/xecond.co.kr\\/web\\/product\\/medium\\/j6536.jpg")
                .fit()
                .into(imageView2);

*/

    }

    View.OnClickListener movePageListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int tag = (int)v.getTag();
            vp.setCurrentItem(tag);
        }
    };

    private class pagerAdapter extends FragmentStatePagerAdapter{


        public pagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i){
                case 0:
                    return new Main_AllProducts_Fragment(getApplicationContext());
                case 1:
                    return new Main_MallProducts_Fragment(getApplicationContext());
                    default:
                        return null;
            }

        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
