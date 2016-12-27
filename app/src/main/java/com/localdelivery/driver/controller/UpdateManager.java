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
 * Created by vijay on 20/12/16.
 */

public class UpdateManager {


    private static final String TAG = UpdateManager.class.getSimpleName();


    public void getupdatedetail(Context context, String params) {

        new UpdateManager.ExecuteApi(context).execute(params);
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

            Log.e(TAG, "sign-up response--"+response);

            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("update response",s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONObject response = jsonObject.getJSONObject("response");
                String  status = response.getString("complete_status");
                String id = response.getString("id");
                Log.e("status of updation",status);
                EventBus.getDefault().post(new Event(Constants.update_status,id+","+status));

            } catch (JSONException ex) {
                ex.printStackTrace();
            }


        }
    }

}
