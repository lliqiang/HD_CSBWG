package com.hengda.smart.changsha.d.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hengda.smart.changsha.d.R;
import com.hengda.smart.changsha.d.app.HdAppConfig;
import com.hengda.smart.changsha.d.app.HdApplication;
import com.hengda.smart.changsha.d.common.util.FileUtils;
import com.hengda.smart.changsha.d.common.util.RxBus;
import com.hengda.smart.changsha.d.common.util.SDCardUtil;
import com.hengda.smart.changsha.d.http.HttpResponse;
import com.hengda.smart.changsha.d.http.RequestApi;
import com.hengda.smart.changsha.d.model.TcpResp;
import com.hengda.smart.changsha.d.widget.dialog.NotifyDialog;
import com.orhanobut.logger.Logger;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;


/**
 * 作者：Tailyou
 * 时间：2016/1/18 08:50
 * 邮箱：tailyou@163.com
 * 描述：
 */
public class BaseActivity extends AppCompatActivity {
    public NotifyDialog notifyDialog;
    public RxBus rxBus;
    public Subscriber<TcpResp> subscribe;
    private TcpResp tcpResp1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        rxBus = RxBus.getDefault();
        notifyDialog = new NotifyDialog(this);
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
                        tcpResp1 = tcpResp;
                        notifyDialog.title(tcpResp.getSend_type())
                                .message(tcpResp.getSend_content())
                                .pBtnText(getString(R.string.submit))
                                .pBtnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        notifyDialog.dismiss();
                                    }
                                }).show();
                    }
                });


            }
        };
    }

    protected void openActivity(Context context, Class<?> pClass) {
        openActivity(context, pClass, null, null);
    }

    protected void openActivity(Context context, Class<?> pClass, Bundle pBundle) {
        openActivity(context, pClass, pBundle, null);
    }

    protected void openActivity(Context context, Class<?> pClass, String action) {
        openActivity(context, pClass, null, action);
    }

    protected void openActivity(Context context, Class<?> pClass, Bundle pBundle, String action) {
        Intent intent = new Intent(context, pClass);
        if (action != null) {
            intent.setAction(action);
        }
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
//      if (tcpResp1!=null){
        rxBus.toObservable(TcpResp.class).subscribe(subscribe);
//      }
    }

    @Override
    protected void onStop() {
        super.onStop();
        subscribe.unsubscribe();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
