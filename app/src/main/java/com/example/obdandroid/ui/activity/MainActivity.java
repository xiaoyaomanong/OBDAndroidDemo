package com.example.obdandroid.ui.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseActivity;
import com.example.obdandroid.ui.adapter.SimpleFragmentPagerAdapter;
import com.example.obdandroid.ui.fragment.HomeFragment;
import com.example.obdandroid.ui.fragment.MsgFragment;
import com.example.obdandroid.ui.fragment.PersonalFragment;
import com.example.obdandroid.ui.fragment.VehicleCheckFragment;
import com.example.obdandroid.utils.ActivityManager;
import com.example.obdandroid.utils.SPUtil;
import com.example.obdandroid.utils.StringUtil;
import com.gyf.immersionbar.ImmersionBar;
import com.kongzue.dialog.util.BlurView;

import java.util.ArrayList;
import java.util.List;

import static com.kongzue.dialog.v2.DialogSettings.THEME_DARK;
import static com.kongzue.dialog.v2.DialogSettings.blur_alpha;
import static com.kongzue.dialog.v2.DialogSettings.dialog_background_color;
import static com.kongzue.dialog.v2.DialogSettings.dialog_theme;
import static com.kongzue.dialog.v2.DialogSettings.use_blur;

/**
 * 作者：Jealous
 * 日期：2020/12/22 0022
 * 描述：正式项目
 */

public class MainActivity extends BaseActivity {
    private ViewPager viewPager;
    private BottomNavigationView navigation;
    private final List<Fragment> fragments = new ArrayList<>();
    private Context context;
    private int blur_front_color;
    private BlurView blur;
    private SPUtil spUtil;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    public void initView() {
        super.initView();
        context = this;
        ImmersionBar.with(MainActivity.this).statusBarColor(R.color.black)
                .fitsSystemWindows(true).init();
        viewPager = findViewById(R.id.viewPager);
        navigation = findViewById(R.id.navigation);
        spUtil = new SPUtil(context);
        fragments.add(HomeFragment.getInstance());
        fragments.add(VehicleCheckFragment.getInstance());
        fragments.add(MsgFragment.getInstance());
        fragments.add(PersonalFragment.getInstance());
        viewPager.setAdapter(new SimpleFragmentPagerAdapter(getSupportFragmentManager(), fragments));
        viewPager.addOnPageChangeListener(mOnPageChangeListener);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private final ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            navigation.getMenu().getItem(position).setChecked(true);
            switch (position){
                case 0:
                case 2:
                case 3:
                    ImmersionBar.with(MainActivity.this).statusBarColor(R.color.black)
                            .fitsSystemWindows(true).init();
                    break;
                case 1:
                    ImmersionBar.with(MainActivity.this).statusBarColor(R.color.color_bar)
                            .fitsSystemWindows(true).init();
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    viewPager.setCurrentItem(0);
                    ImmersionBar.with(MainActivity.this).statusBarColor(R.color.black)
                            .fitsSystemWindows(true).init();
                    return true;
                case R.id.navigation_check:
                    viewPager.setCurrentItem(1);
                    //沉浸式状态栏
                    ImmersionBar.with(MainActivity.this).statusBarColor(R.color.color_bar)
                            .fitsSystemWindows(true).init();
                    return true;
                case R.id.navigation_msg:
                    viewPager.setCurrentItem(2);
                    ImmersionBar.with(MainActivity.this).statusBarColor(R.color.black)
                            .fitsSystemWindows(true).init();
                    return true;
                case R.id.navigation_my:
                    viewPager.setCurrentItem(3);
                    ImmersionBar.with(MainActivity.this).statusBarColor(R.color.black)
                            .fitsSystemWindows(true).init();
                    return true;
            }
            return false;
        }
    };

    /**
     * 退出提示
     */
    private void goBack() {
        final AlertDialog exitDialog = new AlertDialog.Builder(context, R.style.darkMode).create();
        View rootView = LayoutInflater.from(context).inflate(R.layout.dialog_select_ios, null);
        Window window = exitDialog.getWindow();
        exitDialog.setView(rootView);
        window.setWindowAnimations(R.style.iOSAnimStyle);
        RelativeLayout bkg = rootView.findViewById(R.id.bkg);
        TextView txtDialogTitle = rootView.findViewById(R.id.txt_dialog_title);
        TextView txtDialogTip = rootView.findViewById(R.id.txt_dialog_tip);
        ImageView splitHorizontal = rootView.findViewById(R.id.split_horizontal);
        TextView btnSelectNegative = rootView.findViewById(R.id.btn_selectNegative);
        ImageView splitVertical = rootView.findViewById(R.id.split_vertical);
        TextView btnSelectPositive = rootView.findViewById(R.id.btn_selectPositive);
        splitVertical.setVisibility(View.VISIBLE);
        if (StringUtil.isNull("系统提示")) {
            txtDialogTitle.setVisibility(View.GONE);
        } else {
            txtDialogTitle.setVisibility(View.VISIBLE);
            txtDialogTitle.setText("系统提示");
        }
        if (StringUtil.isNull("你确定要退出吗")) {
            txtDialogTip.setVisibility(View.GONE);
        } else {
            txtDialogTip.setVisibility(View.VISIBLE);
            txtDialogTip.setText("你确定要退出吗");
        }
        btnSelectPositive.setText("确定");
        btnSelectPositive.setOnClickListener(v -> {
            exitDialog.dismiss();
            ActivityManager.getInstance().finishActivitys();
        });
        btnSelectNegative.setVisibility(View.VISIBLE);
        btnSelectNegative.setText("取消");
        btnSelectNegative.setOnClickListener(v -> {
            exitDialog.dismiss();
            Toast.makeText(context, "取消退出", Toast.LENGTH_SHORT).show();
        });

        //onKeyListener用于设置监听手机back键的操作
        exitDialog.setOnKeyListener((dialog, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                return true;// false时dialog会消失
            }
            return true;
        });

        int bkgResId;
        if (dialog_theme == THEME_DARK) {
            splitHorizontal.setBackgroundResource(R.color.ios_dialog_split_dark);
            splitVertical.setBackgroundResource(R.color.ios_dialog_split_dark);
            btnSelectNegative.setBackgroundResource(R.drawable.button_dialog_left_dark);
            btnSelectPositive.setBackgroundResource(R.drawable.button_dialog_right_dark);
            bkgResId = R.drawable.rect_dlg_dark;
            blur_front_color = Color.argb(blur_alpha, 0, 0, 0);
        } else {
            btnSelectNegative.setBackgroundResource(R.drawable.button_dialog_left);
            btnSelectPositive.setBackgroundResource(R.drawable.button_dialog_right);
            bkgResId = R.drawable.rect_light;
            blur_front_color = Color.argb(blur_alpha, 255, 255, 255); //白
        }

        if (use_blur) {
            bkg.post(() -> {
                blur = new BlurView(context, null);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, bkg.getHeight());
                blur.setOverlayColor(blur_front_color);
                bkg.addView(blur, 0, params);
            });
        } else {
            bkg.setBackgroundResource(bkgResId);
        }

        if (dialog_background_color != -1) {
            bkg.setBackgroundResource(dialog_background_color);
        }
        exitDialog.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            goBack();
        }
        return true;
    }
}
