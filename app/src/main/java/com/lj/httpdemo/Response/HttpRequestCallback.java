package com.lj.httpdemo.Response;

/**
 * @author luojing
 * @description HttpRequestCallback 请求接口回调
 * @date 2022/07/14
 */

public interface HttpRequestCallback {

    void onRequestNetFail(int type);

    void onRequestSuccess(String result, int type);

    void onRequestFail(String value, String failCode, int type);
}
