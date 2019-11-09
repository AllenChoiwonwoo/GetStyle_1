package com.example.choiww.getstyle_1;

import android.util.Log;

//import com.example.choiww.getstyle_1.DataClass.orderDate;

import com.example.choiww.getstyle_1.DataClass.MallOrderListDate;
import com.example.choiww.getstyle_1.DataClass.SimpleOrderInfoData;
import com.example.choiww.getstyle_1.DataClass.orderDataFromServer;
import com.example.choiww.getstyle_1.DataClass.userInfoData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
    /*(인터페이스)목적 : 웹페이지와 연결해주는 부분
                          하단에 메소드만 늘려주면 하나의 인터페이스로 여러 http요청을 보낼 수 있다.

            */
public interface news_ex1 {
    String API_URL = "http://ec2-13-209-50-185.ap-northeast-2.compute.amazonaws.com/";

    @GET("api/php_pyi_limit.php") //실시간으로 크롤링한 신상품 데이더를 받아올 수 있는 url
    Call<List<dataclass_1>> getNewProdLimit(@Query("startIndex") int startIndex, @Query("endIndex") int endIndex);
    //쿼리문 "SELECT mall_name, img_src, prodname, price, count, href FROM new_products LIMIT $startIndex(여기서부터), $endIndex(몇개 가져올지)"
        // 쿼리문을 정확하게 몰라서 시간을 낭비했다.

    @GET("api/php_pyi.php") //실시간으로 크롤링한 신상품 데이더를 받아올 수 있는 url
    Call<List<dataclass_1>> getNewProd();
    //쿼리문 "SELECT mall_name, img_src, prodname, price, count, href FROM new_products

    @GET("api/joiner.php")// @ 인단은 get 으로 눈에 보이게 확인하고, 잘 되면 post로 변경
    Call<List<boolenData>> joinNewUser(
            @Query("inputEmail") String inputEmail
            , @Query("inputName") String inputName
            , @Query("inputNickname") String inputNickname
            , @Query("inputPasswd") String inputPasswd
    );

    @GET("api/loginer.php") Call<List<userInfoForLogin>> loginConfirm(
            @Query("inputEmail") String inputEmail
            ,@Query("inputPasswd") String inputName
    );
    @GET("api/mallUpdateInfo.php") Call<List<mallUpdateDate>> getMallUpdateInfo();
    //어떤 데이터를 보내서 받아야할까?

//        @GET("test/json.php") Call<String> sendJson(@Body dataclass_1 body);
        // 장바구니에 넣어놓은 상품이 품절 되었는지를 확인하는 메서드
        @FormUrlEncoded
    @POST("api/checkBasketItems.php")
//    @POST로 하니까 서버에서 데이터를 못받았는데 .. get으로 하니까 보내진다..
    // @POST를 쓰는 방법에 대한
    Call<List<checkedItemData>> checkBasketItems(
//            @Query("prodNumbArray") String itemArray
//            , @Query("mallNameArray") String mallArray
            @Field("prodNumbArray") String itemArray
            ,@Field("mallNameArray") String mallArray
    );
        @FormUrlEncoded
        @POST("api/getOrder.php") Call <ResponseBody> sendOrder(
                @Field("orderData")String jArray
                );
        @FormUrlEncoded
        @POST("api/saveOrder.php") Call <ResponseBody> saveOrder(
//                @Field("orderData")String jArray
                @Field("userId")int userId2
                ,@Field("userEmail")String userEmail
                ,@Field("products_json")String products_json
                ,@Field("orderNumb")String orderNumb
                ,@Field("orderDate")String orderDate
                ,@Field("buyer")String buyer
                ,@Field("orderState")String orderState
                ,@Field("productsPrice")int productsPrice
                ,@Field("saleDiscount")int saleDiscount
                ,@Field("billedPoint")int billedPoint
                ,@Field("deliveryCharge")int deliveryCharge
                ,@Field("finalBill")int finalBill
                ,@Field("paymentMethod")String paymentMethod
                ,@Field("depositor")String depositor
                ,@Field("accountNumb")String accountNumb
                ,@Field("payDay")String payDay
                ,@Field("receiverName")String receiverName
                ,@Field("receiverAddress")String receiverAddress
                ,@Field("receiverCallNumb")String receiverCallNumb
                ,@Field("receiverCellphone")String receiverCellphone
//                ,@Field("receiverName")String receiverName
//                ,@Field("receiverName")String receiverName

        );
     @FormUrlEncoded
     @POST("api/sendMallOrderList.php")
             Call<ResponseBody> sendAdminMallOrderList(
                     @Field("json_mallOrderList") String jArray
            );

//     @FormUrlEncoded
     @GET("api/getMallOrderList.php")
             Call<ArrayList<MallOrderListDate>> getAdminMallOrderList(
//                     @Query("ShowAndHideClassifier") int i
//                     @Field("json_mallOrderList") String jArray

     );
     @GET("api/getHideMallList.php")
             Call<ArrayList<MallOrderListDate>> getAdminHideMallOrderList();

        @FormUrlEncoded
        @POST("api/sendAdminOrderList.php")// 필터를 쓸때
        Call<ArrayList<SimpleOrderInfoData>> getAdminOrderList(
                @Field("filter") int filter
//                @Field("") String jArray
                // 지금 여러가지 조건이 있을수 있다,( 전체, 미결제, 결제완료 )
                // 조건에 따라 field를 보낸수도 안보낼 수 도 있어야하낟.
        );
//        @FormUrlEncoded
        @POST("api/sendAdminOrderList.php")// 필터를 안쓸때
        Call<ArrayList<SimpleOrderInfoData>> getAdminOrderList(
//                @Field("filter") String filter
//                @Field("") String jArray
                // 지금 여러가지 조건이 있을수 있다,( 전체, 미결제, 결제완료 )
                // 조건에 따라 field를 보낸수도 안보낼 수 도 있어야하낟.
        );
        @FormUrlEncoded
        @POST("api/sendAdminOneOrder.php")
                Call<ArrayList<orderDataFromServer>> getAdminOneOrder(
                        @Field("numb") int numb
        );

        @FormUrlEncoded
        @POST("api/updateOrderInfo.php")
                Call<ResponseBody> updateOrderInfo(
                        @Field("changedOrderState") String changedOrderState
                        ,@Field("orderNumb") String ordernumb
        );

//        @FormUrlEncoded
        @POST("api/sendAdminFilteredUserInfo.php")
                Call<ArrayList<userInfoData>> getUserInfo();
        @FormUrlEncoded
        @POST("api/sendAdminFilteredUserInfo.php")
                Call<ArrayList<userInfoData>> getUserInfo(
                        @Field("keyword") String keyword
                        ,@Field("filter") int filter
        );

        @FormUrlEncoded
        @POST("api/sendAdminOneUserInfo.php")
                Call<ArrayList<userInfoData>> getAdminOneUserInfo(
                        @Field("id") int id
        );

        @FormUrlEncoded
                @POST("api/setUserStatus.php")
                Call<ResponseBody> setUserStatus(
                        @Field("userStatus") int userStatus
                        ,@Field("id") int id
        );

        @GET("api/makeNewChatRoom.php")
        Call<ResponseBody> sendNewChatRoomRequest(
                @Query("newRoomData_json") String roomData
        );


        //string 화한 array를 서버로 보낸다.

//    OkHttpClient okHttpClient = new OkHttpClient.Builder()
//            .connectTimeout(1, TimeUnit.MINUTES)
//            .readTimeout(30, TimeUnit.SECONDS)
//            .writeTimeout(15, TimeUnit.SECONDS)
//            .build();
//        Retrofit.Builder retrofit = new Retrofit.Builder()
//                .baseUrl(API_URL)
//                .client(okHttpClient)
//                .addConverterFactory(GsonConverterFactory.create());
//
    OkHttpClient okHttpClient = new OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.MINUTES)
        .readTimeout(150, TimeUnit.SECONDS)
        .writeTimeout(150, TimeUnit.SECONDS)
        .build();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(API_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
