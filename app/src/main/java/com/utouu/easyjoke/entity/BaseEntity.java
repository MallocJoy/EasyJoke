package com.utouu.easyjoke.entity;

/**
 * Create by 黄思程 on 2017/3/13  13:44
 * Function：
 * Desc：实体类的基类,所有实体类都继承于该类
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
