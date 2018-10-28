package com.example.mettre.myaprojectandroid.http;

import com.example.mettre.myaprojectandroid.app.MyApplication;
import com.example.mettre.myaprojectandroid.bean.AddressBean;
import com.example.mettre.myaprojectandroid.bean.AdvBean;
import com.example.mettre.myaprojectandroid.bean.CartBean;
import com.example.mettre.myaprojectandroid.bean.EnumBean;
import com.example.mettre.myaprojectandroid.bean.GoodsDetailsBean;
import com.example.mettre.myaprojectandroid.bean.GoodsListBean;
import com.example.mettre.myaprojectandroid.bean.NoticeBean;
import com.example.mettre.myaprojectandroid.bean.OrderBean;
import com.example.mettre.myaprojectandroid.bean.OrderRequestBean;
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

public class HttpMethods {

//    public static final String BASE_URL = "http://192.168.0.176:8800/";//公司

//    public static final String BASE_URL = "http://192.168.1.107:8800/";//家

    public static final String BASE_URL = "http://114.116.15.224:8800/";//华为服务器

    private static final int DEFAULT_TIMEOUT = 25;

    private Retrofit retrofit;
    private MovieService movieService;

    //构造方法私有
    private HttpMethods() {
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
    public static HttpMethods getInstance() {
        return HttpMethods.SingletonHolder.INSTANCE;
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
    public void sendVerificationCode(Subscriber<HttpResult3> subscriber, String phone, String captchaCode) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("phone", phone);
        map.put("captchaCode", captchaCode);
        Observable observable = movieService.sendVerificationCode(map).map(new HttpMethods.HttpResultFunc2());
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
        Observable observable = movieService.forgetPassword(map).map(new HttpMethods.HttpResultFunc2<AddressBean>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 添加收货地址
     */
    public void AddAddressRequest(Subscriber<HttpResult3> subscriber, AddressBean addressBean) {
        Observable observable = movieService.addAddressRequest("Bearer " + MyApplication.getInstances().getToken(), addressBean).map(new HttpMethods.HttpResultFunc2<AddressBean>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 修改收货地址
     */
    public void modifyAddressRequest(Subscriber<HttpResult3> subscriber, AddressBean addressBean) {
        Observable observable = movieService.modifyAddressRequest("Bearer " + MyApplication.getInstances().getToken(), addressBean).map(new HttpMethods.HttpResultFunc2<AddressBean>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 删除收货地址
     */
    public void deleteDelivery(Subscriber<HttpResult3> subscriber, long addressIds) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("addressIds", String.valueOf(addressIds));
        Observable observable = movieService.deleteDelivery("Bearer " + MyApplication.getInstances().getToken(), map).map(new HttpMethods.HttpResultFunc2<AddressBean>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 收货地址列表
     */
    public void AddressList(Subscriber<HttpResult5> subscriber, int page, int size) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("page", String.valueOf(page));
        map.put("size", String.valueOf(size));
        Observable observable = movieService.getListAddress("Bearer " + MyApplication.getInstances().getToken(), map).map(new HttpMethods.HttpResultFunc3<AddressBean>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 广告列表
     */
    public void getBannerList(Subscriber<HttpResult5> subscriber, String adPositionNo) {
        Observable observable = movieService.getBannerList(adPositionNo).map(new HttpMethods.HttpResultFunc3<AdvBean>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 修改个人信息
     */
    public void getEditUserInfo(Subscriber<HttpResult3> subscriber, UserBean userBean) {
        Observable observable = movieService.getEditUserInfo("Bearer " + MyApplication.getInstances().getToken(), userBean).map(new HttpMethods.HttpResultFunc2<UserBean>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取个人信息
     */
    public void getUserInfo(Subscriber<HttpResult3> subscriber) {
        Observable observable = movieService.getUserInfo("Bearer " + MyApplication.getInstances().getToken()).map(new HttpMethods.HttpResultFunc2());
        toSubscribe(observable, subscriber);
    }

    /**
     * 商品三级分类
     */
    public void getCategoryList(Subscriber<HttpResult5> subscriber) {
        Observable observable = movieService.getCategoryList().map(new HttpMethods.HttpResultFunc3());
        toSubscribe(observable, subscriber);
    }


    /**
     * 获取商品列表
     */
    public void getGoodsList(Subscriber<HttpResult5> subscriber, int categoryId, int page, int size) {
        Observable observable = movieService.getGoodsList(categoryId, page, size).map(new HttpMethods.HttpResultFunc3<GoodsListBean>());
        toSubscribe(observable, subscriber);
    }


    /**
     * 获取商品详细地址
     */
    public void getGoodsDetails(Subscriber<HttpResult3> subscriber, Long goodsId) {
        Observable observable = movieService.getGoodsDetails(goodsId).map(new HttpMethods.HttpResultFunc2<GoodsDetailsBean>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 编辑购物车数量
     */
    public void editCartNum(Subscriber<HttpResult3> subscriber, Long sessionId, long goodsId, int cartNumber) {
        Observable observable = movieService.editCartNum(sessionId, goodsId, cartNumber).map(new HttpMethods.HttpResultFunc2<GoodsDetailsBean>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 删除某个购物车项
     */
    public void deleteCart(Subscriber<HttpResult3> subscriber, List<Long> cartIds) {
        Observable observable = movieService.deleteCart(cartIds).map(new HttpMethods.HttpResultFunc2());
        toSubscribe(observable, subscriber);
    }


    /**
     * 加入购物车
     */
    public void getCartGoodsInfo(Subscriber<HttpResult3> subscriber, Long sessionId, Long goodsId, int cartNumber) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("sessionId", sessionId);
        map.put("goodsId", goodsId);
        map.put("cartNumber", cartNumber);
        Observable observable = movieService.getCartGoodsNoInfo(sessionId, goodsId, cartNumber).map(new HttpMethods.HttpResultFunc2());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取购物车列表
     */
    public void getCartListInfo(Subscriber<HttpResult5> subscriber, long sessionId) {
        Observable observable = movieService.getCartListInfo(sessionId).map(new HttpMethods.HttpResultFunc3<CartBean>());
        toSubscribe(observable, subscriber);
    }


    /**
     * 提交订单
     */
    public void submitOrder(Subscriber<HttpResult3> subscriber, List<OrderRequestBean> orderItems) {
        Observable observable = movieService.submitOrder(orderItems).map(new HttpMethods.HttpResultFunc2());
        toSubscribe(observable, subscriber);
    }


    /**
     * 订单列表
     */
    public void getOrderList(Subscriber<HttpResult5> subscriber, int page, int pageSize, int orderStatus) {
        Observable observable = movieService.getOrderList(page, pageSize, orderStatus).map(new HttpMethods.HttpResultFunc3<OrderBean>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 订单详情
     */
    public void getOrderDetail(Subscriber<HttpResult3> subscriber, Long orderId) {
        Observable observable = movieService.getOrderDetail(orderId).map(new HttpMethods.HttpResultFunc2<OrderBean>());
        toSubscribe(observable, subscriber);
    }


    /**
     * 取消订单
     */
    public void cancelOrder(Subscriber<HttpResult3> subscriber, Long orderId) {
        Observable observable = movieService.cancelOrder(orderId).map(new HttpMethods.HttpResultFunc2());
        toSubscribe(observable, subscriber);
    }


    /**
     * 公告列表
     */
    public void getNoticeList(Subscriber<HttpResult5> subscriber, int page, int size) {
        Observable observable = movieService.getNoticeList(page, size).map(new HttpMethods.HttpResultFunc3<NoticeBean>());
        toSubscribe(observable, subscriber);
    }


    /**
     * 促销商品
     */
    public void getPromotionInfo(Subscriber<HttpResult5> subscriber, int page, int size) {
        Observable observable = movieService.getPromotionInfo(page, size).map(new HttpMethods.HttpResultFunc3<GoodsListBean>());
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
        private static final HttpMethods INSTANCE = new HttpMethods();
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
                throw new ApiException(httpResult.getCode(), httpResult.getMessage());
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
                throw new ApiException(httpResult.getCode(), httpResult.getMessage());
            }
            return httpResult.getData();
        }
    }
}
