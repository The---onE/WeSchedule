package com.xmx.weschedule.TodayOnHistory;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.xmx.weschedule.Constants;
import com.xmx.weschedule.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by The_onE on 2016/3/2.
 */
public class TOHManager {
    private static TOHManager instance;

    Context mContext;

    public synchronized static TOHManager getInstance() {
        if (null == instance) {
            instance = new TOHManager();
        }
        return instance;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public void setTodayOnHistoryList(int month, int day, final ListView list) {
        String urlString = "http://v.juhe.cn/todayOnhistory/queryEvent.php?key="
                + Constants.TOH_APP_KEY + "&date=" + month + "/" + day;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(urlString, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                int id = -1;
                String day = "";
                String date = "";
                String title = mContext.getResources().getString(R.string.loading);
                List<TodayOnHistory> todayOnHistories = new ArrayList<>();
                TodayOnHistory todayOnHistory = new TodayOnHistory(id, day, date, title);
                todayOnHistories.add(todayOnHistory);
                TOHAdapter tohAdapter = new TOHAdapter(mContext, todayOnHistories);
                list.setAdapter(tohAdapter);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                try {
                    JSONObject jsonObject = new JSONObject(new String(response));
                    JSONArray result = jsonObject.getJSONArray("result");
                    List<TodayOnHistory> todayOnHistories = new ArrayList<>();
                    for (int i = result.length() - 1; i >= 0; --i) {
                        JSONObject item = result.getJSONObject(i);
                        int id = item.getInt("e_id");
                        String day = item.getString("day");
                        String date = item.getString("date");
                        String title = item.getString("title");
                        TodayOnHistory todayOnHistory = new TodayOnHistory(id, day, date, title);
                        todayOnHistories.add(todayOnHistory);
                    }
                    TOHAdapter tohAdapter = new TOHAdapter(mContext, todayOnHistories);
                    list.setAdapter(tohAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                    showToast("JSON Exception");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                showToast("Error " + statusCode);
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
                showToast("Retrying");
            }
        });
    }

    public void setTodayOnHistoryDetail(long id, final TextView titleView,
                                        final TextView contentView, final LinearLayout layout) {
        String urlString = "http://v.juhe.cn/todayOnhistory/queryDetail.php?key="
                + Constants.TOH_APP_KEY + "&e_id=" + id;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(urlString, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                titleView.setText(R.string.loading);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                try {
                    JSONObject jsonObject = new JSONObject(new String(response));
                    int errorCode = jsonObject.getInt("error_code");
                    if (errorCode == 0) {
                        JSONArray result = jsonObject.getJSONArray("result");
                        JSONObject item = result.getJSONObject(0);
                        String title = item.getString("title");
                        String content = item.getString("content");
                        titleView.setText(title);
                        titleView.setTextColor(Color.BLACK);
                        contentView.setText(content);
                        contentView.setTextColor(Color.BLACK);

                        JSONArray images = item.getJSONArray("picUrl");
                        for (int i = 0; i < images.length(); ++i) {
                            JSONObject image = images.getJSONObject(i);

                            SimpleDraweeView imageView = new SimpleDraweeView(mContext);
                            LinearLayout.LayoutParams params =
                                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT);
                            imageView.setLayoutParams(params);
                            imageView.setAspectRatio(1);

                            GenericDraweeHierarchyBuilder builder =
                                    new GenericDraweeHierarchyBuilder(mContext.getResources());
                            GenericDraweeHierarchy hierarchy = builder
                                    .setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER)
                                    .build();
                            hierarchy.setPlaceholderImage(R.mipmap.ic_launcher);
                            imageView.setHierarchy(hierarchy);

                            String url = image.getString("url");
                            Uri uri = Uri.parse(url);
                            imageView.setImageURI(uri);

                            layout.addView(imageView);
                        }
                    } else if (errorCode == 206302) {
                        titleView.setText(R.string.no_data);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showToast("JSON Exception");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                titleView.setText(R.string.failure);
                showToast("Error " + statusCode);
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
                showToast("Retrying");
            }
        });
    }

    protected void showToast(String str) {
        Toast.makeText(mContext, str, Toast.LENGTH_SHORT).show();
    }
}
