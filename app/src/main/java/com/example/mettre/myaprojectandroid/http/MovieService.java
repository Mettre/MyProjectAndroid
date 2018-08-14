package com.example.mettre.myaprojectandroid.http;

import com.example.mettre.myaprojectandroid.bean.AddressBean;
import com.example.mettre.myaprojectandroid.utils.ConstantUtil;

import java.util.HashMap;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by liukun on 16/3/9.
 */
public interface MovieService {

    /**
     * 商品列表查询 =-新
     */
    @POST(ConstantUtil.SHOPPINGMALLSERVER + "/fproducts")
    Observable<HttpResult3<AddressBean>> getlistgoodsInfoM(
            @Header("Authorization") String Authorization,
            @Body HashMap<String, Object> map);

    /**
     * 商品列表查询 =-新
     */
    @POST(ConstantUtil.SHOPPINGMALLSERVER + "/fproducts")
    Observable<HttpResult3<AddressBean>> getlistgoodsInfo(
            @Header("Authorization") String Authorization,
            @Body AddressBean addressBean);

    /**
     * 商品列表查询 =-新
     */
    @POST(ConstantUtil.SHOPPINGMALLSERVER + "/fproducts")
    Observable<HttpResult3<AddressBean>> getAddressList(
            @Header("Authorization") String Authorization,
            @Body HashMap<String, String> map);

}
