package com.example.mettre.myaprojectandroid.app;

import android.app.Application;

import com.example.mettre.myaprojectandroid.utils.SharedPrefsUtil;
import com.example.mettre.myaprojectandroid.utils.Utils;

/**
 * Created by Mettre on 2018/8/14.
 */

public class MyApplication extends Application {

    private String token;

    private static MyApplication instances;

    public static MyApplication getInstances() {
        return instances;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instances = this;
        Utils.init(instances);
        String token = SharedPrefsUtil.getValue(getApplicationContext(), "token", "");
        MyApplication.getInstances().setToken(token);
    }
}
