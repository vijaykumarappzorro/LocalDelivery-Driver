package com.localdelivery.driver.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import com.localdelivery.driver.R;

public class SettingActivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageView imgedit1,imageedit2,imageedit3,imageedit4,driverimae,carimage;
    EditText edtname,edtcarname,edtphone,edtemail;
    MenuItem shareditem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initViews();



    }

/*------------ here all widgt are intilize of Setting screen-------------------------*/
    public void initViews() {
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Setting");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        }
        edtname =(EditText)findViewById(R.id.edtname);
        edtcarname =(EditText)findViewById(R.id.edtcarname);
        edtphone =(EditText)findViewById(R.id.edtphonenumer);
        edtemail =(EditText)findViewById(R.id.edtemailid);
        driverimae =(ImageView)findViewById(R.id.driverimage);
        carimage =(ImageView)findViewById(R.id.carimage);
        HideEditText();

    }
    public void HideEditText(){

        edtname.setEnabled(false);
        edtname.setClickable(false);
        edtemail.setEnabled(false);
        edtemail.setClickable(false);
        edtphone.setClickable(false);
        edtphone.setEnabled(false);
        edtcarname.setEnabled(false);
        edtcarname.setEnabled(false);
    }
    public void enableeditText(){


        edtname.setEnabled(true);
        edtname.setClickable(true);
        edtemail.setEnabled(true);
        edtemail.setClickable(true);
        edtphone.setClickable(true);
        edtphone.setEnabled(true);
        edtcarname.setEnabled(true);
        edtcarname.setEnabled(true);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bar, menu);
        shareditem = menu.findItem(R.id.tick);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.tick:
                HideEditText();
                shareditem.setVisible(false);
                break;
            case R.id.edit:
                shareditem.setVisible(true);
                enableeditText();
                break;

        }


      /*  //noinspection SimplifiableIfStatement
        if (id == R.id.tick) {


            return true;
        }
        else  if (id==R.id.edit){



        }
*/
        return super.onOptionsItemSelected(item);
    }

}
