package com.example.mettre.myaprojectandroid.utils;

import android.text.TextUtils;

import com.example.mettre.myaprojectandroid.app.MyApplication;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Mettre on 2018/5/23.
 */

public class HeaderInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        String token = SharedPrefsUtil.getValue(Utils.getContext(), "token", "");
        if ( TextUtils.isEmpty(token)) {
            Request mRequest = chain.request().newBuilder()
                    .build();
            return chain.proceed(mRequest);
        } else {
            Request mRequest = chain.request().newBuilder()
                    .header("authorities", "Bearer " + token)
                    .build();
            return chain.proceed(mRequest);
        }

    }
}
