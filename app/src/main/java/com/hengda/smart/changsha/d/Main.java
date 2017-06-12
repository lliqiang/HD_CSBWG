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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hengda.smart.changsha.d.admin.CheckCallback;
import com.hengda.smart.changsha.d.app.HdAppConfig;
import com.hengda.smart.changsha.d.app.HdApplication;
import com.hengda.smart.changsha.d.common.util.RxBus;
import com.hengda.smart.changsha.d.model.AutoBean;
import com.hengda.smart.changsha.d.model.CheckResponse;
import com.hengda.smart.changsha.d.model.VoiceBean;
import com.hengda.smart.changsha.d.rfid.NetStateMonitor;
import com.hengda.smart.changsha.d.ui.BaseActivity;
import com.hengda.smart.changsha.d.ui.DigitalActivity;
import com.hengda.smart.changsha.d.ui.IntroActicity;
import com.hengda.smart.changsha.d.ui.LaucherActivity;
import com.hengda.smart.changsha.d.ui.ListTipActivity;
import com.hengda.smart.changsha.d.ui.MapActivity;
import com.hengda.smart.changsha.d.ui.SettingView;
import com.hengda.smart.changsha.d.widget.dialog.NotifyDialog;
import com.hengda.smart.changsha.d.widget.dialog.WelcomeDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.functions.Action1;

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
    //    @Bind(R.id.scan)
//    ImageView scan;
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
    AudioManager mAudioManager;
    private WelcomeDialog welcomeDialog;
    private int current;
    private int netMobile;
    public MyVolumeReceiver mVolumeReceiver;
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
        EventBus.getDefault().register(this);
        myRegisterReceiver();
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
        mAudioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE) ;
        ButterKnife.bind(this);
        registerReceiver(broadcastReceiver, new IntentFilter(
                Intent.ACTION_BATTERY_CHANGED));
        txtDigital.setTypeface(HdApplication.typeface);
        txtMap.setTypeface(HdApplication.typeface);
        txtList.setTypeface(HdApplication.typeface);
        tztIntro.setTypeface(HdApplication.typeface);
        initListener();
        RxBus.getDefault().toObservable(AutoBean.class).subscribe(new Action1<AutoBean>() {
            @Override
            public void call(AutoBean voiceBean) {
                if (voiceBean.getVolunm() == 0) {
                    autoImg.setImageResource(R.drawable.auto_close);
                } else {
                    autoImg.setImageResource(R.drawable.auto_voice);
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

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
//            case R.id.scan:
//                openActivity(Main.this, BarCodeActivity.class);
//                break;
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainEventBus(VoiceBean bean) {
        if (bean.getVolunm() == 0) {
            voiceImg.setImageResource(R.drawable.voice_close);
        } else {
            voiceImg.setImageResource(R.drawable.voice_open);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unregisterReceiver(mVolumeReceiver);
        unregisterReceiver(broadcastReceiver);
        unregisterReceiver(netStateMonitor);
        ButterKnife.unbind(this);
    }

    public class MyVolumeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //如果音量发生变化则更改seekbar的位置
            if (intent.getAction().equals("android.media.VOLUME_CHANGED_ACTION")) {

                 current = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);// 当前的媒体音量
                volumn.setText(current + "");
            }
        }
    }

//    public class WifiReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_DISABLED);
//            Log.e("", "wifiState:" + wifiState);
//            switch (wifiState) {
//                case WifiManager.WIFI_STATE_DISABLED:
//                    wifiImg.setImageResource(R.drawable.wifi_close);
//                    break;
//                case WifiManager.WIFI_STATE_ENABLED:
//                    wifiImg.setImageResource(R.drawable.wifi_open);
//                    break;
//            }
//        }
//    }

    /**
     * 注册当音量发生变化时接收的广播
     */
    private void myRegisterReceiver() {
        mVolumeReceiver = new MyVolumeReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.media.VOLUME_CHANGED_ACTION");
        registerReceiver(mVolumeReceiver, filter);

    }





    /**
     * 注册当wifi发生变化时接收的广播
     */
//    private void myWifiReceiver() {
//        wifiReceiver = new WifiReceiver();
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
//        registerReceiver(wifiReceiver, filter);
//    }
    /**
     * 注册网络状态监听器
     */
    private void registerNetStateMonitor() {
        netStateMonitor = new NetStateMonitor() {
            @Override
            public void onConnected() {
                if (wifiImg!=null){

                    wifiImg.setImageResource(R.drawable.wifi_open);
                }
            }

            @Override
            public void onDisconnected() {

                if (wifiImg!=null){

                    wifiImg.setImageResource(R.drawable.wifi_close);
                }

            }
        };
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netStateMonitor, mFilter);
    }
}
