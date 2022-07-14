package com.lj.httpdemo.Response;

import android.content.Context;

import com.blankj.utilcode.util.JsonUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * @author luojing
 * @description RxJavaObserver 处理接口返回的请求结果
 * @date 2022/07/14
 */

public class RxJavaObserver implements Observer<String> {

    private HttpRequestCallback callback;
    private int type;
    private Context context;

    public RxJavaObserver(HttpRequestCallback callback, int type, Context context) {
        this.callback = callback;
        this.type = type;
        this.context = context;
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {

    }

    // 接口请求成功
    @Override
    public void onNext(@NonNull String s) {
        try {
            if (JsonUtils.isJSONObject(s)) {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.has("resultStatus")) {
                    String response = jsonObject.getString("resultStatus");
                    if (response.toUpperCase().equals("SUCCESS")) {
                        if (jsonObject.has("returnData")) {
                            callback.onRequestSuccess(jsonObject.getString("returnData"), type);
                        } else {
                            if (jsonObject.has("errCode") && jsonObject.has("errMsg")) {
                                String code = jsonObject.getString("errCode");
                                String errMsg = jsonObject.getString("errMsg");
                                callback.onRequestSuccess(errMsg, type);
                            }
                        }
                    } else {
                        String errMsg = jsonObject.getString("errMsg");
                        String code = jsonObject.getString("errCode");
                        callback.onRequestFail(errMsg, code, type);
                    }
                }
            } else {
                callback.onRequestSuccess(s, type);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 接口请求失败
    @Override
    public void onError(@NonNull Throwable e) {
        // 1.检查网络设置
        if (!NetworkUtils.isAvailable()) {
            ToastUtils.showShort("Net Connect Error!");
            onComplete();
            callback.onRequestNetFail(type);
            return;
        }
        // 2.非网络错误，接口请求错误
        callback.onRequestFail(e.getMessage(), "0000", type);
    }

    // 接口请求完成
    @Override
    public void onComplete() {

    }
}
