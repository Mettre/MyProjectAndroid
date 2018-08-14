package com.example.mettre.myaprojectandroid.http;

import java.util.List;

/**
 * Created by app on 2017/10/13.
 */

public class HttpResult5<T> {

    private List<T> data;
    private boolean result;
    private String message;
    private String errCode;
    private List<HttpResult3.ErrorsBean> errors;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public List<HttpResult3.ErrorsBean> getErrors() {
        return errors;
    }

    public void setErrors(List<HttpResult3.ErrorsBean> errors) {
        this.errors = errors;
    }
}
