package com.example.obdandroid.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseActivity;
import com.example.obdandroid.config.Constant;
import com.example.obdandroid.http.HttpService;
import com.example.obdandroid.ui.adapter.AddressAdapter;
import com.example.obdandroid.ui.entity.NearbyAddressEntity;
import com.example.obdandroid.ui.entity.ResultEntity;
import com.example.obdandroid.ui.view.CustomeDialog;
import com.example.obdandroid.ui.view.finger.JDCityPicker;
import com.example.obdandroid.ui.view.popwindow.CustomPop;
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
 * 日期：2021/9/2 0002
 * 描述：添加新收货地址
 */
public class AddAddressActivity extends BaseActivity {
    private Context context;
    private TextView tvName;
    private TextView tvPhone;
    private TextView tvSelectArea;
    private TextView tvDetailsAddress;
    private CheckBox idCbSelectDefault;
    private ImageView ivLocation;
    private JDCityPicker mJDCityPicker;
    private static final int REQUEST_CODE = 101;
    private static final int GPS_REQUEST_CODE = 1;
    private GeoCoder mCoder;
    private double latitude;
    private double longitude;
    private boolean isPermissionRequested;
    public LocationClient mLocationClient = null;
    private AddressAdapter adapter;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_add_address;
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
        TitleBar titleBarSet = findViewById(R.id.titleBarSet);
        tvName = findViewById(R.id.tvName);
        tvPhone = findViewById(R.id.tvPhone);
        tvSelectArea = findViewById(R.id.tvSelectArea);
        tvDetailsAddress = findViewById(R.id.tvDetailsAddress);
        idCbSelectDefault = findViewById(R.id.id_cb_select_default);
        ivLocation = findViewById(R.id.ivLocation);
        LinearLayout layoutSelectName = findViewById(R.id.layoutSelectName);
        LinearLayout layoutSelectArea = findViewById(R.id.layoutSelectArea);
        Button btnSave = findViewById(R.id.btnSave);
        mCoder = GeoCoder.newInstance();
        mLocationClient = new LocationClient(context);
        requestPermission();
        if (isLocServiceEnable(context)) {
            openGPS();
        }
        getLocation();
        layoutSelectName.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, REQUEST_CODE);
        });
        layoutSelectArea.setOnClickListener(v -> {
            bgAlpha(0.7f);
            mJDCityPicker = new JDCityPicker(context, (province, city, area) -> tvSelectArea.setText(province + "   " + city + "   " + area));
            mJDCityPicker.showAtLocation(tvSelectArea, Gravity.BOTTOM, 0, 0);
            mJDCityPicker.setOnDismissListener(() -> bgAlpha(1.0f));
        });
        btnSave.setOnClickListener(v -> {
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
            addAppUserAddress(getToken(), getUserId(), tvPhone.getText().toString(), tvName.getText().toString(), detailsAddress, idCbSelectDefault.isChecked());
        });
        mCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
                if (null != geoCodeResult && null != geoCodeResult.getLocation()) {
                    if (geoCodeResult == null || geoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
                        showTipDialog("没有找到检索结果");
                    } else {
                        double latitude = geoCodeResult.getLocation().latitude;
                        double longitude = geoCodeResult.getLocation().longitude;
                        LogE("latitude:" + latitude + ";longitude:" + longitude);
                    }
                }
            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                if (reverseGeoCodeResult == null || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
                    showTipDialog("没有找到检索结果");
                } else {
                    LogE("检索数据：" + JSON.toJSONString(reverseGeoCodeResult.getPoiList()));
                    showPopMenu(tvDetailsAddress, reverseGeoCodeResult.getPoiList());
                }
            }
        });
        ivLocation.setOnClickListener(v -> mCoder.reverseGeoCode(new ReverseGeoCodeOption()
                .location(new LatLng(latitude, longitude))
                // 设置是否返回新数据 默认值0不返回，1返回
                .newVersion(1)
                // POI召回半径，允许设置区间为0-1000米，超过1000米按1000米召回。默认值为1000
                .radius(1000)));

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

    /**
     * @param view view
     * @param list 附件地址
     *             展示附件地址
     */
    private void showPopMenu(View view, List<PoiInfo> list) {
        CustomPop.show(context, R.layout.pop_company, view, (dialog, rootView) -> {
            TextView tvTitle = rootView.findViewById(R.id.tvTitle);
            RecyclerView recycleAddress = rootView.findViewById(R.id.recycleAddress);
            tvTitle.setText("请选择详细地址");
            LinearLayoutManager manager = new LinearLayoutManager(context);
            manager.setOrientation(OrientationHelper.VERTICAL);
            recycleAddress.setLayoutManager(manager);
            adapter = new AddressAdapter(context);
            adapter.setList(list);
            recycleAddress.setAdapter(adapter);
            adapter.setClickCallBack(entity -> tvDetailsAddress.setText(entity.getAddress()));
        });
    }

    /**
     * Android6.0之后需要动态申请权限
     */
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= 23 && !isPermissionRequested) {
            isPermissionRequested = true;
            ArrayList<String> permissionsList = new ArrayList<>();
            String[] permissions = {
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.INTERNET,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.ACCESS_WIFI_STATE,
            };

            for (String perm : permissions) {
                if (PackageManager.PERMISSION_GRANTED != checkSelfPermission(perm)) {
                    permissionsList.add(perm);
                    // 进入到这里代表没有权限.
                }
            }

            if (!permissionsList.isEmpty()) {
                String[] strings = new String[permissionsList.size()];
                requestPermissions(permissionsList.toArray(strings), 0);
            }
        }
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
     *                  添加收货地址
     */
    private void addAppUserAddress(String token, String appUserId, String telephone, String contacts, String address, boolean isDefault) {
        HttpService.getInstance().addAppUserAddress(token, appUserId, telephone, contacts, address, isDefault).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                String msg = null;
                try {
                    if (response.body() != null) {
                        msg = response.body().string();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ResultEntity entity = JSON.parseObject(msg, ResultEntity.class);
                assert entity != null;
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
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable throwable) {

            }
        });
    }

    /**
     * 配置定位SDK参数
     */
    private void setBDLocation() {
        LocationClientOption option = new LocationClientOption();

        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，设置定位模式，默认高精度
        //LocationMode.Hight_Accuracy：高精度；
        //LocationMode. Battery_Saving：低功耗；
        //LocationMode. Device_Sensors：仅使用设备；

        option.setCoorType("bd09ll");
        //可选，设置返回经纬度坐标类型，默认GCJ02
        //GCJ02：国测局坐标；
        //BD09ll：百度经纬度坐标；
        //BD09：百度墨卡托坐标；
        //海外地区定位，无需设置坐标类型，统一返回WGS84类型坐标

        option.setScanSpan(1000);
        //可选，设置发起定位请求的间隔，int类型，单位ms
        //如果设置为0，则代表单次定位，即仅定位一次，默认为0
        //如果设置非0，需设置1000ms以上才有效

        option.setOpenGps(true);
        //可选，设置是否使用gps，默认false
        //使用高精度和仅用设备两种定位模式的，参数必须设置为true

        option.setLocationNotify(true);
        //可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false

        option.setIgnoreKillProcess(false);
        //可选，定位SDK内部是一个service，并放到了独立进程。
        //设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)

        option.SetIgnoreCacheException(false);
        //可选，设置是否收集Crash信息，默认收集，即参数为false

        option.setWifiCacheTimeOut(5 * 60 * 1000);
        //可选，V7.2版本新增能力
        //如果设置了该接口，首次启动定位时，会先判断当前Wi-Fi是否超出有效期，若超出有效期，会先重新扫描Wi-Fi，然后定位

        option.setEnableSimulateGps(false);
        //可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false

        option.setNeedNewVersionRgc(true);
        //可选，设置是否需要最新版本的地址信息。默认需要，即参数为true

        mLocationClient.setLocOption(option);
        //mLocationClient为第二步初始化过的LocationClient对象
        //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        //更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明
    }

    private void getLocation() {
        setBDLocation();
        mLocationClient.start();
        mLocationClient.registerLocationListener(new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                latitude = location.getLatitude();    //获取纬度信息
                longitude = location.getLongitude();    //获取经度信息
            }
        });
    }

    //获取附近位置
    private void getAddresses(String latitude, String longitude) {
        if (!TextUtils.isEmpty(latitude) && !TextUtils.isEmpty(longitude)) {
            requestAddresses(Constant.MAP_SERVER_AK, latitude + "," + longitude);
        }
    }

    private void openGPS() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请打开GPS连接")
                .setIcon(R.drawable.ic_gps)
                .setMessage("为了提高定位的准确度，更好的为您服务，请打开GPS")
                .setPositiveButton("设置", (dialogInterface, i) -> {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(intent, GPS_REQUEST_CODE);
                })
                .setNeutralButton("取消", (dialogInterface, i) -> dialogInterface.dismiss()).show();
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
                NearbyAddressEntity entity = JSON.parseObject(response, NearbyAddressEntity.class);
                if (entity.getStatus() == 0) {

                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCoder.destroy();
        mLocationClient.stop();
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