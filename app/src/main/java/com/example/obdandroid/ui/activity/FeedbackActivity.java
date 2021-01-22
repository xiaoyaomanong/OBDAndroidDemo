package com.example.obdandroid.ui.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseActivity;
import com.example.obdandroid.http.HttpClientUtils;
import com.example.obdandroid.ui.adapter.GridImageAdapter;
import com.example.obdandroid.ui.entity.ResultEntity;
import com.example.obdandroid.ui.view.CustomeDialog;
import com.example.obdandroid.ui.view.PhotoDialog;
import com.example.obdandroid.utils.DialogUtils;
import com.example.obdandroid.utils.FullyGridLayoutManager;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.kongzue.dialog.v2.TipDialog;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.obdandroid.config.APIConfig.SERVER_URL;
import static com.example.obdandroid.config.APIConfig.addFeedback_URL;

/**
 * 作者：Jealous
 * 日期：2021/1/22 0022
 * 描述：
 */
public class FeedbackActivity extends BaseActivity {
    private Context context;
    private final Map<String, File> files = new HashMap<>();
    private GridImageAdapter adapter;
    private int themeId;
    private final int chooseMode = PictureMimeType.ofImage();
    private final int maxSelectNum = 9;
    private String compressPath;
    private List<LocalMedia> selectList = new ArrayList<>();
    private EditText etFeedBack;
    private EditText etContact;
    private DialogUtils dialogUtils;
    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == 1) {
                new CustomeDialog(context, msg.obj.toString(), confirm -> {
                    if (confirm) {
                        finish();
                    }
                }).setPositiveButton("确定").setTitle("意见反馈").show();
            }
        }
    };


    @Override
    protected int getContentViewId() {
        return R.layout.activity_feedback;
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
        TitleBar titleBarSet = findViewById(R.id.titleBarSet);
        etFeedBack = findViewById(R.id.etFeedBack);
        etContact = findViewById(R.id.etContact);
        Button btnSubmit = findViewById(R.id.btn_submit);
        RecyclerView recycler = findViewById(R.id.recycler);
        dialogUtils = new DialogUtils(context);
        FullyGridLayoutManager manager = new FullyGridLayoutManager(context, 4, GridLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(manager);
        adapter = new GridImageAdapter(context, this::showSelectPhoto);
        adapter.setList(selectList);
        adapter.setSelectMax(maxSelectNum);
        recycler.setAdapter(adapter);
        adapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                LocalMedia media = selectList.get(position);
                String pictureType = media.getPictureType();
                int mediaType = PictureMimeType.pictureToVideo(pictureType);
                switch (mediaType) {
                    case 1:
                        // 预览图片
                        PictureSelector.create(FeedbackActivity.this).externalPicturePreview(position, selectList);
                        break;
                    case 2:
                        // 预览视频
                        PictureSelector.create(FeedbackActivity.this).externalPictureVideo(media.getPath());
                        break;
                    case 3:
                        // 预览音频
                        PictureSelector.create(FeedbackActivity.this).externalPictureAudio(media.getPath());
                        break;
                }
            }

            @Override
            public void onClickDelete(List<LocalMedia> picList, View v) {
                // 注册外部预览图片删除按钮回调
                files.clear();
                refreshAdapter(picList);
            }
        });

        btnSubmit.setOnClickListener(v -> {
            if (TextUtils.isEmpty(etFeedBack.getText().toString())) {
                showTipsDialog("请输入反馈内容", TipDialog.TYPE_ERROR);
                return;
            }
            addFeedback(getUserId(), etFeedBack.getText().toString(), etContact.getText().toString(), getToken(), files);
        });

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
     * @param appUserId   APP用户ID
     * @param content     反馈内容
     * @param contactInfo 联系方式
     * @param token       用户Token
     *                    添加意见反馈
     */
    private void addFeedback(String appUserId, String content, String contactInfo, String token, Map<String, File> path) {
        dialogUtils.showProgressDialog();
        new Thread(() -> {
            HttpClientUtils.MultipartForm form = HttpClientUtils.getInstance().new MultipartForm();
            //设置form属性、参数
            form.setAction(SERVER_URL + addFeedback_URL);
            form.setMethod(HttpClientUtils.Method_POST);
            form.setFileField(path);
            form.addNormalField("appUserId", appUserId);
            form.addNormalField("content", content);
            form.addNormalField("contactInfo", contactInfo);
            form.addNormalField("token", token);
            HttpClientUtils.submitForm(form, new HttpClientUtils.UPLoadIMGCallBack() {
                @Override
                public void OnSuccess(String response) {
                    ResultEntity entity = JSON.parseObject(response, ResultEntity.class);
                    if (entity.isSuccess()) {
                        dialogUtils.dismiss();
                        Message message = new Message();
                        message.arg1 = 1;
                        message.obj = "反馈成功";
                        handler.sendMessage(message);
                    } else {
                        dialogError(context, entity.getMessage());
                        dialogUtils.dismiss();
                    }
                }

                @Override
                public void OnFail(String error) {
                    dialogUtils.dismiss();
                }
            });
        }).start();
    }

    /**
     * 选取照片方式
     */
    private void showSelectPhoto() {
        new PhotoDialog(context, R.style.dialog, "请选取照片方式", new PhotoDialog.OnCloseListener() {
            @Override
            public void onClickAlbum(Dialog dialog, boolean confirm) {
                // 进入相册 以下是例子：不需要的api可以不写
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
        }).show();
    }

    /**
     * 相册
     */
    private void Album() {
        // 进入相册 以下是例子：不需要的api可以不写
        PictureSelector.create(FeedbackActivity.this)
                .openGallery(chooseMode)// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .theme(themeId)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                .maxSelectNum(maxSelectNum)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .imageSpanCount(4)// 每行显示个数
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选
                .previewImage(true)// 是否可预览图片
                .previewVideo(true)// 是否可预览视频
                .enablePreviewAudio(true) // 是否可播放音频
                .isCamera(true)// 是否显示拍照按钮
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                .enableCrop(false)// 是否裁剪
                .compress(true)// 是否压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
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
        PictureSelector.create(FeedbackActivity.this)
                .openCamera(chooseMode)// 单独拍照，也可录像或也可音频 看你传入的类型是图片or视频
                .theme(themeId)// 主题样式设置 具体参考 values/styles
                .maxSelectNum(maxSelectNum)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选
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
            LogE("compressPath：" + compressPath);
            File file = new File(compressPath);
            String key = System.currentTimeMillis() + "";
            files.put(key, file);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PictureConfig.CHOOSE_REQUEST) {// 图片选择
                selectList = PictureSelector.obtainMultipleResult(data);
                adapter.setList(selectList);
                adapter.notifyDataSetChanged();
                refreshAdapter(selectList);
            }
        }
    }
}