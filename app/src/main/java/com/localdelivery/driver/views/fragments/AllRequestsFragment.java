package com.localdelivery.driver.views.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.localdelivery.driver.R;
import com.localdelivery.driver.controller.ModelManager;
import com.localdelivery.driver.controller.PendingRequestsManager;
import com.localdelivery.driver.model.Constants;
import com.localdelivery.driver.model.Event;
import com.localdelivery.driver.model.LDPreferences;
import com.localdelivery.driver.model.Operations;
import com.localdelivery.driver.views.adapters.RecentRequestAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import cc.cloudist.acplibrary.ACProgressFlower;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class AllRequestsFragment extends Fragment {

    Context context;
    RecyclerView recyclerView;
    ACProgressFlower dialog ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = LayoutInflater.from(context).inflate(R.layout.fragment_recent_requests, container, false);

        initViews(view);

        return view;
    }

    public void initViews(View v) {
        recyclerView = (RecyclerView)v.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

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

    @Override
    public void onResume() {
        super.onResume();
        dialog  = new ACProgressFlower.Builder(getActivity()).build();
        dialog.show();
        ModelManager.getInstance().getPendingRequestsManager().getCompleteRequests(context, Operations.getPendingRequests(context,
                LDPreferences.readString(context, "driver_id")));

    }

    @Subscribe
    public void onEvent(Event event) {
        switch (event.getKey()) {

            case Constants.PENDING_REQUESTS:
                dialog.dismiss();
                String recivemessage = event.getValue();
                String[] split = recivemessage.split(",");
                int id = Integer.parseInt(split[split.length - 2]);
                String message = split[split.length - 1];
                if (id > 0) {
                    RecentRequestAdapter adapter = new RecentRequestAdapter(context, PendingRequestsManager.allRequestsList);
                    recyclerView.setAdapter(adapter);
                    break;
                } else {
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error")
                            .setContentText(message)
                            .show();

                }

            case Constants.REQUESTACEPT:

                dialog.dismiss();
                String completemessage = event.getValue();
                String split1[] = completemessage.split(",");
                int reponseid = Integer.parseInt(split1[split1.length - 2]);
                String responsemessage = split1[split1.length - 1];
                if (reponseid > 0) {
                    new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText(responsemessage)
                            .setContentText("Please wait for some time until you dont any response from the Response")
                            .show();
                } else {
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText(responsemessage)
                            .setContentText("Your request is  not sent to Customer due to bad inernet connection please try again")
                            .show();
                }


        }
    }
}
