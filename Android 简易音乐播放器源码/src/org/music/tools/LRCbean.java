package org.music.tools;

/**
 * 歌词实体类
 */
public class LRCbean {
	private int beginTime = 0;// 开始时间
	private int lineTime = 0;// 时间轴
	private String lrcBody = null;// 歌词本体

	public int getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(int beginTime) {
		this.beginTime = beginTime;
	}

	public int getLineTime() {
		return lineTime;
	}

	public void setLineTime(int lineTime) {
		this.lineTime = lineTime;
	}

	public String getLrcBody() {
		return lrcBody;
	}

	public void setLrcBody(String lrcBody) {
		this.lrcBody = lrcBody;
	}

}
