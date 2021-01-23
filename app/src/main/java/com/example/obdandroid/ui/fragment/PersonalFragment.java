package com.example.obdandroid.ui.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseFragment;
import com.example.obdandroid.config.Constant;
import com.example.obdandroid.ui.activity.FeedbackActivity;
import com.example.obdandroid.ui.activity.LoginActivity;
import com.example.obdandroid.ui.activity.MyVehicleActivity;
import com.example.obdandroid.ui.activity.RechargeRecordActivity;
import com.example.obdandroid.ui.activity.RechargeSetMealActivity;
import com.example.obdandroid.ui.entity.UserInfoEntity;
import com.example.obdandroid.ui.entity.VehicleInfoEntity;
import com.example.obdandroid.ui.view.CircleImageView;
import com.example.obdandroid.ui.view.IosDialog;
import com.example.obdandroid.utils.ActivityManager;
import com.example.obdandroid.utils.JumpUtil;
import com.example.obdandroid.utils.SPUtil;
import com.hjq.bar.TitleBar;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;
import okhttp3.Response;

import static com.example.obdandroid.config.APIConfig.SERVER_URL;
import static com.example.obdandroid.config.APIConfig.USER_INFO_URL;
import static com.example.obdandroid.config.APIConfig.getVehicleInfoById_URL;
import static com.example.obdandroid.config.Constant.CONNECT_BT_KEY;

/**
 * 作者：Jealous
 * 日期：2020/12/23 0023
 * 描述：
 */
public class PersonalFragment extends BaseFragment {
    private Context context;
    private ImageView ivCarLogo;
    private TextView tvAutomobileBrandName;
    private TextView tvOBDState;
    private TextView tvModelName;
    private LinearLayout llHistoryRecord;
    private LinearLayout llBuyHistory;
    private LinearLayout llFaceBack;
    private LinearLayout llVersion;
    private LinearLayout llHelp;
    private LinearLayout llAbout;
    private LinearLayout layoutAddCar;
    private Button btnLogout;
    private SPUtil spUtil;
    private TextView tvName;
    private TextView tvIntegral;
    private TitleBar titleBarSet;
    private CircleImageView myHeaderImage;
    private LinearLayout layoutCar;
    private LocalBroadcastManager lm;
    private TestReceiver testReceiver;
    private LinearLayout layoutGo;

    public static PersonalFragment getInstance() {
        return new PersonalFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_person_center;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {
        context = getHoldingActivity();

        ivCarLogo = getView(R.id.ivCarLogo);
        tvAutomobileBrandName = getView(R.id.tvAutomobileBrandName);
        tvOBDState = getView(R.id.tvOBDState);
        tvModelName = getView(R.id.tvModelName);
        llHistoryRecord = getView(R.id.ll_history_record);
        llBuyHistory = getView(R.id.ll_buy_history);
        llFaceBack = getView(R.id.ll_faceBack);
        llVersion = getView(R.id.ll_Version);
        llHelp = getView(R.id.ll_help);
        llAbout = getView(R.id.ll_about);
        btnLogout = getView(R.id.btnLogout);
        tvName = getView(R.id.tvName);
        tvIntegral = getView(R.id.tvIntegral);
        titleBarSet = getView(R.id.titleBarSet);
        myHeaderImage = getView(R.id.my_header_image);
        layoutCar = getView(R.id.layoutCar);
        layoutAddCar = getView(R.id.layoutAddCar);
        layoutGo = getView(R.id.layoutGo);
        spUtil = new SPUtil(context);
        initReceiver();
        getUserInfo(getUserId(), getToken());
        getVehicleInfoById(getToken(), spUtil.getString("vehicleId", ""));

        layoutGo.setOnClickListener(v -> JumpUtil.startAct(context, RechargeSetMealActivity.class));
        llBuyHistory.setOnClickListener(v -> JumpUtil.startAct(context, RechargeRecordActivity.class));
        llFaceBack.setOnClickListener(v -> JumpUtil.startAct(context, FeedbackActivity.class));
        layoutAddCar.setOnClickListener(v -> JumpUtil.startAct(context, MyVehicleActivity.class));
        //退出账户
        btnLogout.setOnClickListener(v ->
                new IosDialog(context, new IosDialog.DialogClick() {
                    @Override
                    public void Confirm(AlertDialog exitDialog, boolean confirm) {
                        if (confirm) {
                            spUtil.put(CONNECT_BT_KEY, "OFF");
                            spUtil.put(Constant.IS_LOGIN, false);
                            JumpUtil.startAct(context, LoginActivity.class);
                            try {
                                ActivityManager.getInstance().finishActivitys();
                            } catch (Exception e) {
                                LogE("该服务未注册");
                            }
                            exitDialog.dismiss();
                        }
                    }

                    @Override
                    public void Cancel(AlertDialog exitDialog, boolean confirm) {
                        if (confirm) {
                            exitDialog.dismiss();
                        }
                    }
                }).setMessage("是否退出客户端").setTitle("退出提示").setSelectNegative("取消").setSelectPositive("确定").showDialog());
/* multipleItemQuickAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.ll_my_car:

                    break;
                case R.id.ll_my_pay:

                    break;
                case R.id.ll_my_obd:
                    JumpUtil.startAct(context, OBDSettingActivity.class);
                    break;
                case R.id.ll_my_trouble:
                    JumpUtil.startAct(context, TroubleCodeQueryActivity.class);
                    break;
                case R.id.my_header_settings:
                    JumpUtil.startAct(context, AppSettingActivity.class);
                    break;
            }*/
    }

    /**
     * @param userId 用户id
     * @param token  接口令牌
     *               用户信息
     */
    private void getUserInfo(String userId, String token) {
        OkHttpUtils.get().url(SERVER_URL + USER_INFO_URL).
                addParam("userId", userId).
                addParam("token", token).
                build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                UserInfoEntity entity = JSON.parseObject(response, UserInfoEntity.class);
                if (entity.isSuccess()) {
                    tvName.setText(entity.getData().getNickname());
                    //myHeaderMobile.setText(entity.getData().getPhoneNum());
                    if (entity.getData().getHeadPortrait().length() > 0) {
                        myHeaderImage.setImageBitmap(stringToBitmap(entity.getData().getHeadPortrait()));
                    }
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
        OkHttpUtils.get().url(SERVER_URL + getVehicleInfoById_URL).
                addParam("token", token).
                addParam("vehicleId", vehicleId).
                build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response, int id) {
                VehicleInfoEntity entity = JSON.parseObject(response, VehicleInfoEntity.class);
                if (entity.isSuccess()) {
                    tvAutomobileBrandName.setText(entity.getData().getAutomobileBrandName());
                    tvModelName.setText(TextUtils.isEmpty(entity.getData().getModelName()) ? "" : entity.getData().getModelName());
                    if (!TextUtils.isEmpty(entity.getData().getLogo())) {
                        Glide.with(context).load(SERVER_URL+entity.getData().getLogo()).into(ivCarLogo);
                        //ivCarLogo.setImageBitmap(BitMapUtils.stringToBitmap(entity.getData().getLogo()));
                    }
                    if (entity.getData().getVehicleStatus() == 1) {//车辆状态 1 未绑定 2 已绑定 ,
                        tvOBDState.setText("  OBD 未绑定");
                        Drawable drawable = context.getResources().getDrawable(R.drawable.icon_no);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        tvOBDState.setCompoundDrawables(drawable, null, null, null);
                    } else {
                        tvOBDState.setText("  OBD 已绑定");
                        Drawable drawable = context.getResources().getDrawable(R.drawable.icon_ok);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        tvOBDState.setCompoundDrawables(drawable, null, null, null);
                    }
                }
            }
        });
    }

    private void initReceiver() {
        //获取实例
        lm = LocalBroadcastManager.getInstance(context);
        IntentFilter intentFilter = new IntentFilter("com.android.ObdCar");
        testReceiver = new TestReceiver();
        //绑定
        lm.registerReceiver(testReceiver, intentFilter);

    }

    private class TestReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String vehicleId = intent.getStringExtra(Intent.EXTRA_TEXT);
            getVehicleInfoById(getToken(), vehicleId);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        lm.unregisterReceiver(testReceiver);
    }

    /**
     * @param base64Data Base64图片
     * @return Base64转换图片
     */
    public static Bitmap stringToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}