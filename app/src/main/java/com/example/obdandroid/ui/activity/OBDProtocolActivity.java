package com.example.obdandroid.ui.activity;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseActivity;
import com.example.obdandroid.config.Constant;
import com.example.obdandroid.ui.adapter.OBDProtocolAdapter;
import com.example.obdandroid.ui.entity.OBDProtocolEntity;
import com.example.obdandroid.utils.SPUtil;
import com.github.pires.obd.enums.ObdProtocols;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;

import java.util.ArrayList;

/**
 * 作者：Jealous
 * 日期：2020/12/30 0030
 * 描述：
 */
public class OBDProtocolActivity extends BaseActivity {
    private Context context;
    private TitleBar titleBarSet;
    private RecyclerView recycleObdProtocol;
    private OBDProtocolAdapter adapter;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_obd_protocol;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    public void initView() {
        super.initView();
        context = this;
        titleBarSet = findViewById(R.id.titleBarSet);
        recycleObdProtocol = findViewById(R.id.recycle_obd_protocol);
        titleBarSet.setTitle("OBD协议");
        SPUtil spUtil = new SPUtil(context);
        String obdProtocol = spUtil.getString(Constant.PROTOCOLS_LIST_KEY, "AUTO");
        ArrayList<OBDProtocolEntity> protocolStrings = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(OrientationHelper.VERTICAL);
        recycleObdProtocol.setLayoutManager(manager);
        adapter = new OBDProtocolAdapter(context);
        //可用的OBD协议
        for (ObdProtocols protocol : ObdProtocols.values()) {
            OBDProtocolEntity entity = new OBDProtocolEntity();
            entity.setChecked(obdProtocol.equals(protocol.name()));
            entity.setName(protocol.name());
            protocolStrings.add(entity);
        }
        adapter.setList(protocolStrings);
        recycleObdProtocol.setAdapter(adapter);
        adapter.setClick(protocol -> {
            spUtil.put(Constant.PROTOCOLS_LIST_KEY,protocol);
        });
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
}