package com.example.obdandroid.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.obdandroid.R;
import com.example.obdandroid.ui.entity.VehicleEntity;
import com.example.obdandroid.utils.BitMapUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 作者：Jealous
 * 日期：2021/1/9 0009
 * 描述：
 */
public class MyVehicleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private final LayoutInflater inflater;
    private List<VehicleEntity.DataEntity.ListEntity> list;
    private OnClickCallBack clickCallBack;
    private final int EMPTY_VIEW = 0;//空页面
    private final int NOT_EMPTY_VIEW = 1;//正常页面
    private int mSelectedPos = 0;   //实现单选，保存当前选中的position
    //这个是checkbox的Hashmap集合
    private HashMap<Integer, Boolean> map;

    public MyVehicleAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setClickCallBack(OnClickCallBack clickCallBack) {
        this.clickCallBack = clickCallBack;
    }

    public void setList(List<VehicleEntity.DataEntity.ListEntity> list) {
        this.list = list;
        map = new HashMap<>();
        for (int i = 0; i < 30; i++) {
            map.put(i, false);
        }
    }

    /**
     * 单选
     *
     * @param postion
     */
    public void singlesel(int postion) {
        Set<Map.Entry<Integer, Boolean>> entries = map.entrySet();
        for (Map.Entry<Integer, Boolean> entry : entries) {
            entry.setValue(false);
        }
        map.put(postion, true);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (EMPTY_VIEW == viewType) {
            return new EmptyViewHolder(inflater.inflate(R.layout.stub_empty, parent, false));
        }
        return new MyViewHolder(inflater.inflate(R.layout.item_vehicle, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        int itemViewType = getItemViewType(position);
        if (EMPTY_VIEW == itemViewType) {
            EmptyViewHolder viewHolder = (EmptyViewHolder) holder;
            viewHolder.mEmptyTextView.setText("暂无数据");
        } else if (NOT_EMPTY_VIEW == itemViewType) {
            final MyViewHolder holder1 = (MyViewHolder) holder;
            holder1.tvAutomobileBrandName.setText(list.get(position).getVehicleName());
            if (TextUtils.isEmpty(list.get(position).getLicensePlateNumber())) {
                holder1.tv_licensePlateNumber.setText("无车牌号");
            } else {
                holder1.tv_licensePlateNumber.setText(list.get(position).getLicensePlateNumber());
            }
            String FuelTypeName = TextUtils.isEmpty(list.get(position).getFuelTypeName()) ? "未知" : list.get(position).getFuelTypeName();
            String EngineDisplacement = TextUtils.isEmpty(list.get(position).getEngineDisplacement()) ? "未知" : list.get(position).getEngineDisplacement();
            holder1.tvFuelTypeName.setText(FuelTypeName + "  " + EngineDisplacement);
            if (list.get(position).getVehicleStatus() == 1) {
                holder1.ivObd.setImageResource(R.drawable.icon_obd_bind_no);
                holder1.tvObd.setTextColor(context.getResources().getColor(R.color.red));
            } else {
                holder1.ivObd.setImageResource(R.drawable.icon_obd_bind);
                holder1.tvObd.setTextColor(context.getResources().getColor(R.color.white));
            }
            holder1.tvObd.setText(list.get(position).getVehicleStatusName());
            holder1.id_cb_vehicleIndex.setText("第" + (position + 1) + "辆车");
            if (TextUtils.isEmpty(list.get(position).getLogo())) {
                holder1.ivLogo.setImageResource(R.drawable.icon_car_def);
            } else {
                holder1.ivLogo.setImageBitmap(BitMapUtils.stringToBitmap(list.get(position).getLogo()));
            }
            holder1.id_cb_vehicleIndex.setChecked(map.get(position));
            holder1.id_cb_vehicleIndex.setOnClickListener(v -> {
                map.put(position, !map.get(position));
                //刷新适配器
                notifyDataSetChanged();
                //单选
                singlesel(position);
                clickCallBack.select(list.get(position));
            });
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
        private final AppCompatCheckBox id_cb_vehicleIndex;
        private final TextView tv_licensePlateNumber;
        private final ImageView ivLogo;
        private final TextView tvAutomobileBrandName;
        private final TextView tvFuelTypeName;
        private final ImageView ivObd;
        private final TextView tvObd;
        private final CardView card_view;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            id_cb_vehicleIndex = itemView.findViewById(R.id.id_cb_vehicleIndex);
            tv_licensePlateNumber = itemView.findViewById(R.id.tv_licensePlateNumber);
            ivLogo = itemView.findViewById(R.id.ivLogo);
            tvAutomobileBrandName = itemView.findViewById(R.id.tvAutomobileBrandName);
            tvFuelTypeName = itemView.findViewById(R.id.tvFuelTypeName);
            ivObd = itemView.findViewById(R.id.ivObd);
            tvObd = itemView.findViewById(R.id.tvObd);
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
        void select(VehicleEntity.DataEntity.ListEntity entity);
    }

    public void addFootItem(List<VehicleEntity.DataEntity.ListEntity> lists) {
        list.addAll(lists);
        notifyDataSetChanged();
    }

}
