package com.example.obdandroid.http;


import android.util.Log;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 作者：Jealous
 * 日期：2017/10/17 0017 14:55
 */
public class HttpClientUtils {
    public final static String Method_POST = "POST";
    public final static String Method_GET = "GET";

    private HttpClientUtils() {
    }

    // 将自身的实例对象设置为一个属性,并加上Static和final修饰符
    private static final HttpClientUtils instance = new HttpClientUtils();

    // 静态方法返回该类的实例
    public static HttpClientUtils getInstance() {
        return instance;
    }

    /**
     * multipart/form-data类型的表单提交
     *
     * @param form 表单数据
     */
    public static void submitForm(MultipartForm form, UPLoadIMGCallBack callBack) {
        // 返回字符串
        String responseStr = "";
        HttpClient httpClient = new DefaultHttpClient();
        try {
            // 实例化提交请求
            HttpPost httpPost = new HttpPost(form.getAction());

            // 创建MultipartEntityBuilder
            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
            // 追加普通表单字段
            Map<String, String> normalFieldMap = form.getNormalField();
            for (Entry<String, String> entity : normalFieldMap.entrySet()) {
                entityBuilder.addPart(entity.getKey(), new StringBody(entity.getValue(), ContentType.create("multipart/form-data", Consts.UTF_8)));
            }
            // 追加文件字段
            Map<String, File> fileFieldMap = form.getFileField();
            for (Entry<String, File> entity : fileFieldMap.entrySet()) {
                Log.e("name", "名称：" + entity.getValue().getName());
                Log.e("name", "名称：" + entity.getValue().getPath());
                entityBuilder.addPart(entity.getKey(), new FileBody(entity.getValue()));
            }
            // 设置请求实体
            httpPost.setEntity(entityBuilder.build());
            // 发送请求
            HttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            // 取得响应数据
            HttpEntity resEntity = response.getEntity();
            Log.e("HttpClientUtil", "结果：" + Integer.toString(statusCode));
            if (HttpStatus.SC_OK == statusCode) {
                if (resEntity != null) {
                    responseStr = EntityUtils.toString(resEntity, HTTP.UTF_8);
                    callBack.OnSuccess(responseStr);
                }
            } else {
                callBack.OnFail("网络错误，错误码为：" + statusCode);
            }
        } catch (Exception e) {
            System.out.println("提交表单失败，原因：" + e.getMessage());
        } finally {
            try {
                httpClient.getConnectionManager().shutdown();
            } catch (Exception ex) {
            }
        }
    }

    /**
     * 表单字段Bean
     */
    public class MultipartForm implements Serializable {
        /**
         * 序列号
         */
        private static final long serialVersionUID = -2138044819190537198L;

        /**
         * 提交URL
         **/
        private String action = "";

        /**
         * 提交方式：POST/GET
         **/
        private String method = "POST";

        /**
         * 普通表单字段
         **/
        private Map<String, String> normalField = new LinkedHashMap<String, String>();

        /**
         * 文件字段
         **/
        private Map<String, File> fileField = new LinkedHashMap<String, File>();

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public Map<String, String> getNormalField() {
            return normalField;
        }

        public void setNormalField(Map<String, String> normalField) {
            this.normalField = normalField;
        }

        public Map<String, File> getFileField() {
            return fileField;
        }

        public void setFileField(Map<String, File> fileField) {
            this.fileField = fileField;
        }

        public void addFileField(String key, File value) {
            fileField.put(key, value);
        }

        public void addNormalField(String key, String value) {
            normalField.put(key, value);
        }
    }

    public interface UPLoadIMGCallBack {
        void OnSuccess(String response);

        void OnFail(String error);
    }
}
