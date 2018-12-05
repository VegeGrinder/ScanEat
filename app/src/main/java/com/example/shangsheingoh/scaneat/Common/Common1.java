package com.example.shangsheingoh.scaneat.Common;

import com.example.shangsheingoh.scaneat.Model.User;
import com.example.shangsheingoh.scaneat.Model.UserProfile;


public class Common1 {
    public static User currentUser;

    public static String convertCodeToStatus(String status) {
        if(status.equals("0")){
            return "Placed";
        }
        else if(status.equals("1")){
            return "On my way";
        }
        else{
            return "Shipped";
        }
    }
}
