package com.example.obdandroid.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.obdandroid.R;
import com.example.obdandroid.ui.entity.BluetoothDeviceEntity;

import java.util.List;


/**
 * 作者：Jealous
 * 日期：2020/12/28 0028
 * 描述：
 */
public class BluetoothSimpleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final LayoutInflater inflater;
    private OnClickCallBack clickCallBack;
    private List<BluetoothDeviceEntity> list;
    ;
    private final int EMPTY_VIEW = 0;//空页面
    private final int NOT_EMPTY_VIEW = 1;//正常页面

    public BluetoothSimpleAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    public void setClickCallBack(OnClickCallBack clickCallBack) {
        this.clickCallBack = clickCallBack;
    }

    public void setList(List<BluetoothDeviceEntity> list) {
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder result;
        if (EMPTY_VIEW == viewType) {
            result = new EmptyViewHolder(inflater.inflate(R.layout.item_empty, parent, false));
        } else {
            result = new MyViewHolder(inflater.inflate(R.layout.devices, parent, false));
        }
        return result;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        int itemViewType = getItemViewType(position);
        if (EMPTY_VIEW == itemViewType) {
            EmptyViewHolder viewHolder = (EmptyViewHolder) holder;
            viewHolder.mEmptyTextView.setText("附近没有蓝牙");
        } else if (NOT_EMPTY_VIEW == itemViewType) {
            final MyViewHolder holder1 = (MyViewHolder) holder;
            holder1.tvDeviceName.setText(list.get(position).getBlue_name());
            holder1.tvStatue.setText(list.get(position).getBlue_address());
            holder1.card_view.setOnClickListener(v -> clickCallBack.Click(list.get(position)));
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
        private final TextView tvDeviceName;
        private final TextView tvStatue;
        private final CardView card_view;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tvDeviceName = itemView.findViewById(R.id.tvDeviceName);
            tvStatue = itemView.findViewById(R.id.tvStatue);
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
        void Click(BluetoothDeviceEntity device);
    }
}
