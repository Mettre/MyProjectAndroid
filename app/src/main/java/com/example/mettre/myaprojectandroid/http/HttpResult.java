package com.example.mettre.myaprojectandroid.http;

/**
 * Created by liukun on 16/3/5.
 */
public class HttpResult<T> {

    /**
     * status : 1成功 ; 0失败
     * message : 操作成功
     * data :
     * total : 返回数据总数
     */

    private int status;
    private String message;
    private String total;
    private T data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
