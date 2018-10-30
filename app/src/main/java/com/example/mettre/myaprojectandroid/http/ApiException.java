package com.example.mettre.myaprojectandroid.http;

/**
 * Created by app on 2017/9/25.
 */

public class ApiException extends RuntimeException {

    public static final int USER_NOT_EXIST = 200;
    public static final int WRONG_PASSWORD = 100;
    private static final int LOGINEXPIRED = 300;
    private static final int USER_CODE_ERROR = 400;
    private static final int NOT_LOGIN = 401;

    public ApiException(Integer code, String Message) {
        this(getApiExceptionMessage(code, Message));
    }

    public ApiException(String detailMessage) {
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
            case LOGINEXPIRED:
                message = "用户登录已过期，请重新登录!";
                break;
            case USER_CODE_ERROR:
                message = "用户编码错误";
                break;
            case NOT_LOGIN:
                message = "未登录";
                break;
            default:
                message = Message;
        }
        return message;
    }
}
