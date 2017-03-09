package com.utouu.module.login.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.utouu.module.login.R;

/**
 * Created by yang on 16/2/25.
 */
public class BaseDialog extends Dialog {

    public BaseDialog(Context context) {
        super(context);
    }

    public BaseDialog(Context context, int theme) {
        super(context, theme);
    }

    protected BaseDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCanceledOnTouchOutside(false);
    }

    public static class Builder {

        private Context context;
        private CharSequence title;
        private CharSequence message;
        private String positiveButtonText;
        private String negativeButtonText;
        private float mAdd = 0;
        private float mMult = 0;
        private View.OnClickListener mContentOnClickListener;
        private View contentView;
        private OnClickListener positiveButtonClickListener;
        private OnClickListener negativeButtonClickListener;
        private boolean mCancelable = true;
        private boolean mCanceledOnTouchOutside;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setMessage(CharSequence message) {
            this.message = message;
            return this;
        }

        /**
         * Set the Dialog message from resource
         *
         * @param message
         * @return
         */
        public Builder setMessage(int message) {
            this.message = context.getText(message);
            return this;
        }

        /**
         * Set the Dialog title from resource
         *
         * @param title
         * @return
         */
        public Builder setTitle(int title) {
            this.title = context.getText(title);
            return this;
        }

        /**
         * Set the Dialog title from String
         *
         * @param title
         * @return
         */

        public Builder setTitle(CharSequence title) {
            this.title = title;
            return this;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }


        /**
         * Sets whether the dialog is cancelable or not.  Default is true.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setCancelable(boolean cancelable) {
            this.mCancelable = cancelable;
            return this;
        }

        public Builder setLineSpacing(float add, float mult) {
            this.mAdd = add;
            this.mMult = mult;
            return this;
        }

        public Builder setContentOnClickListener(View.OnClickListener contentOnClickListener) {
            this.mContentOnClickListener = contentOnClickListener;
            return this;
        }

        public Builder setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
            this.mCanceledOnTouchOutside = canceledOnTouchOutside;
            return this;
        }

        /**
         * Set the positive button resource and it's listener
         *
         * @param positiveButtonText
         * @return
         */
        public Builder setPositiveButton(int positiveButtonText, OnClickListener listener) {
            this.positiveButtonText = (String)context.getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText, OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonText, OnClickListener listener) {
            this.negativeButtonText = (String)context.getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText, OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public BaseDialog create() {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final BaseDialog baseDialog = new BaseDialog(context, R.style.commons_baseDialog1);
            View layout = inflater.inflate(R.layout.base_dialog, null);
            baseDialog.addContentView(layout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            // set the dialog title
            if (!TextUtils.isEmpty(title)) {
                ((TextView)layout.findViewById(R.id.titleTextView)).setText(title);
            }
            // set the confirm button
            if (positiveButtonText != null) {
                ((Button)layout.findViewById(R.id.positiveButton)).setText(positiveButtonText);
                ((Button)layout.findViewById(R.id.positiveButton)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (positiveButtonClickListener != null) {
                            positiveButtonClickListener.onClick(baseDialog, DialogInterface.BUTTON_POSITIVE);
                        } else {
                            if (baseDialog.isShowing()) {
                                baseDialog.dismiss();
                            }
                        }
                    }
                });
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.positiveButton).setVisibility(View.GONE);
            }
            // set the cancel button
            if (negativeButtonText != null) {
                ((Button)layout.findViewById(R.id.negativeButton)).setText(negativeButtonText);

                ((Button)layout.findViewById(R.id.negativeButton)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (negativeButtonClickListener != null) {
                            negativeButtonClickListener.onClick(baseDialog, DialogInterface.BUTTON_NEGATIVE);
                        } else {
                            if (baseDialog.isShowing()) {
                                baseDialog.dismiss();
                            }
                        }
                    }
                });

            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.negativeButton).setVisibility(View.GONE);
            }
            // set the content message
            if (message != null) {
                TextView textView = (TextView)layout.findViewById(R.id.contentTextView);
                if (null != textView) {
                    textView.setText(message);
                    if (mAdd != 0 || mMult != 0) {
                        textView.setLineSpacing(mAdd, mMult);
                    }
                    if (null != mContentOnClickListener) {
                        textView.setOnClickListener(mContentOnClickListener);
                    }
                }
            } else if (contentView != null) {
                // if no message set
                // add the contentView to the dialog body
                LinearLayout contentLinearLayout = (LinearLayout)layout.findViewById(R.id.contentLinearLayout);
                contentLinearLayout.removeAllViews();
                contentLinearLayout.addView(contentView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT));
            }
            baseDialog.setContentView(layout);
            baseDialog.setCancelable(mCancelable);
            baseDialog.setCanceledOnTouchOutside(mCanceledOnTouchOutside);
            return baseDialog;
        }

        public BaseDialog show() {
            BaseDialog dialog = create();
            dialog.show();
            return dialog;
        }
    }
}
