package com.localdelivery.driver.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.localdelivery.driver.R;
import com.localdelivery.driver.controller.ModelManager;
import com.localdelivery.driver.model.Constants;
import com.localdelivery.driver.model.Event;
import com.localdelivery.driver.model.Operations;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import cc.cloudist.acplibrary.ACProgressFlower;
import cn.pedant.SweetAlert.SweetAlertDialog;

/*--------- this is login screen of the app here  user can login if already register with us and also choose the facebook login
option , if user choose the facebook login the first user check facebook detail and match with own server if the email id already register with us
then check the vicle type if already exit then go to the home screen of the app ---------------------Created by Vijay Kumar
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    Activity mContext;
    ACProgressFlower dialog;
    EditText editEmail, editPassword;
    ImageView img_fb;
    SharedPreferences userdetailSharedP,loginSharedP;
    SharedPreferences.Editor editor,editor1;
    TextView txtresetpassword;
    CallbackManager callbackManager;
    private final static String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_login);


        mContext = this;

        initViews();

       /* try {
            PackageInfo info = getPackageManager().getPackageInfo("com.localdelivery.driver", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("MY KEY HASH:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }*/
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
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.img_fb) {
           ModelManager.getInstance().getLoginManager().doFacebookLogin(mContext, callbackManager);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        ModelManager.getInstance().getLoginManager().getFacebookData(mContext);

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
                "token", "A", "driver", "12.223231", "12.121213"));
    }
    public void forgottext(View v){

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
                dialog.dismiss();
                loginSharedP = getSharedPreferences("LOGINDETAIL", Context.MODE_PRIVATE);
                editor1 = loginSharedP.edit();

                String loginstatus = event.getValue();
                String[] split = loginstatus.split(",");
                int id = Integer.parseInt(split[split.length-2]);
                String message = split[split.length-1];
                editor1.putString("status","logged").apply();
                dialog = new ACProgressFlower.Builder(this).build();
                dialog.show();
                ModelManager.getInstance().getUserDetailManager().getuserDeatil(mContext,Operations.getUserDeatil(mContext,String.valueOf(id),"driver"));

                break;
            case Constants.ACCOUNT_NOT_REGISTERED:

                Toast.makeText(mContext, event.getValue(), Toast.LENGTH_SHORT).show();
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
               // Toast.makeText(mContext, "Name: "+event.getName()+ "\n Id: "+event.getId(), Toast.LENGTH_SHORT).show();
                break;


            case Constants.userfullDetail:

                dialog.dismiss();

                Toast.makeText(mContext,"done"+event.getValue(),Toast.LENGTH_LONG).show();
                userdetailSharedP = getSharedPreferences("USERDEATIL", Context.MODE_PRIVATE);
                editor = userdetailSharedP.edit();
                Log.e("after login","you can get the user detail"+event.getValue());
                try {
                    JSONObject jsonObject = new JSONObject(event.getValue());
                    JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                    String userid = jsonObject1.getString("id");
                    String firstname = jsonObject1.getString("firstname");
                    String lastname = jsonObject1.getString("lastname");
                    String fullname = firstname+" "+lastname;
                    String emailid = jsonObject1.getString("email");
                    String mobile =  jsonObject1.getString("mobile");
                    String vehicletype = jsonObject1.getString("vehicle_type");
                    String profilepic = jsonObject1.getString("profile_pic");

/*-----------   save the all detail of the user  locally ---------------------------------*/

                    editor.putString("name",fullname).apply();
                    editor.putString("emailid",emailid).apply();
                    editor.putString("mobile",mobile).apply();
                    editor.putString("userid",userid).apply();
                    editor.putString("profilepic",profilepic).apply();

                    Intent intent1 = new Intent(mContext,HomePageActivity.class);
                    startActivity(intent1);
                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

        }
    }


}