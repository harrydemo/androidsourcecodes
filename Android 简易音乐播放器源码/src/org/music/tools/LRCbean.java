package org.music.tools;

/**
 * ���ʵ����
 */
public class LRCbean {
	private int beginTime = 0;// ��ʼʱ��
	private int lineTime = 0;// ʱ����
	private String lrcBody = null;// ��ʱ���

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
