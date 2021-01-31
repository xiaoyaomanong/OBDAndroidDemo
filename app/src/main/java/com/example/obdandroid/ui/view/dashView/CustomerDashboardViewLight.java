package com.example.obdandroid.ui.view.dashView;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.example.obdandroid.R;
import com.example.obdandroid.config.TAG;

import java.io.File;

/**
 * DashboardView style 4，仿汽车速度仪表盘
 * Created by woxingxiao on 2016-12-19.
 */
public class CustomerDashboardViewLight extends View {
    private Context mContext;
    private int mRadius; // 扇形半径
    private int mStartAngle = 150; // 起始角度
    private int mSweepAngle = 240; // 绘制角度
    private int mMin = 0; // 最小值
    private int mMax = 240; // 最大值
    private int mSection = 8; // 值域（mMax-mMin）等分份数
    private String mHeaderText = "km/h"; // 表头
    private float mVelocity = 0; // 实时速度
    private int mStrokeWidth; // 画笔宽度
    private int mLength1; // 长刻度的相对圆弧的长度
    private int mLength2; // 刻度读数顶部的相对圆弧的长度
    private int mPLRadius; // 指针长半径
    private int mPSRadius; // 指针短半径

    private int mPadding;
    private int backgroudColor = R.color.color_2C2B30;
    private float mCenterX, mCenterY; // 圆心坐标
    private Paint mPaint;
    private RectF mRectFArc;
    private RectF mRectFInnerArc;
    private Rect mRectText;
    private String[] mTexts;
    private int[] mColors;
    private static final int DEFAULT_COLOR_LOWER = Color.parseColor("#1d953f");
    private static final int DEFAULT_COLOR_MIDDLE = Color.parseColor("#228fbd");
    private static final int DEFAULT_TEXT_SIZE_DIAL = 11;
    private static final int DEFAULT_STROKE_WIDTH = 8;
    private static final int DEFAULT_RADIUS_DIAL = 15;
    private static final int DEAFAULT_TITLE_SIZE = 16;
    private static final int DEFAULT_VALUE_SIZE = 28;
    private static final int DEFAULT_ANIM_PLAY_TIME = 2000;
    private static final int DEFAULT_COLOR_HIGH = Color.RED;
    private static final int DEAFAULT_COLOR_TITLE = Color.BLACK;

    public CustomerDashboardViewLight(Context context) {
        this(context, null);
    }

    public CustomerDashboardViewLight(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomerDashboardViewLight(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext=context;
        initSizes();
    }

    private void initSizes() {
        mStrokeWidth = dp2px(1);
        mLength1 = dp2px(8) + mStrokeWidth;
        mLength2 = mLength1 + dp2px(2);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mRectFArc = new RectF();
        mRectFInnerArc = new RectF();
        mRectText = new Rect();

        mTexts = new String[getmSection() + 1]; // 需要显示mSection + 1个刻度读数
        for (int i = 0; i < mTexts.length; i++) {
            int n = (getmMax() - getmMin()) / getmSection();
            mTexts[i] = String.valueOf(getmMin() + i * n);
        }

        mColors = new int[]{ContextCompat.getColor(getContext(), R.color.color_green),
                ContextCompat.getColor(getContext(), R.color.color_yellow),
                ContextCompat.getColor(getContext(), R.color.color_red)};
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mPadding = Math.max(
                Math.max(getPaddingLeft(), getPaddingTop()),
                Math.max(getPaddingRight(), getPaddingBottom())
        );
        setPadding(mPadding, mPadding, mPadding, mPadding);

        int width = resolveSize(dp2px(260), widthMeasureSpec);
        mRadius = (width - mPadding * 2 - mStrokeWidth * 2) / 2;

        // 由起始角度确定的高度
        float[] point1 = getCoordinatePoint(mRadius, mStartAngle);
        // 由结束角度确定的高度
        float[] point2 = getCoordinatePoint(mRadius, mStartAngle + mSweepAngle);
        int height = (int) Math.max(point1[1] + mRadius + mStrokeWidth * 2,
                point2[1] + mRadius + mStrokeWidth * 2);

        setMeasuredDimension(width, width);

        mCenterX = mCenterY = getMeasuredWidth() / 2f;
        mRectFArc.set(
                getPaddingLeft() + mStrokeWidth,
                getPaddingTop() + mStrokeWidth,
                getMeasuredWidth() - getPaddingRight() - mStrokeWidth,
                getMeasuredWidth() - getPaddingBottom() - mStrokeWidth
        );

        mPaint.setTextSize(sp2px(12));
        mPaint.getTextBounds("0", 0, "0".length(), mRectText);
        mRectFInnerArc.set(
                getPaddingLeft() + mLength2 + mRectText.height() + dp2px(30),
                getPaddingTop() + mLength2 + mRectText.height() + dp2px(30),
                getMeasuredWidth() - getPaddingRight() - mLength2 - mRectText.height() - dp2px(30),
                getMeasuredWidth() - getPaddingBottom() - mLength2 - mRectText.height() - dp2px(30)
        );

        mPLRadius = mRadius - dp2px(30);
        mPSRadius = dp2px(25);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //canvas.drawColor(ContextCompat.getColor(getContext(), R.color.cpb_blue_dark));
        /**
         * 画圆弧
         */
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setColor(DEFAULT_COLOR_LOWER);
        canvas.drawArc(mRectFArc, 135, 54, false, mPaint);
        mPaint.setColor(DEFAULT_COLOR_MIDDLE);
        canvas.drawArc(mRectFArc, 189, 162, false, mPaint);
        mPaint.setColor(DEFAULT_COLOR_HIGH);
        canvas.drawArc(mRectFArc, 351, 54, false, mPaint);

        /**
         * 画长刻度
         * 画好起始角度的一条刻度后通过canvas绕着原点旋转来画剩下的长刻度
         */
        double cos = Math.cos(Math.toRadians(mStartAngle - 180));
        double sin = Math.sin(Math.toRadians(mStartAngle - 180));
        float x0 = (float) (mPadding + mStrokeWidth + mRadius * (1 - cos));
        float y0 = (float) (mPadding + mStrokeWidth + mRadius * (1 - sin));
        float x1 = (float) (mPadding + mStrokeWidth + mRadius - (mRadius - mLength1) * cos);
        float y1 = (float) (mPadding + mStrokeWidth + mRadius - (mRadius - mLength1) * sin);

        canvas.save();
        canvas.drawLine(x0, y0, x1, y1, mPaint);
        float angle = mSweepAngle * 1f / getmSection();
        for (int i = 0; i < getmSection(); i++) {
            canvas.rotate(angle, mCenterX, mCenterY);
            if (i <= 3) {
                mPaint.setColor(DEFAULT_COLOR_LOWER);
            } else if (i <= 8) {
                mPaint.setColor(DEFAULT_COLOR_MIDDLE);
            } else {
                mPaint.setColor(DEFAULT_COLOR_MIDDLE);
            }
            canvas.drawLine(x0, y0, x1, y1, mPaint);

        }
        canvas.restore();

        /**
         * 画长刻度读数
         */
        mPaint.setTextSize(sp2px(12));
        mPaint.setStyle(Paint.Style.FILL);
        float α;
        float[] p;
        angle = mSweepAngle * 1f / mSection;
        for (int i = 0; i <= mSection; i++) {
            α = mStartAngle + angle * i;
            p = getCoordinatePoint(mRadius - mLength2, α);
            if (α % 360 > 135 && α % 360 < 225) {
                mPaint.setTextAlign(Paint.Align.LEFT);
            } else if ((α % 360 >= 0 && α % 360 < 45) || (α % 360 > 315 && α % 360 <= 360)) {
                mPaint.setTextAlign(Paint.Align.RIGHT);
            } else {
                mPaint.setTextAlign(Paint.Align.CENTER);
            }
            mPaint.getTextBounds(getmHeaderText(), 0, mTexts[i].length(), mRectText);
            int txtH = mRectText.height();
            if (i <= 1 || i >= getmSection() - 1) {
                canvas.drawText(mTexts[i], p[0], p[1] + txtH / 2, mPaint);
            } else if (i == 3) {
                canvas.drawText(mTexts[i], p[0] + txtH / 2, p[1] + txtH, mPaint);
            } else if (i == getmSection() - 3) {
                canvas.drawText(mTexts[i], p[0] - txtH / 2, p[1] + txtH, mPaint);
            } else {
                canvas.drawText(mTexts[i], p[0], p[1] + txtH, mPaint);
            }
        }

        mPaint.setStrokeCap(Paint.Cap.SQUARE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(dp2px(5));
        mPaint.setShader(generateSweepGradient());
        canvas.drawArc(mRectFInnerArc, mStartAngle + 1, mSweepAngle - 2, false, mPaint);

        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setShader(null);

        /**
         * 画表头
         * 没有表头就不画
         */
        if (!TextUtils.isEmpty(getmHeaderText())) {
            mPaint.setTextSize(sp2px(9));
            mPaint.setTextAlign(Paint.Align.CENTER);
            mPaint.setColor(DEFAULT_COLOR_HIGH);
            mPaint.getTextBounds(getmHeaderText(), 0, getmHeaderText().length(), mRectText);
            canvas.drawText(getmHeaderText(), mCenterX, mCenterY - mRectText.height() * 2, mPaint);
        }

        /**
         * 画指针
         */
        float θ = mStartAngle + mSweepAngle * (mVelocity - getmMin()) / (getmMax() - getmMin()); // 指针与水平线夹角
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.color_dark_light));
        int r = mRadius / 15;
        canvas.drawCircle(mCenterX, mCenterY, r, mPaint);
        mPaint.setStrokeWidth(3);
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.color_light));
        float[] p1 = getCoordinatePoint(mPLRadius, θ);
        canvas.drawLine(p1[0], p1[1], mCenterX, mCenterY, mPaint);
        float[] p2 = getCoordinatePoint(mPSRadius, θ + 180);
        canvas.drawLine(mCenterX, mCenterY, p2[0], p2[1], mPaint);

        /**
         * 画实时度数值
         */
        mPaint.setStrokeWidth(dp2px(2));
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        String value = String.valueOf(mVelocity);
        mPaint.getTextBounds(value, 0, value.length(), mRectText);
        mPaint.setTextSize(sp2px(25));
        String file = "fonts" + File.separator + "digital-7.ttf";
        AssetManager assets = mContext.getAssets();
        Typeface font = Typeface.createFromAsset(assets, file);
        mPaint.setTypeface(font);
        canvas.drawText(value, mCenterX, mCenterY + mPSRadius + mRectText.height() * 5, mPaint);
    }

    /**
     * 数码管样式
     */
    private void drawDigitalTube(Canvas canvas, int num, int xOffset) {
        float x = mCenterX + xOffset;
        float y = mCenterY + dp2px(40);
        int lx = dp2px(5);
        int ly = dp2px(10);
        int gap = dp2px(2);

        // 1
        mPaint.setAlpha(num == -1 || num == 1 || num == 4 ? 25 : 255);
        canvas.drawLine(x - lx, y, x + lx, y, mPaint);
        // 2
        mPaint.setAlpha(num == -1 || num == 1 || num == 2 || num == 3 || num == 7 ? 25 : 255);
        canvas.drawLine(x - lx - gap, y + gap, x - lx - gap, y + gap + ly, mPaint);
        // 3
        mPaint.setAlpha(num == -1 || num == 5 || num == 6 ? 25 : 255);
        canvas.drawLine(x + lx + gap, y + gap, x + lx + gap, y + gap + ly, mPaint);
        // 4
        mPaint.setAlpha(num == -1 || num == 0 || num == 1 || num == 7 ? 25 : 255);
        canvas.drawLine(x - lx, y + gap * 2 + ly, x + lx, y + gap * 2 + ly, mPaint);
        // 5
        mPaint.setAlpha(num == -1 || num == 1 || num == 3 || num == 4 || num == 5 || num == 7
                || num == 9 ? 25 : 255);
        canvas.drawLine(x - lx - gap, y + gap * 3 + ly,
                x - lx - gap, y + gap * 3 + ly * 2, mPaint);
        // 6
        mPaint.setAlpha(num == -1 || num == 2 ? 25 : 255);
        canvas.drawLine(x + lx + gap, y + gap * 3 + ly,
                x + lx + gap, y + gap * 3 + ly * 2, mPaint);
        // 7
        mPaint.setAlpha(num == -1 || num == 1 || num == 4 || num == 7 ? 25 : 255);
        canvas.drawLine(x - lx, y + gap * 4 + ly * 2, x + lx, y + gap * 4 + ly * 2, mPaint);
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                Resources.getSystem().getDisplayMetrics());
    }

    private int sp2px(int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                Resources.getSystem().getDisplayMetrics());
    }

    public float[] getCoordinatePoint(int radius, float angle) {
        float[] point = new float[2];

        double arcAngle = Math.toRadians(angle); //将角度转换为弧度
        if (angle < 90) {
            point[0] = (float) (mCenterX + Math.cos(arcAngle) * radius);
            point[1] = (float) (mCenterY + Math.sin(arcAngle) * radius);
        } else if (angle == 90) {
            point[0] = mCenterX;
            point[1] = mCenterY + radius;
        } else if (angle > 90 && angle < 180) {
            arcAngle = Math.PI * (180 - angle) / 180.0;
            point[0] = (float) (mCenterX - Math.cos(arcAngle) * radius);
            point[1] = (float) (mCenterY + Math.sin(arcAngle) * radius);
        } else if (angle == 180) {
            point[0] = mCenterX - radius;
            point[1] = mCenterY;
        } else if (angle > 180 && angle < 270) {
            arcAngle = Math.PI * (angle - 180) / 180.0;
            point[0] = (float) (mCenterX - Math.cos(arcAngle) * radius);
            point[1] = (float) (mCenterY - Math.sin(arcAngle) * radius);
        } else if (angle == 270) {
            point[0] = mCenterX;
            point[1] = mCenterY - radius;
        } else {
            arcAngle = Math.PI * (360 - angle) / 180.0;
            point[0] = (float) (mCenterX + Math.cos(arcAngle) * radius);
            point[1] = (float) (mCenterY - Math.sin(arcAngle) * radius);
        }

        return point;
    }

    private SweepGradient generateSweepGradient() {
        SweepGradient sweepGradient = new SweepGradient(mCenterX, mCenterY,
                mColors,
                new float[]{0, 140 / 360f, mSweepAngle / 360f}
        );

        Matrix matrix = new Matrix();
        matrix.setRotate(mStartAngle - 3, mCenterX, mCenterY);
        sweepGradient.setLocalMatrix(matrix);

        return sweepGradient;
    }

    public int getmMin() {
        return mMin;
    }

    public int getmMax() {
        return mMax;
    }

    public String getmHeaderText() {
        return mHeaderText;
    }

    public int getmSection() {
        return mSection;
    }

    public void setmSection(int mSection) {
        this.mSection = mSection;
        initSizes();
        invalidate();
    }

    public void setmMin(int mMin) {
        this.mMin = mMin;
        initSizes();
        invalidate();
    }

    public void setmMax(int mMax) {
        this.mMax = mMax;
        initSizes();
        invalidate();
    }

    public void setmHeaderText(String mHeaderText) {
        this.mHeaderText = mHeaderText;
        initSizes();
        invalidate();
    }

    public float getVelocity() {
        return mVelocity;
    }

    public void setVelocity(float velocity) {
        if (mVelocity == velocity || velocity < getmMin() || velocity > getmMax()) {
            return;
        }
        mVelocity = velocity;
        postInvalidate();
    }
}
