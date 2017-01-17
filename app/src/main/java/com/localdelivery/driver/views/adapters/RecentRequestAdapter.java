package com.localdelivery.driver.views.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.localdelivery.driver.R;
import com.localdelivery.driver.model.Beans.PendingRequestsBeans;

import java.util.ArrayList;

/*
 * Created by rishav on 4/1/17.
 */

public class RecentRequestAdapter extends RecyclerView.Adapter<RecentRequestAdapter.ViewHolder> {

    private Context context;
    private ArrayList<PendingRequestsBeans> list;

    public RecentRequestAdapter(Context context, ArrayList<PendingRequestsBeans> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecentRequestAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_recent_requests, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecentRequestAdapter.ViewHolder holder, int position) {
        PendingRequestsBeans pendingRequests = list.get(position);

        holder.customerName.setText(pendingRequests.getFirst_name() + " "+ pendingRequests.getLast_name());
        holder.pickup_location.setText(pendingRequests.getPickup_location().replaceAll(",", ",\n"));
        holder.drop_location.setText(pendingRequests.getDrop_location().replaceAll(",", ",\n"));
        holder.price.setText(pendingRequests.getPrice());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView customerName, pickup_location, drop_location, price;

        public ViewHolder(View itemView) {
            super(itemView);

            customerName = (TextView)itemView.findViewById(R.id.customerName);
            pickup_location = (TextView)itemView.findViewById(R.id.pickup_address);
            drop_location = (TextView)itemView.findViewById(R.id.drop_address);
            price = (TextView)itemView.findViewById(R.id.price);

        }
    }
}
