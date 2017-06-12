package com.hengda.smart.changsha.d.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hengda.smart.changsha.d.R;
import com.hengda.smart.changsha.d.admin.AlarmActivity;
import com.hengda.smart.changsha.d.admin.CheckCallback;
import com.hengda.smart.changsha.d.admin.DRfidReceiver;
import com.hengda.smart.changsha.d.admin.LoginActivity;
import com.hengda.smart.changsha.d.admin.SystemSetting;
import com.hengda.smart.changsha.d.app.HdAppConfig;
import com.hengda.smart.changsha.d.app.HdApplication;
import com.hengda.smart.changsha.d.app.HdConstants;
import com.hengda.smart.changsha.d.common.util.FileUtils;
import com.hengda.smart.changsha.d.common.util.NetUtil;
import com.hengda.smart.changsha.d.common.util.RxBus;
import com.hengda.smart.changsha.d.common.util.SDCardUtil;
import com.hengda.smart.changsha.d.http.HttpMethods;
import com.hengda.smart.changsha.d.http.HttpResponse;
import com.hengda.smart.changsha.d.http.RequestApi;
import com.hengda.smart.changsha.d.model.AutoNum;
import com.hengda.smart.changsha.d.model.CheckResponse;
import com.hengda.smart.changsha.d.model.DeviceNoEvent;
import com.hengda.smart.changsha.d.model.Exhibit;
import com.hengda.smart.changsha.d.model.TcpResp;
import com.hengda.smart.changsha.d.rfid.NetStateMonitor;
import com.hengda.smart.changsha.d.subscribers.ProgressSubscriber;
import com.hengda.smart.changsha.d.subscribers.SubscriberOnNextListener;
import com.hengda.smart.changsha.d.tileview.BitmapProviderFile;
import com.hengda.smart.changsha.d.widget.dialog.NotifyDialog;
import com.orhanobut.logger.Logger;


import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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

public class LaucherActivity extends DRfidReceiver implements View.OnClickListener {

    @Bind(R.id.off)
    ImageView off;
    @Bind(R.id.admin)
    ImageView admin;
    @Bind(R.id.start_guide)
    TextView startGuide;
    String fromAction;
    @Bind(R.id.start_eguide)
    TextView startEguide;
    @Bind(R.id.scan)
    ImageView scan;
    private int intLevel=1;
    private int intScale;
    private Intent intent;
    NetStateMonitor netStateMonitor;
    private boolean flag;
    private int lastNum = -1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                //注册电量变化广播
                if (SDCardUtil.isSDCardEnable()) {
                    registerReceiver(broadcastReceiver, new IntentFilter(
                            Intent.ACTION_BATTERY_CHANGED));
//                    initStcMode();
                    registerNetStateMonitor();
                    flag = true;
                } else {
                    handler.sendEmptyMessageDelayed(1, 500);
                }

            }
        }
    };
    private Subscriber<TcpResp> subscribe;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (intent.ACTION_BATTERY_CHANGED.equals(action)) {
                intLevel = intent.getIntExtra("level", 0);
                intScale = intent.getIntExtra("scale", 100);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laucher);
        ButterKnife.bind(this);
        handler.sendEmptyMessage(1);
        HdAppConfig.setAutoPlay(true);
        registerRFIDReceiver();
        startGuide.setOnClickListener(this);
        off.setOnClickListener(this);
        admin.setOnClickListener(this);
        scan.setOnClickListener(this);
        startGuide.setTypeface(HdApplication.typeface);
        startEguide.setTypeface(HdApplication.typeface);
        HdAppConfig.setPowerMode(0);
        if (HdAppConfig.getPowerMode() == 0) {
            off.setVisibility(View.INVISIBLE);
        } else {
            off.setVisibility(View.VISIBLE);
        }

    }

    private SubscriberOnNextListener mNextListener = new SubscriberOnNextListener<HttpResponse>() {
        @Override
        public void onSuccess(HttpResponse response) {
            Logger.e(response.getMsg());
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        initPowerMode();
        rxBus.toObservable(DeviceNoEvent.class)
                .subscribe(new Subscriber<DeviceNoEvent>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(LaucherActivity.this, "e" + e, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(DeviceNoEvent deviceNoEvent) {
                        initPushListener();
                        Toast.makeText(LaucherActivity.this, deviceNoEvent.getIP(), Toast.LENGTH_SHORT).show();
                    }
                });
        if (subscribe == null) {
            subscribe = new Subscriber<TcpResp>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    Log.i("base", "base:--------------" + e);
                }

                @Override
                public void onNext(TcpResp tcpResp) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            notifyDialog.title(tcpResp.getSend_type())
                                    .message(tcpResp.getSend_content())
                                    .pBtnText(getString(R.string.submit))
                                    .pBtnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            notifyDialog.dismiss();
                                        }
                                    });
                            if (!notifyDialog.isShowing()) {
                                notifyDialog.show();
                            }

                        }
                    });
                }
            };

        }
        if (flag) {
            rxBus.toObservable(TcpResp.class).subscribe(subscribe);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
//
    }

    /**
     * 收号之后的操作
     *
     * @param autoNo
     */
    private void onReceiveAutoNo(int autoNo) {
        switch (autoNo) {
            case 2001:
                if (AlarmActivity.mInstance == null) {
                    startActivity(new Intent(LaucherActivity.this, AlarmActivity.class));
                }
                break;
            case 2002:
                if (AlarmActivity.mInstance == null) {
                    startActivity(new Intent(LaucherActivity.this, AlarmActivity.class));
                }
                break;
//                HttpMethods.getInstance().alarm(new ProgressSubscriber(LaucherActivity.this, mNextListener, false),
//                        HdAppConfig.getDeviceNo(), HdConstants.START_ALARM);
            case 2003:
                if (AlarmActivity.mInstance != null) {
                    AlarmActivity.mInstance.finish();
                    AlarmActivity.mInstance = null;
                }
                HttpMethods.getInstance().alarm(new ProgressSubscriber(LaucherActivity.this, mNextListener, false),
                        HdAppConfig.getDeviceNo(), HdConstants.STOP_ALARM);
                break;
            case 4041:
            case 4042:
            case 4043:
            case 4044:
                HttpMethods.getInstance().preAlarm(new ProgressSubscriber(this, new SubscriberOnNextListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Log.e("TAG", o.toString());
                    }
                }, false), HdAppConfig.getDeviceNo(), HdConstants.START_ALARM, String.valueOf(autoNo));
                break;
        }
    }

    /**
     * 收号-rfid
     *
     * @param buffer
     * @param size
     */
    @Override
    protected void onDataReceived(byte[] buffer, int size) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (HdAppConfig.getReceiveNoMode() != 0) {
                    for (int m = 0; m < 8; m++) {
                        if (buffer[m] == (byte) 0xAA && buffer[m + 1] == (byte) 0x55) {
                            if ((buffer[m + 2] == (byte) 0x05)
                                    && buffer[m + 6] == (buffer[m] ^ buffer[m + 1]
                                    ^ buffer[m + 2] ^ buffer[m + 3]
                                    ^ buffer[m + 4] ^ buffer[m + 5])) {
                                int rfidNum = (buffer[m + 4] & 0x0FF) * 256
                                        + (buffer[m + 5] & 0x0FF);
                                Log.i("rfid","rfid:----------------------"+rfidNum);
                                onReceiveAutoNo(rfidNum);
                                SubscriberOnNextListener uploadListener = new SubscriberOnNextListener<HttpResponse>() {
                                    @Override
                                    public void onSuccess(HttpResponse response) {
                                        Log.i("respose","respose:-----------------"+response.getMsg()+response.getData()+response.getStatus());
                                    }

                                };
                                if (NetUtil.isConnected(LaucherActivity.this)){

                                    HttpMethods.getInstance().uploadPosition(new ProgressSubscriber(LaucherActivity.this, uploadListener, false),HdAppConfig.getDeviceNo(), 3,String.valueOf(rfidNum), intLevel);
                                }
//
//                                }
                            }
                        }
                    }
                }
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.admin:
                intent = new Intent(LaucherActivity.this, LoginActivity.class);
                fromAction = "ADMIN";
                intent.putExtra("fromAction", fromAction);
                startActivity(intent);
                break;
            case R.id.off:
                intent = new Intent(LaucherActivity.this, LoginActivity.class);
                fromAction = "POWER";
                intent.putExtra("fromAction", fromAction);
                startActivity(intent);
                break;
            case R.id.start_guide:
                if (HdAppConfig.isDbExist()) {

                    openActivity(LaucherActivity.this, LanguageActivity.class);
                } else {
                    Toast.makeText(this, "数据库资源不存在，请到管理员界面下载", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.start_eguide:
                if (HdAppConfig.isDbExist()) {

                    openActivity(LaucherActivity.this, LanguageActivity.class);
                } else {
                    Toast.makeText(this, "数据库资源不存在，请到管理员界面下载", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.scan:
                openActivity(LaucherActivity.this, BarCodeActivity.class);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscribe.unsubscribe();
        if (flag) {
            unregisterRFIDReceiver();
            unregisterReceiver(netStateMonitor);
        }
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }

    /**
     * 注册网络状态监听器
     */
    private void registerNetStateMonitor() {
        netStateMonitor = new NetStateMonitor() {
            @Override
            public void onConnected() {
                checkNewVersion(new CheckCallback() {
                    @Override
                    public void hasNewVersion(CheckResponse checkResponse) {
                        showHasNewVersionDialog(checkResponse);

                    }

                    @Override
                    public void isAlreadyLatestVersion() {
                        super.isAlreadyLatestVersion();
                    }
                });
                initPushListener();
            }

            @Override
            public void onDisconnected() {
                Toast.makeText(LaucherActivity.this, "网络不可用", Toast.LENGTH_SHORT).show();
            }
        };
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netStateMonitor, mFilter);
    }

    /**
     * 初始化关机权限
     */
    private void initPowerMode() {
        if (HdAppConfig.getPowerMode() == 1) {
            off.setVisibility(View.GONE);
            sendBroadcast(new Intent("android.intent.action.ALLOW_POWERDOWN"));
        } else {
            off.setVisibility(View.VISIBLE);
            sendBroadcast(new Intent("android.intent.action.LIMIT_POWERDOWN"));
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    /************************************************************/
    /************************以下是推送**************************/
    /************************************************************/
    public void initPushListener() {
        new Thread(() -> {
            try {
                String ipPort = HdAppConfig.getDefaultIpPort();
                Log.i("DEf", "def----------------" + HdAppConfig.getDefaultIpPort());
                String deviceNo = HdAppConfig.getDeviceNo();
                Socket client = new Socket(ipPort, 8282);
                receiveMsg(client, ipPort, deviceNo);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    /**
     * 接收推送
     *
     * @param client
     * @param ipPort
     * @param deviceNo
     * @throws IOException
     */
    private void receiveMsg(Socket client, String ipPort, String deviceNo) {
       /* if (SDCardUtil.isSDCardEnable()) {*/
        while (TextUtils.equals(ipPort, HdAppConfig.getDefaultIpPort()) && TextUtils.equals(deviceNo, HdAppConfig.getDeviceNo())) {
            try {
                FileUtils.writeStringToFile(SDCardUtil.getSDCardPath() + "PushLog.txt", ipPort + "###" + deviceNo + "\n", false);
                DataInputStream input = new DataInputStream(client.getInputStream());
                byte[] b = new byte[10000];
                int length = input.read(b);
                String msg = new String(b, 0, length, "gb2312");
                //noinspection deprecation
                msg = Html.fromHtml(msg).toString();
                for (TcpResp tcpResp : formatMsg(msg)) {
                    dealTcpResp(client, tcpResp);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 处理推送消息
     *
     * @param client
     * @param tcpResp
     */
    private void dealTcpResp(Socket client, TcpResp tcpResp) {
        switch (tcpResp.getType()) {
            case "bind":
                bindDevice(tcpResp);
                break;
            case "heart":
                sendMsg(client, "heart_beat");
                break;
            case "send_msg":
                FileUtils.writeStringToFile(SDCardUtil.getSDCardPath() + "PushLog.txt", new Gson().toJson(tcpResp), true);
                RxBus.getDefault().post(tcpResp);
                Log.i("TGD", "-----------ddddd-----------" + tcpResp.toString());
                break;
        }
    }
    /*}*/

    /**
     * 绑定设备
     *
     * @param tcpResp
     */
    private void bindDevice(TcpResp tcpResp) {
        FileUtils.writeStringToFile(SDCardUtil.getSDCardPath() + "PushLog.txt", new Gson().toJson(tcpResp), true);
        HdAppConfig.setClientId(tcpResp.getClient_id());
        RequestApi.getInstance().bindDevice(new Subscriber<HttpResponse>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Logger.e("bind device error," + e.getMessage());
            }

            @Override
            public void onNext(HttpResponse httpResponse) {
                Logger.e("bind device success");
            }
        }, HdAppConfig.getDeviceNo(), tcpResp.getClient_id());
    }

    /**
     * 消息格式化
     *
     * @author 祝文飞（Tailyou）
     * @time 2017/3/8 14:57
     */
    private List<TcpResp> formatMsg(String msg) {
        msg = msg.replace("}", "},");
        msg = msg.substring(0, msg.length() - 1);
        msg = "[" + msg + "]";
        List<TcpResp> tcpRespList = new Gson().fromJson(msg,
                new TypeToken<List<TcpResp>>() {
                }.getType());
        Logger.json(new Gson().toJson(tcpRespList));
        return tcpRespList;
    }

    /**
     * socket 发送数据
     *
     * @author 祝文飞（Tailyou）
     * @time 2017/2/4 14:30
     */
    private void sendMsg(Socket client, String heart_beat) {
        try {
            BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
            PrintWriter out = new PrintWriter(wr, true);
            out.println(heart_beat);
            out.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
