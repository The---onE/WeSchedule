package com.xmx.weschedule.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.util.Date;

/**
 * Created by The_onE on 2015/10/23.
 */
public class SQLManager {
    private static SQLManager instance;

    SQLiteDatabase database = null;
    long version = System.currentTimeMillis();

    public synchronized static SQLManager getInstance() {
        if (null == instance) {
            instance = new SQLManager();
        }
        return instance;
    }

    private SQLManager() {
        openDatabase();
    }

    public long getVersion() {
        return version;
    }

    public static int getId(Cursor c) {
        return c.getInt(0);
    }

    public static int getStatus(Cursor c) {
        return c.getInt(1);
    }

    public static String getTitle(Cursor c) {
        return c.getString(2);
    }

    public static String getText(Cursor c) {
        return c.getString(3);
    }

    public static long getTime(Cursor c) {
        return c.getLong(4);
    }

    public static int getType(Cursor c) {
        return c.getInt(5);
    }

    private boolean openDatabase() {
        String d = android.os.Environment.getExternalStorageDirectory() + "/WeSchedule/Database";
        File dir = new File(d);
        boolean flag = dir.exists() || dir.mkdirs();

        if (flag) {
            String sqlFile = android.os.Environment.getExternalStorageDirectory() + "/WeSchedule/Database/schedule.db";
            File file = new File(sqlFile);
            database = SQLiteDatabase.openOrCreateDatabase(file, null);
            if (database == null) {
                Log.e("DatabaseError", "创建文件失败");
                return false;
            }
            // ID TITLE TEXT PHOTO TIME
            String createScheduleSQL = "create table if not exists SCHEDULE(" +
                    "ID integer not null primary key autoincrement, " +
                    "STATUS integer default(0), " +
                    "TITLE text not null, " +
                    "TEXT text, " +
                    "TIME integer not null default(0), " +
                    "TYPE integer default(0)" +
                    ")";
            database.execSQL(createScheduleSQL);
        } else {
            Log.e("DatabaseError", "创建目录失败");
            return false;
        }
        return database != null;
    }

    private boolean checkDatabase() {
        return database != null || openDatabase();
    }

    public boolean clearDatabase() {
        if (!checkDatabase()) {
            return false;
        }
        String clear = "delete from SCHEDULE";
        database.execSQL(clear);
        String zero = "delete from sqlite_sequence where NAME = 'SCHEDULE'";
        database.execSQL(zero);

        version++;
        return true;
    }

    public long insertSchedule(String title, String text, Date date, int type) {
        if (!checkDatabase()) {
            return -1;
        }
        ContentValues content = new ContentValues();
        content.put("STATUS", 0);
        content.put("TITLE", title);
        content.put("TEXT", text);
        content.put("TIME", date.getTime());
        content.put("TYPE", type);

        long id = database.insert("SCHEDULE", null, content);

        version++;

        return id;
    }

    public Cursor getLatestSchedule() {
        if (!checkDatabase()) {
            return null;
        }
        return database.rawQuery("select * from SCHEDULE where STATUS = 0 order by TIME asc limit " + 1, null);
    }

    public boolean cancelSchedule(int id) {
        Cursor c = selectById(id);
        if (c == null) {
            return false;
        }
        if (c.moveToFirst()) {
            String update = "update SCHEDULE set STATUS = 1 where ID = " + id;
            database.execSQL(update);
            version++;
            return true;
        } else {
            return false;
        }
    }

    public boolean completeSchedule(int id) {
        Cursor c = selectById(id);
        if (c == null) {
            return false;
        }
        if (c.moveToFirst()) {
            String update = "update SCHEDULE set STATUS = 1 where ID = " + id;
            database.execSQL(update);
            version++;
            return true;
        } else {
            return false;
        }
    }

    public Cursor selectFutureSchedule() {
        if (!checkDatabase()) {
            return null;
        }
        return database.rawQuery("select * from SCHEDULE where STATUS = 0 order by TIME", null);
    }

    public Cursor selectById(int id) {
        if (!checkDatabase()) {
            return null;
        }
        return database.rawQuery("select * from SCHEDULE where ID=" + id, null);
    }
}
