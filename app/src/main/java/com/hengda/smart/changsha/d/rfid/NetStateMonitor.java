package com.hengda.smart.changsha.d.rfid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hengda.smart.changsha.d.common.util.NetUtil;

public abstract class NetStateMonitor extends BroadcastReceiver {

    public abstract void onConnected();

    public abstract void onDisconnected();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (NetUtil.isConnected(context)) {
            onConnected();
        } else {
            onDisconnected();
        }
    }

}
