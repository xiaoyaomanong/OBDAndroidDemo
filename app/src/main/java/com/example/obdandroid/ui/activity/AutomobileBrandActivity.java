package com.example.obdandroid.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseActivity;
import com.example.obdandroid.ui.adapter.commonAdapter.CommonRecycleAdapter;
import com.example.obdandroid.ui.adapter.commonAdapter.CommonViewHolder;
import com.example.obdandroid.ui.adapter.commonAdapter.DefaultItemDecoration;
import com.example.obdandroid.ui.adapter.commonAdapter.ItemClickListener;
import com.example.obdandroid.ui.entity.AutomobileBrandEntity;
import com.example.obdandroid.ui.entity.BrandPinYinEntity;
import com.example.obdandroid.ui.view.LetterSideBarView;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

import static com.example.obdandroid.config.APIConfig.SERVER_URL;
import static com.example.obdandroid.config.APIConfig.getAutomobileBrandList_URL;

/**
 * 作者：Jealous
 * 日期：2021/1/11 0011
 * 描述：
 */
public class AutomobileBrandActivity extends BaseActivity {
    private RecyclerView recycleBrand;
    private TextView indexTv;
    private final List<BrandPinYinEntity> mList = new ArrayList<>();
    private final Handler mHandler = new Handler();
    private boolean isScale = false;
    private Context context;
    private AutomobileBrandAdapter adapter;
    private static final int COMPLETED = 0;
    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == COMPLETED) {
                //排序
                Collections.sort(mList);
                adapter = new AutomobileBrandAdapter(context, mList, R.layout.person_recycler_item);
                recycleBrand.setAdapter(adapter);
                recycleBrand.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                recycleBrand.addItemDecoration(new DefaultItemDecoration(context, R.drawable.default_item));
                adapter.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Intent intent = new Intent();
                        intent.putExtra("automobileBrand", mList.get(position));
                        setResult(101, intent);
                        finish();
                    }
                });
            }
        }
    };

    @Override
    protected int getContentViewId() {
        return R.layout.activity_automobile_brand;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    public void initView() {
        super.initView();
        context = this;
        recycleBrand = findViewById(R.id.recycle_Brand);
        LetterSideBarView letterSideBarView = findViewById(R.id.letterSideBarView);
        indexTv = findViewById(R.id.indexTv);
        TitleBar titleBarSet = findViewById(R.id.titleBarSet);
        titleBarSet.setOnTitleBarListener(new OnTitleBarListener() {
            @Override
            public void onLeftClick(View v) {
                finish();
            }

            @Override
            public void onTitleClick(View v) {

            }

            @Override
            public void onRightClick(View v) {

            }
        });
        getAutomobileBrandList(getToken());
        letterSideBarView.setOnSideBarTouchListener((letter, isTouch) -> {
            for (int i = 0; i < mList.size(); i++) {
                if (letter.equals(mList.get(i).getPinyin().charAt(0) + "")) {
                    recycleBrand.scrollToPosition(i);
                    break;
                }
            }
            showCurrentIndex(letter);
        });
    }


    private void getAutomobileBrandList(String token) {
        OkHttpUtils.get().
                url(SERVER_URL + getAutomobileBrandList_URL).
                addParam("token", token).
                build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                AutomobileBrandEntity entity = JSON.parseObject(response, AutomobileBrandEntity.class);
                if (entity.isSuccess()) {
                    initRv(entity.getData());
                } else {
                    dialogError(context, entity.getMessage());
                }
            }
        });
    }


    private void showCurrentIndex(String letter) {
        indexTv.setText(letter);
        if (!isScale) {
            isScale = true;
            ViewCompat.animate(indexTv)
                    .scaleX(1f)
                    .setInterpolator(new OvershootInterpolator())
                    .setDuration(380)
                    .start();
            ViewCompat.animate(indexTv)
                    .scaleY(1f)
                    .setInterpolator(new OvershootInterpolator())
                    .setDuration(380)
                    .start();
        }

        mHandler.removeCallbacksAndMessages(null);
        // 延时隐藏
        mHandler.postDelayed(() -> {
            ViewCompat.animate(indexTv)
                    .scaleX(0f)
                    .setDuration(380)
                    .start();
            ViewCompat.animate(indexTv)
                    .scaleY(0f)
                    .setDuration(380)
                    .start();
            isScale = false;
        }, 380);
    }

    private void initRv(List<AutomobileBrandEntity.DataEntity> list) {
        new Thread(() -> {
            for (int i = 0; i < list.size(); i++) {
                BrandPinYinEntity person = new BrandPinYinEntity(list.get(i).getName(), list.get(i).getAutomobileBrandId(), list.get(i).getLogo(), list.get(i).getBrandType());
                mList.add(person);
            }
            //处理完成后给handler发送消息
            Message msg = new Message();
            msg.what = COMPLETED;
            handler.sendMessage(msg);
        }).start();
    }

    private class AutomobileBrandAdapter extends CommonRecycleAdapter<BrandPinYinEntity> {
        public AutomobileBrandAdapter(Context context, List<BrandPinYinEntity> mData, int layoutId) {
            super(context, mData, layoutId);
        }

        @Override
        protected void convert(CommonViewHolder holder, BrandPinYinEntity entity, int position) {
            if (entity.getPinyin().length() != 0) {
                String currentWord = entity.getPinyin().charAt(0) + "";
                if (position > 0) {
                    if (mList.get(position - 1).getPinyin().length() != 0) {
                        String lastWord = mList.get(position - 1).getPinyin().charAt(0) + "";
                        holder.setVisibility(R.id.indexTv, currentWord.equals(lastWord) ? View.GONE : View.VISIBLE);
                    }
                } else {
                    holder.setVisibility(R.id.indexTv, View.VISIBLE);
                }
                holder.setText(R.id.indexTv, currentWord);
                holder.setText(R.id.userNameTv, entity.getName());
                if (TextUtils.isEmpty(entity.getLogo())) {
                    holder.setImageResoucrce(R.id.iv_car_lable, R.drawable.icon_car_lable);
                } else {
                    holder.setImageBitMap(R.id.iv_car_lable, stringToBitmap(entity.getLogo()));
                }
            }
        }
    }

    /**
     * @param string Base64图片
     * @return Base64转换图片
     */
    public static Bitmap stringToBitmap(String string) {
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray = Base64.decode(string.split(",")[1], Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}