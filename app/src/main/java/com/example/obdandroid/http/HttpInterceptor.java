package com.example.obdandroid.http;

import android.support.multidex.BuildConfig;


import com.example.obdandroid.utils.LogUtil;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * 作者：Jealous
 * 日期：2021/7/16 0016
 * 描述：okhttp  拦截器，主要功能为添加请求头,统一记录日志等等统一行为
 */
public class HttpInterceptor implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");

    private static String requestValue;
    private static String reponseValue;


    @Override
    public Response intercept(Chain chain) throws IOException {
        //请求token
//        String token = SPUtils.getInstance(SPConstant.TOKEN).getString(SPConstant.TOKEN);
        Request request = chain.request();
        // LogUtils.d( "device_id="+BaseApplication.device);
        //添加请求头
        Request updateRequest = request.newBuilder()
                .addHeader("deviceType", "Android")
                .addHeader("version", BuildConfig.VERSION_NAME)
                .build();

        Response response = chain.proceed(updateRequest);


        if (BuildConfig.DEBUG) {
            //添加打印服务器返回的数据
            ResponseBody responseBody = response.body();
            long contentLength = responseBody.contentLength();
            BufferedSource source = responseBody.source();
            source.request(Integer.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();

            LogUtil.d("TT", "请求参数：" + response.toString() + "\n" + "主体：" + getRequestInfo(request) + "\n" + "返回数据：" + getResponseInfo(response) + "\n" + updateRequest.headers());
            requestValue = response.toString();
            reponseValue = getResponseInfo(response);
        }
        return response;
    }

    /**
     * @return
     */
    public static String getRequestParameter() {

        return requestValue;
    }

    public static String getResponseParameter() {

        return reponseValue;
    }

    /**
     * 打印请求消息
     *
     * @param request 请求的对象
     */
    private String getRequestInfo(Request request) {
        String str = "";
        if (request == null) {
            return str;
        }
        RequestBody requestBody = request.body();
        if (requestBody == null) {
            return str;
        }
        try {
            Buffer bufferedSink = new Buffer();
            requestBody.writeTo(bufferedSink);
            Charset charset = StandardCharsets.UTF_8;
            str = bufferedSink.readString(charset);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 打印返回消息
     *
     * @param response 返回的对象
     */
    private String getResponseInfo(Response response) {
        String str = "";
        if (response == null || !response.isSuccessful()) {
            return str;
        }
        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();
        BufferedSource source = responseBody.source();
        try {
            source.request(Long.MAX_VALUE); // Buffer the entire body.
        } catch (IOException e) {
            e.printStackTrace();
        }
        Buffer buffer = source.buffer();
        Charset charset = StandardCharsets.UTF_8;
        if (contentLength != 0) {
            str = buffer.clone().readString(charset);
        }
        return str;
    }
}