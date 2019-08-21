package com.example.choiww.getstyle_1.DataClass;

public class MallOrderListDate {
    int numb = 0;
    int mall_id=0;
    String mall_name="";
    String kor_name="";

    public String getKor_name() {
        return kor_name;
    }

    public void setKor_name(String kor_name) {
        this.kor_name = kor_name;
    }

    String mall_order="";

    public MallOrderListDate(int numb, int mall_id, String mall_name, String mall_order) {
        this.numb = numb;
        this.mall_id = mall_id;
        this.mall_name = mall_name;
        this.mall_order = mall_order;
    }
    public MallOrderListDate(){

    }

    public int getNumb() {
        return numb;
    }

    public void setNumb(int numb) {
        this.numb = numb;
    }

    public int getMall_id() {
        return mall_id;
    }

    public void setMall_id(int mall_id) {
        this.mall_id = mall_id;
    }

    public String getMall_name() {
        return mall_name;
    }

    public void setMall_name(String mall_name) {
        this.mall_name = mall_name;
    }

    public String getMall_order() {
        return mall_order;
    }

    public void setMall_order(String mall_order) {
        this.mall_order = mall_order;
    }
}
