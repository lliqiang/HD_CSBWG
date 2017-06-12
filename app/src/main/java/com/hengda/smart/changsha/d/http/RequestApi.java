package com.hengda.smart.changsha.d.http;

import android.text.TextUtils;


import com.hengda.smart.changsha.d.app.HdAppConfig;
import com.hengda.smart.changsha.d.app.HdApplication;
import com.hengda.smart.changsha.d.app.HdConstants;
import com.hengda.smart.changsha.d.common.util.AppUtil;
import com.hengda.smart.changsha.d.model.CheckResponse;


import java.util.Hashtable;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 作者：Tailyou （祝文飞）
 * 时间：2016/6/11 16:17
 * 邮箱：tailyou@163.com
 * 描述：
 */
public class RequestApi {

    private static final int DEFAULT_TIMEOUT = 5;
    private Retrofit retrofit;
    private IRequestService iRequestService;
    private volatile static RequestApi instance;
    private static Hashtable<String, RequestApi> mRequestApiTable;

    static {
        mRequestApiTable = new Hashtable<>();
    }

    /**
     * 私有构造函数
     *
     * @param baseHttpUrl
     */
    private RequestApi(String baseHttpUrl) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(baseHttpUrl)
                .build();
        iRequestService = retrofit.create(IRequestService.class);
    }

    /**
     * 获取单例
     *
     * @return
     */
    public static RequestApi getInstance() {
        String baseHttpUrl = getBaseHttpUrl();
        instance = mRequestApiTable.get(baseHttpUrl);
        if (instance == null) {
            synchronized (RequestApi.class) {
                if (instance == null) {
                    instance = new RequestApi(baseHttpUrl);
                    mRequestApiTable.clear();
                    mRequestApiTable.put(baseHttpUrl, instance);
                }
            }
        }
        return instance;
    }

    /**
     * 获取单例
     *
     * @return
     */
    public static RequestApi getInstance(String baseHttpUrl) {
        instance = mRequestApiTable.get(baseHttpUrl);
        if (instance == null) {
            synchronized (RequestApi.class) {
                if (instance == null) {
                    instance = new RequestApi(baseHttpUrl);
                    mRequestApiTable.clear();
                    mRequestApiTable.put(baseHttpUrl, instance);
                }
            }
        }
        return instance;
    }

    /**
     * 获取网络请求基地址
     *
     * @return
     */
    public static String getBaseHttpUrl() {
        return "http://" + HdAppConfig.getDefaultIpPort() + "/csbwg/";
    }
//    http://192.168.10.28/hnbwy/

    /**
     * 检查App版本更新
     *
     * @param subscriber
     */
    public void checkVersion(Subscriber<CheckResponse> subscriber) {
        Observable<CheckResponse> observable = iRequestService.checkVersion(HdConstants.APP_KEY,
                HdConstants.APP_SECRET, 3, AppUtil.getVersionCode(HdApplication.mContext),
                HdAppConfig.getDeviceNo());
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
    /**
     * 获取DB的数据
     *
     * @param subscriber
     */
    public void getDBData(Subscriber subscriber) {
        Observable observable = iRequestService.getDBData().map(new RequestApi.HttpResultFunc());
        toSubscribe(observable, subscriber);
    }
    private <T> void toSubscribe(Observable<T> o, Subscriber<T> s) {
        o.subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }

    private class HttpResultFunc<T> implements Func1<HttpResponse<T>, T> {
        @Override
        public T call(HttpResponse<T> httpResponse) {
            if (httpResponse.getStatus() != 1) {
                throw new ApiException(httpResponse.getStatus(), httpResponse.getMsg());
            } else {
                if (httpResponse.getData() == null) {
                    return (T) httpResponse.getMsg();
                } else if (httpResponse.getData().toString().equals("[]")) {
                    return null;
                } else {
                    return httpResponse.getData();
                }
            }
        }
    }
    public void getRes(Subscriber<ResUpdate> subscriber, int version) {
        Observable<ResUpdate> observable = iRequestService.getRes(version);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void bindDevice(Subscriber<HttpResponse> subscriber, String deviceno, String client_id) {
        Observable<HttpResponse> observable = iRequestService.bindDevice(deviceno, client_id);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /*
    * 浏览次数上传
    * */
    public void lookCount(Subscriber<HttpResponse> subscriber, String exhibit_id) {
        Observable<HttpResponse> observable = iRequestService.lookCount(exhibit_id);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 请求机器号
     *
     * @param subscriber
     * @param app_kind
     */
    public void reqDeviceNo(Subscriber<String> subscriber, String app_kind) {
        Observable<Response<String>> observable = iRequestService.reqDeviceNo(app_kind);
        doSubscribe(subscriber, observable);
    }



    /**
     * 订阅（抽取公共部分）
     *
     * @param subscriber
     * @param observable
     */


    private <T> void doSubscribe(Subscriber<T> subscriber, Observable<Response<T>> observable) {
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Response<T>, T>() {
                    @Override
                    public T call(Response<T> response) {
                        if (TextUtils.equals(HdConstants.HTTP_STATUS_SUCCEED, response.getStatus())) {
                            return response.getData();
                        } else {
                            throw new RequestException(response.getMsg());
                        }
                    }
                })
                .subscribe(subscriber);
    }


}
