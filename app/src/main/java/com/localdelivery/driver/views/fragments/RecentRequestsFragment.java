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


public class RecentRequestsFragment extends Fragment {

    Context context;
    RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = LayoutInflater.from(context).inflate(R.layout.fragment_recent_requests, container, false);

        ModelManager.getInstance().getPendingRequestsManager().getRecentRequests(context, Operations.getPendingRequests(context,
                LDPreferences.readString(context, "driver_id")));

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

    @Subscribe
    public void onEvent(Event event) {
        switch (event.getKey()) {
            case Constants.PENDING_REQUESTS:
                RecentRequestAdapter adapter = new RecentRequestAdapter(context, PendingRequestsManager.recentRequestsList);
                recyclerView.setAdapter(adapter);
                break;
        }
    }
}
