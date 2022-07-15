package com.lj.httpdemo.OkHttp;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * @author luojing
 * @description OkHttpClientUtil OkHttp实例的封装
 * @date 2022/07/14
 */

public class OkHttpClientUtil {

    // 是否开启拦截，默认情况下开启
    private boolean isIntercept = true;
    // 设置数据读取超时时间
    private long readTimeOut = 20000;
    // 设置网络连接超时时间
    private long connectTimeOut = 20000;
    // 设置写入服务器的超时时间
    private long writeTimeOut = 20000;

    private static volatile OkHttpClientUtil okHttpUtil;

    public static OkHttpClientUtil getOkHttpUtil() {
        if (okHttpUtil == null) {
            synchronized (OkHttpClientUtil.class) {
                if (okHttpUtil == null) {
                    okHttpUtil = new OkHttpClientUtil();
                }
            }
        }
        return okHttpUtil;
    }

    /**
     * 私有构造函数，保证全局唯一
     */
    private OkHttpClientUtil(){

    }

    // 设置数据读取超时时间
    public OkHttpClientUtil setTimeOutTime(long timeout) {
        readTimeOut = timeout;
        return this;
    }

    // 设置网络连接超时时间
    public OkHttpClientUtil setConnectTime(long timeout) {
        connectTimeOut = timeout;
        return this;
    }

    // 设置写入服务器的超时时间
    public OkHttpClientUtil setWriteTime(long timeout) {
        writeTimeOut = timeout;
        return this;
    }

    // 设置拦截器
    public OkHttpClientUtil setIntercept(boolean isIntercept) {
        this.isIntercept = isIntercept;
        return this;
    }

    // 设置Build方法
    public OkHttpClient build() {
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        okHttpClient.readTimeout(readTimeOut, TimeUnit.MILLISECONDS);
        okHttpClient.connectTimeout(connectTimeOut, TimeUnit.MILLISECONDS);
        okHttpClient.writeTimeout(writeTimeOut, TimeUnit.MILLISECONDS);
        // 默认开启请求的打印信息数据，在每次发布版本的时候可以手动关闭
        if (isIntercept) {
            okHttpClient.addInterceptor(new HttpRequestInterceptor());
        }
        return okHttpClient.build();
    }
}