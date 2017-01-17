package com.localdelivery.driver.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.localdelivery.driver.R;


public class CompletedTripsDetailsActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView customerName, cash, orderId, tripDate,tripTime;
    String customer_name, price, order_id, trip_date, trip_time;

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

        getValues();
    }

    public void getValues() {
        Intent i = getIntent();
        customer_name = i.getStringExtra("customer_name");
        price = i.getStringExtra("cash");
        order_id = i.getStringExtra("order_id");
        trip_date = i.getStringExtra("trip_date");
        trip_time = i.getStringExtra("trip_time");

        customerName.setText(customer_name);
        cash.setText(price);
        orderId.setText(order_id);
        tripDate.setText(String.format("Date: %s", trip_date));
        tripTime.setText(String.format("Time: %s", trip_time));
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