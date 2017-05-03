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

public class SignUpManager {

    private static final String TAG = SignUpManager.class.getSimpleName();


    public void getSignUpDetails(Context context,String url, String params) {

        new ExecuteApi(context).execute(url,params);
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
        protected String doInBackground(String... strings) {

            HttpHandler httpHandler = new HttpHandler();
            String response = httpHandler.getResponse(strings[0],strings[1]);
            Log.e(TAG, "sign-up response--"+response);

            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s!=null) {

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject response = jsonObject.getJSONObject("response");
                    int id = response.getInt("id");
                    String message = response.getString("message");

                    if (id >= 1) {

                        EventBus.getDefault().post(new Event(Constants.ACCOUNT_REGISTERED, ""));

                        //  new ChecktheVichleType(mContext).execute(Operations.checkviechletype(mContext, String.valueOf(id), "driver"));

                    } else

                        EventBus.getDefault().post(new Event(Constants.ACCOUNT_NOT_REGISTERED, message));

                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }
            else{

                EventBus.getDefault().post(new Event(Constants.SERVER_ERROR, ""));
            }


        }
    }


    private class ChecktheVichleType extends AsyncTask<String, String, String> {

        Context mContext;

        ChecktheVichleType(Context context) {
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

            Log.e("check response", "sign-up response--"+response);

            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("check response","of vechile"+s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                String completestatus= jsonObject1.getString("complete_status");
                String id = jsonObject1.getString("id");
                Log.e("status",completestatus);
                EventBus.getDefault().post(new Event(Constants.vechile_check, completestatus+","+id));


            } catch (JSONException ex) {
                ex.printStackTrace();
            }


        }
    }

  // check the provided vehicle type is update on the server or not

    private class Updatevehicletype extends AsyncTask<String, String, String> {

        Context mContext;

        Updatevehicletype(Context context) {
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

            Log.e("check response", "sign-up response--"+response);

            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("upate response","of vechile"+s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONObject jsonObject1 = jsonObject.getJSONObject("response");




            } catch (JSONException ex) {
                ex.printStackTrace();
            }


        }
    }
}
