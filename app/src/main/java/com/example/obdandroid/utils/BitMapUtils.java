package com.example.obdandroid.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.TypedValue;

import java.io.ByteArrayOutputStream;

/**
 * 作者：Jealous
 * 日期：2021/1/13 0013
 * 描述：
 */
public class BitMapUtils {

    /**
     * @param base64Data Base64图片
     * @return Base64转换图片
     */
    public static Bitmap stringToBitmap(String base64Data) {
        if (base64Data.contains("data:image")) {
            Bitmap bitmap = null;
            try {
                byte[] bitmapArray = Base64.decode(base64Data.split(",")[1], Base64.DEFAULT);
                bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        } else {
            byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }
    }

    /**
     * @param context 上下文对象
     * @param resId   资源id
     * @return 将Bitmap对象转换成Base64字节码
     */
    public String bitmaptoString(Context context, int resId) {
        // 将Bitmap转换成字符串
        String string = null;
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        getBitmap(context, resId).compress(Bitmap.CompressFormat.PNG, 100, bStream);
        byte[] bytes = bStream.toByteArray();
        string = Base64.encodeToString(bytes, Base64.DEFAULT);
        return string;
    }

    /**
     * @param context 上下文对象
     * @param resId   资源id
     * @return 将图片转换成Bitmap对象
     */
    public static Bitmap getBitmap(Context context, int resId) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        TypedValue value = new TypedValue();
        context.getResources().openRawResource(resId, value);
        options.inTargetDensity = value.density;
        options.inScaled = false;
        return BitmapFactory.decodeResource(context.getResources(), resId, options);
    }
}