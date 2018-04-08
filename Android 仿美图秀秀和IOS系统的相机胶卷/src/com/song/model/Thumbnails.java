package com.song.model;

import java.io.Serializable;

import com.song.Constant;
import com.song.util.ImageUtil;

import android.graphics.Bitmap;

/**
 * ����ͼʵ��
 * @author admin
 *
 */
public class Thumbnails implements Serializable{
	
	//����������ͼ id���� 1 ��ʼ����
	private int _id;
	//ͼƬ����·��
	private String _data;
	//����ͼ����ӦͼƬ�� id�������� images �� _id �ֶΣ��ɽ������
	private int image_id;
	//����ͼ���ͣ�1 �Ǵ�����ͼ��2 �������ã�3 ��΢������ͼ������Ϣ�����������ݿ�
	private int kind;
	//����ͼ���
	private int width;
	//����ͼ�߶�
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
