package com.example.obdandroid;

import android.app.Application;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Looper;
import android.support.multidex.MultiDex;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.widget.TextView;

import com.example.obdandroid.base.BaseActivity;
import com.example.obdandroid.ui.wechatPay.WeiXinConstants;
import com.example.obdandroid.utils.ExceptionHandler;
import com.hjq.bar.TitleBar;
import com.hjq.bar.initializer.LightBarInitializer;
import com.kongzue.dialog.v2.DialogSettings;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import static com.kongzue.dialog.v2.DialogSettings.STYLE_IOS;
import static com.kongzue.dialog.v2.DialogSettings.THEME_DARK;
import static com.kongzue.dialog.v2.DialogSettings.THEME_LIGHT;

/**
 * 作者：Jealous
 * 日期：2020/12/22 0022
 * 描述：2222
 */
public class MainApplication extends Application {
    public static Context context;
    private final String TAG = BaseActivity.class.getSimpleName();
    //不管是蓝牙连接方还是服务器方，得到socket对象后都传入
    public static BluetoothSocket bluetoothSocket;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this.getApplicationContext();
        IWXAPI api = WXAPIFactory.createWXAPI(this, WeiXinConstants.APP_ID, false);//通过WXAPIFactory工厂，获取IWXAPI的实例,IWXAPI 是第三方app和微信通信的openapi接口
        api.registerApp(WeiXinConstants.APP_ID);
        // 初始化 TitleBar
        TitleBar.setDefaultInitializer(new LightBarInitializer() {
            @Override
            protected TextView createTextView(Context context) {
                return new AppCompatTextView(context);
            }
        });
        ExceptionHandler.getInstance().initConfig(context, error -> Log.e(TAG, error));
        DialogSettings.dialog_theme = THEME_LIGHT;
        DialogSettings.tip_theme = THEME_LIGHT;
        DialogSettings.style = STYLE_IOS;
        DialogSettings.use_blur = false;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    public static Context getContext() {
        return context;
    }
}