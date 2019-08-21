package com.example.choiww.getstyle_1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;

/*
목적 : 로그인과 회원가입 회면으로 이동 ( 자동로그인은 로딩화면 onpause에서 체크하는 걸로 )

시나리오 :
    1. 아이디,비밀번호를 사용자가 입력한다.
    2. 로그인 버튼을 누르면 아이디와 비밀번호가 맞는지 체크한다.(보안을 어떻게 해야하지?)
    3. if 아이디비번이 일치하면 로그인 시킨다.
       if 아이디비번이 일치하지 않으면 "아이디 비밀번호가 일치하지 않습니다."라고 토스트를 띄워준다.
    4. join버튼을누르면 회원가입 페이지로 이동시킨다.

 */
public class login extends AppCompatActivity {
    EditText et_loginMail;
    EditText et_loginPassword;
    TextView bt_login;
    TextView bt_join;
    // 네이밍 규칙 찾아보기

    @Override
    protected void onCreate(Bundle savedInstanceState) { // 보통 메소드명과 변수명 이 비슷하다.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_loginMail = findViewById(R.id.login_mail);
        et_loginPassword = findViewById(R.id.login_password);
        bt_login = findViewById(R.id.login_bt);
        bt_join = findViewById(R.id.join_bt);
        Log.d("find", " login 엑티비티 시작");

        bt_login.setOnClickListener(new View.OnClickListener() {
            // 2 . 아이디 비밀번호 입력 후 로그인 버튼을 누를시
            @Override
            public void onClick(View v) {
                final String inputEmail = String.valueOf(et_loginMail.getText());
                String inputPasswd = String.valueOf(et_loginPassword.getText());


                //여기에 서버-db에 email 과 passwd가 매칭되는 값이 있는지 확인한다.(보안은..? 나중에)
                news_ex1 login_interface = news_ex1.retrofit.create(news_ex1.class);
                retrofit2.Call<List<userInfoForLogin>> call = login_interface.loginConfirm(inputEmail,inputPasswd);
                //2-1. 아이디 버번을 서버로 전송하여 회원정보일치여부 확인요청을 보낸다.
                call.enqueue(new Callback<List<userInfoForLogin>>() {
                    @Override
                    public void onResponse(retrofit2.Call<List<userInfoForLogin>> call, Response<List<userInfoForLogin>> response) {

                        // 입력한 아이디 비번이 회원정보와 일치하는지 여부를 편별해 로그인 후 메인화면으로 이동시킨다.

                        boolean isCanLogin;     // 회원정보 일치여부를 담는 변수 // @ 좋은 사용의 예시
//                        Log.d("find", "respose length : "+ response.body().size());
                        if (response.body().size() == 0){
                            //3-1-1. 아이디 번이 일치하지 않을경우
                            Log.d("find", "아이디 비밀번호가 일치하지 않습니다.");
                            Toast.makeText(getApplicationContext(),"아이디 비번번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show();
                            // 3-1-2. "아이디 비밀번호가 일치하지 않습니다." 토스트 메시지르 띄워준다
                            et_loginPassword.setText("");
                            // 3-1-3.틀린 비밀번호를 지워준다.
                            return;
                        }

                            if ((response.body().get(0).getUserEmail()).equals(inputEmail)){
                                // 3-2-1. 회원정보가 일치할 경우
                                isCanLogin = true;
                                Log.d("find" , response.body().get(0).getUserEmail());
                                // 접속이 제한된 회원일경우 (관리자가 접속제한을 건 회원이 로그인을 시도하면 접속이 제한되었다고 알려준다.)
                                if (response.body().get(0).getUser_status()==1){
                                    Toast.makeText(getApplicationContext(),"부적절한 사용으로 인해 접속이 제한된 계정입니다. 관리자에게 문의해주세요",Toast.LENGTH_SHORT).show();
                                    return;
                                }

                            }else {
                                // 3-2-2. 회원정보가 일치하지 않을 경우 ( 만약을 대비 : -)
                                isCanLogin = false;
                                Log.d("find", "false");
                            }

                        // (isCanLogin)돌아온 값이 true 면 일치, false 면 불일치다

                        if (isCanLogin){
                            //3-2-2. 일치할 경우 로그인을 시키고 main으로 화면을 전환 시킨다.
//                            Log.d("find","find, true가 어떻게 난겨?");
                            Intent intent = new Intent(getApplicationContext(), main.class);
                            intent.putExtra("userId", response.body().get(0).getUserId());
//                            Log.d("find", "response 받은 userid = "+response.body().get(0).getUserId());
                            intent.putExtra("userName", response.body().get(0).getUserName());
                            intent.putExtra("userNickname", response.body().get(0).getUserNickname());
                            intent.putExtra("userEmail", response.body().get(0).getUserEmail());
//                            intent.putExtra("test", "test ok");
                            // 다음엑티비티로 현재 로그인한 유저의 데이터(아이디,이름,닉네임)을 같이 보내준다.
                            startActivity(intent);
                            finish();//현재 엑티비티 종료 메서드                   //링크  - https://koreamin.tistory.com/entry/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C-%ED%98%84%EC%9E%AC-%EC%95%A1%ED%8B%B0%EB%B9%84%ED%8B%B0-%EB%8B%A4%EB%A5%B8-%EC%95%A1%ED%8B%B0%EB%B9%84%ED%8B%B0-%EC%A2%85%EB%A3%8C%ED%95%98%EA%B8%B0

                        }else{
                            //3-2-3. 일치하지 않을경우 비밀번호를 지워준다. 토스트 메시지를 띄운다 "이메일과 비밀번호가 일치하지 않습니다"
                            Toast.makeText(getApplicationContext(),"아이디 비번번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show();
                            et_loginPassword.setText("");//틀린 비밀번호를 지워준다.
                            return; // 코드진행 중지
                        }

                    }

                    @Override
                    public void onFailure(retrofit2.Call<List<userInfoForLogin>> call, Throwable t) {
                        // userInfoForLogin -> UserInfoForLogin ( 클레스 )  => 네이밍 찾아보기
                        // 서버에 요청 보내기를 실패했을 경우 (레트로핏요청을 실패했을 경우)
                        Log.e("find", "onFailure: Retrofit error "+t.toString());
                        Toast.makeText(getApplicationContext(),"네트워크가 원활하지 못해 로그인정보를 받아올 수 없습니다.",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        bt_join.setOnClickListener(new View.OnClickListener() {
            //4. 회원가입 버튼을 누르면 회면가입 화면으로 이동한다.
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), join.class);
                startActivity(intent);
                finish();
            }
        });


    }
}
