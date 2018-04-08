package com.mzba.peng.util;

import java.lang.ref.WeakReference;

import android.content.Context;

/**
 * 全局类
 * 
 * @author 06peng
 * 
 */
public class AppContext {

	public static boolean FIRTH_INIT = false;
	private int OAUTH_TYPE;
	
	public static final int oauth_bind = 10011;
	
	public static boolean auto_locate = true;

	public int getOAUTH_TYPE() {
		return OAUTH_TYPE;
	}

	public void setOAUTH_TYPE(int oAUTH_TYPE) {
		OAUTH_TYPE = oAUTH_TYPE;
	}

	private static AppContext instance = new AppContext();

	public static AppContext getInstance() {
		if (instance == null) {
			instance = new AppContext();
		}
		return instance;
	}

	public static final String UNAVAILABLE = "unavailable";
	public static final String AVAILABLE = "available";

	/**
	 * 获取 系统上下文
	 */
	private WeakReference<Context> context;

	/**
	 * 获取 系统上下文
	 * 
	 * @return
	 */
	public static Context getContext() {
		if (getInstance().context == null) {
			return null;
		}
		return getInstance().context.get();
	}

	/**
	 * 设置 系统上下文
	 * 
	 * @return
	 */
	public static void setContext(Context contextx) {
		getInstance().context = null;
		getInstance().context = new WeakReference<Context>(contextx);
	}

	public static int language;

	public static void setLanguage(int language) {
		AppContext.language = language;
	}
	
	public static final String SD_PATH = "mobibottle";
	/**用户头像图片路径*/
	public static final String USERHEADICON = SD_PATH + "/userhead/icons";
	/**瓶子类型相关图片路径*/
	public static final String BottleTypeIcon = SD_PATH + "/bottle_type/icons";
	
	/**捞网类型相关图片路径*/
	public static final String NetTypeIcon = SD_PATH + "/nettype/icons";
	
	/**Bottle Timeline相关图片路径*/
	public static final String BottleTimelineIcon = SD_PATH + "/bttimeline/icons";
	
	/**查看瓶子回复图片路径*/
	public static final String BottleFeedlistIcon = SD_PATH + "/btfeedlists";
	
	/**Timeline big图片路径*/
	public static final String BottleBigPic = SD_PATH + "/bttimeline";
	
	/**Exchange Timeline相关图片路径*/
	public static final String ExchangeTimelineIcon = SD_PATH + "/extimeline";
	
	/**BTFriend Timeline相关图片路径*/
	public static final String BtFriendTimelineIcon = SD_PATH + "/btfriendtimeline";
	
	/**用户头像缓存*/
	public static final String UserAvatarIcon = SD_PATH + "/user_avatar";
	
	/**相册路径*/
	public static final String AlbumIcon = SD_PATH + "/albums";
	/**好友动态路径*/
	public static final String FriendFeedIcon = SD_PATH + "/friendicon";
	
	
	private int login_uid;
	private String login_token;
	private String deviceId;
	private int currentItem;

	private boolean isFromExchangeTime;
	private boolean isFromBottleTime;
	private boolean isFromBtFriend;
	
	
	public int getLogin_uid() {
		return login_uid;
	}

	public void setLogin_uid(int login_uid) {
		this.login_uid = login_uid;
	}

	public String getLogin_token() {
		return login_token;
	}

	public void setLogin_token(String login_token) {
		this.login_token = login_token;
	}
	
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public int getCurrentItem() {
		return currentItem;
	}

	public void setCurrentItem(int currentItem) {
		this.currentItem = currentItem;
	}

	/**跳转到“选择用户”界面的类型*/
	/**从“扔瓶子”跳转*/
	public static final int CHOOSE_FRIEND_THROWBOTTLE_TYPE = 1;
	/**从“交换资料”跳转*/
	public static final int CHOOSE_FRIEND_EXCHANGE_INFO_TYPE = 2;
	/**从“交换照片”跳转*/
	public static final int CHOOSE_FRIEND_EXCHANGE_PHOTO_TYPE = 3;
	/**从“转发”跳转*/
	public static final int CHOOSE_FRIEND_FORWARD_TYPE = 4;
	
	public static final String SEND_BT = "send_bt";  //扔瓶子
	public static final String REPLY_BT = "reply_bt"; //回复瓶子
	public static final String COPYMANUAL_BT = "copymanual_bt"; //手动传递瓶子
	public static final String TRANSMIT_BT = "transmit_bt"; //转载瓶子
	public static final String REPLY_INFOEX = "reply_infoex"; //回应资料交换 包含了：回应资料交换、发起资料交换两个功能。
	public static final String APPLY_PICEX = "apply_picex"; //发起照片交换
	public static final String REPLY_PICEX = "reply_picex"; //回应照片交换 包含了回应照片交换、接受照片交换两个功能。
	public static final String REPLYPIC_PICEX = "replypic_picex"; //回应照片交换中的照片
	public static final String FORWARPIC_PICEX = "forwardpic_picex"; //转发照片交换中的照片
	public static final String REPLY_PHOTO = "reply_photo"; //评论相册照片
	public static final String REPLY_COMMENT = "reply_comment"; //回复评论
	public static final String APPLY_FRIEND = "apply_friend"; //申请加好友
	public static final String SEND_MESSAGE = "send_message"; //发表私信
	public static final String ADD_ALBUM = "add_album"; //添加相册
	public static final String EDIT_ALBUM = "edit_album"; //编辑相册
	public static final String UPLOAD_PHOTO = "upload_photo"; //上传照片
	public static final String EDIT_PHOTO = "edit_photo"; //编辑相册
	public static final String EDIT_MEMO = "edit_memo"; //编辑用户备注
	public static final String SET_DOING = "set_doing"; //设置心情

	public void setFromBottleTime(boolean isFromBottleTime) {
		this.isFromBottleTime = isFromBottleTime;
	}

	public boolean isFromBottleTime() {
		return isFromBottleTime;
	}

	public void setFromExchangeTime(boolean isFromExchangeTime) {
		this.isFromExchangeTime = isFromExchangeTime;
	}

	public boolean isFromExchangeTime() {
		return isFromExchangeTime;
	}

	public void setFromBtFriend(boolean isFromBtFriend) {
		this.isFromBtFriend = isFromBtFriend;
	}

	public boolean isFromBtFriend() {
		return isFromBtFriend;
	}
	
	public void setFromExchangePicDetail(boolean fromExchangePicDetail) {
		this.fromExchangePicDetail = fromExchangePicDetail;
	}

	public boolean isFromExchangePicDetail() {
		return fromExchangePicDetail;
	}

	private boolean fromExchangePicDetail;

	private boolean fromExchangeTimeToChangeFriend;
	public boolean isFromExchangeTimeToChangeFriend() {
		return fromExchangeTimeToChangeFriend;
	}

	public void setFromExchangeTimeToChangeFriend(boolean b) {
		this.fromExchangeTimeToChangeFriend = b;
	}

}
