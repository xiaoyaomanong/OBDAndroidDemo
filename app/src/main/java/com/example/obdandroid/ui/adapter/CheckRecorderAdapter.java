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

import com.example.obdandroid.R;
import com.example.obdandroid.config.TAG;
import com.example.obdandroid.ui.entity.CarModelEntity;
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
    private final int EMPTY_VIEW = 0;//空页面
    private final int NOT_EMPTY_VIEW = 1;//正常页面
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
        if (EMPTY_VIEW == viewType) {
            return new EmptyViewHolder(inflater.inflate(R.layout.stub_empty, parent, false));
        }
        return new MyViewHolder(inflater.inflate(R.layout.item_check_content, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        int itemViewType = getItemViewType(position);
        if (EMPTY_VIEW == itemViewType) {
            EmptyViewHolder viewHolder = (EmptyViewHolder) holder;
            viewHolder.mEmptyTextView.setText("暂无数据");
        } else if (NOT_EMPTY_VIEW == itemViewType) {
            final MyViewHolder holder1 = (MyViewHolder) holder;
            if (!TextUtils.isEmpty(list.get(position))) {
                holder1.tvCode.setText("故障码:" + list.get(position));
                holder1.tvContent.setTextColor(context.getResources().getColor(R.color.gray_light_d));
                getFaultCodeDetails(list.get(position), token, holder1.tvContent);
            } else {
                holder1.tvCode.setVisibility(View.GONE);
                holder1.tvContent.setText("通过检测,无故障码,您的车辆很健康!");
                holder1.tvContent.setTextColor(context.getResources().getColor(R.color.color_green));
            }
            //holder1.card_view.setOnClickListener(v -> clickCallBack.Click(list.get(position)));
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

    @Override
    public int getItemViewType(int position) {
        //根据传入adapter来判断是否有数据
        if (list != null) {
            if (list.size() != 0) {
                return NOT_EMPTY_VIEW;
            } else {
                return EMPTY_VIEW;
            }
        }
        return EMPTY_VIEW;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        private final TextView tvCode;
        private final TextView tvContent;
        private final LinearLayout card_view;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tvCode = itemView.findViewById(R.id.tvCode);
            tvContent = itemView.findViewById(R.id.tvContent);
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
    private void getFaultCodeDetails(String faultCode, String token, TextView textView) {
        OkHttpUtils.get().
                url(SERVER_URL + FAULT_CODE_URL).
                addParam("token", token).
                addParam("faultCode", faultCode).
                build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                Log.e(TAG.TAG_Fragemnt, "查询故障码:" + response);
                textView.setText(response);
            }
        });
    }
}