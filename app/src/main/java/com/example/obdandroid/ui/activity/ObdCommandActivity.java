package com.example.obdandroid.ui.activity;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseActivity;
import com.example.obdandroid.config.ObdConfig;
import com.example.obdandroid.ui.adapter.ObdCommandAdapter;
import com.example.obdandroid.utils.SPUtil;
import com.github.pires.obd.commands.ObdCommand;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;

import java.util.ArrayList;

import static com.example.obdandroid.config.Constant.PROTOCOLS_LIST_KEY;

/**
 * 作者：Jealous
 * 日期：2020/12/30 0030
 * 描述：
 */
public class ObdCommandActivity extends BaseActivity {

    @Override
    protected int getContentViewId() {
        return R.layout.activity_obd_command;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    public void initView() {
        super.initView();
        Context context = this;
        TitleBar titleBarSet = findViewById(R.id.titleBarSet);
        RecyclerView recycleObdCommand = findViewById(R.id.recycle_obd_command);
        titleBarSet.setTitle("OBD指令");
        SPUtil spUtil = new SPUtil(context);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(OrientationHelper.VERTICAL);
        recycleObdCommand.setLayoutManager(manager);
        ObdCommandAdapter adapter = new ObdCommandAdapter(context);
        adapter.setList(ObdConfig.getCommands(spUtil.getString(PROTOCOLS_LIST_KEY, "AUTO")));
        recycleObdCommand.setAdapter(adapter);
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