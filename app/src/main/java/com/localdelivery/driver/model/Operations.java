package com.localdelivery.driver.model;


import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

public class Operations {

    private static String TAG = Operations.class.getSimpleName();

    public static String getLoginDetails(Context context, String email, String password, String deviceToken, String deviceType,
                                         String user_type) {

        String params = Config.login_url+"&email="+email+"&password="+password+"&device_token="+deviceToken+"&device_type="+deviceType
                +"&user_type="+user_type;

        Log.v(TAG, "loginff-parameters---"+params);

        return params;
    }

    public static String getSignUpDetails(Context context, String email, String firstName, String lastName, String password, String mobile,
                                          String deviceToken, String deviceType, String user_type, String profile_pic,String vehicle) {

        String params = null;

        params = Config.signUp_url+"&email="+email+"&firstname="+firstName+"&lastname="+lastName+"&password="+password
                +"&mobile="+mobile+"&device_token="+deviceToken+"&device_type="+deviceType +"&user_type="+user_type+"&vehicle_type="+vehicle
                +"&profile_pic="+profile_pic;

        Log.v(TAG, "sign-up-parameters---"+params);

        return params.replaceAll("\n","");
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
    public static String acceptRequestofCustomer(Context context,String request_id,String customer_id,String driver_id,String estimatedcost,String estimateddate,String estimatedtime)
    {


        String parms = Config.accept_Rquest_customer+"&request_id="+request_id+"&customer_id="+customer_id+"&driver_id="+driver_id
                +"&estimated_cost="+estimatedcost+"&estimated_date="+estimateddate+"&estimated_time="+estimatedtime;
        Log.e("accept url",parms);
        return parms;
    }
    public static String completedHistory(Context context,String id){

        String parms= Config.completed_Trips+id;

        Log.e("completed Trips","History  "+parms);


        return parms;
    }

    public static String simpleuserRegister(Context context,String emailid,String password ,String firstname,String lastname ,
                                            String vehicletype, String usertype, String devicetoken,
                                            String devicetype, String profilepic, String mobileno) {
        /*String params = Config.fb_login_url+company_id+"&email="+email+"&password=WJBJvfHTRNT"+"&name="+name+"&device_token="+token+"&device_type=A"+"&mobile="+mobile+"&profileImage="+imageUrl+"&facebook_id="+fb_id;*/
        try {
            JSONObject postDataParams = new JSONObject();

            postDataParams.put("email", emailid);
            postDataParams.put("password", password);
            postDataParams.put("firstname", firstname);
            postDataParams.put("lastname",lastname);
            postDataParams.put("vehicle_type",vehicletype);
            postDataParams.put("user_type",usertype);
            postDataParams.put("device_token",devicetoken);
            postDataParams.put("device_type",devicetype);
            postDataParams.put("profile_pic",profilepic);
            postDataParams.put("mobile",mobileno);
            String params = null;
            try {
                params = Utility.getPostDataString(postDataParams);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return params;

        } catch (Exception e) {

            e.printStackTrace();
        }
        //     Log.e(TAG, "fb_login params-- "+params);return params;} catch (JSONException e) {e.printStackTrace();}return null;}
        return null;
    }
}
