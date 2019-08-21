package com.example.choiww.getstyle_1.DataClass;

public class SimpleOrderInfoData {
    String orderNumb;
    String orderDate;
    String orderState;
    int finalBill;
    int deliveryNumb;
    String deliveryState;
    String numb;

    public String getOrderNumb() {
        return orderNumb;
    }

    public void setOrderNumb(String orderNumb) {
        this.orderNumb = orderNumb;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public String getDeliveryState() {
        return deliveryState;
    }

    public void setDeliveryState(String deliveryState) {
        this.deliveryState = deliveryState;
    }

    public String getNumb() {
        return numb;
    }

    public void setNumb(String numb) {
        this.numb = numb;
    }

    public SimpleOrderInfoData(){

    }

    public int getDeliveryNumb() {
        return deliveryNumb;
    }

    public void setDeliveryNumb(int deliveryNumb) {
        this.deliveryNumb = deliveryNumb;
    }

    public int getFinalBill() {
        return finalBill;
    }

    public void setFinalBill(int finalBill) {
        this.finalBill = finalBill;
    }


}
