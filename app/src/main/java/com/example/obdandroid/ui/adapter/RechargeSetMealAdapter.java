package com.example.obdandroid.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
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
import com.example.obdandroid.ui.entity.ChargeMealEntity;
import com.example.obdandroid.utils.AppDateUtils;

import java.text.ParseException;
import java.util.List;

/**
 * 作者：Jealous
 * 日期：2021/1/7 0007
 * 描述：
 */
public class RechargeSetMealAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final LayoutInflater inflater;
    private List<ChargeMealEntity.DataEntity.ListEntity> list;
    private OnClickCallBack clickCallBack;
    private final int EMPTY_VIEW = 0;//空页面
    private final int NOT_EMPTY_VIEW = 1;//正常页面
    private int selectedPosition = -5; //默认一个参数

    public RechargeSetMealAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    public void setClickCallBack(OnClickCallBack clickCallBack) {
        this.clickCallBack = clickCallBack;
    }

    public void setList(List<ChargeMealEntity.DataEntity.ListEntity> list) {
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (EMPTY_VIEW == viewType) {
            return new EmptyViewHolder(inflater.inflate(R.layout.stub_empty, parent, false));
        }
        return new MyViewHolder(inflater.inflate(R.layout.item_charge_meal, parent, false));
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
            //充值套餐类型(1 数量 2 时间) ,
            try {
                holder.tvEffectiveDays.setText("即日起有效期至:" + AppDateUtils.addDate(AppDateUtils.getTodayDateTime(), list.get(position).getEffectiveDays()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (list.get(position).getRechargeSetMeaNum() == 0) {
                holder.tvRechargeSetMeaNum.setText("不限制次数");
            } else {
                holder.tvRechargeSetMeaNum.setText("使用次数:" + list.get(position).getRechargeSetMeaNum());
            }
            if (TextUtils.isEmpty(list.get(position).getRechargeSetMeaName())) {
                holder.tvRechargeSetMeaName.setText("");
            } else {
                holder.tvRechargeSetMeaName.setText(list.get(position).getRechargeSetMeaName());
            }
            holder.tvRechargeSetMeaAmount.setText("￥" + list.get(position).getRechargeSetMeaAmount());

            if (list.get(position).isChecked()) {
                holder.card_view.setBackgroundResource(R.drawable.bg_charge_ok);
            } else {
                holder.card_view.setBackgroundResource(R.drawable.bg_charge);
            }
            holder.itemView.setOnClickListener(view -> {
                setIsChecked(position);
                clickCallBack.Click(list.get(position));
            });
        }
    }

    private void setIsChecked(int position) {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setChecked(i == position);
        }
        notifyDataSetChanged();
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
        private final TextView tvRechargeSetMeaName;
        private final TextView tvEffectiveDays;
        private final TextView tvRechargeSetMeaAmount;
        private final TextView tvRechargeSetMeaNum;
        private final LinearLayout card_view;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tvRechargeSetMeaName = itemView.findViewById(R.id.tvRechargeSetMeaName);
            tvEffectiveDays = itemView.findViewById(R.id.tvEffectiveDays);
            tvRechargeSetMeaAmount = itemView.findViewById(R.id.tvRechargeSetMeaAmount);
            tvRechargeSetMeaNum = itemView.findViewById(R.id.tvRechargeSetMeaNum);
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

    public interface OnClickCallBack {
        void Click(ChargeMealEntity.DataEntity.ListEntity entity);
    }

    public void addFootItem(List<ChargeMealEntity.DataEntity.ListEntity> lists) {
        list.addAll(lists);
        notifyDataSetChanged();
    }
}