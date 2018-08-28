package com.example.mettre.myaprojectandroid.http;

/**
 * Created by app on 2017/9/25.
 */

public class ApiException3 extends RuntimeException {

    public static final int USER_NOT_EXIST = 200;
    public static final int WRONG_PASSWORD = 100;
    private static final int LOGINEXPIRED = 300;
    private static final int USER_CODE_ERROR = 400;

    public ApiException3(Integer code, String Message) {
        this(getApiExceptionMessage(code, Message));
    }

    public ApiException3(String detailMessage) {
        super(detailMessage);
    }

    /**
     * 由于服务器传递过来的错误信息直接给用户看的话，用户未必能够理解
     * 需要根据错误码对错误信息进行一个转换，在显示给用户
     *
     * @param code
     * @return
     */
    private static String getApiExceptionMessage(int code, String Message) {
        String message = "";
        switch (code) {
            case USER_NOT_EXIST:
                message = "该用户不存在";
                break;
            case WRONG_PASSWORD:
                message = "密码错误";
                break;
            case LOGINEXPIRED:
                message = "用户登录已过期，请重新登录!";
                break;
            case USER_CODE_ERROR:
                message = "用户编码错误";
                break;
            default:
                message = Message;

        }
        return message;
    }
}
