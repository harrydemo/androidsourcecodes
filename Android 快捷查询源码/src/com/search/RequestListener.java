package com.search;

/**
 * 自定义一个监听器接口
 * 以后所有监听器类都要实现该接口
 * @author Administrator
 *
 */
public interface RequestListener {
	
	//成功结束时，触发
	public void onComplete(String result); 
	
	//出现异常时触发
	public void onException(Exception e);

}
