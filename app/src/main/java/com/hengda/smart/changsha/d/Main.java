package com.hengda.smart.changsha.d;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.hengda.smart.changsha.d.app.HdAppConfig;
import com.hengda.smart.changsha.d.app.HdApplication;
import com.hengda.smart.changsha.d.rfid.NetStateMonitor;
import com.hengda.smart.changsha.d.ui.BaseActivity;
import com.hengda.smart.changsha.d.ui.DigitalActivity;
import com.hengda.smart.changsha.d.ui.IntroActicity;
import com.hengda.smart.changsha.d.ui.ListTipActivity;
import com.hengda.smart.changsha.d.ui.MapActivity;
import com.hengda.smart.changsha.d.ui.SettingView;
import com.hengda.smart.changsha.d.widget.dialog.WelcomeDialog;
import butterknife.Bind;
import butterknife.ButterKnife;
public class Main extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.digital_play)
    ImageView digitalPlay;
    @Bind(R.id.map_guide)
    ImageView mapGuide;
    @Bind(R.id.plafrom_intro)
    ImageView plafromIntro;
    @Bind(R.id.list_wenwu)
    ImageView listWenwu;
    @Bind(R.id.setting_btn)
    ImageView settingBtn;
    @Bind(R.id.txt_digital)
    TextView txtDigital;
    @Bind(R.id.txt_map)
    TextView txtMap;
    @Bind(R.id.tzt_intro)
    TextView tztIntro;
    @Bind(R.id.txt_list)
    TextView txtList;
    @Bind(R.id.wifi_img)
    ImageView wifiImg;
    @Bind(R.id.voice_img)
    ImageView voiceImg;
    @Bind(R.id.auto_img)
    ImageView autoImg;
    @Bind(R.id.battery_img)
    ImageView batteryImg;
    @Bind(R.id.volumn)
    TextView volumn;
    private int intLevel;
    private int intScale;
    static AudioManager mAudioManager;
    private WelcomeDialog welcomeDialog;
    public int current;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (intent.ACTION_BATTERY_CHANGED.equals(action)) {
                intLevel = intent.getIntExtra("level", 0);
                intScale = intent.getIntExtra("scale", 100);
                showBatteryState(batteryImg, intScale, intLevel);
            }
        }
    };
    private NetStateMonitor netStateMonitor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registerNetStateMonitor();
        welcomeDialog = new WelcomeDialog(this);
        welcomeDialog.message(getString(R.string.welcome_cs))
                .setTypeface(HdApplication.typeface)
                .outsideCancelable(false)
                .pBtnText(getString(R.string.sure))
                .pBtnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        welcomeDialog.dismiss();
                    }
                }).show();
        mAudioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        current = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        ButterKnife.bind(this);
        registerReceiver(broadcastReceiver, new IntentFilter(
                Intent.ACTION_BATTERY_CHANGED));
        txtDigital.setTypeface(HdApplication.typeface);
        txtMap.setTypeface(HdApplication.typeface);
        txtList.setTypeface(HdApplication.typeface);
        tztIntro.setTypeface(HdApplication.typeface);
        initListener();
        mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        current = (int) ((double) (mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC)) * 100 / 60);
        volumn.setText(current + "");

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (HdAppConfig.getAutoFlag()==1) {
            if (HdAppConfig.auto == 1) {
                autoImg.setImageResource(R.drawable.auto_voice);
            } else {
                autoImg.setImageResource(R.drawable.auto_close);
            }
        } else {
            autoImg.setImageResource(R.drawable.auto_close);
        }
    }

    private void initListener() {
        digitalPlay.setOnClickListener(this);
        mapGuide.setOnClickListener(this);
        plafromIntro.setOnClickListener(this);
        listWenwu.setOnClickListener(this);
        settingBtn.setOnClickListener(this);
        txtDigital.setOnClickListener(this);
        txtList.setOnClickListener(this);
        txtMap.setOnClickListener(this);
        tztIntro.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.digital_play:
                openActivity(Main.this, DigitalActivity.class);
                break;
            case R.id.map_guide:
                openActivity(Main.this, MapActivity.class);
                break;
            case R.id.plafrom_intro:
                openActivity(Main.this, IntroActicity.class);
                break;
            case R.id.list_wenwu:
                openActivity(Main.this, ListTipActivity.class);
                break;
            case R.id.setting_btn:
                openActivity(Main.this, SettingView.class);
                break;
            case R.id.txt_digital:
                openActivity(Main.this, DigitalActivity.class);
                break;
            case R.id.txt_map:
                openActivity(Main.this, MapActivity.class);
                break;
            case R.id.tzt_intro:
                openActivity(Main.this, IntroActicity.class);
                break;
            case R.id.txt_list:
                openActivity(Main.this, ListTipActivity.class);
                break;

        }
    }

    public void showBatteryState(ImageView imageView, int intScale, int intLevel) {
        if ((intLevel * 100) / intScale == 100) {
            imageView.setImageResource(R.drawable.battery_full);
        } else if ((intLevel * 100) / intScale < 100 && (intLevel * 100) / intScale > 67) {
            imageView.setImageResource(R.drawable.battery_two);
        } else if ((intLevel * 100) / intScale < 67 && (intLevel * 100) / intScale > 33) {
            imageView.setImageResource(R.drawable.battery_one);
        } else {
            imageView.setImageResource(R.drawable.battery_empty);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
        unregisterReceiver(netStateMonitor);
        ButterKnife.unbind(this);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_DOWN:


                if (current > 0) {
                    current = current - 10;
                    if (current == 0) {
                        voiceImg.setImageResource(R.drawable.voice_close);
                    }
                } else if (current <= 0) {
                    current = 0;
                    voiceImg.setImageResource(R.drawable.voice_close);
                }
                volumn.setText(current + "");
                return true;
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (current >= 100) {
                    current = 100;
                } else {
                    current = current + 10;
                }
                volumn.setText(current + "");
                voiceImg.setImageResource(R.drawable.voice_open);
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 注册网络状态监听器
     */
    private void registerNetStateMonitor() {
        netStateMonitor = new NetStateMonitor() {
            @Override
            public void onConnected() {
                if (wifiImg != null) {
                    wifiImg.setImageResource(R.drawable.wifi_open);
                }
            }

            @Override
            public void onDisconnected() {

                if (wifiImg != null) {

                    wifiImg.setImageResource(R.drawable.wifi_close);
                }

            }
        };
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netStateMonitor, mFilter);
    }
}
