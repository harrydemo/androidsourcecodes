package com.song.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.song.Constant;
import com.song.util.ImageUtil;

import android.graphics.Bitmap;

/**
 * 文件夹实体
 * @author admin
 *
 */
public class Bucket implements Serializable{
	
	private String name;
	
	private int imageCount;
	
	private String path;

	private List<Images> images = new ArrayList<Images>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getImageCount() {
		return this.images.size();
	}

	public void setImageCount(int imageCount) {
		this.imageCount = imageCount;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public List<Images> getImages() {
		return images;
	}

	public void setImages(List<Images> images) {
		this.images = images;
	}
	
	public void addImages(Images image){
		
		this.images.add(image);
		
	}

	@Override
	public String toString() {
		return "Bucket [name=" + name + ", imageCount=" + imageCount
				+ ", path=" + path + ", images=" + images + "]";
	}
	
	
	
	
	
	

}
