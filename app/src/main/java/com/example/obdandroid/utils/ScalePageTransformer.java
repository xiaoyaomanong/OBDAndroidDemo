package com.example.obdandroid.utils;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * 作者：Jealous
 * 日期：2021/1/28 0028
 * 描述：
 */
public class ScalePageTransformer implements ViewPager.PageTransformer {
    //最小状态时，Size缩小为90%
    private static final float MIN_SCALE = 0.9F;

    @Override
    public void transformPage(View view, float position) {
        float scale = Math.max(MIN_SCALE,1 - Math.abs(position));
        if (position < -1.0f) {
            view.setScaleY(MIN_SCALE);
        } else if (position <= 0.0f) {
            view.setScaleY(scale);
        } else if (position <= 1.0f) {
            view.setScaleY(scale);
        } else {
            view.setScaleY(MIN_SCALE);
        }
    }
}