package com.localdelivery.driver.views.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.localdelivery.driver.R;

/**
 * Created by rishav on 2/1/17.
 */

public class PendingTripsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_pending_trips, container, false);
    }
}
