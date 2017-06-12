package com.hengda.smart.changsha.d.rfid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by lenovo on 2017/3/11.
 */

public class BatteryReceiver extends BroadcastReceiver {
    private int mBatteryLevel;
    private int mBatteryScale;
    private MyListener myListener;
    @Override
    public void onReceive(Context context, Intent intent) {
        //判断它是否是为电量变化的Broadcast Action
        if(Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())){
            //获取当前电量
            mBatteryLevel = intent.getIntExtra("level", 0);
            //电量的总刻度
            mBatteryScale = intent.getIntExtra("scale", 100);
        }
    }
    public interface MyListener {
        public void onListener(String level);
    }
    public void setMyListener(MyListener myListener) {
        this.myListener = myListener;
    }
}
