package com.example.obdandroid.ui.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by yechao on 2017/12/15.
 * Describe :
 */

public class MultipleItem implements MultiItemEntity {

    public static final int TYPE_OBD = 1;
    public static final int TYPE_CAR = 2;
    public static final int TYPE_PAY = 3;
    public static final int TYPE_TROUBLE = 4;
    public static final int TYPE_TOOLS_HEADER = 5;
    public static final int TYPE_TOOLS = 6;
    private int itemType;
    private int spanSize;

    public MultipleItem(int itemType, int spanSize) {
        this.itemType = itemType;
        this.spanSize = spanSize;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public int getSpanSize() {
        return spanSize;
    }

    public void setSpanSize(int spanSize) {
        this.spanSize = spanSize;
    }


    public String mString1;

    public String mString2;

    public boolean isShow;

    public int count;

}
