package com.hengda.smart.changsha.d.subscribers;

import android.content.Context;
import android.util.Log;

import com.hengda.smart.changsha.d.progress.ProgressCancelListener;
import com.hengda.smart.changsha.d.progress.ProgressDialogHandler;
import com.orhanobut.logger.Logger;


import java.net.ConnectException;
import java.net.SocketTimeoutException;

import rx.Subscriber;

/**
 * 用于在Http请求开始时，自动显示一个ProgressDialog
 * 在Http请求结束是，关闭ProgressDialog
 * 调用者自己对请求数据进行处理
 */
public class ProgressSubscriber<T> extends Subscriber<T> implements ProgressCancelListener {

    private SubscriberOnNextListener mSubscriberOnNextListener;
    private ProgressDialogHandler mProgressDialogHandler;
    private boolean show;

    public ProgressSubscriber(Context context, SubscriberOnNextListener mSubscriberOnNextListener, boolean show) {
        this.mSubscriberOnNextListener = mSubscriberOnNextListener;
        this.show = show;
        if (show)
            mProgressDialogHandler = new ProgressDialogHandler(context, this, true);
    }

    private void showProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG).sendToTarget();
        }
    }

    private void dismissProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG).sendToTarget();
            mProgressDialogHandler = null;
        }
    }

    /**
     * 订阅开始时调用
     * 显示ProgressDialog
     */
    @Override
    public void onStart() {
        if (show)
            showProgressDialog();
    }

    /**
     * 完成，隐藏ProgressDialog
     */
    @Override
    public void onCompleted() {
        if (show)
            dismissProgressDialog();
    }

    /**
     * 对错误进行统一处理
     * 隐藏ProgressDialog
     *
     * @param e
     */
    @Override
    public void onError(Throwable e) {

        if (e instanceof SocketTimeoutException) {
//            Toast.makeText(HdApplication.mContext, R.string.network_error, Toast.LENGTH_SHORT).show();
        } else if (e instanceof ConnectException) {
            Log.i("ConnectException",e.getMessage());
//            Toast.makeText(HdApplication.mContext,  R.string.network_error, Toast.LENGTH_SHORT).show();
        } else {
        }
        if (show)
            dismissProgressDialog();
    }

    /**
     * 将onNext方法中的返回结果交给Activity或Fragment自己处理
     *
     * @param t 创建Subscriber时的泛型类型
     */
    @Override
    public void onNext(T t) {
        if (mSubscriberOnNextListener != null) {
            mSubscriberOnNextListener.onSuccess(t);
        }
    }

    /**
     * 取消ProgressDialog的时候，取消对observable的订阅，同时也取消了http请求
     */
    @Override
    public void onCancelProgress() {
        if (!this.isUnsubscribed()) {
            this.unsubscribe();
        }
    }
}