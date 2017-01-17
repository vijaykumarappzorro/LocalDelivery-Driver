package com.localdelivery.driver.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.localdelivery.driver.R;
import com.localdelivery.driver.model.Beans.RatingBeans;
import com.localdelivery.driver.views.adapters.RatingsAdapter;

import java.util.ArrayList;

public class RatingScreenActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Toolbar toolbar;
    ArrayList<RatingBeans> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_screen);

        initViews();
    }

    public void initViews() {
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Ratings");

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView)findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        list = new ArrayList<>();

        RatingBeans ratingBeans = new RatingBeans("Vijay", "", "4", "a minute ago");
        RatingBeans ratingBeans2 = new RatingBeans("Rishav", "", "5", "an hour ago");
        list.add(ratingBeans);
        list.add(ratingBeans2);

        RatingsAdapter adapter = new RatingsAdapter(list);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                break;

        }

        return true;
    }
}
