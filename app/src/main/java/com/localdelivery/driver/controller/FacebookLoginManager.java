package com.localdelivery.driver.controller;

import android.app.Activity;
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
import com.facebook.login.*;
import com.localdelivery.driver.model.Constants;
import com.localdelivery.driver.model.Event;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import static com.facebook.GraphRequest.TAG;

/**
 * Created by rishav on 27/12/16.
 */

public class FacebookLoginManager {

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

    // get the detail of facebook ..........................
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
                                String first_name = split[split.length-2];
                                String last_name = split[split.length-1];
                                String email = "";
                                try {
                                    email = data.getString("email");
                                } catch (JSONException ex) {
                                    ex.printStackTrace();
                                }

                                String profilePicUrl="";
                                Log.e("user id",id);
                                if(data.has("picture")) {
                                    profilePicUrl = data.getJSONObject("picture").getJSONObject("data").getString("url");

                                    Log.e(TAG, "Image: "+profilePicUrl);
                                    if(email != null) {
                                        EventBus.getDefault().post(new Event(Constants.FACEBOOK_LOGIN_SUCCESS,
                                                id, profilePicUrl, first_name, last_name, email));
                                    }
                                    else{
                                        Toast.makeText(context, "please register with simple user", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).executeAsync();
    }
}
