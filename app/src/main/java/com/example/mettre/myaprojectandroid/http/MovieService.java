package com.example.mettre.myaprojectandroid.http;

import com.example.mettre.myaprojectandroid.bean.AddressBean;
import com.example.mettre.myaprojectandroid.bean.AdvBean;
import com.example.mettre.myaprojectandroid.bean.CaptchaBean;
import com.example.mettre.myaprojectandroid.bean.CartBean;
import com.example.mettre.myaprojectandroid.bean.CategoryBean;
import com.example.mettre.myaprojectandroid.bean.GoodsDetailsBean;
import com.example.mettre.myaprojectandroid.bean.GoodsListBean;
import com.example.mettre.myaprojectandroid.bean.LoginBean;
import com.example.mettre.myaprojectandroid.bean.OrderRequestBean;
import com.example.mettre.myaprojectandroid.bean.UserBean;
import com.example.mettre.myaprojectandroid.utils.ConstantUtil;

import java.util.HashMap;
import java.util.List;

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
     * 广告列表
     */
    @FormUrlEncoded
    @POST(ConstantUtil.SHOPPING + "/adv/findSpecificAdv")
    Observable<HttpResult5<AdvBean>> getBannerList(
            @Field(value = "adPositionNo") String adPositionNo);


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

    /**
     * 获取商品详细地址
     */
    @FormUrlEncoded
    @POST(ConstantUtil.SHOPPING + "/goods/findGoodDetails")
    Observable<HttpResult3<GoodsDetailsBean>> getGoodsDetails(
            @Field(value = "goodsId") long goodsId);

    /**
     * 编辑购物车数量
     */
    @FormUrlEncoded
    @POST(ConstantUtil.SHOPPING + "/cart/editCartNum")
    Observable<HttpResult3<GoodsDetailsBean>> editCartNum(
            @Field(value = "sessionId") Long sessionId, @Field(value = "goodsId") long goodsId, @Field(value = "cartNumber") int cartNumber);


    /**
     * 购物车添加商品
     */
    @FormUrlEncoded
    @POST(ConstantUtil.SHOPPING + "/loginEd/cart/addCart")
    Observable<HttpResult3> getCartGoodsNoInfo(
            @Field(value = "sessionId") Long sessionId,
            @Field(value = "goodsId") Long goodsId, @Field(value = "cartNumber") int cartNumber);

    /**
     * 获取购物车列表
     */
    @FormUrlEncoded
    @POST(ConstantUtil.SHOPPING + "/loginEd/cart/findAllCart")
    Observable<HttpResult5<CartBean>> getCartListInfo(
            @Field(value = "sessionId") Long sessionId);

    /**
     * 删除某个购物车项
     */
    @FormUrlEncoded
    @POST(ConstantUtil.SHOPPING + "/cart/deleteCart")
    Observable<HttpResult3> deleteCart(
            @Field(value = "cartIds") List<Long> cartIds);

    /**
     * 提交订单
     */
    @POST(ConstantUtil.SHOPPING + "/loginEd/order/addOrder")
    Observable<HttpResult3> submitOrder(
            @Body List<OrderRequestBean> orderItems);


}
