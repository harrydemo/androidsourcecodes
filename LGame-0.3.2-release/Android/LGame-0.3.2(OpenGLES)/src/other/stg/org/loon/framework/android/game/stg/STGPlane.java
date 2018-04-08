package org.loon.framework.android.game.stg;

import java.util.Hashtable;

import org.loon.framework.android.game.stg.effect.Picture;
import org.loon.framework.android.game.core.geom.RectBox;
import org.loon.framework.android.game.core.graphics.LFont;
import org.loon.framework.android.game.core.graphics.opengl.GLColor;
import org.loon.framework.android.game.core.timer.LTimer;

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
public class STGPlane {

	boolean view = false;

	boolean animation = false;

	public int posX = 0;

	public int posY = 0;

	int animeNo;

	int[] animeList;

	int planeMode = 0;

	float scaleX = 1;

	float scaleY = 1;

	RectBox rect;

	Hashtable<Integer, Integer> images = new Hashtable<Integer, Integer>();

	LTimer delay = new LTimer(100);

	String str;

	LFont font;

	GLColor color;

	Picture draw;

}
