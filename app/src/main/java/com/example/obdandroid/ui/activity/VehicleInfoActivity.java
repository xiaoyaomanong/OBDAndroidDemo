package com.example.obdandroid.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseActivity;
import com.example.obdandroid.config.Constant;
import com.example.obdandroid.ui.entity.VehicleInfoEntity;
import com.example.obdandroid.ui.view.CircleImageView;
import com.example.obdandroid.utils.DialogUtils;
import com.example.obdandroid.utils.StringUtil;
import com.github.ielse.imagewatcher.ImageWatcherHelper;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.net.URL;

import io.reactivex.disposables.Disposable;
import okhttp3.Call;
import okhttp3.Response;

import static com.example.obdandroid.config.APIConfig.SERVER_URL;
import static com.example.obdandroid.config.APIConfig.getVehicleInfoById_URL;
import static com.example.obdandroid.config.Constant.VEHICLE_ID;

/**
 * 作者：Jealous
 * 日期：2021/1/13 0013
 * 描述：
 */
public class VehicleInfoActivity extends BaseActivity {
    private Context context;
    private DialogUtils dialogUtils;
    private String vehicleId;
    private CircleImageView ivLogo;
    private TextView tvAutomobileBrandName;
    private TextView tvModelName;
    private TextView tvObd;
    private TextView tvHomeObdTip;
    private TextView tvFuelType;
    private TextView tvTransmissionType;
    private TextView tvCurrentMileage;
    private TextView tvVehiclePurchaseDate;
    private TextView tvEngineNumber;
    private TextView tvLicensePlateNumber;
    private TextView tvEngineDisplacement;
    private TextView tvFuelCost;
    private TextView tvGrossVehicleWeight;
    private TextView tvLastMaintenanceDate;
    private TextView tvLastMaintenanceMileage;
    private TextView tvMaintenanceInterval;
    private TextView tvMaintenanceMileageInterval;
    private TextView tvTankCapacity;
    private TextView tvYearManufacture;
    private TextView tvInterfaceDesc;
    private LinearLayout layoutBind;
    private boolean isRefresh = false;
    private final LevelListDrawable mDrawable = new LevelListDrawable();
    // 注意啦，这么写Handler是会造成内存泄漏的，实际项目中不要这么直接用。
    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1123) { // 使用1123仅仅是因为在11月23号写的
                Bitmap bitmap = (Bitmap) msg.obj;
                BitmapDrawable drawable = new BitmapDrawable(null, bitmap);
                mDrawable.addLevel(1, 1, drawable);
                mDrawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
                mDrawable.setLevel(1);
                CharSequence charSequence = tvInterfaceDesc.getText();
                tvInterfaceDesc.setText(charSequence);
                tvInterfaceDesc.invalidate();
            }
        }
    };


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
        ivLogo = findViewById(R.id.ivLogo);
        tvAutomobileBrandName = findViewById(R.id.tvAutomobileBrandName);
        tvModelName = findViewById(R.id.tvModelName);
        tvObd = findViewById(R.id.tv_obd);
        tvHomeObdTip = findViewById(R.id.tv_home_obd_tip);
        tvFuelType = findViewById(R.id.tvFuelType);
        tvTransmissionType = findViewById(R.id.tvTransmissionType);
        tvCurrentMileage = findViewById(R.id.tvCurrentMileage);
        tvVehiclePurchaseDate = findViewById(R.id.tvVehiclePurchaseDate);
        tvEngineNumber = findViewById(R.id.tvEngineNumber);
        tvLicensePlateNumber = findViewById(R.id.tvLicensePlateNumber);
        tvEngineDisplacement = findViewById(R.id.tvEngineDisplacement);
        tvFuelCost = findViewById(R.id.tvFuelCost);
        tvGrossVehicleWeight = findViewById(R.id.tvGrossVehicleWeight);
        tvLastMaintenanceDate = findViewById(R.id.tvLastMaintenanceDate);
        tvLastMaintenanceMileage = findViewById(R.id.tvLastMaintenanceMileage);
        tvMaintenanceInterval = findViewById(R.id.tvMaintenanceInterval);
        tvMaintenanceMileageInterval = findViewById(R.id.tvMaintenanceMileageInterval);
        tvTankCapacity = findViewById(R.id.tvTankCapacity);
        tvYearManufacture = findViewById(R.id.tvYearManufacture);
        layoutBind = findViewById(R.id.layoutBind);
        tvInterfaceDesc = findViewById(R.id.tvInterfaceDesc);
        titleBarSet.setRightTitle("修改");
        dialogUtils = new DialogUtils(context);
        getVehicleInfoById(getToken(), vehicleId);
        titleBarSet.setOnTitleBarListener(new OnTitleBarListener() {
            @Override
            public void onLeftClick(View v) {
                if (isRefresh) {
                    setResult(12, new Intent());
                }
                finish();
            }

            @Override
            public void onTitleClick(View v) {

            }

            @Override
            public void onRightClick(View v) {
                Intent intent = new Intent(context, ModifyVehicleActivity.class);
                intent.putExtra(VEHICLE_ID, vehicleId);
                startActivityForResult(intent, 100);
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

            @RequiresApi(api = Build.VERSION_CODES.N)
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    private void setViewData(VehicleInfoEntity.DataEntity entity) {
        if (!StringUtil.isNull(entity.getInterfaceDesc())) {
            LogE("接口介绍：" + entity.getInterfaceDesc());
            tvInterfaceDesc.setText(Html.fromHtml(entity.getInterfaceDesc(), Html.FROM_HTML_MODE_COMPACT, source -> {
                new Thread(() -> {
                    mDrawable.addLevel(0, 0, getResources().getDrawable(R.drawable.ic_launcher));
                    mDrawable.setBounds(0, 0, 200, 200);

                    Bitmap bitmap;
                    try {
                        bitmap = BitmapFactory.decodeStream(new URL(source).openStream());
                        Message msg = handler.obtainMessage();
                        msg.what = 1123;
                        msg.obj = bitmap;
                        handler.sendMessage(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
                return mDrawable;
            }, null));
        }

        if (StringUtil.isNull(entity.getBluetoothDeviceNumber())) {//车辆状态 1 未绑定 2 已绑定 ,
            tvObd.setText("  设备未绑定");
            tvHomeObdTip.setText(" 设备序列号");
            Drawable drawable = context.getResources().getDrawable(R.drawable.icon_no);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tvHomeObdTip.setCompoundDrawables(drawable, null, null, null);
        } else {
            tvObd.setText("  设备已绑定");
            tvHomeObdTip.setText(entity.getBluetoothDeviceNumber());
            Drawable drawable = context.getResources().getDrawable(R.drawable.icon_ok);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tvHomeObdTip.setCompoundDrawables(drawable, null, null, null);
        }

        if (TextUtils.isEmpty(entity.getLogo())) {
            ivLogo.setImageResource(R.drawable.icon_car_def);
        } else {
            Glide.with(context).load(SERVER_URL + entity.getLogo()).into(ivLogo);
        }
        tvAutomobileBrandName.setText(checkNull(entity.getAutomobileBrandName()));
        tvModelName.setText(checkNull(entity.getModelName()));
        tvFuelType.setText(checkNull(entity.getFuelTypeName()));
        tvTransmissionType.setText(checkNull(entity.getTransmissionTypeName()));
        tvCurrentMileage.setText(String.valueOf(entity.getCurrentMileage()));
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
        layoutBind.setOnClickListener(v -> {
            Intent intent = new Intent(context, BindBluetoothDeviceActivity.class);
            intent.putExtra(VEHICLE_ID, String.valueOf(entity.getVehicleId()));
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
                isRefresh = true;
                getVehicleInfoById(getToken(), vehicleId);
            }
        }
    }
}