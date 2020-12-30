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
import com.example.obdandroid.ui.entity.OBDProtocolEntity;

import java.util.ArrayList;

/**
 * 作者：Jealous
 * 日期：2020/12/30 0030
 * 描述：
 */
public class OBDProtocolAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<OBDProtocolEntity> list;
    private OnSelectClick click;

    public OBDProtocolAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setList(ArrayList<OBDProtocolEntity> list) {
        this.list = list;
    }

    public void setClick(OnSelectClick click) {
        this.click = click;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(inflater.inflate(R.layout.item_obd_protocol, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        MyViewHolder holder = (MyViewHolder) viewHolder;
        holder.tvCommandName.setText(list.get(position).getName());
        if (list.get(position).isChecked()) {
            holder.check_box.setImageResource(R.drawable.rb_check_ok);
        } else {
            holder.check_box.setImageResource(R.drawable.rb_check_no);
        }
        holder.layoutSelect.setOnClickListener(v -> {
            setRadioIsChecked(position);
            if (click != null) {
                click.click(list.get(position).getName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void setRadioIsChecked(int position) {
        for (int i = 0; i < list.size(); i++) {
            if (i == position) {
                list.get(i).setChecked(true);
            } else {
                list.get(i).setChecked(false);
            }
        }
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCommandName;
        private ImageView check_box;
        private LinearLayout layoutSelect;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCommandName = itemView.findViewById(R.id.tvCommandName);
            layoutSelect = itemView.findViewById(R.id.layoutSelect);
            check_box = itemView.findViewById(R.id.check_box);
        }
    }

    public interface OnSelectClick {
        void click(String protocol);
    }
}