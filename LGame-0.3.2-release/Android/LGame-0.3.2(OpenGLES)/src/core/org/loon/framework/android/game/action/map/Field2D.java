package org.loon.framework.android.game.action.map;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.loon.framework.android.game.core.geom.Vector2f;
import org.loon.framework.android.game.utils.CollectionUtils;

/**
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
 * @email ceponline@yahoo.com.cn
 * @version 0.1.1
 */
public class Field2D implements Config {

	private static Vector2f vector2;

	final static private Map<Vector2f, Integer> directions = new HashMap<Vector2f, Integer>(
			9);

	final static private Map<Integer, Vector2f> directionValues = new HashMap<Integer, Vector2f>(
			9);

	static {
		directions.put(new Vector2f(0, 0), Config.EMPTY);
		directions.put(new Vector2f(1, -1), Config.UP);
		directions.put(new Vector2f(-1, -1), Config.LEFT);
		directions.put(new Vector2f(1, 1), Config.RIGHT);
		directions.put(new Vector2f(-1, 1), Config.DOWN);
		directions.put(new Vector2f(0, -1), Config.TUP);
		directions.put(new Vector2f(-1, 0), Config.TLEFT);
		directions.put(new Vector2f(1, 0), Config.TRIGHT);
		directions.put(new Vector2f(0, 1), Config.TDOWN);

		directionValues.put(Config.EMPTY, new Vector2f(0, 0));
		directionValues.put(Config.UP, new Vector2f(1, -1));
		directionValues.put(Config.LEFT, new Vector2f(-1, -1));
		directionValues.put(Config.RIGHT, new Vector2f(1, 1));
		directionValues.put(Config.DOWN, new Vector2f(-1, 1));
		directionValues.put(Config.TUP, new Vector2f(0, -1));
		directionValues.put(Config.TLEFT, new Vector2f(-1, 0));
		directionValues.put(Config.TRIGHT, new Vector2f(1, 0));
		directionValues.put(Config.TDOWN, new Vector2f(0, 1));

	}

	private ArrayList<Vector2f> result;

	private int[][] data;

	private int tileWidth, tileHeight;

	private int width, height;

	public Field2D(Field2D field) {
		this(CollectionUtils.copyOf(field.data), field.tileWidth,
				field.tileHeight);
	}

	public Field2D(String fileName, int w, int h) {
		try {
			set(TileMapConfig.loadAthwartArray(fileName), w, h);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public Field2D(int[][] data) {
		this(data, 0, 0);
	}

	public Field2D(int[][] data, int w, int h) {
		this.set(data, w, h);
	}

	public void set(int[][] data, int w, int h) {
		this.setMap(data);
		this.setTileWidth(w);
		this.setTileHeight(h);
		this.width = data[0].length;
		this.height = data.length;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int pixelsToTilesWidth(int x) {
		return x / tileWidth;
	}

	public int pixelsToTilesHeight(int y) {
		return y / tileHeight;
	}

	public int tilesToWidthPixels(int tiles) {
		return tiles * tileWidth;
	}

	public int tilesToHeightPixels(int tiles) {
		return tiles * tileHeight;
	}

	public int getTileHeight() {
		return tileHeight;
	}

	public void setTileHeight(int tileHeight) {
		this.tileHeight = tileHeight;
	}

	public int getTileWidth() {
		return tileWidth;
	}

	public void setTileWidth(int tileWidth) {
		this.tileWidth = tileWidth;
	}

	public int getType(int x, int y) {
		try {
			return data[x][y];
		} catch (Exception e) {
			return -1;
		}
	}

	public void setType(int x, int y, int tile) {
		try {
			this.data[x][y] = tile;
		} catch (Exception e) {
		}
	}

	public int[][] getMap() {
		return data;
	}

	public void setMap(int[][] data) {
		this.data = data;
	}

	public boolean isHit(Vector2f point) {
		if (get(data, point) != -1) {
			return true;
		}
		return false;
	}

	public boolean isHit(int px, int py) {
		if (get(data, px, py) != -1) {
			return true;
		}
		return false;
	}

	public static int getDirection(int x, int y) {
		if (vector2 == null) {
			vector2 = new Vector2f(x, y);
		} else {
			vector2.set(x, y);
		}
		return directions.get(vector2);
	}

	public static Vector2f getDirection(int type) {
		return directionValues.get(type);
	}

	private static void insertArrays(int[][] arrays, int index, int px, int py) {
		arrays[index][0] = px;
		arrays[index][1] = py;
	}

	public int[][] neighbors(int px, int py, boolean flag) {
		int[][] pos = new int[8][2];
		insertArrays(pos, 0, px, py - 1);
		insertArrays(pos, 0, px + 1, py);
		insertArrays(pos, 0, px, py + 1);
		insertArrays(pos, 0, px - 1, py);
		if (flag) {
			insertArrays(pos, 0, px - 1, py - 1);
			insertArrays(pos, 0, px + 1, py - 1);
			insertArrays(pos, 0, px + 1, py + 1);
			insertArrays(pos, 0, px - 1, py + 1);
		}
		return pos;
	}

	public ArrayList<Vector2f> neighbors(Vector2f pos, boolean flag) {
		if (result == null) {
			result = new ArrayList<Vector2f>(8);
		} else {
			result.clear();
		}
		int x = pos.x();
		int y = pos.y();
		result.add(new Vector2f(x, y - 1));
		result.add(new Vector2f(x + 1, y));
		result.add(new Vector2f(x, y + 1));
		result.add(new Vector2f(x - 1, y));
		if (flag) {
			result.add(new Vector2f(x - 1, y - 1));
			result.add(new Vector2f(x + 1, y - 1));
			result.add(new Vector2f(x + 1, y + 1));
			result.add(new Vector2f(x - 1, y + 1));
		}
		return result;
	}

	public int score(Vector2f goal, Vector2f point) {
		return Math.abs(point.x() - goal.x()) + Math.abs(point.y() - goal.y());
	}

	public int score(int x, int y, int px, int py) {
		return Math.abs(px - x) + Math.abs(py - y);
	}

	private int get(int[][] data, int px, int py) {
		try {
			if (px < width && py < height) {
				return data[py][px];
			} else {
				return -1;
			}
		} catch (Exception e) {
			return -1;
		}
	}

	private int get(int[][] data, Vector2f point) {
		try {
			if (point.x() < width && point.y() < height) {
				return data[point.y()][point.x()];
			} else {
				return -1;
			}
		} catch (Exception e) {
			return -1;
		}
	}
}
