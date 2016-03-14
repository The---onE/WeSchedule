package com.xmx.weschedule.Database;

import android.database.Cursor;

import com.xmx.weschedule.Schedule.Schedule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by The_onE on 2016/2/24.
 */
public class ScheduleManager {
    private static ScheduleManager instance;

    long sqlVersion = 0;
    long version = System.currentTimeMillis();
    List<Schedule> schedules = new ArrayList<>();

    public synchronized static ScheduleManager getInstance() {
        if (null == instance) {
            instance = new ScheduleManager();
        }
        return instance;
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public long updateSchedules() {
        boolean changeFlag = false;

        SQLManager sqlManager = SQLManager.getInstance();
        if (sqlManager.getVersion() != sqlVersion) {
            sqlVersion = sqlManager.getVersion();

            Cursor c = sqlManager.selectFutureSchedule();
            schedules.clear();
            if (c.moveToFirst()) {
                do {
                    int id = SQLManager.getId(c);
                    String title = SQLManager.getTitle(c);
                    String text = SQLManager.getText(c);
                    int type = SQLManager.getType(c);
                    long time = SQLManager.getTime(c);

                    Schedule s = new Schedule(id, title, text, time, type);
                    schedules.add(s);
                } while (c.moveToNext());
            }
            changeFlag = true;
        }

        if (changeFlag) {
            version++;
        }
        return version;
    }
}
