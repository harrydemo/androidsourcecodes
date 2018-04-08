package com.appspot.hexagon.util;

import java.util.ArrayList;
import android.graphics.Point;

public class Graphic2DUtil {

	public static ArrayList<Point> getPathOf2Points(Point pointFrom, Point pointTo, int speed) {
		// List<Point> retPath = new ArrayList<Point>();
		ArrayList<Point> lPath = null;
		lPath = new ArrayList<Point>();
		int dx = pointTo.x - pointFrom.x;
		int dy = pointTo.y - pointFrom.y;
		int ndx = Math.abs(dx);
		int ndy = Math.abs(dy);
		int steps = ((ndx > ndy) ? ndx : ndy);
		steps = (steps > speed) ? (steps / speed) : steps;
		// slide
		float sx = (float) dx / (float) steps;
		float sy = (float) dy / (float) steps;
		// Log.v(PlayerObject.class.getName(), "sx = " + sx + ", sy = " + sy);
		String msg = "";

		float dxi = (float) pointFrom.x;
		float dyi = (float) pointFrom.y;
		for (int i = 0; i < steps; i++) {
			dxi += sx;
			dyi += sy;
			Point p = new Point(Math.round(dxi), Math.round(dyi));
			// retPath.add(p);
			lPath.add(p);
		}
		return lPath;
	}
}
