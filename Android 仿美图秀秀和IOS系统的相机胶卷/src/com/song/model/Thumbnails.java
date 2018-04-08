package com.song.model;

import java.io.Serializable;

import com.song.Constant;
import com.song.util.ImageUtil;

import android.graphics.Bitmap;

/**
 * 缩略图实体
 * @author admin
 *
 */
public class Thumbnails implements Serializable{
	
	//主键。缩略图 id，从 1 开始自增
	private int _id;
	//图片绝对路径
	private String _data;
	//缩略图所对应图片的 id，依赖于 images 表 _id 字段，可建立外键
	private int image_id;
	//缩略图类型，1 是大缩略图，2 基本不用，3 是微型缩略图但其信息不保存在数据库
	private int kind;
	//缩略图宽度
	private int width;
	//缩略图高度
	private int height;
	
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
	public int getImage_id() {
		return image_id;
	}
	public void setImage_id(int image_id) {
		this.image_id = image_id;
	}
	public int getKind() {
		return kind;
	}
	public void setKind(int kind) {
		this.kind = kind;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	@Override
	public String toString() {
		return "Thumbnails [_id=" + _id + ", _data=" + _data + ", image_id="
				+ image_id + ", kind=" + kind + ", width=" + width
				+ ", height=" + height + "]";
	}
	
	

}
