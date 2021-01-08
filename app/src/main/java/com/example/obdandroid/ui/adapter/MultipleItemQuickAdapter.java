package com.example.obdandroid.ui.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.obdandroid.R;
import com.example.obdandroid.ui.entity.MultipleItem;

import java.util.List;


/**
 * 作者：Jealous
 * 日期：2021/1/7 0007
 * 描述：
 */

public class MultipleItemQuickAdapter extends BaseMultiItemQuickAdapter<MultipleItem, BaseViewHolder> {

    public MultipleItemQuickAdapter(List data) {
        super(data);
        addItemType(MultipleItem.TYPE_OBD, R.layout.layout_my_obd);
        addItemType(MultipleItem.TYPE_CAR, R.layout.layout_my_car);
        addItemType(MultipleItem.TYPE_PAY, R.layout.layout_my_pay);
        addItemType(MultipleItem.TYPE_TROUBLE, R.layout.layout_my_trouble);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultipleItem item) {
        switch (helper.getItemViewType()) {
            case MultipleItem.TYPE_OBD:
                helper.addOnClickListener(R.id.ll_my_obd);
                break;
            case MultipleItem.TYPE_CAR:
                helper.addOnClickListener(R.id.ll_my_car);
                break;
            case MultipleItem.TYPE_PAY:
                helper.addOnClickListener(R.id.ll_my_pay);
                break;
            case MultipleItem.TYPE_TROUBLE:
                helper.addOnClickListener(R.id.ll_my_trouble);
                break;
        }
    }

}
