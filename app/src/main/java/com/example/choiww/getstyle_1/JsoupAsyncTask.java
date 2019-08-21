package com.example.choiww.getstyle_1;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;

import java.io.IOException;

import okhttp3.Response;

public class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {

    String cookies;
    Connection.Response pageDocument;
    String TAG = "find";

    public JsoupAsyncTask(String cookies){
        this.cookies = cookies;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        try {
            pageDocument = Jsoup.connect("https://m.xecond.co.kr/order/basket.html")// 크롤링하는 웹페이지 주소
                    .userAgent("Mozilla/5.0 (Linux; Android 7.1.2; Pixel Build/NHG47O) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.125 Mobile Safari/537.36")
                    .timeout(3000)
                    .cookie("cookie",cookies)
                    .execute();

            org.jsoup.nodes.Document doc = pageDocument.parse();
//            Elements links1 = doc.select("#contents > div.xans-element-.xans-order.xans-order-basketpackage > div.xans-element-.xans-order.xans-order-emptyitem.toggleArea.eToggle.selected > div.contents > div.xans-element-.xans-order.xans-order-normnormal.xans-record- > div.xans-element-.xans-order.xans-order-list > div > div.description > strong > a");
            Elements links1 = doc.select("#contents > div.xans-element-.xans-order.xans-order-basketpackage > div.xans-element-.xans-order.xans-order-tabinfo.tab.typeStrong.gFlex > ul > li.selected > a");
//            Log.d("find", "doInBackground: linkss의 길이 : "+links1.size());
            Log.d(TAG, "doInBackground: "+links1.get(0).text());
//            for (Element link : links1){
////                    Log.d("find", "내 아이디의 장바구이 아이탬 가져오기 성공 : "+link.text());
//                Log.d("find", "상품이름 : "+link.text());
//            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
