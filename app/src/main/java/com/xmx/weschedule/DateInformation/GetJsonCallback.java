package com.xmx.weschedule.DateInformation;

import org.json.JSONObject;

/**
 * Created by The_onE on 2016/3/9.
 */
public abstract class GetJsonCallback {
    public abstract void start(); //开始获取老黄历信息的回调
    public abstract void success(JSONObject json); //获取老黄历信息成功的回调
}
