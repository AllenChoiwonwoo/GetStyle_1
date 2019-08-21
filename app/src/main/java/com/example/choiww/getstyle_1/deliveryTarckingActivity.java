package com.example.choiww.getstyle_1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

public class deliveryTarckingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_tarcking);

        Intent intent = getIntent();
        String deliveryNumb = intent.getStringExtra("deliveryNumb");
        String deliveryTrackingAddress = "https://www.cjlogistics.com/ko/tool/parcel/tracking?gnbInvcNo="+deliveryNumb;

        WebView deliveryTracking_webview = findViewById(R.id.deliveryTracking_webview);
        deliveryTracking_webview.getSettings().setJavaScriptEnabled(true);
        deliveryTracking_webview.loadUrl(deliveryTrackingAddress);
        deliveryTracking_webview.setWebChromeClient(new WebChromeClient());
//        webView2.setWebViewClient(webViewClient);
        deliveryTracking_webview.setWebViewClient(new WebViewClient());

        ImageView deliveryTracking_X_img = findViewById(R.id.deliveryTracking_X_img);
        deliveryTracking_X_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}
