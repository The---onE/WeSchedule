package com.xmx.weschedule;

import com.xmx.weschedule.TodayOnHistory.TOHManager;

/**
 * Created by The_onE on 2016/1/3.
 */
public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();

        TOHManager.getInstance().setContext(this);
    }
}
