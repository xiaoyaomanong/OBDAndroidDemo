package com.example.obdandroid.http;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 作者：Jealous
 * 日期：2021/7/16 0016
 * 描述：
 */
public class HttpService {
    /**
     * ApiService Api接口每一个接口
     */
    ApiService apiService;
    //类初始化时，不初始化这个对象(延时加载，真正用的时候再创建)
    private static HttpService instance;

    //构造器私有化
    private HttpService() {
        if (apiService == null) {
            retrofit2.Retrofit retrofit = RetrofitHttp.provideRetrofit();
            apiService = retrofit.create(ApiService.class);
        }
    }

    //方法同步，调用效率低
    public static synchronized HttpService getInstance() {
        if (instance == null) {
            instance = new HttpService();
        }
        return instance;
    }


    /**
     * 添加收货地址
     */
    public Call<ResponseBody> addAppUserAddress(String token, String appUserId, String telephone, String contacts, String address, boolean isDefault) {
        return apiService.addAppUserAddress(token, appUserId, telephone, contacts, address, isDefault);
    }

    /**
     * 修改收货地址
     */
    public Call<ResponseBody> updateAppUserAddress(String id, String token, String appUserId, String telephone, String contacts, String address, boolean isDefault) {
        return apiService.updateAppUserAddress(id, token, appUserId, telephone, contacts, address, isDefault);
    }

    /**
     * 刷新客户端连接时间接口
     */
    public Call<ResponseBody> setDefaultAddress(String token, String appUserId, String id, boolean isDefault) {
        return apiService.setDefaultAddress(token, appUserId, id, isDefault);
    }
}