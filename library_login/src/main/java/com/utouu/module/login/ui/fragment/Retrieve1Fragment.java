package com.utouu.module.login.ui.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.utouu.android.commons.constants.HttpURLConstant;
import com.utouu.android.commons.http.BaseCallback;
import com.utouu.android.commons.http.UtouuHttpUtils;
import com.utouu.android.commons.ui.fragment.BaseFragment;
import com.utouu.android.commons.utils.CheckParamsUtils;
import com.utouu.android.commons.utils.StringUtils;
import com.utouu.android.commons.utils.ToastUtils;
import com.utouu.module.login.R;
import com.utouu.module.login.presenter.VerifyPresenter;
import com.utouu.module.login.presenter.view.IVerifyView;
import com.utouu.module.login.widget.BaseTextWatcher;

import java.util.HashMap;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Retrieve1Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Retrieve1Fragment extends BaseFragment implements IVerifyView {

    private static final String ARG_SOURCE = "source";

    private OnFragmentInteractionListener mListener;

    private Button nextButton;
    private EditText mobileEditText;
    private EditText verifyEditText;
    private ImageView verifyImageView;

    private String verifyKey;
    private String source;

    private VerifyPresenter verifyPresenter;

    public Retrieve1Fragment() {
    }

    public static Retrieve1Fragment newInstance(String source) {
        Retrieve1Fragment fragment = new Retrieve1Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_SOURCE, source);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        verifyPresenter = new VerifyPresenter(this);

        if (getArguments() != null) {
            source = getArguments().getString(ARG_SOURCE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_retrieve1, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        mobileEditText = (EditText)view.findViewById(R.id.mobile_editText);
        mobileEditText.addTextChangedListener(new BaseTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                super.onTextChanged(charSequence, start, before, count);
                if (nextButton != null) {
                    nextButton.setEnabled(StringUtils.isNotBlank(charSequence));
                }
            }
        });

        verifyEditText = (EditText)getView().findViewById(R.id.verify_editText);
        verifyImageView = (ImageView)view.findViewById(R.id.verify_imageView);
        verifyImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageVify();
            }
        });

        nextButton = (Button)view.findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doNext();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mobileEditText != null) {
            mobileEditText.setText("");
        }
        if (verifyEditText != null) {
            verifyEditText.setText("");
        }

        getImageVify();
    }

    /**
     * 下一步
     */
    private void doNext() {
        //判断手机号是否合法
        final String mobile = mobileEditText.getText().toString().trim();
        if (StringUtils.isBlank(mobile)) {
            ToastUtils.showShortToast(getActivity(), getString(R.string.commons_account_validate3));
            return;
        }
        if (!CheckParamsUtils.checkMobile(mobile)) {
            ToastUtils.showShortToast(getActivity(), getString(R.string.commons_account_validate2));
            return;
        }

        // 判断验证码是否填写
        final String verifyValue = verifyEditText.getText().toString().trim();
        if (StringUtils.isBlank(verifyValue)) {
            ToastUtils.showShortToast(getActivity(), getString(R.string.commons_verify_validate));
            return;
        }

        showProgress();
        sendSMS(mobile, verifyKey, verifyValue);
    }

    /**
     * 发送短信验证码
     *
     * @param mobile
     * @param verifyKey
     * @param verifyValue
     */
    private void sendSMS(final String mobile, final String verifyKey, final String verifyValue) {
        if (getActivity() != null) {

            nextButton.setEnabled(false);

            HashMap<String, String> params = new HashMap<>();
            params.put("username", mobile);
            params.put("imgVCodeKey", verifyKey);
            params.put("imgVCode", verifyValue);

            UtouuHttpUtils.operation(getActivity(), HttpURLConstant.baseHttpURL.getForgetSendSMS(), params, new BaseCallback() {
                @Override
                public void success(String s) {
                    dismissProgress();
                    if (getActivity() != null && nextButton != null) {
                        nextButton.setEnabled(true);
                        if (mListener != null) {
                            ToastUtils.showLongToast(getActivity(),getString(R.string.commons_sms_send_two));
                            mListener.doNextPage1(mobile);
                        }
                    }
                }

                @Override
                public void failure(String s) {
                    dismissProgress();
                    if (getActivity() != null && nextButton != null) {
                        nextButton.setEnabled(true);
                        ToastUtils.showLongToast(getActivity(), Html.fromHtml(s).toString());
                    }
                }

                @Override
                public void failure(String s, String s1) {
                    failure(s1);
                }
            });
        }
    }

    /**
     * 获取图片验证码
     */
    public void getImageVify() {
        if (verifyImageView != null && getActivity() != null) {
            verifyImageView.setEnabled(false);

            verifyKey = UUID.randomUUID().toString();
            verifyPresenter.getImageVify(getActivity(), source, verifyKey);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener)context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public String getText() {
        return "cn.utcard.fragment.Retrieve1Fragment";
    }

    @Override
    public void verifySuccess(Bitmap bitmap) {
        if (verifyImageView != null) {
            verifyImageView.setEnabled(true);
            if (bitmap != null) {
                verifyImageView.setImageBitmap(bitmap);
            }
        }
    }

    @Override
    public void verifyFailure(String message) {
        if (verifyImageView != null) {
            verifyImageView.setEnabled(true);
        }
        if (getActivity() != null) {
            ToastUtils.showLongToast(getActivity(), message);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {

        void doNextPage1(String mobile);
    }
}
