package com.utouu.easyjoke.util;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Du_Li on 2017/3/1 17:09.
 * Function: 过滤表情 特殊字符
 * Desc:
 */

public class EditTextFilter implements InputFilter {
	
	public static Pattern mEmojiPattern = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]", Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
	
	@Override
	public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
		Matcher emojiMatcher = mEmojiPattern.matcher(source);
		if (emojiMatcher.find() || RegularUtils.isContainF(source.toString())) {
			return "";
		}
		return source;
	}
}