package com.localdelivery.driver.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.localdelivery.driver.model.Beans.CompletedTripsBeans;
import com.localdelivery.driver.model.Constants;
import com.localdelivery.driver.model.Event;
import com.localdelivery.driver.model.HttpHandler;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by vijay on 30/1/17.
 */

public class CompletedTripsManager {


    private static final String TAG = CompletedTripsManager.class.getSimpleName();
    public static ArrayList<CompletedTripsBeans> completetriplist;


    public void CompletedTripsManager(Context context, String params) {

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
            completetriplist = new ArrayList<>();

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
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                for (int i = 0; i<jsonArray.length();i++){

                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                    String deliver_requestId= jsonObject1.getString("deliver_request_id");
                    int id = Integer.parseInt(deliver_requestId);
                    if (id>0){

                        String cash = jsonObject1.getString("cash");
                        String picklocation = jsonObject1.getString("pickup_location");
                        String droplocation = jsonObject1.getString("drop_location");
                        String distance = jsonObject1.getString("Distance");
                        String datetime = jsonObject1.getString("datetime");
                        String[] spilt = datetime.split(",");
                        String date = spilt[spilt.length-2];
                        String time = spilt[spilt.length-1];


                        JSONObject jsonObject2 = jsonObject1.getJSONObject("Customer Detail");
                        String customerid =  jsonObject2.getString("id");
                        String firstname = jsonObject2.getString("firstname");
                        String lastname = jsonObject2.getString("lastname");
                        String fullname = firstname+" "+lastname;
                        String  profilepic =jsonObject2.getString("profile_pic");
                        String mobile = jsonObject2.getString("mobile");

                        CompletedTripsBeans completedTripsBeans = new CompletedTripsBeans(deliver_requestId,cash,picklocation,
                                droplocation,distance,fullname,date,time,profilepic);
                        completetriplist.add(completedTripsBeans);

                        EventBus.getDefault().post(new Event(Constants.COMPLETEDTRIPS,deliver_requestId));

                    }
                    else{

                        String message = jsonObject1.getString("message");

                    }



                }

            //    EventBus.getDefault().post(new Event(Constants.update_status,id+","+status));

            } catch (JSONException ex) {
                ex.printStackTrace();
            }


        }
    }

}
