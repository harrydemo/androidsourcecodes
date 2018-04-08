package org.loon.framework.javase.game.core.graphics.opengl;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

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
class LWjglGLU implements GLU {

	private FloatBuffer modelb;

	private FloatBuffer projectb;

	private IntBuffer viewb;

	private FloatBuffer winb;

	public LWjglGLU() {
		modelb = BufferUtils.createFloatBuffer(16);
		projectb = BufferUtils.createFloatBuffer(16);
		viewb = BufferUtils.createIntBuffer(4);
		winb = BufferUtils.createFloatBuffer(3);
	}

	public void gluLookAt(GL10 gl, float eyeX, float eyeY, float eyeZ,
			float centerX, float centerY, float centerZ, float upX, float upY,
			float upZ) {
		LWjglGLUs.gluLookAt(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX,
				upY, upZ);
	}

	public void gluOrtho2D(GL10 gl, float left, float right, float bottom,
			float top) {
		LWjglGLUs.gluOrtho2D(left, right, bottom, top);
	}

	public void gluPerspective(GL10 gl, float fovy, float aspect, float zNear,
			float zFar) {
		LWjglGLUs.gluPerspective(fovy, aspect, zNear, zFar);
	}

	public boolean gluProject(float objX, float objY, float objZ,
			float[] model, int modelOffset, float[] project, int projectOffset,
			int[] view, int viewOffset, float[] win, int winOffset) {
		modelb.clear();
		modelb.put(model, modelOffset, 16);
		projectb.clear();
		projectb.put(project, projectOffset, 16);
		viewb.clear();
		viewb.put(view, viewOffset, 4);
		winb.clear();

		boolean result = LWjglGLUs.gluProject(objX, objY, objZ, modelb,
				projectb, viewb, winb);
		win[winOffset] = winb.get(0);
		win[winOffset + 1] = winb.get(1);
		win[winOffset + 2] = winb.get(2);
		return result;
	}

	public boolean gluUnProject(float winX, float winY, float winZ,
			float[] model, int modelOffset, float[] project, int projectOffset,
			int[] view, int viewOffset, float[] obj, int objOffset) {
		modelb.clear();
		modelb.put(model, modelOffset, 16);
		projectb.clear();
		projectb.put(project, projectOffset, 16);
		viewb.clear();
		viewb.put(view, viewOffset, 4);
		winb.clear();

		boolean result = LWjglGLUs.gluUnProject(winX, winY, winZ, modelb,
				projectb, viewb, winb);
		obj[objOffset] = winb.get(0);
		obj[objOffset + 1] = winb.get(1);
		obj[objOffset + 2] = winb.get(2);
		return result;
	}
}
