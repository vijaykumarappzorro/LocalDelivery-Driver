package com.localdelivery.driver.controller;


import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.localdelivery.driver.model.Constants;
import com.localdelivery.driver.model.Event;
import com.localdelivery.driver.model.HttpHandler;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class LoginManager {

    private static final String TAG = LoginManager.class.getSimpleName();

    public void getLoginData(Context context, String params) {
        new ExecuteApi(context).execute(params);
    }
// Login using facebook account.............
    public void doFacebookLogin(final Activity context, CallbackManager callbackManager) {



        com.facebook.login.LoginManager.getInstance().logInWithReadPermissions(context,
                Arrays.asList("email", "user_friends", "public_profile")
        );

        com.facebook.login.LoginManager.getInstance().registerCallback(
                callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // Handle success
                        Toast.makeText(context, "Logged in", Toast.LENGTH_SHORT).show();
                        Log.e("loginResult",String.valueOf(loginResult));
                        Log.e(TAG, "Id: "+loginResult.getAccessToken().getUserId());
                        Log.e(TAG, "Token: "+loginResult.getAccessToken().getToken());

                    }
                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException exception) {
                        exception.printStackTrace();
                    }
                }
        );

    }
 // get the deatil of facebook ..........................
    public void getFacebookData(final Activity context) {
        Bundle params = new Bundle();
        params.putString("fields", "id,name,email,picture.type(large)");
        new GraphRequest(AccessToken.getCurrentAccessToken(), "me", params, HttpMethod.GET,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        if (response != null) {
                            try {
                                JSONObject data = response.getJSONObject();
                                Log.e(TAG, "Data: "+data);
                                String id = data.getString("id");
                                String name = data.getString("name");
                                String[] split = name.split(" ");
                                String firstname = split[split.length-2];
                                String lastname = split[split.length-1];
                                String email = data.getString("email");
                                String profilePicUrl="";
                                Log.e("user id",id);
                                if(data.has("picture")) {
                                     profilePicUrl = data.getJSONObject("picture").getJSONObject("data").getString("url");
                                    // Bitmap profilePic = BitmapFactory.decodeStream(profilePicUrl.openConnection().getInputStream());
                                    // set profilePic bitmap to imageview
                                    Log.e(TAG, "Image: "+profilePicUrl);
                                    if(email!=null) {
                                        EventBus.getDefault().post(new Event(Constants.FACEBOOK_LOGIN_SUCCESS,
                                                id, profilePicUrl, firstname, lastname, email));
                                    }
                                    else{

                                        Toast.makeText(context, "please register with simple user", Toast.LENGTH_SHORT).show();

                                    }
                                    return;
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).executeAsync();
    }
// Simple login ..
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
                if (id >= 1)
                    EventBus.getDefault().post(new Event(Constants.LOGIN_SUCCESS, String.valueOf(id)+","+message));
                else
                    EventBus.getDefault().post(new Event(Constants.ACCOUNT_NOT_REGISTERED, message));

            } catch (JSONException ex) {
                ex.printStackTrace();
            }


        }
    }

}
