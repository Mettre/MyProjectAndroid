package com.example.mettre.myaprojectandroid.http;

import com.example.mettre.myaprojectandroid.bean.AddressBean;
import com.example.mettre.myaprojectandroid.utils.ConstantUtil;

import java.util.HashMap;

import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by liukun on 16/3/9.
 */
public interface MovieService {

    /**
     * 登录
     */
    @POST(ConstantUtil.USER_INFORMATION + "/login")
    Observable<HttpResult3<AddressBean>> getLoginOnlineInfoM(
            @QueryMap HashMap<String, Object> map);

    /**
     * 注册
     */
    @POST(ConstantUtil.USER_INFORMATION + "/register")
    Observable<HttpResult3<AddressBean>> register(
            @QueryMap HashMap<String, Object> map);

    /**
     * 获取用户信息
     */
    @POST(ConstantUtil.USER_INFORMATION + "/loginEd/getUserId")
    Observable<HttpResult3<AddressBean>> getUserInfo(
            @QueryMap HashMap<String, Object> map);

    /**
     * 发送验证码
     */
    @POST(ConstantUtil.USER_INFORMATION + "/captcha")
    Observable<HttpResult3<AddressBean>> sendVerificationCode(
            @QueryMap HashMap<String, Object> map);

    /**
     * 忘记密码
     */
    @POST(ConstantUtil.USER_INFORMATION + "/forgetPassword")
    Observable<HttpResult3<AddressBean>> forgetPassword(
            @QueryMap HashMap<String, Object> map);

    /**
     * 添加收货地址
     */
    @POST(ConstantUtil.DELIVERY + "/loginEd/insertAddress")
    Observable<HttpResult3<AddressBean>> addAddressRequest(
            @Header("Authorization") String Authorization,
            @QueryMap AddressBean bean);

    /**
     * 收货地址列表
     */
    @POST(ConstantUtil.DELIVERY + "/loginEd/updateAddress")
    Observable<HttpResult3<AddressBean>> modifyAddressRequest(
            @Header("Authorization") String Authorization,
            @QueryMap AddressBean bean);


    /**
     * 收货地址列表
     */
    @POST(ConstantUtil.DELIVERY + "/loginEd/findByPage")
    Observable<HttpResult3<AddressBean>> getListAddress(
            @Header("Authorization") String Authorization,
            @QueryMap HashMap<String, String> map);

}
