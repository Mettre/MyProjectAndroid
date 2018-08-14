package com.example.mettre.myaprojectandroid.subscribers;

import org.json.JSONException;

/**
 * Created by liukun on 16/3/10.
 */
public interface SubscriberOnNextListener<T> {
    void onNext(T t) throws JSONException;
    void onCompleted();
    void onError();
    void onSocketTimeout();
    void onConnectException();
}
