package org.loon.framework.android.game.physics;

import java.util.ArrayList;

import com.badlogic.gdx.physics.box2d.FixtureDef;

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
public interface LShape {
	
	public abstract LBody getLBody();

	public abstract void createInBody(LBody b);

	public abstract void setRestitution(float r);

	public abstract void setFriction(float f);

	public abstract void setDensity(float d);

	public abstract ArrayList<FixtureDef> getBox2DFixtures();

}
