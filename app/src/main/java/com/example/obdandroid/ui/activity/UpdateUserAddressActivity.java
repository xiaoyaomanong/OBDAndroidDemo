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
import com.example.obdandroid.ui.entity.AppUserAddressEntity;
import com.example.obdandroid.ui.entity.ResultEntity;
import com.example.obdandroid.ui.entity.ReverseGeoAddressEntity;
import com.example.obdandroid.ui.view.CustomeDialog;
import com.example.obdandroid.ui.view.finger.JDCityPicker;
import com.example.obdandroid.ui.view.popwindow.CustomPop;
import com.example.obdandroid.utils.PermissionUtils;
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
    private JDCityPicker mJDCityPicker;
    private static final int REQUEST_CODE = 101;
    private AppUserAddressEntity.DataEntity.ListEntity entity;
    private final String[] permissions = {
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.INTERNET,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_CONTACTS,
    };

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
        entity = (AppUserAddressEntity.DataEntity.ListEntity) getIntent().getSerializableExtra(Constant.ACT_FLAG);
        TitleBar titleBarSet = findViewById(R.id.titleBarSet);
        tvName = findViewById(R.id.tvName);
        LinearLayout layoutSelectName = findViewById(R.id.layoutSelectName);
        tvPhone = findViewById(R.id.tvPhone);
        LinearLayout layoutSelectArea = findViewById(R.id.layoutSelectArea);
        tvSelectArea = findViewById(R.id.tvSelectArea);
        tvDetailsAddress = findViewById(R.id.tvDetailsAddress);
        Button btnUpdate = findViewById(R.id.btnUpdate);
        setViewData(entity);
        PermissionUtils.requestPermission(this, permissions);
        layoutSelectName.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, REQUEST_CODE);
        });
        layoutSelectArea.setOnClickListener(v -> {
            bgAlpha(0.7f);
            mJDCityPicker = new JDCityPicker(context, (province, city, area) ->tvSelectArea.setText(province + " " + city + " " + area+","));
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
            updateAppUserAddress(entity.getId(), getToken(), getUserId(), tvPhone.getText().toString(), tvName.getText().toString(), detailsAddress,entity.isDefault());
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

    private void setViewData(AppUserAddressEntity.DataEntity.ListEntity entity) {
        tvName.setText(entity.getContacts());
        tvSelectArea.setText(entity.getAddress().split(",")[0]);
        tvDetailsAddress.setText(entity.getAddress().split(",")[1]);
        tvPhone.setText(entity.getTelephone());
    }


    /**
     * @param token     接口令牌
     * @param appUserId 用户id
     * @param telephone 手机号码
     * @param contacts  联系人
     * @param address   收货地址
     */
    private void updateAppUserAddress(String id, String token, String appUserId, String telephone, String contacts, String address,boolean isDefault) {
        HttpService.getInstance().
                updateAppUserAddress(id, token, appUserId, telephone, contacts, address, isDefault).
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
                            }).setPositiveButton("知道了").setTitle("修改提示").show();
                        }
                    }

                    @Override
                    public void onFail(Throwable t) {

                    }
                }));
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