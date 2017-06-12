package com.hengda.smart.changsha.d.common.util;

import android.text.TextUtils;

import com.hengda.smart.changsha.d.app.HdApplication;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * 作者：祝文飞（Tailyou）
 * 邮箱：tailyou@163.com
 * 时间：2017/2/15 13:37
 * 描述：
 */

public class SystemUtil {

    /**
     * 检查是否有可用网络
     *
     * @return
     */
    public static boolean isNetworkConnected() {
        return NetUtil.isConnected(HdApplication.getInstance().getApplicationContext());
    }

    /**
     * 检查WIFI是否连接
     *
     * @return
     */
    public static boolean isWifiConnected() {
        return NetUtil.isWifi(HdApplication.getInstance().getApplicationContext());
    }

    /**
     * 检查是否内网连接
     *
     * @return
     */
//    public static boolean isNetworkIntranet() {
//        String wifiSSID = NetUtil.getWifiSSID(HdApplication.getInstance().getApplicationContext());
//        return SystemUtil.isWifiConnected() && (wifiSSID.contains(HdConstants.MUSEUM_DEFAULT_SSID) || wifiSSID.contains(HdConstants.HD_DEFAULT_SSID));
//    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    public static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

}
