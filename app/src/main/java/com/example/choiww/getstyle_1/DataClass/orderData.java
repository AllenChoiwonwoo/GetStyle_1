package com.example.choiww.getstyle_1.DataClass;

import java.util.ArrayList;

public class orderData {



    public orderData(String buyer_name, String buyer_postNumb, String buyer_address1, String buyer_address2, String buyer_callNumb, String buyer_cellphoneNumb, String buyer_email, String receiver_name, String receiver_postNumb, String receiver_address1, String receiver_address2, String receiver_callNumb, String receiver_cellphone, String receiver_message, String sendCash, boolean card, boolean mobile, String sendPayment) {
        this.buyer_name = buyer_name;
        this.buyer_postNumb = buyer_postNumb;
        this.buyer_address1 = buyer_address1;
        this.buyer_address2 = buyer_address2;
        this.buyer_callNumb = buyer_callNumb;
        this.buyer_cellphoneNumb = buyer_cellphoneNumb;
        this.buyer_email = buyer_email;
        this.receiver_name = receiver_name;
        this.receiver_postNumb = receiver_postNumb;
        this.receiver_address1 = receiver_address1;
        this.receiver_address2 = receiver_address2;
        this.receiver_callNumb = receiver_callNumb;
        this.receiver_cellphone = receiver_cellphone;
        this.receiver_message = receiver_message;
        this.sendCash = sendCash;
        this.card = card;
        this.mobile = mobile;
        this.sendPayment = sendPayment;
    }
    public orderData(){

    }

    public ArrayList getSelectedItemInfo_list() {
        return selectedItemInfo_list;
    }

    public void setSelectedItemInfo_list(ArrayList selectedItemInfo_list) {
        this.selectedItemInfo_list = selectedItemInfo_list;
    }

    public String isSendCash() {
        return sendCash;
    }

    public boolean isCard() {
        return card;
    }

    public boolean isMobile() {
        return mobile;
    }

    ArrayList selectedItemInfo_list;

    String buyer_name;
    String buyer_postNumb;
    String buyer_address1;
    String buyer_address2;
    String buyer_callNumb;
    String buyer_cellphoneNumb;
    String buyer_email;
    String receiver_name;
    String receiver_postNumb;
    String receiver_address1;
    String receiver_address2;
    String receiver_callNumb;
    String receiver_cellphone;
    String receiver_message;
    String sendCash;
    boolean card;
    boolean mobile;
    String sendPayment;
    int coast;

    public int getCoast() {
        return coast;
    }

    public void setCoast(int coast) {
        this.coast = coast;
    }

    public String getBuyer_name() {
        return buyer_name;
    }

    public void setBuyer_name(String buyer_name) {
        this.buyer_name = buyer_name;
    }

    public String getBuyer_postNumb() {
        return buyer_postNumb;
    }

    public void setBuyer_postNumb(String buyer_postNumb) {
        this.buyer_postNumb = buyer_postNumb;
    }

    public String getBuyer_address1() {
        return buyer_address1;
    }

    public void setBuyer_address1(String buyer_address1) {
        this.buyer_address1 = buyer_address1;
    }

    public String getBuyer_address2() {
        return buyer_address2;
    }

    public void setBuyer_address2(String buyer_address2) {
        this.buyer_address2 = buyer_address2;
    }

    public String getBuyer_callNumb() {
        return buyer_callNumb;
    }

    public void setBuyer_callNumb(String buyer_callNumb) {
        this.buyer_callNumb = buyer_callNumb;
    }

    public String getBuyer_cellphoneNumb() {
        return buyer_cellphoneNumb;
    }

    public void setBuyer_cellphoneNumb(String buyer_cellphoneNumb) {
        this.buyer_cellphoneNumb = buyer_cellphoneNumb;
    }

    public String getBuyer_email() {
        return buyer_email;
    }

    public void setBuyer_email(String buyer_email) {
        this.buyer_email = buyer_email;
    }

    public String getReceiver_name() {
        return receiver_name;
    }

    public void setReceiver_name(String receiver_name) {
        this.receiver_name = receiver_name;
    }

    public String getReceiver_postNumb() {
        return receiver_postNumb;
    }

    public void setReceiver_postNumb(String receiver_postNumb) {
        this.receiver_postNumb = receiver_postNumb;
    }

    public String getReceiver_address1() {
        return receiver_address1;
    }

    public void setReceiver_address1(String receiver_address1) {
        this.receiver_address1 = receiver_address1;
    }

    public String getReceiver_address2() {
        return receiver_address2;
    }

    public void setReceiver_address2(String receiver_address2) {
        this.receiver_address2 = receiver_address2;
    }

    public String getReceiver_callNumb() {
        return receiver_callNumb;
    }

    public void setReceiver_callNumb(String receiver_callNumb) {
        this.receiver_callNumb = receiver_callNumb;
    }

    public String getReceiver_cellphone() {
        return receiver_cellphone;
    }

    public void setReceiver_cellphone(String receiver_cellphone) {
        this.receiver_cellphone = receiver_cellphone;
    }

    public String getReceiver_message() {
        return receiver_message;
    }

    public void setReceiver_message(String receiver_message) {
        this.receiver_message = receiver_message;
    }

    public String getSendCash() {
        return sendCash;
    }

    public void setSendCash(String sendCash) {
        this.sendCash = sendCash;
    }

    public boolean getCard() {
        return card;
    }

    public void setCard(boolean card) {
        this.card = card;
    }

    public boolean getMobile() {
        return mobile;
    }

    public void setMobile(boolean mobile) {
        this.mobile = mobile;
    }

    public String getSendPayment() {
        return sendPayment;
    }

    public void setSendPayment(String sendPayment) {
        this.sendPayment = sendPayment;
    }
}
