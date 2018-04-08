package com.android.tools;

/*
 * Copyright (C) 2011 Androd源码工作室
 * 
 * Android实战教程--网络视频类播发器
 * 
 * taobao : http://androidsource.taobao.com
 * mail : androidSource@139.com
 * QQ:    androidSource@139.com
 * 
 */
public class Configer
{

	//键数组
	public static  String KeyArray[] = null;
	//value数组
	public static  String ValueArray[] = null;
	
	
	public static final String ACTIVITY_TITLE = "TITLE";
	public static final int ActivityRequestCode_One = 1;
	public static final int ActivityResponseCode_Three = 3;
	public static final String BAND_WIDTH = "band_width";
	public static final int COMMAND_0 = 0;
	public static final int COMMAND_1 = 1;
	public static final String DownLoadNewAPK = "http://m.vfun.tv:8080/vfun/sph.apk";
	public static final String FROME_PAGE = "FROME_PAGE";
	public static final String PREF = "faplayer_prefer";
	public static final String REQUEST_URL = "REQUEST_URL";
	public static final String RequestUrlSH = "http://m.vfun.tv:8080/webparser/get_live.php?vid=";
	public static final String RequestUrlYC = "http://m.vfun.tv:8080/webparser/";
	public static final String RequestVersion = "http://m.vfun.tv:8080/android_version.php";
	public static final String SOCKET_ERROR = "socket_error";
	public static final String StarchParame = "searchhot.php";
	public static final String TAB_PUSH_BTN = "TAB_PUSH_BTN";
	public static final String TodayNewest = "newest.php";
	public static final String VERSION_1 = "1.0.1";
	public static final String WEIBO = "WEIBO";
	public static final String WEIBO_SHARE_URL = "WEIBO_SHARE_URL";
	public static final String WEIBO_TOKEN = "WEIBO_TOKEN";
	public static final String WEIBO_TOKENSECRET = "WEIBO_TOKENSECRET";
	public static final String parameter1 = "_category.php?c=";
	public static final String parameter2 = "&index=";
	public static final String parameter3 = "so_";
	public static final String parameter4 = ".php?title=";
	public static final String parameter5 = "&o=1";

	public Configer()
	{
	}

	//键值数据数组初始化
	static {
		String as[] = new String[31];
		as[0] = "原创";
		as[1] = "体育";
		as[2] = "音乐";
		as[3] = "动漫";
		as[4] = "搞笑";
		as[5] = "电影";
		as[6] = "电视";
		as[7] = "汽车";
		as[8] = "教育";
		as[9] = "资讯";
		as[10] = "娱乐";
		as[11] = "科技";
		as[12] = "游戏";
		as[13] = "生活";
		as[14] = "时尚";
		as[15] = "旅游";
		as[16] = "母婴";
		as[17] = "广告";
		as[18] = "宠物";
		as[19] = "房产";
		as[20] = "拍客";
		as[21] = "军事";
		as[22] = "女性";
		as[23] = "记录";
		as[24] = "美食";
		as[25] = "财经";
		as[26] = "猎奇";
		as[27] = "数码";
		as[28] = "家庭";
		as[29] = "社会";
		as[30] = "综艺";
		KeyArray = as;
		
		String as1[] = new String[31];
		as1[0] = "dv";
		as1[1] = "sports";
		as1[2] = "music";
		as1[3] = "cartoon";
		as1[4] = "joke";
		as1[5] = "movie";
		as1[6] = "tv";
		as1[7] = "car";
		as1[8] = "edu";
		as1[9] = "news";
		as1[10] = "entertainment";
		as1[11] = "tech";
		as1[12] = "game";
		as1[13] = "life";
		as1[14] = "fashion";
		as1[15] = "tour";
		as1[16] = "baby";
		as1[17] = "advertise";
		as1[18] = "pet";
		as1[19] = "house";
		as1[20] = "paike";
		as1[21] = "mil";
		as1[22] = "girl";
		as1[23] = "record";
		as1[24] = "food";
		as1[25] = "finance";
		as1[26] = "lieqi";
		as1[27] = "digi";
		as1[28] = "family";
		as1[29] = "soc";
		as1[30] = "art";
		ValueArray = as1;
	}
}
