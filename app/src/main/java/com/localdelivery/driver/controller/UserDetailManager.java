package com.localdelivery.driver.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.localdelivery.driver.model.Constants;
import com.localdelivery.driver.model.Event;
import com.localdelivery.driver.model.HttpHandler;
import com.localdelivery.driver.model.LDPreferences;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by vijay on 21/12/16.
 */

public class UserDetailManager {
    private static final String TAG = UserDetailManager.class.getSimpleName();


    public void getUserDetails(Context context, String params) {

        new UserDetailManager.ExecuteApi(context).execute(params);
    }

    private class ExecuteApi extends AsyncTask<String, String, String> {

        Context mContext;

        ExecuteApi(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            HttpHandler httpHandler = new HttpHandler();
            String response = httpHandler.makeServiceCall(params[0]);

            Log.e(TAG, "user detail response--"+response);

            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s!=null) {

                try {

                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                    String userId = jsonObject1.getString("id");
                    String firstName = jsonObject1.getString("firstname");
                    String lastName = jsonObject1.getString("lastname");
                    String fullName = firstName + " " + lastName;
                    String emailId = jsonObject1.getString("email");
                    String mobile = jsonObject1.getString("mobile");
                    String vehicleType = jsonObject1.getString("vehicle_type");
                    String profilePic = jsonObject1.getString("profile_pic");

                    LDPreferences.putString(mContext, "driver_name", fullName);
                    LDPreferences.putString(mContext, "email_Id", emailId);
                    LDPreferences.putString(mContext, "mobile", mobile);
                    LDPreferences.putString(mContext, "driver_id", userId);
                    LDPreferences.putString(mContext, "driver_image", profilePic);
                    LDPreferences.putString(mContext, "vehicle_type", vehicleType);

                    EventBus.getDefault().post(new Event(Constants.USER_DETAILS_SUCCESS, ""));

                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }else{

                EventBus.getDefault().post(new Event(Constants.SERVER_ERROR, ""));

            }



        }
    }

}
