package com.example.mettre.myaprojectandroid.menu;


import com.example.mettre.myaprojectandroid.constant.CommonConstant;

public class MettreMenu {

    public static String getSexStr(int sex) {
        return sex == CommonConstant.SEX_MAN ? "男" : "女";
    }

    public static int getSexInterger(String sex) {
        return "男".equals(sex) ? CommonConstant.SEX_WOMAN : CommonConstant.SEX_MAN;
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
