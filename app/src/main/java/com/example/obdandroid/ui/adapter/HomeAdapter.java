package com.example.obdandroid.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.obdandroid.R;
import com.example.obdandroid.ui.entity.AutomobileBrandEntity;

import java.util.List;

import static com.example.obdandroid.config.APIConfig.SERVER_URL;

/**
 * 作者：Jealous
 * 日期：2020/12/29 0029
 * 描述：
 */
public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private final LayoutInflater inflater;
    private OnClickCallBack clickCallBack;
    private List<AutomobileBrandEntity.DataEntity> list;
    private final int EMPTY_VIEW = 0;//空页面
    private final int NOT_EMPTY_VIEW = 1;//正常页面

    public HomeAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setList(List<AutomobileBrandEntity.DataEntity> list) {
        this.list = list;
    }

    public void setClickCallBack(OnClickCallBack clickCallBack) {
        this.clickCallBack = clickCallBack;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (EMPTY_VIEW == viewType) {
            return new EmptyViewHolder(inflater.inflate(R.layout.item_empty, parent, false));
        }
        return new MyViewHolder(inflater.inflate(R.layout.item_home, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        int itemViewType = getItemViewType(position);
        if (EMPTY_VIEW == itemViewType) {
            EmptyViewHolder viewHolder = (EmptyViewHolder) holder;
            viewHolder.mEmptyTextView.setText("附近没有蓝牙");
        } else if (NOT_EMPTY_VIEW == itemViewType) {
            final MyViewHolder holder1 = (MyViewHolder) holder;
            holder1.tvName.setText(list.get(position).getName());
            Glide.with(context).load(SERVER_URL + list.get(position).getLogo()).into(holder1.ivName);
            holder1.card_view.setOnClickListener(v -> clickCallBack.click(list.get(position)));
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
        private final ImageView ivName;
        private final TextView tvName;
        private final LinearLayout card_view;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ivName = itemView.findViewById(R.id.ivName);
            tvName = itemView.findViewById(R.id.tvName);
            card_view = itemView.findViewById(R.id.card_view);
        }
    }

    public class EmptyViewHolder extends RecyclerView.ViewHolder {
        private final TextView mEmptyTextView;

        public EmptyViewHolder(View itemView) {
            super(itemView);
            mEmptyTextView = itemView.findViewById(R.id.tv_empty_text);
        }
    }

    public interface OnClickCallBack {
        void click(AutomobileBrandEntity.DataEntity entity);
    }
}