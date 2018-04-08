package org.music.tools;

/**
 * 下载时，进度条记录当前位置，大小等。
 *
 * 
 */
public class LoadInfo {
	public int fileSize;// 文件大小
	private int complete;// 完成度
	private String urlstring;// 下载起标识

	public LoadInfo(int fileSize, int complete, String urlstring) {
		super();
		this.fileSize = fileSize;
		this.complete = complete;
		this.urlstring = urlstring;

	}

	public int getFileSize() {
		return fileSize;
	}

	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}

	public int getComplete() {
		return complete;
	}

	public void setComplete(int complete) {
		this.complete = complete;
	}

	public String getUrlstring() {
		return urlstring;
	}

	public void setUrlstring(String urlstring) {
		this.urlstring = urlstring;
	}

}
