package com.example.choiww.getstyle_1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

public class BasketFilteredByMallActivity extends AppCompatActivity {
    String TAG = "find";

    /*
    * 목적 : 사용자가 장바구니에서 쇼핑몰 별로 몇개의 상품을 넣었는지 확인할 수 있도록 해주려 한다.
    *
    * 시나리오 :
    *   1. 사용자가 '쇼핑몰별 보기'버튼을 클릭
    *   2. db에서 몰별로 몇개의 상품이 담겨있는지 개수 가져오기
    *   3. 화면에 표시
    *   (해당 쇼핑몰을 누를시 그 쇼핑몰의 상품만 볼 수 있는 페이지로 넘어간다.)
    * */

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket_filtered_by_mall);

        RecyclerView filteredBasket_recyclerview = findViewById(R.id.filteredBasket_recyclerview);

        //dbHelper 가 선언된적이 없으면 초기화 시킨다.
        if (dbHelper == null){
            dbHelper = new DBHelper(getApplicationContext(), "basket", null, DBHelper.DB_VERSION);
            Log.d(DBHelper.TAG, "onCreate: 초기화 됨");
        }

        ArrayList<itemInfo> items;
        /*쇼핑몰 별로 몇개의 상품이 있는지 데이터를 가져온다.
        *
        * 데이터를 가져온다.
        *   모든쇼핑몰의 이름을 가져온다.
        *   이름을 활용해 쇼핑몰에 몇개의 상품이 들어있는지 확인한다.
        *   배열에 데이터를 담아놓는다.
        *   다른 쇼핑몰들도 같은 과정을 반복한다.
        *   (상품이 하나도 담겨 있지 않다면 화면에 나오지 않도록 배열에 넣지 않는다.)
        *
        * 데이터를 recyclerview adapter에 넣는다.
        * */

//  @@@@@@ vintagetalk, vintagesister, xecond
// 지금 무슨 쇼핑몰이 있느지 앱에 집적써줘야하지만.. 나중엔 바꿔야겠지
// > db에 있는 쇼핑몰이름 목록을 가져와 하나 반복문속에서 getMallItemCount()메소드가 반복되도록
        ArrayList mallCountList = new ArrayList();

        String vintagetalk= "vintagetalk";
        int vintagetalk_count;
        vintagetalk_count = dbHelper.getMallItemCount(vintagetalk);
        Log.d(TAG, "onCreate: vintagetalk_count = "+vintagetalk_count);
//        String vintage
        mallCountList.add(new MallCount(vintagetalk, vintagetalk_count,R.drawable.vintagetalklogo));

        String vintagesister = "vintagesister";
        int vintagesister_count;
        vintagesister_count = dbHelper.getMallItemCount(vintagesister);
        Log.d(TAG, "onCreate: vintagesister_count = "+vintagesister_count);
        mallCountList.add(new MallCount(vintagesister, vintagesister_count,R.drawable.vintagesisterlogo));

        String xecond = "xecond";
        int xecond_count;
        xecond_count = dbHelper.getMallItemCount(xecond);
        Log.d(TAG, "onCreate: xecond_count = "+xecond_count);
        mallCountList.add(new MallCount(xecond, xecond_count,R.drawable.xecond_logo));



        //@@@@@ 나중에는 최근에 넣었던 쇼핑몰 부터 보여줬으면 좋겠다.
        //그 방법으로 는 쇼핑몰별 상품개수를 불러오는 쿼리에는 where 에 쇼핑몰 이름의 조건을 넣는데
        // 쇼핑몰 이름조건을 sub쿼리로 하여 '최근에 추가된 상품의 쇼핑몰 순'으로 쇼핑몰 이름을 가져오게 하면된다.


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        RecyclerView basketMallCount = findViewById(R.id.filteredBasket_recyclerview);
        basketMallCount.setLayoutManager(linearLayoutManager);
        Adapter_BasketFilteredByMall adapter = new Adapter_BasketFilteredByMall(getApplicationContext(), mallCountList);
        basketMallCount.setAdapter(adapter);




    }
    public class MallCount{
        String mallName;
        int count;
        int drawable_numb;

        public int getDrawable_numb() {
            return drawable_numb;
        }


        public MallCount(String mallName, int count, int drawable_numb) {
            this.mallName = mallName;
            this.count = count;
            this.drawable_numb = drawable_numb;
        }

        public String getMallName() {
            return mallName;
        }

        public void setMallName(String mallName) {
            this.mallName = mallName;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}
