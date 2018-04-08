package com.androidmediaplayer.model;

import java.io.Serializable;

public class DownloadTask implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public static final int STATE_DOWNLOADING = 1;
	public static final int STATE_DONE = 2;
	public static final int STATE_NONE = 3;
	
	private int taskIndex;
	private int state_download = STATE_NONE;
	private int groupIndex;
	private int childIndex;
	
	private Mp3Info mp3Info = null;

	public Mp3Info getMp3Info() {
		return mp3Info;
	}

	public void setMp3Info(Mp3Info mp3Info) {
		this.mp3Info = mp3Info;
	}

	public int getTaskIndex() {
		return taskIndex;
	}

	public void setTaskIndex(int taskIndex) {
		this.taskIndex = taskIndex;
	}

	public int getState_download() {
		return state_download;
	}

	public void setState_download(int state_download) {
		this.state_download = state_download;
	}

	public int getGroupIndex() {
		return groupIndex;
	}

	public void setGroupIndex(int groupIndex) {
		this.groupIndex = groupIndex;
	}

	public int getChildIndex() {
		return childIndex;
	}

	public void setChildIndex(int childIndex) {
		this.childIndex = childIndex;
	}

    @Override
    public String toString() {
        return "DownloadTask [taskIndex=" + taskIndex + ", state_download="
                + state_download + ", groupIndex=" + groupIndex
                + ", childIndex=" + childIndex + ", mp3Info=" + mp3Info + "]";
    }
	
}
