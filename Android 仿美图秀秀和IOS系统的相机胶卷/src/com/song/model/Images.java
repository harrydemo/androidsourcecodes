package com.song.model;

import java.io.Serializable;

/**
 * 图片实体
 * @author admin
 *
 */
public class Images implements Serializable{
	
	//主键。图片 id，从 1 开始自增
	private int _id;		
	//图片绝对路径
	private String _data;
	//文件大小，单位为 byte
	private int _size;
	//文件名
	private String _display_name;
	//类似于 image/jpeg 的 MIME 类型
	private String mime_type;
	//不带扩展名的文件名
	private String title;
	//添加到数据库的时间，单位秒
	private long date_added;
	//文件最后修改时间，单位秒
	private long date_modified;
	private String description;
	//用于 picasa 网络相册
	private String picasa_id;
	private int isprivate;
	//纬度，需要照片有 GPS 信息
	private float latitude;
	//经度，需要照片有 GPS 信息
	private float longitude;
	//取自 EXIF 照片拍摄时间，若为空则等于文件修改时间，单位毫秒
	private long datetaken;
	//取自 EXIF 旋转角度，在图库旋转图片也会改变此值
	private int orientation;
	//取小缩略图时生成的一个随机数，见 MediaThumbRequest
	private int mini_thumb_magic;
	//等于 path.toLowerCase.hashCode()，见 MediaProvider.computeBucketValues()
	private String bucket_id;
	//直接包含图片的文件夹就是该图片的 bucket，就是文件夹名
	private String bucket_display_name;
	
	private Thumbnails thumbnails;
	
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	public String get_data() {
		return _data;
	}
	public void set_data(String _data) {
		this._data = _data;
	}
	public int get_size() {
		return _size;
	}
	public void set_size(int _size) {
		this._size = _size;
	}
	public String get_display_name() {
		return _display_name;
	}
	public void set_display_name(String _display_name) {
		this._display_name = _display_name;
	}
	public String getMime_type() {
		return mime_type;
	}
	public void setMime_type(String mime_type) {
		this.mime_type = mime_type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public long getDate_added() {
		return date_added;
	}
	public void setDate_added(long date_added) {
		this.date_added = date_added;
	}
	public long getDate_modified() {
		return date_modified;
	}
	public void setDate_modified(long date_modified) {
		this.date_modified = date_modified;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPicasa_id() {
		return picasa_id;
	}
	public void setPicasa_id(String picasa_id) {
		this.picasa_id = picasa_id;
	}
	public int getIsprivate() {
		return isprivate;
	}
	public void setIsprivate(int isprivate) {
		this.isprivate = isprivate;
	}
	public float getLatitude() {
		return latitude;
	}
	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}
	public float getLongitude() {
		return longitude;
	}
	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
	public long getDatetaken() {
		return datetaken;
	}
	public void setDatetaken(long datetaken) {
		this.datetaken = datetaken;
	}
	public int getOrientation() {
		return orientation;
	}
	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}
	public int getMini_thumb_magic() {
		return mini_thumb_magic;
	}
	public void setMini_thumb_magic(int mini_thumb_magic) {
		this.mini_thumb_magic = mini_thumb_magic;
	}
	public String getBucket_id() {
		return bucket_id;
	}
	public void setBucket_id(String bucket_id) {
		this.bucket_id = bucket_id;
	}
	public String getBucket_display_name() {
		return bucket_display_name;
	}
	public void setBucket_display_name(String bucket_display_name) {
		this.bucket_display_name = bucket_display_name;
	}
	public Thumbnails getThumbnails() {
		return thumbnails;
	}
	public void setThumbnails(Thumbnails thumbnails) {
		this.thumbnails = thumbnails;
	}
	
	@Override
	public String toString() {
		return "Images [_id=" + _id + ", _data=" + _data + ", _size=" + _size
				+ ", _display_name=" + _display_name + ", mime_type="
				+ mime_type + ", title=" + title + ", date_added=" + date_added
				+ ", date_modified=" + date_modified + ", description="
				+ description + ", picasa_id=" + picasa_id + ", isprivate="
				+ isprivate + ", latitude=" + latitude + ", longitude="
				+ longitude + ", datetaken=" + datetaken + ", orientation="
				+ orientation + ", mini_thumb_magic=" + mini_thumb_magic
				+ ", bucket_id=" + bucket_id + ", bucket_display_name="
				+ bucket_display_name + ", thumbnails=" + thumbnails + "]";
	}
	@Override
	public boolean equals(Object o) {
		Images image = (Images)o;
		if(this._id == image.get_id() && this._data.equals(image.get_data()))
			return true;
		return false;
	}
	
	
	
	
}
