package com.xmx.weschedule.DateInformation;

import android.content.Context;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.xmx.weschedule.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by The_onE on 2016/3/9.
 */
public class DateManager {
    private static DateManager instance;

    Context mContext;

    public synchronized static DateManager getInstance() {
        if (null == instance) {
            instance = new DateManager();
        }
        return instance;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public void getDateInformation(int year, int month, int day, final GetJsonCallback callback) {
        String urlString = "http://v.juhe.cn/laohuangli/d?date=" + year + "-"
                + month + "-" + day + "&key="+Constants.DATE_APP_KEY;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(urlString, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                callback.start();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                try {
                    JSONObject jsonObject = new JSONObject(new String(response));
                    callback.success(jsonObject);
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

    protected void showToast(String str) {
        Toast.makeText(mContext, str, Toast.LENGTH_SHORT).show();
    }
}
