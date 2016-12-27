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
import android.widget.TextView;
import android.widget.Toast;

import com.localdelivery.driver.R;
import com.localdelivery.driver.controller.ModelManager;
import com.localdelivery.driver.model.Constants;
import com.localdelivery.driver.model.Event;
import com.localdelivery.driver.model.Operations;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cc.cloudist.acplibrary.ACProgressFlower;
import cn.pedant.SweetAlert.SweetAlertDialog;

//This Activity used for reset the pasword of the user and send the reset link to registered mail id ...CREATED BY VIJAY KUMAR

public class ForgotPasswordActivity extends AppCompatActivity {
    Context context;
    Toolbar toolbar;
    TextView instrution;
    Button reset;
    EditText emailid;
    ACProgressFlower dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        context = this;
        initViews();


        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (emailid.getText().toString().isEmpty()){
                    emailid.setError("Required");
                }
                else if (!emailValidator(emailid.getText().toString())){

                    emailid.setError("Please fill the valid email id");
                }
                else{
                    dialog = new ACProgressFlower.Builder(context).build();
                    dialog.show();
                    ModelManager.getInstance().getForgotPassManager().ForgotPassManager(context, Operations.forgotpassword(context,emailid.getText().toString().trim(),"driver"));
                }
            }
        });
    }
 /*------------    here intilize the all widght  and set the tool bar title-------*/

    public void initViews() {
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Forgot Password");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        }

        instrution =(TextView)findViewById(R.id.txtprotocol);
        reset =(Button)findViewById(R.id.btnreset);
        emailid =(EditText)findViewById(R.id.edtemail);

    }

/*------------------ This fuction used for validiate email domain--------*/

    public boolean emailValidator(String email)
    {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
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
                Log.e("message recieved",message);
                String[] split = message.split(",");
                int id = Integer.parseInt(split[split.length-2]);
                String status = split[split.length-1];
                Toast.makeText(context, event.getValue(), Toast.LENGTH_SHORT).show();

                if (id>0){
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
