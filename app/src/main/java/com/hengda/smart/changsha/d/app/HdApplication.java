package com.hengda.smart.changsha.d.app;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.multidex.MultiDex;

import com.hengda.smart.changsha.d.common.util.CrashHandler;
import com.hengda.smart.changsha.d.common.util.SDCardUtil;

import org.litepal.LitePalApplication;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import android_serialport_api.SerialPort;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;


/**
 * Created by lenovo on 2017/2/28.
 */

public class HdApplication extends LitePalApplication {
    public static Context mContext;
    private static HdApplication instance;
    public static Typeface typeface;
    private static SerialPort mSerialPort;
    public static synchronized HdApplication getInstance() {
        return instance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mContext=getApplicationContext();
        instance = this;
        typeface = Typeface.createFromAsset(getAssets(), "fonts/fa.TTF");
//        if (SDCardUtil.isSDCardEnable()){
//
//            CrashHandler crashHandler = CrashHandler.getInstance();
//            crashHandler.init(getApplicationContext());
//        }

    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

//    /**
//     * 往Activity栈加入Activity
//     *
//     * @param activity
//     */
//    public static void addActivity(Activity activity) {
//        activities.add(activity);
//    }
//
//    /**
//     * 从Activity栈移除Activity
//     *
//     * @param activity
//     */
//    public static void removeActivity(Activity activity) {
//        activities.remove(activity);
//    }
//
//    /**
//     * 清空Activity栈
//     */
//    public static void clearActivity() {
//        Observable.from(activities)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(AndroidSchedulers.mainThread())
//                .subscribe(Activity::finish);
//    }

    /**
     * 获取串口
     *
     * @return
     * @throws SecurityException
     * @throws IOException
     * @throws InvalidParameterException
     */
    public static SerialPort getSerialPort() throws
            SecurityException,
            IOException,
            InvalidParameterException {
        if (mSerialPort == null) {
            String path = "/dev/s3c2410_serial3";
            int baudrate = 57600;
            /* Check parameters */
            if ((path.length() == 0) || (baudrate == -1)) {
                throw new InvalidParameterException();
            }
            /* Open the serial port */


            mSerialPort = new SerialPort(new File(path), baudrate, 0);

        }
        return mSerialPort;
    }
    /**
     * 关闭串口
     */
    public static void closeSerialPort() {
        if (mSerialPort != null) {
            mSerialPort.close();
            mSerialPort = null;
        }
    }
}
