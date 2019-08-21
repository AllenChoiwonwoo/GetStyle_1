package com.example.choiww.getstyle_1;

import java.util.List;

public class dataclass_1 {

    String mall_name;
    String img_src;
    String prodname;
    String price;
    String count;
    String href;

    public List getAll() {
        return all;
    }

    List all;

    public String getMall_name() {
        return mall_name;
    }

    public String getImg_src() {
        return img_src;
    }

    public String getProdname() {
        return prodname;
    }
    public String getPrice() {
        return price;
    }

    public String getCount() {
        return count;
    }

    public String getHref() {
        return href;
    }
}

class boolenData{
    String aBoolean;

    public String isaBoolean() {
        return aBoolean;
    }
}

class userInfoForLogin{
    int id;
    String userEmail;
    String userName;
    String userNickname;
    int user_status;

    public int getUser_status() {
        return user_status;
    }

    public void setUser_status(int user_status) {
        this.user_status = user_status;
    }

    public int getUserId(){
        return id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserNickname() {
        return userNickname;
    }
}
