package com.localdelivery.driver.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.localdelivery.driver.model.Constants;
import com.localdelivery.driver.model.Event;
import com.localdelivery.driver.model.HttpHandler;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by vijay on 21/12/16.
 */

public class ForgotPassManager {

    private static final String TAG = ForgotPassManager.class.getSimpleName();


    public void ForgotPassManager(Context context, String params) {

        new ForgotPassManager.ExecuteApi(context).execute(params);
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

            Log.e(TAG, "forgot password "+response);

            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("forgot passwordresponse",s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                int id = jsonObject1.getInt("id");
                String message = jsonObject1.getString("message");


                    EventBus.getDefault().post(new Event(Constants.forgotpasswprd,String.valueOf(id)+","+message ));


            } catch (JSONException ex) {
                ex.printStackTrace();
            }


        }
    }

}
