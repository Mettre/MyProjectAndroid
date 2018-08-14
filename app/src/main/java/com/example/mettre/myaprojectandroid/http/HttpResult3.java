package com.example.mettre.myaprojectandroid.http;

import java.util.List;

/**
 * Created by app on 2017/9/25.
 */

public class HttpResult3<T> {

    private T data;
    private boolean result;
    private String message;
    private String errCode;
    private List<ErrorsBean> errors;

    public T getData() {
        return data;
    }

    public void setData(T data) {
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

    public static class ErrorsBean {
        /**
         * code : string
         * errorMsg : string
         * field : string
         * objectName : string
         */

        private String code;
        private String errorMsg;
        private String field;
        private String objectName;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getErrorMsg() {
            return errorMsg;
        }

        public void setErrorMsg(String errorMsg) {
            this.errorMsg = errorMsg;
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public String getObjectName() {
            return objectName;
        }

        public void setObjectName(String objectName) {
            this.objectName = objectName;
        }
    }
}
