package com.localdelivery.driver.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.localdelivery.driver.model.Constants;
import com.localdelivery.driver.model.Event;
import com.localdelivery.driver.model.HttpHandler;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by vijay on 21/12/16.
 */

public class UserDetailManager {
    private static final String TAG = UserDetailManager.class.getSimpleName();


    public void getuserDeatil(Context context, String params) {

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
            Log.e("userdetail response",s);

            EventBus.getDefault().post(new Event(Constants.userfullDetail,s));

        }
    }

}
