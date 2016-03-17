package com.xmx.weschedule.Fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;

/**
 */
public class ScheduleFragment extends Fragment implements BGARefreshLayout.BGARefreshLayoutDelegate {
    SlideAndDragListView scheduleList;
    ScheduleAdapter adapter;
    BGARefreshLayout mRefreshLayout;

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

        initRefreshLayout(view);

        return view;
    }

    private void initRefreshLayout(View view) {
        mRefreshLayout = (BGARefreshLayout) view.findViewById(R.id.item_refresh);
        // 为BGARefreshLayout设置代理
        mRefreshLayout.setDelegate(this);
        // 设置下拉刷新和上拉加载更多的风格     参数1：应用程序上下文，参数2：是否具有上拉加载更多功能
        BGARefreshViewHolder refreshViewHolder = new BGANormalRefreshViewHolder(getContext(), true);
        // 设置下拉刷新和上拉加载更多的风格
        mRefreshLayout.setRefreshViewHolder(refreshViewHolder);

        // 设置正在加载更多时的文本
        refreshViewHolder.setLoadingMoreText("正在加载");

        // 为了增加下拉刷新头部和加载更多的通用性，提供了以下可选配置选项  -------------START
        // 设置整个加载更多控件的背景颜色资源id
        //refreshViewHolder.setLoadMoreBackgroundColorRes(loadMoreBackgroundColorRes);
        // 设置整个加载更多控件的背景drawable资源id
        //refreshViewHolder.setLoadMoreBackgroundDrawableRes(loadMoreBackgroundDrawableRes);
        // 设置下拉刷新控件的背景颜色资源id
        //refreshViewHolder.setRefreshViewBackgroundColorRes(refreshViewBackgroundColorRes);
        // 设置下拉刷新控件的背景drawable资源id
        //refreshViewHolder.setRefreshViewBackgroundDrawableRes(refreshViewBackgroundDrawableRes);
        // 设置自定义头部视图（也可以不用设置）     参数1：自定义头部视图（例如广告位）， 参数2：上拉加载更多是否可用
        //mRefreshLayout.setCustomHeaderView(mBanner, false);
        // 可选配置  -------------END
    }

    private class RefreshHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (ScheduleManager.getInstance().updateSchedules() >= 0) {
                adapter.changeList();
            }
            mRefreshLayout.endRefreshing();
        }
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        RefreshHandler timerHandler = new RefreshHandler();
        timerHandler.sendEmptyMessage(0);
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }
}
