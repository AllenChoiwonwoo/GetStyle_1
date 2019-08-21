package com.example.choiww.getstyle_1;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.net.CookieStore;
import java.util.HashMap;
import java.util.Map;

/*
목적 : 상품리스트를 볼 수 있는 화며에서 해당 상품클릭시 "쇼핑몰의 해당상품상세페이지"로 넘어간다.

라이브러리 : webview(웹뷰) - 앱 내에서 웹브라우저처럼 사용할 수 있는 화면을 만들어준다.
            (대안은 아직 찾아보지 못함, ['a' 주석]뒤로가기 버튼으로 웹페이지를 뒤로가게 할 수 있게 설정 가능)
            @A를 줄번호로 변경하기
                > 근데 줄번호는 한줄만 추가해도 바뀌잖어.. 그냥 번호나 기호가 나을거 같음

전체 시나리오 :
    1. 전 화면에서 화면에 띄울 '쇼핑몰 재품 상세페이지'의 url을 받아온다.
    2. 화면(웹뷰)에 띄워준다.
*/


public class webViewActivity extends AppCompatActivity {
    private WebView mWebView;
    TextView textView3;  //테스트용은 테스트용이라고 적기, 어떤 테스트인지 (페이지 URL)
    ImageView insertToBasket_bnt; // 장바구니에 추가 버튼
    ImageView deleteFromBasket_bnt; // 장부구니 삭제 버튼
    private DBHelper dbHelper;
    private Intent intentHadItemData;
    String TAG = "find";
    private String myUrl;
    private String href;
    private String img_src;
    private String mallName;
    private String prodName;
    private String prodNumb;
    private String price;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        intentHadItemData = getIntent();
        textView3 = findViewById(R.id.textView3);
        textView3.setVisibility(View.INVISIBLE);// test 용 textview 숨기기
        href = intentHadItemData.getStringExtra("href");
        Log.d(TAG, "onCreate: "+href);
        img_src = intentHadItemData.getStringExtra("img_src");
        // 메인(신상리스트)화면에서 넘겨 받은 상품상세페이지 url을 가져온다.
        Log.d(TAG, "onCreate: "+img_src);
        mallName = intentHadItemData.getStringExtra("mallName");
        Log.d(TAG, "onCreate: "+mallName);
        prodName = intentHadItemData.getStringExtra("prodName");
        Log.d(TAG, "onCreate: "+prodName);
        prodNumb = intentHadItemData.getStringExtra("prodNumb");
        Log.d(TAG, "onCreate: "+prodNumb);
        price = intentHadItemData.getStringExtra("price");
        Log.d(TAG, "onCreate: "+price);

        textView3.setText(href);


        //웹뷰 셋팅
        mWebView = findViewById(R.id.webVeiw);
        mWebView.getSettings().setJavaScriptEnabled(true);

//        mWebView.loadUrl("http://m.xecond.co.kr/product/detail.html?product_no=47226&cate_no=1&display_group=3");
        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                // string message 를 통해서 alert내용을 받아올 수 있고
                // alert 내용에 상품 솔드아웃 정보가 있다면 message에서 솔드아웃된 상품을 이름을 추출할 수 있다.
                Log.d(DBHelper.TAG, "onJsAlert: JsResult result = "+result);
                new android.app.AlertDialog.Builder(webViewActivity.this)
                        .setTitle("Alert")
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok,
                                new android.app.AlertDialog.OnClickListener()
                                {
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        result.confirm();
                                    }
                                })
                        .setCancelable(false)
                        .create()
                        .show();


//                return super.onJsAlert(view, url, message, result);
                return true;
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
                Log.d(DBHelper.TAG, "onJsConfirm: JsResult result = "+result);
                new android.app.AlertDialog.Builder(webViewActivity.this)
                        .setTitle("Confirm")
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok,
                                new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int which) {
                                        result.confirm();
                                    }
                                })
                        .setNegativeButton(android.R.string.cancel,
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        result.cancel();
                                    }
                                })
                        .create()
                        .show();
                return true;
            }
        });
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                String cookie_str = CookieManager.getInstance().getCookie(url);
//                CookieManager.getInstance().
                Log.d(TAG, "onPageStarted: cookie : "+cookie_str);

//                JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask(cookie_str);
//                jsoupAsyncTask.execute();

            }
        });
//        java.net.CookieManager.setDefault();
//        CookieManager.getInstance().
//        HashMap<String, String> map = new HashMap<String, String>();
//        map.put("Set-Cookie", "CUK45=cuk45_smurfs81_042658e63d74fdfc2a58c04e638a21b1; CUK2Y=cuk2y_smurfs81_042658e63d74fdfc2a58c04e638a21b1; CID=CIDe6885df80e09196b4f09e5eaf88ce1e6; atl_epcheck=1; wish_id=6497617e2d45f0d2b8c076eb8b2ad2cc; isviewtype=mob; return_url=/; atl_option=0%2C0; CIDe6885df80e09196b4f09e5eaf88ce1e6=810fea3e8668f614c0a282bae072d7f4%3A%3A%3A%3A%3A%3A%3A%3A%3A%3A%3A%3A%3A%3A%3A%3A%3A%3A%3A%3A%2F%3A%3A1558423208%3A%3A%3A%3Apmdm%3A%3A1558423208%3A%3A%3A%3A%3A%3A%3A%3A; ECSESSID=1bf86e6c5820759fb8d562854691f4b1; recent_plist=47773%7C47759%7C47769%7C47790%7C46806%7C46820%7C47230%7C47202%7C47872%7C47756%7C47813%7C47844%7C47088%7C47227%7C46934%7C47829%7C46840%7C47867%7C47080%7C47226; wcs_bt=s_35c90374e148:1558424266; vt=1558424269; iscache=F; ec_mem_level=1; PHPSESSVERIFY=f53e095ea164ed1547216ad63a787d71");
        mWebView.loadUrl(href);

        // 아직 작성중인 코드
        //목적 : 장바구니에 있는 상품을 웹뷰로 보여줄때장바구니에 있는 상품과 없는 상품일 경우 아이콘 변경 필요
        /*1. userid , mallName, prodNumb를 통해서 선택한 상품이 장바구니에 있는지 확인
          2. 있으면 장바구니에서 지우는 버튼을 활성화(visible)
            -> "장바구니에서 삭제" 버튼을 누른후 다시 "장바구니에 넣기"버튼을 누를 수 있기 때문에 '장바구니에 넣기'를 할 수 있는 모든 정보를 보내줘야한다.
          3. 없으면 장바구니에 넣는 버튼을 활성화
        * */
        if (dbHelper == null){ // 생성된 db가 없을때 초기화해준다.
            dbHelper = new DBHelper(webViewActivity.this, "basket", null, DBHelper.DB_VERSION);
        }
        insertToBasket_bnt = findViewById(R.id.insertToBasket_bnt); // 장바구니 추가버튼
        deleteFromBasket_bnt = findViewById(R.id.deleteFromBasket_bnt);

        String result = dbHelper.checkProdExist(prodNumb,mallName);
        //선택한 상품이 장바구니에 있는지 구분해주는 메서드
        Log.d(TAG, "onCreate: "+result);
        if (result != null){//result 가 품번이 있으면 장바구니에 있는 상품, 없으면 장바구니에 없는 상품
            //장바구니에 상품이 있으면
            insertToBasket_bnt.setVisibility(View.GONE);//장바구니에 넣기버튼 숨기기
            deleteFromBasket_bnt.setVisibility(View.VISIBLE);//장바구니에서 제거 버튼 보이기
        }else {
            insertToBasket_bnt.setVisibility(View.VISIBLE);
            deleteFromBasket_bnt.setVisibility(View.GONE);
        }



        insertToBasket_bnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //클릭시 쇼핑몰 장바구니에 상품을 추가시킨다.
                //  == 장바구니에 추가 메소드(자바스크립트) 실행시키기
//                mWebView.loadUrl("javascript:product_submit(2, '/exec/front/order/basket/', this)");

                //클릭시 앱의 장바구니에 상품을 추가시킨다.(이걸 꼭 해야하는지는 생각해보자)
                // 앱db 에 상품데이터를 저장하는데
                //[ unmb, userId, userNickname, mallName,img_src, prodNumb, price, 삭제 메서드, 생성날짜, 수정날짜, hide, 품절여부, 삭제여부 ]
                //를 저장해야한다.


                LinearLayout layout = new LinearLayout(webViewActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);

                android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(webViewActivity.this);
                dialog.setTitle("장바구니에 넣으시겠습니까?")
                        .setView(layout)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                itemInfo itemInfo = new itemInfo();
                                itemInfo.setUserId(main.userId);
                                itemInfo.setUserNickname(main.userNickname);
                                itemInfo.setMallName(mallName);
                                itemInfo.setImg_src(img_src);
                                itemInfo.setProdName(prodName);
                                itemInfo.setProdNumb(prodNumb);
                                itemInfo.setPrice(price);
                                itemInfo.setProdHref(href);

                                // @@@ 추후 추가사항
                                //itemInfo.setSailPrice(); 가격을 원가와 할인가로 분리하는것은 일단은 보류
                                //itemInfo.setDeleteMethod(); 삭제 메서드는 장바구니에서 실행시킬수 있게 쇼핑몰 별로 정리해서 넣어놓을 수 있게 한다.
                                // credatedDate, modifiedDate, hide, soldout, deleted 는 추후에 생각하자..
//                                Log.d(DBHelper.TAG, "3 onClick: 상품 장바구니에 넣기 "+intentHadItemData.getStringExtra("prodName"));
                                dbHelper.addItem(itemInfo);


                            }
                        })
                        .setNeutralButton("CANCLE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create()
                        .show();
            }
        });

        deleteFromBasket_bnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout layout = new LinearLayout(webViewActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);

                android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(webViewActivity.this);
                dialog.setTitle("장바구니에서 해당상품을 삭제하시겠습니까?")
                        .setView(layout)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dbHelper.deleteItems(prodNumb, mallName);

                            }
                        })
                        .setNeutralButton("CANCLE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create()
                        .show();

            }
        });

        // 초기화 버튼( 새 테이블 생성 버튼)
        ImageView btnInsertButton = findViewById(R.id.btnInsertButton);
        btnInsertButton.setVisibility(View.INVISIBLE);
        btnInsertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* @ 데이블 초기 생성을 위한 코드 @
                final EditText etDBname = new EditText(webViewActivity.this);
                etDBname.setHint("DB명을 입력하세요.");

                AlertDialog.Builder dialog = new AlertDialog.Builder(webViewActivity.this);
                dialog.setTitle("Datebase 이름을 입력하세요.")
                        .setMessage("Database 이름을 입력하세요.")
                        .setView(etDBname)
                        .setPositiveButton("생성", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (etDBname.getText().toString().length() > 0){
                                    dbHelper = new DBHelper(
                                            webViewActivity.this,
                                            etDBname.getText().toString(),
                                            null, 1);
                                    dbHelper.testDB();

                                }
                            }
                        })
                        .create()
                        .show();*/
            }
        });



    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()){
            // a-1. 뒤로가기가 눌렸고 && 웹뷰가 뒤로갈 페이지가 있다면
            mWebView.goBack();
            // a-2. 뒤로가라
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
//    private class WebViewClientClass extends WebViewClient {
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//            Log.d("check URL", "shouldOverrideUrlLoading: "+request);
//            view.loadUrl(request);
//            return super.shouldOverrideUrlLoading(view, request);
//        }
//    }



}


class itemInfo{
    int numb;
    int userId;
    String userNickname;
    String mallName;
    String prodHref;
    String img_src;
    String prodName;
    String prodNumb;
    String price;
    String sailPrice;
    String deleteMethod;
    int hide;
    int soldout;
    int count;

    public int getCount() {
        return count;
    }

    public String getProdHref() {
        return prodHref;
    }

    public void setProdHref(String prodHref) {
        this.prodHref = prodHref;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public int getNumb() {
        return numb;
    }

    public void setNumb(int numb) {
        this.numb = numb;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getMallName() {
        return mallName;
    }

    public void setMallName(String mallName) {
        this.mallName = mallName;
    }

    public String getImg_src() {
        return img_src;
    }

    public void setImg_src(String img_src) {
        this.img_src = img_src;
    }

    public String getProdNumb() {
        return prodNumb;
    }

    public void setProdNumb(String prodNumb) {
        this.prodNumb = prodNumb;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSailPrice() {
        return sailPrice;
    }

    public void setSailPrice(String sailPrice) {
        this.sailPrice = sailPrice;
    }

    public String getDeleteMethod() {
        return deleteMethod;
    }

    public void setDeleteMethod(String deleteMethod) {
        this.deleteMethod = deleteMethod;
    }

    public int getHide() {
        return hide;
    }

    public void setHide(int hide) {
        this.hide = hide;
    }

    public int getSoldout() {
        return soldout;
    }

    public void setSoldout(int soldout) {
        this.soldout = soldout;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    int deleted;
}