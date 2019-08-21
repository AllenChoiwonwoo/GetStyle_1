package com.example.choiww.getstyle_1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Response;

import static com.example.choiww.getstyle_1.DBHelper.TAG;
    /*
    * 목적 : 장바구니를 recyclerview로 보여주기위한 어텝터 클레스
    *
    *
    * */
public class BasketRecyclerviewAdapter extends RecyclerView.Adapter<BasketRecyclerviewAdapter.itemViewHolder>{
    ArrayList<itemInfo> arrayList;
    Context mcontext;
    DBHelper dbHelper;
    BasketRecyclerviewAdapter adapter;
    Response<List<checkedItemData>> checkedResponse;
    ArrayList checkedIndex = new ArrayList();
    ArrayList checkedMallname = new ArrayList();
    ArrayList checkedProdNumb= new ArrayList();
    ArrayList checkBoxList = new ArrayList();
    HashMap<String, String> checkMap = new HashMap();
    String isCheckedProdNumb;
    WebView hiddenWebview;
    String member_form_numb;
    boolean pageDone = false;
    boolean isGotItem = false;
    boolean isGoDetail = false;

//        public BasketRecyclerviewAdapter(Context applicationContext, ArrayList<itemInfo> items, Response checkedResponse) {
//        }


        public class itemViewHolder extends RecyclerView.ViewHolder{
        CheckBox selectCheckBox_forBasket;
        TextView mall_name;
        ImageView thumb_img;
        TextView product_name;
        TextView product_price;
        TextView product_count;
        TextView delete_forBasket;
        TextView order_forBasket;
        TextView likes_forBasket;
        TextView soldOut;
        TextView sendItemToMallbasket;


        public itemViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mall_name = itemView.findViewById(R.id.mallName_forBasket);
            this.thumb_img = itemView.findViewById(R.id.img_forBasket);
            this.product_name = itemView.findViewById(R.id.prodName_forBasket);
            this.product_price = itemView.findViewById(R.id.price_forBasket);
            this.product_count = itemView.findViewById(R.id.count_forBasket);
            this.delete_forBasket = itemView.findViewById(R.id.delete_forBasket);
            this.order_forBasket = itemView.findViewById(R.id.order_forBasket);
            this.likes_forBasket = itemView.findViewById(R.id.likes_forBasket);
            this.selectCheckBox_forBasket = itemView.findViewById(R.id.selectCheckBox__forBasket);
            this.soldOut = itemView.findViewById(R.id.text_soldOut);
            this.sendItemToMallbasket = itemView.findViewById(R.id.sendItem_text);

//            hiddenWebview = itemView.findViewById(R.id.hiddenWebview);
        }
    }
    public BasketRecyclerviewAdapter(Context context, ArrayList list, Response response, WebView hiddenWebview){
        this.arrayList = list;
        this.mcontext = context;
        this.adapter = this;
        this.checkedResponse = response;
        this.hiddenWebview = hiddenWebview;
        //db를 제어가하기 위한 dbhelper 선언시 초기화
        //어텝터 선언시 db를 사용할 수 있도록 준비시킨다.
        if (dbHelper == null){
            dbHelper = new DBHelper(context, "basket", null, DBHelper.DB_VERSION);
            Log.d(TAG, "onCreate: 초기화 됨");
        }
    }
//    public BasketRecyclerviewAdapter(Context context, ArrayList list){
//            this.arrayList = list;
//            this.mcontext = context;
//        if (dbHelper == null){
//            dbHelper = new DBHelper(context, "basket", null, DBHelper.DB_VERSION);
//            Log.d(TAG, "onCreate: 초기화 됨");
//        }
//    }



    @NonNull
    @Override
    public itemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.basketviewholder,viewGroup, false);
        itemViewHolder viewHolder = new itemViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final itemViewHolder viewHolder, final int i) {
        viewHolder.mall_name.setText(arrayList.get(i).getMallName());
        Picasso.with(mcontext).load(arrayList.get(i).getImg_src()).into(viewHolder.thumb_img);
        viewHolder.product_name.setText(arrayList.get(i).getProdName());
        viewHolder.product_price.setText(arrayList.get(i).getPrice());
        viewHolder.product_count.setText(i+"");
        Log.d(TAG, "onBindViewHolder: response = "+checkedResponse);

        try {
//            String got0 = checkedResponse.body().get(0).getProdNumb();
//            String got1 = checkedResponse.body().get(1).getProdNumb();
//            String got2 = checkedResponse.body().get(2).getProdNumb();
//            String got3 = checkedResponse.body().get(3).getProdNumb();
//            Log.e(TAG, "onResponse: get1.getProdNumb() : "+got0+got1+got2+got3);

            if (checkedResponse.body().get(i).getProdNumb().length() < 2){
                viewHolder.soldOut.setVisibility(View.VISIBLE);
                viewHolder.soldOut.setText("SOLD OUT");
                viewHolder.soldOut.setTextColor(Color.RED);
                Log.d(TAG, "onBindViewHolder: visible = "+i);
                isCheckedProdNumb = checkedResponse.body().get(i).prodNumb;
            }else {
                viewHolder.soldOut.setVisibility(View.INVISIBLE);
                Log.d(TAG, "onBindViewHolder: invisible = "+i);
                isCheckedProdNumb = "팔렸음 ";
            }

        }catch (java.lang.NullPointerException e){
            viewHolder.soldOut.setText("재고 상태 감지중..");
            viewHolder.soldOut.setTextColor(Color.GRAY);
            Log.d(TAG, "onBindViewHolder: nullPointException = "+i);
            isCheckedProdNumb = "아직 감지중";
        }
//        if (checkedResponse.body().get(i).getProdNumb() == null){
//            viewHolder.soldOut.setText("품절 감지중..");
////            viewHolder.soldOut.setTextColor(V);
//        } else if (checkedResponse.body().get(i).getProdNumb().length() > 2){
////            isCheckedProdNumb = checkedResponse.body().get(i).getProdNumb();
//            viewHolder.soldOut.setVisibility(View.VISIBLE);
//        } else {
//            viewHolder.soldOut.setVisibility(View.INVISIBLE);
//        }
        viewHolder.order_forBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, webViewActivity.class);
                //회원id, mallName, img_src, prodName, prodNumb, price, prodHref
                intent.putExtra("href", arrayList.get(i).getProdHref());
                intent.putExtra("img_src", arrayList.get(i).getImg_src());
                intent.putExtra("mallName", arrayList.get(i).getMallName());
                intent.putExtra("prodName", arrayList.get(i).getProdName());
                intent.putExtra("prodNumb", arrayList.get(i).getProdNumb());
                intent.putExtra("price", arrayList.get(i).getPrice());

//                dbHelper.checkProdExist(arrayList.get(i).getProdNumb(), arrayList.get(i).getMallName());

//                intent.putExtra("href",list.body().get(i).getHref());
//                intent.putExtra("img_src",list.body().get(i).getImg_src());
//                intent.putExtra("mallName",list.body().get(i).getMall_name());
//                intent.putExtra("prodNumb", prodNumb);
//                intent.putExtra("price",list.body().get(i).getPrice());
//                intent.putExtra("prodName",list.body().get(i).getProdname());
//                intent.putExtra("fromMain","fromMain");//메인에서 왔다는 지표-

                mcontext.startActivity(intent);

//                itemInfo itemInfo = new itemInfo();
//                itemInfo.setUserId(main.userId);
//                itemInfo.setUserNickname(main.userNickname);
//                itemInfo.setMallName(intentHadItemData.getStringExtra("mallName"));
//                itemInfo.setImg_src(intentHadItemData.getStringExtra("img_src"));
//                itemInfo.setProdName(intentHadItemData.getStringExtra("prodName"));
//                itemInfo.setProdNumb(prodNumb);
//                itemInfo.setPrice(intentHadItemData.getStringExtra("price"));
//                itemInfo.setProdHref(myUrl);
            }
        });
// @@ 쇼핑몰 장바구니의 체크박스를 통해 다중항목 선택 삭제하는 기능을 구현하기 위한 코드
        /* 목적 : a.체크한 아이탬의 체크상태를 유지시켜주기
         *      (*리사이클러뷰의 경우 viewholder를 재활용하기에
         *      viewholder 안에 체크박스를 체크 후 스크롤을 할 시 재활용된 아이템에도 체크가 되어있는 현상이나,
         *      다시 원래 위치의 viewholder로 되돌아 와도 체크가 안되 있을 수 있다.
         *      이문제를 보완하기 위한 코드이다.)
         *      b. 최종적으로 체크한 상품을 한번에 삭제할 수 있도록 list 에 넣기
         *
         * 객체설명 : HashMap checkMap 는 사용자가 체크한 viewholder의 포지션을 저장하는 객체이다.
         *           ArrayList checkedIndex 는 사용자가 체크한 viewholder의 포지션을 저장하는 객체이다(log 용)
                     ArrayList checkedMallname 는 '쇼핑몰이름'을 저장하기 위한 객체
                     ArrayList checkedProdNumb 는 '상뭄번호'를 저장하기 위한 객체
         *
         * 시나리오 :
         *      1. 체크여부 판별
         *      2. 체크여부 반영
         *      3. 체크시 체크여부를 checkMap에 저장, 상품정보 저장
         *      4. 체크해제시 체크여부를 checkMap에서 삭제, 상품정보 삭제
         *
         *      */
        final boolean isCheck; // 체크 여부를 판별하는 변수
        if (checkMap.get(i+"") == null){
            //1-1. 사용자가 체크한적이 있으면
            isCheck = false;
            Log.d(TAG, "onBindViewHolder: xxx");
            Log.d(TAG, "onBindViewHolder: "+isCheckedProdNumb);
        }else {
            //1-2. 사용자가 체크한적이 없으면
            isCheck = true;                                     /// 이 안으로 들어가지 않는다.
            Log.d(TAG, "onBindViewHolder: 0000000000000");
        }
        viewHolder.selectCheckBox_forBasket.setChecked(isCheck);
        //2. 체크박스의 상태를 반영시킨다.
        Log.d(TAG, "onBindViewHolder: "+i);

        //아이템안의 "삭제"버튼을 누를시 해당 상품을 장바구니에서 삭제한다.
        viewHolder.delete_forBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.deleteItems(arrayList.get(i).getProdNumb(),arrayList.get(i).getMallName());
                adapter.notifyItemRemoved(i);

//                adapter.notifyDataSetChanged();
            }
        });
                                                                                    //코드 작성전 주석
                                                                                    // 체크박스는 상품을 여러개를 선택해 삭제하기위해 있다.
                                                                                    // 체크박스를 누르면 체크되면서 해당상품의 데이터를 리스트에 담는다.
                                                                                    // 체크박스를 다시 누르면 데이터를 리스트에서 제거한다.
                                                                                    // 최종적으로 '선택상품 삭제'버튼을 누를 시 리스트에 있는 모든 상품을 삭제한다.

        viewHolder.selectCheckBox_forBasket.setOnClickListener(new View.OnClickListener() {
            //3. 체크 여부를 checkMap에 저장
            @Override
            public void onClick(View v) {

                if (viewHolder.selectCheckBox_forBasket.isChecked()){
                    //3-1. 체크하면
                    //3-2. 상품의 "mallName", "prodName"를 삭제용list에 넣는다. ( 두 col이 유니크 키이기 때문에 )
                    checkedMallname.add(arrayList.get(i).getMallName());
                    checkedProdNumb.add(arrayList.get(i).getProdNumb());
                    //3-2-1. 상폼의 포지션값 저장
                    checkedIndex.add(i+"");
                    //3-3. 해당 포지션의 체크여부를 저장
                    checkMap.put(i+"","notnull");
                    Log.d(TAG, "onClick: "+i+"번째가 체크됨 "+viewHolder.selectCheckBox_forBasket.isChecked());
                    Log.d(TAG, "onClick: 체크시 "+checkMap.get(i+""));
                }else {
                    //4. 체크를 해제하면
                    //4-1. 삭제용 list에서 해당상품을 제거한다.
                    checkedMallname.remove(arrayList.get(i).getMallName());
                    checkedProdNumb.remove(arrayList.get(i).getProdNumb());
                    //4-1-1. 상품의 포지션값 삭제
                    checkedIndex.remove(i+"");          // index와 object를 구별하기위해 int(포지션값)을 string 화 시킨다.
                    //4-2. 해당포지션의 체크여부데이터를 삭제
                    checkMap.remove(i+"");
                    Log.d(TAG, "onClick: "+i+"번째가 체크 해제 됨 "+viewHolder.selectCheckBox_forBasket.isChecked());
                }


                Log.d(TAG, "onClick: checkedProdNumb = "+checkedProdNumb);
            }
        });

        viewHolder.sendItemToMallbasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertItemToMallbasket(arrayList.get(i).getProdHref());
            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void insertItemToMallbasket(String URL){
        // 웹뷰에 로그인 페이지를 띄우고, 장바구니에 상품넣게 해주는 메서드
        final String goturl = URL; //상품 상세페이지 url
        String loginUrl; // 쇼핑몰 로그인 url
        loginUrl = "https://vintagetalk.co.kr/member/login.html";

        //숨겨진 웹뷰
        hiddenWebview.getSettings().setJavaScriptEnabled(true);
        hiddenWebview.addJavascriptInterface(new myJavascriptInterface(), "Android");
        hiddenWebview.loadUrl(loginUrl);
        hiddenWebview.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                Log.d(TAG, "onJsAlert: String url : "+url);
                Log.d(TAG, "onJsAlert: message : "+message);
                Log.d(TAG, "onJsAlert: result"+result);
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                Log.d(TAG, "onJsPrompt: ");
                return super.onJsPrompt(view, url, message, defaultValue, result);
            }
        });
        hiddenWebview.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
//                view.loadUrl("javascript:window.Android.getHtml(document.getElementsByTagName('html')[0].innerHTML);");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                String[] durl = url.split("/");
                Log.d(TAG, "1. onPageFinished: 로드된 페이지 url ");
                
                if (durl[(durl.length-1)].equals("login.html")){
                    Log.d(TAG, "2. 로그인 페이지인걸 확인함 onPageFinished: "+durl[(durl.length-1)]);
                    view.loadUrl("javascript:window.Android.getHtml(document.getElementsByTagName('html')[0].innerHTML);");
                    int i =0;
                    while (!pageDone){

                        i++;
                        if (i%10000000==0){
                            Log.d(TAG, "onPageFinished: i = "+i);
                        }
                    }
                    String id = "dkssudnjsdn";
                    String pw = "cww1003";
                    Log.d(TAG, "onPageFinished: 자바스크립트 인터페이스보다 이코드가 먼저 작동하나?");
//                    AutoLoginAsync autoLoginAsync = new AutoLoginAsync(hiddenWebview, member_form_numb, id, pw);
//                    autoLoginAsync.execute();

//                    hiddenWebview.loadUrl("Javascript:(document.getElementById('member_passwd')).setAttribute('value', '"+pw+"')");
//                       hiddenWebview.loadUrl("Javascript:(document.getElementById('member_id').click())"); 안됌
//                       hiddenWebview.loadUrl("Javascript:(document.getElementById('member_id').click();");  안됌
//                       hiddenWebview.loadUrl("Javascript:(document.getElementById('member_id')).value='dkssudnjsdn'"); //이렇게 하면 화면이 넘어가서 dkssudnjsdn 만 입려되어있음
//                       hiddenWebview.loadUrl("Javascript:(document.getElementById('member_id')).setAttribute('type', 'hidden')");
//                    hiddenWebview.loadUrl("Javascript:(document.getElementById('member_id')).setAttribute('type', 'hidden')");
//                    hiddenWebview.loadUrl("Javascript:(document.getElementById('member_id')).setAttribute('value', 'dkssudnjsdn')");
//                       hiddenWebview.loadUrl("Javascript:document.body.appendChild(input)");

//                        hiddenWebview.loadUrl("Javascript:(document.getElementById(\"member_id\")).focus();");
                    hiddenWebview.loadUrl("Javascript:(document.getElementById('member_passwd')).setAttribute('value', '"+pw+"')");
//        webView.loadUrl("Javascript:(document.getElementById(\"member_id\")).focus();");
                    hiddenWebview.loadUrl("Javascript:(document.getElementById('member_id')).setAttribute('type', 'hidden')");
                    hiddenWebview.loadUrl("Javascript:(document.getElementById('member_id')).setAttribute('value', 'dkssudnjsdn')");

                    hiddenWebview.loadUrl("Javascript:MemberAction.login('"+member_form_numb+"');"); //로그인 버튼
                    Log.d(TAG, "onPostExecute: 로그인 메서드 : "+"Javascript:MemberAction.login('"+member_form_numb+"');");
                    if (isGoDetail){
                        hiddenWebview.loadUrl(goturl);
//                    hiddenWebview.loadUrl("javascript:product_submit(2, '/exec/front/order/basket/', this)");

                    }

                }
                else {
                    //로그인이 이미 되어있는 경우
                    Log.d(TAG, "2. onPageFinished: 바로 장바구니 넣기진행.");
                    if (!isGotItem){
                        hiddenWebview.loadUrl(goturl);
                        isGotItem = true;
                        Log.d(TAG, "2-1. onPageFinished: isGotItem 바뀌어 다시 onFinished 와도 if 문안으로 안들어가 또 상세페이지를 로드하지 않음");
//                        log
                    }else {
                        //로그인 됬고, 상품페이지 로드도 다 됬으면 발동
                        Log.d(TAG, "2-2. onPageFinished: 로그인0,상품페이지로드0, 장바구니에 넣기 메소드만 진행");
                        hiddenWebview.loadUrl("javascript:product_submit(2, '/exec/front/order/basket/', this)");
                        Toast toast = Toast.makeText(mcontext, "장바구니에 담았습니다", Toast.LENGTH_SHORT); toast.show();


                    }



                }

            }
        });


    }
        public class myJavascriptInterface{
            @JavascriptInterface
            public void getHtml(String html){
                Log.d(TAG, "3. getHtml: 자바스크립트인터페이스 작동하는거 맞아.?");
//            System.out.println(html);
                Document doc = Jsoup.parse(html);
                Elements links = doc.select("#contents > form");
                if (links.size() > 0){
                    member_form_numb = links.get(0).attr("id");
                    Log.e(TAG, "getHtml: member_form_numb = "+member_form_numb);
                    pageDone = true;
                    Log.d(TAG, "4. getHtml: memberformnumb 를 찾을 수 있음");
                }else {
//                    member_form_numb = links.get(0).attr("id");
//                    Log.e(TAG, "getHtml: member_form_numb = "+member_form_numb);
                    pageDone = true;
                    Log.d(TAG, "4. getHtml: memberformnumb 를 찾을 수 없음 => 아마 로그인 이미함 or 다른페이지임");
                    isGoDetail = true;
                }





            }
        }

        @Override
        public void registerAdapterDataObserver(@NonNull RecyclerView.AdapterDataObserver observer) {
            super.registerAdapterDataObserver(observer);
//            observer.o
        }
    }

