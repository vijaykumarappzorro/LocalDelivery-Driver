package com.localdelivery.driver.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.localdelivery.driver.R;
import com.localdelivery.driver.controller.CompletedTripsManager;
import com.localdelivery.driver.controller.ModelManager;
import com.localdelivery.driver.model.Beans.CompletedTripsBeans;
import com.localdelivery.driver.model.Constants;
import com.localdelivery.driver.model.Event;
import com.localdelivery.driver.model.LDPreferences;
import com.localdelivery.driver.model.Operations;
import com.localdelivery.driver.views.adapters.CompletedTripsAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import cc.cloudist.acplibrary.ACProgressFlower;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by rishav on 2/1/17.
 */

public class CompletedTripsFragment extends Fragment {

    Context context;
    ArrayList<CompletedTripsBeans> tripsList;
    RecyclerView recyclerView;
    ACProgressFlower dialog;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = LayoutInflater.from(context).inflate(R.layout.fragment_completed_trips, container, false);

        initViews(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
       /* dialog  = new ACProgressFlower.Builder(getActivity()).build();
        dialog.show();*/
        ModelManager.getInstance().getCompletedTripsManager().CompletedTripsManager(context, Operations.completedHistory(context,
                LDPreferences.readString(context,"driver_id")));

    }

    public void initViews(View v) {

        recyclerView = (RecyclerView)v.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        tripsList = new ArrayList<>();

        //CompletedTripsBeans trips1 = new CompletedTripsBeans("C133223", "Rs. 250", "Rishav Kumar", "Mon, 02 Jan 16", "03:20 PM", "");
       // CompletedTripsBeans trips2 = new CompletedTripsBeans("D232443", "Rs. 200", "Vijay Kumar", "Mon, 02 Jan 16", "03:30 PM", "");
       //tripsList.add(trips1);
        //tripsList.add(trips2);

        CompletedTripsAdapter adapter = new CompletedTripsAdapter(context, tripsList);
        recyclerView.setAdapter(adapter);
    }
    @Override
    public void onStart() {
        super.onStart();

        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();

        EventBus.getDefault().unregister(this);
    }
    @Subscribe
    public void onEvent(Event event) {
        switch (event.getKey()) {

            case Constants.COMPLETEDTRIPS:
            //    dialog.dismiss();
                String recivemessage= event.getValue();
                int id =Integer.parseInt(recivemessage);



                if (id>0) {

                    CompletedTripsAdapter adapter = new CompletedTripsAdapter(context, CompletedTripsManager.completetriplist);
                    recyclerView.setAdapter(adapter);
                    break;
                }
                else {
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error")
                            .setContentText("There is not trips completed yet")
                            .show();

                }
        }
    }


}
