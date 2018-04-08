package org.loon.framework.javase.game.core.graphics.opengl.particle;

import org.loon.framework.javase.game.core.geom.Vector2f;
import org.loon.framework.javase.game.utils.MathUtils;

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
public class Particle {

	public Vector2f position;

	public Vector2f velocity;

	public Vector2f acceleration;

	public float lifetime;

	public float timeSinceStart;

	public float scale;

	public float rotation;

	public float rotationSpeed;

	public boolean isActive() {
		return timeSinceStart < lifetime;
	}

	public void initialize(Vector2f position, Vector2f velocity,
			Vector2f acceleration, float lifetime, float scale,
			float rotationSpeed) {
		this.position = position;
		this.velocity = velocity;
		this.acceleration = acceleration;
		this.lifetime = lifetime;
		this.scale = scale;
		this.rotationSpeed = rotationSpeed;
		this.timeSinceStart = 0.0f;
		this.rotation = ParticleSystem.getRandomBetween(0, MathUtils.TWO_PI);
	}

	public void update(long elapsedTime) {
		float dt = (float) elapsedTime / 10000;
		velocity.add(acceleration.scale(dt));
		position.add(velocity.scale(dt));
		rotation += rotationSpeed * dt;
		timeSinceStart += dt;
	}
}
