package com.example.shangsheingoh.scaneat.Model;

public class TemporaryDetails {
    private String tempUserID;
    private String tempUserName;
    private double latitude;
    private double longitude;
    private String slotID;

    public TemporaryDetails() {
    }

    public String getTempUserID() {
        return tempUserID;
    }

    public String getTempUserName() {
        return tempUserName;
    }

    public String getSlotID() {
        return slotID;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public TemporaryDetails (String tempUserID,String tempUserName,double latitude, double longitude,String slotID){
        this.tempUserID = tempUserID;
        this.tempUserName = tempUserName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.slotID = slotID;
    }

}