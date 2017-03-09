package com.utouu.easyjoke.data.exception;

/**
 * Created by AriesHoo on 2016-09-12 13:55
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
