package com.xmx.weschedule.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.xmx.weschedule.Datepicker.cons.DPMode;
import com.xmx.weschedule.Datepicker.views.DatePicker;
import com.xmx.weschedule.R;

import java.util.Calendar;

/**
 *
 */
public class CalendarFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        DatePicker picker = (DatePicker) view.findViewById(R.id.date_picker);
        Calendar calendar = Calendar.getInstance();
        picker.setDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);
        picker.setMode(DPMode.SINGLE);
        picker.setOnDatePickedListener(new DatePicker.OnDatePickedListener() {
            @Override
            public void onDatePicked(String date) {
                Toast.makeText(getContext(), date, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
