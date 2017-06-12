package com.hengda.smart.changsha.d.subscribers;

/**
 * Created by baishiwei on 16/4/28.
 */
public interface SubscriberOnNextListener<T> {
    void onSuccess(T t);
}
