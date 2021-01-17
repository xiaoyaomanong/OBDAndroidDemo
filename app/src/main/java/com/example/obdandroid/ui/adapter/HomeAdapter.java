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

import com.example.obdandroid.R;
import com.example.obdandroid.ui.view.CircleImageView;

/**
 * 作者：Jealous
 * 日期：2020/12/29 0029
 * 描述：
 */
public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private final LayoutInflater inflater;
    private OnClickCallBack clickCallBack;
    private final String[] list = {"大众", "本田", "别克", "丰田", "宝马", "日产", "奥迪", "奔驰", "马自达", "更多"};
    private final int[] resImg = {R.drawable.icon_dz, R.drawable.icon_bt, R.drawable.icon_bk, R.drawable.icon_ft,
            R.drawable.icon_bm, R.drawable.icon_rc, R.drawable.icon_audi, R.drawable.icon_bc, R.drawable.icon_mzd,
            R.drawable.icon_car_more};
    private final int EMPTY_VIEW = 0;//空页面
    private final int NOT_EMPTY_VIEW = 1;//正常页面

    public HomeAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setClickCallBack(OnClickCallBack clickCallBack) {
        this.clickCallBack = clickCallBack;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (EMPTY_VIEW == viewType) {
            return new EmptyViewHolder(inflater.inflate(R.layout.stub_empty, parent, false));
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
            holder1.tvName.setText(list[position]);
            holder1.ivName.setImageResource(resImg[position]);
            holder1.card_view.setOnClickListener(v -> clickCallBack.Click(list[position]));
        }
    }

    @Override
    public int getItemCount() {
        //获取传入adapter的条目数，没有则返回 1
        if (list != null) {
            if (list.length > 0) {
                return list.length;
            }
        }
        //位空视图保留一个条目
        return 1;
    }

    @Override
    public int getItemViewType(int position) {
        //根据传入adapter来判断是否有数据
        if (list != null) {
            if (list.length != 0) {
                return NOT_EMPTY_VIEW;
            } else {
                return EMPTY_VIEW;
            }
        }
        return EMPTY_VIEW;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        private final CircleImageView ivName;
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
        void Click(String name);
    }
}