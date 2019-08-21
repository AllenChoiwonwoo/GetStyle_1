package com.example.choiww.getstyle_1;

import android.app.Instrumentation;
import android.os.AsyncTask;
import android.util.Log;
import android.webkit.WebView;

public class AutoLoginAsync extends AsyncTask<Void, Integer, Void> {
    WebView webView;
    String member_form_numb;
    String TAG = "find";
    String id;
    String pw;


    public AutoLoginAsync(WebView webView, String member_form_numb,String id, String pw){
        this.webView = webView;
        this.member_form_numb = member_form_numb;
        this.id = id;
        this.pw = pw;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        webView.loadUrl("Javascript:(document.getElementById('member_passwd')).setAttribute('value', '"+pw+"')");
//        webView.loadUrl("Javascript:(document.getElementById(\"member_id\")).focus();");
        webView.loadUrl("Javascript:(document.getElementById('member_id')).setAttribute('type', 'hidden')");
        webView.loadUrl("Javascript:(document.getElementById('member_id')).setAttribute('value', 'dkssudnjsdn')");
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Instrumentation instrumentation = new Instrumentation();
//        instrumentation.sendStringSync("dkssudnjsdn"); // 특수문자도 입력가능!
//        webView.loadUrl("Javascript:(document.getElementById('member_id')).setAttribute('type', 'hidden')");
//        webView.loadUrl("Javascript:(document.getElementById('member_id')).setAttribute('value', 'dkssudnjsdn')");
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        webView.loadUrl("Javascript:MemberAction.login('"+member_form_numb+"');"); //로그인 버튼
        Log.d(TAG, "onPostExecute: 로그인 메서드 : "+"Javascript:MemberAction.login('"+member_form_numb+"');");
    }
}
