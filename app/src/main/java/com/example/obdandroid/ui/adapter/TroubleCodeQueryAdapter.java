package com.example.obdandroid.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.obdandroid.R;
import com.example.obdandroid.ui.entity.FaultCodeDetailsEntity;
import com.example.obdandroid.ui.entity.TestRecordEntity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

import static com.example.obdandroid.config.APIConfig.FAULT_CODE_URL;
import static com.example.obdandroid.config.APIConfig.SERVER_URL;

/**
 * 作者：Jealous
 * 日期：2021/2/1 0001
 * 描述：
 */
public class TroubleCodeQueryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private final LayoutInflater inflater;
    private List<String> list;
    private String token;
    private OnClickCallBack callBack;

    public TroubleCodeQueryAdapter(Context context) {
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
        return new MyViewHolder(inflater.inflate(R.layout.item_trouble, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final MyViewHolder holder1 = (MyViewHolder) holder;
        if (!TextUtils.isEmpty(list.get(position))) {
            holder1.tvCode.setText(list.get(position));
            holder1.tvBelongingSystem.setTextColor(context.getResources().getColor(R.color.gray_light_d));
            getFaultCodeDetails(list.get(position), token, holder1.tvBelongingSystem, holder1.tvScopeOfApplication);
            holder1.layoutDetails.setOnClickListener(v -> callBack.click(list.get(position)));
        } else {
            holder1.layoutDetails.setVisibility(View.GONE);
            holder1.layoutBelongingSystem.setVisibility(View.GONE);
            holder1.layoutScopeOfApplication.setVisibility(View.GONE);
            holder1.tvCodeTitle.setVisibility(View.GONE);
            holder1.tvCode.setText("通过检测,无故障码,您的车辆很健康!");
            holder1.tvCode.setTextColor(context.getResources().getColor(R.color.color_green));
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
        private final TextView tvBelongingSystem;
        private final TextView tvScopeOfApplication;
        private final TextView tvCodeTitle;
        private final LinearLayout layoutBelongingSystem;
        private final LinearLayout layoutScopeOfApplication;
        private final LinearLayout layoutDetails;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tvCode = itemView.findViewById(R.id.tvCode);
            tvBelongingSystem = itemView.findViewById(R.id.tvBelongingSystem);
            tvScopeOfApplication = itemView.findViewById(R.id.tvScopeOfApplication);
            tvCodeTitle = itemView.findViewById(R.id.tvCodeTitle);
            layoutBelongingSystem = itemView.findViewById(R.id.layoutBelongingSystem);
            layoutScopeOfApplication = itemView.findViewById(R.id.layoutScopeOfApplication);
            layoutDetails = itemView.findViewById(R.id.layoutDetails);
        }
    }

    public interface OnClickCallBack {
        void click(String faultCode);
    }

    /**
     * @param faultCode OBD汽车故障码
     * @param token     接口令牌
     *                  查询故障码
     */
    private void getFaultCodeDetails(String faultCode, String token, TextView tvBelongingSystem, TextView tvScopeOfApplication) {
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
                        tvBelongingSystem.setText(entity.getData().get(0).getBelongingSystem());
                        tvScopeOfApplication.setText(entity.getData().get(0).getScopeOfApplication());
                    } else {
                        tvBelongingSystem.setText("未知");
                        tvScopeOfApplication.setText("未知");
                    }
                }
            }
        });
    }
}