package com.example.obdandroid.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.obdandroid.config.Constant;
import com.example.obdandroid.ui.activity.LoginActivity;
import com.example.obdandroid.ui.entity.UserLoginEntity;
import com.example.obdandroid.ui.view.CustomeDialog;
import com.example.obdandroid.utils.ActivityManager;
import com.example.obdandroid.utils.AppDateUtils;
import com.example.obdandroid.utils.JumpUtil;
import com.example.obdandroid.utils.SPUtil;
import com.example.obdandroid.utils.StringUtil;
import com.kongzue.dialog.v2.TipDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Response;

import static com.example.obdandroid.config.APIConfig.LOGIN_URL;
import static com.example.obdandroid.config.APIConfig.SERVER_URL;
import static com.example.obdandroid.config.Constant.EXPIRE_TIME;
import static com.example.obdandroid.config.Constant.IS_CHECK;
import static com.example.obdandroid.config.Constant.PASSWORD;
import static com.example.obdandroid.config.Constant.TOKEN;
import static com.example.obdandroid.config.Constant.USER_ID;
import static com.example.obdandroid.config.Constant.USER_NAME;


/**
 * 作者：Jealous
 * 日期：2018/11/2 0002 15:44
 * Activity 封装基类
 */

public abstract class BaseActivity extends AppCompatActivity {
    private static final String TAG = BaseActivity.class.getSimpleName();
    public Context mContext;
    private final SparseArray<View> mViews = new SparseArray<>();
    private String token;
    private String userId = "";
    private String expireTime = "";
    private String phone = "";
    private SPUtil spUtil;
    public AppCompatActivity mActivity;
    private RefreshCallBack callBack;

    //布局文件ID
    protected abstract int getContentViewId();

    //布局中装载Fragment的ID
    protected abstract int getFragmentContentId();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //将activity添加到列表中
        ActivityManager.getInstance().addActivity(this);
        mContext = this;
        mActivity = this;
        initView();
    }

    /**
     * @param viewId 控件ID
     * @return 替代findViewById方法
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 初始化
     */
    public void initView() {
        setContentView(getContentViewId());
        spUtil = new SPUtil(mContext);
        token = spUtil.getString(TOKEN, "");
        userId = spUtil.getString(USER_ID, "");
        expireTime = spUtil.getString(EXPIRE_TIME, "");
        phone = spUtil.getString(USER_NAME, "");
        if (!StringUtil.isNull(token)) {
            setToken(token);
        }
        if (!StringUtil.isNull(userId)) {
            setUserId(userId);
        }
        if (!StringUtil.isNull(expireTime)) {
            setExpireTime(expireTime);
        }
        if (!StringUtil.isNull(phone)) {
            setPhone(phone);
        }
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @param msg 日志内容
     *            输出日志
     */
    public void LogE(String msg) {
        Log.e(TAG, msg);
    }

    /**
     * @param text 吐司内容
     *             弹吐司方法
     */
    public void showToast(String text) {
        Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
    }

    public void showTipDialog(String msg) {
        TipDialog.show(mContext, msg, TipDialog.TYPE_ERROR, TipDialog.TYPE_WARNING);
    }

    public AppCompatActivity getActivity() {
        return mActivity;
    }

    /**
     * @param resId 资源id
     *              弹吐司方法
     */
    public void showToast(int resId) {
        Toast.makeText(mContext, resId, Toast.LENGTH_SHORT).show();
    }

    public void dialogError(final Context context, final String msg) {
        if (msg.equals("token失效，请重新登录")) {
            if (spUtil.getBoolean(IS_CHECK, false)) {
                String userNameValue = spUtil.getString(USER_NAME, "");
                String passwordValue = spUtil.getString(PASSWORD, "");
                userLogin(userNameValue, passwordValue);
            } else {
                JumpUtil.startAct(context, LoginActivity.class);
                ActivityManager.getInstance().finishActivitys();
            }
        } else if (msg.equals("未知异常，请联系管理员")) {
            new CustomeDialog(context, msg, confirm -> {
                if (confirm) {
                    JumpUtil.startAct(context, LoginActivity.class);
                    ActivityManager.getInstance().finishActivitys();
                }
            }).setPositiveButton("确定").setTitle("提示").show();
        } else {
            new CustomeDialog(context, msg, confirm -> {
                if (confirm) {
                    finish();
                }
            }).setPositiveButton("确定").setTitle("提示").show();
        }
    }

    /**
     * @param mobile   手机号
     * @param password 密码
     */
    private void userLogin(String mobile, String password) {
        OkHttpUtils.post().url(SERVER_URL + LOGIN_URL).
                addParam("mobile", mobile).
                addParam("password", password).
                addParam("loginType", "1").
                addParam("verificationCode", "").
                addParam("taskID", "").
                build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                UserLoginEntity entity = JSON.parseObject(response, UserLoginEntity.class);
                if (entity.isSuccess()) {
                    //记住用户名、密码、
                    spUtil.put(PASSWORD, password);
                    spUtil.put(USER_NAME, mobile);
                    spUtil.put(Constant.IS_LOGIN, true);
                    spUtil.put(TOKEN, entity.getData().getToken());
                    spUtil.put(USER_ID, String.valueOf(entity.getData().getUserId()));
                    spUtil.put(EXPIRE_TIME, AppDateUtils.dealDateFormat(entity.getData().getExpireTime()));
                    setUserId(String.valueOf(entity.getData().getUserId()));
                    setPhone(mobile);
                    setToken(entity.getData().getToken());
                    callBack.refresh(true);
                }
            }
        });
    }

    /**
     * @param list 数据集
     * @return String 和List<String> 的互相转换
     */
    public static String listToString(Map<String, String> list) {
        if (list == null) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        boolean first = true;
        //第一个前面不拼接","
        Set<Map.Entry<String, String>> set = list.entrySet();
        for (Map.Entry<String, String> me : set) {
            if (first) {
                first = false;
            } else {
                result.append(",");
            }
            // 根据键值对对象获取键和值
            String value = me.getValue();
            result.append(value);
        }
        return result.toString();
    }

    public interface RefreshCallBack {
        void refresh(boolean re);
    }
}
