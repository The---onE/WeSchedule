package com.xmx.weschedule.DateInformation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xmx.weschedule.ActivityBase.BaseTempActivity;
import com.xmx.weschedule.Constants;
import com.xmx.weschedule.R;
import com.xmx.weschedule.Schedule.AddScheduleActivity;
import com.xmx.weschedule.TodayOnHistory.TOHActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class DateInformationActivity extends BaseTempActivity {
    int year, month, day;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_date_information);

        year = getIntent().getIntExtra("year", 1900);
        month = getIntent().getIntExtra("month", 1);
        day = getIntent().getIntExtra("day", 1);

        DateManager.getInstance().getDateInformation(year, month, day, new GetJsonCallback() {
            @Override
            public void start() {
                TextView yangliView = getViewById(R.id.date_yangli);
                yangliView.setText(year + "年" + month + "月" + day + "日");

                Date date = new Date();
                date.setYear(year - 1900);
                date.setMonth(month - 1);
                date.setDate(day);
                String week = Constants.DAY_OF_WEEK[date.getDay()];
                TextView weekView = getViewById(R.id.date_week);
                weekView.setText(week);

                TextView dayView = getViewById(R.id.date_day);
                dayView.setText("" + day);

                TextView yinliView = getViewById(R.id.date_yinli);
                yinliView.setText(getString(R.string.loading));
            }

            @Override
            public void success(JSONObject json) {
                try {
                    JSONObject result = json.getJSONObject("result");
                    String yangli = result.getString("yangli");
                    String yinli = result.getString("yinli");
                    String wuxing = result.getString("wuxing");
                    String chongsha = result.getString("chongsha");
                    String baiji = result.getString("baiji");
                    String jishen = result.getString("jishen");
                    String yi = result.getString("yi");
                    String xiongshen = result.getString("xiongshen");
                    String ji = result.getString("ji");

                    TextView yinliView = getViewById(R.id.date_yinli);
                    yinliView.setText("" + yinli);
                    TextView wuxingView = getViewById(R.id.date_wuxing);
                    wuxingView.setText("五行：" + wuxing);
                    TextView chongshaView = getViewById(R.id.date_chongsha);
                    chongshaView.setText("冲煞：" + chongsha);
                    TextView baijiView = getViewById(R.id.date_baiji);
                    baijiView.setText("" + baiji);
                    TextView jishenView = getViewById(R.id.date_jishen);
                    jishenView.setText("吉神宜趋：" + jishen);
                    TextView yiView = getViewById(R.id.date_yi);
                    yiView.setText("宜：" + yi);
                    TextView xiongshenView = getViewById(R.id.date_xiongshen);
                    xiongshenView.setText("凶神宜忌：" + xiongshen);
                    TextView jiView = getViewById(R.id.date_ji);
                    jiView.setText("忌：" + ji);

                } catch (JSONException e) {
                    e.printStackTrace();
                    showToast("JSON Exception");
                }
            }
        });
    }

    @Override
    protected void setListener() {
        Button toh = getViewById(R.id.btn_toh);
        toh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DateInformationActivity.this, TOHActivity.class);
                intent.putExtra("year", year);
                intent.putExtra("month", month);
                intent.putExtra("day", day);
                startActivity(intent);
            }
        });

        Button addSchedule = getViewById(R.id.btn_add_schedule);
        addSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DateInformationActivity.this, AddScheduleActivity.class);
                intent.putExtra("year", year);
                intent.putExtra("month", month);
                intent.putExtra("day", day);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }
}
