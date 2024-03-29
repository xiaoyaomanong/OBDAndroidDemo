package com.example.obdandroid.http;

import com.example.obdandroid.config.APIConfig;
import com.example.obdandroid.ui.entity.ResultEntity;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 作者：Jealous
 * 日期：2021/7/16 0016
 * 描述：
 */

public interface ApiService {
    /**
     * @return 反查地理地点名称
     */
    @GET(APIConfig.reverse_geocoding_URL)
    Call<ResponseBody> reverseGeo(@Query("ak") String ak, @Query("output") String output, @Query("location") String location, @Query("extensions_poi") String extensions_poi);

    /**
     * @return 添加收货地址
     */
    @FormUrlEncoded
    @POST(APIConfig.addAppUserAddress_URL)
    Call<ResponseBody> addAppUserAddress(@Field("token") String token, @Field("appUserId") String appUserId, @Field("telephone") String telephone, @Field("contacts") String contacts, @Field("address") String address, @Field("isDefault") boolean isDefault);

    /**
     * @return 修改收货地址
     */
    @FormUrlEncoded
    @POST(APIConfig.updateAppUserAddress_URL)
    Call<ResponseBody> updateAppUserAddress(@Field("id") String id, @Field("token") String token, @Field("appUserId") String appUserId, @Field("telephone") String telephone, @Field("contacts") String contacts, @Field("address") String address, @Field("isDefault") boolean isDefault);

    /**
     * @return 设置默认地址
     */
    @FormUrlEncoded
    @POST(APIConfig.setDefault_URL)
    Call<ResponseBody> setDefaultAddress(@Field("token") String token, @Field("appUserId") String appUserId, @Field("id") String id, @Field("isDefault") boolean isDefault);
}
