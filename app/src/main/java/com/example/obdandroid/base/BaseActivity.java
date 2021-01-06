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
import com.example.obdandroid.utils.ActivityManager;
import com.example.obdandroid.utils.SPUtil;
import com.example.obdandroid.utils.StringUtil;
import com.hacknife.immersive.Immersive;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Set;

import static com.example.obdandroid.config.Constant.EXPIRETIME;
import static com.example.obdandroid.config.Constant.TOKEN;
import static com.example.obdandroid.config.Constant.USERID;


/**
 * 作者：Jealous
 * 日期：2018/11/2 0002 15:44
 * Activity 封装基类
 */

public abstract class BaseActivity extends AppCompatActivity {
    private static final String TAG = BaseActivity.class.getSimpleName();
    public Context mContext;
    private final SparseArray<View> mViews = new SparseArray<>();
    private WeakReference<Activity> weakReference = null;
    private String token;
    private String userId = "";
    private String expireTime = "";

    //布局文件ID
    protected abstract int getContentViewId();

    //布局中装载Fragment的ID
    protected abstract int getFragmentContentId();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //将activity添加到列表中
        mContext = this;
        if (weakReference == null) {
            weakReference = new WeakReference<>(this);
        }
        ActivityManager.getInstance().addActivity(weakReference.get());
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
        SPUtil spUtil = new SPUtil(mContext);
        token = spUtil.getString(TOKEN, "");
        userId = spUtil.getString(USERID, "");
        expireTime = spUtil.getString(EXPIRETIME, "");
        if (!StringUtil.isNull(token)) {
            setToken(token);
        }
        if (!StringUtil.isNull(userId)) {
            setUserId(userId);
        }
        if (!StringUtil.isNull(expireTime)) {
            setExpireTime(expireTime);
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


    /**
     * @param resId 资源id
     *              弹吐司方法
     */
    public void showToast(int resId) {
        Toast.makeText(mContext, resId, Toast.LENGTH_SHORT).show();
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
