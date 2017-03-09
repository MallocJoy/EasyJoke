package com.utouu.easyjoke.widgets;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.marno.easyutilcode.StackUtil;

import cn.utsoft.xunions.R;
import cn.utsoft.xunions.util.AppUtil;

/**
 * Created by cj on 2017/2/9.
 * Function:
 * Desc:
 */

public class TgtInvalidDialog extends AlertDialog.Builder {

    public TgtInvalidDialog(Context context) {
        super(context, R.style.invalidDialogTheme);
        setTitle("身份令牌过期");
        setCancelable(false);

        setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                click();
            }
        });
    }

    // TODO: 2017/2/9 点击确定之后，跳转到那个界面
    private void click() {
        AppUtil.startLoginActivity(StackUtil.getIns().current());
    }
}
