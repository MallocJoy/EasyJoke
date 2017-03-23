package com.utouu.easyjoke.data.exception;

/**
 * Create by 黄思程 on 2017/3/20  16:59
 * Function：
 * Desc：自定义异常
 */
public class DefaultErrorException extends Exception {
    public final String code;

    public DefaultErrorException(String detailMessage) {
        this("404", detailMessage);
    }

    public DefaultErrorException(String error, String detailMessage) {
        super(detailMessage);
        this.code = error;
    }
}
