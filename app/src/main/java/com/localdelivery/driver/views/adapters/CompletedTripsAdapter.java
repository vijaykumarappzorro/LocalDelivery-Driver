package com.localdelivery.driver.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.localdelivery.driver.R;
import com.localdelivery.driver.model.Beans.CompletedTripsBeans;
import com.localdelivery.driver.views.CompletedTripsDetailsActivity;

import java.util.ArrayList;

/**
 *  Created by rishav on 2/1/17.
 */

public class CompletedTripsAdapter extends RecyclerView.Adapter<CompletedTripsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<CompletedTripsBeans> list;

    public CompletedTripsAdapter(Context context, ArrayList<CompletedTripsBeans> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public CompletedTripsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_completed_trips, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CompletedTripsAdapter.ViewHolder holder, final int position) {
        final CompletedTripsBeans trips = list.get(position);

        holder.customerName.setText(trips.getCustomerName());
        holder.orderId.setText(trips.getOrderId());
        holder.cash.setText(trips.getPrice());
        holder.tripDate.setText("Date: "+trips.getTripDate());
        holder.tripTime.setText("Time: "+trips.getTripTime());

        holder.viewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, CompletedTripsDetailsActivity.class);
                i.putExtra("postion",String.valueOf(position));
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

     class ViewHolder extends RecyclerView.ViewHolder{
        private TextView customerName, cash, orderId, tripDate, tripTime, viewDetails;

        private ViewHolder(View itemView) {
            super(itemView);

            customerName = (TextView)itemView.findViewById(R.id.customerName);
            cash = (TextView)itemView.findViewById(R.id.cash);
            orderId = (TextView)itemView.findViewById(R.id.orderId);
            tripDate = (TextView)itemView.findViewById(R.id.tripDate);
            tripTime = (TextView)itemView.findViewById(R.id.tripTime);
            viewDetails = (TextView)itemView.findViewById(R.id.viewDetails);
        }
    }
}
