package com.xmx.weschedule;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.xmx.weschedule.DateInformation.DateManager;
import com.xmx.weschedule.TodayOnHistory.TOHManager;

/**
 * Created by The_onE on 2016/1/3.
 */
public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Fresco.initialize(this);
        TOHManager.getInstance().setContext(this);
        DateManager.getInstance().setContext(this);
    }
}
