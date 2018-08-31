package com.example.mettre.myaprojectandroid.http;

import com.example.mettre.myaprojectandroid.bean.AddressBean;
import com.example.mettre.myaprojectandroid.bean.CaptchaBean;
import com.example.mettre.myaprojectandroid.bean.CategoryBean;
import com.example.mettre.myaprojectandroid.bean.GoodsListBean;
import com.example.mettre.myaprojectandroid.bean.LoginBean;
import com.example.mettre.myaprojectandroid.bean.UserBean;
import com.example.mettre.myaprojectandroid.utils.ConstantUtil;

import java.util.HashMap;

import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
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
    Observable<HttpResult3<LoginBean>> getLoginOnlineInfoM(
            @Body HashMap<String, Object> map);

    /**
     * 注册
     */
    @POST(ConstantUtil.USER_INFORMATION + "/register")
    Observable<HttpResult3<AddressBean>> register(
            @Body HashMap<String, Object> map);

    /**
     * 获取用户信息
     */
    @POST(ConstantUtil.USER_INFORMATION + "/loginEd/getUserId")
    Observable<HttpResult3<AddressBean>> getUserInfo(
            @Body HashMap<String, Object> map);

    /**
     * 发送验证码
     */
    @POST(ConstantUtil.CONTROLLER + "/captcha")
    Observable<HttpResult3<CaptchaBean>> sendVerificationCode(
            @Body HashMap<String, Object> map);

    /**
     * 忘记密码
     */
    @POST(ConstantUtil.USER_INFORMATION + "/forgetPassword")
    Observable<HttpResult3<AddressBean>> forgetPassword(
            @Body HashMap<String, Object> map);

    /**
     * 添加收货地址
     */
    @POST(ConstantUtil.DELIVERY + "/loginEd/insertAddress")
    Observable<HttpResult3<AddressBean>> addAddressRequest(
            @Header("authorities") String authorities,
            @Body AddressBean bean);

    /**
     * 修改收货地址
     */
    @POST(ConstantUtil.DELIVERY + "/loginEd/updateAddress")
    Observable<HttpResult3<AddressBean>> modifyAddressRequest(
            @Header("authorities") String authorities,
            @Body AddressBean bean);

    /**
     * 删除收货地址
     */
    @POST(ConstantUtil.DELIVERY + "/loginEd/deleteDelivery")
    Observable<HttpResult3<AddressBean>> deleteDelivery(
            @Header("authorities") String authorities,
            @Body HashMap<String, String> map);


    /**
     * 收货地址列表
     */
    @POST(ConstantUtil.DELIVERY + "/loginEd/findByPage")
    Observable<HttpResult5<AddressBean>> getListAddress(
            @Header("authorities") String authorities,
            @Body HashMap<String, String> map);


    /**
     * 修改个人信息
     */
    @POST(ConstantUtil.USER_INFORMATION + "/loginEd/editUserInfo")
    Observable<HttpResult3<UserBean>> getEditUserInfo(
            @Header("authorities") String authorities,
            @Body UserBean bean);

    /**
     * 获取个人信息
     */
    @GET(ConstantUtil.USER_INFORMATION + "/loginEd/getUserInfo")
    Observable<HttpResult3<UserBean>> getUserInfo(
            @Header("authorities") String authorities);


    /**
     * 商品三级分类
     */
    @POST(ConstantUtil.SHOPPING + "/category/findAllCategory")
    Observable<HttpResult5<CategoryBean>> getCategoryList();

    /**
     * 查找商品列表
     */
    @FormUrlEncoded
    @POST(ConstantUtil.SHOPPING + "/goods/findGoods")
    Observable<HttpResult5<GoodsListBean>> getGoodsList(
            @Field(value = "categoryId") int categoryId, @Field(value = "page") int page, @Field(value = "size") int size);

}
