package com.example.obdandroid.http;

import com.example.obdandroid.utils.HttpExceptionUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 作者：Jealous
 * 日期：2020/6/28 0028 11:24
 */
public class OkHttpClientUtils {
    private static UPLoadIMGCallBack callBacks;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClientUtils() {
    }

    // 将自身的实例对象设置为一个属性,并加上Static和final修饰符
    private static final OkHttpClientUtils instance = new OkHttpClientUtils();

    // 静态方法返回该类的实例
    public static OkHttpClientUtils getInstancei() {
        return instance;
    }

    public static void submitFormBody(String url, String param, UPLoadIMGCallBack callBack) {
        //  创建  RequestBody
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), param);
        //  获取OkhthpClient 实例
        OkHttpClient okHttpClient1 = OkHttpUtils.getInstance().getOkHttpClient();

        //  创建请求
        Request request = new Request.Builder().url(url)
                .post(requestBody).build();
        //  请求网络
        okHttpClient1.newCall(request).
                enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException error) {
                        callBack.OnFail(HttpExceptionUtils.validateError(error));
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        callBack.OnSuccess(response.body().string());
                    }
                });
    }

    public static void submitFormBodyN(String url, String param, String sign, String token, String taxnum, String method, UPLoadIMGCallBack callBack) {
        //  创建  RequestBody
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), param);
        //  获取OkhthpClient 实例
        OkHttpClient okHttpClient1 = OkHttpUtils.getInstance().getOkHttpClient();
        okHttpClient1.newBuilder().addInterceptor(chain -> {
            Request request = chain.request()
                    .newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("X-Nuonuo-Sign", sign)
                    .addHeader("accessToken", token)
                    .addHeader("userTax", taxnum)
                    .addHeader("method", method)
                    .addHeader("sdkVer", "1.0.4")
                    .addHeader("Accept-Encoding", "gzip, deflate, br")
                    .addHeader("Connection", "keep-alive")
                    .addHeader("Accept", "*/*")
                    .addHeader("Cookie", "add cookies here")
                    .build();
            return chain.proceed(request);
        });

        //  创建请求
        Request request = new Request.Builder().url(url)
                .post(requestBody).build();
        //  请求网络
        okHttpClient1.newCall(request).

                enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        callBack.OnFail(e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String string = response.body().string();
                        callBack.OnSuccess(string);
                    }
                });
    }


    public static void postJson(String url, String param, UPLoadIMGCallBack callBack) {
        //申明给服务端传递一个json串
        //创建一个OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //创建一个RequestBody(参数1：数据类型 参数2传递的json串)
        RequestBody requestBody = RequestBody.create(JSON, param);
        //创建一个请求对象
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        //发送请求获取响应
        try {
            Response response = okHttpClient.newCall(request).execute();
            //判断请求是否成功
            if (response.isSuccessful()) {
                //打印服务端返回结果
                callBack.OnSuccess(response.body().string());
            }
        } catch (IOException error) {
            error.printStackTrace();
            callBack.OnFail(HttpExceptionUtils.validateError(error));
        }

    }


    public interface UPLoadIMGCallBack {
        void OnSuccess(String response);

        void OnFail(String error);
    }
}
