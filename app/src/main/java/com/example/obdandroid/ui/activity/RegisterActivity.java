package com.example.obdandroid.ui.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseLoginActivity;
import com.example.obdandroid.config.Constant;
import com.example.obdandroid.ui.entity.ResultEntity;
import com.example.obdandroid.ui.entity.SMSVerificationCodeEntity;
import com.example.obdandroid.ui.entity.UserLoginEntity;
import com.example.obdandroid.ui.view.CircleImageView;
import com.example.obdandroid.ui.view.CustomeDialog;
import com.example.obdandroid.ui.view.PhotoDialog;
import com.example.obdandroid.ui.view.progressButton.CircularProgressButton;
import com.example.obdandroid.utils.ActivityManager;
import com.example.obdandroid.utils.AppDateUtils;
import com.example.obdandroid.utils.CountDownTimerUtils;
import com.example.obdandroid.utils.DialogUtils;
import com.example.obdandroid.utils.DisplayUtils;
import com.example.obdandroid.utils.JumpUtil;
import com.example.obdandroid.utils.SPUtil;
import com.hjq.bar.TitleBar;
import com.kongzue.dialog.v2.TipDialog;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Response;

import static com.example.obdandroid.config.APIConfig.LOGIN_URL;
import static com.example.obdandroid.config.APIConfig.REGISTER_URL;
import static com.example.obdandroid.config.APIConfig.SERVER_URL;
import static com.example.obdandroid.config.APIConfig.sendSMSVerificationCode_URL;
import static com.example.obdandroid.config.APIConfig.verifySMSVerificationCode_URL;
import static com.example.obdandroid.config.Constant.EXPIRE_TIME;
import static com.example.obdandroid.config.Constant.PASSWORD;
import static com.example.obdandroid.config.Constant.PLATFORM;
import static com.example.obdandroid.config.Constant.TOKEN;
import static com.example.obdandroid.config.Constant.USER_ID;
import static com.example.obdandroid.config.Constant.USER_NAME;
import static com.example.obdandroid.config.TAG.TAG_Activity;

/**
 * 作者：Jealous
 * 日期：2020/12/23 0023
 * 描述：
 */
public class RegisterActivity extends BaseLoginActivity {
    private Context context;
    private EditText etUser;
    private EditText etPwd;
    private CircularProgressButton btnSignUp;
    private CircleImageView myHeaderImage;
    private EditText etNick;
    private EditText etCode;
    private LinearLayout layoutRegisterCode;
    private CircularProgressButton btnSignUpAgree;
    private LinearLayout layoutRegisterInfo;
    private TextView tvSex;
    private int themeId;
    private final int chooseMode = PictureMimeType.ofImage();
    private final int maxSelectNum = 1;
    private DialogUtils dialogUtils;
    private String compressPath;
    private String path = "";
    private String headPortrait = "";
    private List<LocalMedia> selectList = new ArrayList<>();
    private final Map<String, File> files = new HashMap<>();
    private SPUtil spUtil;
    private TextInputLayout textLayout;
    private CountDownTimerUtils mCountDownTimerUtils;
    private String taskID;
    private String sex = "0";
    private int index = 0;
    private final String[] sexArr = new String[]{"保密", "男", "女"};// 性别选择
    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == 1) {
                taskID = msg.obj.toString();
            }
        }
    };

    @Override
    protected int getContentViewId() {
        return R.layout.activity_register;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    public void initView() {
        super.initView();
        context = this;
        themeId = R.style.picture_default_style;
        DisplayUtils.getInstance(context);
        etUser = findViewById(R.id.etUser);
        etPwd = findViewById(R.id.etPwd);
        btnSignUp = findViewById(R.id.btnSignUp);
        myHeaderImage = findViewById(R.id.my_header_image);
        etNick = findViewById(R.id.etNick);
        etCode = findViewById(R.id.etCode);
        Button btnCode = findViewById(R.id.btn_code);
        textLayout = findViewById(R.id.textLayout);
        TitleBar titleBarSet = findViewById(R.id.titleBarSet);
        layoutRegisterCode = findViewById(R.id.layoutRegisterCode);
        btnSignUpAgree = findViewById(R.id.btnSignUpAgree);
        layoutRegisterInfo = findViewById(R.id.layoutRegisterInfo);
        tvSex = findViewById(R.id.tvSex);
        dialogUtils = new DialogUtils(context);
        spUtil = new SPUtil(context);
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
        mCountDownTimerUtils = new CountDownTimerUtils(btnCode, context, 60000, 1000);
        btnCode.setOnClickListener(v -> {
            if (TextUtils.isEmpty(etUser.getText().toString())) {
                showToast("请输入手机号");
                return;
            }
            sendSMSVerificationCode(etUser.getText().toString());
        });
        btnSignUpAgree.setIndeterminateProgressMode(true);
        btnSignUpAgree.setOnClickListener(v -> {
            if (TextUtils.isEmpty(etUser.getText().toString())) {
                showToast("请输入手机号");
                return;
            }
            if (TextUtils.isEmpty(etCode.getText().toString())) {
                showToast("请输入验证码");
                return;
            }
            if (btnSignUpAgree.getProgress() == -1) {
                btnSignUpAgree.setProgress(0);
            }
            verifySMSVerificationCode(taskID, etUser.getText().toString(), etCode.getText().toString());
        });
        tvSex.setOnClickListener(v -> showSexChooseDialog(tvSex));
        //注册
        btnSignUp.setIndeterminateProgressMode(true);
        btnSignUp.setOnClickListener(v -> {
            if (TextUtils.isEmpty(etNick.getText().toString())) {
                showToast("请输入昵称");
                return;
            }
            if (TextUtils.isEmpty(etUser.getText().toString())) {
                showToast("请输入手机号");
                return;
            }
            if (!isMobileNO(etUser.getText().toString())) {
                showTipsDialog("请输入有效的手机号码！", TipDialog.TYPE_ERROR);
                return;
            }
            if (TextUtils.isEmpty(etPwd.getText().toString())) {
                showToast("请输入密码");
                return;
            }
            if (!isPassword(etPwd.getText().toString())) {
                showToast("请输入正确的密码格式");
                textLayout.setError("必须包含小写字母，数字，可以是字母数字下划线组成并且长度是6到16");
                return;
            }
            if (TextUtils.isEmpty(etCode.getText().toString())) {
                showToast("请输入验证码");
                return;
            }
            if (TextUtils.isEmpty(path)) {
                headPortrait = bitmaptoString(context, R.drawable.header_image);
            } else {
                headPortrait = imageToBase64(path);
            }
            if (btnSignUp.getProgress() == -1) {
                btnSignUp.setProgress(0);
            }
            registerUser(headPortrait, etUser.getText().toString(), etNick.getText().toString(), etPwd.getText().toString(), sex, etCode.getText().toString(), taskID);
        });
    }

    /**
     * @param changeSex 性别显示控件
     *                  选择性别
     */
    private void showSexChooseDialog(TextView changeSex) {
        AlertDialog.Builder builder3 = new AlertDialog.Builder(this);// 自定义对话框
        // 2默认的选中
        builder3.setSingleChoiceItems(sexArr, index, (dialog, which) -> {// which是被选中的位置
            sex = String.valueOf(which);
            index = which;
            changeSex.setText(sexArr[which]);
            dialog.dismiss();// 随便点击一个item消失对话框，不用点击确认取消
        });
        builder3.show();// 让弹出框显示
    }

    /**
     * @param mobiles 手机号
     * @return 校验手机号
     */
    public boolean isMobileNO(String mobiles) {
        //"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，
        //"\\d{9}"代表后面是可以是0～9的数字，有9位。
        String telRegex = "[1][3456789]\\d{9}";
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }

    /**
     * @param password 密码
     * @return 必须包含小写字母，数字，可以是字母数字下划线组成并且长度是6到16
     */
    public boolean isPassword(String password) {
        String regex = "^(?=.*?[a-z])(?=.*?[0-9])[a-zA-Z0-9_]{6,16}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(password);
        boolean isMatch = m.matches();
        Log.i(TAG_Activity, "isPassword: 是否密码正则匹配" + isMatch);
        return isMatch;
    }

    /**
     * @param mobile 手机号
     *               发送短信验证码
     */
    private void sendSMSVerificationCode(String mobile) {
        mCountDownTimerUtils.start();
        OkHttpUtils.post().url(SERVER_URL + sendSMSVerificationCode_URL).
                addParam("mobile", mobile).
                build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                SMSVerificationCodeEntity entity = JSON.parseObject(response, SMSVerificationCodeEntity.class);
                if (entity.isSuccess()) {
                    showTipsDialog("验证码发送成功", TipDialog.TYPE_FINISH);
                    Message message = new Message();
                    message.arg1 = 1;
                    message.obj = entity.getData().getTaskID();
                    handler.sendMessage(message);
                    mCountDownTimerUtils.onFinish();

                }
            }
        });
    }

    /**
     * @param taskID           短信验证码id
     * @param mobile           手机号
     * @param verificationCode 验证码
     *                         校验短信验证码
     */
    private void verifySMSVerificationCode(String taskID, String mobile, String verificationCode) {
        btnSignUpAgree.setProgress(0);
        new Handler().postDelayed(() -> btnSignUpAgree.setProgress(50), 3000);
        OkHttpUtils.post().url(SERVER_URL + verifySMSVerificationCode_URL).
                addParam("taskID", taskID).
                addParam("mobile", mobile).
                addParam("verificationCode", verificationCode).
                build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                ResultEntity entity = JSON.parseObject(response, ResultEntity.class);
                if (entity.isSuccess()) {
                    btnSignUpAgree.setProgress(100);
                    layoutRegisterCode.setVisibility(View.GONE);
                    layoutRegisterInfo.setVisibility(View.VISIBLE);
                } else {
                    btnSignUpAgree.setProgress(-1);
                    showTipsDialog("验证码校验失败", TipDialog.TYPE_ERROR);
                }
            }
        });
    }

    /**
     * @param headPortrait     用户头像
     * @param mobile           手机号
     * @param nickname         昵称
     * @param password         密码
     * @param verificationCode 验证码
     */
    private void registerUser(String headPortrait, String mobile, String nickname, String password, String sex, String verificationCode, String taskID) {
        btnSignUp.setProgress(0);
        new Handler().postDelayed(() -> btnSignUp.setProgress(50), 3000);
        OkHttpUtils.post().url(SERVER_URL + REGISTER_URL).
                addParam("headPortrait", headPortrait).
                addParam("mobile", mobile).
                addParam("nickname", nickname).
                addParam("password", password).
                addParam("registrationPlatform", PLATFORM).
                addParam("verificationCode", verificationCode).
                addParam("taskID", taskID).
                addParam("sex", sex).
                build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
                btnSignUp.setProgress(-1);
                showTipsDialog(validateError(e, response), TipDialog.TYPE_ERROR);
            }

            @Override
            public void onResponse(String response, int id) {
                ResultEntity entity = JSON.parseObject(response, ResultEntity.class);
                if (TextUtils.equals(entity.getCode(), "SUCCESS")) {
                    btnSignUp.setProgress(100);
                    new CustomeDialog(context, "注册成功,请登录！", confirm -> {
                        if (confirm) {
                            userLogin(mobile, password, "1", "", taskID);
                        }
                    }).setPositiveButton("登录").setTitle("注册提示").show();
                } else {
                    btnSignUp.setProgress(-1);
                    showTipsDialog(entity.getMessage(), TipDialog.TYPE_ERROR);
                }
            }
        });
    }

    /**
     * @param mobile   手机号
     * @param password 密码
     *                 用户登录
     */
    private void userLogin(String mobile, String password, String loginType, String verificationCode, String taskID) {
        dialogUtils.showProgressDialog("正在登录！");
        OkHttpUtils.post().url(SERVER_URL + LOGIN_URL).
                addParam("mobile", mobile).
                addParam("password", password).
                addParam("loginType", loginType).
                addParam("verificationCode", verificationCode).
                addParam("taskID", taskID).
                build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
                dialogUtils.dismiss();
                showTipsDialog(validateError(e, response), TipDialog.TYPE_ERROR);
            }

            @Override
            public void onResponse(String response, int id) {
                UserLoginEntity entity = JSON.parseObject(response, UserLoginEntity.class);
                if (entity.isSuccess()) {
                    spUtil.put(Constant.IS_LOGIN, true);
                    spUtil.put(USER_NAME, mobile);
                    spUtil.put(PASSWORD, password);
                    spUtil.put(TOKEN, entity.getData().getToken());
                    spUtil.put(USER_ID, String.valueOf(entity.getData().getUserId()));
                    spUtil.put(EXPIRE_TIME, AppDateUtils.dealDateFormat(entity.getData().getExpireTime()));
                    JumpUtil.startAct(context, MainActivity.class);
                    ActivityManager.getInstance().finishActivitys();
                } else {
                    showTipsDialog(entity.getMessage(), TipDialog.TYPE_ERROR);
                }
            }
        });
    }

    /**
     * @param msg  提示信息
     * @param type 提示类型
     *             提示
     */
    private void showTipsDialog(String msg, int type) {
        TipDialog.show(context, msg, TipDialog.SHOW_TIME_SHORT, type);
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
