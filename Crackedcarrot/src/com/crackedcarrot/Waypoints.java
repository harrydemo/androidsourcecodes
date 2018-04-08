package com.crackedcarrot;

/**
 * A class that defines the specific way that each creature will take, by adding
 * specific Coords to an ArrayList
 */
public class Waypoints {

	public Coords[] way;
	public Scaler res;

	public Waypoints(int nrWP, Scaler scale) {
		this.res = scale;
		way = new Coords[nrWP];
	}

	/**
	 * 
	 * @param x
	 * @param i
	 * @param i
	 */
	public void setWaypoint(int x, int y, int i) {
		way[i] = res.scale(x, y);
	}

	public Coords[] getCoords() {
		return way;
	}

	public Coords getFirstWP() {
		return way[0];
	}
}