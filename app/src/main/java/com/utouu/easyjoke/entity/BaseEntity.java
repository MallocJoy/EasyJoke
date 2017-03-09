package com.utouu.easyjoke.entity;

/**
 * Created by Marno on 2016/8/29/09:50.
 */
public class BaseEntity<T> {

    public T data;
    public String code;
    public String msg;
    public boolean success;

    @Override
    public String toString() {
        return "BaseEntity{" +
                "data=" + data +
                ", code=" + code +
                ", msg='" + msg + '\'' +
                ", success=" + success +
                '}';
    }
}
