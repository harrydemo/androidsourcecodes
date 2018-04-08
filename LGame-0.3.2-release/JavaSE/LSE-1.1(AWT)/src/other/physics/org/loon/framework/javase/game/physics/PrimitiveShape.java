package org.loon.framework.javase.game.physics;

import java.util.ArrayList;


import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;

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
public abstract class PrimitiveShape implements LShape {

	protected ArrayList<FixtureDef> jboxFixtures = new ArrayList<FixtureDef>();

	protected Fixture jboxFixture;

	protected PolygonDef def;

	protected LBody body;

	protected PrimitiveShape(PolygonDef def) {
		this.def = def;
	}

	public void createInBody(LBody body) {
		this.body = body;
		this.jboxFixture = body.getBox2DBody().createShape(this.def);
		this.jboxFixtures.add(this.def);
	}

	public ArrayList<FixtureDef> getBox2DFixtures() {
		return this.jboxFixtures;
	}

	public LBody getLBody() {
		return this.body;
	}

	public void setDensity(float density) {
		if (this.jboxFixture == null) {
			this.def.density = density;
		} else {
			this.jboxFixture.setDensity(density);
		}
	}

	public void setFriction(float friction) {
		if (this.jboxFixture == null) {
			this.def.friction = friction;
		} else {
			this.jboxFixture.setFriction(friction);
		}
	}

	public void setRestitution(float rest) {
		if (this.jboxFixture == null) {
			this.def.restitution = rest;
		} else {
			this.jboxFixture.setRestitution(rest);
		}
	}
}
