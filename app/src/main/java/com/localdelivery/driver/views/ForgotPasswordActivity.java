package com.localdelivery.driver.views;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.localdelivery.driver.R;
import com.localdelivery.driver.controller.ModelManager;
import com.localdelivery.driver.model.Constants;
import com.localdelivery.driver.model.Event;
import com.localdelivery.driver.model.Operations;
import com.localdelivery.driver.model.Utility;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import cc.cloudist.acplibrary.ACProgressFlower;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    Context context;
    Toolbar toolbar;
    Button reset;
    EditText editEmail;
    ACProgressFlower dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        context = this;
        initViews();

    }

    public void initViews() {
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Forgot Password");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        }

        reset =(Button)findViewById(R.id.btnreset);
        editEmail =(EditText)findViewById(R.id.edtemail);

        reset.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnreset:

               if (!Utility.emailValidator(editEmail.getText().toString())){
                   editEmail.setError("Please enter the valid email address");
                }
                else{
                    dialog = new ACProgressFlower.Builder(context).build();
                    dialog.show();
                    ModelManager.getInstance().getForgotPassManager().forgotPassword(context, Operations.forgotpassword(context,editEmail.getText().toString().trim(),"driver"));
                }
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        EventBus.getDefault().register(context);
    }

    @Override
    protected void onStop() {
        super.onStop();

        EventBus.getDefault().unregister(context);
    }

    @Subscribe
    public void onEvent(Event event) {

        switch (event.getKey()) {
            case Constants.forgotpasswprd:
                dialog.dismiss();
                String message = event.getValue();
                Log.e("message: ",message);
                String[] split = message.split(",");
                int id = Integer.parseInt(split[split.length-2]);
                String status = split[split.length-1];
                Toast.makeText(context, event.getValue(), Toast.LENGTH_SHORT).show();

                if (id > 0) {
                    new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Email sent successfully ")
                            .setContentText(status)
                            .show();
                }
                else {
                    new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error ")
                            .setContentText(status)
                            .show();
                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
