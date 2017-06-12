package com.hengda.smart.changsha.d.rfid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.hengda.smart.changsha.d.common.util.NetUtil;
import com.hengda.smart.changsha.d.common.util.RxBus;
import com.hengda.smart.changsha.d.model.NetBean;
import com.hengda.smart.changsha.d.model.VoiceBean;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by lenovo on 2017/3/13.
 */

public class MyBroadcastReceiver extends BroadcastReceiver {
    AudioManager audioManager;



    @Override
    public void onReceive(Context context, Intent intent) {

        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int wifistate = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_DISABLED);
            switch (wifistate) {
                case WifiManager.WIFI_STATE_DISABLED:
                    EventBus.getDefault().post(new NetBean(2));
                    break;
                case WifiManager.WIFI_STATE_ENABLED:
                    EventBus.getDefault().post(new NetBean(1));
                    break;
            }

    }


    ;

}
