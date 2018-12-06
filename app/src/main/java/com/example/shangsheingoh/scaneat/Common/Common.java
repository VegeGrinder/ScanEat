package com.example.shangsheingoh.scaneat.Common;

import com.example.shangsheingoh.scaneat.Model.Request;
import com.example.shangsheingoh.scaneat.Model.User;
import com.example.shangsheingoh.scaneat.Model.UserProfile;

public class Common {
    public static UserProfile currentUser;
    public static Request currentRequest;

    public static String convertCodeToStatus(String status) {
        if(status.equals("0")){
            return "Placed";
        }
        else if(status.equals("1")){
            return "Ready";
        }
        else if(status.equals("2")){
            return "Shipped";
        }
        else{
            return "Finished";
        }
    }
}
