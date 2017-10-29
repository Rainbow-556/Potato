package com.lx.potato.model.remote;

/**
 * Created by lixiang on 2017/10/29.
 */
public final class ApiException extends Exception {
    public static String CODE_DEFAULT = "0";
    public static String CODE_TIME_OUT = "1";
    public static String CODE_JSON_ERROR = "2";
    public static String CODE_UNCONNECTED = "3";
    private String errorCode = CODE_DEFAULT;
    private String errorMsg;

    public ApiException() {
    }

    public ApiException(String errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public String toString() {
        return "ApiException{" +
                "errorCode='" + errorCode + '\'' +
                ", errorMsg='" + errorMsg + '\'' +
                '}';
    }
}
