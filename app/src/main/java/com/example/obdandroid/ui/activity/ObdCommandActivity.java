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
import com.github.pires.obd.commands.ObdCommand;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;

import java.util.ArrayList;

/**
 * 作者：Jealous
 * 日期：2020/12/30 0030
 * 描述：
 */
public class ObdCommandActivity extends BaseActivity {
    private Context context;
    private TitleBar titleBarSet;
    private RecyclerView recycleObdCommand;
    private ObdCommandAdapter adapter;

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
        context = this;
        titleBarSet = findViewById(R.id.titleBarSet);
        recycleObdCommand = findViewById(R.id.recycle_obd_command);
        titleBarSet.setTitle("OBD指令");
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(OrientationHelper.VERTICAL);
        recycleObdCommand.setLayoutManager(manager);
        adapter = new ObdCommandAdapter(context);
        // ArrayList<ObdCommand> commands= ObdConfig.getCommands();
        adapter.setList(ObdConfig.getCommands());
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