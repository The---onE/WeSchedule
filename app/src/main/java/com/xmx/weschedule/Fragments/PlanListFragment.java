package com.xmx.weschedule.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.xmx.weschedule.R;

/**
 */
public class PlanListFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plan_list, container, false);

        ListView lv = (ListView) view.findViewById(R.id.list_plan);


        return view;
    }

}
