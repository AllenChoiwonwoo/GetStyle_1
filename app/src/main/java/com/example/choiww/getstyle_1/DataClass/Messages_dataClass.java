package com.example.choiww.getstyle_1.DataClass;

import android.graphics.Bitmap;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;

/*
public class Messages_dataClass implements Parcelable {
    String roomNumb;
    String userId;
    String userEmail;
    String sendTime;
    String message;
    String messageType;
//    byte[] byteArrayImg;
//    Bitmap bitmap;
//    byte[] images
//    Boolean isNewChatRoom;

    protected Messages_dataClass(Parcel in) {
        roomNumb = in.readString();
        userId = in.readString();
        userEmail = in.readString();
        sendTime = in.readString();
        message = in.readString();
        messageType = in.readString();
//        byteArrayImg = in.read
//        bitmap = in.readB

//        isNewChatRoom = in.readbool
    }

    public static final Creator<Messages_dataClass> CREATOR = new Creator<Messages_dataClass>() {
        @Override
        public Messages_dataClass createFromParcel(Parcel in) {
            return new Messages_dataClass(in);
        }

        @Override
        public Messages_dataClass[] newArray(int size) {
            return new Messages_dataClass[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(roomNumb);
        dest.writeString(userId);
        dest.writeString(userEmail);
        dest.writeString(sendTime);
        dest.writeString(message);
        dest.writeString(messageType);

    }

    public Messages_dataClass(String received_roomNumb, String received_userid, String received_userEmail, String received_message, String received_sendTime){

    }

    public Messages_dataClass(String roomNumb, String userId, String userEmail, String message, String sendTime, String messageType) {
        this.roomNumb = roomNumb;
        this.userId = userId;
        this.userEmail = userEmail;
        this.message = message;
        this.sendTime = sendTime;
        this.messageType = messageType;
    }

    public String getRoomNumb() {
        return roomNumb;
    }

    public void setRoomNumb(String roomNumb) {
        this.roomNumb = roomNumb;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

//    public static Creator<Messages_dataClass> getCREATOR() {
//        return CREATOR;
//    }
}
*/

public class Messages_dataClass {
    String roomNumb =null;
    String userId=null;
    String userEmail=null;
    String sendTime=null;
    String message=null;
    String messageType=null;
    Bitmap bitmap=null;
    String bitmapName = null;
    public Messages_dataClass(){

    }

    public Messages_dataClass(String roomNumb, String userId, String userEmail, String sendTime, String message, String messageType, Bitmap bitmap) {
        this.roomNumb = roomNumb;
        this.userId = userId;
        this.userEmail = userEmail;
        this.sendTime = sendTime;
        this.message = message;
        this.messageType = messageType;
        this.bitmap = bitmap;
    }

    public String getRoomNumb() {
        return roomNumb;
    }

    public void setRoomNumb(String roomNumb) {
        this.roomNumb = roomNumb;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getBitmapName() {
        return bitmapName;
    }

    public void setBitmapName(String bitmapName) {
        this.bitmapName = bitmapName;
    }
}
