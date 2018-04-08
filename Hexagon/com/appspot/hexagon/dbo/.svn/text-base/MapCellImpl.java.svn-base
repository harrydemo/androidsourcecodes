package com.appspot.hexagon.dbo;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import com.appspot.hexagon.HexagonGameView;
import com.appspot.hexagon.R;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class MapCellImpl extends MapCell{

	private Drawable sprite = null;
	private Point coordinate;
	private WorldMap parentMap = null;

	/**
	 * TODO:
	 * - method to define row x col in world map
	 * - method to define tile (width x height) size (32x32)
	 * - use DB to enquiry???
	 */	

	/**
	 * 
	 * @param name
	 * @param description
	 * @param x
	 * @param y
	 * @param sprite
	 */
	public MapCellImpl(String name, String description, int image, int parentMapId, int mapRow, int mapColumn, Drawable sprite) {
		super(name,description,image,parentMapId,mapRow,mapColumn);
		this.sprite = sprite;
		this.coordinate = new Point(mapRow*getHeight(), mapColumn*getWidth());
		// TODO: query by parentMapId
		this.parentMap = null;
	}
	
	/**
	 * @param parentMap the parentMap to set
	 */
	public void setParentMap(WorldMap parentMap) {
		this.parentMap = parentMap;
	}

	/**
	 * @return the parentMap
	 */
	public WorldMap getParentMap() {
		return parentMap;
	}


	public int getWidth() {
		return sprite.getIntrinsicWidth();
	}

	public int getHeight() {
		return sprite.getIntrinsicHeight();
	}



	public Point getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(Point coordinate) {
		this.coordinate = coordinate;
	}

	public void setXCoordinate(int x) {
		this.coordinate.x = x;
	}

	public void setYCoordinate(int y) {
		this.coordinate.y = y;
	}

	public int getXCoordinate() {
		return this.coordinate.x;
	}

	public int getYCoordinate() {
		return this.coordinate.y;
	}

	public void drawOnScreen(Canvas canvas) {
		// check for off screen
		int x = getXCoordinate() + getWidth();
		int y = getYCoordinate() + getHeight();
		if (x > canvas.getWidth()) {
			setXCoordinate(canvas.getWidth() - getWidth());
		} else if (x < 0) {
			setXCoordinate(0);
		}
		if (y > canvas.getHeight()) {
			setYCoordinate(canvas.getHeight() - getHeight());
		} else if (y < 0) {
			setYCoordinate(0);
		}

		this.sprite
				.setBounds(this.coordinate.x, this.coordinate.y,
						this.coordinate.x + getWidth(), this.coordinate.y
								+ getHeight());

		/* Make the sprite draw itself to the canvas */
		this.sprite.draw(canvas);
	}

	
}
