package org.loon.framework.javase.game.physics;

import java.util.ArrayList;
import java.util.HashMap;

import org.loon.framework.javase.game.core.geom.RectBox;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
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
public class LWorld {

	private World jboxWorld;

	private ArrayList<LBody> bodies = new ArrayList<LBody>();

	private HashMap<FixtureDef, LBody> shapeMap = new HashMap<FixtureDef, LBody>();

	private ArrayList<LWorldListener> listeners = new ArrayList<LWorldListener>();

	private int iterations;

	private Vector2 gravity;

	private WorldBox worldBox;

	public LWorld(float gx, float gy, int width, int height, boolean doSleep,
			float iterations) {
		this.iterations = 10;
		this.jboxWorld = new World(this.gravity = new Vector2(gx, gy), true);
		this.worldBox = new WorldBox(jboxWorld,
				new RectBox(0, 0, width, height));
	}

	public void add(LBody body) {
		body.addToWorld(this);
		ArrayList<FixtureDef> shapes = body.getLShape().getBox2DFixtures();
		for (int i = 0; i < shapes.size(); i++) {
			this.shapeMap.put(shapes.get(i), body);
		}
		this.bodies.add(body);
	}

	public void remove(LBody body) {
		ArrayList shapes = body.getLShape().getBox2DFixtures();
		for (int i = 0; i < shapes.size(); i++) {
			this.shapeMap.remove(shapes.get(i));
		}
		body.removeFromWorld(this);
		this.bodies.remove(body);
	}

	public int getBodyCount() {
		return this.bodies.size();
	}

	public LBody getLBody(int index) {
		return (LBody) this.bodies.get(index);
	}

	public void update(float timeStep) {
		this.jboxWorld.setContinuousPhysics(true);
		this.jboxWorld.setWarmStarting(true);
		this.jboxWorld.step(timeStep, this.iterations, this.iterations);
	}

	public void addListener(LWorldListener listener) {
		this.listeners.add(listener);
	}

	public void removeListener(LWorldListener listener) {
		this.listeners.remove(listener);
	}

	public World getBox2DWorld() {
		return this.jboxWorld;
	}

	public RectBox getWorldBox() {
		return worldBox.getBox();
	}

	public void setWorldBox(RectBox box) {
		this.worldBox.setBox(box);
	}

	public void setWorldBox(int w, int h) {
		this.worldBox.setBox(new RectBox(0, 0, w, h));
	}

	public Vector2 getGravity() {
		return gravity;
	}

	public void setGravity(Vector2 gravity) {
		this.gravity = gravity;
	}
}
