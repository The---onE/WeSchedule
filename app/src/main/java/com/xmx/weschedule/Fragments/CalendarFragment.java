package com.xmx.weschedule.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xmx.weschedule.DateInformation.DateInformationActivity;
import com.xmx.weschedule.Datepicker.cons.DPMode;
import com.xmx.weschedule.Datepicker.views.DatePicker;
import com.xmx.weschedule.R;
import com.xmx.weschedule.TodayOnHistory.TOHActivity;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
                Intent intent = new Intent(getContext(), DateInformationActivity.class);

                String regex = "(.+?)-(\\d+)-(\\d+)"; //格式为yyyy-M-d
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(date);
                int year = 1900;
                int month = 1;
                int day = 1;
                if (matcher.find()) {
                    year = Integer.valueOf(matcher.group(1));
                    month = Integer.valueOf(matcher.group(2));
                    day = Integer.valueOf(matcher.group(3));
                }
                intent.putExtra("year", year);
                intent.putExtra("month", month);
                intent.putExtra("day", day);

                startActivity(intent);
            }
        });

        return view;
    }
}
