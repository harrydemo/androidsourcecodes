package org.loon.framework.javase.game.core.graphics.opengl.particle;

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
public class AlphaParticleSystem extends ParticleSystem {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AlphaParticleSystem(int howManyEffects, String textureFileName) {
		super(howManyEffects, textureFileName);
	}

	protected void initializeConstants() {
		this.minInitialSpeed = 20;
		this.maxInitialSpeed = 200;
		this.minAcceleration = -10;
		this.maxAcceleration = -50;
		this.minLifetime = 1.0f;
		this.maxLifetime = 2.5f;
		this.minScale = 1.0f;
		this.maxScale = 1.5f;
		this.minNumParticles = 5;
		this.maxNumParticles = 10;
		this.minRotationSpeed = -MathUtils.PI_OVER4;
		this.maxRotationSpeed = MathUtils.PI_OVER4;
		this.spriteBlendMode = GL.MODE_NORMAL;
	}
}
