package com.example.choiww.getstyle_1.AdminMode;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.choiww.getstyle_1.DataClass.MallOrderListDate;
import com.example.choiww.getstyle_1.R;
import com.example.choiww.getstyle_1.mallUpdateDate;
import com.example.choiww.getstyle_1.news_ex1;
import com.google.gson.Gson;
//import

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.constraint.Constraints.TAG;

/**
     * 목적 : 관리자가 제휴쇼핑몰 관리하기위한 엑티비티이다.
     *         '최신상품(main)'화면에 보여지는 쇼핑몰의 순서를 바꾸거나, 안보이고&보이게 할 수 있다.
     * 시나리오 :
     *      a. 화면표시 순서를 변경하고자 할때
     *          1. '변경'버튼을 누른다.
     *          2. '변경'버튼이 '저장'버튼으로 바뀌고, 쇼핑몰의 순서값이 변경할 수 있게 editText로 바뀐다.
     *          3. 변경하고자 하는 쇼핑몰의 순서값을 변경한다.
     *              (동일한 순서값을 입력했을 시 예외처리 필요)
     *          4. '저장'버튼을 누른다.
     *          5. '저장'버튼일 '변경'으로 바뀌고, 변경된 순서값으로 쇼핑몰이 정렬되어 표시된다.
     *      b. 화면표시 쇼핑몰에서 제외하기
     *          1. "변경"버튼을 누른다.
     *          2. 쇼핑몰이름들 옆에 숨기기 버튼이 표시된다.
     *          3. 숨기기 버튼을 클릭하면 버튼의 색이 바뀐다.(취소하려면 한번더 숨기기 버튼을 누른다.)
     *          4. '저장'버튼을 누르면 숨기기 버튼을 누른 쇼핑몰은 화면에서 사라진다.
     *             (숨긴 쇼핑몰들은 '숨긴 쇼핑몰'화면에 가면 볼 수 있다.)
     *      c. 숨겨진 쇼핑몰 다시보이게 하기
     *          1. 제휴 쇼핑몰 곤리 화면하단의 '숨긴 쇼핑몰'버튼을 누르면 숨긴 쇼핑몰을 볼 수 있는 화면으로 넘어간다.
     *          2. 숨기기 버튼을 누르면 '보이기'버튼 옆에 editText가 나타나고, 여기에 순서값을 입력한다.
     *          3. 저장버튼을 누른다.
     *          4. 재휴 쇼핑몰 관리 화면으로 이동하고, 변경된 순서값이 반영되어 보여진다.
     *
     * */
public class AffiliateMallAdminActivity extends AppCompatActivity {
    /*서버에서 mall_order 테이블에서 각 쇼핑몰의 순서값을 받아와 mall_order_arraylist에 넣는다.
            (ex : [빈티지톡, 빈티지언니, 세컨드]
              받아오려면 retrofit으로 서버에 값 보내고 (dbHelper 에 메소드 만들어야함 )
              php파일도 만들어야하고
              값을 받아오기 위한 데이터 클래스도 만들어야한다.

        * mall_order_rraylist을 recyclerview에 보여준다.
        * 변경버튼을 누르면
        * 순서 textview를 gone, 순서를 입력할 editText와 숨기기버튼을 visible 한다.
        *   빈티지 언니의 순서를 editText에 입력한다.(2->1)
        *   빈티지 언니의 id를 HashMap에 입력한 순서를 key로 넣어놓는다.
        *   저장을 누른다.
        *   new_mall_order_arraylist를 만든다.
        *   순서값이 0이 아닌(숨기지 않은) 항목들의 개수를 구한다.
        *   그 개수만큼 반복문을 돌린다.
        *       new_mall_order_arraylist에 값 반복적으로 넣는다.
        *       만약 index 순서대로 new_mall_order_arraylist에 값을 넣으려 할때
        *       해당 인덱스의 키값에 메칭되는 hashMap값이 없다면
        *       기존의 mall_order_arraylist의 첫번째 값을 넣는다.
        *       그리고 첫번째값을 지운다.
        *   이렇게 반복문이 끝나게 되면 모든 사용자가 지정한 순서대로 mall순서가 정렬되게된다.
        *   이 값을 어뎁터에 넣어주고 nofitydateSetChanged를 날려 화면을 갱신한다.
        *   서버에 new_mall_order_arraylist를 업로드한다.
        *
        * */

    String TAG = "find";

    ArrayList<MallOrderListDate> mallOrderList;
    RecyclerView affiliateMallAdmin_recyclerview;
    Adapter_MallAdminEditor mAdapter;
    Button affiliateMallAdmin_changeOrder_btn;
    Button affiliateMallAdmin_goHideAffiliateMall_btn;
    Button affiliateMallAdmin_changeHide_btn;
    Gson gson;
    news_ex1 news_ex1;

    @Override
    protected void onRestart() {
        super.onRestart();
        recreate();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affiliate_mall_admin);


        news_ex1 = com.example.choiww.getstyle_1.news_ex1.retrofit.create(com.example.choiww.getstyle_1.news_ex1.class);
        final Call<ArrayList<MallOrderListDate>> call = news_ex1.getAdminMallOrderList();
        call.enqueue(new Callback<ArrayList<MallOrderListDate>>() {
            @Override
            public void onResponse(Call<ArrayList<MallOrderListDate>> call, Response<ArrayList<MallOrderListDate>> response) {
                int response_size = response.body().size();
                Log.d(TAG, "onResponse: response.body().size()= "+response_size);
//                Log.d(TAG, "onResponse: "+response.body().get(1).getKor_name());
                mallOrderList = response.body();
                //여기까진 된다,.
//                for (int i=0;response_size>i;i++){
//
////                    mallOrderList.add(response.body().get(i));
////                    mallOrderList = response.body().get(i);
//
//                }

                affiliateMallAdmin_recyclerview = findViewById(R.id.affiliateMallAdmin_recyclerview);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                affiliateMallAdmin_recyclerview.setLayoutManager(linearLayoutManager);
                mAdapter = new Adapter_MallAdminEditor(getApplicationContext(),mallOrderList);
                affiliateMallAdmin_recyclerview.setAdapter(mAdapter);


            }

            @Override
            public void onFailure(Call<ArrayList<MallOrderListDate>> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.toString());
            }
        });

        affiliateMallAdmin_changeOrder_btn = findViewById(R.id.affiliateMallAdmin_changeOrder_btn); // 변경, 저장버튼
        affiliateMallAdmin_changeOrder_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 변경 버튼을 누르면 변경버튼일 저장버튼으로 바뀌고
                // 순번이 editText로 바뀌고, 숨기기 버튼일 보여한다.
                //


                if(!mAdapter.isClickChange){
                    mAdapter.isClickChange = true;// 변경버튼을 누르면
                    affiliateMallAdmin_changeOrder_btn.setText("순서저장");
//                    mAdapter.isClickChange = true;

                }else {
//                    mAdapter.isClickChange = false;// 저장버튼을 누르면
//                    affiliateMallAdmin_changeOrder_btn.setText("변경");
                    Log.d(TAG, "onClick: hideButtonCheckList 의 길이 : "+mAdapter.hideButtonCheckList.size());
                    Log.d(TAG, "onClick: hideButoonCheckList to string : "+ mAdapter.hideButtonCheckList.toString());
                    Log.d(TAG, "onClick: arraylist의 쇼핑몰 순서 1 : "+mAdapter.arrayList.get(0).getKor_name());
                    Log.d(TAG, "onClick: arraylist의 쇼핑몰 순서 2 : "+mAdapter.arrayList.get(1).getKor_name());
                    Log.d(TAG, "onClick: arraylist의 쇼핑몰 순서 3 : "+mAdapter.arrayList.get(2).getKor_name());
                    for (int i=0;mAdapter.hideButtonCheckList.size()>i;i++){
                        //저장버튼을 누르면 숨기기를 눌렀던 쇼핑몰이 어떤 쇼핑몰인지를 감지하는 부분
                        if(mAdapter.hideButtonCheckList.get(i)){
                            Log.d(TAG, "onClick: 숨김한 쇼핑몰 이름 : "+mAdapter.arrayList.get(i).getKor_name()+", "+i+"번째");
//                            mAdapter.arrayList.remove()
                            mAdapter.arrayList.remove(i);
                            //이상한 값이 지워지는 이유는 remove로 인해 변경되 index를 반영하지 못하고 고정된 index 값의 데이터를 지우기 때문에 하나씩 계속 밀리는 현상이 발생한다.
                        }
                        //for 문이 끝나면 mAdapter.arraylist엔 숨기기 완료한 쇼핑몰 목록만 남게 된다.
                        //이걸 서버에 보내서 저장시키면 된다.

//                        Gson gson = new Gson();
//                        String str_editedMallArraylist = gson.toJson(mAdapter.arrayList);
                    }

//                    if (mAdapter.saveChangeMap.size()+mAdapter.editedArraylist.size() != mAdapter.arrayList.size()){
//                        Toast toast = Toast.makeText(getApplicationContext(), "중복되는 순번이 있습니다 확인해 주세요", Toast.LENGTH_LONG); toast.show();
//                        return;
//                    }
                    //저장을 누르면 순서를 입력 한 값들이 반영되어야한다.
                    // 일력후 포커스 아웃 되었을때 그값을 받아왔어야하나.?
//                    mAdapter.
                    MallOrderListDate[] editedList = new MallOrderListDate[mAdapter.arrayList.size()];
                    Log.d(TAG, "onClick: mAdapter.arrayList.size()"+mAdapter.arrayList.size());
                    Log.d(TAG, "onClick: editedList 길이 = "+editedList.length);
                    int editedArrayIndex = 0;
                    for (int i=0;mAdapter.arrayList.size()>i;i++){
                        Object changedNumb;
                        try {
                            changedNumb = mAdapter.saveChangeMap.get(i);
                        }catch (NullPointerException e){
                            Log.e(TAG, "onClick: ",e );
                            changedNumb = null;
                        }catch (IndexOutOfBoundsException e){
                            Log.e(TAG, "onClick: ",e );
                            changedNumb = null;
                        }
                        if (changedNumb!=null){
                            //값이 있으면
                            Log.d(TAG, "onClick: "+i+"을" +changedNumb+" 로 변경");
                            editedList[Integer.parseInt(changedNumb.toString())] = mAdapter.arrayList.get(i);
                        }else {
                            //값이 없으면
//                            Log.d(TAG, "onClick: "+i+"는 "+mAdapter.editedArraylist.get(0).getMall_order()+"로 변경");
//                            editedList[]
                        }
//                        else {
//                            //없으 없으면
//                            mAdapter.editedArraylist.get(editedArrayIndex);
//                            editedList[Integer.parseInt(changedNumb)]
//                        }

                    }
                    for (int i=0;mAdapter.arrayList.size()>i;i++){
                        Log.d(TAG, "onClick: editedList.length =  "+editedList.length);
                        if (editedList[i]==null){
                            Log.d(TAG, "onClick: editedArrayIndex 는 "+editedArrayIndex);
                            try {
                                editedList[i] = mAdapter.editedArraylist.get(editedArrayIndex);
                                editedList[i].setMall_order((i+1)+"");// 순번 변경한 쇼핑몰 말거 나머지에 변경된 순서를 적용해준다.
                                Log.d(TAG, "onClick: 지금 넣는 쇼핑몰 이름 : "+editedList[i].getKor_name()+", mall_order = "+editedList[i].getMall_order());

                            }catch (IndexOutOfBoundsException e){
                                Toast toast = Toast.makeText(getApplicationContext(), "중복되는 순번이 있습니다 확인해 주세요", Toast.LENGTH_LONG); toast.show();
                                Log.e(TAG, "onClick: 중복되는 순번 입력함",e );
                                return;
                            }

                            // 3개중 2개를 수정해서 editedArraylist엔 1개 밖에 없는데 2번째 값을 빼려하니 에러가 난다.
//                            Log.d(TAG, "onClick: "+i+"번째는 "+editedList[i].getKor_name());
                            editedArrayIndex++;
                            // 쇼핑몰에 순서를 수정하게 되면 arraylist에서 해당 쇼핑몰을 지운다.
                            // 그리고 hashMap에 '기존index-바꿀index'로 값을 저장한다.
                            // 이러한 흐름속에서 같은 두개의 쇼핑몰에 같은 값을 순번으로 수정하게 되면 arraylist에서는 두곳을 모두 지우고
                            // hashMap에는 (두개의 값이 들어 있는게 아닌) 같은 값에 덮어쓰게 되어 1개의 값밖에 안들가 있는다.
                            // 다시 회면에 표시해줄때는 hashMap에 값이 없으면 (수정시 값을 지우던)arraylist에서 순서대로 값을 가져와 넣게 되어있는데
                            // hashmap에는 값이 하나만 저장되어 있으니 나머지는 arraylist에서 값을 가져와야한다.
                            // 하지만 두곳을 수정했음으로 arraylist엔 값이 하나 비게 된다.
                            // 그래서 IndexOutOfBoundsException 에러가 뜨게 되는 것이다.
                        }else {
                            editedList[i].setMall_order((i+1)+"");//순번을 변경한 쇼핑몰 데이터의 mall_order를 변경해준다.
                            Log.d(TAG, "onClick: 지금 넣는 쇼핑몰 이름 : "+editedList[i].getKor_name()+", mall_order = "+editedList[i].getMall_order());
                        }
                    }
                    ArrayList newArrayList = new ArrayList(Arrays.asList(editedList));
                    ArrayList newArrayList1 = new ArrayList(Arrays.asList(editedList));
//                    ArrayList newArrayList = Arrays.asArrayList(editedList);
                    Log.d(TAG, "onClick: 어뎁터의 어레이리스트 수정값으로 변경 : "+newArrayList.size());
                    mAdapter.arrayList = newArrayList;
                    mAdapter.editedArraylist = newArrayList1;
                    mAdapter.saveChangeMap = new HashMap();
                    //변경사항 적용, 초기화

                    // 서버에 저장할 수 있게 해야한다.
                    // json 으로 변환 후 서버로 보낸다.
                    gson = new Gson();
                    String str_editedList = gson.toJson(editedList);
                    Log.d(TAG, "onClick: 내 서버에 보낼 쇼핑몰 순번 값은 : "+str_editedList);
                    Call<ResponseBody> call = news_ex1.sendAdminMallOrderList(str_editedList);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            Log.d(TAG, "onResponse: 응답옴 , 코드 : "+response.code() );
//                            response.code()
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.d(TAG, "onFailure:  실패"+t.toString() );
                        }
                    });

                    mAdapter.isClickChange = false;// 저장버튼을 누르면
                    affiliateMallAdmin_changeOrder_btn.setText("순서변경");
                    recreate();
                }

                mAdapter.notifyDataSetChanged();

            }
        });
        //새로만든 숨기기 전용 버튼
        affiliateMallAdmin_changeHide_btn = findViewById(R.id.affiliateMallAdmin_changeHide_btn);
        affiliateMallAdmin_changeHide_btn.setText("숨기기 변경");
        affiliateMallAdmin_changeHide_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: isClickHide = "+mAdapter.isClickHide);
                if (!mAdapter.isClickHide){
                    affiliateMallAdmin_changeHide_btn.setText("숨기기 저장");
                    mAdapter.isClickHide = true;
                    mAdapter.notifyDataSetChanged();
                }else {
                    Log.d(TAG, "onClick: hideButtonCheckList 의 길이 : "+mAdapter.hideButtonCheckList.size());
                    Log.d(TAG, "onClick: hideButoonCheckList to string : "+ mAdapter.hideButtonCheckList.toString());
//                    Log.d(TAG, "onClick: arraylist의 쇼핑몰 순서 1 : "+mAdapter.arrayList.get(0).getKor_name());
//                    Log.d(TAG, "onClick: arraylist의 쇼핑몰 순서 2 : "+mAdapter.arrayList.get(1).getKor_name());
//                    Log.d(TAG, "onClick: arraylist의 쇼핑몰 순서 3 : "+mAdapter.arrayList.get(2).getKor_name());

                    int editedIndexByHide = 1;
                    for (int i=0;mAdapter.hideButtonCheckList.size()>i;i++){
                        //저장버튼을 누르면 숨기기를 눌렀던 쇼핑몰이 어떤 쇼핑몰인지를 감지하는 부분
                        if(mAdapter.hideButtonCheckList.get(i)){
                            Log.d(TAG, "onClick: 숨김한 쇼핑몰 이름 : "+mAdapter.arrayList.get(i).getKor_name()+", "+i+"번째");
//                            mAdapter.arrayList.remove()
                            //이상한 값이 지워지는 이유는 remove로 인해 변경되 index를 반영하지 못하고 고정된 index 값의 데이터를 지우기 때문에 하나씩 계속 밀리는 현상이 발생한다.
                            mAdapter.arrayList.get(i).setMall_order("0");

                        }else {
                            mAdapter.arrayList.get(i).setMall_order(editedIndexByHide+"");
                        }
                        Log.d(TAG, "onClick: 현재 순번값 : "+i+" : "+mAdapter.arrayList.get(i).getKor_name());
                    }
                    gson = new Gson();
                    String str_editedOrderList = gson.toJson(mAdapter.arrayList);
                    Call call1 = news_ex1.sendAdminMallOrderList(str_editedOrderList);
                    call1.enqueue(new Callback<ArrayList<MallOrderListDate>>() {
                        @Override
                        public void onResponse(Call<ArrayList<MallOrderListDate>> call, Response<ArrayList<MallOrderListDate>> response) {
                            Log.d(TAG, "onResponse: 응답옴 , 코드 : "+response.code() );
                        }

                        @Override
                        public void onFailure(Call<ArrayList<MallOrderListDate>> call, Throwable t) {
                            Log.d(TAG, "onFailure:  실패"+t.toString() );
                        }
                    });
                    recreate();
                }




            }
        });

        // 숨긴 쇼핑몰 보기 버튼
        affiliateMallAdmin_goHideAffiliateMall_btn = findViewById(R.id.affiliateMallAdmin_goHideAffiliateMall_btn);
        affiliateMallAdmin_goHideAffiliateMall_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HideAffiliateMallAdminActivity.class);
                gson = new Gson();
                String str_mllOrderList = gson.toJson(mAdapter.arrayList);
                intent.putExtra("mallOrderList" ,str_mllOrderList);
                startActivity(intent);
            }
        });

//        MallOrderListDate mallOrderListDate = new MallOrderListDate();


    }
}

class Adapter_MallAdminEditor extends RecyclerView.Adapter<Adapter_MallAdminEditor.itemViewHolder>{

    Context mcontext;
    ArrayList<MallOrderListDate> arrayList;
    ArrayList<MallOrderListDate> editedArraylist;
    ArrayList<Boolean> hideButtonCheckList = new ArrayList<>();
    Boolean isClickChange = false;
    Boolean isClickHide = false;
    Boolean isClickHideMalls = null;
//    ArrayList editedMallOrderList = new ArrayList();
//    MallOrderListDate[] editedMallOrderList = new MallOrderListDate[6];
    HashMap saveChangeMap = new HashMap();
    HashMap saveHideChangeMap = new HashMap();
    ArrayList<Boolean> isShowBtnClick = new ArrayList<Boolean>();

    class itemViewHolder extends RecyclerView.ViewHolder{
        TextView mallAdminitem_mallName_tx;
        TextView mallAdminitem_mallOrder_tx;
        EditText mallAdminitem_mallOrderEdit_ex;
        Button mallAdminitem_HideOrVisibleButton_btn;
        Button mallAdminitem_showHideMall_btn;



        public itemViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mallAdminitem_mallName_tx = itemView.findViewById(R.id.mallAdminitem_mallName_tx);
            this.mallAdminitem_mallOrder_tx = itemView.findViewById(R.id.mallAdminitem_mallOrder_tx);
            this.mallAdminitem_mallOrderEdit_ex = itemView.findViewById(R.id.mallAdminitem_mallOrderEdit_ex);
            this.mallAdminitem_HideOrVisibleButton_btn = itemView.findViewById(R.id.mallAdminitem_HideOrVisibleButton_btn);
            this.mallAdminitem_showHideMall_btn = itemView.findViewById(R.id.mallAdminitem_showHideMall_btn);

        }
    }

    public Adapter_MallAdminEditor(Context context, ArrayList<MallOrderListDate> arrayList){
        this.mcontext = context;
        this.arrayList = arrayList;
        this.editedArraylist = new ArrayList<>(arrayList);

        for (int i=0;arrayList.size()>i;i++){
            hideButtonCheckList.add(false);
            this.isShowBtnClick.add(false);
        }


    }

    @NonNull
    @Override
    public itemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.admin_malladmin_item, viewGroup, false);
        itemViewHolder viewHolder = new itemViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final itemViewHolder viewHolder, final int i) {
        Log.d(TAG, "onBindViewHolder: 여기가 작동하는중");
        viewHolder.mallAdminitem_mallName_tx.setText(arrayList.get(i).getKor_name());
//        viewHolder.mallAdminitem_mallOrder_tx.setText((arrayList.get(i).getMall_order())+"");// array의 인덱스값+1을 표시순번으로서 표시한다.\
        viewHolder.mallAdminitem_mallOrder_tx.setText((i+1)+"");
        viewHolder.mallAdminitem_mallOrderEdit_ex.setText(arrayList.get(i).getMall_order());

        setHideButton(viewHolder,i); // 숨기기 버튼을 클릭했는 지 안했는 지 색으로 표시
        if (!isClickChange){// 변경 버튼을 누르면 변경할 수 있는 view 가 보이고,
            viewHolder.mallAdminitem_mallOrder_tx.setVisibility(View.VISIBLE);
            viewHolder.mallAdminitem_mallOrderEdit_ex.setVisibility(View.GONE);
            viewHolder.mallAdminitem_HideOrVisibleButton_btn.setVisibility(View.GONE);
            viewHolder.mallAdminitem_showHideMall_btn.setVisibility(View.INVISIBLE);
        }else { // 저장을 누르면 다시 원래 화면으로 돌아온다.
            viewHolder.mallAdminitem_mallOrder_tx.setVisibility(View.GONE);
            viewHolder.mallAdminitem_mallOrderEdit_ex.setVisibility(View.VISIBLE);
            viewHolder.mallAdminitem_mallOrderEdit_ex.setTextColor(Color.GRAY);
            viewHolder.mallAdminitem_showHideMall_btn.setVisibility(View.INVISIBLE);
            viewHolder.mallAdminitem_mallOrderEdit_ex.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    String insertedOrder = viewHolder.mallAdminitem_mallOrderEdit_ex.getText().toString();
                    Log.d(TAG, "onKey: 입력됨 : "+insertedOrder);
                    viewHolder.mallAdminitem_mallOrderEdit_ex.setTextColor(Color.BLACK);
                    //글자 하나하나 입력시마다 발동
                    int numb;
                    try {
                        numb = Integer.parseInt(insertedOrder)-1;
                        Log.d(TAG, "onKey: 입력한 숫자 : "+numb);
                    }catch (NumberFormatException e){
                        Log.d(TAG, "onKey: 숫자를 입력하세요");
                        return false;
                    }

//                        ArrayList changOrderArr = new ArrayList();
//                        changOrderArr.add(numb);
//                        changOrderArr.add(arrayList.get(i));
                    saveChangeMap.put(i,numb);
                    Log.d(TAG, "onKey: key : "+i+" 바꿀값 : "+numb);
                    Log.d(TAG, "onFocusChange: "+saveChangeMap.toString());
                    Boolean isDeleteData = editedArraylist.remove(arrayList.get(i));
                    if (isDeleteData){
                        Log.d(TAG, "onFocusChange: 삭제함 = 첫 수정임");
                    }else {
                        Log.d(TAG, "onFocusChange: 삭제못함 = 첫 수정이 아님");
                    }
                    return false;
                }
            });

//            viewHolder.mallAdminitem_HideOrVisibleButton_btn.setVisibility(View.VISIBLE);
//            viewHolder.mallAdminitem_mallOrderEdit_ex.setText(arrayList.get(i).getMall_order());
//            viewHolder.mallAdminitem_HideOrVisibleButton_btn.setText("숨기기");
        }

//        viewHolder.mallAdminitem_HideOrVisibleButton_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(hideButtonCheckList.get(i)){
//                    hideButtonCheckList.set(i,false);
//                    notifyItemChanged(i);
//                    Log.d(TAG, "onClick: "+i+"번째 쇼핑몰 숨기기 해제");
////                    MallOrderListDate anMallOrderListDate = arrayList.get(i);\\\\\\\\\\\\\
//                }else {
//                    hideButtonCheckList.set(i,true);
//                    notifyItemChanged(i);
//                    Log.d(TAG, "onClick: "+i+"번째 쇼핑몰 숨기기 클릭");
//                }
//
//            }
//        });
        if (isClickHide){//여기서 isClickHide를 사용해서 숨기기 버튼을 보이게 해야한다.
//            viewHolder.mallAdminitem_HideOrVisibleButton_btn.setVisibility(View.VISIBLE);
            /*
            * 숨긴 쇼핑몰 보기 버튼을 누르면 숨긴 쇼핑몰의 리스트가 있는 화면으로 넘어간다.
            *   intent를 통해 str화 된 보여지고 있는 보여지는 쇼핑몰 리스트를 보내준다.
            * 넘어간 화면에는 각쇼핑몰의 이름 우측에 보이기 버튼이 있다.
            * 보이기 버튼을 버튼 우측에 보이게할 순번을 입력할 수 있는 란이 생긴다.
            *   보이기 버튼을 누르면 순번입력editText를 보이게 하고
            *   입력한 순번을 받아서 해당 쇼핑몰의 순번값으로 입력한다.
            *   넘어온 보여지는 쇼핑몰 리스트는 순번값 순서로 index화 되어 있는데 입력한 새로 보이게한 순번값과
            *   동일한 값의 순번값을 가진 쇼핑몰이 있다면
            *   해당 쇼핑몰 보다 index가 큰 모든
            *
            *   아니다 그냥 보이게할 쇼핑몰의 순서값을 key, 쇼핑몰의 데이터를 value로 해서 hash 에 담아 놓고
            *   반복문안에서 hash에 해당 index의 key 를 가진 값이 없다면 넘어온 보여지는 쇼핑몰 리스트에서 하나씩 빼서 array에 넣는 작업을 한다.
            *   array엔 보이게한 쇼핑몰이 추가된 쇼핑몰 순번 데이터가 담겨 있고 이것을 서버에 업데이트 하면된다.
            *
            * 순번을 입력하고 하단의 저장버튼을 누르면 서버에 바뀐 순번값을 저장한다.
            * 제휴쇼핑몰 관리 페이지로 돌아간다.
            * */
            viewHolder.mallAdminitem_HideOrVisibleButton_btn.setVisibility(View.VISIBLE);
            viewHolder.mallAdminitem_mallOrderEdit_ex.setText(arrayList.get(i).getMall_order());
            viewHolder.mallAdminitem_HideOrVisibleButton_btn.setText("숨기기");
            viewHolder.mallAdminitem_showHideMall_btn.setVisibility(View.INVISIBLE);

            viewHolder.mallAdminitem_HideOrVisibleButton_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(hideButtonCheckList.get(i)){
                        hideButtonCheckList.set(i,false);
                        notifyItemChanged(i);
                        Log.d(TAG, "onClick: "+i+"번째 쇼핑몰 숨기기 해제");
//                    MallOrderListDate anMallOrderListDate = arrayList.get(i);\\\\\\\\\\\\\
                    }else {
                        hideButtonCheckList.set(i,true);
                        notifyItemChanged(i);
                        Log.d(TAG, "onClick: "+i+"번째 쇼핑몰 숨기기 클릭");
                    }

                }
            });
        }

        if (isClickHideMalls == null){
            viewHolder.mallAdminitem_showHideMall_btn.setVisibility(View.INVISIBLE);

        }else if(isClickHideMalls) {
            // 숨기 쇼핑몰 보기 화면에서만 동작하는 코드
            viewHolder.mallAdminitem_mallOrder_tx.setVisibility(View.INVISIBLE);
            viewHolder.mallAdminitem_showHideMall_btn.setVisibility(View.VISIBLE);
            Log.d(TAG, "onBindViewHolder: isClickHideMalls 는 : "+isClickHideMalls);
            viewHolder.mallAdminitem_showHideMall_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    viewHolder.mallAdminitem_mallOrderEdit_ex.setVisibility(View.VISIBLE);
//                    viewHolder.mallAdminitem_mallOrderEdit_ex.setText("");
//                    viewHolder.mallAdminitem_mallOrderEdit_ex.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                        @Override
//                        public void onFocusChange(View v, boolean hasFocus) {
//                            if (!hasFocus){
//                                String getIndex = viewHolder.mallAdminitem_mallOrderEdit_ex.getText().toString();
//                                int int_getIndex = Integer.parseInt(getIndex)-1;
//                                int limitIndex;// @@ 입력할 수 있는 순번의 한계숫자이다.
//                                // 보이는 쇼핑몰 개수 + 숨긴 쇼핑몰 개수
//                                // 하지만 모든 숨긴 쇼핑몰을 다 보이게 하지 않은면 중간에 구멍이 생기기에 이에대한 보완책이 필요하다.
////                                if (int_getIndex > )
//                                saveHideChangeMap.put(int_getIndex, arrayList.get(i));
//                                // key 값을 배열에 맞게 1줄여서 넣었다.
//
//
//                            }
//                        }
//                    });
//
                    /*
                    * 보이기 버튼을 누르면 버튼 색이 변한다.
                    *   다시 누르면 원래대로 돌아온다.
                    * 저장버튼을 누르면 버튼 색이 변한 쇼핑몰들이 쇼핑몰 관리 페이지에서 가장 하단에 보여지게 된다.
                    *   체크된(버튼색 변한) 쇼핑몰에게 순번을 부여한다.
                    *   보이는 쇼핑몰 리스트보다 큰 숫자의 순번을 부여한다.
                    *   부여된 순번으로 서버에 update한다.
                    *   업데이트가 완료되면 쇼핑몰 관리 페이지로 화면을 이동한다.
                    * */
                    if (!isShowBtnClick.get(i)){
                        isShowBtnClick.set(i, true);
                        viewHolder.mallAdminitem_showHideMall_btn.setBackgroundColor(Color.BLUE);
//                        viewHolder.mallAdminitem_showHideMall_btn.setBackgroundColor(Color.);
                    }else {
                        isShowBtnClick.set(i, false);
                        viewHolder.mallAdminitem_showHideMall_btn.setBackgroundColor(Color.GRAY);
                    }

                }
            });
        }else {
            Log.d(TAG, "onBindViewHolder: isClickHideMalls 는 : "+isClickHideMalls);
            viewHolder.mallAdminitem_showHideMall_btn.setVisibility(View.VISIBLE);
            viewHolder.mallAdminitem_showHideMall_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public void setHideButton(itemViewHolder viewHolder, int i){
        //boolen 여부에 따라 여기에 버튼 색을 바꾸는 그 값이 나오게 해야한다.
        if (hideButtonCheckList.get(i)){//디폴트는 false , 눌렀을 true
            viewHolder.mallAdminitem_HideOrVisibleButton_btn.setBackgroundColor(Color.RED);
            // 누른적 있으면 빨간색
        }else {
            viewHolder.mallAdminitem_HideOrVisibleButton_btn.setBackgroundColor(Color.GRAY);
            // 누른적 없으면 회색
        }


    }
}
