package com.androidmediaplayer.constant;

import java.util.Stack;

import android.app.Application;

import com.androidmediaplayer.model.DownloadTask;
import com.androidmediaplayer.model.Mp3Info;

public class ConstantExtendsApplication extends Application {

	//是否有下载新歌曲，决定了跳转后是否要刷新库和更新UI
	private boolean refresh = false;
	//播放列表是否有变动，有则允许重新加载
	private boolean refreshPlayList = true;
	//单纯更新本地音乐UI
	private boolean refreshOnly = false;
	
	private DownloadTask downloadTask = null;
	public Stack<DownloadTask> downloadTasks = new Stack<DownloadTask>();
	
	private boolean hadInitPlayer = false;
	private Mp3Info currentPlayingMp3Info = null;
	
	public boolean isHadInitPlayer() {
        return hadInitPlayer;
    }

    public void setHadInitPlayer(boolean hadInitPlayer) {
        this.hadInitPlayer = hadInitPlayer;
    }

    public Mp3Info getCurrentPlayingMp3Info() {
        return currentPlayingMp3Info;
    }

    public void setCurrentPlayingMp3Info(Mp3Info currentPlayingMp3Info) {
        this.currentPlayingMp3Info = currentPlayingMp3Info;
    }

    private boolean playerActivityExist = false;
	
	public boolean isRefreshOnly() {
        return refreshOnly;
    }

    public void setRefreshOnly(boolean refreshOnly) {
        this.refreshOnly = refreshOnly;
    }

    public boolean isPlayerActivityExist() {
		return playerActivityExist;
	}

	public void setPlayerActivityExist(boolean playerActivityExist) {
		this.playerActivityExist = playerActivityExist;
	}

	public DownloadTask getDownloadTask() {
		return downloadTask;
	}

	public void setDownloadTask(DownloadTask downloadTask) {
		this.downloadTask = downloadTask;
	}

	public boolean isRefresh() {
		return refresh;
	}

	public void setRefresh(boolean refresh) {
		this.refresh = refresh;
	}

	public boolean isRefreshPlayList() {
		return refreshPlayList;
	}

	public void setRefreshPlayList(boolean refreshPlayList) {
		this.refreshPlayList = refreshPlayList;
	}

}
