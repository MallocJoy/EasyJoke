package com.utouu.module.login.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.utouu.module.login.R;
import com.utouu.module.login.ui.fragment.Retrieve1Fragment;
import com.utouu.module.login.ui.fragment.Retrieve2Fragment;
import com.utouu.module.login.ui.fragment.Retrieve3Fragment;

public class RetrieveActivity extends BaseActivity implements Retrieve1Fragment.OnFragmentInteractionListener,
        Retrieve2Fragment.OnFragmentInteractionListener, Retrieve3Fragment.OnFragmentInteractionListener {

    private String deviceType;

    public static void startForResult(Activity context, String deviceType, int requestCode) {
        Intent intent = new Intent(context, RetrieveActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(BaseLoginActivity.ARG_DEVICE_TYPE, deviceType);
        intent.putExtras(bundle);
        context.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve);

        setTitleBar(getString(R.string.commons_title_retrieve));

        if (getIntent() != null) {
            deviceType = getIntent().getStringExtra(BaseLoginActivity.ARG_DEVICE_TYPE);
        }

        Retrieve1Fragment retrieve1Fragment = Retrieve1Fragment.newInstance(deviceType);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frameLayout, retrieve1Fragment).commit();
    }

    /**
     * 找回密码输入验证码页码
     * @param mobile
     */
    @Override
    public void doNextPage1(String mobile) {
        Retrieve2Fragment retrieve2Fragment = Retrieve2Fragment.newInstance(mobile);//.addToBackStack(retrieve2Fragment.getText())
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frameLayout, retrieve2Fragment).commit();
    }

    /**
     * 跳转到找回密码输入新密码页
     * @param mobile
     * @param identKey
     */
    @Override
    public void doNextPage2(String mobile, String identKey) {
        Retrieve3Fragment retrieve3Fragment = Retrieve3Fragment
                .newInstance(mobile, identKey);//.addToBackStack(retrieve3Fragment.getText())
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frameLayout, retrieve3Fragment).commit();

    }

    /**
     * 新密码设置成功回调
     * @param mobile
     */
    @Override
    public void closePage(String mobile) {
        getSupportFragmentManager().popBackStackImmediate(Retrieve2Fragment.class.getName(), 1);
        Intent data = new Intent();
        data.putExtra("mobile", mobile);
        setResult(RESULT_OK, data);
        finish();
    }
}
