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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
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
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseActivity;
import com.example.obdandroid.config.Constant;
import com.example.obdandroid.http.HttpService;
import com.example.obdandroid.http.ResponseCallBack;
import com.example.obdandroid.ui.adapter.AddressAdapter;
import com.example.obdandroid.ui.entity.ResultEntity;
import com.example.obdandroid.ui.entity.ReverseGeoAddressEntity;
import com.example.obdandroid.ui.view.CustomeDialog;
import com.example.obdandroid.ui.view.finger.JDCityPicker;
import com.example.obdandroid.ui.view.popwindow.CustomPop;
import com.example.obdandroid.utils.StringUtil;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.wyt.searchedittext.SearchEditText;

import java.util.ArrayList;
import java.util.List;

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
    private double latitude;
    private double longitude;
    private boolean isPermissionRequested;
    public LocationClient mLocationClient = null;
    private AddressAdapter adapter;
    private CustomPop customPop;

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
        ImageView ivLocation = findViewById(R.id.ivLocation);
        mLocationClient = new LocationClient(context);
        requestPermission();
        if (isLocServiceEnable(context)) {
            openGPS();
        }

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
            updateAppUserAddress(id, getToken(), getUserId(), tvPhone.getText().toString(), tvName.getText().toString(), detailsAddress);
        });
       /* ivLocation.setOnClickListener(v ->
                mCoder.reverseGeoCode(new ReverseGeoCodeOption()
                        .location(new LatLng(latitude, longitude))
                        // 设置是否返回新数据 默认值0不返回，1返回
                        .newVersion(1)
                        // POI召回半径，允许设置区间为0-1000米，超过1000米按1000米召回。默认值为1000
                        .radius(1000)));*/
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
     * @param view   view
     * @param entity 附件地址
     *               展示附件地址
     */
    @SuppressLint("SetTextI18n")
    private void showAddress(View view, ReverseGeoAddressEntity entity) {
        customPop = CustomPop.show(context, R.layout.pop_company, view, (dialog, rootView) -> {
            TextView tvTitle = rootView.findViewById(R.id.tvTitle);
            RecyclerView recycleAddress = rootView.findViewById(R.id.recycleAddress);
            SearchEditText etSearch = rootView.findViewById(R.id.etSearch);
            tvTitle.setText("请选择详细地址(当前城市:" + entity.getResult().getAddressComponent().getCity() + ")");
            LinearLayoutManager manager = new LinearLayoutManager(context);
            manager.setOrientation(OrientationHelper.VERTICAL);
            recycleAddress.setLayoutManager(manager);
            adapter = new AddressAdapter(context);
            adapter.setList(entity.getResult().getPois());
            recycleAddress.setAdapter(adapter);
            adapter.setClickCallBack(new AddressAdapter.OnClickCallBack() {
                @Override
                public void clickGeo(ReverseGeoAddressEntity.ResultEntity.PoisEntity entity) {
                    tvDetailsAddress.setText(entity.getAddr());
                    customPop.doDismiss();
                }

                @Override
                public void clickSuggestion(SuggestionResult.SuggestionInfo entity) {

                }
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
                   // mSuggestionSearch.requestSuggestion(new SuggestionSearchOption().city(city).keyword(s.toString()));
                }
            });
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
                }
            }

            if (!permissionsList.isEmpty()) {
                String[] strings = new String[permissionsList.size()];
                requestPermissions(permissionsList.toArray(strings), 0);
            }
        }
    }


    /**
     * @param token     接口令牌
     * @param appUserId 用户id
     * @param telephone 手机号码
     * @param contacts  联系人
     * @param address   收货地址
     */
    private void updateAppUserAddress(String id, String token, String appUserId, String telephone, String contacts, String address) {
        HttpService.getInstance().
                updateAppUserAddress(id, token, appUserId, telephone, contacts, address, false).
                enqueue(new ResponseCallBack(new ResponseCallBack.CallBack() {
                    @Override
                    public void onSuccess(String response) {
                        ResultEntity entity = JSON.parseObject(response, ResultEntity.class);
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
                    public void onFail(Throwable t) {

                    }
                }));
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
        mLocationClient.stop();
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