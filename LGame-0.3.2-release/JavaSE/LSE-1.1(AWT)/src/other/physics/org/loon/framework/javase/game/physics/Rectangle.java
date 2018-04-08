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
public class Rectangle extends PolygonBasedShape {
	
	private float width;

	private float height;

	public Rectangle(float width, float height) {
		this(width, height, 0, 0, 0);
	}

	public Rectangle(float width, float height, float density) {
		this(width, height, density, 0, 0);
	}

	public Rectangle(float width, float height, float density, float resitution) {
		this(width, height, density, resitution, 0);
	}

	public Rectangle(float width, float height, float density,
			float restitution, float friction) {
		this.def.setAsBox(width / 2, height / 2);
		this.def.density = density;
		this.def.restitution = restitution;
		this.def.friction = friction;
		this.width = width;
		this.height = height;
	}

	public float getWidth() {
		return this.width;
	}

	public float getHeight() {
		return this.height;
	}

	protected void applyOffset(float x, float y) {
		this.def.setAsBox(this.width / 2.0F, this.height / 2.0F);
	}
}
