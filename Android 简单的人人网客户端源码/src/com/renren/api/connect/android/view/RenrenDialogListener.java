package com.renren.api.connect.android.view;

import android.os.Bundle;

import com.renren.api.connect.android.exception.RenrenError;

/**
 * @author 李勇(yong.li@opi-corp.com) 2010-7-15
 */
public interface RenrenDialogListener {

	/**
	 * dialog请求的结果返回时调用
	 * 
	 * @param values
	 *            请求返回的结果Key-value
	 */
	public void onComplete(Bundle values);

	/**
	 * 服务器请求返回错误内容时调用（如参数不正确）
	 */
	public void onRenrenError(RenrenError renrenError);

	/**
	 * 当请求的url出现时调用（如服务器返回404,500等）
	 */
	public void onHttpError(int errorCode, String description, String failingUrl);

	/**
	 * 用户取消人人对话框时调用。
	 */
	public void onCancel(Bundle values);
}
