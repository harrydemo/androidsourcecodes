package org.loon.framework.javase.game.action.sprite;

import java.io.Serializable;

import org.loon.framework.javase.game.core.LRelease;

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
 * @email：ceponline@yahoo.com.cn
 * @version 0.1
 */
public class Mask implements Serializable, LRelease {

	/**
	 * 像素遮挡关系处理器
	 */
	private static final long serialVersionUID = -4316629891519820901L;

	private int height;

	private int width;

	private boolean[][] data;

	public Mask(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public Mask(boolean[][] data, int width, int height) {
		this.data = data;
		this.width = width;
		this.height = height;
	}

	public boolean[][] getData() {
		return data;
	}

	public boolean getPixel(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height) {
			return false;
		}
		return data[y][x];
	}

	public void setData(boolean[][] data) {
		this.data = data;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void dispose() {
		if (data != null) {
			data = null;
		}
	}

}
