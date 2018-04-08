package org.loon.framework.android.game.core.geom;

import java.util.ArrayList;

/**
 * 
 * Copyright 2008 - 2011
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
public class Path extends Shape {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ArrayList<float[]> localPoints;

	private float cx;

	private float cy;

	private boolean closed;

	private ArrayList<ArrayList<float[]>> holes;

	private ArrayList<float[]> hole;

	public Path(float sx, float sy) {
		if (holes == null) {
			holes = new ArrayList<ArrayList<float[]>>(10);
		}
		if (localPoints == null) {
			localPoints = new ArrayList<float[]>(10);
		}
		this.set(sx, sy);
		this.type = ShapeType.PATH_SHAPE;
	}

	public void set(float sx, float sy) {
		localPoints.add(new float[] { sx, sy });
		cx = sx;
		cy = sy;
		pointsDirty = true;
	}

	public void moveTo(float sx, float sy) {
		hole = new ArrayList<float[]>();
		holes.add(hole);
	}

	public void clear() {
		if (hole != null) {
			hole.clear();
		}
		if (localPoints != null) {
			localPoints.clear();
		}
	}

	public void lineTo(float x, float y) {
		if (hole != null) {
			hole.add(new float[] { x, y });
		} else {
			localPoints.add(new float[] { x, y });
		}
		cx = x;
		cy = y;
		pointsDirty = true;
	}

	public void close() {
		closed = true;
	}

	public void curveTo(float x, float y, float cx1, float cy1, float cx2,
			float cy2) {
		curveTo(x, y, cx1, cy1, cx2, cy2, 10);
	}

	public void curveTo(float x, float y, float cx1, float cy1, float cx2,
			float cy2, int segments) {
		if ((cx == x) && (cy == y)) {
			return;
		}
		Curve curve = new Curve(new Vector2f(cx, cy), new Vector2f(cx1, cy1),
				new Vector2f(cx2, cy2), new Vector2f(x, y));
		float step = 1.0f / segments;

		for (int i = 1; i < segments + 1; i++) {
			float t = i * step;
			Vector2f p = curve.pointAt(t);
			if (hole != null) {
				hole.add(new float[] { p.x, p.y });
			} else {
				localPoints.add(new float[] { p.x, p.y });
			}
			cx = p.x;
			cy = p.y;
		}
		pointsDirty = true;
	}

	protected void createPoints() {
		points = new float[localPoints.size() * 2];
		for (int i = 0; i < localPoints.size(); i++) {
			float[] p = (float[]) localPoints.get(i);
			points[(i * 2)] = p[0];
			points[(i * 2) + 1] = p[1];
		}
	}

	public Shape transform(Matrix transform) {
		Path p = new Path(cx, cy);
		p.localPoints = transform(localPoints, transform);
		for (int i = 0; i < holes.size(); i++) {
			p.holes.add(transform((ArrayList<float[]>) holes.get(i), transform));
		}
		p.closed = this.closed;

		return p;
	}

	private ArrayList<float[]> transform(ArrayList<float[]> pts, Matrix t) {
		float[] in = new float[pts.size() * 2];
		float[] out = new float[pts.size() * 2];

		for (int i = 0; i < pts.size(); i++) {
			in[i * 2] = ((float[]) pts.get(i))[0];
			in[(i * 2) + 1] = ((float[]) pts.get(i))[1];
		}
		t.transform(in, 0, out, 0, pts.size());
		ArrayList<float[]> outList = new ArrayList<float[]>();
		for (int i = 0; i < pts.size(); i++) {
			outList.add(new float[] { out[(i * 2)], out[(i * 2) + 1] });
		}
		return outList;
	}

	public boolean closed() {
		return closed;
	}
}
