package com.example.obdandroid.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseActivity;
import com.example.obdandroid.config.Constant;
import com.example.obdandroid.ui.entity.VehicleInfoEntity;
import com.example.obdandroid.ui.view.CircleImageView;
import com.example.obdandroid.utils.BitMapUtils;
import com.example.obdandroid.utils.DialogUtils;
import com.example.obdandroid.utils.JumpUtil;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;
import okhttp3.Response;

import static com.example.obdandroid.config.APIConfig.SERVER_URL;
import static com.example.obdandroid.config.APIConfig.getVehicleInfoById_URL;

/**
 * 作者：Jealous
 * 日期：2021/1/13 0013
 * 描述：
 */
public class VehicleInfoActivity extends BaseActivity {
    private Context context;
    private CircleImageView ivBind;
    private ImageView ivLogo;
    private TextView tvAutomobileBrandName;
    private TextView tvModelName;
    private TextView tvFuelType;
    private TextView tvTransmissionType;
    private TextView tvCurrentMileage;
    private TextView tvVehicleStatusName;
    private TextView tvEngineDisplacement;
    private TextView tvEngineNumber;
    private TextView tvFuelCost;
    private TextView tvGrossVehicleWeight;
    private TextView tvLastMaintenanceDate;
    private TextView tvLastMaintenanceMileage;
    private TextView tvLicensePlateNumber;
    private TextView tvMaintenanceInterval;
    private TextView tvMaintenanceMileageInterval;
    private TextView tvTankCapacity;
    private TextView tvVehiclePurchaseDate;
    private TextView tvYearManufacture;
    private DialogUtils dialogUtils;
    private String vehicleId;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_vehicle_info;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    public void initView() {
        super.initView();
        context = this;
        vehicleId = getIntent().getStringExtra(Constant.ACT_FLAG);
        TitleBar titleBarSet = findViewById(R.id.titleBarSet);
        ivBind = findViewById(R.id.ivBind);
        ivLogo = findViewById(R.id.ivLogo);
        tvAutomobileBrandName = findViewById(R.id.tvAutomobileBrandName);
        tvModelName = findViewById(R.id.tvModelName);
        tvFuelType = findViewById(R.id.tvFuelType);
        tvTransmissionType = findViewById(R.id.tvTransmissionType);
        tvCurrentMileage = findViewById(R.id.tvCurrentMileage);
        tvVehicleStatusName = findViewById(R.id.tvVehicleStatusName);
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
        titleBarSet.setRightTitle("修改");
        dialogUtils = new DialogUtils(context);
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
                Intent intent = new Intent(context, ModifyVehicleActivity.class);
                intent.putExtra("vehicleId", vehicleId);
                startActivity(intent);
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
                LogE("获取用户车辆详情:"+response);
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
        if (entity.getVehicleStatus() == 1) {
            ivBind.setVisibility(View.VISIBLE);
            tvVehicleStatusName.setTextColor(getResources().getColor(R.color.red));
        } else {
            ivBind.setVisibility(View.GONE);
            tvVehicleStatusName.setTextColor(getResources().getColor(R.color.cpb_blue_dark));
        }
        if (TextUtils.isEmpty(entity.getLogo())) {
            ivLogo.setImageResource(R.drawable.icon_car_def);
        } else {
            ivLogo.setImageBitmap(BitMapUtils.stringToBitmap(entity.getLogo()));
        }
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
        ivBind.setOnClickListener(v -> {
            Intent intent = new Intent(context, BindBluetoothDeviceActivity.class);
            intent.putExtra("vehicleId", String.valueOf(entity.getVehicleId()));
            startActivityForResult(intent, 100);
        });
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
        if (requestCode == 100) {
            if (resultCode == 101) {
                getVehicleInfoById(getToken(), vehicleId);
            }
        }
    }
}