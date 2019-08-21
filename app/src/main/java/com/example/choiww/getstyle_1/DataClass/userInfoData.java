package com.example.choiww.getstyle_1.DataClass;

public class userInfoData {
    int id=0;
    String userEmail = "";
    String userName="";
    String userNickname="";

    String createdDate="";

    int totalNumberOfOrders=0;// 총 주문개수
    int totalOrderAmount=0;// 총 주문금액
    int user_status = 0;
    int isUserAccessRestrict=0; // 접속제한
    int isWithDrawed=0; //탈퇴상태

    public userInfoData(){


    }

    public userInfoData(int id, String userEmail, String userName, String userNickname, String createdDate, int totalNumberOfOrders, int totalOrderAmount, int isUserAccessRestrict, int isWithDrawed) {
        this.id = id;
        this.userEmail = userEmail;
        this.userName = userName;
        this.userNickname = userNickname;
        this.createdDate = createdDate;
        this.totalNumberOfOrders = totalNumberOfOrders;
        this.totalOrderAmount = totalOrderAmount;
        this.isUserAccessRestrict = isUserAccessRestrict;
        this.isWithDrawed = isWithDrawed;
    }

    public int getUser_status() {
        return user_status;
    }

    public void setUser_status(int user_status) {
        this.user_status = user_status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public int getTotalNumberOfOrders() {
        return totalNumberOfOrders;
    }

    public void setTotalNumberOfOrders(int totalNumberOfOrders) {
        this.totalNumberOfOrders = totalNumberOfOrders;
    }

    public int getTotalOrderAmount() {
        return totalOrderAmount;
    }

    public void setTotalOrderAmount(int totalOrderAmount) {
        this.totalOrderAmount = totalOrderAmount;
    }

    public int getIsUserAccessRestrict() {
        return isUserAccessRestrict;
    }

    public void setIsUserAccessRestrict(int isUserAccessRestrict) {
        this.isUserAccessRestrict = isUserAccessRestrict;
    }

    public int getIsWithDrawed() {
        return isWithDrawed;
    }

    public void setIsWithDrawed(int isWithDrawed) {
        this.isWithDrawed = isWithDrawed;
    }
}
