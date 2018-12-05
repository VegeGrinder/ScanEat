package com.example.shangsheingoh.scaneat.Model;

public class LocationDetails {
    //  private int id;
    private String userName;
    private String pickUpTime;
    private String slotID;
    //   private double rating;
    //   private double price;


    public LocationDetails() {
    }

    public LocationDetails(/*int id,*/ String username, String pickUpTime ,String slotID/*, double rating, double price*/) {
        //  this.id = id;
        this.userName = username;
        this.pickUpTime = pickUpTime;
        this.slotID = slotID;
        //   this.rating = rating;
        //   this.price = price;
    }

    public String getUserName() {
        return userName;
    }

    public String getPickUpTime() {
        return pickUpTime;
    }

    public String getSlotID(){return slotID;}

}