package com.shinylife.smalltools.helper;

/**
 * 公用静态变量存放
 * 
 * @author Leder
 * 
 */
public class Constants {
	/**
	 * 版本号
	 */
	public final static String VERSION = "1.0.0.0";

	/**
	 * 是否是测试开关
	 */
	public static final boolean DEBUG = false;
	/**
	 * HTTP请求参数
	 */
	/**
	 * 尝试请求次数
	 */
	public static final int HTTP_REQUEST_RETRY_COUNT = 1;
	/**
	 * 尝试请求时间间隔
	 */
	public static final int HTTP_REQUEST_RETRY_INTERVAL_SECONDS = 2;
	/**
	 * 链接超时时间
	 */
	public static final int HTTP_REQUEST_CONNECTION_TIMEOUT = 60;
	/**
	 * 读取响应超时时间
	 */
	public static final int HTTP_REQUEST_READ_TIMEOUT = 60;
	/**
	 * 接口请求地址
	 */
	public static final String API_URL = "http://shinylife.net/api/BaiBaoXianApi.ashx?t=%s&q=%s";
	/**
	 * 检测新版本
	 */
	public static final String CHECK_VERSION_API_URL = "http://shinylife.net/api/CheckVersion.ashx?m=android&app=com.shinylife.smalltools&v=%s";
}
