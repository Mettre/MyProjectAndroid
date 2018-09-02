package com.example.mettre.myaprojectandroid.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;

public class BigDecimalUtils {

    public static String wipeBigDecimalZero(BigDecimal number) {
//        if(number==null) return "";
        number.setScale(2,BigDecimal.ROUND_DOWN);
        NumberFormat nf = NumberFormat.getInstance();
        return "￥"+nf.format(number);
    }
}
