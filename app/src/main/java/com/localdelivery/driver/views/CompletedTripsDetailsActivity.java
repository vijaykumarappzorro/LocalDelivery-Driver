package com.localdelivery.driver.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.localdelivery.driver.R;
import com.localdelivery.driver.controller.CompletedTripsManager;
import com.localdelivery.driver.model.Beans.CompletedTripsBeans;

import java.util.ArrayList;


public class CompletedTripsDetailsActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView customerName, cash, orderId, tripDate,tripTime,picadd,dropadd,basefare,txtmiles,txtmilesfare,
            txtminute,txttimefare,txtsubtotal;
    String customer_name, price, order_id, trip_date, trip_time;
    ArrayList<CompletedTripsBeans>list;
    int postion1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_trips_details);

        initViews();

    }

    public void initViews() {
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(getIntent().getStringExtra("trip_date"));
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        customerName = (TextView)findViewById(R.id.customerName);
        cash = (TextView)findViewById(R.id.cash);
        orderId = (TextView)findViewById(R.id.orderId);
        tripDate = (TextView)findViewById(R.id.tripDate);
        tripTime = (TextView)findViewById(R.id.tripTime);
        picadd =(TextView)findViewById(R.id.picaddress);
        dropadd=(TextView)findViewById(R.id.dropaddress);
        basefare=(TextView)findViewById(R.id.farevalue);
        txtmiles=(TextView)findViewById(R.id.milesvalue);
        txtmilesfare=(TextView)findViewById(R.id.mailesfare);
        txtminute =(TextView)findViewById(R.id.timevalue) ;
        txttimefare=(TextView)findViewById(R.id.timefare);
        txtsubtotal=(TextView)findViewById(R.id.subtotal);


        getValues();
    }

    public void getValues() {
        Intent i = getIntent();
        customer_name = i.getStringExtra("customer_name");
        price = i.getStringExtra("cash");
        order_id = i.getStringExtra("order_id");
        trip_date = i.getStringExtra("trip_date");
        trip_time = i.getStringExtra("trip_time");
        String postion  =i.getStringExtra("postion");
        postion1 = Integer.parseInt(postion);

        CompletedTripsBeans completedTripsBeans= CompletedTripsManager.completetriplist.get(postion1);



        customerName.setText(completedTripsBeans.getCustomerName());

        cash.setText(completedTripsBeans.getPrice());
        orderId.setText(completedTripsBeans.getOrderId());
        tripDate.setText("Date: "+completedTripsBeans.getTripDate());
        tripTime.setText("Time: "+completedTripsBeans.getTripTime());
        picadd.setText(completedTripsBeans.getPicklocaton());
        dropadd.setText(completedTripsBeans.getDroplocation());
        txtmiles.setText(completedTripsBeans.getDistance());
        txtsubtotal.setText(completedTripsBeans.getPrice());




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