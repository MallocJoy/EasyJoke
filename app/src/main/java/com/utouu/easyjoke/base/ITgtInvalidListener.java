package com.utouu.easyjoke.base;

/**
 * Created by cj on 2017/2/9.
 * Function:
 * Desc:
 */

public interface ITgtInvalidListener extends IBaseStatusView {

    /**
     * 令牌失效的回调
     * @param msg
     */
    void tgtInvalid(String msg);
}
