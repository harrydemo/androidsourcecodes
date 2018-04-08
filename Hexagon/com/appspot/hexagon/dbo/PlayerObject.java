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

public class PlayerObject {

	private double longitude;
	private double latitude;
	private Drawable sprite = null;
	private Point coordinate;
	/*
	 * Working with a Enum is 10000% safer than working with int's to 'remember'
	 * the direction.
	 */
	private HorizontalDirection hDirection;
	private VerticalDirection vDirection;
	private String name;
	private String description;
	private int speed = 4; // default speed
	private ArrayList<Point> path = null;

	public ArrayList<Point> getPath() {
		return path;
	}

	public void setPath(ArrayList<Point> path) {
		this.path = path;
	}

	public static enum HorizontalDirection {
		LEFT, RIGHT
	}

	public static enum VerticalDirection {
		UP, DOWN
	}

	public PlayerObject(String name, String description, int x, int y,
			Drawable sprite) {
		this.name = name;
		this.description = description;
		this.coordinate = new Point(x, y);
		this.sprite = sprite;
	}

	public PlayerObject(int x, int y, Drawable sprite) {
		this.coordinate = new Point(x, y);
		this.sprite = sprite;
	}

	public HorizontalDirection getHDirection() {
		return hDirection;
	}

	public void setHDirection(HorizontalDirection direction) {
		hDirection = direction;
	}

	public VerticalDirection getVDirection() {
		return vDirection;
	}

	public void setVDirection(VerticalDirection direction) {
		vDirection = direction;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public int getWidth() {
		return sprite.getIntrinsicWidth();
	}

	public int getHeight() {
		return sprite.getIntrinsicHeight();
	}

	public void setDescription(String description) {
		this.description = description;
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

	public void setOnMapCoordinates(double longitude, double latitude) {
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public void drawOnScreen(Canvas canvas) {
		// any un-walked path?
		if (path != null) {
			if (path.iterator().hasNext()) {
				Point pNew = path.iterator().next();
				this.setXCoordinate(pNew.x);
				this.setYCoordinate(pNew.y);
				path.remove(pNew);
			} else {
				// lPath = new ArrayList<Point>();
				path = null;
			}
		}
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

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getSpeed() {
		return speed;
	}
}
