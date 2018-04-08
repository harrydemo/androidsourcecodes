package org.loon.framework.android.game.action.map;

import org.loon.framework.android.game.core.geom.Vector2f;
import org.loon.framework.android.game.core.graphics.opengl.LTexture;

/**
 * Copyright 2008 - 2009
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
public class TileMap {

	private LTexture[] tiles;

	private int tileWidth;

	private int tileHeight;

	private int width, height;

	private Vector2f offset;

	/**
	 * 构造一个瓦片地图（默认大小32*32）
	 * 
	 * @param width
	 * @param height
	 */
	public TileMap(int width, int height) {
		this(width, height, 32, 32);
	}

	/**
	 * 构造一个瓦片地图，并指定宽x高
	 * 
	 * @param width
	 * @param height
	 * @param tileWidth
	 * @param tileHeight
	 */
	public TileMap(int width, int height, int tileWidth, int tileHeight) {
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		this.width = width;
		this.height = height;
		tiles = new LTexture[width * height];
		offset = new Vector2f(0, 0);
	}

	public LTexture getTile(int x, int y) {
		return tiles[x + width * y];
	}

	public void setTile(int x, int y, LTexture img) {
		tiles[x + width * y] = img;
	}

	public LTexture getTileFromPixels(float x, float y) {
		return getTileFromPixels(new Vector2f(x, y));
	}

	public LTexture getTileFromPixels(Vector2f p) {
		float x = (p.getX() + offset.getX());
		float y = (p.getY() + offset.getY());
		Vector2f tileCoordinates = pixelsToTiles(x, y);
		return getTile((int) Math.round(tileCoordinates.getX()), (int) Math
				.round(tileCoordinates.getY()));
	}

	public Vector2f pixelsToTiles(float x, float y) {
		float xprime = x / tileWidth - 1;
		float yprime = y / tileHeight - 1;
		return new Vector2f(xprime, yprime);
	}

	/**
	 * 转换坐标为像素坐标
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public Vector2f tilesToPixels(float x, float y) {
		float xprime = x * tileWidth - offset.getX();
		float yprime = y * tileHeight - offset.getY();
		return new Vector2f(xprime, yprime);
	}

	/**
	 * 获得矫正后的碰撞位置
	 * 
	 * @param p
	 * @param width
	 * @param height
	 * @return
	 */
	public Vector2f getCollision(Vector2f p, float width, float height) {
		LTexture tile1 = getTileFromPixels(p.getX(), p.getY());
		LTexture tile2 = getTileFromPixels(p.getX(), p.getY() + height);
		LTexture tile3 = getTileFromPixels(p.getX() + width, p.getY());
		float x, y;
		x = y = 0;
		if (tile1 != null) {
			x = -1;
		} else if (tile3 != null) {
			x = 1;
		}
		if (tile2 != null) {
			y = 1;
		} else if (tile1 != null) {
			y = -1;
		}
		return new Vector2f(x, y);
	}

	/**
	 * 设置瓦片位置
	 * 
	 * @param x
	 * @param y
	 */
	public void setOffset(float x, float y) {
		this.offset.setX(x);
		this.offset.setY(y);
	}

	/**
	 * 设定偏移量
	 * 
	 * @param offset
	 */
	public void setOffset(Vector2f offset) {
		this.offset.setX(offset.getX());
		this.offset.setY(offset.getY());
	}

	/**
	 * 获得瓦片位置
	 * 
	 * @return
	 */
	public Vector2f getOffset() {
		return offset;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

}
