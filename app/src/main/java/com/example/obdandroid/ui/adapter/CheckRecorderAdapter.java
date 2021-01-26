package com.example.obdandroid.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.obdandroid.R;
import com.example.obdandroid.config.TAG;
import com.example.obdandroid.ui.entity.CarModelEntity;
import com.example.obdandroid.ui.entity.FaultCodeDetailsEntity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

import static com.example.obdandroid.config.APIConfig.FAULT_CODE_URL;
import static com.example.obdandroid.config.APIConfig.SERVER_URL;

/**
 * 作者：Jealous
 * 日期：2021/1/23 0023
 * 描述：
 */
public class CheckRecorderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private final LayoutInflater inflater;
    private List<String> list;
    private String token;

    public CheckRecorderAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(inflater.inflate(R.layout.item_check_content, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final MyViewHolder holder1 = (MyViewHolder) holder;
        if (!TextUtils.isEmpty(list.get(position))) {
            holder1.tvCode.setText("故障码:" + list.get(position));
            holder1.tvContent.setTextColor(context.getResources().getColor(R.color.gray_light_d));
            holder1.tvScopeOfApplication.setTextColor(context.getResources().getColor(R.color.gray_light_d));
            getFaultCodeDetails(list.get(position), token, holder1.tvContent,holder1.tvScopeOfApplication);
        } else {
            holder1.tvCode.setVisibility(View.GONE);
            holder1.tvScopeOfApplication.setVisibility(View.GONE);
            holder1.tvContent.setText("通过检测,无故障码,您的车辆很健康!");
            holder1.tvContent.setTextColor(context.getResources().getColor(R.color.color_green));
        }
    }

    @Override
    public int getItemCount() {
        //获取传入adapter的条目数，没有则返回 1
        if (list != null) {
            if (list.size() > 0) {
                return list.size();
            }
        }
        //位空视图保留一个条目
        return 1;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        private final TextView tvCode;
        private final TextView tvContent;
        private final TextView tvScopeOfApplication;
        private final LinearLayout card_view;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tvCode = itemView.findViewById(R.id.tvCode);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvScopeOfApplication = itemView.findViewById(R.id.tvScopeOfApplication);
            card_view = itemView.findViewById(R.id.card_view);
        }
    }

    public static class EmptyViewHolder extends RecyclerView.ViewHolder {
        private final TextView mEmptyTextView;

        public EmptyViewHolder(View itemView) {
            super(itemView);
            mEmptyTextView = itemView.findViewById(R.id.tv_empty_text);
        }
    }

    /**
     * @param faultCode OBD汽车故障码
     * @param token     接口令牌
     *                  查询故障码
     */
    private void getFaultCodeDetails(String faultCode, String token, TextView tvContent, TextView tvScopeOfApplication) {
        OkHttpUtils.get().url(SERVER_URL + FAULT_CODE_URL).
                addParam("token", token).
                addParam("faultCode", faultCode).
                build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response, int id) {
                FaultCodeDetailsEntity entity = JSON.parseObject(response, FaultCodeDetailsEntity.class);
                if (entity.isSuccess()) {
                    if (entity.getData().size() != 0) {
                        tvContent.setText("中文含义: " + entity.getData().get(0).getChineseMeaning());
                        tvScopeOfApplication.setText("适用车商: " + entity.getData().get(0).getScopeOfApplication());
                    } else {
                        tvContent.setTextColor(context.getResources().getColor(R.color.cpb_blue_dark));
                        tvContent.setText("该故障码暂时未录入系统,请联系相关人员");
                        tvScopeOfApplication.setVisibility(View.GONE);
                    }
                }
            }
        });
    }
}