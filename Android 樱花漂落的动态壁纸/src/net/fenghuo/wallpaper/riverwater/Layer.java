package net.fenghuo.wallpaper.riverwater;

import android.graphics.Canvas;

public abstract class Layer {
	int x, y;
	int width,height;
	boolean visible = true; // LayerÊÇ·ñ¿É¼û
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}	
	
	public void setPostion(int x, int y) {
		this.x = x;
		this.y = y;
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
	
	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public abstract void draw(Canvas canvas,int referencex,int referencey);
}
