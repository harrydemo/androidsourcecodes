package org.loon.framework.javase.game.core.graphics.opengl.particle;

import org.loon.framework.javase.game.core.geom.Vector2f;
import org.loon.framework.javase.game.core.graphics.opengl.GL;
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
public class AlphaOneParticleSystem extends ParticleSystem {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AlphaOneParticleSystem(int howManyEffects, String textureFileName) {
		super(howManyEffects, textureFileName);
	}

	protected void initializeConstants() {
		this.minInitialSpeed = 40;
		this.maxInitialSpeed = 500;
		this.minAcceleration = 0;
		this.maxAcceleration = 0;
		this.minLifetime = 0.5f;
		this.maxLifetime = 0.8f;
		this.minScale = 0.4f;
		this.maxScale = 1.0f;
		this.minNumParticles = 15;
		this.maxNumParticles = 20;
		this.minRotationSpeed = -MathUtils.PI_OVER2;
		this.maxRotationSpeed = MathUtils.PI_OVER4;
		this.spriteBlendMode = GL.MODE_ALPHA_ONE;
	}

	protected void initializeParticle(Particle p, Vector2f where) {
		super.initializeParticle(p, where);
		Vector2f newVel = p.velocity.scale(-1f);
		newVel.x /= p.lifetime;
		newVel.y /= p.lifetime;
	}
}
