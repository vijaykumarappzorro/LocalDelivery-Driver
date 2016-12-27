package com.localdelivery.driver.views.fragment.activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.localdelivery.driver.R;

/**
 * Created by vijay on 26/12/16.
 */

public class AllRequest extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.recent_request_adapter, container, false);
    }
}
