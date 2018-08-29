package com.example.mettre.myaprojectandroid.http;

import com.example.mettre.myaprojectandroid.app.MyApplication;
import com.example.mettre.myaprojectandroid.bean.AddressBean;
import com.example.mettre.myaprojectandroid.bean.UserBean;
import com.example.mettre.myaprojectandroid.utils.AndroidScheduler;
import com.example.mettre.myaprojectandroid.utils.HeaderInterceptor;
import com.example.mettre.myaprojectandroid.utils.LoggerInterceptor;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by app on 2017/9/22.
 */

public class HttpMethods3 {

    public static final String BASE_URL = "http://192.168.0.231:8888/";//公司

//    public static final String BASE_URL = "http://192.168.1.107:8888/";//家

    private static final int DEFAULT_TIMEOUT = 25;

    private Retrofit retrofit;
    private MovieService movieService;

    //构造方法私有
    private HttpMethods3() {
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder.addInterceptor(new HeaderInterceptor());
        builder.addInterceptor(new LoggerInterceptor("HttpLog", true));

        retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        movieService = retrofit.create(MovieService.class);
    }

    //获取单例
    public static HttpMethods3 getInstance() {
        return HttpMethods3.SingletonHolder.INSTANCE;
    }

    /**
     * 登录
     */
    public void getLoginOnlineInfo(Subscriber subscriber, String phone, String password) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("phone", phone);
        map.put("password", password);
        Observable observable = movieService.getLoginOnlineInfoM(map)
                .map(new HttpResultFunc2());

        toSubscribe(observable, subscriber);
    }

    /**
     * 注册
     */
    public void register(Subscriber subscriber, String phone, String password, String captchaCode) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("phone", phone);
        map.put("password", password);
        map.put("captchaCode", captchaCode);
        Observable observable = movieService.register(map)
                .map(new HttpResultFunc2());

        toSubscribe(observable, subscriber);
    }

    /**
     * 发送验证码
     */
    public void sendVerificationCode(Subscriber<HttpResult3> subscriber, String phone, int captchaType) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("phone", phone);
        map.put("captchaType", captchaType);
        Observable observable = movieService.sendVerificationCode(map).map(new HttpMethods3.HttpResultFunc2());
        toSubscribe(observable, subscriber);
    }


    /**
     * 忘记密码
     */
    public void forgetPassword(Subscriber<HttpResult3> subscriber, String phone, String password, String captchaCode) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("phone", phone);
        map.put("password", password);
        map.put("captchaCode", captchaCode);
        Observable observable = movieService.forgetPassword(map).map(new HttpMethods3.HttpResultFunc2<AddressBean>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 添加收货地址
     */
    public void AddAddressRequest(Subscriber<HttpResult3> subscriber, AddressBean addressBean) {
        Observable observable = movieService.addAddressRequest("Bearer " + MyApplication.getInstances().getToken(), addressBean).map(new HttpMethods3.HttpResultFunc2<AddressBean>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 修改收货地址
     */
    public void modifyAddressRequest(Subscriber<HttpResult3> subscriber, AddressBean addressBean) {
        Observable observable = movieService.modifyAddressRequest("Bearer " + MyApplication.getInstances().getToken(), addressBean).map(new HttpMethods3.HttpResultFunc2<AddressBean>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 删除收货地址
     */
    public void deleteDelivery(Subscriber<HttpResult3> subscriber, long addressIds) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("addressIds", String.valueOf(addressIds));
        Observable observable = movieService.deleteDelivery("Bearer " + MyApplication.getInstances().getToken(), map).map(new HttpMethods3.HttpResultFunc2<AddressBean>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 收货地址列表
     */
    public void AddressList(Subscriber<HttpResult5> subscriber, int page, int size) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("page", String.valueOf(page));
        map.put("size", String.valueOf(size));
        Observable observable = movieService.getListAddress("Bearer " + MyApplication.getInstances().getToken(), map).map(new HttpMethods3.HttpResultFunc3<AddressBean>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 修改个人信息
     */
    public void getEditUserInfo(Subscriber<HttpResult3> subscriber, UserBean userBean) {
        Observable observable = movieService.getEditUserInfo("Bearer " + MyApplication.getInstances().getToken(), userBean).map(new HttpMethods3.HttpResultFunc2<UserBean>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取个人信息
     */
    public void getUserInfo(Subscriber<HttpResult3> subscriber) {
        Observable observable = movieService.getUserInfo("Bearer " + MyApplication.getInstances().getToken()).map(new HttpMethods3.HttpResultFunc2());
        toSubscribe(observable, subscriber);
    }


    private <T> void toSubscribe(Observable<T> o, Subscriber<T> s) {
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidScheduler.mainThread())
                .subscribe(s);
    }

    //在访问HttpMethods时创建单例
    private static class SingletonHolder {
        private static final HttpMethods3 INSTANCE = new HttpMethods3();
    }

    private class HttpResultFunc<T> implements Func1<T, T> {

        @Override
        public T call(T httpResult) {

            return httpResult;
        }
    }

    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    private class HttpResultFunc2<T> implements Func1<HttpResult3<T>, T> {

        @Override
        public T call(HttpResult3<T> httpResult) {
            if (!httpResult.isSuccess()) {
                throw new ApiException3(httpResult.getCode(), httpResult.getMessage());
            }
            return httpResult.getData();
        }
    }

    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     * 订单样式---购物车
     *
     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    private class HttpResultFunc3<T> implements Func1<HttpResult5<T>, List<T>> {

        @Override
        public List<T> call(HttpResult5<T> httpResult) {
            if (!httpResult.isSuccess()) {
                throw new ApiException3(httpResult.getCode(), httpResult.getMessage());
            }
            return httpResult.getData();
        }
    }
}
