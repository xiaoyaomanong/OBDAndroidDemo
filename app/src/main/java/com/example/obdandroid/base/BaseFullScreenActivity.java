package com.example.obdandroid.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;


import com.example.obdandroid.R;
import com.example.obdandroid.utils.ActivityManager;

import java.lang.ref.WeakReference;

import notchtools.geek.com.notchtools.NotchTools;

/**
 * 作者：Jealous
 * 日期：2019/11/20 0020 11:43
 */
public abstract class BaseFullScreenActivity extends AppCompatActivity {
    private static final String TAG = BaseActivity.class.getSimpleName();
    public Context mContext;
    private SparseArray<View> mViews = new SparseArray<View>();
    private WeakReference<AppCompatActivity> weakReference = null;

    //布局文件ID
    protected abstract int getContentViewId();

    //布局中装载Fragment的ID
    protected abstract int getFragmentContentId();

    /**
     * 刘海容器
     */
    private FrameLayout mNotchContainer;
    /**
     * 主内容区
     */
    private FrameLayout mContentContainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //将activity添加到列表中
        if (weakReference == null) {
            weakReference = new WeakReference<>(this);
        }
        ActivityManager.getInstance().addActivity(weakReference.get());
        mContext = this;
        initView();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.activity_base);
        mNotchContainer = findViewById(R.id.notch_container);
        mNotchContainer.setTag(NotchTools.NOTCH_CONTAINER);
        mContentContainer = findViewById(R.id.content_container);
        onBindContentContainer(layoutResID);
    }

    private void onBindContentContainer(int layoutResID) {
        LayoutInflater.from(this).inflate(layoutResID, mContentContainer, true);
    }

    /**
     * @param viewId 控件ID
     * @param <T>
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


    }

    /**
     * @param msg 日志内容
     *            输出日志
     */
    public void LogE(String msg) {
        Log.e(TAG, msg);
    }

    /**
     * 弹吐司方法     这样设置方便每个类调用，不用多次点击多次弹出
     */
    public void showToast(String text) {
        Toast.makeText(mContext,text,Toast.LENGTH_SHORT).show();
    }

    /**
     * @param resId
     */
    public void showToast(int resId) {
        Toast.makeText(mContext,resId,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 结束Activity&从栈中移除该Activity
        ActivityManager.getInstance().removeActivity(this);
    }
}
