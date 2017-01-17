package com.localdelivery.driver.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.localdelivery.driver.R;
import com.localdelivery.driver.model.Beans.RatingBeans;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

/*
 * Created by rishav on 28/12/16.
 */

public class RatingsAdapter extends RecyclerView.Adapter<RatingsAdapter.ViewHolder> {

    private ArrayList<RatingBeans> list;

    public RatingsAdapter(ArrayList<RatingBeans> list) {
        this.list = list;
    }

    @Override
    public RatingsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_customer_ratings, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RatingsAdapter.ViewHolder holder, int position) {
        RatingBeans ratingBeans = list.get(position);

        holder.customerName.setText(ratingBeans.getCustomerName());
        holder.time.setText(ratingBeans.getTime());
        holder.customerRatings.setRating(Float.parseFloat(ratingBeans.getCustomerRatings()));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

     class ViewHolder extends RecyclerView.ViewHolder{
        private TextView customerName, time;
        private RatingBar customerRatings;
        private CircularImageView customerImage;

         ViewHolder(View itemView) {
            super(itemView);

            customerName = (TextView)itemView.findViewById(R.id.customerName);
            time = (TextView)itemView.findViewById(R.id.time);

            customerRatings = (RatingBar)itemView.findViewById(R.id.customerRatings);
            customerImage = (CircularImageView)itemView.findViewById(R.id.customerImage);
        }
    }
}
