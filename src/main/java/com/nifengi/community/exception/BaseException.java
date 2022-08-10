package com.nifengi.community.exception;

/**
 * @author Yu
 * @title: BaseException
 * @projectName community
 * @date 2022/7/27 12:00
 */
public class BaseException extends RuntimeException {
    protected Integer errorCode;
    protected String errorMsg;

    public BaseException(){

    }
    public BaseException(Integer errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
