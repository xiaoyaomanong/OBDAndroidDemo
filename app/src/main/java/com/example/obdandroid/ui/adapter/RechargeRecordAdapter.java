package com.example.obdandroid.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.obdandroid.R;
import com.example.obdandroid.ui.entity.RechargeRecordEntity;
import com.example.obdandroid.utils.AppDateUtils;

import java.util.List;

/**
 * 作者：Jealous
 * 日期：2021/1/22 0022
 * 描述：
 */
public class RechargeRecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private final LayoutInflater inflater;
    private List<RechargeRecordEntity.DataEntity.ListEntity> list;
    private final int EMPTY_VIEW = 0;//空页面
    private final int NOT_EMPTY_VIEW = 1;//正常页面

    public RechargeRecordAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }


    public void setList(List<RechargeRecordEntity.DataEntity.ListEntity> list) {
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (EMPTY_VIEW == viewType) {
            return new EmptyViewHolder(inflater.inflate(R.layout.stub_empty, parent, false));
        }
        return new MyViewHolder(inflater.inflate(R.layout.item_charge_record, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, @SuppressLint("RecyclerView") int position) {
        int itemViewType = getItemViewType(position);
        if (EMPTY_VIEW == itemViewType) {
            EmptyViewHolder holder = (EmptyViewHolder) viewHolder;
            holder.mEmptyTextView.setText("暂无数据");
        } else if (NOT_EMPTY_VIEW == itemViewType) {
            final MyViewHolder holder = (MyViewHolder) viewHolder;
            holder.tvRechargeTime.setText("购买时间: " + list.get(position).getRechargeTime());
            if (TextUtils.isEmpty(list.get(position).getRechargeSetMeaName())) {
                holder.tvRechargeSetMeaName.setText("");
            } else {
                holder.tvRechargeSetMeaName.setText(list.get(position).getRechargeSetMeaName());
            }
            holder.tvRechargeStatusName.setText(list.get(position).getRechargeStatusName());
            if (!TextUtils.isEmpty(list.get(position).getRechargeStatusName())) {
                if (list.get(position).getRechargeStatusName().equals("成功")) {
                    holder.tvRechargeStatusName.setTextColor(context.getResources().getColor(R.color.gray_light_d));
                } else {
                    holder.tvRechargeStatusName.setTextColor(context.getResources().getColor(R.color.red));
                }
            }
            holder.tvPaymentChannelsName.setText(list.get(position).getPaymentChannelsName());
            holder.tvRechargetAmount.setText("￥" + list.get(position).getRechargetAmount());
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
        private final TextView tvRechargeTime;
        private final TextView tvRechargeStatusName;
        private final TextView tvPaymentChannelsName;
        private final TextView tvRechargeSetMeaName;
        private final TextView tvRechargetAmount;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tvRechargeTime = itemView.findViewById(R.id.tvRechargeTime);
            tvPaymentChannelsName = itemView.findViewById(R.id.tvPaymentChannelsName);
            tvRechargeStatusName = itemView.findViewById(R.id.tvRechargeStatusName);
            tvRechargeSetMeaName = itemView.findViewById(R.id.tvRechargeSetMeaName);
            tvRechargetAmount = itemView.findViewById(R.id.tvRechargetAmount);
        }
    }

    public static class EmptyViewHolder extends RecyclerView.ViewHolder {
        private final TextView mEmptyTextView;

        public EmptyViewHolder(View itemView) {
            super(itemView);
            mEmptyTextView = itemView.findViewById(R.id.tv_empty_text);
        }
    }


    public void addFootItem(List<RechargeRecordEntity.DataEntity.ListEntity> lists) {
        list.addAll(lists);
        notifyDataSetChanged();
    }
}