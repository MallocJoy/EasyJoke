package com.utouu.easyjoke.util;

import android.text.InputFilter;
import android.text.Spanned;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created: AriesHoo on 2017-01-09 11:38
 * Function: TextView过滤工具
 * Desc:
 */
public class InputFilterUtil {

    public interface OnInputFilterListener {
        void onInput(CharSequence input, boolean isCanInput);
    }

    private static volatile InputFilterUtil instance;

    private InputFilterUtil() {
    }

    public static InputFilterUtil getInstance() {
        if (instance == null) {
            synchronized (InputFilterUtil.class) {
                if (instance == null) {
                    instance = new InputFilterUtil();
                }
            }
        }
        return instance;
    }

    /**
     * 过滤TextView及其子类中表情
     *
     * @param textView
     * @return
     */
    public void setFilterCleanEmoji(TextView textView, OnInputFilterListener listener) {
        addFilter(textView, getCleanEmojiFilter(listener));
    }

    /**
     * 过滤TextView及其子类中所有空格
     *
     * @param textView
     */
    public void setFilterCleanSpace(TextView textView, OnInputFilterListener listener) {
        addFilter(textView, getSpaceFilter(listener));
    }

    /**
     * 限制textView最大输入字符
     *
     * @param textView
     * @param max
     * @param listener
     */
    public void setFilterTextMax(TextView textView, int max, OnInputFilterListener listener) {
        addFilter(textView, getTextMaxFilter(textView, max, listener));
    }


    /**
     * 限制EditText小数点后最大输入位数(EditText inputType 为 numberDecimal输入double类型)
     *
     * @param textView
     * @param max
     * @param listener
     */
    public void setFilterTextMaxAfterDot(EditText textView, int max, OnInputFilterListener listener) {
        addFilter(textView, getTextMaxAfterDotFilter(textView, max, listener));
    }

    /**
     * TextView增加Filter
     *
     * @param textView
     * @param filter
     */
    public void addFilter(TextView textView, InputFilter filter) {
        if (textView == null || filter == null) {
            return;
        }
        InputFilter[] filters = textView.getFilters();
        InputFilter[] filterNew;
        try {
            if (filters != null && filters.length > 0) {
                filterNew = new InputFilter[filters.length + 1];
                for (int i = 0; i < filters.length; i++) {
                    filterNew[i] = filters[i];
                }
                filterNew[filters.length] = filter;
            } else {
                filterNew = new InputFilter[]{filter};
            }
        } catch (Exception e) {
            filterNew = filters;
        }
        textView.setFilters(filterNew);
    }

    public InputFilter getSpaceFilter(final OnInputFilterListener listener) {
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals(" ")) {
                    if (listener != null) {
                        listener.onInput(source, false);
                    }
                    return "";
                }
                if (listener != null) {
                    listener.onInput(source, true);
                }
                return null;
            }
        };
        return filter;
    }

    public InputFilter getTextMaxFilter(final TextView textView, final int max, final OnInputFilterListener listener) {
        if (textView == null || max < 1) {
            return null;
        }
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                int lengthText = textView.getText().toString().length();
                int lengthSource = source.length();
                if (lengthText >= max) {
                    if (listener != null) {
                        listener.onInput(source, false);
                    }
                    return "";
                } else if (lengthSource + lengthText > max) {
                    if (listener != null) {
                        listener.onInput(source, false);
                    }
                    return source.subSequence(0, max - lengthText);
                }
                if (listener != null) {
                    listener.onInput(source, true);
                }
                return null;
            }
        };
        return filter;
    }

    public InputFilter getTextMaxAfterDotFilter(final TextView textView, final int max, final OnInputFilterListener listener) {
        if (textView == null || max < 1) {
            return null;
        }
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String textInput = source.toString();
                String textSpanned = textView.getText().toString();
                int lengthText = textSpanned.length();
                if (textInput.contains(".")) {//输入小数点
                    if (lengthText == 0 || dstart == 0) {//
                        if (listener != null) {
                            listener.onInput(source, true);
                        }
                        return "0.";
                    }
                } else if (dest.toString().contains(".")) {
                    int index = dest.toString().indexOf(".");
                    int mLength = dest.toString().substring(index).length() + (source.toString().isEmpty() ? -1 : 0);
                    if (mLength > max) {
                        if (listener != null) {
                            listener.onInput(source, false);
                        }
                        return "";
                    }
                }
                if (listener != null) {
                    listener.onInput(source, true);
                }
                return null;
            }
        };
        return filter;
    }

    /**
     * 获取
     *
     * @return
     */
    public InputFilter getCleanEmojiFilter(final OnInputFilterListener listener) {
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (isEmoji(source.toString())) {
                    if (listener != null) {
                        listener.onInput(source, false);
                    }
                    return "";
                }
                if (listener != null) {
                    listener.onInput(source, true);
                }
                //返回null表示接收输入的字符,返回空字符串表示不接受输入的字符
                return null;
            }
        };
        return filter;
    }

    /**
     * 判断是否emoji表情
     *
     * @param string
     * @return
     */
    public boolean isEmoji(String string) {
        Pattern p = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(string);
        return m.find();
    }
}
