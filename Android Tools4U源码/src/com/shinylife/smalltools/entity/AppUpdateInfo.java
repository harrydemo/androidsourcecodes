package com.shinylife.smalltools.entity;

public class AppUpdateInfo {
	private String lastVersion;
	private String url;
	private boolean hasNewVersion;
	private String appSize;
	public String getLastVersion() {
		return lastVersion;
	}
	public String getUrl() {
		return url;
	}
	public boolean isHasNewVersion() {
		return hasNewVersion;
	}
	public void setLastVersion(String lastVersion) {
		this.lastVersion = lastVersion;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public void setHasNewVersion(boolean hasNewVersion) {
		this.hasNewVersion = hasNewVersion;
	}
	public String getAppSize() {
		return appSize;
	}
	public void setAppSize(String appSize) {
		this.appSize = appSize;
	}
}
