package com.example.obdandroid.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseFragment;
import com.example.obdandroid.ui.activity.OBDSettingActivity;
import com.example.obdandroid.ui.activity.AppSettingActivity;
import com.example.obdandroid.ui.activity.RechargeSetMealActivity;
import com.example.obdandroid.ui.adapter.MultipleItemQuickAdapter;
import com.example.obdandroid.ui.entity.MultipleItem;
import com.example.obdandroid.ui.entity.UserInfoEntity;
import com.example.obdandroid.ui.view.CircleImageView;
import com.example.obdandroid.utils.JumpUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

import static com.example.obdandroid.config.APIConfig.SERVER_URL;
import static com.example.obdandroid.config.APIConfig.USER_INFO_URL;
import static com.example.obdandroid.config.TAG.TAG_Activity;

/**
 * 作者：Jealous
 * 日期：2020/12/23 0023
 * 描述：
 */
public class PersonalFragment extends BaseFragment {
    private MultipleItem multipleItem = null;
    private List<MultipleItem> itemDataList;
    private MultipleItemQuickAdapter multipleItemQuickAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private Context context;

    public static PersonalFragment getInstance() {
        PersonalFragment fragment = new PersonalFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_person_center;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {
        context = getHoldingActivity();
        swipeRefreshLayout = getView(R.id.swipeRefreshLayout);
        recyclerView = getView(R.id.recyclerView);
        initSwipeRefreshLayout();

        initItemData();
        getUserInfo(getUserId(), getToken());

    }

    /**
     * @param userId 用户id
     * @param token  接口令牌
     *               用户信息
     */
    private void getUserInfo(String userId, String token) {
        OkHttpUtils.get().
                url(SERVER_URL + USER_INFO_URL).
                addParam("userId", userId).
                addParam("token", token).
                build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                Log.e(TAG_Activity, "用户信息:" + response);
                UserInfoEntity entity = JSON.parseObject(response, UserInfoEntity.class);
                if (entity.isSuccess()) {
                    initRecyclerView(entity);
                    initListener();
                }
            }
        });
    }

    private void initSwipeRefreshLayout() {
        swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light);

        swipeRefreshLayout.setOnRefreshListener(() -> new Handler().postDelayed(() -> {
            multipleItemQuickAdapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
            showToast("刷新完成");
        }, 2000));
    }


    private void initItemData() {
        itemDataList = new ArrayList<>();
        multipleItem = new MultipleItem(MultipleItem.TYPE_OBD, 5);
        multipleItem.mString2 = "type1";
        itemDataList.add(multipleItem);

        multipleItem = new MultipleItem(MultipleItem.TYPE_CAR, 5);
        multipleItem.mString2 = "type2";
        itemDataList.add(multipleItem);

        multipleItem = new MultipleItem(MultipleItem.TYPE_PAY, 5);
        multipleItem.mString2 = "type3";
        itemDataList.add(multipleItem);
    }

    @SuppressLint("NonConstantResourceId")
    private void initRecyclerView(UserInfoEntity entity) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 5);
        recyclerView.setLayoutManager(gridLayoutManager);
        multipleItemQuickAdapter = new MultipleItemQuickAdapter(itemDataList);
        View headerView = getHeaderView(entity, view -> {
            switch (view.getId()) {
                case R.id.my_header_image:
                    showToast("你点击了头像");
                    break;
                case R.id.my_header_settings:
                    JumpUtil.startAct(context, AppSettingActivity.class);
                    break;
            }
        });
        multipleItemQuickAdapter.addHeaderView(headerView);
        recyclerView.setAdapter(multipleItemQuickAdapter);
    }


    /**
     * @param listener 点击监听事件
     * @return 头部布局
     */
    @SuppressLint("SetTextI18n")
    private View getHeaderView(UserInfoEntity entity, View.OnClickListener listener) {
        View headerView = getLayoutInflater().inflate(R.layout.layout_my_header, (ViewGroup) recyclerView.getParent(), false);
        CircleImageView myHeaderImage = headerView.findViewById(R.id.my_header_image);
        myHeaderImage.setImageResource(R.drawable.header_image);
        myHeaderImage.setOnClickListener(listener);
        TextView myHeaderName = headerView.findViewById(R.id.my_header_name);
        myHeaderName.setText(entity.getData().getNickname());
        TextView myHeaderMobile = headerView.findViewById(R.id.my_header_mobile);
        myHeaderMobile.setText(entity.getData().getPhoneNum());
        ImageView myHeaderSettings = headerView.findViewById(R.id.my_header_settings);
        myHeaderImage.setImageBitmap(stringToBitmap(entity.getData().getHeadPortrait()));
        myHeaderSettings.setOnClickListener(listener);
        ImageView ivVip = headerView.findViewById(R.id.ivVip);
        if (entity.getData().getIsVip() == 1) {
            ivVip.setImageResource(R.drawable.icon_vip_ok);
        } else {
            ivVip.setImageResource(R.drawable.icon_vip_no);
        }
        return headerView;
    }


    @SuppressLint("NonConstantResourceId")
    private void initListener() {
        multipleItemQuickAdapter.setSpanSizeLookup((gridLayoutManager, position) -> itemDataList.get(position).getSpanSize());
        multipleItemQuickAdapter.setOnItemClickListener((adapter, view, position) -> {
            showToast("第  " + position);
            //可以再加一层 类型 的判断，一般来说订单不是点了就消失的
            if (itemDataList.get(position).getItemType() == MultipleItem.TYPE_TOOLS) {
                if (itemDataList.get(position).isShow) {
                    itemDataList.get(position).isShow = false;
                    LogE("count  =  " + itemDataList.get(position).count);
                    multipleItemQuickAdapter.notifyItemChanged(position + 1);
                } else
                    itemDataList.get(position).isShow = false;
            }
        });

        multipleItemQuickAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.ll_my_car:
                    showToast("我的车辆");
                    break;
                case R.id.ll_my_pay:
                    JumpUtil.startAct(context, RechargeSetMealActivity.class);
                    break;
                case R.id.ll_my_obd:
                    JumpUtil.startAct(context, OBDSettingActivity.class);
                    break;
               /* case R.id.my_balance_btn:
                    showToast("立即充值");
                    break;*/
            }
        });
    }

    /**
     * @param string Base64图片
     * @return Base64转换图片
     */
    public static Bitmap stringToBitmap(String string) {
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray = Base64.decode(string.split(",")[1], Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}