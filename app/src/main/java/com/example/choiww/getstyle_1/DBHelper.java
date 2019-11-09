package com.example.choiww.getstyle_1;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

//import com.example.choiww.getstyle_1.DataClass.SimpleInfoData;
import com.example.choiww.getstyle_1.DataClass.SimpleOrderInfoData;
import com.example.choiww.getstyle_1.DataClass.orderData;
//import com.example.choiww.getstyle_1.DataClass.simpleInfoData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
//import com.example.choiww.getstyle_1.DataClass.orderDate;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;

/*
    * 목적 : 안드로이내부의 db(sqlite)에 접근을 쉽게 도와주는 클래스 이다.
    *       DBHelper 는 안드로이드의 sqlite와의 연결을 도외주는 클래스이다.
    *
    * 메서드 :
    *   onCreate : db(테이블) 생성 및 초기화를 위한 메서드
    *   onUpgrade : 테이블 구조에 수정이 생겼을때 업데이트 시켜주는 메서드(버전정보를 구별하여 초기화시 발동됨)
    *   addItem : 장바구니table(BASKET table) 에 상품을 추가할때 사용하는 메서드
    *   getItem : 장바구니table에서 상품을 가져와 list로 보여줄때 사용하는 메서드
    *   deleteItem : 장바구니에 있는 상품을 삭제할때 사용하는 메서드
    *   checkProdExist : 작성중..
    *
    * */
public class DBHelper extends SQLiteOpenHelper {

    private Context context;
    public static final int DB_VERSION = 3; //db의 구조가 바뀔시 버전을 높히면 초기화시 table이 업데이트 된다.
    public static final String TAG = "find";

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
        this.context = context;
        SQLiteDatabase db = getWritableDatabase();
        int db_version = db.getVersion();
        Log.d(TAG, "DBHelper: 데이터 베이스의 버전은 (getVersion) = "+db_version);

        // 이상한 업데이트와 함께 sqlite의 데이터가 다 날라가서 db 가 초기화 되어 onCreate만 발동된상태이다
        // 즉 upGrade 메서드가 실행이 안되어 있는 상태여서 나중에 추가한 컬럼들이 없어 에러가 난다.
        // 하지만 현제 upGragde 메서드를 발동시키는 방법을 정화게 알 수 없다.
        // 그래서 컬럼이 없다면 에러가 나니 그 에러를 발생시켜 예외처리를 통해 upGrade 메서드가 발동되게 해야겠다.
        // -> 아니면 SQLite 용 예제를 다시 만들어서 해보는게 좋을 수 도 있겠다.

    }
    /** * Database가 존재하지 않을 때, 딱 한번 실행된다.
     * DB를 만드는 역할을 한다.
     * @param db
     * */

    @Override
    public void onCreate(SQLiteDatabase db) {
        //테이블 생성시 동작하는 메서드
        StringBuffer sb = new StringBuffer();
        // 테이블 구조
        //[ unmb, userId, userNickname, mallName, prodHref, img_src, prodName, prodNumb, price, 삭제 메서드, 생성날짜, 수정날짜, hide, 품절여부, 삭제여부 ]
        sb.append(" CREATE TABLE BASKET (");
        sb.append(" numb INTEGER PRIMARY KEY AUTOINCREMENT, ");
        sb.append(" userId INTEGER, ");
        sb.append(" userNickname VARCHAR, ");
        sb.append(" mallName VARCHAR, ");
//        sb.append(" prodHref VARCHAR, ");//
        sb.append(" img_src VARCHAR, ");
//        sb.append (" prodName VARCHAR. ");//
        sb.append(" prodNumb VARCHAR, ");
        sb.append(" price VARCHAR, ");
        sb.append(" sailPrice VARCHAR, ");
        sb.append(" deleteMethod VARCHAR, ");
        sb.append(" createdDate DATETIME, ");
        sb.append(" modifiedDate DATETIME, ");
        sb.append(" hide INTEGER, ");
        sb.append(" soldout INTEGER, ");
        sb.append(" deleted INTEGER, ");
        sb.append(" UNIQUE(mallName, prodNumb) ) ");

        db.execSQL(sb.toString());

        Log.e(TAG, "onCreate: dbhelper-oncreate 발동");

        Toast.makeText(context, "Table 생성완료", Toast.LENGTH_SHORT).show();
    }

    /**
     * Application의 버전이 올라가서
     * Table 구조가 변경되었을 때 실행된다.
     * @param db
     * @param oldVersion
     * @param newVersion
     */

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 테이블이 업데이트 될 시 적용시켜주는 메서드
//        if (oldVersion == 1 && newVersion ==2){
//            Log.d(TAG, "onUpgrade: 버전올리기를 시작");
//            StringBuffer sb = new StringBuffer();
//            sb.append(" ALTER TABLE BASKET ADD prodName VARCHAR ");
//            db.execSQL(sb.toString());
//            StringBuffer sb2 = new StringBuffer();
//            sb2.append(" ALTER TABLE BASKET ADD prodHref VARCHAR ");
//            db.execSQL(sb2.toString());
//                    //하나의 쿼리로 2개의 컬럼의 추가하려다가 잘 안되어 하나씩 2번작업함
//
//        }
        if (newVersion <=2 && newVersion <4){
            Log.d(TAG, "onUpgrade: 버전올리기를 시작");
            StringBuffer sb = new StringBuffer();
            sb.append(" ALTER TABLE BASKET ADD prodName VARCHAR ");
            db.execSQL(sb.toString());
            StringBuffer sb2 = new StringBuffer();
            sb2.append(" ALTER TABLE BASKET ADD prodHref VARCHAR ");
            db.execSQL(sb2.toString());
            //하나의 쿼리로 2개의 컬럼의 추가하려다가 잘 안되어 하나씩 2번작업함

            sb = new StringBuffer();
            sb.append(" CREATE TABLE orderHistory ( ");
            sb.append(" numb INTEGER PRIMARY KEY AUTOINCREMENT, "); //index
            sb.append(" userId INTEGER, "); // 회원번호
            sb.append(" userEmail VARCHAR, ");  // 회원아이디(이메일)
            sb.append(" products_json TEXT, "); // json화 된 구매상품정보들
            sb.append(" orderNumb VARCHAR, ");  // 주문번호
            sb.append(" orderDate DATETIME, "); // 주문일자
            sb.append(" buyer VARCHAR, ");      // 구매자
            sb.append(" orderState VARCHAR, "); //  주문상태(임금전,배송준비중,배송중,배송완료)
            sb.append(" productsPrice INTEGER, ");  // 상품들 가격의 합
            sb.append(" saleDiscount INTEGER, ");   // 할인금액의 합
            sb.append(" billedPoint INTEGER, ");    // 사용한 포인트
            sb.append(" deliveryCharge INTEGER, "); // 배송비
            sb.append(" finalBill INTEGER, ");      // 총 결제금액
            sb.append(" paymentMethod VARCHAR, ");  // 지불방식
            sb.append(" depositor VARCHAR, ");      // 입금자명
            sb.append(" accountNumb VARCHAR, ");    // 입금계좌번호, 은행
            sb.append(" payDay DATETIME, ");        // 입금기한
            sb.append(" receiverName VARCHAR, ");   // 받는사람
            sb.append(" receiverAddress VARCHAR, ");// 받는이 주소
            sb.append(" receiverCallNumb VARCHAR, ");// 받는이 집전화
            sb.append(" receiverCellphone VARCHAR, ");// 받는이 핸드폰
            sb.append(" createdDate DATETIME, "); // 생성날짜
            sb.append(" modifiedDate DATETIME ) ");   // 수정날짜

            db.execSQL(sb.toString());
            Log.d(TAG, "onUpgrade: db에 새로운 테이블을 추가하였습니다.");


        }// sqlite 가 업데이트 안되서 장바구니를 사용할 수 없는 문제를 해결하기 위해
//        if (oldVersion ==2 && newVersion == 3){
//            StringBuffer sb = new StringBuffer();
//            sb.append(" CREATE TABLE orderHistory ( ");
//            sb.append(" numb INTEGER PRIMARY KEY AUTOINCREMENT, "); //index
//            sb.append(" userId INTEGER, "); // 회원번호
//            sb.append(" userEmail VARCHAR, ");  // 회원아이디(이메일)
//            sb.append(" products_json TEXT, "); // json화 된 구매상품정보들
//            sb.append(" orderNumb VARCHAR, ");  // 주문번호
//            sb.append(" orderDate DATETIME, "); // 주문일자
//            sb.append(" buyer VARCHAR, ");      // 구매자
//            sb.append(" orderState VARCHAR, "); //  주문상태(임금전,배송준비중,배송중,배송완료)
//            sb.append(" productsPrice INTEGER, ");  // 상품들 가격의 합
//            sb.append(" saleDiscount INTEGER, ");   // 할인금액의 합
//            sb.append(" billedPoint INTEGER, ");    // 사용한 포인트
//            sb.append(" deliveryCharge INTEGER, "); // 배송비
//            sb.append(" finalBill INTEGER, ");      // 총 결제금액
//            sb.append(" paymentMethod VARCHAR, ");  // 지불방식
//            sb.append(" depositor VARCHAR, ");      // 입금자명
//            sb.append(" accountNumb VARCHAR, ");    // 입금계좌번호, 은행
//            sb.append(" payDay DATETIME, ");        // 입금기한
//            sb.append(" receiverName VARCHAR, ");   // 받는사람
//            sb.append(" receiverAddress VARCHAR, ");// 받는이 주소
//            sb.append(" receiverCallNumb VARCHAR, ");// 받는이 집전화
//            sb.append(" receiverCellphone VARCHAR, ");// 받는이 핸드폰
//            sb.append(" createdDate DATETIME, "); // 생성날짜
//            sb.append(" modifiedDate DATETIME ) ");   // 수정날짜
//
//            db.execSQL(sb.toString());
//            Log.d(TAG, "onUpgrade: db에 새로운 테이블을 추가하였습니다.");
//
//        }
        if (oldVersion ==3 && newVersion == 4){
            // 배송메시지, 운송장번호를 넣는 col이 필요하다.
            // 입금 상태는 있지만 서버에서 받아와야한다.
        }
        Toast.makeText(context,"버전이 올라갔습니다.", Toast.LENGTH_SHORT).show();
    }
    public void testDB(){
        SQLiteDatabase db = getReadableDatabase();
    }
    public  void addItem(itemInfo itemInfo){
        // 장바구니에 상품 추가시 사용하는 메서드
//        1. 쓸 수 있는 DB객체를 가져온다.
        SQLiteDatabase db = getWritableDatabase();



//        2. itemInfo 를 insert한다.
//        _id는 자동으로 증가하기 때문에 넣지 않습니다.
        StringBuffer sb = new StringBuffer();
        sb.append(" INSERT INTO BASKET ( ");
        sb.append(" userId, userNickname, mallName, img_src, prodName, prodNumb, price, createdDate, prodHref ) ");
        sb.append(" VALUES (?, ?, ?, ?, ?, ?, ?, DATETIME('now'), ? ) ");

        try {
//            Log.d(TAG, "4 addItem: 상품이름 : "+itemInfo.getProdName());
            db.execSQL(sb.toString(),
                    new Object[]{
                            itemInfo.getUserId(),
                            itemInfo.getUserNickname(),
                            itemInfo.getMallName(),
                            itemInfo.getImg_src(),
                            itemInfo.getProdName(),
                            itemInfo.getProdNumb(),
                            itemInfo.getPrice(),
                            itemInfo.getProdHref()

                    });
            Toast.makeText(context, "Insert 완료", Toast.LENGTH_SHORT).show();
            //@@@@ 여기에서 첫번째 상품의 이름을 불러왔을때 방금 넣은 상품이 나와야한다.
        }catch (SQLiteConstraintException e){
            Log.d(TAG, "addPerson: "+e);
            Toast.makeText(context, "Insert 실패, 유니키 중복", Toast.LENGTH_SHORT).show();

        }

    }
    public ArrayList<itemInfo> getItems(int userId, String mallName){


        // 장바구니에서 상품정보들을 가져오기위한 메서드
        SQLiteDatabase db = getReadableDatabase();
//        Log.d(TAG, "getItems: readable db선언");
        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT ");
        sb.append(" mallName,img_src, prodName, prodNumb, price, createdDate, prodHref ");
        sb.append(" FROM BASKET ");
        sb.append(" WHERE mallName='"+mallName+"' AND userId = "+userId+"");
        sb.append(" ORDER BY numb DESC ");
//         ORDER BY numb DESC 를 통해 넣은 순으로 불러올 수 있다.
//        Log.d(TAG, "getItems: 쿼리만들기"+sb.toString());
        Cursor results = db.rawQuery(sb.toString(), null);
        // result에 db에서 가져온 장바구니 데이터를 담는다.
//        Log.d(TAG, "getItems: result = "+results.toString());


        ArrayList<itemInfo> alist = new ArrayList();
        int n=0;
                        // Cursor 을 그대로 넘기는게 좋은것일까? 아니면 itemInfo 객체에 담아서 보내는게 좋은것일까?( => 다른사람이 보기엔 itemInfo가 낫것지?)

        // 장바구니 리사이클러뷰로 데이터를 보내주기 위한 list를 만든다.
        while (results.moveToNext()){ // 다음에 값있으면 true, 없으면 false를 반환한다.
            itemInfo nIteminfo = new itemInfo();
            nIteminfo.setMallName(results.getString(0));//컬럼의 index(내가 작성한 쿼리문과 순서 일치)
            nIteminfo.setImg_src(results.getString(1));
            nIteminfo.setProdName(results.getString(2));
            nIteminfo.setProdNumb(results.getString(3));
            nIteminfo.setPrice(results.getString(4));
            nIteminfo.setProdHref(results.getString(6));

            alist.add(nIteminfo);
            Log.d(TAG, "getItems: 반복 횟수 n = "+(n++));

        }
        return alist;

    }
        public ArrayList<itemInfo> getItems(int userId){


            // 장바구니에서 상품정보들을 가져오기위한 메서드
            // 앱의 db-basket table에서 해당 회원이 장바구니에 담은 상품을 가져오기 위한 메서드다.
            // 인자값(userld)를 활용하여 로그인한 회원이 장바구니에 넣은 상품만 가져온다.

            SQLiteDatabase db = getReadableDatabase();
//        Log.d(TAG, "getItems: readable db선언");
            StringBuffer sb = new StringBuffer();
            sb.append(" SELECT ");
            sb.append(" mallName,img_src, prodName, prodNumb, price, createdDate, prodHref ");
            sb.append(" FROM BASKET WHERE userId = "+userId+" ORDER BY numb DESC");
//         ORDER BY numb DESC 를 통해 넣은 순으로 불러올 수 있다.
//        Log.d(TAG, "getItems: 쿼리만들기"+sb.toString());
            Cursor results = db.rawQuery(sb.toString(), null);
            // result에 db에서 가져온 장바구니 데이터를 담는다.
//        Log.d(TAG, "getItems: result = "+results.toString());


            ArrayList<itemInfo> alist = new ArrayList();
            int n=0;
            // Cursor 을 그대로 넘기는게 좋은것일까? 아니면 itemInfo 객체에 담아서 보내는게 좋은것일까?( => 다른사람이 보기엔 itemInfo가 낫것지?)

            // 장바구니 리사이클러뷰로 데이터를 보내주기 위한 list를 만든다.
            while (results.moveToNext()){ // 다음에 값있으면 true, 없으면 false를 반환한다.
                itemInfo nIteminfo = new itemInfo();
                nIteminfo.setMallName(results.getString(0));//컬럼의 index(내가 작성한 쿼리문과 순서 일치)
                nIteminfo.setImg_src(results.getString(1));
                nIteminfo.setProdName(results.getString(2));
                nIteminfo.setProdNumb(results.getString(3));
                nIteminfo.setPrice(results.getString(4));
                nIteminfo.setProdHref(results.getString(6));

                alist.add(nIteminfo);
                Log.d(TAG, "getItems: 반복 횟수 n = "+(n++));

            }
            return alist;

        }

    public int getMallItemCount(String mallNumb){

        /*이 메서드는 쇼핑몰별로 몇개의 상품이 담겨있는지 반환해주기 위한 코드이다.


        * */

        // 장바구니에서 쇼핑몰별 상품의 개수를 가져오기 위한 코드
        SQLiteDatabase db = getReadableDatabase();
//        Log.d(TAG, "getItems: readable db선언");
        StringBuffer sb = new StringBuffer();
//        sb.append(" SELECT ");
//        sb.append(" prodNumb FROM basket Where mallName = '' ");
        String query = "select prodNumb from basket where mallName = '"+mallNumb+"'";
//        sb.append(" mallName,img_src, prodName, prodNumb, price, createdDate, prodHref ");
//        sb.append(" FROM BASKET ORDER BY numb DESC");
//         ORDER BY numb DESC 를 통해 넣은 순으로 불러올 수 있다.
//        Log.d(TAG, "getItems: 쿼리만들기"+sb.toString());
        Cursor results = db.rawQuery(query, null);
        int cnt = results.getCount();

        // result에 db에서 가져온 장바구니 데이터를 담는다.
//        Log.d(TAG, "getItems: result = "+results.toString());

        return cnt;
    }


    // 상품이 장바구니에 담겨있는 상품인지 아닌지 구별해주는 메서드다 - return으로 상품이 있다면 prodNumb를 없다면 ""를 반환하게 한다.
    public String checkProdExist(String prodNumb, String mallName){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT prodNumb FROM BASKET WHERE userID='"+main.userId+"' AND prodNumb='"+prodNumb+"' AND mallName='"+mallName+"'";
        Cursor result = db.rawQuery(query,null);
//        result.
        String resultProdNumb = null;
        while (result.moveToNext()){
            Log.d(TAG, "checkProdExist: 쿼리 성공해서 값 result 안에 있다."+result.getString(0));
            resultProdNumb = result.getString(0);
        }
//        result.moveToFirst();
////        result.
//        Log.d(TAG, "checkProdExist: result = "+result.getString(0));
        return resultProdNumb;
    }
    public void deleteItems(String prodNumb, String mallName){
        //상품을 db에서 삭제하기위한 메서드
        SQLiteDatabase db = getWritableDatabase();
        String query = "DELETE FROM BASKET WHERE prodNumb='"+prodNumb+"'"+"AND mallName='"+mallName+"'";
        // WHERE을 prodNumb 와 prodName으로 하는 이유는 두 값이 unique key 이기 때문이다.
        db.execSQL(query);
        Log.d(TAG, "deleteItems: "+mallName+"의 "+prodNumb+"이 삭제되었습니다.");
    }
    public void InsertNewOrder(String orderNumb, String orderDate, String orderProdList, orderData orderData){
        // 완료된 주문내역을 저장한다.
        // 상품 리스트는 그냥 json으로 넣자.



        // 회원번호
        int userId = main.userId;
        // 회원아이디(이메일)
        String userEmail = main.userEmail;
        // json 화된 구매상품 정보들(주문상품리스트)
        String myorderProdList_str = orderProdList;
        // 주문번호
        String myorderNumb = orderNumb;
        // 주문일자
        String myorderDate = orderDate;
        // 구매자
        String buyer = orderData.getBuyer_name();
        // 주문상태
//        String orderState = orderData.get
        String orderState = "입금전"; //임시
        // 상품원가 합계
        int productsPrice = 0;
        // 총할인 합계
        int saleDIscount = 0;
        // 사용 포인트
        int billedPoint = 0;
        // 배송비 합계
        int deliveryCharge = 0;

        // 총 결제금액
        int finalBill = orderData.getCoast();
        // 지불방식
        String paymentMethod = orderData.getSendPayment();
//        String paymentMethod = "무통장입금";
        // 입금자명
//        String depositor = "최원우";   //주문화면에서 .. 이값을 받았어야했는데 깜빡했네..
        String depositor = orderData.getBuyer_name();//구매자로 하자
        // 입금계좌번호, 은행
        String accountNumb = orderData.getSendCash();  //이건 또 서버서 줬어야하는데.
//        String accountNumb = "우리은행 100245862365";
        // 입금기한
//        String payDay =  //이건 주문일로 부터 하루? 정도로 해야겠다. -> 서버에서 주는게 낫겠다.
        String payDay = "2019-06-25";
        // 받는사람
        final String receiverName = orderData.getReceiver_name();
        // 받는이 주소
        String receiverAddress = orderData.getReceiver_address1()+" "+orderData.getReceiver_address2();
        // 받는이 집전화
        String receiverCallNumb = orderData.getReceiver_callNumb();
        // 받는이 핸드폰
        String receiverCellphone = orderData.getReceiver_cellphone();
            // row 생성날짜
            // row 수정날짜
        //$$ 배송메시지.. 저장해야지..

        SQLiteDatabase db = getWritableDatabase();

        StringBuffer sb = new StringBuffer();
        sb.append(" insert into orderHistory ( ");
        sb.append(" userId, userEmail, products_json, orderNumb, orderDate" +
                ", buyer, orderState, productsPrice, saleDiscount, billedPoint" +
                ", deliveryCharge, finalBill, paymentMethod, depositor, accountNumb" +
                ", payDay, receiverName, receiverAddress, receiverCallNumb, receiverCellphone" +
                ", createdDate " +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, datetime('now','localtime') ) "); //20개 물음표
        try {
            db.execSQL(sb.toString(), new Object[]{
                    userId, userEmail, myorderProdList_str, myorderNumb, myorderDate
                    , buyer, orderState, productsPrice, saleDIscount, billedPoint
                    , deliveryCharge, finalBill, paymentMethod, depositor, accountNumb
                    , payDay, receiverName, receiverAddress, receiverCallNumb, receiverCellphone
            });
            Log.d(TAG, "InsertNewOrder: 주문 저장 성공");

            // 서버 db - all_orders table에 주문내역을 저장하게 하는 코드 작성
            news_ex1 connector = news_ex1.retrofit.create(news_ex1.class);
            retrofit2.Call call = connector.saveOrder(userId,userEmail, myorderProdList_str, myorderNumb, myorderDate
            , buyer, orderState, productsPrice, saleDIscount, billedPoint
            , deliveryCharge, finalBill, paymentMethod, depositor, accountNumb
            , payDay, receiverName, receiverAddress, receiverCallNumb, receiverCellphone );
            call.enqueue(new Callback() {
                @Override
                public void onResponse(retrofit2.Call call, Response response) {
                    Log.d(TAG, "onResponse: saveOrder : "+response.code());
                }

                @Override
                public void onFailure(retrofit2.Call call, Throwable t) {
                    Log.e(TAG, "onFailure: fail save : "+t);
                }
            });


        }catch (SQLiteConstraintException e){
            Log.d(TAG, "InsertNewOrder: 주문저장 실패");
            
        }

    }
//    public void InsertNewOrder(String orderNumb, String orderDate, String orderProdList, orderData orderData){
//        // 완료된 주문내역을 저장한다.
//        // 상품 리스트는 그냥 json으로 넣자.
//
//
//
//        // 회원번호
//        int userId = main.userId;
//        // 회원아이디(이메일)
//        String userEmail = main.userEmail;
//        // json 화된 구매상품 정보들(주문상품리스트)
//        String myorderProdList_str = orderProdList;
//        // 주문번호
//        String myorderNumb = orderNumb;
//        // 주문일자
//        String myorderDate = orderDate;
//        // 구매자
//        String buyer = orderData.getBuyer_name();
//        // 주문상태
////        String orderState = orderData.get
//        String orderState = "입금전"; //임시
//        // 상품원가 합계
//        int productsPrice = 0;
//        // 총할인 합계
//        int saleDIscount = 0;
//        // 사용 포인트
//        int billedPoint = 0;
//        // 배송비 합계
//        int deliveryCharge = 0;
//
//        // 총 결제금액
//        int finalBill = orderData.getCoast();
//        // 지불방식
//        String paymentMethod = orderData.getSendPayment();
////        String paymentMethod = "무통장입금";
//        // 입금자명
////        String depositor = "최원우";   //주문화면에서 .. 이값을 받았어야했는데 깜빡했네..
//        String depositor = orderData.getBuyer_name();//구매자로 하자
//        // 입금계좌번호, 은행
//        String accountNumb = orderData.getSendCash();  //이건 또 서버서 줬어야하는데.
////        String accountNumb = "우리은행 100245862365";
//        // 입금기한
////        String payDay =  //이건 주문일로 부터 하루? 정도로 해야겠다. -> 서버에서 주는게 낫겠다.
//        String payDay = "2019-06-25";
//        // 받는사람
//        String receiverName = orderData.getReceiver_name();
//        // 받는이 주소
//        String receiverAddress = orderData.getReceiver_address1()+" "+orderData.getReceiver_address2();
//        // 받는이 집전화
//        String receiverCallNumb = orderData.getReceiver_callNumb();
//        // 받는이 핸드폰
//        String receiverCellphone = orderData.getReceiver_cellphone();
//        // row 생성날짜
//        // row 수정날짜
//        //$$ 배송메시지.. 저장해야지..
//
//
//        try {
//
//
//            // 서버 db - all_orders table에 저장하게 하는 코드 작성
//            news_ex1 connector = news_ex1.retrofit.create(news_ex1.class);
//            retrofit2.Call call = connector.sendOrder(userId,userEmail, myorderProdList_str, myorderNumb, myorderDate
//                    , buyer, orderState, productsPrice, saleDIscount, billedPoint
//                    , deliveryCharge, finalBill, paymentMethod, depositor, accountNumb
//                    , payDay, receiverName, receiverAddress, receiverCallNumb, receiverCellphone );
//            call.enqueue(new Callback() {
//                @Override
//                public void onResponse(retrofit2.Call call, Response response) {
//
//                }
//
//                @Override
//                public void onFailure(retrofit2.Call call, Throwable t) {
//
//                }
//            });
//
//
//        }catch (SQLiteConstraintException e){
//            Log.d(TAG, "InsertNewOrder: 주문저장 실패");
//
//        }
//
//    }

    public ArrayList getAllOrderHistory(){
        // 주문내역 화면에 사용자의 모든 주문내역을 보여주기위한 데이터를 가져오기 위한 메서드

        ArrayList<HashMap> arrayList = new ArrayList();//hash로 담으니 서버에서 데이터를 가져오기 어려움
        ArrayList<SimpleOrderInfoData> arrayList1 = new ArrayList(); // 데이터클래스에 담는것으로 변경

        SQLiteDatabase db = getReadableDatabase();
//        StringBuffer sb = new StringBuffer();
//        sb.append(" SELECT orderNumb, orderDate, orderState, finalBill FROM orderHistory WHERE userId= ");
//        sb.append('')
        int i = main.userId;
//        sb.append()
        String query;
        query = "SELECT orderNumb, orderDate, orderState, finalBill, numb FROM orderHistory WHERE userId=" +i+" order by numb desc";
        Cursor cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()){
            HashMap hashMap = new HashMap();
            hashMap.put("orderNumb", cursor.getString(0));
            hashMap.put("orderDate", cursor.getString(1));
            hashMap.put("orderState", cursor.getString(2));
            hashMap.put("finalBill",cursor.getInt(3));
            hashMap.put("numb", cursor.getString(4));
//            Log.d(TAG, "getAllOrderHistory: dbheler 안 메소드 안의 numb : "+cursor.getString(4));
            //여기선 다 다른값이 나온다.
            SimpleOrderInfoData simpleOrderInfoData = new SimpleOrderInfoData();
            simpleOrderInfoData.setOrderNumb(cursor.getString(0));
            simpleOrderInfoData.setOrderDate(cursor.getString(1));
            simpleOrderInfoData.setOrderState(cursor.getString(2));
            simpleOrderInfoData.setFinalBill(cursor.getInt(3));
            simpleOrderInfoData.setNumb(cursor.getString(4));

            arrayList1.add(simpleOrderInfoData);//데이터 클래스르 담음
            arrayList.add(hashMap);

        }

//        Log.d(TAG, "getAllOrderHistory: dbheler 안 메소드 안, while 밖 numb 0 : "+arrayList.get(0).get("numb"));
//        Log.d(TAG, "getAllOrderHistory: dbheler 안 메소드 안, while 밖 numb 1 : "+arrayList.get(1).get("numb"));
//        Log.d(TAG, "getAllOrderHistory: dbheler 안 메소드 안, while 밖 numb 2 : "+arrayList.get(2).get("numb"));
//        Log.d(TAG, "getAllOrderHistory: dbheler 안 메소드 안, while 밖 numb 3 : "+arrayList.get(3).get("numb"));

        return arrayList1;
    }

    public Cursor getOneOrders(int orderNumb){
        // 주문내역 화면에서 하나의 아이탬을 클릭했을때 해당 주문을 상세하게 보여주기 위한 데이터를 가져오는 메서드이다.

        SQLiteDatabase db = getReadableDatabase();
        StringBuffer sb = new StringBuffer();
        itemInfo itemInfo = new itemInfo();
        ArrayList<itemInfo> arrayList;
        sb.append(" select " +
                "   products_json, orderNumb, orderDate" +
                "  , buyer, orderState, productsPrice, saleDiscount, billedPoint" +
                "  , deliveryCharge, finalBill, paymentMethod, depositor, accountNumb" +
                "  , payDay, receiverName, receiverAddress, receiverCallNumb, receiverCellphone");
        sb.append("  from orderHistory where numb = ");
        String ordernumb = orderNumb+"";
        sb.append(ordernumb);
        sb.append( " ORDER BY numb desc ");
        Cursor cursor = db.rawQuery(sb.toString(), null);
        //$$ 이코드는 where 조건만 붙기에 이렇게 마지막에 orderNumb 를 추가하는 식으로 할 수 있었지만 좋은 코드가 아닌듯 하다.
//        나중에 수정이 필요하다.
        String products_json = null;
        while (cursor.moveToNext()){
            Log.d(TAG, "getOneOrders: "+cursor.getString(1));
            products_json = cursor.getString(0);
            Gson gson = new Gson();
//            Type nameType = new TypeToken<>(){}.getType(); //결과가 list인 경우

            Type type = new TypeToken<ArrayList<itemInfo>>(){}.getType();
            arrayList = gson.fromJson(products_json,type);
            //where 조건으로 인해 1개의 row밖에 안나올텐데 설마 2번이상 반복문이 돌진 않겠지?
        }
        HashMap hashMap = new HashMap();
        hashMap.put("products_json", products_json);

        return cursor;
    }
//    public void sendMallList(){
//
//    }

//    public void
}

////dbHelper 가 선언된적이 없으면 초기화 시킨다.
//        if (dbHelper == null){
//                dbHelper = new DBHelper(OrderCompleteActivity.this, "basket", null, DBHelper.DB_VERSION);
//                Log.d(DBHelper.TAG, "onCreate: 초기화 됨");
//                }