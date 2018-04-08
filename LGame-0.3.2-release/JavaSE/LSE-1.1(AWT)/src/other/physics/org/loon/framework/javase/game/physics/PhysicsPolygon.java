package org.loon.framework.javase.game.physics;

import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;

import org.loon.framework.javase.game.action.sprite.SpriteImage;
import org.loon.framework.javase.game.core.geom.Polygon2D;
import org.loon.framework.javase.game.core.geom.Polygon2D.Point2D;
import org.loon.framework.javase.game.utils.CollectionUtils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;

/**
 * Copyright 2008 - 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * @project loonframework
 * @author chenpeng
 * @email：ceponline@yahoo.com.cn
 * @version 0.1
 */
public class PhysicsPolygon {

	final public static int MAX_SIZE = 4;

	protected boolean building;

	private int npoints;

	private int xpoints[];

	private int ypoints[];

	private Polygon polygon;

	private PolygonShape polygonShape;

	public PhysicsPolygon() {
		xpoints = new int[MAX_SIZE];
		ypoints = new int[MAX_SIZE];
		npoints = 0;
	}

	public PhysicsPolygon(String fileName, int x, int y) {
		this(new SpriteImage(fileName).makePolygon(x, y));
	}
	
	public PhysicsPolygon(Image img, int x, int y) {
		this(new SpriteImage(img),x,y);
	}
	
	public PhysicsPolygon(SpriteImage spr, int x, int y) {
		this(spr.makePolygon(x, y));
	}

	public PhysicsPolygon(Polygon polygon) {
		this(polygon.xpoints, polygon.ypoints, polygon.npoints);
	}

	public PhysicsPolygon(int xpoints[], int ypoints[], int npoints) {
		setPolygon(xpoints, ypoints, npoints);
	}

	public PhysicsPolygon(Vector2[] v) {
		npoints = v.length;
		xpoints = new int[npoints];
		ypoints = new int[npoints];
		for (int i = 0; i < npoints; ++i) {
			xpoints[i] = (int) v[i].x;
			ypoints[i] = (int) v[i].y;
		}
	}

	public PolygonShape getPolygonShape() {
		if (polygonShape == null) {
			polygonShape = new PolygonShape();
		}
		polygonShape.set(getVertexs());
		return polygonShape;
	}

	public float[] getVertexs() {
		int vertice_size = npoints * 2;
		float[] verts = new float[vertice_size];
		for (int i = 0, j = 0; i < vertice_size; i += 2, j++) {
			verts[i] = xpoints[j];
			verts[i + 1] = ypoints[j];
		}
		return verts;
	}

	public Point2D[] getPoint2D() {
		Point2D[] points = new Point2D[npoints];
		for (int i = 0; i < npoints; i++) {
			points[i] = new Point2D(xpoints[i], ypoints[i]);
		}
		return points;
	}

	public Polygon2D getPolygon2D() {
		return new Polygon2D(getPoint2D());
	}

	public float[] getVertex(int index) {
		if (index > xpoints.length || index > ypoints.length) {
			throw new IndexOutOfBoundsException("index > xpoints.length || "
					+ "index > ypoints.length");
		}
		return new float[] { xpoints[index], ypoints[index] };
	}

	public void setPolygon(Polygon polygon) {
		setPolygon(polygon.xpoints, polygon.ypoints, polygon.npoints);
	}

	public void setPolygon(int xpoints[], int ypoints[], int npoints) {
		if (npoints > xpoints.length || npoints > ypoints.length) {
			throw new IndexOutOfBoundsException("npoints > xpoints.length || "
					+ "npoints > ypoints.length");
		}
		if (npoints < 0) {
			throw new NegativeArraySizeException("npoints < 0");
		}
		this.npoints = npoints;
		this.xpoints = CollectionUtils.copyOf(xpoints, npoints);
		this.ypoints = CollectionUtils.copyOf(ypoints, npoints);
	}

	public boolean isConvex() {
		boolean isPositive = false;
		for (int i = 0; i < npoints; ++i) {
			int lower = (i == 0) ? (npoints - 1) : (i - 1);
			int middle = i;
			int upper = (i == npoints - 1) ? (0) : (i + 1);
			float dx0 = xpoints[middle] - xpoints[lower];
			float dy0 = ypoints[middle] - ypoints[lower];
			float dx1 = xpoints[upper] - xpoints[middle];
			float dy1 = ypoints[upper] - ypoints[middle];
			float cross = dx0 * dy1 - dx1 * dy0;
			boolean newIsP = (cross >= 0) ? true : false;
			if (i == 0) {
				isPositive = newIsP;
			} else if (isPositive != newIsP) {
				return false;
			}
		}
		return true;
	}

	public void addPoint(Point point) {
		addPoint(point.x, point.y);
	}

	public void addPoint(int x, int y) {
		if (npoints >= xpoints.length || npoints >= ypoints.length) {
			int newLength = npoints * 2;
			if (newLength < MAX_SIZE) {
				newLength = MAX_SIZE;
			} else if ((newLength & (newLength - 1)) != 0) {
				newLength = Integer.highestOneBit(newLength);
			}
			xpoints = CollectionUtils.copyOf(xpoints, newLength);
			ypoints = CollectionUtils.copyOf(ypoints, newLength);
		}
		xpoints[npoints] = x;
		ypoints[npoints] = y;
		npoints++;
	}

	public Point getPoint(int index) {
		if (index > xpoints.length || index > ypoints.length) {
			throw new IndexOutOfBoundsException("index > xpoints.length || "
					+ "index > ypoints.length");
		}
		return new Point(xpoints[index], ypoints[index]);
	}

	public Polygon getPolygon() {
		if (polygon == null) {
			polygon = new Polygon(xpoints, ypoints, npoints);
		} else {
			polygon.xpoints = CollectionUtils.copyOf(xpoints, npoints);
			polygon.ypoints = CollectionUtils.copyOf(ypoints, npoints);
			polygon.npoints = npoints;
		}
		return polygon;
	}

	public int getPoints() {
		return npoints;
	}

}
