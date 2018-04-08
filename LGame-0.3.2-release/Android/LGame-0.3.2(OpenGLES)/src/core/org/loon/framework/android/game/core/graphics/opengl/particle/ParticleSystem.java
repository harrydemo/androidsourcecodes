package org.loon.framework.android.game.core.graphics.opengl.particle;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

import org.loon.framework.android.game.action.sprite.ISprite;
import org.loon.framework.android.game.core.LObject;
import org.loon.framework.android.game.core.LSystem;
import org.loon.framework.android.game.core.geom.RectBox;
import org.loon.framework.android.game.core.geom.Vector2f;
import org.loon.framework.android.game.core.graphics.opengl.GL;
import org.loon.framework.android.game.core.graphics.opengl.GLColor;
import org.loon.framework.android.game.core.graphics.opengl.GLEx;
import org.loon.framework.android.game.core.graphics.opengl.LTexture;
import org.loon.framework.android.game.core.graphics.opengl.GLEx.Direction;
import org.loon.framework.android.game.core.graphics.opengl.LTexture.Format;
import org.loon.framework.android.game.utils.MathUtils;

/**
 * Copyright 2008 - 2011
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
public abstract class ParticleSystem extends LObject implements ISprite {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LTexture texture;

	private Vector2f origin = new Vector2f();

	private int howManyEffects;

	public final int AlphaBlendDrawOrder = 100;

	public final int AdditiveDrawOrder = 200;

	protected int spriteBlendMode = GL.MODE_NORMAL;

	protected int minNumParticles;

	protected int maxNumParticles = 1;

	protected String textureFilename;

	protected float minInitialSpeed;

	protected float maxInitialSpeed;

	protected float minAcceleration;

	protected float maxAcceleration;

	protected float minRotationSpeed;

	protected float maxRotationSpeed;

	protected float minLifetime;

	protected float maxLifetime;

	protected float minScale;

	protected float maxScale;

	Format format = Format.REPEATING_BILINEAR;

	Particle[] particles;

	ArrayBlockingQueue<Particle> freeParticles;

	Direction direction = Direction.TRANS_NONE;

	boolean visible, close;

	float alpha = 1;

	protected ParticleSystem(String textureFileName) {
		this(1, textureFileName);
	}

	protected ParticleSystem(int howManyEffects, String textureFileName) {
		this.howManyEffects = howManyEffects;
		this.textureFilename = textureFileName;
		this.visible = true;
	}

	public void initialize() {
		initializeConstants();
		particles = new Particle[howManyEffects * maxNumParticles];
		freeParticles = new ArrayBlockingQueue<Particle>(howManyEffects
				* maxNumParticles);
		for (int i = 0; i < particles.length; i++) {
			particles[i] = new Particle();
			freeParticles.offer(particles[i]);
		}
	}

	protected abstract void initializeConstants();

	protected void loadContent() {
		texture = new LTexture(textureFilename, format);
		origin.x = texture.getWidth() / 2;
		origin.y = texture.getHeight() / 2;
	}

	public void addParticles(Vector2f where) {
		if (particles == null) {
			initialize();
		}
		Vector2f position = where.copy();
		int numParticles = LSystem.random.nextInt(maxNumParticles)
				+ minNumParticles;
		int freeCount = freeParticles.size();
		for (int i = 0; i < numParticles && freeCount > 0; i++, freeCount--) {
			Particle p = null;
			p = freeParticles.poll();
			initializeParticle(p, position);
		}
	}

	protected Vector2f pickRandomDirection() {
		float angle = getRandomBetween(0, MathUtils.TWO_PI);
		return new Vector2f((float) Math.cos(angle), (float) Math.sin(angle));
	}

	public static float getRandomBetween(float min, float max) {
		return min + (float) LSystem.random.nextDouble() * (max - min);
	}

	protected void initializeParticle(Particle p, Vector2f where) {
		Vector2f direction = pickRandomDirection();
		float velocity = getRandomBetween(minInitialSpeed, maxInitialSpeed);
		float acceleration = getRandomBetween(minAcceleration, maxAcceleration);
		float lifetime = getRandomBetween(minLifetime, maxLifetime);
		float scale = getRandomBetween(minScale, maxScale);
		float rotationSpeed = getRandomBetween(minRotationSpeed,
				maxRotationSpeed);
		Vector2f vel = new Vector2f(direction).scale(velocity);
		Vector2f accel = new Vector2f(direction).scale(acceleration);
		p.initialize(where, vel, accel, lifetime, scale, rotationSpeed);
	}

	public void update(long elapsedTime) {
		if (!visible || close) {
			return;
		}
		if (particles == null) {
			initialize();
		}
		if (texture == null) {
			loadContent();
		}
		for (Particle p : particles) {
			if (p.isActive()) {
				p.update(elapsedTime);
				if (!p.isActive()) {
					freeParticles.offer(p);
				}
			}
		}
	}

	public void createUI(GLEx g) {
		if (!visible || close) {
			return;
		}

		if (GLEx.gl != null && texture != null) {

			g.beginBlend(spriteBlendMode);

			if (alpha > 0.1 && alpha < 1) {
				g.setAlpha(alpha);
			}
			for (Particle p : particles) {

				if (!p.isActive()) {
					continue;
				}

				if (p.position == null) {
					return;
				}
				float normalizedLifetime = p.timeSinceStart / p.lifetime;
				float alpha = 4 * normalizedLifetime * (1 - normalizedLifetime);
				float scale = p.scale * (0.75f + 0.25f * normalizedLifetime);
				GLColor color = new GLColor(1f, 1f, 1f, alpha);
				g.drawTexture(texture, p.position, color, (float) Math
						.toDegrees(p.rotation), origin, scale, direction);
			}
			if (alpha > 0.1 && alpha < 1) {
				g.setAlpha(1.0F);
			}
			g.endBlend();
		}
	}

	public int getSpriteBlendMode() {
		return spriteBlendMode;
	}

	public void setSpriteBlendMode(int spriteBlendMode) {
		this.spriteBlendMode = spriteBlendMode;
	}

	public int freeParticleCount() {
		return freeParticles.size();
	}

	public final Random getRandom() {
		return LSystem.random;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public int getHeight() {
		return 0;
	}

	public int getWidth() {
		return 0;
	}

	public void setAlpha(float a) {
		this.alpha = a;
	}

	public float getAlpha() {
		return alpha;
	}

	public LTexture getBitmap() {
		return null;
	}

	public RectBox getCollisionBox() {
		return null;
	}

	public Format getFormat() {
		return format;
	}

	public void setFormat(Format format) {
		this.format = format;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void dispose() {
		if (texture != null) {
			texture.destroy();
			texture = null;
		}
		close = true;
	}

}
