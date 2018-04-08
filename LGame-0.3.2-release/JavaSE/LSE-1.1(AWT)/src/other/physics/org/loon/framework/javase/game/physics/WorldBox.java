package org.loon.framework.javase.game.physics;

import org.loon.framework.javase.game.core.geom.RectBox;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

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
public class WorldBox {

	private Body northBody, southBody, eastBody, westBody;

	private RectBox worldBox;

	private World world;

	private boolean init;

	public float density, friction, restitution;

	/**
	 * 这是一个物理世界范围用类，用以取代JBox2D中的AABB(gdx未提供)
	 * 
	 * @param world
	 * @param box
	 */
	public WorldBox(World world, RectBox box) {
		this.world = world;
		this.setBox(box);
		this.init = true;
		this.friction = 0.0f;
	}

	public synchronized void remove() {
		if (init) {
			world.destroyBody(northBody);
			world.destroyBody(southBody);
			world.destroyBody(eastBody);
			world.destroyBody(westBody);
		}
	}

	public void setBox(RectBox box) {
		if (init) {
			remove();
		}
		PolygonShape eastWestShape = new PolygonShape();
		eastWestShape.setAsBox(1.0f, box.getHeight());

		PolygonShape northSouthShape = new PolygonShape();
		northSouthShape.setAsBox(box.getWidth(), 0.0f);

		BodyDef northDef = new BodyDef();
		northDef.type = BodyDef.BodyType.StaticBody;
		northDef.position.set(new Vector2(0, 0));
		northBody = world.createBody(northDef);
		FixtureDef northFixture = new FixtureDef();
		northFixture.shape = northSouthShape;
		northFixture.density = density;
		northFixture.friction = friction;
		northFixture.restitution = restitution;
		northBody.createFixture(northFixture);

		BodyDef southDef = new BodyDef();
		southDef.type = BodyDef.BodyType.StaticBody;
		southDef.position.set(new Vector2(0, box.getHeight()));
		southBody = world.createBody(southDef);
		FixtureDef southFixture = new FixtureDef();
		southFixture.shape = northSouthShape;
		southFixture.density = density;
		southFixture.friction = friction;
		southFixture.restitution = restitution;
		southBody.createFixture(southFixture);

		BodyDef eastDef = new BodyDef();
		eastDef.type = BodyDef.BodyType.StaticBody;
		eastDef.position.set(new Vector2(box.getWidth(), 0));
		eastBody = world.createBody(eastDef);
		FixtureDef eastFixture = new FixtureDef();
		eastFixture.shape = eastWestShape;
		eastFixture.density = density;
		eastFixture.friction = friction;
		eastFixture.restitution = restitution;
		eastBody.createFixture(eastFixture);

		BodyDef westDef = new BodyDef();
		westDef.type = BodyDef.BodyType.StaticBody;
		westDef.position.set(new Vector2(0, 0));
		westBody = world.createBody(westDef);
		FixtureDef westFixture = new FixtureDef();
		westFixture.density = density;
		westFixture.friction = friction;
		westFixture.restitution = restitution;
		westFixture.shape = eastWestShape;
		westBody.createFixture(westFixture);

		eastWestShape.dispose();
		northSouthShape.dispose();

		this.worldBox = box;
	}

	public RectBox getBox() {
		return worldBox;
	}

	public float getDensity() {
		return density;
	}

	public void setDensity(float density) {
		this.density = density;
	}

	public Body getEastBody() {
		return eastBody;
	}

	public void setEastBody(Body eastBody) {
		this.eastBody = eastBody;
	}

	public float getFriction() {
		return friction;
	}

	public void setFriction(float friction) {
		this.friction = friction;
	}

	public Body getNorthBody() {
		return northBody;
	}

	public void setNorthBody(Body northBody) {
		this.northBody = northBody;
	}

	public float getRestitution() {
		return restitution;
	}

	public void setRestitution(float restitution) {
		this.restitution = restitution;
	}

	public Body getSouthBody() {
		return southBody;
	}

	public void setSouthBody(Body southBody) {
		this.southBody = southBody;
	}

	public Body getWestBody() {
		return westBody;
	}

	public void setWestBody(Body westBody) {
		this.westBody = westBody;
	}
}
