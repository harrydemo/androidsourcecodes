package org.loon.framework.android.game.action.collision;

import org.loon.framework.android.game.core.geom.Shape;
import org.loon.framework.android.game.core.graphics.opengl.LTexture;

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
public class CollisionEvent {

	private Collidable collidedWith;

	public CollisionEvent(final LTexture texture, final Object ref) {

		this.collidedWith = new Collidable() {

			private CollisionMask cmask;

			private Shape shape;

			public Object getRef() {
				return ref;
			}

			public CollisionMask getMask() {
				if (cmask != null) {
					return cmask;
				}
				return (cmask = new CollisionMask(texture.getMask()));
			}

			public Shape getShape() {
				if (shape != null) {
					return shape;
				}
				return (shape = texture.getShape());
			}

		};
	}

	public Collidable getCollidable() {
		return collidedWith;
	}

}
