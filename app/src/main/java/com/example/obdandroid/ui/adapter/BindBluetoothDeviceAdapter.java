package com.example.obdandroid.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.obdandroid.R;
import com.example.obdandroid.ui.activity.BindBluetoothDeviceActivity;
import com.example.obdandroid.ui.entity.BluetoothDeviceEntity;

import java.util.List;

/**
 * 作者：Jealous
 * 日期：2021/1/13 0013
 * 描述：
 */
public class BindBluetoothDeviceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private final LayoutInflater inflater;
    private OnClickCallBack clickCallBack;
    private List<BluetoothDeviceEntity> list;
    ;
    private final int EMPTY_VIEW = 0;//空页面
    private final int NOT_EMPTY_VIEW = 1;//正常页面

    public BindBluetoothDeviceAdapter(Context context) {
        this.context = context;
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
            result = new EmptyViewHolder(inflater.inflate(R.layout.stub_empty, parent, false));
        } else {
            result = new MyViewHolder(inflater.inflate(R.layout.item_bind_device, parent, false));
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
            switch (list.get(position).getState()) {
                case "0":
                    holder1.tv_obd_state.setText("  已断开连接");
                    Drawable drawable = context.getResources().getDrawable(R.drawable.icon_no);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    holder1.tv_obd_state.setCompoundDrawables(drawable, null, null, null);
                    break;
                case "1":
                    holder1.tv_obd_state.setText("  连接中");
                    Drawable drawable2 = context.getResources().getDrawable(R.drawable.icon_connecting);
                    drawable2.setBounds(0, 0, drawable2.getMinimumWidth(), drawable2.getMinimumHeight());
                    holder1.tv_obd_state.setCompoundDrawables(drawable2, null, null, null);
                    break;
                case "2":
                    if (list.get(position).isConnected()) {
                        holder1.tv_obd_state.setText("  已连接");
                        Drawable drawable1 = context.getResources().getDrawable(R.drawable.icon_ok);
                        drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
                        holder1.tv_obd_state.setCompoundDrawables(drawable1, null, null, null);
                    } else {
                        holder1.tv_obd_state.setText("  未连接");
                        Drawable drawable1 = context.getResources().getDrawable(R.drawable.icon_no);
                        drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
                        holder1.tv_obd_state.setCompoundDrawables(drawable1, null, null, null);
                    }
                    break;
                case "3":
                    Drawable drawable3 = context.getResources().getDrawable(R.drawable.icon_disconnecting);
                    drawable3.setBounds(0, 0, drawable3.getMinimumWidth(), drawable3.getMinimumHeight());
                    holder1.tv_obd_state.setCompoundDrawables(drawable3, null, null, null);
                    holder1.tv_obd_state.setText("  正在断开连接");
                    break;
            }
            holder1.layoutBind.setOnClickListener(v -> clickCallBack.Click(list.get(position)));
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
        private final TextView tv_obd_state;
        private final LinearLayout layoutBind;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tvDeviceName = itemView.findViewById(R.id.tvDeviceName);
            tv_obd_state = itemView.findViewById(R.id.tv_obd_state);
            layoutBind = itemView.findViewById(R.id.layoutBind);
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
