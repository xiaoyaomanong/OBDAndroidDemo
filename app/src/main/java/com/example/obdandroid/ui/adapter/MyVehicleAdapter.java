package com.example.obdandroid.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.obdandroid.R;
import com.example.obdandroid.ui.entity.VehicleEntity;
import com.example.obdandroid.utils.SPUtil;
import com.example.obdandroid.utils.StringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.example.obdandroid.config.APIConfig.SERVER_URL;
import static com.example.obdandroid.config.Constant.VEHICLE_ID;

/**
 * 作者：Jealous
 * 日期：2021/1/9 0009
 * 描述：
 */
public class MyVehicleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private final LayoutInflater inflater;
    private List<VehicleEntity.DataEntity.ListEntity> list;
    private OnClickCallBack clickCallBack;
    private final int EMPTY_VIEW = 0;//空页面
    private final int NOT_EMPTY_VIEW = 1;//正常页面
    private HashMap<Integer, Boolean> map;
    private final String vehicleId;

    public MyVehicleAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        SPUtil spUtil = new SPUtil(context);
        vehicleId = spUtil.getString(VEHICLE_ID, "");
    }

    public void setClickCallBack(OnClickCallBack clickCallBack) {
        this.clickCallBack = clickCallBack;
    }

    public HashMap<Integer, Boolean> getMap() {
        return map;
    }

    public void setList(List<VehicleEntity.DataEntity.ListEntity> list) {
        this.list = list;
        map = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            map.put(i, vehicleId.equals(String.valueOf(list.get(i).getVehicleId())));
        }
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
            return new EmptyViewHolder(inflater.inflate(R.layout.item_empty, parent, false));
        }
        return new MyViewHolder(inflater.inflate(R.layout.item_vehicle, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, @SuppressLint("RecyclerView") int position) {
        int itemViewType = getItemViewType(position);
        if (EMPTY_VIEW == itemViewType) {
            EmptyViewHolder holder = (EmptyViewHolder) viewHolder;
            holder.mEmptyTextView.setText("暂无数据");
        } else if (NOT_EMPTY_VIEW == itemViewType) {
            final MyViewHolder holder = (MyViewHolder) viewHolder;
            holder.tvAutomobileBrandName.setText(list.get(position).getVehicleName());
            holder.tvModelName.setText(list.get(position).getModelName());
            if (StringUtil.isNull(list.get(position).getBluetoothDeviceNumber())) {//车辆状态 1 未绑定 2 已绑定 ,
                holder.tvOBDState.setText(" 未绑定");
                Drawable drawable = context.getResources().getDrawable(R.drawable.icon_no);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                holder.tvOBDState.setCompoundDrawables(drawable, null, null, null);
            } else {
                holder.tvOBDState.setText(" 已绑定");
                Drawable drawable = context.getResources().getDrawable(R.drawable.icon_ok);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                holder.tvOBDState.setCompoundDrawables(drawable, null, null, null);
            }
            if (TextUtils.isEmpty(list.get(position).getLogo())) {
                holder.ivCarLogo.setImageResource(R.drawable.icon_car_def);
            } else {
                Glide.with(context).load(SERVER_URL+list.get(position).getLogo()).into(holder.ivCarLogo);
            }
            holder.id_cb_vehicleIndex.setChecked(map.get(position));
            holder.id_cb_vehicleIndex.setOnClickListener(v -> clickCallBack.select(list.get(position),position));
            holder.card_view.setOnClickListener(v -> clickCallBack.click(list.get(position)));
            holder.card_view.setOnLongClickListener(v -> {
                clickCallBack.delete(list.get(position));
                return true;
            });
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
        private final AppCompatCheckBox id_cb_vehicleIndex;
        private final ImageView ivCarLogo;
        private final TextView tvAutomobileBrandName;
        private final TextView tvModelName;
        private final TextView tvOBDState;
        private final LinearLayout card_view;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            id_cb_vehicleIndex = itemView.findViewById(R.id.id_cb_vehicleIndex);
            ivCarLogo = itemView.findViewById(R.id.ivCarLogo);
            tvAutomobileBrandName = itemView.findViewById(R.id.tvAutomobileBrandName);
            tvModelName = itemView.findViewById(R.id.tvModelName);
            tvOBDState = itemView.findViewById(R.id.tvOBDState);
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
        void click(VehicleEntity.DataEntity.ListEntity entity);

        void select(VehicleEntity.DataEntity.ListEntity entity,int position);

        void delete(VehicleEntity.DataEntity.ListEntity entity);
    }

    public void addFootItem(List<VehicleEntity.DataEntity.ListEntity> lists) {
        list.addAll(lists);
        notifyDataSetChanged();
    }
}
