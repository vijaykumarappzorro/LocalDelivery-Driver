package com.localdelivery.driver.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

import com.localdelivery.driver.R;

public class InviteFriendsActivity extends AppCompatActivity {
    Toolbar toolbar;
    Button invite;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friends);

        initViews();
    }

     public void initViews(){
         toolbar = (Toolbar)findViewById(R.id.toolbar);
         toolbar.setTitle("Invite A Friend");
         setSupportActionBar(toolbar);

         if (getSupportActionBar() != null) {
             getSupportActionBar().setDisplayHomeAsUpEnabled(true);
             getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
         }

         invite =(Button)findViewById(R.id.btninvite);
     }

}
