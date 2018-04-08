package com.threed.jpct.games.alienrunner;

import com.threed.jpct.*;

/**
 * A particle is a client only object for doing special effects like impacts,
 * trails...
 */
public class Particle extends Object3D {

	static final private long serialVersionUID = 1L;

	private final static float NO_MAX_Y = 9999999999999999f;
	private static Object3D PLANE = null;
	private static final SimpleVector GRAV = new SimpleVector(0, 0.01, 0);

	private static float[] adds = new float[200];

	static {
		for (int p = 0; p < adds.length; p++) {
			float y = 0;
			float yAdd = GRAV.y;
			float velY = 0;
			for (int i = 0; i < p; i++) {
				velY += yAdd;
				y += velY;
			}
			adds[p] = y;
		}
	}

	private SimpleVector vel = new SimpleVector();
	private String texture = null;
	private long time = 0;
	private long maxTime = -1;
	private float maxY = NO_MAX_Y;
	private int checkCount = 0;
	private long lastMoved = 0;
	private int speed = 0;

	/**
	 * static initializer to create a PLANE-blueprint.
	 */
	static {
		PLANE = new Object3D(1);
		PLANE.addTriangle(new SimpleVector(-1, -1, 0), 0, 0, new SimpleVector(1, -1, 0), 1, 0, new SimpleVector(1, 1, 0), 1, 1);
		PLANE.getMesh().compress();
		PLANE.invert();
		PLANE.build();
	}

	/**
	 * Creates a new particle. The texture will be "particle" and to exists in
	 * the TextureManager.
	 */
	public Particle() {
		super(PLANE, true);
		setBillboarding(Object3D.BILLBOARDING_ENABLED);
		setVisibility(Object3D.OBJ_VISIBLE);
		setTransparency(10);
		setAdditionalColor(RGBColor.WHITE);
		setLighting(Object3D.LIGHTING_NO_LIGHTS);
		enableLazyTransformations();
		reset(Ticker.getTime());
		shareCompiledData(PLANE);
		strip();
		build();
	}

	/**
	 * Sets the texture. This overrides setTexture in Object3D to keep an
	 * internal reference to the texture's name.
	 * 
	 * @param texture
	 *            String the texture's name like the TextureManager knows it.
	 */
	public void setTexture(String texture) {
		if (this.texture == null || !this.texture.equals(texture)) {
			super.setTexture(texture);
			this.texture = texture;
		}
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	/**
	 * Sets the time in ms this particle should live.
	 * 
	 * @param time
	 *            long the time
	 */
	public void setLifeTime(long time) {
		this.maxTime = time;
	}

	public void setColor(RGBColor col) {
		setAdditionalColor(col);
	}

	/**
	 * Sets the y-limit, i.e. how deep the particle may fall before being
	 * discarded.
	 * 
	 * @param limit
	 *            float the y-limit
	 */
	public void setYLimit(float limit) {
		this.maxY = limit;
	}

	/**
	 * Sets the velocity for the particle.
	 * 
	 * @param vel
	 *            SimpleVector
	 */
	public void setVelocity(SimpleVector vel) {
		this.vel.set(vel);
	}

	/**
	 * Resets the particle.
	 */
	public void reset(long time) {
		this.time = time;
		clearTranslation();
		checkCount = 0;
	}

	/**
	 * Determines is the particle is "old". This has nothing to do with the life
	 * time. The time after that a particle is considered to be old is
	 * hard-wired to 20 sec. here.
	 * 
	 * @return boolean is it old?
	 */
	public boolean isOld() {
		return (Ticker.hasPassed(lastMoved, 20000));
	}

	/**
	 * Move the particle according to it's velocity and such. If it exceed its
	 * life time or its y-limit, the particle will be set to invisible.
	 * 
	 * @param ticks
	 *            long the number of ticks passed since the last call
	 */
	public void move(long ticks) {
		ticks *= speed;

		// Optimized version with y-only gravity.
		float yAdd = GRAV.y;
		float velY = vel.y;
		float x = ticks * vel.x;
		float y = 0;
		float z = ticks * vel.z;

		if (ticks >= adds.length) {
			// Up to 200, this isn't needed...should widely sufficient
			for (int i = 0; i < ticks; i++) {
				velY += yAdd;
				y += velY;
			}
		} else {
			// Use precalculated values...i.e. optimized to the death...
			velY += yAdd * ticks;
			y += adds[(int) ticks] + (ticks * vel.y);
		}
		vel.y = velY;
		translate(x, y, z);
		checkCount += ticks;
		lastMoved = Ticker.getTime();
		if ((maxTime != -1 && lastMoved - time > maxTime) || (checkCount > 10 && maxY != NO_MAX_Y && getTranslation().y >= maxY)) {
			//reset();
			setVisibility(Object3D.OBJ_INVISIBLE);
		}
	}
}
