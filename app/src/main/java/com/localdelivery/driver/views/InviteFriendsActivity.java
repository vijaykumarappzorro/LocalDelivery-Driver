package com.localdelivery.driver.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.localdelivery.driver.R;

//  This activity used for invite to other friend ..... Created By VIJAY KUMAR

public class InviteFriendsActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView instruction,heading;
    Button invite;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friends);
    }

 /*-----------   here intializing all widgt of the activity */
     public void intilizing(){
         toolbar = (Toolbar)findViewById(R.id.toolbar);
         toolbar.setTitle("Invite A Friend");
         setSupportActionBar(toolbar);
         if (getSupportActionBar() != null) {
             getSupportActionBar().setDisplayHomeAsUpEnabled(true);
             getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
         }

         instruction =(TextView)findViewById(R.id.txtinstruction);
         heading =(TextView)findViewById(R.id.txtheading);
         invite =(Button)findViewById(R.id.btninvite);
     }


/*---------------  the click listner of the buton and share this app to other friends using invite button */
public void inviteClick(View view){


    }

}
