package com.example.obdandroid.http;

import java.io.IOException;

import io.reactivex.annotations.NonNull;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 作者：Jealous
 * 日期：2021/9/8 0008
 * 描述：
 */
public class ResponseCallBack implements Callback<ResponseBody> {
    private final CallBack callBack;

    public ResponseCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
        try {
            callBack.onSuccess(response.body().string());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void onFailure(@NonNull Call<okhttp3.ResponseBody> call, @NonNull Throwable t) {
        callBack.onFail(t);
    }

    public interface CallBack {
        void onSuccess(String response);

        void onFail(Throwable t);
    }
}