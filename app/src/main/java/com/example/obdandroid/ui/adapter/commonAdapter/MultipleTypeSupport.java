package com.example.obdandroid.ui.adapter.commonAdapter;

/**
 * Created by zhiwenyan on 5/25/17.
 * 多布局的支持
 */

public interface MultipleTypeSupport<T> {
    int getLayoutId(T item);
}
