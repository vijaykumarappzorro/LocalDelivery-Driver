package com.localdelivery.driver.model;


import android.content.Context;
import android.util.Log;

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

        String params = Config.signUp_url+"&email="+email+"&firstname="+firstName+"&lastname="+lastName+"&password="+password
                +"&mobile="+mobile+"&device_token="+deviceToken+"&device_type="+deviceType +"&user_type="+user_type
                +"&vehicle_type="+vehicle+"&latitude="+latitude+"&longitude="+longitude+"&profile_pic="+profile_pic;

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

        String parms = Config.forgot_password+"&email="+email+"&user_type="+usertype;
        Log.e("forgot password","url"+parms);
        return parms;
    }

    public  static  String getUserDeatil(Context context, String userid,String usertype){

        String parms = Config.user_detail+"&user_id="+userid+"&user_type="+usertype;
        Log.e("getuserdetail update",parms);
        return parms;
    }
     public  static  String verifyEmailId(Context context,String userid,String usertype){


         String parms =Config.verif_email+"&user_id="+userid+"&user_type="+usertype;

         Log.e("verfiy account",parms);

         return parms;
     }
}
