package com.androidmediaplayer.model;

import java.io.Serializable;

/**
 * 进度条相关
 *
 */
public class Progress implements Serializable{

    private static final long serialVersionUID = 1L;
    //文件大小
	private int orgfileSize = -1;
	//已经下载的文件大小
	private int fileSize = 0;

	public int getFileSize() {
		return fileSize;
	}

	public synchronized void setFileSize(int fileSize) {
		this.fileSize = this.fileSize + fileSize;
	}

	public void setOrgfileSize(int orgfileSize) {
		this.orgfileSize = orgfileSize;
	}
	
	public int getPercent(){
		return orgfileSize == -1 ? 0 : (int)(((float)fileSize/(float)orgfileSize)*100);
	}
	
}
