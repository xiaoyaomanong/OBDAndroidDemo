package com.example.obdandroid.ui.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
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
import com.example.obdandroid.ui.activity.AboutActivity;
import com.example.obdandroid.ui.activity.CheckRecordActivity;
import com.example.obdandroid.ui.activity.FeedbackActivity;
import com.example.obdandroid.ui.activity.LoginActivity;
import com.example.obdandroid.ui.activity.MyVehicleActivity;
import com.example.obdandroid.ui.activity.PersonSettingActivity;
import com.example.obdandroid.ui.activity.RechargeRecordActivity;
import com.example.obdandroid.ui.activity.RechargeSetMealActivity;
import com.example.obdandroid.ui.activity.UpdatePwdActivity;
import com.example.obdandroid.ui.activity.VehicleInfoActivity;
import com.example.obdandroid.ui.entity.UserCurrentRechargeEntity;
import com.example.obdandroid.ui.entity.UserInfoEntity;
import com.example.obdandroid.ui.entity.VehicleInfoEntity;
import com.example.obdandroid.ui.view.CircleImageView;
import com.example.obdandroid.ui.view.IosDialog;
import com.example.obdandroid.utils.ActivityManager;
import com.example.obdandroid.utils.BitMapUtils;
import com.example.obdandroid.utils.DialogUtils;
import com.example.obdandroid.utils.JumpUtil;
import com.example.obdandroid.utils.SPUtil;
import com.example.obdandroid.utils.StringUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;
import okhttp3.Response;

import static com.example.obdandroid.config.APIConfig.SERVER_URL;
import static com.example.obdandroid.config.APIConfig.USER_INFO_URL;
import static com.example.obdandroid.config.APIConfig.getTheUserCurrentRecharge_URL;
import static com.example.obdandroid.config.APIConfig.getVehicleInfoById_URL;
import static com.example.obdandroid.config.Constant.OBD_ACTION;
import static com.example.obdandroid.config.Constant.VEHICLE_ID;

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
    private SPUtil spUtil;
    private TextView tvName;
    private TextView tvIntegral;
    private TextView tvRechargeTime;
    private TextView tvRechargeSetMeaName;
    private CircleImageView myHeaderImage;
    private LinearLayout layoutCar;
    private LocalBroadcastManager lm;
    private TestReceiver testReceiver;
    private ImageView ivVip;
    private SwipeRefreshLayout refresh;
    private DialogUtils dialogUtils;

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
        LinearLayout llHistoryRecord = getView(R.id.ll_history_record);
        LinearLayout llBuyHistory = getView(R.id.ll_buy_history);
        LinearLayout llFaceBack = getView(R.id.ll_faceBack);
        LinearLayout llAbout = getView(R.id.ll_about);
        Button btnLogout = getView(R.id.btnLogout);
        tvName = getView(R.id.tvName);
        tvIntegral = getView(R.id.tvIntegral);
        myHeaderImage = getView(R.id.my_header_image);
        layoutCar = getView(R.id.layoutCar);
        LinearLayout layoutAddCar = getView(R.id.layoutAddCar);
        LinearLayout layoutGo = getView(R.id.layoutGo);
        LinearLayout layoutUpdate = getView(R.id.layout_update);
        tvRechargeTime = getView(R.id.tvRechargeTime);
        tvRechargeSetMeaName = getView(R.id.tvRechargeSetMeaName);
        LinearLayout layoutUpdatePwd = getView(R.id.layoutUpdatePwd);
        refresh = getView(R.id.refresh);
        ivVip = getView(R.id.ivVip);
        spUtil = new SPUtil(context);
        dialogUtils=new DialogUtils(context);
        String vehicleId = spUtil.getString(VEHICLE_ID, "");
        initReceiver();
        getUserInfo(getUserId(), getToken());
        getTheUserCurrentRecharge(getUserId(), getToken());
        if (!TextUtils.isEmpty(vehicleId)) {
            getVehicleInfoById(getToken(), vehicleId,String.valueOf(0));
        } else {
            layoutCar.setVisibility(View.GONE);
        }
        layoutGo.setOnClickListener(v -> {
            Intent intent = new Intent(context, RechargeSetMealActivity.class);
            startActivityForResult(intent, 101);
        });//充值
        llBuyHistory.setOnClickListener(v -> JumpUtil.startAct(context, RechargeRecordActivity.class));//购买记录
        llFaceBack.setOnClickListener(v -> JumpUtil.startAct(context, FeedbackActivity.class));//反馈
        layoutAddCar.setOnClickListener(v -> JumpUtil.startAct(context, MyVehicleActivity.class));//车辆管理
        layoutUpdate.setOnClickListener(v -> JumpUtil.startAct(context, PersonSettingActivity.class));//个人信息
        llAbout.setOnClickListener(v -> JumpUtil.startAct(context, AboutActivity.class));//关于我们
        llHistoryRecord.setOnClickListener(v -> JumpUtil.startAct(context, CheckRecordActivity.class));//历史记录
        layoutUpdatePwd.setOnClickListener(v -> JumpUtil.startAct(context, UpdatePwdActivity.class));//修改密码
        refresh.setOnRefreshListener(() -> {
            getUserInfo(getUserId(), getToken());
            getTheUserCurrentRecharge(getUserId(), getToken());
        });
        //退出账户
        btnLogout.setOnClickListener(v ->
                new IosDialog(context, new IosDialog.DialogClick() {
                    @Override
                    public void Confirm(AlertDialog exitDialog, boolean confirm) {
                        if (confirm) {
                            spUtil.put(Constant.IS_LOGIN, false);
                            spUtil.remove(VEHICLE_ID);
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
    }

    /**
     * @param appUserId 用户id
     * @param token     token
     *                  获取当前账号充值状态
     */
    private void getTheUserCurrentRecharge(String appUserId, String token) {
        OkHttpUtils.get().url(SERVER_URL + getTheUserCurrentRecharge_URL).
                addParam("appUserId", appUserId).
                addParam("token", token).
                build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                LogE("获取当前账号充值状态:" + response);
                UserCurrentRechargeEntity entity = JSON.parseObject(response, UserCurrentRechargeEntity.class);
                if (entity.isSuccess()) {
                    if (entity.getCode().equals("SUCCESS")) {
                        ////商品类型1 实物 2 虚拟
                        tvRechargeSetMeaName.setText(entity.getData().getRechargeSetMeaName());
                    } else {
                        tvRechargeSetMeaName.setText(R.string.RechargeSetMeaName_no);
                    }
                } else {
                    tvRechargeSetMeaName.setText(R.string.RechargeSetMeaName_no);
                }
            }
        });
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

            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response, int id) {
                UserInfoEntity entity = JSON.parseObject(response, UserInfoEntity.class);
                if (entity.isSuccess()) {
                    refresh.setRefreshing(false);
                    tvName.setText(entity.getData().getNickname());
                    tvIntegral.setText(entity.getData().getPhoneNum());
                    if (entity.getData().getIsVip() == 1) {
                        if (!TextUtils.isEmpty(entity.getData().getEndValidity())) {
                            tvRechargeTime.setText(getString(R.string.RechargeTime) + entity.getData().getEndValidity().split(" ")[0]);
                        } else {
                            tvRechargeTime.setText(R.string.RechargeTime);
                        }
                    }
                    ivVip.setVisibility(entity.getData().getIsVip() == 1 ? View.VISIBLE : View.GONE);
                    if (entity.getData().getHeadPortrait().length() > 0) {
                        myHeaderImage.setImageBitmap(BitMapUtils.stringToBitmap(entity.getData().getHeadPortrait()));
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
    private void getVehicleInfoById(String token, String vehicleId,String type) {
        if (type.equals("1")){
            dialogUtils.showProgressDialog();
        }
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
                    dialogUtils.dismiss();
                    layoutCar.setVisibility(View.VISIBLE);
                    layoutCar.setOnClickListener(v -> JumpUtil.startActToData(context, VehicleInfoActivity.class, vehicleId, 0));
                    tvAutomobileBrandName.setText(entity.getData().getAutomobileBrandName());
                    tvModelName.setText(TextUtils.isEmpty(entity.getData().getModelName()) ? "" : entity.getData().getModelName());
                    if (!TextUtils.isEmpty(entity.getData().getLogo())) {
                        Glide.with(context).load(SERVER_URL + entity.getData().getLogo()).into(ivCarLogo);
                    }
                    if (StringUtil.isNull(entity.getData().getBluetoothDeviceNumber())) {//车辆状态 1 未绑定 2 已绑定 ,
                        tvOBDState.setText(R.string.device_onbind_no);
                        Drawable drawable = context.getResources().getDrawable(R.drawable.icon_no);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        tvOBDState.setCompoundDrawables(drawable, null, null, null);
                    } else {
                        tvOBDState.setText(R.string.device_onbind_ok);
                        Drawable drawable = context.getResources().getDrawable(R.drawable.icon_ok);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        tvOBDState.setCompoundDrawables(drawable, null, null, null);
                    }
                }else {
                    dialogUtils.dismiss();
                }
            }
        });
    }

    private void initReceiver() {
        lm = LocalBroadcastManager.getInstance(context);
        IntentFilter intentFilter = new IntentFilter(OBD_ACTION);
        testReceiver = new TestReceiver();
        lm.registerReceiver(testReceiver, intentFilter);
    }


    private class TestReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String vehicleId = intent.getStringExtra(VEHICLE_ID);
            String type = intent.getStringExtra("type");
            getVehicleInfoById(getToken(), vehicleId,type);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (resultCode == 102) {
                getTheUserCurrentRecharge(getUserId(), getToken());
                getUserInfo(getUserId(), getToken());
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        lm.unregisterReceiver(testReceiver);
    }
}