package com.localdelivery.driver.model;


import android.content.Context;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Operations {

    private static String TAG = Operations.class.getSimpleName();

    public static String getLoginDetails(Context context, String email, String password, String deviceToken, String deviceType,
                                         String user_type, String latitude, String longitude) {

        String params = Config.login_url+"&email="+email+"&password="+password+"&device_token="+deviceToken+"&device_type="+deviceType
                +"&user_type="+user_type+"&latitude="+latitude+"&longitude="+longitude;

        Log.v(TAG, "login-parameters---"+params);

        return params;
    }

    public static String getSignUpDetails(Context context, String email, String firstName, String lastName, String password, String mobile,
                                          String deviceToken, String deviceType, String user_type, String vehicle,
                                          String latitude, String longitude, String profile_pic) {

        String params = null;
        try {
            params = Config.signUp_url+"&email="+email+"&firstname="+firstName+"&lastname="+lastName+"&password="+password
                    +"&mobile="+mobile+"&device_token="+deviceToken+"&device_type="+deviceType +"&user_type="+user_type
                    +"&vehicle_type="+URLEncoder.encode(vehicle, "utf-8")+"&latitude="+latitude+"&longitude="+longitude+"&profile_pic="+profile_pic;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "sign-up-parameters---"+params);

        return params;
    }

    public  static String checkviechletype(Context context,String id,String user_type){
        String params = Config.check_vichletype+"&id="+id+"&user_type="+user_type;
        Log.e("checkurl",params);
        return params;
    }

    public  static String updatevichletype(Context context,String id,String vehicletype){
        String parms = Config.update_vehicletype+"&id="+id+"&vehicle_type="+vehicletype;
        Log.e("update url",parms);
        return parms;
    }

    public  static  String forgotpassword(Context context,String email,String usertype){

        String params = Config.forgot_password+"&email="+email+"&user_type="+usertype;
        Log.e("forgot password","url"+params);
        return params;
    }

    public  static  String getUserDetail(Context context, String userid,String usertype){

        String parms = Config.user_detail+"&user_id="+userid+"&user_type="+usertype;
        Log.e("user_details update",parms);
        return parms;
    }

     public  static  String verifyEmailId(Context context,String userid,String usertype){

         String parms =Config.verif_email+"&user_id="+userid+"&user_type="+usertype;

         Log.e("verify account",parms);

         return parms;
     }

    public static String getPendingRequests(Context context, String id) {
        String params = Config.pending_requests_url+id;

        Log.e(TAG, "pending_requests parameters--"+params);

        return params;
    }
}
