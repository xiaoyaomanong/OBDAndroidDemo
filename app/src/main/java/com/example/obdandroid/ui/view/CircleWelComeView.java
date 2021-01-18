package com.example.obdandroid.ui.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.obdandroid.R;
import com.example.obdandroid.listener.CallEndListener;

public class CircleWelComeView  extends View {

    /** 扩散圆圈颜色 */
    private int outColor = getResources().getColor(R.color.outColor);
    /** 圆圈中心颜色 */
    private int inColor = getResources().getColor(R.color.inColor);
    /** 中心圆半径 */
    private float radius = 150;
    /** 扩散圆宽度 */
    private int width = 3;
    /** 最大宽度 */
    private Integer maxWidth = 255;
    /** 扩散速度 */
    private int speed = 5;
    /** 是否正在扩散中 */
    private boolean mIsDiffuse = false;
    // 透明度集合
    private List<Integer> mAlphas = new ArrayList<Integer>();
    // 扩散圆半径集合
    private List<Integer> mWidths = new ArrayList<Integer>();
    private Paint mPaint;

    //结束动画监听
    private CallEndListener listener;

    public CircleWelComeView(Context context) {
        this(context, null);
    }

    public CircleWelComeView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public CircleWelComeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleView, defStyleAttr, 0);
        outColor = a.getColor(R.styleable.CircleView_circle_out_color, outColor);
        inColor = a.getColor(R.styleable.CircleView_circle_in_Color, inColor);
        radius = a.getFloat(R.styleable.CircleView_circle_radius, radius);
        width = a.getInt(R.styleable.CircleView_circle_width, width);
        maxWidth = a.getInt(R.styleable.CircleView_max_width, maxWidth);
        speed = a.getInt(R.styleable.CircleView_speed, speed);
        a.recycle();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mAlphas.add(255);
        mWidths.add(0);
    }

    @Override
    public void invalidate() {
        if(hasWindowFocus()){
            super.invalidate();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if(hasWindowFocus){
            invalidate();
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        // 绘制扩散圆
        mPaint.setColor(outColor);
        for (int i = 0; i < mAlphas.size(); i ++) {
            // 设置透明度
            Integer alpha = mAlphas.get(i);
            mPaint.setAlpha(alpha);
            // 绘制扩散圆
            Integer width = mWidths.get(i);
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius + width, mPaint);

            if(alpha > 0 && width < maxWidth){
                mAlphas.set(i, alpha - speed > 0 ? alpha - speed : 1);
                mWidths.set(i, width + speed);
            }
        }
        // 判断当扩散圆扩散到指定宽度时添加新扩散圆
        if (mWidths.get(mWidths.size() - 1) >= maxWidth / width) {
            mAlphas.add(255);
            mWidths.add(0);
        }
        // 超过10个扩散圆，删除最外层
        if(mWidths.size() >= 10){
            mWidths.remove(0);
            mAlphas.remove(0);
        }

        // 绘制中心圆
        mPaint.setAlpha(255);
        mPaint.setColor(inColor);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, mPaint);

        if(mIsDiffuse){
            invalidate();
        }
    }

    /**
     * 开始扩散
     */
    public void start() {
        mIsDiffuse = true;
        invalidate();
    }

    /**
     * 停止扩散
     */
    public void stop() {
        mIsDiffuse = false;
        mWidths.clear();
        mAlphas.clear();
        mAlphas.add(255);
        mWidths.add(0);
        invalidate();
        if(listener!=null){
            listener.doEnd();
        }
    }

    /**
     * 是否扩散中
     */
    public boolean isDiffuse(){
        return mIsDiffuse;
    }

    /**
     * 设置扩散圆颜色
     */
    public void setOutColor(int colorId){
        this.outColor = colorId;
    }

    /**
     * 设置中心圆颜色
     */
    public void setInColor(int colorId){
        this.inColor = colorId;
    }

    /**
     * 设置中心圆半径
     */
    public void setRadius(int radius){
        this.radius = radius;
    }

    /**
     * 设置扩散圆宽度(值越小宽度越大)
     */
    public void setWidth(int width){
        this.width = width;
    }

    /**
     * 设置最大宽度
     */
    public void setMaxWidth(int maxWidth){
        this.maxWidth = maxWidth;
    }

    /**
     * 设置扩散速度，值越大速度越快
     */
    public void setSpeed(int speed){
        this.speed = speed;
    }

    /**
     * 设置动画结束监听
     */
    public void setCallEndListener(CallEndListener listener){
        this.listener = listener;
    }
}

