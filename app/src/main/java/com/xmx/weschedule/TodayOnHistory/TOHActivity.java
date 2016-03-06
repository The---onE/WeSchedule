package com.xmx.weschedule.TodayOnHistory;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xmx.weschedule.ActivityBase.BaseTempActivity;
import com.xmx.weschedule.R;

import java.util.Calendar;

public class TOHActivity extends BaseTempActivity {
    ListView mTOHList;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_toh);

        Calendar date = Calendar.getInstance();
        int month = date.get(Calendar.MONTH) + 1;
        int day = date.get(Calendar.DAY_OF_MONTH);
        month = getIntent().getIntExtra("month", month);
        day = getIntent().getIntExtra("day", day);

        mTOHList = getViewById(R.id.toh_list);
        TOHManager.getInstance().setTodayOnHistoryList(month, day, mTOHList);

        mTOHList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (id != -1) {
                    Intent intent = new Intent(getBaseContext(), TOHDetailActivity.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void setListener() {
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }
}
