/**
 * 
 */
package com.xcontacts.utils;

import android.content.ContentValues;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

/**
 * 所有的EditText都应该添加这个监听器,以监听EditText中内容变化
 * 
 * @author Lefter
 */
public class MyTextWachter implements TextWatcher {
	/**
	 * content代表的是EditText中的字符串修改的是哪一个字段。如:Phone.NUMBER
	 */
	private String itemContent;
	/**
	 * 用于存放用户写入的内容,用于数据库的插入和更新
	 */
	private ContentValues mContentValues;

	public MyTextWachter(String itemContent, ContentValues values) {
		this.itemContent = itemContent;
		mContentValues = values;
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

	@Override
	public void afterTextChanged(Editable s) {
		String content = s.toString();
		if (!TextUtils.isEmpty(content)) {
			mContentValues.put(itemContent, content);
			MyLog.d("	content:" + content);
		}
	}
}