package com.lj.httpdemo.Request;

import android.content.Context;

import com.lj.httpdemo.Response.HttpRequestCallback;

import java.io.File;
import java.util.List;
import java.util.TreeMap;

public interface IHttpRequest {

    // Get请求（使用Path形式）
    void mHttpGetPath(Context context, String url, int type, HttpRequestCallback callback);

    // GET请求(无参)
    void mHttpGet(Context context, String api, int type, HttpRequestCallback callback);

    // Get请求(带参)
    void mHttpGet(Context context, String api, TreeMap map, int type, HttpRequestCallback callback);

    // Post请求(无参)
    void mHttpPost(Context context, String api, int type, HttpRequestCallback callback);

    // Post请求(带参)
    // 以RequestBody方式提交
    void mHttpPost(Context context, String api, TreeMap map, int type, HttpRequestCallback callback);

    // Post请求(包含数组)
    void mHttpPost(Context context, String api, TreeMap treeMap, String[] data, int type, HttpRequestCallback callback);

    // 单文件上传
    void mHttpFile(Context context, String api, File file, String fileKey, TreeMap map, int type, HttpRequestCallback callback);

    // 多文件上传
    void mHttpMultiFile(Context context, String api, List<File> list, List<String> fileList, TreeMap map, int type, HttpRequestCallback callback);
}
