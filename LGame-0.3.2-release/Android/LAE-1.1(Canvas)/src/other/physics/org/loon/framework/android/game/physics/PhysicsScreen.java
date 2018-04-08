package org.loon.framework.android.game.physics;

import java.util.ArrayList;

import org.loon.framework.android.game.action.sprite.ISprite;
import org.loon.framework.android.game.core.LSystem;
import org.loon.framework.android.game.core.geom.RectBox;
import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.core.graphics.Screen;
import org.loon.framework.android.game.core.graphics.device.LGraphics;
import org.loon.framework.android.game.core.timer.LTimerContext;
import org.loon.framework.android.game.core.timer.SystemTimer;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.KeyEvent;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointDef;
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
public abstract class PhysicsScreen extends Screen implements ContactListener,
		PolygonType, Runnable {

	public static final int VELOCITY_ITERATIONS_DEFAULT = 10;

	public static final int POSITION_ITERATIONS_DEFAULT = 10;

	public static final float TIME_ITERATIONS_DEFAULT = 0.5F;

	public static final int WORLD_SCALE = 10;

	public static final int WORLD_WAIT = 500;

	private ArrayList<PhysicsObject> objects = new ArrayList<PhysicsObject>();

	protected int velocityIters = VELOCITY_ITERATIONS_DEFAULT;

	protected int positionIters = POSITION_ITERATIONS_DEFAULT;

	protected float timerIters = TIME_ITERATIONS_DEFAULT;

	protected static int scaleSize = WORLD_SCALE;

	protected static World world;

	private Vector2 gravity;

	private WorldBox worldBox;

	private Thread physicsThread;

	private boolean useContactListener, running;

	private long physicsFPS;

	public PhysicsScreen() {
		this(0, 0, true, VELOCITY_ITERATIONS_DEFAULT,
				POSITION_ITERATIONS_DEFAULT);
	}

	public PhysicsScreen(int velocityIterations, int positionIterations) {
		this(0, 0, true, velocityIterations, positionIterations);
	}

	public PhysicsScreen(float gx, float gy, boolean doSleep) {
		this(new Vector2(gx, gy), doSleep, VELOCITY_ITERATIONS_DEFAULT,
				POSITION_ITERATIONS_DEFAULT);
	}

	public PhysicsScreen(float gx, float gy, boolean doSleep,
			int velocityIterations, int positionIterations) {
		this(new Vector2(gx, gy), doSleep, velocityIterations,
				positionIterations);
	}

	public PhysicsScreen(Vector2 gravity, boolean doSleep,
			int velocityIterations, int positionIterations) {
		super();
		PhysicsScreen.world = new World(this.gravity = gravity, true);
		this.worldBox = new WorldBox(world, new RectBox(0, 0, getWidth(),
				getHeight()));
		this.velocityIters = velocityIterations;
		this.positionIters = positionIterations;
		this.running = true;
		if (LSystem.isEmulator()) {
			this.physicsFPS = LSystem.DEFAULT_MAX_FPS / 2 - 10;
		} else {
			this.physicsFPS = LSystem.DEFAULT_MAX_FPS / 2 - 5;
		}
		this.physicsThread = new Thread(this);
		this.physicsThread.start();
	}

	public void begineContactListener() {
		synchronized (objects) {
			PhysicsScreen.world.setContactListener(this);
			this.useContactListener = true;
		}
	}

	public void endContactListener() {
		synchronized (objects) {
			PhysicsScreen.world.setContactListener(null);
			this.useContactListener = false;
		}
	}

	public PhysicsObject find(int x, int y) {
		synchronized (objects) {
			if (objects.size() == 0) {
				return null;
			}
			int size = objects.size() - 1;
			for (int i = size; i >= 0; i--) {
				PhysicsObject child = objects.get(i);
				RectBox rect = child.getCollisionBox();
				if (rect != null && rect.contains(x, y)) {
					return child;
				}
			}
			return null;
		}
	}

	public PhysicsObject find(int x, int y, Object tag) {
		synchronized (objects) {
			if (objects.size() == 0) {
				return null;
			}
			int size = objects.size() - 1;
			for (int i = size; i >= 0; i--) {
				PhysicsObject child = objects.get(i);
				if (child.getTag() == tag) {
					RectBox rect = child.getCollisionBox();
					if (rect != null && rect.contains(x, y)) {
						return child;
					}
				}
			}
			return null;
		}
	}

	public PhysicsObject bindTo(String fileName, Body body) {
		synchronized (objects) {
			PhysicsObject o = new PhysicsObject(fileName, body);
			addObject(o);
			return o;
		}
	}

	public PhysicsObject bindTo(Bitmap bitmap, Body body) {
		synchronized (objects) {
			PhysicsObject o = new PhysicsObject(bitmap, body);
			addObject(o);
			return o;
		}
	}

	public PhysicsObject bindTo(ISprite spr, int poly, BodyType type) {
		synchronized (objects) {
			PhysicsObject o = new PhysicsObject(spr, poly, type);
			addObject(o);
			return o;
		}
	}

	public PhysicsObject bindTo(ISprite spr, BodyType type) {
		synchronized (objects) {
			PhysicsObject o = new PhysicsObject(spr, type);
			addObject(o);
			return o;
		}
	}

	public PhysicsObject bindTo(String fileName, int poly, BodyType type,
			int x, int y) {
		synchronized (objects) {
			PhysicsObject o = new PhysicsObject(fileName, poly, type, x, y);
			addObject(o);
			return o;
		}
	}

	public PhysicsObject bindTo(String fileName, BodyType type, int x, int y) {
		synchronized (objects) {
			PhysicsObject o = new PhysicsObject(fileName, type, x, y);
			addObject(o);
			return o;
		}
	}

	public PhysicsObject bindTo(LImage img, int poly, BodyType type, int x,
			int y) {
		synchronized (objects) {
			PhysicsObject o = new PhysicsObject(img, poly, type, x, y);
			addObject(o);
			return o;
		}
	}

	public PhysicsObject bindTo(LImage img, BodyType type, int x, int y) {
		synchronized (objects) {
			PhysicsObject o = new PhysicsObject(img, type, x, y);
			addObject(o);
			return o;
		}
	}

	public PhysicsObject bindTo(Bitmap img, int poly, BodyType type, int x,
			int y) {
		synchronized (objects) {
			PhysicsObject o = new PhysicsObject(img, poly, type, x, y);
			addObject(o);
			return o;
		}
	}

	public PhysicsObject bindTo(Bitmap img, BodyType type, int x, int y) {
		synchronized (objects) {
			PhysicsObject o = new PhysicsObject(img, type, x, y);
			addObject(o);
			return o;
		}
	}

	public PhysicsObject bindTo(int polyType, int poly, BodyType type, int x,
			int y, int w, int h) {
		synchronized (objects) {
			PhysicsObject o = new PhysicsObject(polyType, poly, type, x, y, w,
					h);
			addObject(o);
			return o;
		}
	}

	public PhysicsObject bindTo(int polyType, BodyType type, int x, int y,
			int w, int h) {
		synchronized (objects) {
			PhysicsObject o = new PhysicsObject(polyType, type, x, y, w, h);
			addObject(o);
			return o;
		}
	}

	public boolean addObject(PhysicsObject o) {
		synchronized (objects) {
			return objects.add(o);
		}
	}

	public void removeObject(PhysicsObject o) {
		synchronized (objects) {
			objects.remove(o);
			o.dispose();
		}
	}

	public void run() {
		SystemTimer timer = LSystem.getSystemTimer();
		Thread currentThread = Thread.currentThread();
		long lastTimeMicros = timer.getTimeMicros();
		do {
			if (this.isPaused()) {
				try {
					Thread.sleep(WORLD_WAIT);
				} catch (InterruptedException ex) {
				}
				lastTimeMicros = timer.getTimeMicros();
				elapsedTime = 0;
				continue;
			}
			synchronized (objects) {
				if (world == null) {
					try {
						Thread.sleep(WORLD_WAIT);
					} catch (InterruptedException e) {
					}
					continue;
				}
				world.step(timerIters, velocityIters, positionIters);
			}
			long goalTimeMicros = lastTimeMicros + 1000000L / physicsFPS;
			lastTimeMicros = timer.sleepTimeMicros(goalTimeMicros);
		} while ((running && physicsThread == currentThread));
	}

	public void dispose() {
		synchronized (objects) {
			super.dispose();
			try {
				physicsThread.interrupt();
				running = false;
				physicsThread = null;
				if (world != null) {
					world.dispose();
					world = null;
				}
			} catch (Exception e) {
			}
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent e) {
		return true;
	}

	public boolean onKeyUp(int keyCode, KeyEvent e) {
		return true;
	}

	final public void onTouchDown(final LTouch e) {
		Runnable runnable = new Runnable() {
			public void run() {
				synchronized (objects) {
					onDown(e);
				}
			}
		};
		callEvent(new Thread(runnable));
	}

	public abstract void onDown(LTouch e);

	final public void onTouchMove(final LTouch e) {
		onMove(e);
	}

	public abstract void onMove(LTouch e);

	final public void onTouchUp(final LTouch e) {
		onUp(e);
	}

	public abstract void onUp(LTouch e);

	public static Body createBody(BodyDef bodyDef) {
		return world.createBody(bodyDef);
	}

	public static void destroyBody(Body body) {
		world.destroyBody(body);
	}

	public static Joint createJoint(JointDef def) {
		return world.createJoint(def);
	}

	public static void destroyJoint(Joint joint) {
		world.destroyJoint(joint);
	}

	public static World getWorld() {
		return world;
	}

	public void setGravity(int x, int y) {
		gravity.set(x, y);
		world.setGravity(gravity);
	}

	public Vector2 getGravity() {
		return gravity;
	}

	final public void draw(LGraphics g) {
		int size = objects.size();
		for (int i = size - 1; i >= 0; i--) {
			PhysicsObject o = objects.get(i);
			o.update(elapsedTime);
			o.createUI(g);
		}
		paint(g);
	}

	public abstract void paint(LGraphics g);

	final public void alter(LTimerContext t) {

		update(t);
	}

	public abstract void update(LTimerContext t);

	public void beginContact(Contact contact) {

	}

	public void endContact(Contact contact) {
		synchronized (objects) {
			try {
				Fixture fixture1 = contact.getFixtureA();
				Fixture fixture2 = contact.getFixtureB();

				Body body1 = fixture1.getBody();
				Body body2 = fixture2.getBody();

				PhysicsObject object1 = (PhysicsObject) body1.getUserData();
				PhysicsObject object2 = (PhysicsObject) body2.getUserData();

				if (object1 != null) {
					object1.onCollision();
				}
				if (object2 != null) {
					object2.onCollision();
				}

				onCollisionEvent(new CollisionEvent(body1, body2));
			} catch (Exception ex) {
				Log.d("PhysicsScreen", "Contact", ex);
			}
		}
	}

	public void onCollisionEvent(CollisionEvent e) {

	}

	public int getPositionIterations() {
		return this.positionIters;
	}

	public void setPositionIterations(final int positionIterations) {
		this.positionIters = positionIterations;
	}

	public int getVelocityIterations() {
		return this.velocityIters;
	}

	public void setVelocityIterations(final int velocityIterations) {
		this.velocityIters = velocityIterations;
	}

	public float getTimerIters() {
		return timerIters;
	}

	public void setTimerIters(float timerIters) {
		this.timerIters = timerIters;
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

	public void setRunning(boolean isRunning) {
		this.running = isRunning;
	}

	public boolean isRunning() {
		return running;
	}

	public long getPhysicsFPS() {
		return physicsFPS;
	}

	public void setPhysicsFPS(long physicsFPS) {
		this.physicsFPS = physicsFPS;
	}

	public boolean useContactListener() {
		return useContactListener;
	}

	public float getScaleSize() {
		return scaleSize;
	}

	public void setScaleSize(int scaleSize) {
		PhysicsScreen.scaleSize = scaleSize;
	}

	public static int screenToWorld(int size) {
		return size / scaleSize;
	}

	public static int worldToScreen(int size) {
		return size * scaleSize;
	}

	public static float screenToWorld(float size) {
		return size / scaleSize;
	}

	public static float worldToScreen(float size) {
		return size * scaleSize;
	}

}
