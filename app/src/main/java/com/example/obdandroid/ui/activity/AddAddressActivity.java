package com.example.obdandroid.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
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
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseActivity;
import com.example.obdandroid.http.HttpService;
import com.example.obdandroid.http.ResponseCallBack;
import com.example.obdandroid.ui.adapter.AddressAdapter;
import com.example.obdandroid.ui.entity.ResultEntity;
import com.example.obdandroid.ui.entity.ReverseGeoAddressEntity;
import com.example.obdandroid.ui.view.CustomeDialog;
import com.example.obdandroid.ui.view.finger.JDCityPicker;
import com.example.obdandroid.ui.view.popwindow.CustomPop;
import com.example.obdandroid.utils.BluetoothService;
import com.example.obdandroid.utils.DialogUtils;
import com.example.obdandroid.utils.PermissionUtils;
import com.example.obdandroid.utils.SharedPreferencesUtil;
import com.example.obdandroid.utils.StringUtil;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.wyt.searchedittext.SearchEditText;

import java.util.List;

import static com.example.obdandroid.config.Constant.MAP_SERVER_AK;
import static com.example.obdandroid.config.Constant.OUT_PUT;

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
    private SuggestionSearch mSuggestionSearch;
    private String location;
    private AddressAdapter adapter;
    private CustomPop customPop;
    private DialogUtils dialogUtils;
    private final String[] permissions = {
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.INTERNET,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_CONTACTS,
    };

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
        dialogUtils = new DialogUtils(context);
        // mSuggestionSearch = SuggestionSearch.newInstance();
        PermissionUtils.requestPermission(this,permissions);
      /*  if (isLocServiceEnable(context)) {
            openGPS();
        }*/
        // initLocation();
        //initLocationOption();
        layoutSelectName.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, REQUEST_CODE);
        });
        layoutSelectArea.setOnClickListener(v -> {
            bgAlpha(0.7f);
            mJDCityPicker = new JDCityPicker(context, (province, city, area) -> tvSelectArea.setText(province + " " + city + " " + area+","));
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
            addAppUserAddress(getToken(), getUserId(), tvPhone.getText().toString(), tvName.getText().toString(), detailsAddress, false);
        });

        ivLocation.setOnClickListener(v -> {
            if (!StringUtil.isNull(location)) {
                reverseGeoAddress(location);
            } else {
                reverseGeoAddress(SharedPreferencesUtil.getString(context, SharedPreferencesUtil.LOCATION, ""));
            }
        });
      /*  mSuggestionSearch.setOnGetSuggestionResultListener(suggestionResult -> {
            LogE("地点：" + JSON.toJSONString(suggestionResult.getAllSuggestions()));
            suggestionResult.getAllSuggestions().remove(0);
            showAddress(tvDetailsAddress, null, suggestionResult);
        });*/
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
     * 初始化定位参数配置
     */
    private void initLocationOption() {
        //定位服务的客户端。宿主程序在客户端声明此类，并调用，目前只支持在主线程中启动
        LocationClient locationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类实例并配置定位参数
        LocationClientOption locationOption = new LocationClientOption();
        MyLocationListener myLocationListener = new MyLocationListener();
        //注册监听函数
        locationClient.registerLocationListener(myLocationListener);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        locationOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
        locationOption.setCoorType("bd09ll");
        //可选，默认0，即仅定位一次，设置发起连续定位请求的间隔需要大于等于1000ms才是有效的
        locationOption.setScanSpan(1000);
        //可选，设置是否需要地址信息，默认不需要
        locationOption.setIsNeedAddress(true);
        //可选，设置是否需要地址描述
        locationOption.setIsNeedLocationDescribe(true);
        //可选，设置是否需要设备方向结果
        locationOption.setNeedDeviceDirect(false);
        //可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        locationOption.setLocationNotify(true);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        locationOption.setIgnoreKillProcess(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        locationOption.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        locationOption.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否收集CRASH信息，默认收集
        locationOption.SetIgnoreCacheException(false);
        //可选，默认false，设置是否开启Gps定位
        locationOption.setOpenGps(true);
        //可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
        locationOption.setIsNeedAltitude(false);
        //设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者，该模式下开发者无需再关心定位间隔是多少，定位SDK本身发现位置变化就会及时回调给开发者
        locationOption.setOpenAutoNotifyMode();
        //设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者
        locationOption.setOpenAutoNotifyMode(3000, 1, LocationClientOption.LOC_SENSITIVITY_HIGHT);
        //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        locationClient.setLocOption(locationOption);
        //开始定位
        locationClient.start();
    }

    /**
     * @param view   view
     * @param entity 附件地址
     *               展示附件地址
     */
    @SuppressLint("SetTextI18n")
    private void showAddress(View view, ReverseGeoAddressEntity entity, SuggestionResult suggestionResult) {
        customPop = CustomPop.show(context, R.layout.pop_company, view, (dialog, rootView) -> {
            TextView tvTitle = rootView.findViewById(R.id.tvTitle);
            RecyclerView recycleAddress = rootView.findViewById(R.id.recycleAddress);
            SearchEditText etSearch = rootView.findViewById(R.id.etSearch);
            String city = null;
            if (entity != null) {
                city = entity.getResult().getAddressComponent().getCity();
            }
            if (suggestionResult != null) {
                city = suggestionResult.getAllSuggestions().get(0).getCity();
            }
            tvTitle.setText("请选择详细地址(当前城市:" + city + ")");
            LinearLayoutManager manager = new LinearLayoutManager(context);
            manager.setOrientation(OrientationHelper.VERTICAL);
            recycleAddress.setLayoutManager(manager);
            adapter = new AddressAdapter(context);
            if (entity != null) {
                adapter.setList(entity.getResult().getPois());
            }
            if (suggestionResult != null) {
                adapter.setInfos(suggestionResult.getAllSuggestions());
            }
            recycleAddress.setAdapter(adapter);
            adapter.setClickCallBack(new AddressAdapter.OnClickCallBack() {
                @Override
                public void clickGeo(ReverseGeoAddressEntity.ResultEntity.PoisEntity entity) {
                    tvDetailsAddress.setText(entity.getAddr());
                    customPop.doDismiss();
                }

                @Override
                public void clickSuggestion(SuggestionResult.SuggestionInfo entity) {
                    tvDetailsAddress.setText(entity.getAddress());
                    customPop.doDismiss();
                }
            });
            String finalCity = city;
            etSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    mSuggestionSearch.requestSuggestion(new SuggestionSearchOption().city(finalCity).keyword(s.toString()));
                }
            });
        });
    }

    private void reverseGeoAddress(String location) {
        dialogUtils.showProgressDialog();
        HttpService.getInstance().reverseGeo(MAP_SERVER_AK, OUT_PUT, location, "1").
                enqueue(new ResponseCallBack(new ResponseCallBack.CallBack() {
                    @Override
                    public void onSuccess(String response) {
                        ReverseGeoAddressEntity entity = JSON.parseObject(response, ReverseGeoAddressEntity.class);
                        if (entity.getStatus() == 0) {
                            dialogUtils.dismiss();
                            showAddress(tvDetailsAddress, entity, null);
                        } else {
                            showTipDialog("未获取到定位");
                            dialogUtils.dismiss();
                        }
                    }

                    @Override
                    public void onFail(Throwable t) {
                        dialogUtils.dismiss();
                    }
                }));
    }

    /**
     * @param token     接口令牌
     * @param appUserId 用户id
     * @param telephone 手机号码
     * @param contacts  联系人
     * @param address   收货地址
     * @param isDefault 是否默认地址
     *                  添加收货地址
     */
    private void addAppUserAddress(String token, String appUserId, String telephone, String contacts, String address, boolean isDefault) {
        HttpService.getInstance().
                addAppUserAddress(token, appUserId, telephone, contacts, address, isDefault).
                enqueue(new ResponseCallBack(new ResponseCallBack.CallBack() {
                    @Override
                    public void onSuccess(String response) {
                        ResultEntity entity = JSON.parseObject(response, ResultEntity.class);
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

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            //获取纬度信息
            double latitude = bdLocation.getLatitude();
            //获取经度信息
            double longitude = bdLocation.getLongitude();
            //获取定位精度，默认值为0.0f
            float radius = bdLocation.getRadius();
            //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准
            String coorType = bdLocation.getCoorType();
            //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明
            int errorCode = bdLocation.getLocType();
            LogE("latitude:" + latitude);
            LogE("longitude:" + longitude);
            LogE("radius:" + radius);
            LogE("coorType:" + coorType);
            LogE("errorCode:" + errorCode);
            location = latitude + "," + longitude;
            SharedPreferencesUtil.putString(context, SharedPreferencesUtil.LOCATION, location);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       // mSuggestionSearch.destroy();
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