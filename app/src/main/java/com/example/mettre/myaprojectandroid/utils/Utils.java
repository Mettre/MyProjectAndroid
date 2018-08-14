package com.example.mettre.myaprojectandroid.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.math.BigDecimal;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 16/12/08
 *     desc  : Utils初始化相关
 * </pre>
 */
public class Utils {

    private static Context context;

    private Utils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 初始化工具类
     *
     * @param context 上下文
     */
    public static void init(Context context) {
        Utils.context = context.getApplicationContext();
    }

    /**
     * 获取ApplicationContext
     *
     * @return ApplicationContext
     */
    public static Context getContext() {
        if (context != null) return context;
        throw new NullPointerException("u should init first");
    }

    //导致TextView异常换行的原因：安卓默认数字、字母不能为第一行以后每行的开头字符，因为数字、字母为半角字符
    //所以我们只需要将半角字符转换为全角字符即可，方法如下
    public static String ToSBC(String input) {
        if (input.isEmpty()) return input;
        char c[] = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == ' ') {
                c[i] = '\u3000';
            } else if (c[i] < '\177') {
                c[i] = (char) (c[i] + 65248);
            }
        }
        return new String(c);
    }

    public static Integer getTotalPage(int totalCount, int pageSize) {
        int totalPage;
        if (totalCount % pageSize != 0) {
            totalPage = totalCount / pageSize + 1;
        } else {
            totalPage = totalCount / pageSize;
        }
        return totalPage;
    }

    /**
     * 加法
     */
    public static BigDecimal add(double price1, double price2) {
        BigDecimal bignum1 = new BigDecimal(price1);
        BigDecimal bignum2 = new BigDecimal(price2);
        return bignum1.add(bignum2);
    }

    /**
     * 加
     */
    public static BigDecimal cc(double price1, double price2) {
        BigDecimal bignum1 = new BigDecimal(price1);
        BigDecimal bignum2 = new BigDecimal(price2);
        return bignum1.add(bignum2);
    }


    /**
     * 减
     */
    public static BigDecimal subtract(double price1, double price2) {
        BigDecimal bignum1 = new BigDecimal(price1);
        BigDecimal bignum2 = new BigDecimal(price2);
        return bignum1.subtract(bignum2);
    }


    /**
     * 乘
     */
    public static BigDecimal x(double price1, double price2) {
        BigDecimal bignum1 = new BigDecimal(price1);
        BigDecimal bignum2 = new BigDecimal(price2);
        return bignum1.subtract(bignum2);
    }

    /**
     * 获取本地软件版本号
     */
    public static int getLocalVersion(Context ctx) {
        int localVersion = 0;
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionCode;
            Log.d("TAG", "本软件的版本号。。" + localVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }

    /**
     * 获取本地软件版本号名称
     */
    public static String getLocalVersionName(Context ctx) {
        String localVersion = "";
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionName;
            Log.d("TAG", "本软件的版本号。。" + localVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }

    public static String getGender(String gender) {
        String genderStr = "";
        switch (gender) {
            case "MALE":
                genderStr = "男";
                break;
            case "FEMALE":
                genderStr = "女";
                break;
        }
        return genderStr;
    }

    /**
     * 支付方式
     *
     * @param pay
     * @return
     */
    public static String getPayStr(String pay) {
        String payStr = "";
        switch (pay) {
            case "WX_PAY":
                payStr = "微信";
                break;
            case "ALI_PAY":
                payStr = "支付宝";
                break;
            case "BANK_CARD":
                payStr = "银行卡";
                break;
            case " OFFLINE":
                payStr = "线下";
                break;
        }
        return payStr;
    }

}