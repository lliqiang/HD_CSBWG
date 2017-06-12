package com.hengda.smart.changsha.d.rfid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.hengda.smart.changsha.d.common.util.NetUtil;


/**
 * Created by lenovo on 2017/3/13.
 */

public class NetBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            int netWorkState = NetUtil.getNetWorkState(context);
            // 接口回调传过去状态的类型

        }
    }
    // 自定义接口
    public interface NetEvevt {
        public void onNetChange(int netMobile);

}
}
