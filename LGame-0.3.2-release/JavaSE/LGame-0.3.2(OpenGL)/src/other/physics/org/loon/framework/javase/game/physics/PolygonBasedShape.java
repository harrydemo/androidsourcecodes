package org.loon.framework.javase.game.physics;

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
public abstract class PolygonBasedShape extends PrimitiveShape {

	protected float xoffset;

	protected float yoffset;

	public PolygonBasedShape() {
		super(new PolygonDef());
	}

	public PolygonBasedShape setOffset(final float x, final float y) {
		this.xoffset = x;
		this.yoffset = y;
		applyOffset(x, y);
		return this;
	}

	protected abstract void applyOffset(float x, float y);

	public float getXOffset() {
		return this.xoffset;
	}

	public float getYOffset() {
		return this.yoffset;
	}

}
