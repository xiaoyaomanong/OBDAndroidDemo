package com.example.obdandroid.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.obdandroid.R;
import com.example.obdandroid.ui.view.progressButton.CircularProgressButton;
import com.sohrab.obd.reader.trip.OBDTripEntity;

import java.util.List;

/**
 * 作者：Jealous
 * 日期：2021/1/19 0019
 * 描述：
 */
public class VehicleCheckAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private final LayoutInflater inflater;
    private List<OBDTripEntity> list;
    private OnClickCallBack clickCallBack;
    private final int EMPTY_VIEW = 0;//空页面
    private final int NOT_EMPTY_VIEW = 1;//正常页面
    private String msg;

    public VehicleCheckAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setClickCallBack(OnClickCallBack clickCallBack) {
        this.clickCallBack = clickCallBack;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setList(List<OBDTripEntity> list) {
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (EMPTY_VIEW == viewType) {
            return new EmptyViewHolder(inflater.inflate(R.layout.stub_empty, parent, false));
        }
        return new MyViewHolder(inflater.inflate(R.layout.item_vehicle_check, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        int itemViewType = getItemViewType(position);
        if (EMPTY_VIEW == itemViewType) {
            EmptyViewHolder viewHolder = (EmptyViewHolder) holder;
            //viewHolder.mEmptyTextView.setText("请连接OBD设备,进行检测");
            viewHolder.mEmptyTextView.setText(msg);
        } else if (NOT_EMPTY_VIEW == itemViewType) {
            final MyViewHolder holder1 = (MyViewHolder) holder;
            holder1.tvIndex.setText(position);
            holder1.tvCheckName.setText(list.get(position).getName() + ": " + list.get(position).getValue());
            holder1.btnCheck.setIndeterminateProgressMode(true);
            holder1.btnCheck.setProgress(0);
            new Handler().postDelayed(() -> holder1.btnCheck.setProgress(50), 2000);
            if (list.get(position) == null && TextUtils.isEmpty(list.get(position).getValue())) {
                new Handler().postDelayed(() -> holder1.btnCheck.setProgress(-1), 1000);
            } else {
                new Handler().postDelayed(() -> holder1.btnCheck.setProgress(100), 1000);
            }
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
        private final TextView tvIndex;
        private final TextView tvCheckName;
        private final CircularProgressButton btnCheck;
        private final LinearLayout card_view;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tvIndex = itemView.findViewById(R.id.tvIndex);
            tvCheckName = itemView.findViewById(R.id.tvCheckName);
            btnCheck = itemView.findViewById(R.id.btnCheck);
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
        void Click(OBDTripEntity entity);
    }
}