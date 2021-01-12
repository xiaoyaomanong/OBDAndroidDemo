package com.example.obdandroid.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseActivity;
import com.example.obdandroid.ui.entity.BrandPinYinEntity;
import com.example.obdandroid.ui.entity.CarModelEntity;
import com.example.obdandroid.ui.entity.ResultEntity;
import com.example.obdandroid.ui.entity.VocationalDictDataListEntity;
import com.example.obdandroid.ui.view.CustomeDialog;
import com.example.obdandroid.ui.view.progressButton.CircularProgressButton;
import com.example.obdandroid.utils.JumpUtil;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.kongzue.dialog.v2.TipDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;
import okhttp3.Response;

import static com.example.obdandroid.config.APIConfig.ADD_VEHICLE_URL;
import static com.example.obdandroid.config.APIConfig.FAULT_CODE_URL;
import static com.example.obdandroid.config.APIConfig.SERVER_URL;

/**
 * 作者：Jealous
 * 日期：2021/1/9 0009
 * 描述：
 */
public class AddVehiclActivity extends BaseActivity {
    private Context context;
    private TitleBar titleBarSet;
    private TextView tvAutomobileBrandName;
    private TextView tvModelName;
    private TextView tvFuelType;
    private TextView tvTransmissionType;
    private EditText tvCurrentMileage;
    private TextView tvBluetoothDeviceNumber;
    private CircularProgressButton btnAdd;
    private String automobileBrandId;
    private String automobileBrandName;
    private String modelId;
    private String modelName;
    private String fuelType;
    private String transmissionType;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_add_vehicl;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    public void initView() {
        super.initView();
        context = this;
        titleBarSet = findViewById(R.id.titleBarSet);
        tvAutomobileBrandName = findViewById(R.id.tvAutomobileBrandName);
        tvModelName = findViewById(R.id.tvModelName);
        tvFuelType = findViewById(R.id.tvFuelType);
        tvTransmissionType = findViewById(R.id.tvTransmissionType);
        tvCurrentMileage = findViewById(R.id.tvCurrentMileage);
        tvBluetoothDeviceNumber = findViewById(R.id.tvBluetoothDeviceNumber);
        btnAdd = findViewById(R.id.btnAdd);
        tvAutomobileBrandName.setOnClickListener(v -> {
            Intent intent = new Intent(context, AutomobileBrandActivity.class);
            startActivityForResult(intent, 102);
            ((Activity) context).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);//下往上推出效果
        });
        tvModelName.setOnClickListener(v -> {
            Intent intent = new Intent(context, CarModelActivity.class);
            startActivityForResult(intent, 102);
            ((Activity) context).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);//下往上推出效果
        });
        tvFuelType.setOnClickListener(v -> {
            Intent intent = new Intent(context, FuelTypeActivity.class);
            startActivityForResult(intent, 102);
            ((Activity) context).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);//下往上推出效果
        });
        tvTransmissionType.setOnClickListener(v -> {
            Intent intent = new Intent(context, TransmissionTypeActivity.class);
            startActivityForResult(intent, 102);
            ((Activity) context).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);//下往上推出效果
        });

        btnAdd.setIndeterminateProgressMode(true);
        btnAdd.setOnClickListener(v -> {
            if (TextUtils.isEmpty(tvAutomobileBrandName.getText().toString())) {
                showTipsDialog("请选择汽车品牌", TipDialog.TYPE_ERROR);
                return;
            }
            if (TextUtils.isEmpty(tvModelName.getText().toString())) {
                showTipsDialog("请选择品牌型号", TipDialog.TYPE_ERROR);
                return;
            }
            if (TextUtils.isEmpty(tvFuelType.getText().toString())) {
                showTipsDialog("请选择燃油类型", TipDialog.TYPE_ERROR);
                return;
            }
            if (TextUtils.isEmpty(tvTransmissionType.getText().toString())) {
                showTipsDialog("请选择变速箱类型", TipDialog.TYPE_ERROR);
                return;
            }
            if (TextUtils.isEmpty(tvCurrentMileage.getText().toString())) {
                showTipsDialog("请输入当前里程", TipDialog.TYPE_ERROR);
                return;
            }
            if (TextUtils.isEmpty(tvCurrentMileage.getText().toString())) {
                showTipsDialog("请选择蓝牙设备", TipDialog.TYPE_ERROR);
                return;
            }
            if (btnAdd.getProgress() == -1) {
                btnAdd.setProgress(0);
            }
            getVehiclePageList(getUserId(), automobileBrandId, tvAutomobileBrandName.getText().toString(),
                    modelId, tvModelName.getText().toString(), fuelType, transmissionType, tvCurrentMileage.getText().toString(),
                    tvBluetoothDeviceNumber.getText().toString(), getToken());
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
     * @param msg  提示信息
     * @param type 提示类型
     *             提示
     */
    private void showTipsDialog(String msg, int type) {
        TipDialog.show(context, msg, TipDialog.SHOW_TIME_SHORT, type);
    }

    /**
     * @param appUserId             APP用户ID
     * @param automobileBrandId     品牌名称ID
     * @param automobileBrandName   品牌名称
     * @param modelId               型号ID
     * @param modelName             型号名称
     * @param fuelType              燃油类型,字典值
     * @param transmissionType      变速箱类型,字典值
     * @param currentMileage        当前里程(公里)
     * @param bluetoothDeviceNumber 蓝牙设备号
     * @param token                 用户Token
     *                              <p>
     *                              添加车辆信息
     */
    private void getVehiclePageList(String appUserId, String automobileBrandId, String automobileBrandName, String modelId,
                                    String modelName, String fuelType, String transmissionType, String currentMileage,
                                    String bluetoothDeviceNumber, String token) {
        btnAdd.setProgress(0);
        new Handler().postDelayed(() -> btnAdd.setProgress(50), 1000);
        OkHttpUtils.post().
                url(SERVER_URL + ADD_VEHICLE_URL).
                addParam("token", token).
                addParam("appUserId", appUserId).
                addParam("automobileBrandId", automobileBrandId).
                addParam("automobileBrandName", automobileBrandName).
                addParam("modelId", modelId).
                addParam("modelName", modelName).
                addParam("fuelType", fuelType).
                addParam("transmissionType", transmissionType).
                addParam("currentMileage", currentMileage).
                addParam("bluetoothDeviceNumber", bluetoothDeviceNumber).
                build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
                btnAdd.setProgress(-1);
                showTipsDialog(validateError(e, response), TipDialog.TYPE_ERROR);
            }

            @Override
            public void onResponse(String response, int id) {
                LogE("添加车辆信息:" + response);
                ResultEntity entity = JSON.parseObject(response, ResultEntity.class);
                if (entity.isSuccess()) {
                    btnAdd.setProgress(100);
                    new CustomeDialog(context, "车辆信息添加成功！", confirm -> {
                        if (confirm) {
                            setResult(10, new Intent());
                            finish();
                        }
                    }).setPositiveButton("知道了").setTitle("添加车辆").show();
                } else {
                    btnAdd.setProgress(-1);
                    showTipsDialog(entity.getMessage(), TipDialog.TYPE_ERROR);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 102) {
            if (resultCode == 101) {
                BrandPinYinEntity entity = (BrandPinYinEntity) data.getSerializableExtra("automobileBrand");
                automobileBrandId = String.valueOf(entity.getAutomobileBrandId());
                automobileBrandName = entity.getName();
                tvAutomobileBrandName.setText(automobileBrandName);
            }
            if (resultCode == 100) {
                CarModelEntity.DataEntity entity = (CarModelEntity.DataEntity) data.getSerializableExtra("model");
                tvModelName.setText(entity.getName());
                modelId = String.valueOf(entity.getCarModelId());
                modelName = entity.getName();
            }
            if (resultCode == 99) {
                VocationalDictDataListEntity.DataEntity entity = (VocationalDictDataListEntity.DataEntity) data.getSerializableExtra("fuelType");
                fuelType = String.valueOf(entity.getTypeId());
                tvFuelType.setText(entity.getValue());
            }
            if (resultCode == 98) {
                VocationalDictDataListEntity.DataEntity entity = (VocationalDictDataListEntity.DataEntity) data.getSerializableExtra("transmissionType");
                transmissionType = String.valueOf(entity.getTypeId());
                tvTransmissionType.setText(entity.getValue());
            }
        }
    }
}