package com.localdelivery.driver.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.localdelivery.driver.model.HttpHandler;

/**
 * Created by vijay on 23/12/16.
 */

public class VerifyEmailManager  {
    private static final String TAG = VerifyEmailManager.class.getSimpleName();


    public void VerifyEmailManager(Context context, String params) {

        new VerifyEmailManager.ExecuteApi(context).execute(params);
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
            Log.e("verify  response",s);


        }
    }

}
