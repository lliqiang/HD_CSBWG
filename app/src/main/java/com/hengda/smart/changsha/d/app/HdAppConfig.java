package com.hengda.smart.changsha.d.app;


import android.text.TextUtils;

import com.hengda.smart.changsha.d.common.util.FileUtils;
import com.hengda.smart.changsha.d.common.util.SDCardUtil;
import com.hengda.smart.changsha.d.common.util.SharedPrefUtil;


import java.io.File;


/**
 * 作者：Tailyou
 * 时间：2016/1/11 10:05
 * 邮箱：tailyou@163.com
 * 描述：恒达App配置文件
 */
public class HdAppConfig {


    private static SharedPrefUtil appConfigShare = new SharedPrefUtil(HdApplication.mContext,
            HdConstants.SHARED_PREF_NAME);

    //    SharedPref字段

    public static final String AUTO_PLAY = "AUTO_PLAY";//自动讲解
    public static final String LANGUAGE = "LANGUAGE";//语种
    public static final String USER_TYPE = "USER_TYPE";//用户类型
    public static final String PUSH_TYPE = "PUSH_TYPE";//用户类型
    public static final String GROUP_NO = "GROUP_NO";//我的同伴-群组号
    public static final String AGROUP_NO = "AGROUP_NO";//我的同伴-群组号
    public static final String NICKNAME = "NICKNAME";//我的同伴-昵称
    public static final String JOIN_GROUP_TIME = "JOIN_GROUP_TIME";//加入群组的时间
    public static final String PASSWORD = "PASSWORD";//管理员密码
    public static final String RSSI = "RSSI";//RSSI门限
    public static final String AUTO_FLAG = "AUTO_FLAG";//自动讲解：0关闭，1开启
    public static final String SMART_SERVICE = "SMART_SERVICE";//智慧服务：0关闭，1开启
    public static final String AUTO_MODE = "AUTO_MODE";//讲解方式：0隔一，1连续
    public static final String STC_MODE = "STC_MODE";//报警方式：0直接报警，1间接报警
    public static final String RECEIVE_NO_MODE = "RECEIVE_NO_MODE";//收号方式：0蓝牙，1RFID，2混合
    public static final String SCREEN_MODE = "SCREEN_MODE";//节能模式：0关闭，1开启
    public static final String POWER_MODE = "POWER_MODE";//关机权限：0禁止，1允许
    public static final String POWER_ADMIN = "POWER_MODE";//关机权限：0禁止，1允许
    public static final String POWER_PERMI = "POWER_PERMI";//禁止关机下是否获取到关机权限：0无，1有
    public static final String IS_SHOW_MSG_DIALOG = "IS_SHOW_MSG_DIALOG";//是否显示收到消息弹框
    public static final String IP_PORT = "IP_PORT";//服务器IP和端口
    public static final String MSG_ID = "MSG_ID";//聊天消息唯一标识
    public static final String HANDLER = "TEXT";
    public static final String GROUP_NM = "GROUP_NM";
    public static final String GROUP_NMC = "GROUP_NMC";
    public static final String GroupText = "GroupText";
    public static final String PICRES = "PICRES";
    public static int auto = 1;
    public static final String IS_RES = "IS_RES";
    public static final String EA_IS_RES = "EA_IS_RES";
    public static final String EC_IS_RES = "EC_IS_RES";
    public static final String CC_IS_RES = "CC_IS_RES";
    public static final String APRECIATE = "APRECIATE";
    //判断是否在下载
    public static final String IS_LOADING = "IS_LOADING";
    private static final String DEVICE = "DEVICE";
    private static final String AHEART = "HEART";
    private static final String CHEART = "HEART";


    public static void setDefaultIpPort(String ipPort) {
        appConfigShare.setPrefString(IP_PORT, ipPort);
    }

    public static String getDefaultIpPort() {
        return appConfigShare.getPrefString(IP_PORT, HdConstants.DEFAULT_IP_PORT);
    }


    public static void setStatePreciate(boolean isRes) {
        appConfigShare.setPrefBoolean(APRECIATE, isRes);

    }


    public static void setDeviceNo(String deviceNo) {
        FileUtils.writeStringToFile(SDCardUtil.getSDCardPath() + "DeviceNo.txt", deviceNo, false);
    }

    public static String getDeviceNo() {
        StringBuilder deviceNo = FileUtils.readStringFromFile(SDCardUtil.getSDCardPath() + "DeviceNo.txt", "UTF-8");
        return TextUtils.isEmpty(deviceNo) ? HdConstants.DEFAULT_DEVICE_NO : deviceNo.toString();
    }

    public static void setText(String handler) {
        appConfigShare.setPrefString(HANDLER, handler);
    }

    public static void setLanguage(String language) {
        appConfigShare.setPrefString(LANGUAGE, language);
    }

    public static String getLanguage() {
        return appConfigShare.getPrefString(LANGUAGE, HdConstants.LANG_DEFAULT);
    }

    public static void setAutoPlay(boolean flag) {
        appConfigShare.setPrefBoolean(AUTO_PLAY, flag);
    }

    public static boolean getAutoPlay() {
        return appConfigShare.getPrefBoolean(AUTO_PLAY, false);
    }

    public static void setClientId(String userType) {
        appConfigShare.setPrefString(PUSH_TYPE, userType);
    }

    public static String getClientId() {
        return appConfigShare.getPrefString(PUSH_TYPE, HdConstants.ADULT);
    }

    public static void setJoinGroupTime(long joinGroupTime) {
        appConfigShare.setPrefLong(JOIN_GROUP_TIME, joinGroupTime);
    }

    public static long getJoinGroupTime() {
        return appConfigShare.getPrefLong(JOIN_GROUP_TIME);
    }

    public static void setNickname(String nickname) {
        appConfigShare.setPrefString(NICKNAME, nickname);
    }

    public static String getNickname() {
        return appConfigShare.getPrefString(NICKNAME, getDeviceNo());
    }

    public static void setIsLoading(boolean isLoading) {
        appConfigShare.setPrefBoolean(IS_LOADING, isLoading);
    }

    public static boolean isLoading() {
        return appConfigShare.getPrefBoolean(IS_LOADING, false);
    }


    public static void setPassword(String password) {
        appConfigShare.setPrefString(PASSWORD, password);
    }

    public static String getPassword() {
        return appConfigShare.getPrefString(PASSWORD, HdConstants.DEFAULT_PWD);
    }

    public static void setRssi(int rssi) {
        appConfigShare.setPrefInt(RSSI, rssi);
    }

    public static int getRssi() {
        return appConfigShare.getPrefInt(RSSI, HdConstants.BLE_RSSI_THRESHOLD);
    }

    public static void setSmartService(int smartService) {
        appConfigShare.setPrefInt(SMART_SERVICE, smartService);
    }

    public static int getSmartService() {
        return appConfigShare.getPrefInt(SMART_SERVICE, 1);
    }

    public static void setAutoFlag(int autoFlag) {
        appConfigShare.setPrefInt(AUTO_FLAG, autoFlag);
    }

    public static int getAutoFlag() {
        return appConfigShare.getPrefInt(AUTO_FLAG, 1);
    }

    public static void setAutoMode(int autoMode) {
        appConfigShare.setPrefInt(AUTO_MODE, autoMode);
    }

    public static int getAutoMode() {
        return appConfigShare.getPrefInt(AUTO_MODE, 0);
    }

    public static void setSTCMode(int flag) {
        appConfigShare.setPrefInt(STC_MODE, flag);
    }

    public static int getSTCMode() {
        return appConfigShare.getPrefInt(STC_MODE, 1);
    }

    public static void setReceiveNoMode(int receiveNoMode) {
        appConfigShare.setPrefInt(RECEIVE_NO_MODE, receiveNoMode);
    }

    public static int getReceiveNoMode() {
        return appConfigShare.getPrefInt(RECEIVE_NO_MODE, 1);
    }

    public static void setScreenMode(int flag) {
        appConfigShare.setPrefInt(SCREEN_MODE, flag);
    }

    public static int getScreenMode() {
        return appConfigShare.getPrefInt(SCREEN_MODE, 1);
    }

    public static void setPowerMode(int flag) {
        appConfigShare.setPrefInt(POWER_MODE, flag);
    }

    public static int getPowerMode() {
        return appConfigShare.getPrefInt(POWER_MODE, 1);
    }


    public static void setAdminPower(int flag) {
        appConfigShare.setPrefInt(POWER_ADMIN, flag);
    }

    public static int getAdminPower() {
        return appConfigShare.getPrefInt(POWER_ADMIN, 0);
    }


    public static void setPowerPermi(int flag) {
        appConfigShare.setPrefInt(POWER_PERMI, flag);
    }

    public static int getPowerPermi() {
        return appConfigShare.getPrefInt(POWER_PERMI, 0);
    }

    public static void setIsShowMsgDialog(boolean isShowMsgDialog) {
        appConfigShare.setPrefBoolean(IS_SHOW_MSG_DIALOG, isShowMsgDialog);
    }

    public static boolean isShowMsgDialog() {
        return appConfigShare.getPrefBoolean(IS_SHOW_MSG_DIALOG, true);
    }

//    public static void setDefaultInnerNetIp(String ipPort) {
//        appConfigShare.setPrefString(IP_PORT, ipPort);
//    }
//
//    public static String getDefaultInnerNetIp() {
//        return appConfigShare.getPrefString(IP_PORT, HdConstants.DEFAULT_IP_PORT_I);
//    }

    public static String getMsgId() {
        return appConfigShare.getPrefString(MSG_ID, "");
    }

    public static void setMsgId(String msgId) {
        appConfigShare.setPrefString(MSG_ID, msgId);
    }

    public static void setNtcReaded(String ntcUrl, boolean value) {
        appConfigShare.setPrefBoolean(ntcUrl, value);
    }

    public static boolean isNtcReaded(String ntcUrl) {
        return appConfigShare.getPrefBoolean(ntcUrl, true);
    }

    //    获取默认文件存储目录
    public static String getDefaultFileDir() {
        return SDCardUtil.getSDCardPath() + "HD_SSBWG_RES/";
    }

    //获取展品根路径
    public static String getFilePath(String exhibit_id) {
        return getDefaultFileDir() + "exhibit/" + exhibit_id + "/" + getLanguage() + "/";
    }

    //获取特展厅的路径
//获取展品根路径
    public static String getTempPath(String exhibit_id) {
        return getDefaultFileDir() + "te_exhibit/" + exhibit_id + "/" + getLanguage() + "/";
    }

    //获取展品图片根路径
    public static String getImgPath(String exhibit_id) {
        return getDefaultFileDir() + "exhibit/" + exhibit_id + "/" + "images/";
    }

    //获取地图图片根路径
    public static String getMapFilePath(String language, int unitno) {
        return getDefaultFileDir() + "map/" + language + "/" + unitno;
    }

    //获取展厅图片根路径
    public static String getExhibitionFilePath(String language, int unitno) {
        return getDefaultFileDir() + "exhibition/" + unitno;
    }

    public static String getGroupName() {
        return appConfigShare.getPrefString(GROUP_NM, "");
    }

    public static void setCGroupName(String groupnm) {
        appConfigShare.setPrefString(GROUP_NMC, groupnm);
    }

    public static String getCGroupName() {
        return appConfigShare.getPrefString(GROUP_NMC, "");
    }

    public static void setGroupName(String groupnm) {
        appConfigShare.setPrefString(GROUP_NM, groupnm);
    }

    //    获取数据库文件路径
    public static String getDbFilePath() {

        return getDefaultFileDir() + HdConstants.DB_FILE_NAME;
    }

    //判断数据库是否存在
    public static boolean isDbExist() {
        File file = new File(getDefaultFileDir() + HdConstants.DB_FILE_NAME);
        return file.exists();
    }

    //判断数据库是否存在
    public static boolean isResExist() {
        File file = new File(getMapFilePath(getLanguage(), 2) + "/" + "list.png");
        return file.exists();
    }
//http://hengdawb-res.oss-cn-hangzhou.aliyuncs.com/HD-GXKJG-RES%2FCHINESE%2Fadult%2Fmap.zip
//        获取地图文件路径

    public static void clear() {
        appConfigShare.clearPreference();
    }

    //判断缩略图是否存在
    public static boolean isPictureExist(String fileNO) {
        String path = HdAppConfig.getDefaultFileDir() + "picture" + "/" + fileNO;
        File file = new File(path);
        return file.exists();
    }

    //    获取默认文件存储目录
    public static String getPicturePath(String fileNo) {
        return getDefaultFileDir() + fileNo + ".png";
    }

//    /**
//     * 显示加载 ProgressDialog，圆形进度
//     *
//     * @param context
//     */
//    public static void showDownloadProgressDialog(Activity context, String msg) {
//        hideDownloadProgressDialog();
//        progressDialog = new HProgressDialog(context);
//        progressDialog
//                .message(msg)
//                .tweenAnim(R.mipmap.progress_roate, R.anim.progress_rotate)
//                .outsideCancelable(false)
//                .cancelable(false)
//                .show();
//    }

//    /**
//     * 隐藏 ProgressDialog
//     */
//    public static void hideDownloadProgressDialog() {
//        if (progressDialog != null) {
//            progressDialog.dismiss();
//            progressDialog = null;
//        }
//    }
}
