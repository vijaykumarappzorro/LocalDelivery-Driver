package com.localdelivery.driver.views.adapters;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.localdelivery.driver.R;
import com.localdelivery.driver.controller.ModelManager;
import com.localdelivery.driver.controller.PendingRequestsManager;
import com.localdelivery.driver.model.Beans.PendingRequestsBeans;
import com.localdelivery.driver.model.LDPreferences;
import com.localdelivery.driver.model.Operations;
import com.localdelivery.driver.model.Utility;

import java.util.ArrayList;
import java.util.Calendar;

import cc.cloudist.acplibrary.ACProgressFlower;

/*
 * Created by rishav on 4/1/17.
 */

public class RecentRequestAdapter extends RecyclerView.Adapter<RecentRequestAdapter.ViewHolder> {

    private Context context;
    private ArrayList<PendingRequestsBeans> list;
    Dialog dialog;
    ACProgressFlower dialog1;

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
    public void onBindViewHolder(RecentRequestAdapter.ViewHolder holder, final int position) {
        final PendingRequestsBeans pendingRequests = list.get(position);

        holder.customerName.setText(pendingRequests.getFirst_name() + " "+ pendingRequests.getLast_name());
      /*  holder.pickup_location.setText(pendingRequests.getPickup_location().replaceAll(",", ",\n"));
        holder.drop_location.setText(pendingRequests.getDrop_location().replaceAll(",", ",\n"));*/
        holder.pickup_location.setText(pendingRequests.getPickup_location());
        holder.drop_location.setText(pendingRequests.getDrop_location());
        holder.price.setText(pendingRequests.getPrice());
        holder.product_des.setText(pendingRequests.getDescription());
        holder.date.setText(pendingRequests.getDate());
        holder.time.setText(pendingRequests.getTime());
        holder.price.setText(pendingRequests.getPrice());
        holder.requestid.setText(pendingRequests.getRequest_id());
        holder.pickupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               dialog = Utility.createDialog(context);
                final PendingRequestsBeans pendingRequestsBeans = PendingRequestsManager.allRequestsList.get(position);
                TextView edtpick= (TextView) dialog.findViewById(R.id.picked_location);
                TextView edttitle= (TextView) dialog.findViewById(R.id.titlebar);
                TextView edtcustomer_price=(TextView)dialog.findViewById(R.id.customerprice);
                TextView edtdrop= (TextView) dialog.findViewById(R.id.destination);
                final EditText edtprice= (EditText)dialog.findViewById(R.id.price);
                edttitle.setText("Bid Form");
                edtcustomer_price.setText("Customer Price :"+pendingRequestsBeans.getPrice());



              final TextView  date = (TextView)dialog.findViewById(R.id.datepicker);
              final TextView  time = (TextView)dialog.findViewById(R.id.timepicker);
                Button requestbutton = (Button)dialog.findViewById(R.id.btnsend);
                Button dialogCloseButton = (Button)dialog.findViewById(R.id.btncancel);
                edtpick.setText(pendingRequestsBeans.getPickup_location());
                edtdrop.setText(pendingRequestsBeans.getDrop_location());
                edtprice.setTextColor(Color.parseColor("#000000"));
                date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Calendar c = Calendar.getInstance();
                        int  year = c.get(Calendar.YEAR);
                        int   month = c.get(Calendar.MONTH);
                        int day = c.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener(){
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {


                                date.setText(String.valueOf(dayOfMonth)+"-"+(month+1)+"-"+year);


                            }
                        },year,month,day);
                        datePickerDialog.show();;




                    }
                });
                time.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Calendar c = Calendar.getInstance();
                        int hour = c.get(Calendar.HOUR_OF_DAY);
                        int minute =c.get(Calendar.MINUTE);
                        int second =c.get(Calendar.SECOND);
                        TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                String AM_PM ;
                                if(hourOfDay < 12) {
                                    AM_PM = "AM";
                                } else {
                                    AM_PM = "PM";
                                }
                                time.setText(String.valueOf(hourOfDay)+":"+String.valueOf(minute)+" "+AM_PM);
                            }



                        },hour,minute,false);
                        timePickerDialog.show();

                    }
                });



                dialogCloseButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialog.dismiss();

                    }
                });
                requestbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        if (date.getText().toString().isEmpty()) {

                            date.setError("Required");
                        }
                        else if (time.getText().toString().isEmpty())
                        {
                            time.setError("Required Time");

                        }
                        else if (edtprice.getText().toString().isEmpty()){

                            edtprice.setError("Required price");

                        }
                        else {

                            // here sen your bid to the customer
                            dialog1 = new ACProgressFlower.Builder(context).build();
                            dialog1.show();
                            ModelManager.getInstance().getRequestAcecptManager().RequestAcecptManager(context, Operations.acceptRequestofCustomer(context,pendingRequestsBeans.getRequest_id(),
                                    pendingRequests.getCustomer_id(), LDPreferences.readString(context, "driver_id"),edtprice.getText().toString().trim(),
                                    date.getText().toString().replaceAll(" ",""),time.getText().toString().replaceAll(" ","")));
                            dialog.dismiss();
                        }

                    }
                });


                dialog.show();



            }
        });
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView customerName, pickup_location, drop_location, price,product_des,date,time,
                requestid;
      private   ImageView profile;
        private Button pickupbtn;

        public ViewHolder(View itemView) {
            super(itemView);

            customerName = (TextView)itemView.findViewById(R.id.customerName);
            pickup_location = (TextView)itemView.findViewById(R.id.pickup_address);
            drop_location = (TextView)itemView.findViewById(R.id.drop_address);
            price = (TextView)itemView.findViewById(R.id.price);
            product_des = (TextView)itemView.findViewById(R.id.products);
            date = (TextView)itemView.findViewById(R.id.date);
            time = (TextView)itemView.findViewById(R.id.time);
            profile=(ImageView)itemView.findViewById(R.id.profilepic);
            requestid =(TextView)itemView.findViewById(R.id.Requestid);
            pickupbtn =(Button) itemView.findViewById(R.id.pickup);



        }
    }
}
