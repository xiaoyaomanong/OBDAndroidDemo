package com.example.obdandroid.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseActivity;
import com.example.obdandroid.config.Constant;
import com.example.obdandroid.utils.JumpUtil;
import com.example.obdandroid.utils.SPUtil;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.kongzue.dialog.util.InputInfo;
import com.kongzue.dialog.v2.InputDialog;

import static com.example.obdandroid.config.Constant.ENABLE_GPS_KEY;
import static com.example.obdandroid.config.Constant.GPS_DISTANCE_PERIOD_KEY;
import static com.example.obdandroid.config.Constant.GPS_UPDATE_PERIOD_KEY;
import static com.example.obdandroid.config.Constant.IMPERIAL_UNITS_KEY;

/**
 * 作者：Jealous
 * 日期：2020/12/30 0030
 * 描述：
 */
public class OBDSettingActivity extends BaseActivity implements View.OnClickListener {
    private Context context;
    private SPUtil spUtil;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_obd_setting;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    public void initView() {
        super.initView();
        context = this;
        TitleBar titleBarSet = findViewById(R.id.titleBarSet);
        LinearLayout layoutBluetooth = findViewById(R.id.layoutBluetooth);
        AppCompatCheckBox cbGpsCategory = findViewById(R.id.cb_gps_category);
        LinearLayout layoutGpsUpdatePeriod = findViewById(R.id.layout_gps_update_period);
        LinearLayout layoutGpsDistancePeriod = findViewById(R.id.layout_gps_distance_period);
        LinearLayout layoutObdProtocol = findViewById(R.id.layout_obd_protocol);
        AppCompatCheckBox cbImperialUnits = findViewById(R.id.cb_imperial_units);
        LinearLayout layoutCommands = findViewById(R.id.layout_commands);
        TextView tvBtName = findViewById(R.id.tvBtName);
        ImageView ivConnect = findViewById(R.id.ivConnect);
        titleBarSet.setTitle("OBD设置");
        spUtil = new SPUtil(context);
        String BTName = spUtil.getString(Constant.BT_NAME_KEY, "");
        String connectState = spUtil.getString(Constant.CONNECT_BT_KEY, "OFFLINE");
        if (!TextUtils.isEmpty(BTName)) {
            tvBtName.setText(BTName);
        }
        if (!TextUtils.isEmpty(connectState)) {
            if (connectState.equals("ONLINE")) {
                ivConnect.setImageResource(R.drawable.icon_bt_ok);
            } else {
                ivConnect.setImageResource(R.drawable.icon_bt_no);
            }
        }
        layoutBluetooth.setOnClickListener(this);
        layoutCommands.setOnClickListener(this);
        layoutGpsDistancePeriod.setOnClickListener(this);
        layoutGpsUpdatePeriod.setOnClickListener(this);
        layoutObdProtocol.setOnClickListener(this);
        cbImperialUnits.setOnCheckedChangeListener((buttonView, isChecked) -> spUtil.put(IMPERIAL_UNITS_KEY, isChecked));
        cbGpsCategory.setOnCheckedChangeListener((buttonView, isChecked) -> spUtil.put(ENABLE_GPS_KEY, isChecked));

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


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layoutBluetooth:

                break;
            case R.id.layout_commands:
                JumpUtil.startAct(context, ObdCommandActivity.class);
                break;
            case R.id.layout_gps_distance_period:
                InputDialog.show(context, getString(R.string.update_distance), "", (dialog, inputText) -> {
                    if (!TextUtils.isEmpty(inputText)) {
                        spUtil.put(GPS_DISTANCE_PERIOD_KEY, inputText);
                        dialog.dismiss();
                    }
                }).setInputInfo(new InputInfo().setMAX_LENGTH(100).setInputType(InputType.TYPE_CLASS_NUMBER)).
                        setDefaultInputText(spUtil.getString(GPS_DISTANCE_PERIOD_KEY,"5")).
                        setDefaultInputHint(getResources().getString(R.string.pref_gps_distance_period_summary));
                break;
            case R.id.layout_gps_update_period:
                InputDialog.show(context, getString(R.string.update_period), "", (dialog, inputText) -> {
                    if (!TextUtils.isEmpty(inputText)) {
                        spUtil.put(GPS_UPDATE_PERIOD_KEY, inputText);
                        dialog.dismiss();
                    }
                }).setInputInfo(new InputInfo().setMAX_LENGTH(100).setInputType(InputType.TYPE_CLASS_NUMBER)).
                        setDefaultInputText(spUtil.getString(GPS_UPDATE_PERIOD_KEY,"1")).
                        setDefaultInputHint(getResources().getString(R.string.pref_gps_update_period_summary));
                break;
            case R.id.layout_obd_protocol:
                JumpUtil.startAct(context, OBDProtocolActivity.class);
                break;
        }
    }
}