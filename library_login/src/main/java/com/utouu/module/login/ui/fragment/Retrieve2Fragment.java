package com.utouu.module.login.ui.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.utouu.android.commons.constants.HttpURLConstant;
import com.utouu.android.commons.http.BaseCallback;
import com.utouu.android.commons.http.UtouuHttpUtils;
import com.utouu.android.commons.ui.fragment.BaseFragment;
import com.utouu.android.commons.utils.GsonUtils;
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
 * Use the {@link Retrieve2Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Retrieve2Fragment extends BaseFragment {

    private static final String ARG_MOBILE = "mobile";

    private String mobile;

    private OnFragmentInteractionListener mListener;
    private TextView countdownTextView;
    private EditText mobileVerifyEditText;
    private Button nextButton;

    private int waitCount = 60;
    private CountDownTimer countDownTimer;

    public Retrieve2Fragment() {
    }

    public static Retrieve2Fragment newInstance(String mobile) {
        Retrieve2Fragment fragment = new Retrieve2Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_MOBILE, mobile);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mobile = getArguments().getString(ARG_MOBILE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_retrieve2, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        countdownTextView = (TextView)view.findViewById(R.id.countdown_textView);

        mobileVerifyEditText = (EditText)getView().findViewById(R.id.mobile_verify_editText);
        if (mobileVerifyEditText != null) {
            mobileVerifyEditText.addTextChangedListener(new BaseTextWatcher() {
                @Override
                public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                    super.onTextChanged(charSequence, start, before, count);
                    if (nextButton != null) {
                        nextButton.setEnabled(StringUtils.isNotBlank(charSequence));
                    }
                }
            });
        }

        nextButton = (Button)view.findViewById(R.id.next_button);
        if (nextButton != null) {
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doNext();
                }
            });
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        countDownTimer = new CountDownTimer(1000 * waitCount, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (null != countdownTextView) {
                    countdownTextView.setText(getString(R.string.commons_countdown_hint, (millisUntilFinished / 1000)));
                }
            }

            @Override
            public void onFinish() {
                if (null != countdownTextView) {
                    countdownTextView.setText(R.string.commons_sms_retry);
                    countdownTextView.setTextColor(getResources().getColor(R.color.commons_text_color2));
                    countdownTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            repeatSendSMS();
                        }
                    });
                }
            }
        };
        countDownTimer.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null != mobileVerifyEditText) {
            mobileVerifyEditText.setText("");
        }
    }

    /**
     * 下一步
     */
    public void doNext() {
        if (getActivity() != null) {
            final String smsContent = mobileVerifyEditText.getText().toString().trim();

            if (StringUtils.isBlank(smsContent)) {
                ToastUtils.showShortToast(getActivity(), getString(R.string.commons_sms_validate));
                return;
            }

            showProgress();

            HashMap<String, String> params = new HashMap<>();
            params.put("mobile", mobile);
            params.put("code", smsContent);

            UtouuHttpUtils.loadData(getActivity(), HttpURLConstant.baseHttpURL.getCheckSMS(), params, new BaseCallback() {
                @Override
                public void success(String s) {
                    dismissProgress();
                    if (mListener != null) {
                        mListener.doNextPage2(mobile, GsonUtils.getGson().fromJson(s, String.class));
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

    /**
     * 重新发送验证码
     */
    private void repeatSendSMS() {
        if (getActivity() == null) {
            return;
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("username", mobile);

        UtouuHttpUtils.operation(getActivity(), HttpURLConstant.baseHttpURL.getForgetSendSMSRetry(), params, new BaseCallback() {
            @Override
            public void success(String message) {
                if (getActivity() != null && !getActivity().isFinishing()) {
                    if (countdownTextView != null) {
                        countdownTextView.setOnClickListener(null);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            countdownTextView.setTextColor(getActivity().getColor(R.color.commons_text_hint_color));
                        } else {
                            countdownTextView.setTextColor(getResources().getColor(R.color.commons_text_hint_color));
                        }
                        countDownTimer.start();
                    }
                    ToastUtils.showShortToast(getActivity(), message);
                }
            }

            @Override
            public void failure(String message) {
                if (getActivity() != null) {
                    ToastUtils.showLongToast(getActivity(), message);
                }
            }

            @Override
            public void failure(String statusCode, String message) {
                failure(message);
            }
        });
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

        void doNextPage2(String mobile, String identKey);
    }
}
