package com.example.choiww.getstyle_1.DataClass;

public class roomInfoDataClass {
    int roomNumb;
    String roomName;
    String roomTages;
    String roomCertainsId;
    String roomCertainsEmail;
    int maxUserVolume;
    String createdDate;

    public roomInfoDataClass(){

    }

    public roomInfoDataClass(int roomNumb, String roomName, String roomTages, String roomCertainsId, String roomCertainsEmail, int maxUserVolume, String createdDate) {
        this.roomNumb = roomNumb;
        this.roomName = roomName;
        this.roomTages = roomTages;
        this.roomCertainsId = roomCertainsId;
        this.roomCertainsEmail = roomCertainsEmail;
        this.maxUserVolume = maxUserVolume;
        this.createdDate = createdDate;
    }

    public int getRoomNumb() {
        return roomNumb;
    }

    public void setRoomNumb(int roomNumb) {
        this.roomNumb = roomNumb;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomTages() {
        return roomTages;
    }

    public void setRoomTages(String roomTages) {
        this.roomTages = roomTages;
    }

    public String getRoomCertainsId() {
        return roomCertainsId;
    }

    public void setRoomCertainsId(String roomCertainsId) {
        this.roomCertainsId = roomCertainsId;
    }

    public String getRoomCertainsEmail() {
        return roomCertainsEmail;
    }

    public void setRoomCertainsEmail(String roomCertainsEmail) {
        this.roomCertainsEmail = roomCertainsEmail;
    }

    public int getMaxUserVolume() {
        return maxUserVolume;
    }

    public void setMaxUserVolume(int maxUserVolume) {
        this.maxUserVolume = maxUserVolume;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
