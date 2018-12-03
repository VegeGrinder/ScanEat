package com.example.shangsheingoh.scaneat.Model;

public class LocationDetails {
    //  private int id;
    private String user;
    private String pickUpTime;
    private String slotID;
    //   private double rating;
    //   private double price;


    public LocationDetails() {
    }

    public LocationDetails(/*int id,*/ String user, String pickUpTime ,String slotID/*, double rating, double price*/) {
        //  this.id = id;
        this.user = user;
        this.pickUpTime = pickUpTime;
        this.slotID = slotID;
        //   this.rating = rating;
        //   this.price = price;
    }


    public String getUser() {
        return user;
    }


    public String getPickUpTime() {
        return pickUpTime;
    }

    public String getSlotID(){return slotID;}

}