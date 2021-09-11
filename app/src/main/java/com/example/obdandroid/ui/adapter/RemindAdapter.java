package com.example.obdandroid.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.obdandroid.R;
import com.example.obdandroid.ui.entity.MessageCheckEntity;
import com.example.obdandroid.ui.entity.RemindPageEntity;
import com.example.obdandroid.utils.JsonUtils;

import java.util.List;

/**
 * 作者：Jealous
 * 日期：2021/2/22 0022
 * 描述：
 */
public class RemindAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<RemindPageEntity.DataEntity.ListEntity> list;
    private final LayoutInflater inflater;
    private final int EMPTY_VIEW = 0;//空页面
    private final int NOT_EMPTY_VIEW = 1;//正常页面
    private OnClickCallBack clickCallBack;
    private Context context;

    public RemindAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setClickCallBack(OnClickCallBack clickCallBack) {
        this.clickCallBack = clickCallBack;
    }

    public void setList(List<RemindPageEntity.DataEntity.ListEntity> list) {
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (EMPTY_VIEW == viewType) {
            return new EmptyViewHolder(inflater.inflate(R.layout.item_empty, parent, false));
        }
        return new MyViewHolder(inflater.inflate(R.layout.item_remind, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        int itemViewType = getItemViewType(position);
        if (EMPTY_VIEW == itemViewType) {
            EmptyViewHolder viewHolder = (EmptyViewHolder) holder;
            viewHolder.mEmptyTextView.setText("没有消息");
        } else if (NOT_EMPTY_VIEW == itemViewType) {
            final MyViewHolder holder1 = (MyViewHolder) holder;
            holder1.setPosition(position);
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

    class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvRemindTitle;
        private final TextView tvCreateTime;
        private final TextView tvRemindContent;
        private final ImageView ivRead;
        private final LinearLayout card_view;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvRemindTitle = itemView.findViewById(R.id.tvRemindTitle);
            tvCreateTime = itemView.findViewById(R.id.tvCreateTime);
            card_view = itemView.findViewById(R.id.card_view);
            ivRead = itemView.findViewById(R.id.ivRead);
            tvRemindContent = itemView.findViewById(R.id.tvRemindContent);
        }

        @SuppressLint("SetTextI18n")
        public void setPosition(int position) {
            RemindPageEntity.DataEntity.ListEntity listEntity = list.get(position);
            switch (listEntity.getRemindType()) {
                case 1:
                    tvRemindContent.setText(listEntity.getContent());
                    tvCreateTime.setVisibility(View.GONE);
                    break;
                case 2:
                    if (JsonUtils.isGoodJson(listEntity.getContent())) {
                        MessageCheckEntity entity = JSON.parseObject(listEntity.getContent(), MessageCheckEntity.class);
                        tvRemindContent.setText(entity.getContent());
                        tvCreateTime.setText(entity.getCreateTime());
                    } else {
                        tvRemindContent.setText(listEntity.getContent());
                        tvCreateTime.setVisibility(View.GONE);
                    }
                    break;
            }
            tvRemindTitle.setText(listEntity.getTitle());
            //是否读取 1是 2 否 ,
            if (listEntity.getIsRead() >= 1) {
                ivRead.setImageResource(R.drawable.icon_read_ok);
            } else {
                ivRead.setImageResource(R.drawable.icon_read_no);
            }
            card_view.setOnClickListener(v -> clickCallBack.click(listEntity));
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
        void click(RemindPageEntity.DataEntity.ListEntity entity);
    }

    public void addFootItem(List<RemindPageEntity.DataEntity.ListEntity> lists) {
        list.addAll(lists);
        notifyDataSetChanged();
    }
}