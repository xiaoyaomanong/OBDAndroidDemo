package com.example.obdandroid.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.obdandroid.R;
import com.example.obdandroid.ui.entity.TestRecordEntity;
import com.example.obdandroid.ui.entity.VehicleEntity;
import com.example.obdandroid.utils.DensityUtil;
import com.sohrab.obd.reader.trip.OBDJsonTripEntity;

import java.util.Arrays;
import java.util.List;

/**
 * 作者：Jealous
 * 日期：2021/1/29 0029
 * 描述：
 */
public class CheckRecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<TestRecordEntity.DataEntity.ListEntity> list;
    private final LayoutInflater inflater;
    private final int EMPTY_VIEW = 0;//空页面
    private final int NOT_EMPTY_VIEW = 1;//正常页面
    private OnClickCallBack clickCallBack;
    private Context context;
    private String token;

    public CheckRecordAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setClickCallBack(OnClickCallBack clickCallBack) {
        this.clickCallBack = clickCallBack;
    }

    public void setList(List<TestRecordEntity.DataEntity.ListEntity> list) {
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (EMPTY_VIEW == viewType) {
            return new EmptyViewHolder(inflater.inflate(R.layout.stub_empty, parent, false));
        }
        return new MyViewHolder(inflater.inflate(R.layout.item_test_record, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        int itemViewType = getItemViewType(position);
        if (EMPTY_VIEW == itemViewType) {
            EmptyViewHolder viewHolder = (EmptyViewHolder) holder;
            viewHolder.mEmptyTextView.setText("绑定OBD设备,开始检测");
        } else if (NOT_EMPTY_VIEW == itemViewType) {
            final MyViewHolder holder1 = (MyViewHolder) holder;
            holder1.setPosition(position);
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

    class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtDateTime;
        private final TextView txt_date_size;
        private final RelativeLayout rlTitle;
        private final RelativeLayout card_view;
        private final RecyclerView recycleContent;
        private final View vLine;

        public MyViewHolder(View itemView) {
            super(itemView);
            rlTitle = itemView.findViewById(R.id.rl_title);
            vLine = itemView.findViewById(R.id.v_line);
            card_view = itemView.findViewById(R.id.card_view);
            txtDateTime = itemView.findViewById(R.id.txt_date_time);
            txt_date_size = itemView.findViewById(R.id.txt_date_size);
            recycleContent = itemView.findViewById(R.id.recycleContent);
        }

        @SuppressLint("SetTextI18n")
        public void setPosition(int position) {
            TestRecordEntity.DataEntity.ListEntity timeData = list.get(position);
            OBDJsonTripEntity tripEntity = JSON.parseObject(timeData.getTestData(), OBDJsonTripEntity.class);
            int size;
            if (!TextUtils.isEmpty(tripEntity.getFaultCodes())) {
                size = tripEntity.getFaultCodes().replaceAll("\r|\n", ",").split(",").length;
            } else {
                size = 0;
            }
            //时间轴竖线的layoutParams,用来动态的添加竖线
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) vLine.getLayoutParams();
            //position等于0的处理
            if (position == 0) {
                //数据个数必须大于等于2，要不对于时间的判断会有越界危险
                layoutParams.setMargins(DensityUtil.dip2px(vLine.getContext(), 20), DensityUtil.dip2px(vLine.getContext(), 15), 0, 0);
                rlTitle.setVisibility(View.VISIBLE);
                txtDateTime.setText(timeData.getDetectionTime());
                if (size > 0) {
                    txt_date_size.setText("发现" + size + "个故障码");
                } else {
                    txt_date_size.setText("未发现故障码");
                    txt_date_size.setTextColor(context.getResources().getColor(R.color.gray));
                }
                layoutParams.addRule(RelativeLayout.ALIGN_TOP, R.id.rl_title);
                layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.recycleContent);
            } else if (position < list.size() - 1) {
                if (timeData.getDetectionTime().equals(list.get(position - 1).getDetectionTime())) {
                    if (timeData.getDetectionTime().equals(list.get(position + 1).getDetectionTime())) {
                        rlTitle.setVisibility(View.GONE);
                        layoutParams.setMargins(DensityUtil.dip2px(vLine.getContext(), 20), DensityUtil.dip2px(vLine.getContext(), 0), 0, 0);
                        layoutParams.addRule(RelativeLayout.ALIGN_TOP, R.id.recycleContent);
                        layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.recycleContent);
                    } else {
                        rlTitle.setVisibility(View.GONE);
                        layoutParams.addRule(RelativeLayout.ALIGN_TOP, R.id.recycleContent);
                        layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.recycleContent);
                    }
                } else {
                    layoutParams.setMargins(DensityUtil.dip2px(vLine.getContext(), 20), DensityUtil.dip2px(vLine.getContext(), 0), 0, 0);
                    rlTitle.setVisibility(View.VISIBLE);
                    txtDateTime.setText(timeData.getDetectionTime());
                    if (size > 0) {
                        txt_date_size.setText("发现" + size + "个故障码");
                    } else {
                        txt_date_size.setText("未发现故障码");
                        txt_date_size.setTextColor(context.getResources().getColor(R.color.gray));
                    }
                    layoutParams.addRule(RelativeLayout.ALIGN_TOP, R.id.rl_title);
                    layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.recycleContent);
                }
            } else {
                if (!timeData.getDetectionTime().equals(list.get(position - 1).getDetectionTime())) {
                    rlTitle.setVisibility(View.VISIBLE);
                    txtDateTime.setText(timeData.getDetectionTime());
                    if (size > 0) {
                        txt_date_size.setText("发现" + size + "个故障码");
                    } else {
                        txt_date_size.setText("未发现故障码");
                        txt_date_size.setTextColor(context.getResources().getColor(R.color.gray));
                    }
                    layoutParams.setMargins(DensityUtil.dip2px(vLine.getContext(), 20), DensityUtil.dip2px(vLine.getContext(), 0), 0, 0);
                    layoutParams.addRule(RelativeLayout.ALIGN_TOP, R.id.rl_title);
                } else {
                    rlTitle.setVisibility(View.GONE);
                    txtDateTime.setText(timeData.getDetectionTime());
                    if (size > 0) {
                        txt_date_size.setText("发现" + size + "个故障码");
                    } else {
                        txt_date_size.setText("未发现故障码");
                        txt_date_size.setTextColor(context.getResources().getColor(R.color.gray));
                    }
                    layoutParams.setMargins(DensityUtil.dip2px(vLine.getContext(), 20), DensityUtil.dip2px(vLine.getContext(), 0), 0, 0);
                    layoutParams.addRule(RelativeLayout.ALIGN_TOP, R.id.recycleContent);
                }
                layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.recycleContent);
            }
            vLine.setLayoutParams(layoutParams);
            rlTitle.setOnClickListener(v -> clickCallBack.click(list.get(position)));
            LinearLayoutManager manager = new LinearLayoutManager(context);
            manager.setOrientation(OrientationHelper.VERTICAL);
            recycleContent.setLayoutManager(manager);
            CheckRecorderAdapter adapter = new CheckRecorderAdapter(context);
            adapter.setList(Arrays.asList(tripEntity.getFaultCodes().replaceAll("\r|\n", ",").split(",")));
            adapter.setToken(token);
            recycleContent.setAdapter(adapter);
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
        void click(TestRecordEntity.DataEntity.ListEntity entity);
    }

    public void addFootItem(List<TestRecordEntity.DataEntity.ListEntity> lists) {
        list.addAll(lists);
        notifyDataSetChanged();
    }
}