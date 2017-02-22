package com.localdelivery.driver.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.localdelivery.driver.model.Beans.PendingRequestsBeans;
import com.localdelivery.driver.model.Constants;
import com.localdelivery.driver.model.Event;
import com.localdelivery.driver.model.HttpHandler;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/*
 * Created by rishav on 4/1/17.
 */

public class PendingRequestsManager {

    public static final String TAG = PendingRequestsManager.class.getSimpleName();
    public static ArrayList<PendingRequestsBeans> recentRequestsList;
    public static ArrayList<PendingRequestsBeans> allRequestsList;

    public void getRecentRequests(Context context, String params) {
        new ExecuteApi(context).execute(params);
    }

    public void getCompleteRequests(Context context, String params) {
        new ExecuteApiCompleteRequests(context).execute(params);
    }

    private class ExecuteApi extends AsyncTask<String, String, String> {

        Context context;

         ExecuteApi(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... strings) {
            recentRequestsList = new ArrayList<>();
            HttpHandler httpHandler = new HttpHandler();
            String response = httpHandler.makeServiceCall(strings[0]);
            Log.e(TAG, "recent_requests--"+response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                for (int i=0; i<2; i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    String request_id = jsonObject1.getString("request_id");
                    int id = Integer.parseInt(request_id);
                    if (id>0) {
                        String customer_id = jsonObject1.getString("customer_id");
                        String firstName = jsonObject1.getString("firstname");
                        String lastName = jsonObject1.getString("lastname");
                        String pickUp_location = jsonObject1.getString("pickup_location");
                        String drop_location = jsonObject1.getString("drop_location");
                        String price = jsonObject1.getString("price");
                        String source_lat = jsonObject1.getString("source_latitude");
                        String source_lng = jsonObject1.getString("source_longitude");
                        String dest_lat = jsonObject1.getString("destination_latitude");
                        String dest_lng = jsonObject1.getString("destination_longitude");
                        String product_descrption = jsonObject1.getString("product_desc");
                        String datetime = jsonObject1.getString("datetime");
                        String[] split = datetime.split(" ");
                        String date = split[split.length-2];
                        String time = split[split.length-1];

                        PendingRequestsBeans pendingRequestsBeans = new PendingRequestsBeans(request_id, customer_id,
                                firstName, lastName, pickUp_location, drop_location, price, source_lat, source_lng, dest_lat, dest_lng,product_descrption,date,time);
                        recentRequestsList.add(pendingRequestsBeans);
                        EventBus.getDefault().post(new Event(Constants.PENDING_REQUESTS, request_id+","+"true"));
                    }
                    else {

                            String message = jsonObject1.getString("message");
                        EventBus.getDefault().post(new Event(Constants.PENDING_REQUESTS, request_id+","+message));

                    }

                    /*EventBus.getDefault().post(new Event(Constants.PENDING_REQUESTS, firstName, lastName, request_id, customer_id,
                            pickUp_location, drop_location, price));*/
                }
               // EventBus.getDefault().post(new Event(Constants.PENDING_REQUESTS, ""));

            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
    }

    private class ExecuteApiCompleteRequests extends AsyncTask<String, String, String> {

        Context context;

        ExecuteApiCompleteRequests(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... strings) {
            allRequestsList = new ArrayList<>();
            HttpHandler httpHandler = new HttpHandler();
            String response = httpHandler.makeServiceCall(strings[0]);
            Log.e(TAG, "pending_requests--"+response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                for (int i=0; i<jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    String request_id = jsonObject1.getString("request_id");
                    int id = Integer.parseInt(request_id);
                    if (id>0) {
                        String customer_id = jsonObject1.getString("customer_id");
                        String firstName = jsonObject1.getString("firstname");
                        String lastName = jsonObject1.getString("lastname");
                        String pickUp_location = jsonObject1.getString("pickup_location");
                        String drop_location = jsonObject1.getString("drop_location");
                        String price = jsonObject1.getString("price");
                        String source_lat = jsonObject1.getString("source_latitude");
                        String source_lng = jsonObject1.getString("source_longitude");
                        String dest_lat = jsonObject1.getString("destination_latitude");
                        String dest_lng = jsonObject1.getString("destination_longitude");
                        String product_descrption = jsonObject1.getString("product_desc");
                        String datetime = jsonObject1.getString("datetime");
                        String[] split = datetime.split(" ");
                        String date = split[split.length-2];
                        String time = split[split.length-1];

                        PendingRequestsBeans pendingRequestsBeans = new PendingRequestsBeans(request_id, customer_id,
                                firstName, lastName, pickUp_location, drop_location, price, source_lat, source_lng, dest_lat, dest_lng,product_descrption,date,time);

                        allRequestsList.add(pendingRequestsBeans);
                        EventBus.getDefault().post(new Event(Constants.PENDING_REQUESTS, request_id+","+"true"));
                    }
                    else {

                        String message = jsonObject1.getString("message");
                        EventBus.getDefault().post(new Event(Constants.PENDING_REQUESTS, request_id+","+message));

                    }

                    /*EventBus.getDefault().post(new Event(Constants.PENDING_REQUESTS, firstName, lastName, request_id, customer_id,
                            pickUp_location, drop_location, price));*/
                }


            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
    }
}
