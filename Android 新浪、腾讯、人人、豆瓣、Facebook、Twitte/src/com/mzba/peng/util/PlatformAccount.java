package com.mzba.peng.util;
/**
 * 开放平台
 * @author 06peng
 *
 */
public class PlatformAccount {

	/**
	 * 开放平台类型
	 * QQ（0）、
	 * 新浪微博（1）、
	 * 腾讯微博（2）、
	 * 人人网（3）、
	 * 豆瓣网（4）、
	 * twitter（5）、
	 * facebook（6）。
	 */
	private int openType;
	
	private String accessToken;
	
	private String tokenSecret;
	
	private String nickName;
	
	/**
	 * 开放平台uid（QQ为一个用户ID的字符串，腾讯微博为用户登录名，其他开放平台为一个数字ID）
	 */
	private String openUid;
	
	/**
	 * 开放平台性别（新浪微博、腾讯微博、人人网有，其他没有）
	 */
	private int openSex;
	
	/**
	 * 开放平台过期标识（人人网特有）
	 */
	private String openExpire;
	
	private String openAvatar;

	public int getOpenType() {
		return openType;
	}

	public void setOpenType(int openType) {
		this.openType = openType;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getTokenSecret() {
		return tokenSecret;
	}

	public void setTokenSecret(String tokenSecret) {
		this.tokenSecret = tokenSecret;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getOpenUid() {
		return openUid;
	}

	public void setOpenUid(String openUid) {
		this.openUid = openUid;
	}

	public int getOpenSex() {
		return openSex;
	}

	public void setOpenSex(int openSex) {
		this.openSex = openSex;
	}

	public String getOpenExpire() {
		return openExpire;
	}

	public void setOpenExpire(String openExpire) {
		this.openExpire = openExpire;
	}

	public String getOpenAvatar() {
		return openAvatar;
	}

	public void setOpenAvatar(String openAvatar) {
		this.openAvatar = openAvatar;
	}
}
