package com.lj.httpdemo.OkHttp;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * @author luojing
 * @description HttpRequestInterceptor 拦截每次发出的请求并打印出来
 * @date 2022/07/14
 */

public class HttpRequestInterceptor implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();
        long startTime = System.currentTimeMillis();
        okhttp3.Response response = chain.proceed(chain.request());
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        okhttp3.MediaType mediaType = response.body().contentType();
        String content = response.body().string();
        Log.d("request", "请求地址：| " + request.toString());
        if (request.body() != null) {
            printParams(request.body());
        }
        Log.d("request", "请求体返回：| Response:" + content);
        Log.d("request", "----------请求耗时:" + duration + "毫秒----------");
        return response.newBuilder().body(okhttp3.ResponseBody.create(mediaType, content)).build();
    }

    private void printParams(RequestBody body) {
        Buffer buffer = new Buffer();
        try {
            body.writeTo(buffer);
            Charset charset = UTF8;
            MediaType contentType = body.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }
            String params = buffer.readString(charset);
            Log.d("request", "请求参数： | " + params);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
