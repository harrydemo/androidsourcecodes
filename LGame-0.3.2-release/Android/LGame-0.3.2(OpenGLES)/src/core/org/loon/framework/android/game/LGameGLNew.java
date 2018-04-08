package org.loon.framework.android.game;

import org.loon.framework.android.game.LGameAndroid2DView.GLType;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.SurfaceHolder;

/**
 * 
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
 * @version 0.1.1
 */
public class LGameGLNew extends GLSurfaceView {

	private SurfaceHolder holder;

	public LGameGLNew(Context context) {
		super(context);
		init();
	}

	public LGameGLNew(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		holder = GLType.updateSurfaceHolder(getHolder(), this);
	}

	public SurfaceHolder getSurfaceHolder() {
		return holder;
	}

}
