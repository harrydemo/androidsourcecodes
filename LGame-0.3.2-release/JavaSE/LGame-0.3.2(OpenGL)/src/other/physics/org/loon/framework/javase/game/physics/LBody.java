package org.loon.framework.javase.game.physics;

import java.util.ArrayList;

import org.loon.framework.javase.game.core.geom.Vector2f;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

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
public class LBody {

	private Body jboxBody;

	private BodyDef jboxBodyDef;

	private boolean staticBody;

	private ArrayList<Body> touching = new ArrayList<Body>();

	private LShape shape;

	private Object userData;

	public LBody(LShape shape, float x, float y) {
		this(shape, x, y, false);
	}

	public LBody(LShape shape, float x, float y, boolean staticBody) {
		this.jboxBodyDef = new BodyDef();
		this.jboxBodyDef.position.set(new Vector2f(x, y));
		this.staticBody = staticBody;
		this.shape = shape;
	}

	public boolean isStatic() {
		return this.staticBody;
	}

	public Object getUserData() {
		return this.userData;
	}

	public void setUserData(Object object) {
		this.userData = object;
	}

	public boolean isTouching(Body other) {
		return this.touching.contains(other);
	}

	public int touchCount(Body other) {
		int count = 0;
		for (int i = 0; i < this.touching.size(); i++) {
			if (this.touching.get(i) == other) {
				count++;
			}
		}
		return count;
	}

	void touch(Body other) {
		this.touching.add(other);
	}

	void untouch(Body other) {
		this.touching.remove(other);
	}

	public void applyForce(float x, float y) {
		checkBody();
		this.jboxBody.applyForce(new Vector2f(x, y), new Vector2f(0.0F, 0.0F));
	}

	public float getX() {
		checkBody();
		return this.jboxBody.getPosition().x;
	}

	public float getY() {
		checkBody();
		return this.jboxBody.getPosition().y;
	}

	public float getRotation() {
		checkBody();
		return this.jboxBody.getAngle();
	}

	public float getXVelocity() {
		checkBody();
		return this.jboxBody.getLinearVelocity().x;
	}

	public float getYVelocity() {
		checkBody();
		return this.jboxBody.getLinearVelocity().y;
	}

	public float getAngularVelocity() {
		checkBody();
		return this.jboxBody.getAngularVelocity();
	}

	public void setRestitution(float rest) {
		this.shape.setRestitution(rest);
	}

	public void setFriction(float f) {
		this.shape.setFriction(f);
	}

	public void setDensity(float den) {
		this.shape.setDensity(den);
	}

	void addToWorld(LWorld world) {
		World jboxWorld = world.getBox2DWorld();
		this.jboxBody = jboxWorld.createBody(this.jboxBodyDef);
		this.shape.createInBody(this);
		if (!this.staticBody) {
			this.jboxBody.setType(BodyType.StaticBody);
		} else {
			this.jboxBody.setType(BodyType.KinematicBody);
		}
	}

	public void removeFromWorld(LWorld world) {
		World jboxWorld = world.getBox2DWorld();
		jboxWorld.destroyBody(this.jboxBody);
	}

	public Body getBox2DBody() {
		return this.jboxBody;
	}

	public LShape getLShape() {
		return this.shape;
	}

	public void setPosition(float x, float y) {
		checkBody();
		jboxBody.setTransform(new Vector2f(x, y), this.jboxBody.getAngle());
	}

	public void setRotation(float rotation) {
		checkBody();
		jboxBody.setTransform(jboxBody.getPosition(), this.jboxBody.getAngle());
	}

	private void checkBody() {
		if (this.jboxBody == null)
			throw new RuntimeException("This Box2D-Body is NULL !");
	}

	public boolean isSleeping() {
		checkBody();
		return this.jboxBody.isSleepingAllowed();
	}

	public void translate(float x, float y) {
		setPosition(getX() + x, getY() + y);
	}

	public void setDamping(float damping) {
		if (this.jboxBody == null)
			this.jboxBodyDef.linearDamping = damping;
	}

	public void setVelocity(float xVelocity, float yVelocity) {
		checkBody();
		Vector2f vel = jboxBody.getLinearVelocity();
		vel.x = xVelocity;
		vel.y = yVelocity;
		this.jboxBody.setLinearVelocity(vel);
	}

	public void setAngularVelocity(float vel) {
		checkBody();
		this.jboxBody.setAngularVelocity(vel);
	}
}
