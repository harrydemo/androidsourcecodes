package com.androidmediaplayer.constant;

public interface AppConstant {

	public class PlayerMsg {
		// 已添加到下载列表
		public static final int ADDTODOWNLOADLIST_MSG = 6;
		// 文件已经存在
		public static final int EXIST_MSG = 7;
		// 文件下载线程数量
		public static final int THREADNUM = 1;
		// 重新播放
		public static final int AGAINLOAD_MSG = 1;
		// 继续播放
		public static final int KEEPGOING_MSG = 0;
		// 播放完毕
		public static final int COMPLETE = 3;
		// 播放错误
		public static final int MEDIAPLAYERERROR = 4;
	}

	public class PlayMode {
		public static final int MODE_NOAMAL = 5;
		public static final int MODE_LIST_REPEAT = 8;
		public static final int MODE_SINGLE_REPEAT = 9;
		public static final int MODE_SHUFFLE = 10;
	}

	public class URL {
		// public static final String BASE_URL =
		// "http://116.76.74.139:8080/mp3server/";
		public static final String BASE_URL = "http://116.76.74.139:8080/mp3server/";
	}

	public class Action {
		public static final String LRC_MESSAGE_ACTION = "com.androidmediaplayer.lrcmessage.action";
		public static final String DOWNLOAD_MESSAGE_ACTION = "com.androidmediaplayer.downloadmessage.action";
		public static final String TEL_ACTION = "android.permission.READ_PHONE_STATE";
		public static final String MEDIAPLAYERSERVICE_MESSAGE_ACTION = "com.androidmediaplayer.mediaplayerservice";
	}

}
