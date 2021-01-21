package com.example.obdandroid.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.obdandroid.R;
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
    private List<ChargeMealEntity.DataEntity> list;
    private OnClickCallBack clickCallBack;
    private final int EMPTY_VIEW = 0;//空页面
    private final int NOT_EMPTY_VIEW = 1;//正常页面

    public RechargeSetMealAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    public void setClickCallBack(OnClickCallBack clickCallBack) {
        this.clickCallBack = clickCallBack;
    }

    public void setList(List<ChargeMealEntity.DataEntity> list) {
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (EMPTY_VIEW == viewType) {
            return new EmptyViewHolder(inflater.inflate(R.layout.stub_empty, parent, false));
        }
        return new MyViewHolder(inflater.inflate(R.layout.item_charge_meal, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
        int itemViewType = getItemViewType(position);
        if (EMPTY_VIEW == itemViewType) {
            EmptyViewHolder holder = (EmptyViewHolder) viewHolder;
            holder.mEmptyTextView.setText("暂无数据");
        } else if (NOT_EMPTY_VIEW == itemViewType) {
            final MyViewHolder holder = (MyViewHolder) viewHolder;
            //充值套餐类型(1 数量 2 时间) ,
            if (list.get(position).getRechargeSetMeaType() == 1) {
                holder.tvEffectiveDays.setText("使用次数:" + list.get(position).getRechargeSetMeaNum());
            } else {
                try {
                    holder.tvEffectiveDays.setText("有效期至:" + AppDateUtils.addDate(AppDateUtils.getTodayDateTime(), list.get(position).getEffectiveDays()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if (TextUtils.isEmpty(list.get(position).getRechargeSetMeaName())) {
                holder.tvRechargeSetMeaName.setVisibility(View.GONE);
            } else {
                holder.tvRechargeSetMeaName.setText(list.get(position).getRechargeSetMeaName());
            }
            holder.tvBindingDeviceNum.setText("1个OBD终端 | 绑定" + list.get(position).getBindingDeviceNum() + "台车");
            holder.tvRechargeSetMeaAmount.setText("￥" + list.get(position).getRechargeSetMeaAmount());
            holder.card_view.setOnClickListener(v -> clickCallBack.Click(list.get(position)));
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
        private final TextView tvRechargeSetMeaName;
        private final TextView tvEffectiveDays;
        private final TextView tvRechargeSetMeaAmount;
        private final TextView tvBindingDeviceNum;
        private final LinearLayout card_view;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tvRechargeSetMeaName = itemView.findViewById(R.id.tvRechargeSetMeaName);
            tvEffectiveDays = itemView.findViewById(R.id.tvEffectiveDays);
            tvRechargeSetMeaAmount = itemView.findViewById(R.id.tvRechargeSetMeaAmount);
            tvBindingDeviceNum = itemView.findViewById(R.id.tvBindingDeviceNum);
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
        void Click(ChargeMealEntity.DataEntity entity);
    }

    public void addFootItem(List<ChargeMealEntity.DataEntity> lists) {
        list.addAll(lists);
        notifyDataSetChanged();
    }
}