package com.xmx.weschedule.Schedule;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.xmx.weschedule.ActivityBase.BaseTempActivity;
import com.xmx.weschedule.Database.SQLManager;
import com.xmx.weschedule.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddScheduleActivity extends BaseTempActivity {
    int year, month, day;
    Date scheduleTime;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_add_schedule);

        year = getIntent().getIntExtra("year", 1900);
        month = getIntent().getIntExtra("month", 1);
        day = getIntent().getIntExtra("day", 1);

        Date date = new Date();
        date.setYear(year - 1900);
        date.setMonth(month - 1);
        date.setDate(day);
        scheduleTime = date;
    }

    @Override
    protected void setListener() {
        final TextView timeView = getViewById(R.id.time);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String time = df.format(scheduleTime);
        timeView.setText(time);
        timeView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        final TimePickerView pvTime = new TimePickerView(this, TimePickerView.Type.HOURS_MINS);
        pvTime.setTime(new Date());
        pvTime.setCancelable(true);
        pvTime.setCyclic(true);
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                date.setYear(year - 1900);
                date.setMonth(month - 1);
                date.setDate(day);
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String time = df.format(date);
                timeView.setText(time);

                scheduleTime = date;
            }
        });
        timeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvTime.show();
            }
        });

        Button ok = getViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView titleText = getViewById(R.id.title);
                if (titleText.getText().toString().equals("")) {
                    showToast("请输入标题");
                    return;
                }
                String title = titleText.getText().toString();

                TextView textText = getViewById(R.id.text);
                String text = textText.getText().toString();

                long id = SQLManager.getInstance().insertSchedule(title, text, scheduleTime, 0);
                if (id > 0) {
                    showToast("添加成功");
                    finish();
                }
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }
}
