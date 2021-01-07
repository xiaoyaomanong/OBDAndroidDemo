package com.example.obdandroid.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.obdandroid.utils.SPUtil;
import com.example.obdandroid.utils.StringUtil;

import static com.example.obdandroid.config.Constant.EXPIRE_TIME;
import static com.example.obdandroid.config.Constant.TOKEN;
import static com.example.obdandroid.config.Constant.USER_ID;


/**
 * 作者：Jealous
 * 日期：2018/11/2 0002 15:44
 * Fragment封装基类
 */
public abstract class BaseFragment extends Fragment {
    private static final String TAG = BaseFragment.class.getSimpleName();
    protected BaseActivity mActivity; // 宿主Activity
    private final SparseArray<View> mViews = new SparseArray<View>();
    protected View frView;
    private String token;
    private String userId = "";
    private String expireTime = "";

    //获取布局文件ID
    protected abstract int getLayoutId();

    // 初始化视图方法
    public abstract void initView(View view, Bundle savedInstanceState);


    public void setFrView(View frView) {
        this.frView = frView;
    }

    //获取宿主Activity
    protected BaseActivity getHoldingActivity() {
        return mActivity;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = (BaseActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (frView == null) {
            frView = inflater.inflate(getLayoutId(), container, false);
        }
        // 缓存的rootView需要判断是否已经被加过parent，
        // 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) frView.getParent();
        if (parent != null) {
            parent.removeView(frView);
        }
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        SPUtil spUtil = new SPUtil(getHoldingActivity());
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
        initView(frView, savedInstanceState);
        setFrView(frView);
        return frView;
    }

    /**
     * @param viewId 控件ID
     * @param <T>
     * @return 替代findViewById方法
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = frView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
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
     * 弹吐司方法     这样设置方便每个类调用，不用多次点击多次弹出
     */
    public void showToast(String text) {
        Toast.makeText(getHoldingActivity(), text, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onResume() {
        super.onResume();
    }

}
