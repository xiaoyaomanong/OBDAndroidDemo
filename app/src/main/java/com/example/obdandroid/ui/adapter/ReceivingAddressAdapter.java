package com.example.obdandroid.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.obdandroid.R;
import com.example.obdandroid.ui.entity.AppUserAddressEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 作者：Jealous
 * 日期：2021/9/2 0002
 * 描述：
 */
public class ReceivingAddressAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final LayoutInflater inflater;
    private List<AppUserAddressEntity.DataEntity.ListEntity> list;
    private OnClickCallBack clickCallBack;
    private final int EMPTY_VIEW = 0;//空页面
    private final int NOT_EMPTY_VIEW = 1;//正常页面
    private HashMap<Integer, Boolean> map;

    public ReceivingAddressAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    public HashMap<Integer, Boolean> getMap() {
        return map;
    }

    public void setList(List<AppUserAddressEntity.DataEntity.ListEntity> list) {
        this.list = list;
        map = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            map.put(i, list.get(i).isDefault());
        }
    }

    public void setClickCallBack(OnClickCallBack clickCallBack) {
        this.clickCallBack = clickCallBack;
    }

    /**
     * 单选
     *
     * @param postion 下标
     */
    public void singlesel(int postion) {
        Set<Map.Entry<Integer, Boolean>> entries = map.entrySet();
        for (Map.Entry<Integer, Boolean> entry : entries) {
            entry.setValue(false);
        }
        map.put(postion, true);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (EMPTY_VIEW == viewType) {
            return new EmptyViewHolder(inflater.inflate(R.layout.stub_empty, parent, false));
        }
        return new MyViewHolder(inflater.inflate(R.layout.item_address, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        int itemViewType = getItemViewType(position);
        if (EMPTY_VIEW == itemViewType) {
            EmptyViewHolder holder = (EmptyViewHolder) viewHolder;
            holder.mEmptyTextView.setText("暂无数据");
        } else if (NOT_EMPTY_VIEW == itemViewType) {
            final MyViewHolder holder = (MyViewHolder) viewHolder;
            holder.tvName.setText(list.get(position).getContacts() + "  " + list.get(position).getTelephone());
            holder.tvAddress.setText(list.get(position).getAddress());
            holder.ivChange.setOnClickListener(v -> clickCallBack.update(list.get(position)));
            holder.tvDelete.setOnClickListener(v -> clickCallBack.delete(list.get(position)));
            holder.cb_select_default.setChecked(map.get(position));
            holder.cb_select_default.setOnClickListener(v -> clickCallBack.setDefault(list.get(position), position));
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
        private final TextView tvName;
        private final TextView tvAddress;
        private final ImageView ivChange;
        private final CheckBox cb_select_default;
        private final TextView tvDelete;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tvName = itemView.findViewById(R.id.tvName);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            ivChange = itemView.findViewById(R.id.ivChange);
            cb_select_default = itemView.findViewById(R.id.cb_select_default);
            tvDelete = itemView.findViewById(R.id.tvDelete);
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
        void update(AppUserAddressEntity.DataEntity.ListEntity entity);

        void delete(AppUserAddressEntity.DataEntity.ListEntity entity);

        void setDefault(AppUserAddressEntity.DataEntity.ListEntity entity, int position);
    }
}