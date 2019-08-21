package com.example.choiww.getstyle_1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.util.LogPrinter;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.regex.Pattern;

import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
    /*
    목적 : 회원가입을 시켜준다.

    시나리오 :
        1. 사용자에게 정보를 입력받는다.(메일,이름,닉네임,비번)
        2. submit을 누르면 각각의 정보가 규격에 맞는지 체크한다.
         2-1. 동일한 아이디가 있는지, 아이디에 '@' 이 포함되는지
         2-2. 비번에 영어,숫자 가 들어갔는지 체크
     */

public class join extends AppCompatActivity {

    EditText et_email;
    EditText et_name;
    EditText et_nickname;
    EditText et_passwd;
    TextView bt_submit;
    Retrofit retrofit;
    String TAG = "find";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        et_email = findViewById(R.id.joinMail);
        et_name = findViewById(R.id.joinName);
        et_nickname = findViewById(R.id.joinNickname);
        et_passwd = findViewById(R.id.joinPasswd);
        bt_submit = findViewById(R.id.joinSubmit);

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputEmail = String.valueOf(et_email.getText());
                String inputName = String.valueOf(et_name.getText());
                String inputNickname = String.valueOf(et_nickname.getText());
                String inputPasswd = String.valueOf(et_passwd.getText());

                // 공백이 있는지 검사, ((나중에?)띄어쓰기 있는지 검사)
                if(inputEmail.length()==0 || inputName.length()==0 || inputNickname.length()==0 || inputPasswd.length()==0){ // 안쓴 항목이 있다면
                    Toast.makeText(getApplicationContext(),"입력하지 않은 항목이 있습니다.",Toast.LENGTH_SHORT).show();
                    Log.d("find"," email : "+inputEmail.length()+" name : "+inputName.length()+" nickname : "+inputNickname.length()+" passwd : "+inputPasswd.length());
                    return;
                }else{ // 안쓴 항목이 없다면
                    Log.d("find", "공백항목 없음");
                }

                // inputEmail 이 형식을 갖췄는지 검사 코드, 중복검사(서버)
                /** @@ 서버-db에서 아이디 중복검사를 하고 그 반환값(boolen)으로 결과값을 받아온다. **/
                boolean isCheckedEamil = Patterns.EMAIL_ADDRESS.matcher(inputEmail).matches();//이메일형식 체크해 불린값 반환
                if (isCheckedEamil){
                    Log.d("find", "onClick: 메일유효성 검사 이상무 "+inputEmail);
                }else{
                    Toast.makeText(getApplicationContext(),"이메일 형식이 맞지 않습니다",Toast.LENGTH_SHORT).show();
                    et_email.setText("");
                    return;
                }

                // inputPasswd 가 영어,숫자 가 들어가 있는가,(나중에 가능한 특수문자만 썼는 지 검사)
                boolean isCheckedPasswd = Pattern.matches("^[a-zA-Z0-9]+$",inputPasswd);//숫자와 알파뱃만 가능
                //정규식을 정확히 이해못함.. 나중에 체크 필요   링크  -  http://blog.naver.com/PostView.nhn?blogId=suda552&logNo=220813122485 , https://gocoding.tistory.com/93
                if (isCheckedPasswd){
                    Log.d("find","onClick : 비밀번호 이상무");
                }else {
                    Toast.makeText(getApplicationContext(),"비밀번호 형식에 맞지 않습니다.",Toast.LENGTH_SHORT).show();
                    et_passwd.setText("");
                    return;
                }

                    // 아닐시 토스트를 띄우고, 해당 editText를 지운다.

                    // 맞다면 서버에 회원정보 저장,
                news_ex1 join_interface = news_ex1.retrofit.create((news_ex1.class));
                retrofit2.Call<List<boolenData>> call = join_interface.joinNewUser(inputEmail,inputName,inputNickname,inputPasswd);
                Log.d("find", "onClick: 레트로핏 전까지는 온겨"+inputEmail +" , "+inputName +" , "+inputNickname+" , "+inputPasswd);
                call.enqueue(new Callback<List<boolenData>>() {
                    @Override
                    public void onResponse(retrofit2.Call<List<boolenData>> call, Response<List<boolenData>> response) {
                        Log.d("find", "onResponse: response = "+response.toString());
//                        if ("true".equals(response.body().get(0).isaBoolean())){
//                            Log.d("find", "onResponse: 정상 가입 완료 "+response.body().get(0).isaBoolean());
//
//                        }else{
//                            Log.e("find", "문제 발생(db 에서 반환된 값을 php가 처리하는 과정을 확인할것 "+response.body().get(0).isaBoolean());
//                        }
                        Log.d("find", "onResponse:  "+response.body().get(0).isaBoolean());
//                        Log.d("find", "onResponse:  "+response.body().get(1).isaBoolean());
//                        Log.d("find", "onResponse:  "+response.body().get(2).isaBoolean());
                        String resposeFine = response.body().get(0).isaBoolean();
                        Log.d("find", "isfind = "+resposeFine);
                        if ("true".equals(resposeFine)){
                            Log.d(TAG, "onResponse: 서버와의 소통은 됬거지 , json 도 문제 없지 ... mysql에 데이터 넣는게 문제였네");
                            Log.d(TAG, "onResponse: get으로보낸값은 잘갔나? : "+response.body().get(1).isaBoolean());
                        }else {

                        }
                    }

                    @Override
                    public void onFailure(retrofit2.Call<List<boolenData>> call, Throwable t) {
                        Log.d("find", "통신시 문제 발생 주소확인 or 값의 key 값 일치여부 확인할것");
                        Log.d("Retrofit Error", t.toString());
                        return;
                    }
                });
                Log.d("find", "끝까지 옴");
                    // 로그인 페이지로 이동
//                Intent intent = new Intent(getApplicationContext(), main.class);
//                ///([추후] 아이디 값을 같이 넘겨줄 수 도 있겠지만, 혹은 바로 로그인되어 메인으로 갈 수 도 있겠지만)
//                startActivity(intent);

            }
        });

    }
}
