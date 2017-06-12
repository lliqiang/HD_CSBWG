package com.hengda.smart.changsha.d.app;

import android.os.Environment;

import java.io.File;

/**
 * 作者：Tailyou （祝文飞）
 * 时间：2016/5/27 17:19
 * 邮箱：tailyou@163.com
 * 描述：全局常量
 */
public class HdConstants {

    //    App更新相关常量
    public static String APP_KEY = "594a51ffbd2c3b350357825c83ce51f8";
    public static String APP_SECRET = "3d99bffa50a1f76237c3ac3d37f4e21e";
    public static String APP_UPDATE_URL = "http://101.200.234.14/APPCloud/";

    //    App更新相关常量
    public static final int APP_KIND = 1; //1:Android, 2:iOS
    public static final String START_ALARM = "1";
    public static final String STOP_ALARM = "2";


    //    Cache Path
    public static final String PATH_DATA = HdApplication.getInstance().getCacheDir().getAbsolutePath() + File.separator + "data";
    public static final String PATH_CACHE = PATH_DATA + "/NetCache";

    //    阿里云资源下载
//    public static String RES_LOAD_URL = "http://hengdawb-res.oss-cn-hangzhou.aliyuncs" +
//            ".com/GuangXiTech_Res/";
    public static String RES_LOAD_URL = "http://hengdawb-res.oss-cn-hangzhou.aliyuncs.com/HD-GXKJG-RES/";
    public static String DB_LOAD_URL = "http://hengdawb-res.oss-cn-hangzhou.aliyuncs.com/HD-GXKJG-RES/";

    public static String DB_URL="http://hengdawb-res.oss-cn-hangzhou.aliyuncs.com/HD-GXKJG-RES/DATABASE.zip";
    public static String RES_URL="http://hengdawb-res.oss-cn-hangzhou.aliyuncs.com/HD-GXKJG-RES/Chinese/Chinese.zip";

//
//    测试平台包名
public static final String TEST_APP_PACKAGE_NAME = "com.hengda.apptest";
    //    测试平台LauncherActivity
    public static final String TEST_APP_LAUNCHER_ACT = "com.hengda.apptest.HD_AppTestActivity";
    public static String getDefaultFileDir() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/HD_SSBWG_RES/";
    }



    //    语言
    public static final String LANG_CHINESE = "Chinese";
    public static final String LANG_ENGLISH = "English";
    public static final String LANG_DEFAULT = "CHINESE";

    //    用户类型
    public static final String ADULT = "adult";
    public static final String CHILD = "child";
    //    SharedPrf配置文件名称
    public static final String SHARED_PREF_NAME = "HD_GXT_PREF";

    //    数据库文件名称
    public static final String DB_FILE_NAME = "Filemanage.s3db";

    //    蓝牙RSSI门限
    public static final int BLE_RSSI_THRESHOLD = -69;

    //    请求成功状态码
    public static final String HTTP_STATUS_SUCCEED = "000";
    //请求状态
    public static final String HTTP_STATE = "1";
    //    万能登录密码
    public static final String LOGIN_PWD = "8888";
    //    默认管理员密码
    public static final String DEFAULT_PWD = "9999";
    //    默认设备号
    public static final String DEFAULT_DEVICE_NO = "AG10000000000";

    //    默认IP和端口，默认端口80可以省略
    //    馆方内网-默认网络请求服务器地址
    public static final String DEFAULT_IP_PORT_I = "192.168.10.27/12345/";

//    public static final String DEFAULT_IP_PORT = "192.168.10.20:80";
    public static final String DEFAULT_IP_PORT = "172.16.102.11:80";
//    public static final String DEFAULT_IP_PORT = "222.240.99.67:65523";


    public static final int LOADING_DB = 11;
    public static final int LOADING_RES = 12;

}
