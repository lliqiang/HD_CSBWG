package com.hengda.smart.changsha.d.admin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.hengda.smart.changsha.d.R;

import com.hengda.smart.changsha.d.app.HdAppConfig;
import com.hengda.smart.changsha.d.app.HdConstants;
import com.hengda.smart.changsha.d.common.util.AppUtil;
import com.hengda.smart.changsha.d.common.util.DataManager;
import com.hengda.smart.changsha.d.common.util.FileUtils;
import com.hengda.smart.changsha.d.common.util.GlideCacheUtil;
import com.hengda.smart.changsha.d.common.util.NetUtil;
import com.hengda.smart.changsha.d.common.util.RxBus;
import com.hengda.smart.changsha.d.common.util.ViewUtil;
import com.hengda.smart.changsha.d.common.util.ZipUtil;
import com.hengda.smart.changsha.d.dbase.HResDdUtil;
import com.hengda.smart.changsha.d.http.FileApi;
import com.hengda.smart.changsha.d.http.FileCallback;
import com.hengda.smart.changsha.d.http.HttpMethods;
import com.hengda.smart.changsha.d.http.RequestApi;
import com.hengda.smart.changsha.d.http.RequestSubscriber;
import com.hengda.smart.changsha.d.http.ResUpdate;
import com.hengda.smart.changsha.d.model.AppBean;
import com.hengda.smart.changsha.d.model.Chinese;
import com.hengda.smart.changsha.d.model.CheckResponse;
import com.hengda.smart.changsha.d.model.DataModel;
import com.hengda.smart.changsha.d.model.English;
import com.hengda.smart.changsha.d.model.Japanese;
import com.hengda.smart.changsha.d.model.Korean;
import com.hengda.smart.changsha.d.model.Lchinse;
import com.hengda.smart.changsha.d.model.Lenglish;
import com.hengda.smart.changsha.d.model.Ljapanese;
import com.hengda.smart.changsha.d.model.Lkorean;
import com.hengda.smart.changsha.d.model.MapBase;
import com.hengda.smart.changsha.d.model.MapModel;
import com.hengda.smart.changsha.d.model.MapUpdate;
import com.hengda.smart.changsha.d.model.Map_info;
import com.hengda.smart.changsha.d.ui.BaseActivity;
import com.hengda.smart.changsha.d.widget.dialog.DialogCenter;

import com.hengda.smart.changsha.d.widget.dialog.DialogClickListener;
import com.orhanobut.logger.Logger;

import org.litepal.tablemanager.Connector;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import zlc.season.rxdownload.RxDownload;
import zlc.season.rxdownload.entity.DownloadStatus;

/**
 * 作者：Tailyou （祝文飞）
 * 时间：2016/10/9 10:27
 * 邮箱：tailyou@163.com
 * 描述：
 */
public class CheckUpdateActivity extends BaseActivity {

    TextView txtProgress;
    TextView txtUpdateLog;
    SQLiteDatabase db;
    private final int update_db_success = 1000;
    private final int update_res_success = 2000;
    private final int update_res_loading = 3000;
    private Subscription apkDownloader;
    private Subscription resDownloader;
    private AppBean mAppBean;
    public WifiReceiver wifiReceiver;
    private List<MapUpdate.DataBean> dataBeanList;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case update_db_success:
                    DialogCenter.hideProgressDialog();
                    showSuccessInfoDialog("更新数据库成功");
                    break;
                case update_res_success:
                    DialogCenter.hideProgressDialog();
                    showSuccessInfoDialog("更新资源成功");
                    break;
                case update_res_loading:
                    txtProgress.setText(msg.obj.toString());
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 检查新版本
     */
    public void checkNewVersion(final CheckCallback callback) {
        if (NetUtil.isConnected(CheckUpdateActivity.this)) {
            RequestApi.getInstance(HdConstants.APP_UPDATE_URL)
                    .checkVersion(new RequestSubscriber<com.hengda.smart.changsha.d.model.CheckResponse>() {
                        @Override
                        public void succeed(com.hengda.smart.changsha.d.model.CheckResponse checkResponse) {
                            super.succeed(checkResponse);
                            switch (checkResponse.getStatus()) {
                                case "2001":
                                    callback.isAlreadyLatestVersion();
                                    break;
                                case "2002":
                                    callback.hasNewVersion(checkResponse);
                                    break;
                                case "4041":
                                    break;
                            }
                        }
                    });
        }


    }

    public void showResInfoDialog(String resId) {
        DialogCenter.showDialog(CheckUpdateActivity.this, new DialogClickListener() {
            @Override
            public void p() {
                DialogCenter.hideDialog();
            }
        }, new String[]{"资源更新", "当前已是最新版，版本号：" + resId, "取消"});
    }

    public void showSuccessInfoDialog(String string) {
        DialogCenter.showDialog(CheckUpdateActivity.this, new DialogClickListener() {
            @Override
            public void p() {
                DialogCenter.hideDialog();
            }
        }, new String[]{"提示", string, "确定"});
    }


    /**
     * 从接口中获取数据写入到数据库
     */
    public void getDBDataAndInsertToDB() {
        DialogCenter.showProgressDialog(CheckUpdateActivity.this, "更新数据库文件", false);
        RequestApi.getInstance().getDBData(new Subscriber<DataModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(CheckUpdateActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.i("Throwable", "Throwable:-------------" + e.toString());
                DialogCenter.hideProgressDialog();
            }

            @Override
            public void onNext(DataModel data) {
                Observable.just(data)
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .subscribe(DataMoel -> {
                            resetDb(data);
                        });
            }
        });

    }

    private void loadAndInstall(AppBean appBean) {
        this.mAppBean = appBean;
        loadAndInstall();
    }

    private void loadAndInstall() {
        String apkUrl = mAppBean.getVersionInfo().getVersionUrl();
        String baseUrl = apkUrl.substring(0, apkUrl.lastIndexOf("/") + 1);
        String fileName = apkUrl.substring(apkUrl.lastIndexOf("/") + 1);
        String fileStoreDir = HdAppConfig.getDefaultFileDir();

        apkDownloader = RxDownload.getInstance().download(apkUrl, fileName, fileStoreDir)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<DownloadStatus>() {
                    @Override
                    public void call(DownloadStatus downloadStatus) {
                        txtProgress.setText(String.format("正在下载(%s/%s)",
                                DataManager.getFormatSize(downloadStatus.getDownloadSize()),
                                DataManager.getFormatSize(downloadStatus.getTotalSize())));
                    }
                }, throwable -> {
                    Logger.e(throwable.getMessage());
                    Toast.makeText(CheckUpdateActivity.this, "下载出错", Toast.LENGTH_SHORT).show();
                    DialogCenter.hideDialog();
                }, () -> {
                    Logger.e("下载成功");
                    DialogCenter.hideDialog();
                    AppUtil.installApk(CheckUpdateActivity.this, fileStoreDir + fileName);
                });

    }


    private void resetDb(DataModel data) {
        db = Connector.getDatabase();
        HResDdUtil.deleteTable(db, Chinese.class.getSimpleName());
        HResDdUtil.deleteTable(db, English.class.getSimpleName());
        HResDdUtil.deleteTable(db, Japanese.class.getSimpleName());
        HResDdUtil.deleteTable(db, Korean.class.getSimpleName());
        HResDdUtil.deleteTable(db, MapModel.class.getSimpleName());

        HResDdUtil.deleteTable(db, Lchinse.class.getSimpleName());
        HResDdUtil.deleteTable(db, Lenglish.class.getSimpleName());
        HResDdUtil.deleteTable(db, Ljapanese.class.getSimpleName());
        HResDdUtil.deleteTable(db, Lkorean.class.getSimpleName());


        for (Chinese chinese : data.getExhibit_info().getCHINESE()) {

            chinese.save();
        }
        for (English english : data.getExhibit_info().getENGLISH()) {
            english.save();
        }
        for (Japanese japanese : data.getExhibit_info().getJAPANESE()) {
            japanese.save();
        }
        for (Korean korean : data.getExhibit_info().getKOREAN()) {
            korean.save();
        }
        for (MapModel mapModel : data.getExhibit_info().getMap_info()) {
            mapModel.save();
        }
        for (Lchinse lchinese: data.getTe_exhibit_info().getCHINESE()) {
            lchinese.save();
        }
        for (Lenglish lenglish: data.getTe_exhibit_info().getENGLISH()) {
            lenglish.save();
        }
        for (Ljapanese ljapanese: data.getTe_exhibit_info().getJAPANESE()) {
            ljapanese.save();
        }
        for (Lkorean lkorean: data.getTe_exhibit_info().getKOREAN()) {
            lkorean.save();
        }
        FileUtils.deleteFile(HdAppConfig.getDbFilePath());
        boolean b = FileUtils.copyFile(db.getPath(), HdAppConfig.getDbFilePath());
        if (b) mHandler.sendEmptyMessage(update_db_success);
    }

    /**
     * 有新版本时显示Dialog
     *
     * @param checkResponse
     */
    public void showHasNewVersionDialog(final CheckResponse checkResponse) {
        ScrollView scrollView = (ScrollView) View.inflate(CheckUpdateActivity.this, R.layout
                .dialog_custom_view_scroll_txt, null);
        txtUpdateLog = ViewUtil.getView(scrollView, R.id.tvUpdateLog);
        txtUpdateLog.setText("检查到新版本：" + checkResponse.getVersionInfo().getVersionName() + "\n更新日志：\n"
                + checkResponse.getVersionInfo().getVersionLog());

        DialogCenter.showDialog(CheckUpdateActivity.this, scrollView, new com.hengda.smart.changsha.d.widget.dialog.DialogClickListener() {
            @Override
            public void p() {
                super.p();
                showDownloadingDialog();
                loadAndInstall(checkResponse);
            }

            @Override
            public void n() {
                super.n();
                DialogCenter.hideDialog();
            }
        }, new String[]{"版本更新", "更新", "取消"});

    }

    /**
     * 显示下载Apk Dialog
     */
    private void showDownloadingDialog() {
        txtProgress = (TextView) View.inflate(CheckUpdateActivity.this, R.layout
                .dialog_custom_view_txt, null);

        txtProgress.setText("下载安装包...");


        DialogCenter.showDialog(CheckUpdateActivity.this, txtProgress, new DialogClickListener() {
            @Override
            public void p() {
                DialogCenter.hideDialog();
                FileApi.cancelLoading();
            }

            @Override
            public void n() {
            }
        }, new String[]{"下载更新", "取消"});
    }

    /**
     * 显示下载Apk Dialog
     */
    private void showDownloadingDialog(String s) {
        txtProgress = (TextView) View.inflate(CheckUpdateActivity.this, R.layout
                .dialog_custom_view_txt, null);
        txtProgress.setText(s);
        DialogCenter.showDialog(CheckUpdateActivity.this, txtProgress, new DialogClickListener() {
            @Override
            public void p() {
                if (apkDownloader != null && !apkDownloader.isUnsubscribed()) {
                    apkDownloader.unsubscribe();
                }
                if (resDownloader != null && !resDownloader.isUnsubscribed()) {
                    resDownloader.unsubscribe();
                }
                DialogCenter.hideDialog();
            }

            @Override
            public void n() {

            }
        }, new String[]{"下载更新", "取消"});
    }

    public void showHasNewResVersionDialog(ResUpdate.DataBean resData) {
        ScrollView scrollView = (ScrollView) View.inflate(CheckUpdateActivity.this, R.layout
                .dialog_custom_view_scroll_txt, null);
        txtUpdateLog = ViewUtil.getView(scrollView, R.id.tvUpdateLog);
        if (resData.getIs_update() == 1)
            txtUpdateLog.setText("资源将会更新到版本 " + resData.getVersion_id());
        DialogCenter.showDialog(CheckUpdateActivity.this, scrollView, new DialogClickListener() {
            @Override
            public void p() {
                showDownloadingDialog("下载资源包...");
                downloadRes(resData);
            }

            @Override
            public void n() {
                DialogCenter.hideDialog();
            }
        }, new String[]{"资源更新", "更新", "取消"});
    }

    public void downloadRes(ResUpdate.DataBean resData) {
        GlideCacheUtil.getInstance().clearImageAllCache(this);
        String resUrl = resData.getDown_url();
        Log.i("resUrl","downLoad: ----------------------"+resUrl);
        String fileName = resUrl.substring(resUrl.lastIndexOf("/") + 1);
        String zipPath = HdAppConfig.getDefaultFileDir();
        File file = new File(zipPath + fileName);
        resDownloader = RxDownload.getInstance().maxRetryCount(18).download(resUrl, fileName, zipPath)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(new Action1<DownloadStatus>() {
                    @Override
                    public void call(DownloadStatus downloadStatus) {
                        if (downloadStatus.getDownloadSize() == downloadStatus.getTotalSize()) {
                            unzipFile(file);
                        } else {
                            if (NetUtil.isConnected(CheckUpdateActivity.this)) {
                                Message message = mHandler.obtainMessage();
                                message.obj = String.format("正在下载(%s/%s)",
                                        DataManager.getFormatSize(downloadStatus.getDownloadSize()),
                                        DataManager.getFormatSize(downloadStatus.getTotalSize()));
                                message.what = update_res_loading;
                                mHandler.sendMessageAtTime(message, 1000);
                            } else {
                                CheckUpdateActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(CheckUpdateActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                                        mHandler.removeCallbacksAndMessages(null);
                                    }
                                });
                            }
                        }

                    }
                }, throwable -> {
                    resDownloader.unsubscribe();
                    DialogCenter.hideDialog();
                }, () -> {
                    Logger.e("下载成功");
                    DialogCenter.hideDialog();
                    String resIdFile = HdAppConfig.getDefaultFileDir() + "resId.txt";
                    FileUtils.writeStringToFile(resIdFile, resData.getVersion_id() + "", false);
                    mHandler.sendEmptyMessage(update_res_success);
                });
    }

    /**
     * Dialog-显示当前版本信息
     */
    public void showVersionInfoDialog() {

        DialogCenter.showDialog(CheckUpdateActivity.this, new DialogClickListener() {
            @Override
            public void p() {
                DialogCenter.hideDialog();
            }
        }, new String[]{"版本更新", "当前已是最新版：" + AppUtil.getVersionName(CheckUpdateActivity.this), "取消"});
    }

    /**
     * 下载并安装新版Apk
     *
     * @param checkResponse
     */
    private void loadAndInstall(CheckResponse checkResponse) {
        String apkUrl = checkResponse.getVersionInfo().getVersionUrl();
        String baseUrl = apkUrl.substring(0, apkUrl.lastIndexOf("/") + 1);
        String fileName = apkUrl.substring(apkUrl.lastIndexOf("/") + 1);
        String fileStoreDir = HdConstants.getDefaultFileDir();
        FileApi.getInstance(baseUrl).loadFileByName(fileName,
                new FileCallback(fileStoreDir, fileName) {
                    @Override
                    public void progress(long progress,
                                         long total) {
                        txtProgress.setText(String.format("正在下载(%s/%s)",
                                DataManager.getFormatSize(progress),
                                DataManager.getFormatSize(total)));
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call,
                                          Throwable t) {
                        DialogCenter.hideDialog();
                    }

                    @Override
                    public void onSuccess(File file) {
                        DialogCenter.hideDialog();
                        AppUtil.installApk(CheckUpdateActivity.this, file.getAbsolutePath());
                    }
                });
    }

    public void checkRes() {
        if (NetUtil.isConnected(CheckUpdateActivity.this)) {
            String fileName = HdAppConfig.getDefaultFileDir() + "resId.txt";
            String resId;
            if (FileUtils.readStringFromFile(fileName, "utf-8") != null) {
                resId = FileUtils.readStringFromFile(fileName, "utf-8").toString();
            } else {
                resId = "0";
            }
            DialogCenter.showProgressDialog(CheckUpdateActivity.this, "检查资源版本", false);
            /*
            * 192.168.10.20
            * public static final String DEFAULT_IP_PORT = "192.168.10.20";
            * */

            RequestApi.getInstance("http://" + HdAppConfig.getDefaultIpPort() + "/csbwg/")
                    .getRes(new RequestSubscriber<ResUpdate>() {
                        @Override
                        public void succeed(ResUpdate update) {
                            DialogCenter.hideProgressDialog();
                            switch (update.getData().getIs_update()) {
                                case 1:
                                    showHasNewResVersionDialog(update.getData());
                                    break;
                                case 0:
                                    showResInfoDialog(resId);
                                    break;
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            DialogCenter.hideProgressDialog();
                            Toast.makeText(CheckUpdateActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                        }
                    }, Integer.parseInt(resId));
        }
    }


    /**
     * @param file
     */
    private void unzipFile(File file) {
        try {

            ZipUtil.unzipFolder(file.getAbsolutePath(), HdAppConfig.getDefaultFileDir(), new ZipUtil.IUnzipCallback() {
                @Override
                public void completed() {
//                    Logger.e("解压完成");
//                    Toast.makeText(CheckUpdateActivity.this, "解压完成", Toast.LENGTH_SHORT).show();

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
        if (file.exists()) {
            file.delete();

        }
    }

    private void myWifiReceiver() {
        wifiReceiver = new WifiReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        registerReceiver(wifiReceiver, filter);
    }

    public class WifiReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_DISABLED);
            Log.e("", "wifiState:" + wifiState);
            switch (wifiState) {
                case WifiManager.WIFI_STATE_DISABLED:
                    Toast.makeText(context, "WIFI已断开", Toast.LENGTH_SHORT).show();
                    break;
                case WifiManager.WIFI_STATE_ENABLED:
                    Toast.makeText(context, "WIFI已连接", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
