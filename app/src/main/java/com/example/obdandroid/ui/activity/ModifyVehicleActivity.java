package com.example.obdandroid.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseActivity;
import com.example.obdandroid.ui.entity.BluetoothDeviceEntity;
import com.example.obdandroid.ui.entity.BrandPinYinEntity;
import com.example.obdandroid.ui.entity.CarModelEntity;
import com.example.obdandroid.ui.entity.ResultEntity;
import com.example.obdandroid.ui.entity.VehicleInfoEntity;
import com.example.obdandroid.ui.entity.VocationalDictDataListEntity;
import com.example.obdandroid.ui.view.CustomeDateDialog;
import com.example.obdandroid.ui.view.CustomeDialog;
import com.example.obdandroid.utils.DialogUtils;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.kongzue.dialog.v2.TipDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Response;

import static com.example.obdandroid.config.APIConfig.SERVER_URL;
import static com.example.obdandroid.config.APIConfig.getVehicleInfoById_URL;
import static com.example.obdandroid.config.APIConfig.modifyVehicle_URL;

/**
 * 作者：Jealous
 * 日期：2021/1/13 0013
 * 描述：
 */
public class ModifyVehicleActivity extends BaseActivity {
    private Context context;
    private TitleBar titleBarSet;
    private TextView tvAutomobileBrandName;
    private TextView tvModelName;
    private TextView tvFuelType;
    private TextView tvTransmissionType;
    private EditText tvCurrentMileage;
    private TextView tvVehicleStatusName;
    private TextView tvBluetoothDeviceNumber;
    private EditText tvEngineDisplacement;
    private EditText tvEngineNumber;
    private EditText tvFuelCost;
    private EditText tvGrossVehicleWeight;
    private TextView tvLastMaintenanceDate;
    private EditText tvLastMaintenanceMileage;
    private EditText tvLicensePlateNumber;
    private EditText tvMaintenanceInterval;
    private EditText tvMaintenanceMileageInterval;
    private EditText tvTankCapacity;
    private TextView tvVehiclePurchaseDate;
    private EditText tvYearManufacture;
    private DialogUtils dialogUtils;
    private String vehicleId="";
    private String automobileBrandId="";
    private String modelId="";
    private String fuelType="";
    private String transmissionType="";
    private String bluetoothDeviceNumber="";

    @Override
    protected int getContentViewId() {
        return R.layout.activity_modify_vehicle;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    public void initView() {
        super.initView();
        context = this;
        vehicleId = getIntent().getStringExtra("vehicleId");
        titleBarSet = findViewById(R.id.titleBarSet);
        tvAutomobileBrandName = findViewById(R.id.tvAutomobileBrandName);
        tvModelName = findViewById(R.id.tvModelName);
        tvFuelType = findViewById(R.id.tvFuelType);
        tvTransmissionType = findViewById(R.id.tvTransmissionType);
        tvCurrentMileage = findViewById(R.id.tvCurrentMileage);
        tvVehicleStatusName = findViewById(R.id.tvVehicleStatusName);
        tvBluetoothDeviceNumber = findViewById(R.id.tvBluetoothDeviceNumber);
        tvEngineDisplacement = findViewById(R.id.tvEngineDisplacement);
        tvEngineNumber = findViewById(R.id.tvEngineNumber);
        tvFuelCost = findViewById(R.id.tvFuelCost);
        tvGrossVehicleWeight = findViewById(R.id.tvGrossVehicleWeight);
        tvLastMaintenanceDate = findViewById(R.id.tvLastMaintenanceDate);
        tvLastMaintenanceMileage = findViewById(R.id.tvLastMaintenanceMileage);
        tvLicensePlateNumber = findViewById(R.id.tvLicensePlateNumber);
        tvMaintenanceInterval = findViewById(R.id.tvMaintenanceInterval);
        tvMaintenanceMileageInterval = findViewById(R.id.tvMaintenanceMileageInterval);
        tvTankCapacity = findViewById(R.id.tvTankCapacity);
        tvVehiclePurchaseDate = findViewById(R.id.tvVehiclePurchaseDate);
        tvYearManufacture = findViewById(R.id.tvYearManufacture);
        titleBarSet.setRightTitle("确定");
        dialogUtils = new DialogUtils(context);
        //选择汽车品牌
        tvAutomobileBrandName.setOnClickListener(v -> {
            Intent intent = new Intent(context, AutomobileBrandActivity.class);
            startActivityForResult(intent, 102);
            ((Activity) context).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);//下往上推出效果
        });
        //选择型号
        tvModelName.setOnClickListener(v -> {
            if (TextUtils.isEmpty(automobileBrandId)) {
                showTipsDialog("请选择汽车品牌", TipDialog.TYPE_WARNING);
                return;
            }
            Intent intent = new Intent(context, CarModelActivity.class);
            intent.putExtra("automobileBrandId", automobileBrandId);
            startActivityForResult(intent, 102);
            ((Activity) context).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);//下往上推出效果
        });
        //选择燃油类型
        tvFuelType.setOnClickListener(v -> {
            Intent intent = new Intent(context, FuelTypeActivity.class);
            startActivityForResult(intent, 102);
            ((Activity) context).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);//下往上推出效果
        });
        //选择变速箱类型
        tvTransmissionType.setOnClickListener(v -> {
            Intent intent = new Intent(context, TransmissionTypeActivity.class);
            startActivityForResult(intent, 102);
            ((Activity) context).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);//下往上推出效果
        });
        //选择绑定的蓝牙设备
        tvBluetoothDeviceNumber.setOnClickListener(v -> {
            Intent intent = new Intent(context, BluetoothDeviceActivity.class);
            startActivityForResult(intent, 102);
            ((Activity) context).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);//下往上推出效果
        });
        setDate(tvLastMaintenanceDate);
        setDate(tvVehiclePurchaseDate);
        getVehicleInfoById(getToken(), vehicleId);
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

                modifyVehicle(vehicleId, getUserId(), automobileBrandId, tvAutomobileBrandName.getText().toString(), modelId,
                        tvModelName.getText().toString(), fuelType, transmissionType, tvCurrentMileage.getText().toString(),
                        bluetoothDeviceNumber, tvVehiclePurchaseDate.getText().toString(),
                        tvYearManufacture.getText().toString(), tvEngineDisplacement.getText().toString(),
                        tvGrossVehicleWeight.getText().toString(), tvTankCapacity.getText().toString(),
                        tvFuelCost.getText().toString(), tvLicensePlateNumber.getText().toString(),
                        tvEngineNumber.getText().toString(), tvLastMaintenanceDate.getText().toString(),
                        tvLastMaintenanceMileage.getText().toString(), tvMaintenanceInterval.getText().toString(),
                        tvMaintenanceMileageInterval.getText().toString(), getToken());
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
     * 设置时间
     */
    private void setDate(TextView tvDate) {
        tvDate.setOnClickListener(new View.OnClickListener() {
            final Calendar c = Calendar.getInstance();

            @Override
            public void onClick(View v) {
                new CustomeDateDialog(context, 0, (startDatePicker, startYear, startMonthOfYear, startDayOfMonth) -> {
                    String textString = startYear + "-" + startMonthOfYear + "-" + startDayOfMonth;
                    tvDate.setText(textString);
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), false).show();
            }
        });
    }

    /**
     * @param id                         车辆ID
     * @param appUserId                  APP用户ID
     * @param automobileBrandId          品牌名称ID
     * @param automobileBrandName        品牌名称
     * @param modelId                    型号ID
     * @param modelName                  型号名称
     * @param fuelType                   燃油类型,字典值
     * @param transmissionType           变速箱类型,字典值
     * @param currentMileage             当前里程(公里)
     * @param bluetoothDeviceNumber      蓝牙设备号
     * @param vehiclePurchaseDate        车辆购置日期
     * @param yearManufacture            出厂年份
     * @param engineDisplacement         引擎排气量/排量（升）
     * @param grossVehicleWeight         汽车总重(KG)
     * @param tankCapacity               油箱容量(升)
     * @param fualCost                   油价
     * @param licensePlateNumber         车牌号
     * @param engineNumber               发动机号
     * @param lastMaintenanceDate        上次保养日期
     * @param lastMaintenanceMileage     上次保养里程(公里)
     * @param maintenanceInterval        保养时间间隔（月）
     * @param maintenanceMileageInterval 保养里程间隔（公里）
     * @param token                      用户Token
     *                                   修改车辆信息
     */
    private void modifyVehicle(String id, String appUserId, String automobileBrandId,
                               String automobileBrandName, String modelId, String modelName,
                               String fuelType, String transmissionType, String currentMileage,
                               String bluetoothDeviceNumber, String vehiclePurchaseDate, String yearManufacture,
                               String engineDisplacement, String grossVehicleWeight, String tankCapacity,
                               String fualCost, String licensePlateNumber, String engineNumber,
                               String lastMaintenanceDate, String lastMaintenanceMileage, String maintenanceInterval,
                               String maintenanceMileageInterval, String token) {
        OkHttpUtils.post().url(SERVER_URL + modifyVehicle_URL).
                addParam("id", id).
                addParam("appUserId", appUserId).
                addParam("automobileBrandId", automobileBrandId).
                addParam("automobileBrandName", automobileBrandName).
                addParam("modelId", modelId).
                addParam("modelName", modelName).
                addParam("fuelType", fuelType).
                addParam("transmissionType", transmissionType).
                addParam("currentMileage", currentMileage).
                addParam("bluetoothDeviceNumber", bluetoothDeviceNumber).
                addParam("vehiclePurchaseDate", vehiclePurchaseDate).
                addParam("yearManufacture", yearManufacture).
                addParam("engineDisplacement", engineDisplacement).
                addParam("grossVehicleWeight", grossVehicleWeight).
                addParam("tankCapacity", tankCapacity).
                addParam("fualCost", fualCost).
                addParam("licensePlateNumber", licensePlateNumber).
                addParam("engineNumber", engineNumber).
                addParam("lastMaintenanceDate", lastMaintenanceDate).
                addParam("lastMaintenanceMileage", lastMaintenanceMileage).
                addParam("maintenanceInterval", maintenanceInterval).
                addParam("maintenanceMileageInterval", maintenanceMileageInterval).
                addParam("token", token).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                ResultEntity entity = JSON.parseObject(response, ResultEntity.class);
                if (entity.isSuccess()) {
                    new CustomeDialog(context, "完善成功！", confirm -> {
                        if (confirm) {

                        }
                    }).setPositiveButton("确定").setTitle("提示").show();
                } else {
                    dialogError(context, entity.getMessage());
                }
            }
        });

    }

    /**
     * @param token     用户Token
     * @param vehicleId 车辆ID
     *                  获取用户车辆详情
     */
    private void getVehicleInfoById(String token, String vehicleId) {
        dialogUtils.showProgressDialog();
        OkHttpUtils.get().url(SERVER_URL + getVehicleInfoById_URL).
                addParam("token", token).
                addParam("vehicleId", vehicleId).
                build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                VehicleInfoEntity entity = JSON.parseObject(response, VehicleInfoEntity.class);
                if (entity.isSuccess()) {
                    dialogUtils.dismiss();
                    setViewData(entity.getData());
                } else {
                    dialogUtils.dismiss();
                    dialogError(context, entity.getMessage());
                }
            }
        });
    }

    private void setViewData(VehicleInfoEntity.DataEntity entity) {
        automobileBrandId = String.valueOf(entity.getAutomobileBrandId());
        modelId = String.valueOf(entity.getModelId());
        fuelType = String.valueOf(entity.getFuelType());
        transmissionType = String.valueOf(entity.getTransmissionType());
        bluetoothDeviceNumber = checkNull(entity.getBluetoothDeviceNumber());
        tvAutomobileBrandName.setText(checkNull(entity.getAutomobileBrandName()));
        tvModelName.setText(checkNull(entity.getModelName()));
        tvFuelType.setText(checkNull(entity.getFuelTypeName()));
        tvTransmissionType.setText(checkNull(entity.getTransmissionTypeName()));
        tvCurrentMileage.setText(String.valueOf(entity.getCurrentMileage()));
        tvVehicleStatusName.setText(checkNull(entity.getVehicleStatusName()));
        tvEngineDisplacement.setText(checkNull(entity.getEngineDisplacement()));
        tvEngineNumber.setText(checkNull(entity.getEngineNumber()));
        tvFuelCost.setText(checkNull(entity.getFualCost()));
        tvGrossVehicleWeight.setText(checkNull(entity.getGrossVehicleWeight()));
        tvLastMaintenanceDate.setText(checkNull(entity.getLastMaintenanceDate()));
        tvLastMaintenanceMileage.setText(checkNull(entity.getLastMaintenanceMileage()));
        tvLicensePlateNumber.setText(checkNull(entity.getLicensePlateNumber()));
        tvMaintenanceInterval.setText(String.valueOf(entity.getMaintenanceInterval()));
        tvMaintenanceMileageInterval.setText(String.valueOf(entity.getMaintenanceMileageInterval()));
        tvTankCapacity.setText(checkNull(entity.getTankCapacity()));
        tvVehiclePurchaseDate.setText(checkNull(entity.getVehiclePurchaseDate()));
        tvYearManufacture.setText(checkNull(entity.getYearManufacture()));
        /*ivBind.setOnClickListener(v -> {
            Intent intent = new Intent(context, BindBluetoothDeviceActivity.class);
            intent.putExtra("vehicleId", String.valueOf(entity.getVehicleId()));
            startActivityForResult(intent, 100);
        });*/
    }

    /**
     * @param content 内容
     * @return 检查数据
     */
    private String checkNull(String content) {
        return TextUtils.isEmpty(content) ? "" : content;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 102) {
            if (resultCode == 101) {
                BrandPinYinEntity entity = null;
                if (data != null) {
                    entity = (BrandPinYinEntity) data.getSerializableExtra("automobileBrand");
                }
                if (entity != null) {
                    automobileBrandId = String.valueOf(entity.getAutomobileBrandId());
                }
                String automobileBrandName = entity.getName();
                tvAutomobileBrandName.setText(automobileBrandName);
            }
            if (resultCode == 100) {
                CarModelEntity.DataEntity entity = null;
                if (data != null) {
                    entity = (CarModelEntity.DataEntity) data.getSerializableExtra("model");
                }
                tvModelName.setText(entity != null ? entity.getName() : null);
                modelId = String.valueOf(entity != null ? entity.getCarModelId() : 0);
            }
            if (resultCode == 99) {
                VocationalDictDataListEntity.DataEntity entity = null;
                if (data != null) {
                    entity = (VocationalDictDataListEntity.DataEntity) data.getSerializableExtra("fuelType");
                }
                if (entity != null) {
                    fuelType = String.valueOf(entity.getVocationalDictDataId());
                }
                tvFuelType.setText(entity.getValue());
            }
            if (resultCode == 98) {
                VocationalDictDataListEntity.DataEntity entity = null;
                if (data != null) {
                    entity = (VocationalDictDataListEntity.DataEntity) data.getSerializableExtra("transmissionType");
                }
                transmissionType = String.valueOf(entity != null ? entity.getVocationalDictDataId() : 0);
                tvTransmissionType.setText(entity.getValue());
            }
            if (resultCode == 97) {
                BluetoothDeviceEntity entity = null;
                if (data != null) {
                    entity = (BluetoothDeviceEntity) data.getSerializableExtra("bluetoothDeviceNumber");
                }
                bluetoothDeviceNumber = entity != null ? entity.getBlue_address() : "";
                tvBluetoothDeviceNumber.setText(entity != null ? entity.getBlue_name() : "");
            }
        }
    }


}