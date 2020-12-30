package com.example.obdandroid.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseFragment;
import com.example.obdandroid.ui.activity.OBDSettingActivity;
import com.example.obdandroid.ui.activity.SettingActivity;
import com.example.obdandroid.ui.adapter.MultipleItemQuickAdapter;
import com.example.obdandroid.ui.entity.MultipleItem;
import com.example.obdandroid.ui.view.CircleImageView;
import com.example.obdandroid.utils.JumpUtil;

import java.util.ArrayList;
import java.util.List;

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

        initRecyclerView();

        initListener();
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

        multipleItem = new MultipleItem(MultipleItem.TYPE_COUNT, 5);
        multipleItem.mString1 = "收藏";
        multipleItem.mString2 = "关注";
        itemDataList.add(multipleItem);

        multipleItem = new MultipleItem(MultipleItem.TYPE_ORDER_HEADER, 5);
        multipleItem.mString2 = "type2";
        itemDataList.add(multipleItem);

        for (int i = 0; i < 5; i++) {
            multipleItem = new MultipleItem(MultipleItem.TYPE_ORDER, 1);
            multipleItem.mString1 = "待付款";
            if (i % 2 == 0) {
                multipleItem.isShow = true;
                multipleItem.count = 6;
            } else {
                multipleItem.isShow = false;
                multipleItem.count = 0;
            }
            itemDataList.add(multipleItem);
        }

      /*  multipleItem = new MultipleItem(MultipleItem.TYPE_BALANCE, 5);
        multipleItem.mString1 = "￥9999.00";
        itemDataList.add(multipleItem);*/

        multipleItem = new MultipleItem(MultipleItem.TYPE_TOOLS_HEADER, 5);
        multipleItem.mString1 = "type5";
        itemDataList.add(multipleItem);

        for (int i = 0; i < 5; i++) {
            multipleItem = new MultipleItem(MultipleItem.TYPE_TOOLS, 1);
            multipleItem.mString1 = "使用帮助";
            if (i % 2 == 0) {
                multipleItem.isShow = true;
                multipleItem.count = 100;
            } else {
                multipleItem.isShow = false;
                multipleItem.count = 0;
            }
            itemDataList.add(multipleItem);
        }
    }

    @SuppressLint("NonConstantResourceId")
    private void initRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 5);
        recyclerView.setLayoutManager(gridLayoutManager);

        multipleItemQuickAdapter = new MultipleItemQuickAdapter(itemDataList);

        View headerView = getHeaderView(view -> {
            switch (view.getId()) {
                case R.id.my_header_image:
                    showToast("你点击了头像");
                    break;
                case R.id.my_header_settings:
                    JumpUtil.startAct(context, SettingActivity.class);
                    break;
            }
        });

        multipleItemQuickAdapter.addHeaderView(headerView);

        recyclerView.setAdapter(multipleItemQuickAdapter);
    }


    @SuppressLint("SetTextI18n")
    private View getHeaderView(View.OnClickListener listener) {
        View headerView = getLayoutInflater().inflate(R.layout.layout_my_header, (ViewGroup) recyclerView.getParent(), false);

        CircleImageView myHeaderImage = headerView.findViewById(R.id.my_header_image);
        myHeaderImage.setImageResource(R.drawable.header_image);
        myHeaderImage.setOnClickListener(listener);

        TextView myHeaderName = headerView.findViewById(R.id.my_header_name);
        myHeaderName.setText("马云");

        TextView myHeaderMobile = headerView.findViewById(R.id.my_header_mobile);
        myHeaderMobile.setText("15012134598");

        ImageView myHeaderSettings = headerView.findViewById(R.id.my_header_settings);
        myHeaderSettings.setOnClickListener(listener);

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
                case R.id.my_favorites:
                    showToast("收藏");
                    break;
              /*  case R.id.my_bands:
                    JumpUtil.startAct(context, OBDSettingActivity.class);
                    break;*/
                case R.id.ll_my_order:
                    JumpUtil.startAct(context, OBDSettingActivity.class);
                    break;
               /* case R.id.my_balance_btn:
                    showToast("立即充值");
                    break;*/
            }
        });
    }
}