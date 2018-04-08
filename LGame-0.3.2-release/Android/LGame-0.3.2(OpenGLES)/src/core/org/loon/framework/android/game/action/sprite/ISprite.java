package org.loon.framework.android.game.action.sprite;

import java.io.Serializable;

import org.loon.framework.android.game.core.LRelease;
import org.loon.framework.android.game.core.geom.RectBox;
import org.loon.framework.android.game.core.graphics.opengl.GLEx;
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
public interface ISprite extends Serializable, LRelease {

	public static final int TYPE_FADE_IN = 0;

	public static final int TYPE_FADE_OUT = 1;

	public abstract int getWidth();

	public abstract int getHeight();

	public abstract float getAlpha();

	public abstract int x();

	public abstract int y();

	public abstract float getX();

	public abstract float getY();

	public abstract void setVisible(boolean visible);

	public abstract boolean isVisible();

	public abstract void createUI(GLEx g);

	public abstract void update(long elapsedTime);

	public abstract int getLayer();

	public abstract void setLayer(int layer);

	public abstract RectBox getCollisionBox();

	public abstract LTexture getBitmap();

}
