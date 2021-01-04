package com.example.obdandroid.ui.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.obdandroid.R;
import com.example.obdandroid.ui.entity.MultipleItem;

import java.util.List;

/**
 * Created by yechao on 2017/12/15.
 * Describe :
 */

public class MultipleItemQuickAdapter extends BaseMultiItemQuickAdapter<MultipleItem, BaseViewHolder> {

    public MultipleItemQuickAdapter(List data) {
        super(data);
        addItemType(MultipleItem.TYPE_COUNT, R.layout.layout_my_obd);
        addItemType(MultipleItem.TYPE_ORDER_HEADER, R.layout.layout_my_car);
        //addItemType(MultipleItem.TYPE_ORDER, R.layout.layout_my_order);
        //addItemType(MultipleItem.TYPE_BALANCE, R.layout.layout_my_balance);
        // addItemType(MultipleItem.TYPE_TOOLS_HEADER, R.layout.layout_my_tools_header);
        //addItemType(MultipleItem.TYPE_TOOLS, R.layout.layout_my_tools);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultipleItem item) {
        switch (helper.getItemViewType()) {
            case MultipleItem.TYPE_COUNT:
                helper.addOnClickListener(R.id.ll_my_obd);
                break;
            case MultipleItem.TYPE_ORDER_HEADER:
                helper.addOnClickListener(R.id.ll_my_car);
                break;
          /*  case MultipleItem.TYPE_ORDER:
                helper.setImageDrawable(R.id.my_order_image, ContextCompat.getDrawable(mContext, R.mipmap.ic_launcher));
                helper.setText(R.id.my_order_name, item.mString1);
                break;
            case MultipleItem.TYPE_BALANCE:
                helper.setText(R.id.my_balance_text, item.mString1);
                helper.addOnClickListener(R.id.my_balance_btn);
                break;
            case MultipleItem.TYPE_TOOLS_HEADER:
                //helper.setText(R.id.tv_item_name, item.mString1);
                break;
            case MultipleItem.TYPE_TOOLS:
                helper.setImageDrawable(R.id.my_tools_image, ContextCompat.getDrawable(mContext, R.mipmap.ic_launcher));
                helper.setText(R.id.my_tools_text, item.mString1);
                break;*/
        }
    }

}
