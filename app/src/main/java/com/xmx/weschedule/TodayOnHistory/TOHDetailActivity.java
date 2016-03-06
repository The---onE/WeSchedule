package com.xmx.weschedule.TodayOnHistory;

import android.os.Bundle;
import android.widget.TextView;

import com.xmx.weschedule.ActivityBase.BaseTempActivity;
import com.xmx.weschedule.R;

public class TOHDetailActivity extends BaseTempActivity {

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_toh_detail);
        long id = getIntent().getLongExtra("id", -1);
        if (id != -1) {
            TextView title = getViewById(R.id.toh_title);
            TextView content = getViewById(R.id.toh_content);
            TOHManager.getInstance().setTodayOnHistoryDetail(id, title, content);
        }
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }
}
