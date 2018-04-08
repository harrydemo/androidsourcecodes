package com.renren.api.connect.android.view;

import android.os.Bundle;

import com.renren.api.connect.android.exception.RenrenError;

/**
 * 改接口的方法在非UI线程中调用，请不要在该接口的方法中修改UI。
 * 
 * @author 李勇(yong.li@opi-corp.com) 2010-7-19
 */
public interface ConnectButtonListener {

	/**
	 * 登录成功后调用
	 * 
	 * @param values
	 */
	public void onLogined(Bundle values);

	/**
	 * 登出成功后调用
	 * 
	 * @param values
	 */
	public void onLogouted(Bundle values);

	/**
	 * 服务器返回错误内容时调用
	 * 
	 * @param renrenError
	 */
	public void onRenrenError(RenrenError renrenError);

	/**
	 * 出现严重错误时调用
	 * 
	 * @param error
	 */
	public void onException(Exception error);
}
