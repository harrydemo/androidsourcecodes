package org.loon.framework.javase.game.physics;

import java.util.ArrayList;

import org.loon.framework.javase.game.core.LSystem;
import org.loon.framework.javase.game.core.geom.RectBox;
import org.loon.framework.javase.game.core.geom.Vector2f;
import org.loon.framework.javase.game.core.graphics.Screen;
import org.loon.framework.javase.game.core.graphics.opengl.GLEx;
import org.loon.framework.javase.game.core.graphics.opengl.LTexture;
import org.loon.framework.javase.game.core.input.LTouch;
import org.loon.framework.javase.game.core.timer.LTimerContext;
import org.loon.framework.javase.game.core.timer.SystemTimer;

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

	private Vector2f gravity;

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
		this(new Vector2f(gx, gy), doSleep, VELOCITY_ITERATIONS_DEFAULT,
				POSITION_ITERATIONS_DEFAULT);
	}

	public PhysicsScreen(float gx, float gy, boolean doSleep,
			int velocityIterations, int positionIterations) {
		this(new Vector2f(gx, gy), doSleep, velocityIterations,
				positionIterations);
	}

	public PhysicsScreen(Vector2f gravity, boolean doSleep,
			int velocityIterations, int positionIterations) {
		super();
		PhysicsScreen.world = new World(this.gravity = gravity, true);
		this.worldBox = new WorldBox(world, new RectBox(0, 0, getWidth(),
				getHeight()));
		this.velocityIters = velocityIterations;
		this.positionIters = positionIterations;
	}

	public void onCreate(int width, int height) {
		super.onCreate(width, height);
		this.running = true;
		this.physicsFPS = 20;
		this.physicsThread = new Thread(this);
		this.physicsThread.setPriority(Thread.NORM_PRIORITY);
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

	public PhysicsObject bindTo(LTexture bitmap, Body body) {
		synchronized (objects) {
			PhysicsObject o = new PhysicsObject(bitmap, body);
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

	public PhysicsObject bindTo(LTexture bitmap, BodyType type, int x, int y) {
		synchronized (objects) {
			PhysicsObject o = new PhysicsObject(bitmap, type, x, y);
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

	final public void touchDown(final LTouch e) {
		Runnable runnable = new Runnable() {
			public void run() {
				synchronized (objects) {
					onDown(e);
				}
			}
		};
		callEvent(runnable);
	}

	public abstract void onDown(LTouch e);

	final public void touchMove(final LTouch e) {
		onMove(e);
	}

	public abstract void onMove(LTouch e);

	final public void touchUp(final LTouch e) {
		onUp(e);
	}

	public abstract void onUp(LTouch e);

	final public void touchDrag(final LTouch e) {
		onDrag(e);
	}

	public abstract void onDrag(LTouch e);

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

	public Vector2f getGravity() {
		return gravity;
	}

	final public void draw(GLEx g) {
		int size = objects.size();
		for (int i = size - 1; i >= 0; i--) {
			PhysicsObject o = objects.get(i);
			o.update(elapsedTime);
			o.createUI(g);
		}
		paint(g);
	}

	public abstract void paint(GLEx g);

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
				throw new RuntimeException("Contact:" + ex.getMessage());
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

	public void dispose() {
		synchronized (objects) {
			super.dispose();
			try {
				if (physicsThread != null) {
					try {
						running = false;
						physicsThread.interrupt();
						physicsThread = null;
					} catch (Exception e) {
					}
				}
				if (world != null) {
					world.dispose();
					world = null;
				}
			} catch (Exception e) {
			}
		}
	}

}
