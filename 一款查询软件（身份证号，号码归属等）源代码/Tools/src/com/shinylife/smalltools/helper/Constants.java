package com.shinylife.smalltools.helper;

/**
 * ���þ�̬�������
 * 
 * @author Leder
 * 
 */
public class Constants {
	/**
	 * �汾��
	 */
	public final static String VERSION = "1.0.0.0";

	/**
	 * �Ƿ��ǲ��Կ���
	 */
	public static final boolean DEBUG = false;
	/**
	 * HTTP�������
	 */
	/**
	 * �����������
	 */
	public static final int HTTP_REQUEST_RETRY_COUNT = 1;
	/**
	 * ��������ʱ����
	 */
	public static final int HTTP_REQUEST_RETRY_INTERVAL_SECONDS = 2;
	/**
	 * ���ӳ�ʱʱ��
	 */
	public static final int HTTP_REQUEST_CONNECTION_TIMEOUT = 60;
	/**
	 * ��ȡ��Ӧ��ʱʱ��
	 */
	public static final int HTTP_REQUEST_READ_TIMEOUT = 60;
	/**
	 * �ӿ������ַ
	 */
	public static final String API_URL = "http://shinylife.net/api/BaiBaoXianApi.ashx?t=%s&q=%s";
	/**
	 * ����°汾
	 */
	public static final String CHECK_VERSION_API_URL = "http://shinylife.net/api/CheckVersion.ashx?m=android&app=com.shinylife.smalltools&v=%s";
}
