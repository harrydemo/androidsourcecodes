package org.loon.framework.android.game.core.geom;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import org.loon.framework.android.game.action.map.Field2D;
import org.loon.framework.android.game.utils.MathUtils;

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
 * @version 0.1.3
 */
public class Vector2f implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1844534518528011982L;

	public float x, y;

	public Vector2f(float value) {
		this(value, value);
	}

	public Vector2f() {
		this(0, 0);
	}

	public Vector2f(float[] coords) {
		x = coords[0];
		y = coords[1];
	}

	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Vector2f(Vector2f vector2D) {
		this.x = vector2D.x;
		this.y = vector2D.y;
	}

	public void turn(float angle) {
		setAngle(getAngle() + angle);
	}

	public void move(Vector2f vector2D) {
		this.x += vector2D.x;
		this.y += vector2D.y;
	}

	public void move_multiples(int direction, int multiples) {
		if (multiples <= 0) {
			multiples = 1;
		}
		Vector2f v = Field2D.getDirection(direction);
		move(v.x() * multiples, v.y() * multiples);
	}

	public void moveX(int x) {
		this.x += x;
	}

	public void moveY(int y) {
		this.y += y;
	}

	public void moveByAngle(int degAngle, float distance) {
		if (distance == 0) {
			return;
		}
		float Angle = MathUtils.toRadians(degAngle);
		float dX = (MathUtils.cos(Angle) * distance);
		float dY = (-MathUtils.sin(Angle) * distance);
		int idX = MathUtils.round(dX);
		int idY = MathUtils.round(dY);
		move(idX, idY);
	}

	public void move(float x, float y) {
		this.x += x;
		this.y += y;
	}

	public void move(float distance) {
		float angle = MathUtils.toRadians(getAngle());
		int x = MathUtils.round(getX() + MathUtils.cos(angle) * distance);
		int y = MathUtils.round(getY() + MathUtils.sin(angle) * distance);
		setLocation(x, y);
	}

	public boolean nearlyCompare(Vector2f v, int range) {
		int dX = MathUtils.abs(x() - v.x());
		int dY = MathUtils.abs(y() - v.y());
		return (dX <= range) && (dY <= range);
	}

	public int angle(Vector2f v) {
		int dx = v.x() - x();
		int dy = v.y() - y();
		int adx = MathUtils.abs(dx);
		int ady = MathUtils.abs(dy);
		if ((dy == 0) && (dx == 0)) {
			return 0;
		}
		if ((dy == 0) && (dx > 0)) {
			return 0;
		}
		if ((dy == 0) && (dx < 0)) {
			return 180;
		}
		if ((dy > 0) && (dx == 0)) {
			return 90;
		}
		if ((dy < 0) && (dx == 0)) {
			return 270;
		}
		float rwinkel = MathUtils.atan(ady / adx);
		float dwinkel = 0.0f;
		if ((dx > 0) && (dy > 0)) {
			dwinkel = MathUtils.toDegrees(rwinkel);
		} else if ((dx < 0) && (dy > 0)) {
			dwinkel = (180.0f - MathUtils.toDegrees(rwinkel));
		} else if ((dx > 0) && (dy < 0)) {
			dwinkel = (360.0f - MathUtils.toDegrees(rwinkel));
		} else if ((dx < 0) && (dy < 0)) {
			dwinkel = (180.0f + MathUtils.toDegrees(rwinkel));
		}
		int iwinkel = (int) dwinkel;
		if (iwinkel == 360) {
			iwinkel = 0;
		}
		return iwinkel;
	}

	public void setAngle(float angle) {
		if ((angle < -360) || (angle > 360)) {
			angle = angle % 360;
		}
		if (angle < 0) {
			angle = 360 + angle;
		}
		double oldAngle = getAngle();
		if ((angle < -360) || (angle > 360)) {
			oldAngle = oldAngle % 360;
		}
		if (oldAngle < 0) {
			oldAngle = 360 + oldAngle;
		}
		float len = length();
		x = len * (float) MathUtils.cos(StrictMath.toRadians(angle));
		y = len * (float) MathUtils.sin(StrictMath.toRadians(angle));
	}

	public float getAngle() {
		float theta = (float) StrictMath.toDegrees(StrictMath.atan2(y, x));
		if ((theta < -360) || (theta > 360)) {
			theta = theta % 360;
		}
		if (theta < 0) {
			theta = 360 + theta;
		}
		return theta;
	}

	public float[] getCoords() {
		return (new float[] { x, y });
	}

	public void setLocation(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public boolean equals(Object o) {
		if (o instanceof Vector2f) {
			Vector2f p = (Vector2f) o;
			return p.x == x && p.y == y;
		}
		return false;
	}

	public int hashCode() {
		return (int) (x + y);
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public int x() {
		return (int) x;
	}

	public int y() {
		return (int) y;
	}

	public Object clone() {
		return new Vector2f(x, y);
	}

	public void set(Vector2f other) {
		set(other.getX(), other.getY());
	}

	public void set(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Vector2f reverse() {
		x = -x;
		y = -y;
		return this;
	}

	public float length() {
		return MathUtils.sqrt(x * x + y * y);
	}

	public float lengthSquared() {
		return (x * x) + (y * y);
	}

	public Vector2f add(Vector2f other) {
		float x = this.x + other.x;
		float y = this.y + other.y;
		return new Vector2f(x, y);
	}

	public Vector2f addThis(Vector2f other) {
		this.x += other.x;
		this.y += other.y;
		return this;
	}

	public static Vector2f sum(List<?> summands) {
		Vector2f result = new Vector2f(0, 0);
		for (Iterator<?> it = summands.iterator(); it.hasNext();) {
			Vector2f v = (Vector2f) it.next();
			result.addThis(v);
		}
		return result;
	}

	public static Vector2f sum(Vector2f a, Vector2f b) {
		Vector2f answer = new Vector2f(a);
		return answer.addThis(b);
	}

	public static Vector2f mean(List<?> points) {
		int n = points.size();
		if (n == 0) {
			return new Vector2f(0, 0);
		}
		return Vector2f.sum(points).scale(1.0f / n);

	}

	public Vector2f sub(Vector2f v) {
		x -= v.getX();
		y -= v.getY();
		return this;
	}

	public Vector2f subtract(Vector2f other) {
		float x = this.x - other.x;
		float y = this.y - other.y;
		return new Vector2f(x, y);
	}

	public float dot(Vector2f vec) {
		return (x * vec.x) + (y * vec.y);
	}

	public static float cross(Vector2f a, Vector2f b) {
		return a.cross(b);
	}

	public float cross(Vector2f vec) {
		return x * vec.y - y * vec.x;
	}

	public Vector2f multiply(float value) {
		return new Vector2f(value * x, value * y);
	}

	public float dotProduct(Vector2f other) {
		return other.x * x + other.y * y;
	}

	public Vector2f scale(float a) {
		x *= a;
		y *= a;
		return this;
	}

	public Vector2f normalize() {
		float magnitude = MathUtils.sqrt(dotProduct(this));
		return new Vector2f(x / magnitude, y / magnitude);
	}

	public float level() {
		return MathUtils.sqrt(dotProduct(this));
	}

	public float distanceSquared(Vector2f other) {
		float dx = other.getX() - getX();
		float dy = other.getY() - getY();

		return (dx * dx) + (dy * dy);
	}

	public float distance(Vector2f other) {
		return MathUtils.sqrt(distanceSquared(other));
	}

	public Vector2f modulate(Vector2f other) {
		float x = this.x * other.x;
		float y = this.y * other.y;
		return new Vector2f(x, y);
	}

	public boolean equalsDelta(Vector2f other, float delta) {
		return (other.getX() - delta < x && other.getX() + delta > x
				&& other.getY() - delta < y && other.getY() + delta > y);
	}

	public static Vector2f difference(Vector2f first, Vector2f second) {
		Vector2f answer = new Vector2f(first);
		return answer.sub(second);
	}

	public void rotate90() {
		setLocation(y, -x);
	}

	public static Vector2f rotate90(Vector2f vec) {
		return new Vector2f(-vec.y, vec.x);
	}

	public static Vector2f rotate90R(Vector2f vec) {
		return new Vector2f(vec.y, -vec.x);
	}

	public static float dot(Vector2f a, Vector2f b) {
		return a.dot(b);
	}

	public static float crossZ(Vector2f a, Vector2f b) {
		return a.x * b.y - a.y * b.x;
	}

	public static Vector2f mult(Vector2f vector, float scalar) {
		Vector2f answer = new Vector2f(vector);
		return answer.scale(scalar);
	}

	public Vector2f copy() {
		return new Vector2f(x, y);
	}

	public String toString() {
		return (new StringBuffer("[Vector2f x:")).append(x).append(" y:")
				.append(y).append("]").toString();
	}
}
