package com.example.obdandroid.http;


import com.example.obdandroid.config.APIConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 作者：Jealous
 * 日期：2021/7/16 0016
 * 描述：创建请求对象
 */
public class RetrofitHttp {
    private static final int CONNECT_TIMEOUT = 60;
    private static final int READ_TIMEOUT = 60;
    private static final int WRITE_TIMEOUT = 60;
    private static OkHttpClient okHttpClient;
    private static retrofit2.Retrofit retrofit;
    private static HttpInterceptor httpInterceptor;

    /**
     * @param interceptor 拦截器
     * @return 添加应用拦截器interceptor
     */
    public static OkHttpClient provideOkHttpClient(HttpInterceptor interceptor) {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient.Builder().
                    connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS).
                    readTimeout(READ_TIMEOUT, TimeUnit.SECONDS).
                    writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS).
                    addInterceptor(interceptor).   //添加拦截器
                    retryOnConnectionFailure(true).
                    build();
        }

        return okHttpClient;
    }

    public static retrofit2.Retrofit provideRetrofit() {
        //拦截器
        if (httpInterceptor == null) {
            httpInterceptor = new HttpInterceptor();
        }
        //请求
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(APIConfig.SERVER_URL)
                    .client(provideOkHttpClient(httpInterceptor))
                    .addConverterFactory(GsonConverterFactory.create())   //添加数据解析
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//支持RXJava返回
                    .build();
        }
        return retrofit;
    }
}