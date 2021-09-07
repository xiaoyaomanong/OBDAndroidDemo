package com.example.obdandroid.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.baidu.location.BDLocationListener;
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
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseActivity;
import com.example.obdandroid.http.HttpService;
import com.example.obdandroid.ui.adapter.AddressAdapter;
import com.example.obdandroid.ui.entity.ResultEntity;
import com.example.obdandroid.ui.view.CustomeDialog;
import com.example.obdandroid.ui.view.finger.JDCityPicker;
import com.example.obdandroid.ui.view.popwindow.CustomPop;
import com.example.obdandroid.utils.StringUtil;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.wyt.searchedittext.SearchEditText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private JDCityPicker mJDCityPicker;
    private static final int REQUEST_CODE = 101;
    private static final int GPS_REQUEST_CODE = 1;
    private GeoCoder mCoder;
    private SuggestionSearch mSuggestionSearch;
    private double latitude;
    private double longitude;
    private boolean isPermissionRequested;
    public LocationClient mLocationClient = null;
    private AddressAdapter adapter;
    private CustomPop customPop;
    private String city;

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
        ImageView ivLocation = findViewById(R.id.ivLocation);
        LinearLayout layoutSelectName = findViewById(R.id.layoutSelectName);
        LinearLayout layoutSelectArea = findViewById(R.id.layoutSelectArea);
        Button btnSave = findViewById(R.id.btnSave);
        mCoder = GeoCoder.newInstance();
        mSuggestionSearch = SuggestionSearch.newInstance();
        mLocationClient = new LocationClient(context);
        requestPermission();
        if (!isLocServiceEnable(context)) {
            openGPS();
        }
        initLocation();
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

        ivLocation.setOnClickListener(v ->
                mCoder.reverseGeoCode(new ReverseGeoCodeOption()
                        .location(new LatLng(latitude, longitude))
                        // 设置是否返回新数据 默认值0不返回，1返回
                        .newVersion(1)
                        // POI召回半径，允许设置区间为0-1000米，超过1000米按1000米召回。默认值为1000
                        .radius(1000)));
        mCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
                if (null != geoCodeResult && null != geoCodeResult.getLocation()) {
                    if (geoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
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
                    city = reverseGeoCodeResult.getAddressDetail().city;
                    showPopMenu(tvDetailsAddress, reverseGeoCodeResult.getPoiList());
                }
            }
        });
        mSuggestionSearch.setOnGetSuggestionResultListener(new OnGetSuggestionResultListener() {
            @Override
            public void onGetSuggestionResult(SuggestionResult suggestionResult) {
                LogE("地点："+JSON.toJSONString(suggestionResult.getAllSuggestions()));
            }
        });

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
    @SuppressLint("SetTextI18n")
    private void showPopMenu(View view, List<PoiInfo> list) {
        customPop = CustomPop.show(context, R.layout.pop_company, view, (dialog, rootView) -> {
            TextView tvTitle = rootView.findViewById(R.id.tvTitle);
            RecyclerView recycleAddress = rootView.findViewById(R.id.recycleAddress);
            SearchEditText etSearch = rootView.findViewById(R.id.etSearch);
            tvTitle.setText("请选择详细地址(当前城市:" + city + ")");
            LinearLayoutManager manager = new LinearLayoutManager(context);
            manager.setOrientation(OrientationHelper.VERTICAL);
            recycleAddress.setLayoutManager(manager);
            adapter = new AddressAdapter(context);
            adapter.setList(list);
            recycleAddress.setAdapter(adapter);
            adapter.setClickCallBack(entity -> {
                tvDetailsAddress.setText(entity.getAddress());
                customPop.doDismiss();
            });
            etSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    searchCity(city, s.toString());
                }
            });
        });
    }

    /**
     * @param city    城市
     * @param keyword 关键字
     *                keyword为随您的输入变化的值
     */
    private void searchCity(String city, String keyword) {
        mSuggestionSearch.requestSuggestion(new SuggestionSearchOption().city(city).keyword(keyword));
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
                }
            }

            if (!permissionsList.isEmpty()) {
                String[] strings = new String[permissionsList.size()];
                requestPermissions(permissionsList.toArray(strings), 0);
            }
        }
    }

    /**
     * @param alpha 透明度
     *              背景变暗
     */
    private void bgAlpha(float alpha) {
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.alpha = alpha;
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
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
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
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {

            }
        });
    }

    /**
     * 定位初始化
     */
    public void initLocation() {
        // 定位初始化
        mLocationClient = new LocationClient(this);
        MyLocationListener myListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        // 打开gps
        option.setOpenGps(true);
        // 设置坐标类型
        option.setCoorType("bd09ll");
        option.setScanSpan(1000);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    /**
     * 打开GPS定位设置
     */
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCoder.destroy();
        mLocationClient.stop();
        mSuggestionSearch.destroy();
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // MapView 销毁后不在处理新接收的位置
            if (location == null) {
                return;
            }
            latitude = location.getLatitude();    //获取纬度信息
            longitude = location.getLongitude();    //获取经度信息
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
                    int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                    String contact = cursor.getString(nameFieldColumnIndex);
                    String ContactId = cursor.getString(cursor
                            .getColumnIndex(ContactsContract.Contacts._ID));
                    Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                    + "=" + ContactId, null, null);
                    String num = "";
                    while (phone.moveToNext()) {
                        num = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
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