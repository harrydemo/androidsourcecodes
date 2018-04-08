package com.appspot.hexagon.dbo;

import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.provider.BaseColumns;

public class MapCell implements BaseColumns {
	public interface Constants extends BaseColumns {
		// table:hexagon.WorldMap
		public static final String TABLE = "mapcell";
		// columns
		public static final String _ID = "cell_id";
		public static final String NAME = "cell_name";
		public static final String DESC = "cell_desc";
		public static final String IMAGE = "cell_img";
		public static final String PARENT_MAP = "cell_parent_map";
		public static final String MAP_ROW = "cell_map_row";
		public static final String MAP_COLUMN = "cell_map_column";
	}
	
	private int id = -1;
	private String name;
	private String desc;
	private int image = -1;
	private int parentMapId = -1;
	private int mapRow = -1;
	private int mapColumn = -1;
	
	public MapCell(String name, String description, int image, int parentMapId, int mapRow, int mapColumn) {
		this.name = name;
		this.desc = description;
		this.image = image;
		this.setParentMapId(parentMapId);
		this.mapRow = mapRow;
		this.mapColumn = mapColumn;
	}
	
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
	
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public String getDesc() {
		return desc;
	}
	
	public int getImage() {
		return image;
	}

	public void setImage(int image) {
		this.image = image;
	}

	public int getMapRow() {
		return mapRow;
	}

	public void setMapRow(int mapRow) {
		this.mapRow = mapRow;
	}

	public int getMapColumn() {
		return mapColumn;
	}

	public void setMapColumn(int mapColumn) {
		this.mapColumn = mapColumn;
	}

	/**
	 * @param parentMapId the parentMapId to set
	 */
	public void setParentMapId(int parentMapId) {
		this.parentMapId = parentMapId;
	}

	/**
	 * @return the parentMapId
	 */
	public int getParentMapId() {
		return parentMapId;
	}
}
