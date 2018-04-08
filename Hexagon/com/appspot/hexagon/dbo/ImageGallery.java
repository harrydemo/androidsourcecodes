/**
 * 
 */
package com.appspot.hexagon.dbo;

import android.provider.BaseColumns;

/**
 * @author Admin
 *
 */
public class ImageGallery {

	public interface Constants extends BaseColumns {
		// table:hexagon.WorldMap
		public static final String TABLE = "imagegallery";
		// columns
		public static final String _ID = "gal_id";
		public static final String NAME = "gal_name";
		public static final String DESC = "gal_desc";
		public static final String WIDTH = "img_width";
		public static final String HEIGHT = "img_height";
		public static final String IMAGE = "gal_img";
	}
	
	private int id = -1;
	private String name = null;
	private String desc = null;
	private int width = 0;
	private int height = 0;
	private int image = 0;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
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
	public int getImage() {
		return image;
	}
	public void setImage(int image) {
		this.image = image;
	}
	
	
}
