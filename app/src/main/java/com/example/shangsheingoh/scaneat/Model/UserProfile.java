package com.example.shangsheingoh.scaneat.Model;


/**
 * Created by user_activity on 3/26/2018.
 */

public class UserProfile {
    public  String userName;
    public  String userEmail;
    public  String userIc;
    public  String userPhone;
    

    public UserProfile(){

    }

    public UserProfile(String userName,String userEmail,String userIc,String userPhone){
        this.userName = userName;
        this.userEmail = userEmail;
        this.userIc = userIc;
        this.userPhone = userPhone;

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserIc() {
        return userIc;
    }

    public void setUserIc(String userIc) {
        this.userIc = userIc;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }
}



