package com.example.choiww.getstyle_1;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Downloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

    //클래스 목적 : 리사이클러뷰를 만들어주기위한 어뎁터
    //              상품을 클릭했을때 해당상품의 상세페이지로 넘어간다. (관련 코드&주석은 하단 '84번 라인'주석 참고)
    //
    //라이브러리 : picasso(이미지 로딩 라이브러리)
    //      기존 url->inputstream->bitmap 에서 url->picasso 로 변경하여 로딩시간 시간단축
    //      @주의@ 캐쉬관련하여 메모리에서 outofmemory가 날 가능성이 있고, 나지 않아도 이미지 캐쉬 관련 개선을 위한 검토는 추후에 필요
    //
    //주의사항 : 생성자로서 Response<List<dateclass_1>> 형의 데이터를 받는다.
    //          왜냐하면 Retrofit을 활용하여 데이터를 받아오는 형태가 위의 형태이기 때문에 변환하지 않고 그대로 썼다.
    //              데이너를 꺼낼때 : list.body().get(몇번째).get값이름() 형태로 받아오면 된다.

    //      ##하단의 productData.class는  arraylist를 쓸때를 대비하여 남겨둠

public class productRecyclerviewAdapter extends RecyclerView.Adapter<productRecyclerviewAdapter.productViewHolder>{
    Response<List<dataclass_1>> list;
    ArrayList arrayList;
    Context mcontext;


    public class productViewHolder extends RecyclerView.ViewHolder {
        TextView mall_name;
        ImageView thumb_img;
        TextView product_name;
        TextView product_price;
        TextView product_count;
        String url = "https://xecond.co.kr/product/list.html?cate_no=49";

        public productViewHolder(@NonNull View view) {
            super(view);
            this.mall_name = view.findViewById(R.id.mall_name);
            this.thumb_img = view.findViewById(R.id.thumb_img);
            this.product_name = view.findViewById(R.id.product_name);
            this.product_price = view.findViewById(R.id.product_price);
            this.product_count = view.findViewById(R.id.product_count);
        }
//        @Override
//        public void onClick(View v) {
//            Intent intent = new Intent(mcontext,webViewActivity.class);
//            mcontext.startActivity(intent);
//        }
       }
    public productRecyclerviewAdapter(Context context, Response list){
        this.list = list;
        this.mcontext = context;

    }
    public productRecyclerviewAdapter(Context context, ArrayList arraylist){
        this.arrayList = arraylist;
        this.mcontext = context;
    }

    @NonNull
    @Override
    public productViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.productview,viewGroup,false);
        productRecyclerviewAdapter.productViewHolder viewHolder = new productRecyclerviewAdapter.productViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull productViewHolder ViewHolder, final int i) {
        ViewHolder.mall_name.setText((list.body().get(i).getMall_name()));
        Picasso.with(mcontext).load(list.body().get(i).getImg_src()).into(ViewHolder.thumb_img);
        ViewHolder.product_name.setText(list.body().get(i).getProdname());
        ViewHolder.product_price.setText(list.body().get(i).getPrice());
        ViewHolder.product_count.setText(list.body().get(i).getCount());

        // ** 뷰(@아이템 으로 워딩 변경)를 클릭했을때 '쇼핑몰의 해당상품 상세페이지'로 넘어가기 위한 코드
        ViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            //*-1. 해당viewgroup을 클릭이벤트를 받을 수 있게 한다.
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext,webViewActivity.class);

                // productNumber를 이미지 주소에서 추출하기 위한 코드
                String[] array = (list.body().get(i).getImg_src()).split("/");
                String imgName = array[(array.length-1)];
                // 이미지 주소에서 재품번호를 추출하기 위한 코드
                String prodNumb = imgName.substring(0,imgName.lastIndexOf("."));
//                                Log.d(DBHelper.TAG, "1 onCreate: "+myImg_src);
//                                Log.d(DBHelper.TAG, "2 onCreate: "+prodNumb);

                intent.putExtra("href",list.body().get(i).getHref());
                intent.putExtra("img_src",list.body().get(i).getImg_src());
                intent.putExtra("mallName",list.body().get(i).getMall_name());
                intent.putExtra("prodNumb", prodNumb);
                intent.putExtra("price",list.body().get(i).getPrice());
                intent.putExtra("prodName",list.body().get(i).getProdname());
                intent.putExtra("fromMain","fromMain");//메인에서 왔다는 지표
                mcontext.startActivity(intent);
//                 @제품상세페이지를 볼 수 있는 페이지로 넘어간다.
                //*-2. 웹뷰를 사용할 수 있는 화면으로 넘어가면서 url의 데이터를 전달한다.
                //      (현재는 서버에서 '상세페이지url'을 주지 않기에 일단 테스트로 '상품이미지url'을 넘긴다.)
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.body().size();
    }
}

class productData{
    String mall_name;
    String thumb_img;
    String product_name;
    String product_price;
    String product_count;

    public productData(String mall_name, String thumb_img, String product_price, String product_name, String product_count) {
        this.mall_name = mall_name;
        this.thumb_img = thumb_img;
        this.product_price = product_price;
        this.product_name = product_name;
        this.product_count = product_count;
    }

    public String getMall_name() {
        return mall_name;
    }

    public String getThumb_img() {
        return thumb_img;
    }

    public String getProduct_price() {
        return product_price;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getProduct_count() {
        return product_count;
    }
}




