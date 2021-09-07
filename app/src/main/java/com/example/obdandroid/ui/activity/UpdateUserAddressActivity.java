package com.example.obdandroid.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseActivity;
import com.example.obdandroid.config.Constant;
import com.example.obdandroid.http.HttpService;
import com.example.obdandroid.ui.entity.NearbyAddressEntity;
import com.example.obdandroid.ui.entity.ResultEntity;
import com.example.obdandroid.ui.view.CustomeDialog;
import com.example.obdandroid.ui.view.finger.JDCityPicker;
import com.example.obdandroid.utils.LocationUtils;
import com.example.obdandroid.utils.StringUtil;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Callback;

import static com.example.obdandroid.config.APIConfig.reverse_geocoding_URL;

/**
 * 作者：Jealous
 * 日期：2021/9/3 0003
 * 描述：
 */
public class UpdateUserAddressActivity extends BaseActivity {
    private EditText tvName;
    private EditText tvPhone;
    private TextView tvSelectArea;
    private EditText tvDetailsAddress;
    private Context context;
    private String id;
    private JDCityPicker mJDCityPicker;
    private static final int REQUEST_CODE = 101;
    private static final int GPS_REQUEST_CODE = 1;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_update_address;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void initView() {
        super.initView();
        context = this;
        id = getIntent().getStringExtra(Constant.ACT_FLAG);
        TitleBar titleBarSet = findViewById(R.id.titleBarSet);
        tvName = findViewById(R.id.tvName);
        LinearLayout layoutSelectName = findViewById(R.id.layoutSelectName);
        tvPhone = findViewById(R.id.tvPhone);
        LinearLayout layoutSelectArea = findViewById(R.id.layoutSelectArea);
        tvSelectArea = findViewById(R.id.tvSelectArea);
        tvDetailsAddress = findViewById(R.id.tvDetailsAddress);
        Button btnUpdate = findViewById(R.id.btnUpdate);
        layoutSelectName.setOnClickListener(v -> addPermissByPermissionList(this, new String[]{Manifest.permission.READ_CONTACTS}, 10));
        layoutSelectArea.setOnClickListener(v -> {
            bgAlpha(0.7f);
            mJDCityPicker = new JDCityPicker(context, (province, city, area) -> tvSelectArea.setText(province + "   " + city + "   " + area));
            mJDCityPicker.showAtLocation(tvSelectArea, Gravity.BOTTOM, 0, 0);
            mJDCityPicker.setOnDismissListener(() -> bgAlpha(1.0f));
        });
        btnUpdate.setOnClickListener(v -> {
            if (StringUtil.isNull(tvName.getText().toString())) {
                showTipDialog("请填写联系人姓名");
                return;
            }
            if (StringUtil.isNull(tvPhone.getText().toString())) {
                showTipDialog("请填写手机号码");
                return;
            }
            if (StringUtil.isNull(tvSelectArea.getText().toString())) {
                showTipDialog("请选择地区");
                return;
            }
            if (StringUtil.isNull(tvDetailsAddress.getText().toString())) {
                showTipDialog("请填写详细地址");
                return;
            }
            String detailsAddress = tvSelectArea.getText().toString() + " " + tvDetailsAddress.getText().toString();
            updateAppUserAddress(id,getToken(), getUserId(), tvPhone.getText().toString(), tvName.getText().toString(), detailsAddress);
        });
        getLocation();
        titleBarSet.setOnTitleBarListener(new OnTitleBarListener() {
            @Override
            public void onLeftClick(View v) {
                finish();
            }

            @Override
            public void onTitleClick(View v) {

            }

            @Override
            public void onRightClick(View v) {

            }
        });
    }

    //背景变暗
    private void bgAlpha(float f) {
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.alpha = f;
        getWindow().setAttributes(layoutParams);
    }


    /**
     * @param token     接口令牌
     * @param appUserId 用户id
     * @param telephone 手机号码
     * @param contacts  联系人
     * @param address   收货地址
     */
    private void updateAppUserAddress(String id, String token, String appUserId, String telephone, String contacts, String address) {
        HttpService.getInstance().updateAppUserAddress(id, token, appUserId, telephone, contacts, address, false).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {
                String msg = null;
                try {
                    msg = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ResultEntity entity = JSON.parseObject(msg, ResultEntity.class);
                if (entity.isSuccess()) {
                    new CustomeDialog(context, entity.getMessage(), confirm -> {
                        if (confirm) {
                            setResult(101, new Intent());
                            finish();
                        }
                    }).setPositiveButton("知道了").setTitle("删除提示").show();
                }
            }

            @Override
            public void onFailure(@NonNull retrofit2.Call<ResponseBody> call, @NonNull Throwable throwable) {

            }
        });
    }

    private void getLocation() {
        //先申请定位需要用到的权限
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        //没有权限则申请
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(this, permissions, 1);
        } else {
            LocationUtils.register(this, 60 * 10 * 1000, 1000, new LocationUtils.OnLocationChangeListener() {
                @Override
                public void getLastKnownLocation(Location location) {
                    LogE("latitude:" + location.getLatitude() + "longitude" + location.getLongitude());
                    getAddresses(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
                }

                @Override
                public void onLocationChanged(Location location) {
                    LogE("latitude:" + location.getLatitude() + "longitude" + location.getLongitude());
                    getAddresses(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void openGps(boolean isOpen) {
                    if (!isOpen) {
                        openGPSSetting();
                    }
                }
            });
        }
    }

    //获取附近位置
    private void getAddresses(String latitude, String longitude) {
        if (!TextUtils.isEmpty(latitude) && !TextUtils.isEmpty(longitude)) {
            requestAddresses(Constant.MAP_SERVER_AK, latitude + "," + longitude);
        }
    }

    private void openGPSSetting() {
        new CustomeDialog(context, "打开GPS定位", confirm -> {
            //跳转到手机原生设置页面
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, GPS_REQUEST_CODE);
        }).setPositiveButton("确定").setTitle("定位").show();
    }

    /**
     * @param ak       地图ak
     * @param location 坐标
     *                 获取周边地址
     */
    public void requestAddresses(String ak, String location) {
        OkHttpUtils.get().url(reverse_geocoding_URL).
                addParam("ak", ak).
                addParam("output", "json").
                addParam("coordtype", "wgs84ll").
                addParam("location", location).
                addParam("extensions_poi", "1").
                build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                LogE("获取周边地址:" + response);
                NearbyAddressEntity entity = JSON.parseObject(response, NearbyAddressEntity.class);
                if (entity.getStatus() == 0) {

                }
            }
        });
    }

    /**
     * 动态权限
     */
    public void addPermissByPermissionList(Activity activity, String[] permissions, int request) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {   //Android 6.0开始的动态权限，这里进行版本判断
            ArrayList<String> mPermissionList = new ArrayList<>();
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(activity, permission)
                        != PackageManager.PERMISSION_GRANTED) {
                    mPermissionList.add(permission);
                }
            }
            if (mPermissionList.isEmpty()) {  //非初次进入App且已授权
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE);
            } else {
                //请求权限方法
                String[] permissionsNew = mPermissionList.toArray(new String[mPermissionList.size()]);//将List转为数组
                ActivityCompat.requestPermissions(activity, permissionsNew, request); //这个触发下面onRequestPermissionsResult这个回调
            }
        }
    }


    @SuppressWarnings("LoopStatementThatDoesntLoop")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            Uri uri = data.getData();
            ContentResolver cr = getContentResolver();
            try (Cursor cursor = cr.query(uri, null, null, null, null)) {
                while (cursor.moveToNext()) {
                    // 取得联系人名字
                    int nameFieldColumnIndex = cursor.getColumnIndex(
                            ContactsContract.Contacts.DISPLAY_NAME);

                    String contact = cursor.getString(nameFieldColumnIndex);

                    String ContactId = cursor.getString(cursor
                            .getColumnIndex(ContactsContract.Contacts._ID));

                    Cursor phone = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                    + "=" + ContactId,
                            null, null);
                    String num = "";
                    while (phone.moveToNext()) {
                        num = phone.getString(phone.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        break;//只取第一个
                    }
                    tvName.setText(contact);
                    tvPhone.setText(num);
                    phone.close();
                }
            }
        }
    }
}