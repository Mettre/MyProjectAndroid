package com.example.mettre.myaprojectandroid.bean;

public class EnumBean {

    /**
     * 短信类型
     */
    public enum CaptchaEnum {

        REGISTER_SMS("SMS_146615681", "注册"),
        MODIFY_SMS("SMS_146616148", "重置密码");

        private String code;
        private String explain;

        CaptchaEnum(String code, String explain) {
            this.code = code;
            this.explain = explain;
        }

        public String getCode() {
            return code;
        }

        public String getExplain() {
            return explain;
        }
    }
}
