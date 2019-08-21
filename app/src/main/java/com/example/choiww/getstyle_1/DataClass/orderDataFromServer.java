package com.example.choiww.getstyle_1.DataClass;

public class orderDataFromServer {

    int numb;
    int userId;
    String userEmail;
    String products_json;
    String orderNumb;
    String orderDate;
    String buyer;
    String orderState;
    int productsPrice;
    int saleDiscount;
    int billedPoint;
    int deliveryCharge;
    int finalBill;
    String paymentMethod;
    String depositor;
    String accountNumb;
    String payDay;
    String receiverName;
    String receiverAddress;
    String receiverCallNumb;
    String receiverCellphone;
    String createdDate;
    String modifiedDate;
    String deliveryState;
    String deliveryNumb;

    public orderDataFromServer(){

    }

    public orderDataFromServer(int numb, int userId, String userEmail, String products_json, String orderNumb, String orderDate, String buyer, String orderState, int productsPrice, int saleDiscount, int billedPoint, int deliveryCharge, int finalBill, String paymentMethod, String depositor, String accountNumb, String payDay, String receiverName, String receiverAddress, String receiverCallNumb, String receiverCellphone, String createdDate, String modifiedDate, String deliveryState, String deliveryNumb) {
        this.numb = numb;
        this.userId = userId;
        this.userEmail = userEmail;
        this.products_json = products_json;
        this.orderNumb = orderNumb;
        this.orderDate = orderDate;
        this.buyer = buyer;
        this.orderState = orderState;
        this.productsPrice = productsPrice;
        this.saleDiscount = saleDiscount;
        this.billedPoint = billedPoint;
        this.deliveryCharge = deliveryCharge;
        this.finalBill = finalBill;
        this.paymentMethod = paymentMethod;
        this.depositor = depositor;
        this.accountNumb = accountNumb;
        this.payDay = payDay;
        this.receiverName = receiverName;
        this.receiverAddress = receiverAddress;
        this.receiverCallNumb = receiverCallNumb;
        this.receiverCellphone = receiverCellphone;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.deliveryState = deliveryState;
        this.deliveryNumb = deliveryNumb;
    }

    public int getNumb() {
        return numb;
    }

    public void setNumb(int numb) {
        this.numb = numb;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getProducts_json() {
        return products_json;
    }

    public void setProducts_json(String products_json) {
        this.products_json = products_json;
    }

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

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public int getProductsPrice() {
        return productsPrice;
    }

    public void setProductsPrice(int productsPrice) {
        this.productsPrice = productsPrice;
    }

    public int getSaleDiscount() {
        return saleDiscount;
    }

    public void setSaleDiscount(int saleDiscount) {
        this.saleDiscount = saleDiscount;
    }

    public int getBilledPoint() {
        return billedPoint;
    }

    public void setBilledPoint(int billedPoint) {
        this.billedPoint = billedPoint;
    }

    public int getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(int deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public int getFinalBill() {
        return finalBill;
    }

    public void setFinalBill(int finalBill) {
        this.finalBill = finalBill;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getDepositor() {
        return depositor;
    }

    public void setDepositor(String depositor) {
        this.depositor = depositor;
    }

    public String getAccountNumb() {
        return accountNumb;
    }

    public void setAccountNumb(String accountNumb) {
        this.accountNumb = accountNumb;
    }

    public String getPayDay() {
        return payDay;
    }

    public void setPayDay(String payDay) {
        this.payDay = payDay;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getReceiverCallNumb() {
        return receiverCallNumb;
    }

    public void setReceiverCallNumb(String receiverCallNumb) {
        this.receiverCallNumb = receiverCallNumb;
    }

    public String getReceiverCellphone() {
        return receiverCellphone;
    }

    public void setReceiverCellphone(String receiverCellphone) {
        this.receiverCellphone = receiverCellphone;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getDeliveryState() {
        return deliveryState;
    }

    public void setDeliveryState(String deliveryState) {
        this.deliveryState = deliveryState;
    }

    public String getDeliveryNumb() {
        return deliveryNumb;
    }

    public void setDeliveryNumb(String deliveryNumb) {
        this.deliveryNumb = deliveryNumb;
    }
}
