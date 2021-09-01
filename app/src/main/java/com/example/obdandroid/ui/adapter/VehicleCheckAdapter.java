package com.example.obdandroid.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.obdandroid.R;
import com.sohrab.obd.reader.trip.OBDTripEntity;

import java.util.List;

/**
 * 作者：Jealous
 * 日期：2021/1/19 0019
 * 描述：
 */
public class VehicleCheckAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final LayoutInflater inflater;
    private List<OBDTripEntity> list;

    public VehicleCheckAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    public void setList(List<OBDTripEntity> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(inflater.inflate(R.layout.item_vehicle_check, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final MyViewHolder holder1 = (MyViewHolder) holder;
        holder1.tvIndex.setText(String.valueOf(position));
        holder1.tvCheckName.setText(list.get(position).getName() + ": " + list.get(position).getValue());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    static class MyViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        private final TextView tvIndex;
        private final TextView tvCheckName;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tvIndex = itemView.findViewById(R.id.tvIndex);
            tvCheckName = itemView.findViewById(R.id.tvCheckName);
        }
    }
}