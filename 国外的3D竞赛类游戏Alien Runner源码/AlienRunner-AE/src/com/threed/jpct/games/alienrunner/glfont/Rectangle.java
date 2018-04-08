package com.threed.jpct.games.alienrunner.glfont;


/**
 * 
 * @author  hakan eryargi (r a f t)
 */
public class Rectangle {

    public int x;
    public int y;
    public int width;
    public int height;

    public Rectangle() {
    	this(0, 0, 0, 0);
    }

    public Rectangle(Rectangle r) {
    	this(r.x, r.y, r.width, r.height);
    }

    public Rectangle(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
    }

    public Rectangle(int width, int height) {
    	this(0, 0, width, height);
    }

	public void set(Rectangle r) {
		this.x = r.x;
		this.y = r.y;
		this.width = r.width;
		this.height = r.height;
	}

	public Rectangle translate(int dX, int dY) {
		x += dX;
		y += dY;
		return this;
	}

	@Override
	public String toString() {
		return "Rectangle " + x + "," + y + " " + width + "," + height;
	}

	public Rectangle grow(int amount) {
		x -= amount; 
		y -= amount;
		width += amount + amount;
		height += amount + amount;
		return this;
	}

	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public boolean contains(int x, int y) {
		return (this.x <= x) && (this.y <= y) && (this.x + width >= x)  && (this.y + height >= y); 
	}
    
}
