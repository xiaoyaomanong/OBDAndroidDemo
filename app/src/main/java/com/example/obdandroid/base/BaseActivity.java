package com.example.obdandroid.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Toast;

import com.example.obdandroid.R;
import com.example.obdandroid.config.Constant;
import com.example.obdandroid.ui.activity.LoginActivity;
import com.example.obdandroid.ui.view.CustomeDialog;
import com.example.obdandroid.utils.ActivityManager;
import com.example.obdandroid.utils.AppDateUtils;
import com.example.obdandroid.utils.JumpUtil;
import com.example.obdandroid.utils.SPUtil;
import com.example.obdandroid.utils.StringUtil;
import com.hacknife.immersive.Immersive;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Set;

import static com.example.obdandroid.config.Constant.CONNECT_BT_KEY;
import static com.example.obdandroid.config.Constant.EXPIRE_TIME;
import static com.example.obdandroid.config.Constant.TOKEN;
import static com.example.obdandroid.config.Constant.USER_ID;


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
    private SPUtil spUtil;
    public AppCompatActivity mActivity;

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
        if (!StringUtil.isNull(token)) {
            setToken(token);
        }
        if (!StringUtil.isNull(userId)) {
            setUserId(userId);
        }
        if (!StringUtil.isNull(expireTime)) {
            setExpireTime(expireTime);
        }
        if (!AppDateUtils.isDateTwoBigger(AppDateUtils.getTodayDateTimeHms(), expireTime)) {
            new CustomeDialog(mContext, "您的账号登录时间过长，请重新登录！", confirm -> {
                if (confirm) {
                    spUtil.put(CONNECT_BT_KEY, "OFF");
                    spUtil.put(Constant.IS_LOGIN, false);
                    JumpUtil.startAct(mContext, LoginActivity.class);
                    try {
                        ActivityManager.getInstance().finishActivitys();
                    } catch (Exception e) {
                        LogE("该服务未注册");
                    }
                }
            }).setPositiveButton("知道了").setTitle("提示").show();
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
            new CustomeDialog(context, "你的账号已在其他设备登录或登录时间过长,请检查重新登录", confirm -> {
                if (confirm) {
                    JumpUtil.startAct(context, LoginActivity.class);
                    ActivityManager.getInstance().finishActivitys();
                }
            }).setPositiveButton("确定").setTitle("提示").show();
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
}
