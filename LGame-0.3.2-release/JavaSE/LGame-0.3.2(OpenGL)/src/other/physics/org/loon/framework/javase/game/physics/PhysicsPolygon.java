package org.loon.framework.javase.game.physics;

import org.loon.framework.javase.game.core.geom.Polygon;
import org.loon.framework.javase.game.core.graphics.opengl.LTexture;
import org.loon.framework.javase.game.core.graphics.opengl.LTextures;

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

	private Polygon polygon;

	public PhysicsPolygon(String fileName) {
		this(LTextures.loadTexture(fileName).get());
	}

	public PhysicsPolygon(LTexture img) {
		this((Polygon) img.getShape());
	}

	public PhysicsPolygon(Polygon polygon) {
		setPolygon(polygon);
	}

	public Polygon getPolygon() {
		return polygon;
	}

	public void setPolygon(Polygon polygon) {
		this.polygon = polygon;
	}

	public int getPoints() {
		return polygon.getPointCount();
	}

}
