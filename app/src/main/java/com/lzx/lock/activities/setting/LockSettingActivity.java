package com.lzx.lock.activities.setting;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzx.lock.R;
import com.lzx.lock.activities.about.AboutMeActivity;
import com.lzx.lock.activities.lock.GestureCreateActivity;
import com.lzx.lock.base.AppConstants;
import com.lzx.lock.base.BaseActivity;
import com.lzx.lock.model.LockAutoTime;
import com.lzx.lock.services.BackgroundManager;
import com.lzx.lock.services.LockAccessibilityService;
import com.lzx.lock.services.LockService;
import com.lzx.lock.utils.SpUtil;
import com.lzx.lock.utils.SystemBarHelper;
import com.lzx.lock.utils.ToastUtil;
import com.lzx.lock.widget.SelectLockTimeDialog;


/**
 * Created by xian on 2017/2/17.
 */

public class LockSettingActivity extends BaseActivity implements View.OnClickListener
        , DialogInterface.OnDismissListener {

    public static final String ON_ITEM_CLICK_ACTION = "on_item_click_action";
    private static final int REQUEST_CHANGE_PWD = 3;
    private TextView mBtnAbout, mLockTime, mLockTypeSwitch, mBtnChangePwd, mIsShowPath, mLockTip, mLockScreenSwitch, mLockTakePicSwitch;
    private CheckBox mLockSwitch;
    private RelativeLayout mLockWhen, mLockType, mLockScreen, mLockTakePic;
    private LockSettingReceiver mLockSettingReceiver;
    private SelectLockTimeDialog dialog;
    private RelativeLayout mTopLayout;

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        mBtnChangePwd = findViewById(R.id.btn_change_pwd);
        mLockTime = findViewById(R.id.lock_time);
        mBtnAbout = findViewById(R.id.about_me);
        mLockSwitch = findViewById(R.id.switch_compat);
        mLockWhen = findViewById(R.id.lock_when);
        mLockType = findViewById(R.id.lock_type);
        mLockTypeSwitch = findViewById(R.id.lock_type_switch);
        mLockScreen = findViewById(R.id.lock_screen);
        mLockTakePic = findViewById(R.id.lock_take_pic);
        mIsShowPath = findViewById(R.id.is_show_path);
        mLockTip = findViewById(R.id.lock_tip);
        mLockScreenSwitch = findViewById(R.id.lock_screen_switch);
        mLockTakePicSwitch = findViewById(R.id.lock_take_pic_switch);
        mTopLayout = findViewById(R.id.top_layout);
        mTopLayout.setPadding(0, SystemBarHelper.getStatusBarHeight(this), 0, 0);
    }

    @Override
    protected void initData() {
        mLockSettingReceiver = new LockSettingReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ON_ITEM_CLICK_ACTION);
        registerReceiver(mLockSettingReceiver, filter);
        dialog = new SelectLockTimeDialog(this, "");
        dialog.setOnDismissListener(this);
        boolean isLockOpen = SpUtil.getInstance().getBoolean(AppConstants.LOCK_STATE);
        mLockSwitch.setChecked(isLockOpen);

        boolean isLockAutoScreen = SpUtil.getInstance().getBoolean(AppConstants.LOCK_AUTO_SCREEN, false);
        mLockScreenSwitch.setText(isLockAutoScreen ? "on" : "off");

        boolean isLockAccessibilityOn = SpUtil.getInstance().getBoolean(AppConstants.LOCK_TYPE, false);
        mLockTypeSwitch.setText(isLockAccessibilityOn ? "Accessibility" : "Usages Stats");
        boolean isTakePic = SpUtil.getInstance().getBoolean(AppConstants.LOCK_AUTO_RECORD_PIC, false);
        mLockTakePicSwitch.setText(isTakePic ? "on" : "off");

        mLockTime.setText(SpUtil.getInstance().getString(AppConstants.LOCK_APART_TITLE, "immediately"));
    }

    @Override
    protected void initAction() {
        mBtnChangePwd.setOnClickListener(this);
        mBtnAbout.setOnClickListener(this);
        mLockWhen.setOnClickListener(this);
        mLockType.setOnClickListener(this);
        mLockScreen.setOnClickListener(this);
        mIsShowPath.setOnClickListener(this);
        mLockScreenSwitch.setOnClickListener(this);
       // mLockTypeSwitch.setOnClickListener(this);
        mLockTakePic.setOnClickListener(this);
        mLockSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SpUtil.getInstance().putBoolean(AppConstants.LOCK_STATE, b);
                Intent intent = new Intent(LockSettingActivity.this, LockService.class);
                if (b) {
                    mLockTip.setText("Opened, password is required when the lock application is opened");

                    BackgroundManager.getInstance().init(LockSettingActivity.this).startService(LockService.class);

                } else {
                    mLockTip.setText("Closed, no password is required when the lock app opens");
                    BackgroundManager.getInstance().init(LockSettingActivity.this).stopService(LockService.class);

                }
            }
        });
    }

    @Override
    public void onClick(@NonNull View view) {
        switch (view.getId()) {
            case R.id.btn_change_pwd:
                Intent intent = new Intent(LockSettingActivity.this, GestureCreateActivity.class);
                startActivityForResult(intent, REQUEST_CHANGE_PWD);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.about_me:
                intent = new Intent(LockSettingActivity.this, AboutMeActivity.class);
                startActivity(intent);
                break;
            case R.id.lock_when:
                String title = SpUtil.getInstance().getString(AppConstants.LOCK_APART_TITLE, "");
                dialog.setTitle(title);
                dialog.show();
                break;
            case R.id.is_show_path:
                boolean ishideline = SpUtil.getInstance().getBoolean(AppConstants.LOCK_IS_HIDE_LINE, false);
                if (ishideline) {
                    SpUtil.getInstance().putBoolean(AppConstants.LOCK_IS_HIDE_LINE, false);
                    ToastUtil.showToast("Path is displayed");
                } else {
                    SpUtil.getInstance().putBoolean(AppConstants.LOCK_IS_HIDE_LINE, true);
                    ToastUtil.showToast("Path is hidden");
                }
                break;
            case R.id.lock_screen:
                boolean isLockAutoScreen = SpUtil.getInstance().getBoolean(AppConstants.LOCK_AUTO_SCREEN, false);
                if (isLockAutoScreen) {
                    SpUtil.getInstance().putBoolean(AppConstants.LOCK_AUTO_SCREEN, false);
                    mLockScreenSwitch.setText("off");
                } else {
                    SpUtil.getInstance().putBoolean(AppConstants.LOCK_AUTO_SCREEN, true);
                    mLockScreenSwitch.setText("on");
                }
                break;
            case R.id.lock_type:
                boolean isLockTypeAccessibility = SpUtil.getInstance().getBoolean(AppConstants.LOCK_TYPE, false);
                if (!isLockTypeAccessibility) {
                    if (!LockAccessibilityService.isAccessibilitySettingsOn(getApplicationContext())) {
                        Intent intentForAccessbility = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                        intentForAccessbility.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getApplicationContext().startActivity(intentForAccessbility);
                    }else {
                        SpUtil.getInstance().putBoolean(AppConstants.LOCK_TYPE,true);

                        mLockTypeSwitch.setText("Accessibility");
                    }
                } else {
                    SpUtil.getInstance().putBoolean(AppConstants.LOCK_TYPE, false);

                    mLockTypeSwitch.setText("Usage state");
                }
                break;
            case R.id.lock_take_pic:
                boolean isTakePic = SpUtil.getInstance().getBoolean(AppConstants.LOCK_AUTO_RECORD_PIC, false);
                if (isTakePic) {
                    SpUtil.getInstance().putBoolean(AppConstants.LOCK_AUTO_RECORD_PIC, false);
                    mLockTakePicSwitch.setText("off");
                } else {
                    SpUtil.getInstance().putBoolean(AppConstants.LOCK_AUTO_RECORD_PIC, true);
                    mLockTakePicSwitch.setText("on");
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CHANGE_PWD:
                    ToastUtil.showToast("Password reset succeeded");
                    break;
            }
        }
    }


    @Override
    public void onDismiss(DialogInterface dialogInterface) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mLockSettingReceiver);
    }

    private class LockSettingReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, @NonNull Intent intent) {
            String action = intent.getAction();
            if (action.equals(ON_ITEM_CLICK_ACTION)) {
                LockAutoTime info = intent.getParcelableExtra("info");
                boolean isLast = intent.getBooleanExtra("isLast", true);
                if (isLast) {
                    mLockTime.setText(info.getTitle());
                    SpUtil.getInstance().putString(AppConstants.LOCK_APART_TITLE, info.getTitle());
                    SpUtil.getInstance().putLong(AppConstants.LOCK_APART_MILLISECONDS, 0L);
                    SpUtil.getInstance().putBoolean(AppConstants.LOCK_AUTO_SCREEN_TIME, false);
                } else {
                    mLockTime.setText(info.getTitle());
                    SpUtil.getInstance().putString(AppConstants.LOCK_APART_TITLE, info.getTitle());
                    SpUtil.getInstance().putLong(AppConstants.LOCK_APART_MILLISECONDS, info.getTime());
                    SpUtil.getInstance().putBoolean(AppConstants.LOCK_AUTO_SCREEN_TIME, true);
                }
                dialog.dismiss();
            }
        }
    }

}
