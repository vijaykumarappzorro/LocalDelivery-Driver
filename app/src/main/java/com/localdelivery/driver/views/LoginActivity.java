package com.localdelivery.driver.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.iid.FirebaseInstanceId;
import com.localdelivery.driver.R;
import com.localdelivery.driver.controller.ModelManager;
import com.localdelivery.driver.model.Constants;
import com.localdelivery.driver.model.Event;
import com.localdelivery.driver.model.LDPreferences;
import com.localdelivery.driver.model.Operations;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import cc.cloudist.acplibrary.ACProgressFlower;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    Activity mContext;
    ACProgressFlower dialog;
    EditText editEmail, editPassword;
    ImageView img_fb;
    TextView txtresetpassword;
    CallbackManager callbackManager;
    String device_token;
    private final static String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_login);

        mContext = this;

     device_token = FirebaseInstanceId.getInstance().getToken();
        initViews();

    }

    public void initViews() {
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Login");
        setSupportActionBar(toolbar);
        editEmail = (EditText)findViewById(R.id.editEmail);
        editPassword = (EditText)findViewById(R.id.editPassword);

        callbackManager = CallbackManager.Factory.create();

        img_fb = (ImageView)findViewById(R.id.img_fb);

        img_fb.setOnClickListener(this);
        txtresetpassword =(TextView)findViewById(R.id.txtforgotpassword);
        device_token = FirebaseInstanceId.getInstance().getToken();
        Log.e("ddd",device_token);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.img_fb) {
            ModelManager.getInstance().getFacebookLoginManager().doFacebookLogin(mContext, callbackManager);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        ModelManager.getInstance().getFacebookLoginManager().getFacebookData(mContext);

    }

    public void loginBtn(View v) {

        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(mContext, "Please fill all the details", Toast.LENGTH_SHORT).show();
            return;
        }

        dialog = new ACProgressFlower.Builder(this).build();
        dialog.show();

        ModelManager.getInstance().getLoginManager().getLoginData(mContext, Operations.getLoginDetails(mContext, email, password,
              device_token, "A", "driver", "30.710252502759392", "76.70373268425465"));
    }

    public void forgotPassword(View v) {

        Intent intent = new Intent(LoginActivity.this,ForgotPasswordActivity.class);
        startActivity(intent);
    }

    public void registerBtn(View v) {
        Intent i = new Intent(mContext, SignUpActivity.class);
        startActivity(i);
    }

    @Override
    protected void onStart() {
        super.onStart();

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(Event event) {

        switch (event.getKey()) {
            case Constants.LOGIN_SUCCESS:

                String loginStatus = event.getValue();
                LDPreferences.putString(mContext, "status", "logged");
                ModelManager.getInstance().getUserDetailManager().getUserDetails(mContext,Operations.getUserDetail(mContext,loginStatus,"driver"));

                break;

            case Constants.ACCOUNT_NOT_REGISTERED:

                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error ")
                        .setContentText(event.getValue())
                        .show();
                break;

            case Constants.FACEBOOK_LOGIN_SUCCESS:
                Toast.makeText(LoginActivity.this,"facebooklogin",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                intent.putExtra("facebooklogin","yes");
                intent.putExtra("firstname",event.getFisrtname());
                intent.putExtra("lastname",event.getLastname());
                intent.putExtra("profileimage",event.getImageUrl());
                intent.putExtra("emailid",event.getEmailid());
                startActivity(intent);
                break;

            case Constants.USER_DETAILS_SUCCESS:

                dialog.dismiss();

                Intent intent1 = new Intent(mContext,HomePageActivity.class);
                startActivity(intent1);
                finish();
                break;

        }
    }


}
