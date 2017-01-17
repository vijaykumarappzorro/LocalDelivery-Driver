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

public class LoginManager {

    private static final String TAG = LoginManager.class.getSimpleName();

    public void getLoginData(Context context, String params) {
        new ExecuteApi(context).execute(params);
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

            Log.e(TAG, "login response--"+response);

            return response;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONObject response = jsonObject.getJSONObject("response");
                int id = response.getInt("id");
                String message = response.getString("complete_status");
                if (id >= 1) {
                    LDPreferences.putString(mContext, "driver_id", String.valueOf(id)) ;
                    EventBus.getDefault().post(new Event(Constants.LOGIN_SUCCESS, String.valueOf(id)));
                }
                else
                    EventBus.getDefault().post(new Event(Constants.ACCOUNT_NOT_REGISTERED, message));

            } catch (JSONException ex) {
                ex.printStackTrace();
            }


        }
    }

}
