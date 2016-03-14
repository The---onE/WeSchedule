package com.xmx.weschedule.Fragments;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xmx.weschedule.Database.SQLManager;
import com.xmx.weschedule.Database.ScheduleManager;
import com.xmx.weschedule.DisplayUtil;
import com.xmx.weschedule.R;
import com.xmx.weschedule.Schedule.Schedule;
import com.xmx.weschedule.Schedule.ScheduleAdapter;
import com.yydcdut.sdlv.Menu;
import com.yydcdut.sdlv.MenuItem;
import com.yydcdut.sdlv.SlideAndDragListView;

/**
 */
public class ScheduleFragment extends Fragment {
    SlideAndDragListView scheduleList;
    ScheduleAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        ScheduleManager.getInstance().updateSchedules();

        scheduleList = (SlideAndDragListView) view.findViewById(R.id.list_schedule);

        Menu menu = new Menu(new ColorDrawable(Color.WHITE), true, 0);
        menu.addItem(new MenuItem.Builder().setWidth(DisplayUtil.dip2px(getContext(), 50))
                .setBackground(new ColorDrawable(Color.RED))
                .setDirection(MenuItem.DIRECTION_RIGHT)
                .setIcon(getResources().getDrawable(android.R.drawable.ic_menu_delete))
                .build());
        menu.addItem(new MenuItem.Builder().setWidth(DisplayUtil.dip2px(getContext(), 75))
                .setBackground(new ColorDrawable(Color.GRAY))
                .setText("开始啦")
                .setDirection(MenuItem.DIRECTION_RIGHT)
                .setTextColor(Color.WHITE)
                .setTextSize(20)
                .build());
        scheduleList.setMenu(menu);

        adapter = new ScheduleAdapter(getContext());
        scheduleList.setAdapter(adapter);

        scheduleList.setOnMenuItemClickListener(new SlideAndDragListView.OnMenuItemClickListener() {
            @Override
            public int onMenuItemClick(View v, int itemPosition, int buttonPosition, int direction) {
                Schedule schedule = (Schedule) adapter.getItem(itemPosition);
                int id = schedule.getId();
                switch (direction) {
                    case MenuItem.DIRECTION_LEFT:
                        return Menu.ITEM_SCROLL_BACK;

                    case MenuItem.DIRECTION_RIGHT:
                        switch (buttonPosition) {
                            case 1: //Complete
                                SQLManager.getInstance().completeSchedule(id);
                                ScheduleManager.getInstance().updateSchedules();
                                adapter.changeList();
                                return Menu.ITEM_SCROLL_BACK;
                            case 0: //Cancel
                                SQLManager.getInstance().cancelSchedule(id);
                                ScheduleManager.getInstance().updateSchedules();
                                adapter.changeList();
                                return Menu.ITEM_DELETE_FROM_BOTTOM_TO_TOP;
                        }
                        return Menu.ITEM_SCROLL_BACK;

                    default:
                        return Menu.ITEM_NOTHING;
                }
            }
        });

        return view;
    }

}
