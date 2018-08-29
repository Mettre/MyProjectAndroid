package com.example.mettre.myaprojectandroid.utils;

import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mettre.myaprojectandroid.app.MyApplication;
import com.example.mettre.myaprojectandroid.view.ClearEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by app on 2017/7/26.
 * 登录-注册-修改密码-忘记密码等 。。。输入验证
 */

public class LoginUtils {

    public static final LoginUtils getInstance() {
        return LoginUtilsHolder.INSTANCE;
    }

    /**
     * 验证手机号码
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles) {

//        String regExp = "^((13[0-9])|(15[^4])|(18[0,1,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
//        Pattern p = Pattern.compile(regExp);
//        Matcher m = p.matcher(mobiles);
//        return m.matches();
        return true;

    }

    /**
     * 验证邮箱
     *
     * @param email
     * @return
     */
    public static boolean checkEmail(String email) {
        boolean flag = false;
        try {
            String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }


    /**
     * 登录校验
     */
    public Boolean loginCheck(ClearEditText phone, EditText password) {
        Boolean type = false;
        if (TextUtils.isEmpty(phone.getText().toString())) {
            ToastUtils.showShortToastSafe("手机号码不能为空！");
            phone.setShakeAnimation();
        } else if (!isMobileNO(phone.getText().toString())) {
            ToastUtils.showShortToastSafe("请输入正确的手机号码！");
            phone.setShakeAnimation();
        } else if (TextUtils.isEmpty(password.getText().toString())) {
            ToastUtils.showShortToastSafe("密码不能为空！");
        } else if (password.getText().toString().length() < 6) {
            ToastUtils.showShortToastSafe("账号或者密码不正确！");
        } else {
            type = true;
        }
        return type;
    }

    /**
     * 验证码验证
     *
     * @param verificationCode
     * @return
     */
    public Boolean verificationCode(EditText verificationCode) {
        Boolean type = false;
        if (TextUtils.isEmpty(verificationCode.getText().toString())) {
            ToastUtils.showShortToastSafe("请输入验证码不对");
        } else if (verificationCode.getText().toString().length() < 4) {
            ToastUtils.showShortToastSafe("验证码不对");
        } else {
            type = true;
        }
        return type;
    }

    /**
     * 注册验证
     */

    public Boolean RegisterVerification(EditText phone, EditText verificationCodeNum, EditText Password) {
        Boolean type = false;
        if (TextUtils.isEmpty(phone.getText().toString())) {
            ToastUtils.showShortToastSafe("手机号码不能为空");
        } else if (!isMobileNO(phone.getText().toString())) {
            ToastUtils.showShortToastSafe("请输入正确的手机号码");
        } else if (TextUtils.isEmpty(verificationCodeNum.getText().toString())) {
            ToastUtils.showShortToastSafe("验证码不能为空");
        } else if (verificationCodeNum.getText().toString().length() < 4) {
            ToastUtils.showShortToastSafe("验证码不正确");
        } else if (TextUtils.isEmpty(Password.getText().toString())) {
            ToastUtils.showShortToastSafe("请输入6-16位密码");
        } else if (Password.getText().toString().length() < 6 || Password.getText().toString().length() > 16) {
            ToastUtils.showShortToastSafe("密码必须6-16位");
        } else {
            type = true;
        }
        return type;
    }

    /**
     * 修改密码验证
     *
     * @return
     */
    public Boolean ModifyPassword(TextView phone, EditText verificationCodeNum, EditText newPassword) {
        Boolean type = false;
        if (TextUtils.isEmpty(phone.getText().toString())) {
            ToastUtils.showShortToastSafe("手机号码不能为空！");
        } else if (!isMobileNO(phone.getText().toString())) {
            ToastUtils.showShortToastSafe("请输入正确的手机号码");
        } else if (TextUtils.isEmpty(verificationCodeNum.getText().toString())) {
            ToastUtils.showShortToastSafe("验证码不能为空！");
        } else if (TextUtils.isEmpty(newPassword.getText().toString())) {
            ToastUtils.showShortToastSafe("请输入新密码");
        } else if (newPassword.getText().toString().length() < 6 || newPassword.getText().toString().length() > 16) {
            ToastUtils.showShortToastSafe("密码必须6-16位");
        } else {
            type = true;
        }
        return type;
    }

    /**
     * 忘记密码验证
     *
     * @return
     */
    public Boolean ModifyPassword(EditText phone, EditText verificationCodeNum, EditText newPassword) {
        Boolean type = false;
        if (TextUtils.isEmpty(phone.getText().toString())) {
            ToastUtils.showShortToastSafe("手机号码不能为空！");
        } else if (!isMobileNO(phone.getText().toString())) {
            ToastUtils.showShortToastSafe("请输入正确的手机号码");
        } else if (TextUtils.isEmpty(verificationCodeNum.getText().toString())) {
            ToastUtils.showShortToastSafe("验证码不能为空！");
        } else if (TextUtils.isEmpty(newPassword.getText().toString())) {
            ToastUtils.showShortToastSafe("请输入新密码");
        } else if (newPassword.getText().toString().length() < 6 || newPassword.getText().toString().length() > 16) {
            ToastUtils.showShortToastSafe("密码必须6-16位");
        } else {
            type = true;
        }
        return type;
    }

    /**
     * 新添加地址验证
     */
    public Boolean AddAddress(EditText nameAddress, EditText phoneAddress, TextView cityChoice, EditText detailedAddress) {
        Boolean type = false;

        if (TextUtils.isEmpty(nameAddress.getText().toString())) {
            ToastUtils.showShortToastSafe("收货人不能为空！");
        } else if (TextUtils.isEmpty(phoneAddress.getText().toString())) {
            ToastUtils.showShortToastSafe("手机号不能为空！");
        } else if (!isMobileNO(phoneAddress.getText().toString())) {
            ToastUtils.showShortToastSafe("请输入正确的手机号码！");
        } else if (TextUtils.isEmpty(cityChoice.getText().toString())) {
            ToastUtils.showShortToastSafe("请选择收货地址！");
        } else if (TextUtils.isEmpty(detailedAddress.getText().toString())) {
            ToastUtils.showShortToastSafe("请输入详细地址！");
        } else {
            type = true;
        }
        return type;
    }

    /**
     * 添加房屋验证
     */
    public Boolean AddHouse(EditText name_text, EditText phone_text, TextView house_city_text, TextView house_community_text, TextView house_building_text, TextView house_unit_text, TextView house_doorplate_text, TextView genderText, Boolean hasShow) {
        Boolean type = false;

        if (TextUtils.isEmpty(name_text.getText().toString())) {
            ToastUtils.showShortToastSafe("业主姓名不能为空！");
        } else if (TextUtils.isEmpty(phone_text.getText().toString())) {
            ToastUtils.showShortToastSafe("手机号不能为空！");
        } else if (TextUtils.isEmpty(genderText.getText().toString())) {
            ToastUtils.showShortToastSafe("请选择性别！");
        } else if (!isMobileNO(phone_text.getText().toString())) {
            ToastUtils.showShortToastSafe("请输入正确的手机号码！");
        } else if (TextUtils.isEmpty(house_city_text.getText().toString())) {
            ToastUtils.showShortToastSafe("请选择房屋所在地区！");
        } else if (TextUtils.isEmpty(house_community_text.getText().toString())) {
            ToastUtils.showShortToastSafe("请选择小区！");
        } else if (TextUtils.isEmpty(house_building_text.getText().toString())) {
            ToastUtils.showShortToastSafe("请填写小区所在位置！");
        } else if (hasShow && TextUtils.isEmpty(house_unit_text.getText().toString())) {
            ToastUtils.showShortToastSafe("请选择单元！");
        } else if (TextUtils.isEmpty(house_doorplate_text.getText().toString())) {
            ToastUtils.showShortToastSafe("请填写门牌号！");
        } else {
            type = true;
        }
        return type;
    }


    /**
     * 添加家庭成员验证
     */
    public Boolean AddMemberFamily(EditText name_text, EditText phone_text, TextView genderText) {
        Boolean type = false;

        if (TextUtils.isEmpty(name_text.getText().toString())) {
            ToastUtils.showShortToastSafe("姓名不能为空！");
        } else if (TextUtils.isEmpty(phone_text.getText().toString())) {
            ToastUtils.showShortToastSafe("手机号不能为空！");
        } else if (!isMobileNO(phone_text.getText().toString())) {
            ToastUtils.showShortToastSafe("请输入正确的手机号码！");
        } else if (TextUtils.isEmpty(genderText.getText().toString())) {
            ToastUtils.showShortToastSafe("请选择性别");
        } else {
            type = true;
        }
        return type;
    }

    private static class LoginUtilsHolder {
        private static final LoginUtils INSTANCE = new LoginUtils();
    }

    /**
     * 登录存储token 账号等
     */
    public void loginSaveToken(String phone, String access_token) {
        SharedPrefsUtil.putValue(Utils.getContext(), "phone", phone);
        SharedPrefsUtil.putValue(Utils.getContext(), "token", access_token);
        MyApplication.getInstances().setToken(access_token);
    }


    /**
     * 退出登录清除token
     */
    public void signOutRemoveToken() {
        SharedPrefsUtil.removeValue(Utils.getContext(), "token");
        MyApplication.getInstances().setToken("");
    }
}
