package com.utouu.module.login.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
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
import com.utouu.module.login.widget.BaseTextWatcher;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Retrieve3Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Retrieve3Fragment extends BaseFragment {

    private static final String ARG_MOBILE = "mobile";
    private static final String ARG_IDENTKEY = "identKey";

    private String mobile;
    private String identKey;

    private OnFragmentInteractionListener mListener;

    private Button retrieveButton;
    private ImageView showPasswordImageView;
    private EditText passwordEditText;
    private EditText passwordAgainEditText;

    public Retrieve3Fragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param mobile   Parameter 1.
     * @param identKey Parameter 2.
     * @return A new instance of fragment Retrieve3Fragment.
     */
    public static Retrieve3Fragment newInstance(String mobile, String identKey) {
        Retrieve3Fragment fragment = new Retrieve3Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_MOBILE, mobile);
        args.putString(ARG_IDENTKEY, identKey);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mobile = getArguments().getString(ARG_MOBILE);
            identKey = getArguments().getString(ARG_IDENTKEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_retrieve3, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        showPasswordImageView = (ImageView)view.findViewById(R.id.show_password_imageView);
        retrieveButton = (Button)view.findViewById(R.id.retrieve_submit_button);
        passwordEditText = (EditText)view.findViewById(R.id.password_editText);
        passwordAgainEditText = (EditText)view.findViewById(R.id.password_again_editText);

        showPasswordImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int index = passwordEditText.getSelectionEnd();
                TransformationMethod transformationMethod = passwordEditText.getTransformationMethod();
                if (transformationMethod instanceof PasswordTransformationMethod) {
                    showPasswordImageView.setImageResource(R.mipmap.icon_show_pass_checked);
                    passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else if (transformationMethod instanceof HideReturnsTransformationMethod) {
                    showPasswordImageView.setImageResource(R.mipmap.icon_show_pass_normal);
                    passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                passwordEditText.setSelection(index);
            }
        });

        passwordEditText.addTextChangedListener(new BaseTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                super.onTextChanged(charSequence, start, before, count);
                if (retrieveButton != null) {
                    retrieveButton.setEnabled(StringUtils.isNotBlank(charSequence));
                }
            }
        });

        passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        TransformationMethod transformationMethod = passwordEditText.getTransformationMethod();
        if (transformationMethod instanceof PasswordTransformationMethod) {
            showPasswordImageView.setImageResource(R.mipmap.icon_show_pass_normal);
        } else if (transformationMethod instanceof HideReturnsTransformationMethod) {
            showPasswordImageView.setImageResource(R.mipmap.icon_show_pass_checked);
        }

        retrieveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doRetrieve();
            }
        });
    }

    /**
     * 找回密码
     */
    private void doRetrieve() {
        final String password = passwordEditText.getText().toString().trim();
        if (StringUtils.isBlank(password)) {
            ToastUtils.showShortToast(getActivity(), getString(R.string.commons_password_validate1));
            return;
        }
        if (!CheckParamsUtils.checkPass(password)) {
            ToastUtils.showShortToast(getActivity(), getString(R.string.commons_password_validate2));
            return;
        }

        final String passwordAgain = passwordAgainEditText.getText().toString().trim();
        if (StringUtils.isBlank(passwordAgain)) {
            ToastUtils.showShortToast(getActivity(), getString(R.string.commons_password_validate3));
            return;
        }

        if (!StringUtils.equals(password, passwordAgain)) {
            ToastUtils.showShortToast(getActivity(), getString(R.string.commons_password_validate4));
            return;
        }


        sendRetrieve(password);
    }

    /**
     * 提交新密码
     *
     * @param password
     */
    private void sendRetrieve(String password) {
        if (getActivity() != null) {

            showProgress();

            HashMap<String, String> params = new HashMap<>();
            params.put("mobile", mobile);
            params.put("idenKey", identKey);
            params.put("password", password);

            UtouuHttpUtils.operation(getActivity(), HttpURLConstant.baseHttpURL.getForgetReset(), params, new BaseCallback() {
                @Override
                public void success(String s) {
                    dismissProgress();

                    ToastUtils.showLongToast(getActivity(), "密码找回成功！");
                    if (mListener != null) {
                        mListener.closePage(mobile);
                    }
                }

                @Override
                public void failure(String s) {
                    dismissProgress();
                    if (getActivity() != null) {
                        ToastUtils.showLongToast(getActivity(), s);
                    }
                }

                @Override
                public void failure(String s, String s1) {
                    failure(s1);
                }
            });
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener)context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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

        void closePage(String mobile);
    }
}
