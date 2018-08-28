package com.example.mettre.myaprojectandroid.http;

import java.util.List;

/**
 * Created by app on 2017/10/13.
 */

public class HttpResult5<T> {

    private List<T> data;
    private boolean success;
    private String message;
    private Integer code;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
