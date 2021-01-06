package com.example.obdandroid.ui.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.obdandroid.R;
import com.example.obdandroid.listener.OnSwipeTouchListener;
import com.example.obdandroid.ui.entity.ResultEntity;
import com.example.obdandroid.ui.view.CircleImageView;
import com.example.obdandroid.ui.view.PhotoDialog;
import com.example.obdandroid.utils.DialogUtils;
import com.example.obdandroid.utils.DisplayUtils;
import com.kongzue.dialog.v2.TipDialog;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

import static com.example.obdandroid.config.APIConfig.REGISTER_URL;
import static com.example.obdandroid.config.APIConfig.SERVER_URL;
import static com.example.obdandroid.config.Constant.PLATFORM;
import static com.example.obdandroid.config.TAG.TAG_Activity;

/**
 * 作者：Jealous
 * 日期：2020/12/23 0023
 * 描述：
 */
public class RegisterActivity extends AppCompatActivity {
    private Context context;
    private ImageView imageView;
    private TextView textView;
    int count = 0;
    private LinearLayout linearLayout;
    private EditText etUser;
    private EditText etPwd;
    private Button btnSignUp;
    private CircleImageView myHeaderImage;
    private EditText etNick;
    private EditText etCode;
    private Button btnCode;
    private int themeId;
    private final int chooseMode = PictureMimeType.ofImage();
    private final int maxSelectNum = 1;
    private DialogUtils dialogUtils;
    private String compressPath;
    private String path = "";
    private List<LocalMedia> selectList = new ArrayList<>();
    private Map<String, File> files = new HashMap<>();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        themeId = R.style.picture_default_style;
        DisplayUtils.getInstance(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);
        dialogUtils = new DialogUtils(context);
        initView();

        myHeaderImage.setOnClickListener(v ->
                new PhotoDialog(context, R.style.dialog, "请选取照片方式", new PhotoDialog.OnCloseListener() {
                    @Override
                    public void onClickAlbum(Dialog dialog, boolean confirm) {
                        // 进入相册
                        if (confirm) {
                            Album();
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onClickCamera(Dialog dialog, boolean confirm) {
                        // 单独拍照
                        if (confirm) {
                            Camera();
                            dialog.dismiss();
                        }
                    }
                }).show());
        imageView.setOnTouchListener(new OnSwipeTouchListener(context) {
            public void onSwipeTop() {

            }

            @SuppressLint("SetTextI18n")
            public void onSwipeRight() {
                if (count == 0) {
                    imageView.setImageResource(R.drawable.good_night_img);
                    textView.setText("Night");
                    count = 1;
                } else {
                    imageView.setImageResource(R.drawable.good_morning_img);
                    textView.setText("Morning");
                    count = 0;
                }
            }

            @SuppressLint("SetTextI18n")
            public void onSwipeLeft() {
                if (count == 0) {
                    imageView.setImageResource(R.drawable.good_night_img);
                    textView.setText("Night");
                    count = 1;
                } else {
                    imageView.setImageResource(R.drawable.good_morning_img);
                    textView.setText("Morning");
                    count = 0;
                }
            }

            public void onSwipeBottom() {

            }
        });
        //注册
        btnSignUp.setOnClickListener(v -> {
            if (TextUtils.isEmpty(etNick.getText().toString().trim())) {
                showToast("请输入昵称");
                return;
            }
            if (TextUtils.isEmpty(etUser.getText().toString().trim())) {
                showToast("请输入手机号");
                return;
            }
            if (TextUtils.isEmpty(etPwd.getText().toString().trim())) {
                showToast("请输入密码");
                return;
            }
            if (TextUtils.isEmpty(etCode.getText().toString().trim())) {
                showToast("请输入验证");
                return;
            }
            registerUser(imageToBase64(path),etUser.getText().toString(),etNick.getText().toString(),etPwd.getText().toString(), etCode.getText().toString());
        });
    }

    private void showToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);
        linearLayout = findViewById(R.id.linearLayout);
        etUser = findViewById(R.id.etUser);
        etPwd = findViewById(R.id.etPwd);
        btnSignUp = findViewById(R.id.btnSignUp);
        myHeaderImage = findViewById(R.id.my_header_image);
        etNick = findViewById(R.id.etNick);
        etCode = findViewById(R.id.etCode);
        btnCode = findViewById(R.id.btn_code);
    }

    /**
     * @param headPortrait         用户头像
     * @param mobile               手机号
     * @param nickname             昵称
     * @param password             密码
     * @param verificationCode     验证码
     */
    private void registerUser(String headPortrait, String mobile, String nickname, String password, String verificationCode) {
        dialogUtils.showProgressDialog();
        OkHttpUtils.post().url(SERVER_URL + REGISTER_URL).
                addParam("headPortrait", headPortrait).
                addParam("mobile", mobile).
                addParam("nickname", nickname).
                addParam("password", password).
                addParam("registrationPlatform", PLATFORM).
                addParam("verificationCode", verificationCode).
                build().
                execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Response response, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e(TAG_Activity, "用户注册:" + response);
                        ResultEntity entity = JSON.parseObject(response, ResultEntity.class);
                        if (TextUtils.equals(entity.getCode(), "SUCCESS")) {
                            dialogUtils.dismiss();
                            TipDialog.show(context, "注册成功", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_FINISH);
                        } else {
                            dialogUtils.dismiss();
                            TipDialog.show(context, "注册失败", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_ERROR);
                        }
                    }
                });
    }

    /**
     * 相册
     */
    private void Album() {
        // 进入相册 以下是例子：不需要的api可以不写
        PictureSelector.create(RegisterActivity.this)
                .openGallery(chooseMode)// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .theme(themeId)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                .maxSelectNum(maxSelectNum)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .imageSpanCount(4)// 每行显示个数
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选
                .previewImage(true)// 是否可预览图片
                .previewVideo(true)// 是否可预览视频
                .enablePreviewAudio(true) // 是否可播放音频
                .isCamera(true)// 是否显示拍照按钮
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
                .enableCrop(false)// 是否裁剪
                .compress(true)// 是否压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                //.compressSavePath(getPath())//压缩图片保存地址
                //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(1, 1)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示
                .isGif(true)// 是否显示gif图片
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                .circleDimmedLayer(false)// 是否圆形裁剪
                .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                .openClickSound(false)// 是否开启点击声音
                .selectionMedia(selectList)// 是否传入已选图片
                .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                .cropCompressQuality(90)// 裁剪压缩质量 默认100
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                //.rotateEnabled(true) // 裁剪是否可旋转图片
                //.scaleEnabled(true)// 裁剪是否可放大缩小图片
                //.videoQuality()// 视频录制质量 0 or 1
                //.videoSecond()//显示多少秒以内的视频or音频也可适用
                //.recordVideoSecond()//录制视频秒数 默认60s
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code

    }

    /**
     * 相机
     */
    private void Camera() {
        // 单独拍照
        PictureSelector.create(RegisterActivity.this)
                .openCamera(chooseMode)// 单独拍照，也可录像或也可音频 看你传入的类型是图片or视频
                .theme(themeId)// 主题样式设置 具体参考 values/styles
                .maxSelectNum(maxSelectNum)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选
                .previewImage(true)// 是否可预览图片
                .previewVideo(true)// 是否可预览视频
                .enablePreviewAudio(true) // 是否可播放音频
                .isCamera(true)// 是否显示拍照按钮
                .enableCrop(false)// 是否裁剪
                .compress(true)// 是否压缩
                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(1, 1)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示
                .isGif(true)// 是否显示gif图片
                .freeStyleCropEnabled(false)// 裁剪框是否可拖拽
                .circleDimmedLayer(false)// 是否圆形裁剪
                .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                .openClickSound(false)// 是否开启点击声音
                .selectionMedia(selectList)// 是否传入已选图片
                .previewEggs(false)//预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                .cropCompressQuality(90)// 裁剪压缩质量 默认为100
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                //.rotateEnabled() // 裁剪是否可旋转图片
                //.scaleEnabled()// 裁剪是否可放大缩小图片
                //.videoQuality()// 视频录制质量 0 or 1
                //.videoSecond()////显示多少秒以内的视频or音频也可适用
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    // 处理选择的照片的地址
    private void refreshAdapter(List<LocalMedia> picList) {
        for (int i = 0; i < picList.size(); i++) {
            //被压缩后的图片路径
            if (picList.get(i).isCompressed()) {
                compressPath = picList.get(i).getCompressPath(); //压缩后的图片路径
            }
            File file = new File(compressPath);
            String tag = System.currentTimeMillis() + "";
            files.put(tag + i, file);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PictureConfig.CHOOSE_REQUEST) {// 图片选择
                selectList = PictureSelector.obtainMultipleResult(data);
                path = selectList.get(0).getCompressPath();
                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.header_image)
                        .diskCacheStrategy(DiskCacheStrategy.ALL);
                Glide.with(RegisterActivity.this)
                        .load(path)
                        .apply(options)
                        .into(myHeaderImage);
                refreshAdapter(selectList);
            }
        }
    }

    /**
     * @param path 图片路径
     * @return 将图片转换成Base64编码的字符串
     */
    public static String imageToBase64(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        InputStream is = null;
        byte[] data = null;
        String result = null;
        try {
            is = new FileInputStream(path);
            //创建一个字符流大小的数组。
            data = new byte[is.available()];
            //写入数组
            is.read(data);
            //用默认的编码格式进行编码
            result = Base64.encodeToString(data, Base64.NO_CLOSE);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return result;
    }
}
