package com.hengda.smart.changsha.d.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.hengda.smart.changsha.d.R;
import com.hengda.smart.changsha.d.app.HdAppConfig;
import com.hengda.smart.changsha.d.app.HdApplication;
import com.hengda.smart.changsha.d.common.util.RxBus;
import com.hengda.smart.changsha.d.model.AutoBean;
import com.kyleduo.switchbutton.SwitchButton;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SettingView extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.voice)
    ImageView voice;
    @Bind(R.id.speek)
    TextView speek;
    @Bind(R.id.switchBtn)
    SwitchButton switchBtn;
    @Bind(R.id.light)
    ImageView light;
    @Bind(R.id.txtlight)
    TextView txtlight;
    @Bind(R.id.back_setting)
    ImageView backSetting;
    @Bind(R.id.seekbar_seting)
    SeekBar seekbarSeting;
    @Bind(R.id.activity_setting_view)
    LinearLayout activitySettingView;
    private int normal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_view);
        ButterKnife.bind(this);

        normal = Settings.System.getInt(getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS, 255);
        // 进度条绑定当前亮度
        seekbarSeting.setProgress(normal);
        speek.setTypeface(HdApplication.typeface);
        txtlight.setTypeface(HdApplication.typeface);
        initListener();

        switchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    HdAppConfig.setAutoPlay(true);
                    RxBus.getDefault().post(new AutoBean(1));
                    switchBtn.setBackColorRes(R.color.colorAccent);
                } else {
                    HdAppConfig.setAutoPlay(false);
                    RxBus.getDefault().post(new AutoBean(0));
                    switchBtn.setBackColorRes(R.color.white);
                }
            }
        });
        seekbarSeting.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 取得当前进度
                int tmpInt = seekBar.getProgress();

                // 当进度小于80时，设置成80，防止太黑看不见的后果。
                if (tmpInt < 30) {
                    tmpInt = 30;
                }

                // 根据当前进度改变亮度
                Settings.System.putInt(getContentResolver(),
                        Settings.System.SCREEN_BRIGHTNESS, tmpInt);
                tmpInt = Settings.System.getInt(getContentResolver(),
                        Settings.System.SCREEN_BRIGHTNESS, -1);
                WindowManager.LayoutParams wl = getWindow().getAttributes();

                float tmpFloat = (float) tmpInt / 255;
                if (tmpFloat > 0 && tmpFloat <= 1) {
                    wl.screenBrightness = tmpFloat;
                }
                getWindow().setAttributes(wl);
            }
        });
    }

    private void initListener() {
        backSetting.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (HdAppConfig.getAutoPlay()) {
            switchBtn.setChecked(true);
            switchBtn.setBackColorRes(R.color.colorAccent);
        } else {
            switchBtn.setChecked(false);
            switchBtn.setBackColorRes(R.color.white);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
