package com.example.choiww.getstyle_1.AdminMode;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.example.choiww.getstyle_1.Adapter_userList;
import com.example.choiww.getstyle_1.DataClass.userInfoData;
import com.example.choiww.getstyle_1.R;
import com.example.choiww.getstyle_1.news_ex1;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserManagingActivity extends AppCompatActivity {

    ArrayList userList_arraylist;
    RecyclerView userManaging_userList_recyclerview;
    Adapter_userList adapter_userList;
    LinearLayoutManager linearLayoutManager;
    news_ex1 connector;

    Spinner userSearchFilter_spinner;
    ArrayList<String> spinner_array;
    ArrayAdapter spinnerAdapter;
    EditText insertKeyword_editText;
    ImageView findUser_img;

    int spinnerValue = 0;// 스피너 선택 값
    String TAG = "find";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_managing);

//        Log.d(TAG, "onCreate: 코드진행이 왜안돼?0");

        userSearchFilter_spinner = findViewById(R.id.userManaging_userSearchFilter_spinner);
        spinner_array = new ArrayList<>();
        spinner_array.add("전체");
        spinner_array.add("ID");
        spinner_array.add("이름");
        spinner_array.add("닉네임");
        spinnerAdapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, spinner_array);
        userSearchFilter_spinner.setAdapter(spinnerAdapter);
        userSearchFilter_spinner.setSelection(0);

        userSearchFilter_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerValue = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        insertKeyword_editText = findViewById(R.id.userManaging_insertKeyword_editText);


        findUser_img = findViewById(R.id.userManaging_findUser_img);
        findUser_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                connector = news_ex1.retrofit.create(news_ex1.class);
//        Log.d(TAG, "onCreate: 왜 코드진행이 안된겨?");
                Call<ArrayList<userInfoData>> call = connector.getUserInfo(insertKeyword_editText.getText().toString(), spinnerValue);
                call.enqueue(new Callback<ArrayList<userInfoData>>() {
                    @Override
                    public void onResponse(Call<ArrayList<userInfoData>> call, Response<ArrayList<userInfoData>> response) {
                        adapter_userList.arrayList = response.body();
                        adapter_userList.notifyDataSetChanged();

                    }

                    @Override
                    public void onFailure(Call<ArrayList<userInfoData>> call, Throwable t) {

                    }
                });
            }
        });

        connector = news_ex1.retrofit.create(news_ex1.class);
//        Log.d(TAG, "onCreate: 왜 코드진행이 안된겨?");
        Call<ArrayList<userInfoData>> call = connector.getUserInfo();
        call.enqueue(new Callback<ArrayList<userInfoData>>() {
            @Override
            public void onResponse(Call<ArrayList<userInfoData>> call, Response<ArrayList<userInfoData>> response) {
                Log.d(TAG, "onResponse: 서버 회원정보 요청성공 코드 : "+response.code());

                userList_arraylist = response.body();
                Log.d(TAG, "onResponse: userList : "+userList_arraylist.toString());
                userManaging_userList_recyclerview = findViewById(R.id.userManaging_userList_recyclerview);
                adapter_userList = new Adapter_userList(getApplicationContext(),userList_arraylist);
                linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                userManaging_userList_recyclerview.setLayoutManager(linearLayoutManager);
                userManaging_userList_recyclerview.addItemDecoration(new DividerItemDecoration(getApplicationContext(), 1));
                userManaging_userList_recyclerview.setAdapter(adapter_userList);
            }

            @Override
            public void onFailure(Call<ArrayList<userInfoData>> call, Throwable t) {
                Log.d(TAG, "onFailure: 서버에 회원정보 요청 실패 : 코드 "+ t);

            }
        });
        // 서버에 요청,받은값을 recyclerview에
        // 검색어 입력후 검색버튼을 누르면, 필터값과 검색어를 함께 서버에 보내어 요청

//        Log.d(TAG, "onCreate: 왜 코드진행이 안돼는거야?2");



    }
}
