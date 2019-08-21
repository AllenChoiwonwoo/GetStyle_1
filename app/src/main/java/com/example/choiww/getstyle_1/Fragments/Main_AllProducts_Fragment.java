package com.example.choiww.getstyle_1.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.choiww.getstyle_1.R;
import com.example.choiww.getstyle_1.dataclass_1;
import com.example.choiww.getstyle_1.news_ex1;
import com.example.choiww.getstyle_1.productRecyclerviewAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("ValidFragment")
public class Main_AllProducts_Fragment extends Fragment {
    Context mcontext;
    int limitRange = 0;
    boolean isGetData;

    @SuppressLint("ValidFragment")
    public Main_AllProducts_Fragment(Context context){
        this.mcontext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ConstraintLayout layout = (ConstraintLayout) inflater.inflate(R.layout.fragment_main_allproducts, container, false);
        final RecyclerView newProductRecyclerview_frag = layout.findViewById(R.id.newProductRecyclerview_frag);
//        newProductRecyclerview_frag



        news_ex1 news_ex1 = com.example.choiww.getstyle_1.news_ex1.retrofit.create(com.example.choiww.getstyle_1.news_ex1.class);
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



                GridLayoutManager gridLayoutManager = new GridLayoutManager(mcontext, 3);
//                staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
                // 화면에 어떤 정렬로 상품을 보여줄지 선언
                newProductRecyclerview_frag.setLayoutManager(gridLayoutManager);
                // 새상품(@워딩을 통일)을 보여줄 recyclerview에 적용
                final Response<List<dataclass_1>> newProdList = response;
                final productRecyclerviewAdapter adapter = new productRecyclerviewAdapter(mcontext, newProdList);
                // 어뎁터 선언
                newProductRecyclerview_frag.setAdapter(adapter);
                //
                /**test **/
//                int position = ((LinearLayoutManager)newProdRecyclerview.getLayoutManager()).findLastCompletelyVisibleItemPosition();
//                //linearlayout으로 형변화를 줘서 강제로 메소드를 실행시키며 어떻게 될지 테스트  => position값 확인
//                Log.e("pos", "findLastCompletelyVisibleItemPosition : "+position);


                newProductRecyclerview_frag.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    /*( 경위 : 한번에 모든 데이터를 불러와 앱에 담아놓고 recyclerview를 만들면 시간도 오래걸리고, 용량도 많이 먹기에)
                       목적 : recyclerview의 스크롤이 최하단까지 오는것을 감지하여 다음에 보여줄 데이터를 로드하게 한다.
                       시나리오 : 1. 스크롤이 최하단에 갔다는 것을 감지
                                       마지막임을 알 수 있는 기준데이터 필요 - 리사이크러뷰 포지션값, 데이터의 (db의)id값, y좌표, 아니며 더보기 버튼을 누르게 한다.
                                 2. 그다음에 보여줄 데이터를 요청하여 받아오기(retrofit 사용)
                                 3. 받아온 데이터를 리싸이클러뷰 어뎁터에 들어가는 list에 추가한다.
                                 4. recyclerview를 리프레쉬(?) 시킨다.
                    */
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

                // not
            }
        });

        return layout;

    }
}
