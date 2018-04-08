package com.appspot.hexagon.dbo;

import com.appspot.hexagon.R;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;

public class LocationAnchor {

	private Drawable sprite = null;
	private Point coordinate;
	private String name;
	private String description;
	private long startTime = 0;
	private long stayAliveTime = 5000; // default object stay alive for 5
										// seconds

	public long getStayAliveTime() {
		return stayAliveTime;
	}

	public void setStayAliveTime(long stayAliveTime) {
		this.stayAliveTime = stayAliveTime;
	}

	private boolean isAlive = false;

	public LocationAnchor(String name, String description, int x, int y,
			Drawable sprite) {
		this.name = name;
		this.description = description;
		this.coordinate = new Point(x, y);
		this.sprite = sprite;
	}

	public LocationAnchor(int x, int y, Drawable sprite) {
		this.coordinate = new Point(x, y);
		this.sprite = sprite;
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

	public void invoke(int x, int y) {
		this.setXCoordinate(x);
		this.setYCoordinate(y);
		isAlive = true;
		this.startTime = System.currentTimeMillis();
	}

	public void drawOnScreen(Canvas canvas) {
		if (isAlive()) {
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
			this.sprite.setBounds(this.coordinate.x, this.coordinate.y,
					this.coordinate.x + getWidth(), this.coordinate.y
							+ getHeight());

			/* Make the sprite draw itself to the canvas */
			this.sprite.draw(canvas);
		} else {
			// do nothing
		}
	}

	public boolean isAlive() {
		long curTime = System.currentTimeMillis();
		long endTime = startTime + stayAliveTime;

		if (curTime < endTime) {
			isAlive = true;
		} else {
			startTime = 0;
			isAlive = false;
		}

		return isAlive;
	}
}
