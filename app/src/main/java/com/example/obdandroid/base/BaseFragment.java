package com.example.obdandroid.base;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.obdandroid.ui.activity.LoginActivity;
import com.example.obdandroid.ui.entity.BluetoothDeviceEntity;
import com.example.obdandroid.ui.view.CustomeDialog;
import com.example.obdandroid.utils.ActivityManager;
import com.example.obdandroid.utils.JumpUtil;
import com.example.obdandroid.utils.SPUtil;
import com.example.obdandroid.utils.StringUtil;
import com.example.obdandroid.utils.ToastUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static com.example.obdandroid.config.Constant.EXPIRE_TIME;
import static com.example.obdandroid.config.Constant.TOKEN;
import static com.example.obdandroid.config.Constant.USER_ID;
import static com.example.obdandroid.config.Constant.USER_NAME;


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
    private String phone = "";
    private List<BluetoothDeviceEntity> blueList = new ArrayList<>();

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 初始化蓝牙
     */
    public List<BluetoothDeviceEntity> getBlueTooth() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter != null) {
            if (!adapter.isEnabled()) {
                adapter.enable();
                //睡一秒钟，避免不发现
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

          /*  Set<BluetoothDevice> devices = adapter.getBondedDevices();
            blueList = new ArrayList<>();
            for (BluetoothDevice bluetoothDevice : devices) {
                HashMap<String, Object> blueHashMap = new HashMap<>();
                blueHashMap.put("blue_device", bluetoothDevice);
                blueHashMap.put("blue_name", bluetoothDevice.getName());
                blueHashMap.put("blue_address", bluetoothDevice.getAddress());
                blueList.add(blueHashMap);
            }*/
            // getConnectedBtDevice(adapter);
            return getConnectedBtDevice(adapter);
        } else {
            ToastUtil.shortShow("本机没有蓝牙设备");
            return null;
        }
    }

    //获取已连接的蓝牙设备
    private List<BluetoothDeviceEntity> getConnectedBtDevice(BluetoothAdapter adapter) {
        Class<BluetoothAdapter> bluetoothAdapterClass = BluetoothAdapter.class;//得到BluetoothAdapter的Class对象
        try {
            //得到连接状态的方法
            Method method = bluetoothAdapterClass.getDeclaredMethod("getConnectionState", (Class[]) null);
            //打开权限
            method.setAccessible(true);
            int state = (int) method.invoke(adapter, (Object[]) null);
            Set<BluetoothDevice> devices = adapter.getBondedDevices(); //集合里面包括已绑定的设备和已连接的设备
            for (BluetoothDevice device : devices) {
                Method isConnectedMethod = BluetoothDevice.class.getDeclaredMethod("isConnected", (Class[]) null);
                method.setAccessible(true);
                boolean isConnected = (boolean) isConnectedMethod.invoke(device, (Object[]) null);
                BluetoothDeviceEntity entity = new BluetoothDeviceEntity();
                entity.setBlue_address(device.getAddress());
                entity.setBlue_name(device.getName());
                entity.setState(String.valueOf(state));
                entity.setConnected(isConnected);//根据状态来区分是已连接的还是已绑定的，isConnected为true表示是已连接状态。
                blueList.add(entity);
            }
            return blueList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param macAddress 设备mac地址,例如"78:02:B7:01:01:16"
     * @return 判断给定的设备mac地址是否已连接经典蓝牙
     */
    public static boolean isConnectClassicBT(String macAddress) {
        if (TextUtils.isEmpty(macAddress)) {
            return false;
        }
        final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Class<BluetoothAdapter> bluetoothAdapterClass = BluetoothAdapter.class;//得到BluetoothAdapter的Class对象
        try {
            //是否存在连接的蓝牙设备
            Method method = bluetoothAdapterClass.getDeclaredMethod("getConnectionState", (Class[]) null);
            //打开权限
            method.setAccessible(true);
            int state = (int) method.invoke(bluetoothAdapter, (Object[]) null);
            if (state == BluetoothAdapter.STATE_CONNECTED) {
                Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
                for (BluetoothDevice device : devices) {
                    Method isConnectedMethod = BluetoothDevice.class.getDeclaredMethod("isConnected", (Class[]) null);
                    method.setAccessible(true);
                    boolean isConnected = (boolean) isConnectedMethod.invoke(device, (Object[]) null);
                    if (isConnected) {
                        {
                            return macAddress.contains(device.getAddress());
                        }
                    } else {
                        Log.d("test", device.getName() + " connect false(" + device.getAddress() + ")");
                    }
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
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

            }).setPositiveButton("确定").setTitle("提示").show();
        }
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
