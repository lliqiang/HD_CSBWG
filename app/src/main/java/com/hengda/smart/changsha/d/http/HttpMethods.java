package com.hengda.smart.changsha.d.http;




import android.text.TextUtils;

import com.hengda.smart.changsha.d.app.HdAppConfig;
import com.hengda.smart.changsha.d.app.HdConstants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by baishiwei on 2016/10/22.
 * 所有网络请求的处理类
 */
public class HttpMethods {
    private static final int DEFAULT_TIMEOUT = 1;//超时时间(单位:分钟)
    private Retrofit retrofit;
    private HttpRequests httpRequests;
    private volatile static HttpMethods instance;

    private HttpMethods() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.MINUTES);
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        builder.addInterceptor(loggingInterceptor);
        builder.retryOnConnectionFailure(true);
//        if (BuildConfig.DEBUG) {
//            // Log信息拦截器
//            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
//            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//            //设置 Debug Log 模式
//            builder.addInterceptor(loggingInterceptor);
        http://222.240.99.67:65523/
//        }
        retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl("http://"+ HdAppConfig.getDefaultIpPort()+"/csbwg/")
                .build();
        httpRequests = retrofit.create(HttpRequests.class);
    }





    public static HttpMethods getInstance() {
        if (instance == null) {
            synchronized (HttpMethods.class) {
                if (instance == null) {
                    instance = new HttpMethods();
                }
            }
        }
        return instance;
    }

    /**
     * 获取DB的数据
     *
     * @param subscriber
     */
    public void getDBData(Subscriber subscriber) {
        Observable observable = httpRequests.getDBData().map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }


    public void alarm(Subscriber subscriber, String deviceNo, String type) {
        Observable observable = httpRequests.alarm(deviceNo, type);
        toSubscribe(observable, subscriber);
    }

    public void preAlarm(Subscriber subscriber, String deviceNo, String type, String areaNum) {
        Observable observable = httpRequests.preAlarm(deviceNo, type, areaNum);
        toSubscribe(observable, subscriber);
    }

//    public void getNearbyAttractions(Subscriber subscriber) {
//        Observable observable = httpRequests.getNearbyAttractions().map(new HttpResultFunc<>());
//        toSubscribe(observable, subscriber);
//    }

    private <T> void toSubscribe(Observable<T> o, Subscriber<T> s) {
        o.subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }
//Observable<HttpResponse<DataModel>>
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

    /**
     * 位置上传
     *
     * @param subscriber
     * @param deviceno
     * @param electricity   电量
     * @param app_kind   设备类型1安卓，2IOS,3导览机
     * @param auto_num   点位编号对应app数据表中的AutoNum字段
     */
    public void uploadPosition(Subscriber subscriber, String deviceno, int app_kind, String auto_num, int electricity) {
        Observable observable = httpRequests.uploadPosition(deviceno, app_kind, auto_num, electricity);
        toSubscribe(observable, subscriber);
    }
}
