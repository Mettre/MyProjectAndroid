package com.example.mettre.myaprojectandroid.constant;


/**
 * 常量
 *
 * @author Exrickx
 */
public interface CommonConstant {

    /**
     * 个人信息刷新
     */
    Integer USER_INFOR = 1111;

    /**
     * 商品列表刷新
     */
    Integer GOODS_REFRESH = 2222222;

    /**
     * 性别男
     */
    Integer SEX_MAN = 1;

    /**
     * 性别女
     */
    Integer SEX_WOMAN = 0;

    /**
     * 性别保密
     */
    Integer SEX_SECRET = 2;

    //首页轮播图编码
    String HOME_PROMOTION = "HOME_PROMOTION";

    //首页推荐图编码
    String HOME_RECOMMENT = "HOME_RECOMMENT";

    /**
     * 限流标识
     */
    String LIMIT_ALL = "LIMIT_ALL";

    /**
     * 页面类型权限
     */
    Integer PERMISSION_PAGE = 0;

    /**
     * 操作类型权限
     */
    Integer PERMISSION_OPERATION = 1;

    /**
     * 1级菜单
     */
    Integer LEVEL_ONE = 1;

    /**
     * 2级菜单
     */
    Integer LEVEL_TWO = 2;

    /**
     * 3级菜单
     */
    Integer LEVEL_THREE = 3;

    /**
     * 注册验证码
     */
    Integer REGISTER_CAPTCHA = 1;

    /**
     * 忘记密码验证码
     */
    Integer FORGET_CAPTCHA = 2;


    String JWT_TOKEN = "secretkey";

    /**
     * 权限参数头
     */
    String AUTHORITIES = "authorities";
}
