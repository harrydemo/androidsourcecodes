package com.nmbb.oplayer.po;

public final class PFile {
	public long _id;
	/** 视频标题 */
	public String title;
	/** 视频标题拼音 */
	public String title_pinyin;
	/** 视频路径 */
	public String path;
	/** 最后一次访问时间 */
	public long last_access_time;
	/** 视频时长 */
	public int duration;
	/** 视频播放进度 */
	public int position;
	/** 视频缩略图 */
	public String thumb;
	/** 文件大小 */
	public long file_size;
}
