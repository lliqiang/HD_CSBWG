package com.hengda.smart.changsha.d.ui;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hengda.smart.changsha.d.Main;
import com.hengda.smart.changsha.d.R;
import com.hengda.smart.changsha.d.admin.CheckCallback;
import com.hengda.smart.changsha.d.app.HdAppConfig;
import com.hengda.smart.changsha.d.app.HdApplication;
import com.hengda.smart.changsha.d.common.util.CommonUtil;
import com.hengda.smart.changsha.d.common.util.FileUtils;
import com.hengda.smart.changsha.d.common.util.RxBus;
import com.hengda.smart.changsha.d.common.util.SDCardUtil;
import com.hengda.smart.changsha.d.http.HttpMethods;
import com.hengda.smart.changsha.d.http.HttpResponse;
import com.hengda.smart.changsha.d.http.RequestApi;
import com.hengda.smart.changsha.d.model.CheckResponse;
import com.hengda.smart.changsha.d.model.TcpResp;
import com.hengda.smart.changsha.d.rfid.NetStateMonitor;
import com.hengda.smart.changsha.d.subscribers.ProgressSubscriber;
import com.hengda.smart.changsha.d.subscribers.SubscriberOnNextListener;
import com.hengda.smart.changsha.d.widget.dialog.ListDialog;

import com.hengda.smart.changsha.d.widget.dialog.NotifyDialog;
import com.hss01248.notifyutil.NotifyUtil;
import com.orhanobut.logger.Logger;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class LanguageActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.chinese)
    TextView chinese;
    @Bind(R.id.english)
    TextView english;
    @Bind(R.id.japanese)
    TextView japanese;
    @Bind(R.id.korean)
    TextView korean;
    @Bind(R.id.select_cn)
    TextView selectCn;
    @Bind(R.id.select_en)
    TextView selectEn;
    private NetStateMonitor netStateMonitor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        ButterKnife.bind(this);
        initListner();
        registerNetStateMonitor();

        HttpMethods.getInstance().uploadPosition(new Subscriber() {
            @Override
            public void onCompleted() {
                Log.i("Battery", "onCompleted---------------");
            }

            @Override
            public void onError(Throwable e) {
                Log.i("Battery", "onError---------------" + e.getMessage());
            }

            @Override
            public void onNext(Object o) {

            }
        }, HdAppConfig.getDeviceNo(), 3, "101", 83);

    }

    private void initListner() {
        selectCn.setTypeface(HdApplication.typeface);
        selectEn.setTypeface(HdApplication.typeface);
        chinese.setTypeface(HdApplication.typeface);
        chinese.setOnClickListener(this);
        english.setOnClickListener(this);
        english.setTypeface(HdApplication.typeface);
        japanese.setOnClickListener(this);
        japanese.setTypeface(HdApplication.typeface);
        korean.setOnClickListener(this);
        korean.setTypeface(HdApplication.typeface);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chinese:
                HdAppConfig.setLanguage("CHINESE");
                if (HdAppConfig.isResExist()) {
                    CommonUtil.configLanguage(LanguageActivity.this, "CHINESE");
                    openActivity(LanguageActivity.this, Main.class);
                } else {
                    Toast.makeText(this, "请确认资源是否存在", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.english:
                HdAppConfig.setLanguage("ENGLISH");
                if (HdAppConfig.isResExist()) {
                    CommonUtil.configLanguage(LanguageActivity.this, "ENGLISH");

                    openActivity(LanguageActivity.this, Main.class);
                } else {
                    Toast.makeText(this, "请确认资源是否存在", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.japanese:
                HdAppConfig.setLanguage("JAPANESE");
                if (HdAppConfig.isResExist()) {
                    CommonUtil.configLanguage(LanguageActivity.this, "JAPANESE");
                    openActivity(LanguageActivity.this, Main.class);
                } else {
                    Toast.makeText(this, "请确认资源是否存在", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.korean:
                HdAppConfig.setLanguage("KOREAN");
                if (HdAppConfig.isResExist()) {
                    CommonUtil.configLanguage(LanguageActivity.this, "KOREAN");
                    openActivity(LanguageActivity.this, Main.class);
                } else {
                    Toast.makeText(this, "请确认资源是否存在", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    /**
     * 注册网络状态监听器
     */
    private void registerNetStateMonitor() {
        netStateMonitor = new NetStateMonitor() {
            @Override
            public void onConnected() {


                if (SDCardUtil.isSDCardEnable()) {
//                    initPushListener();
                }
            }

            @Override
            public void onDisconnected() {
                Toast.makeText(LanguageActivity.this, "网络不可用", Toast.LENGTH_SHORT).show();
            }
        };
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netStateMonitor, mFilter);
    }



    /**
     * socket 发送数据
     *
     * @author 祝文飞（Tailyou）
     * @time 2017/2/4 14:30
     */
    private void sendMsg(Socket client, String heart_beat) throws IOException {
        BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
        PrintWriter out = new PrintWriter(wr, true);
        out.println(heart_beat);
        out.flush();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(netStateMonitor);
    }
}
