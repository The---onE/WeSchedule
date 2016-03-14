package com.xmx.weschedule.Schedule;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xmx.weschedule.Constants;
import com.xmx.weschedule.Database.ScheduleManager;
import com.xmx.weschedule.R;

import java.util.List;

/**
 * Created by The_onE on 2016/3/13.
 */
public class ScheduleAdapter extends BaseAdapter {
    Context mContext;

    public ScheduleAdapter(Context context) {
        mContext = context;
    }

    public void changeList() {
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return ScheduleManager.getInstance().getSchedules().size();
    }

    @Override
    public Object getItem(int position) {
        if (position < ScheduleManager.getInstance().getSchedules().size()) {
            return ScheduleManager.getInstance().getSchedules().get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        TextView title;
        TextView time;
        TextView text;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_schedule, null);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.card_title);
            holder.time = (TextView) convertView.findViewById(R.id.card_time);
            holder.text = (TextView) convertView.findViewById(R.id.card_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        List<Schedule> schedules = ScheduleManager.getInstance().getSchedules();
        if (position < schedules.size()) {
            Schedule schedule = schedules.get(position);
            holder.title.setText(schedule.getTitle());
            holder.title.setTextColor(Color.BLACK);

            holder.time.setText(schedule.getTimeString());

            holder.text.setText(schedule.getText());
            holder.text.setTextColor(Color.BLACK);
        } else {
            holder.title.setText("加载失败");
            holder.time.setText("");
            holder.text.setText("");
        }

        return convertView;
    }
}
