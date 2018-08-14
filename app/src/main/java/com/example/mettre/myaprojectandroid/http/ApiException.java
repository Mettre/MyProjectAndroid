package com.example.mettre.myaprojectandroid.http;

/**
 * Created by liukun on 16/3/10.
 */
public class ApiException extends RuntimeException {

    public static final int USER_NOT_EXIST = 100;
    public static final int WRONG_PASSWORD = 101;
    private static final int LOGINEXPIRED =401;
    private static final int USER_CODE_ERROR =3014;

    public ApiException(int status,String Message) {
        this(getApiExceptionMessage(status,Message));
    }

    public ApiException(String detailMessage) {
        super(detailMessage);
    }

    /**
     * 由于服务器传递过来的错误信息直接给用户看的话，用户未必能够理解
     * 需要根据错误码对错误信息进行一个转换，在显示给用户
     * @param status
     * @return
     */
    private static String getApiExceptionMessage(int status, String Message){
        String message = "";
        switch (status) {
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

