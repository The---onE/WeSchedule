package com.xmx.weschedule.Schedule;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by The_onE on 2016/3/13.
 */
public class Schedule {
    int mId;
    String mTitle;
    String mText;
    long mTime;
    String mTimeString;
    int mType;

    public Schedule(int id, String title, String text, long time, int type) {
        mId = id;
        mTitle = title;
        mText = text;

        mTime = time;
        Date date = new Date(time);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mTimeString = df.format(date);

        mType = type;
    }

    public int getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getText() {
        return mText;
    }

    public long getTime() {
        return mTime;
    }

    public String getTimeString() {
        return mTimeString;
    }

    public int getType() {
        return mType;
    }
}
