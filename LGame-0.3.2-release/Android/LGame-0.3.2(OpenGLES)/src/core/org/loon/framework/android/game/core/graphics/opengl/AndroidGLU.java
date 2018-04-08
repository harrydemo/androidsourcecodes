package org.loon.framework.android.game.core.graphics.opengl;


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
 * @email ceponline@yahoo.com.cn
 * @version 0.1
 */
class AndroidGLU implements GLU {	
	
	 public void gluLookAt (GL10 gl, float eyeX, float eyeY, float eyeZ, float centerX, float centerY, float centerZ,
		float upX, float upY, float upZ) {
		android.opengl.GLU.gluLookAt(((AndroidGL10)gl).gl, eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
	}

	 public void gluOrtho2D (GL10 gl, float left, float right, float bottom, float top) {
		android.opengl.GLU.gluOrtho2D(((AndroidGL10)gl).gl, left, right, bottom, top);
	}

	 public void gluPerspective (GL10 gl, float fovy, float aspect, float zNear, float zFar) {
		android.opengl.GLU.gluPerspective(((AndroidGL10)gl).gl, fovy, aspect, zNear, zFar);
	}

	 public boolean gluProject (float objX, float objY, float objZ, float[] model, int modelOffset, float[] project,
		int projectOffset, int[] view, int viewOffset, float[] win, int winOffset) {
		int result = android.opengl.GLU.gluProject(objX, objY, objZ, model, modelOffset, project, projectOffset, view, viewOffset, win, winOffset);
		return result == GL10.GL_TRUE;
	}

	 public boolean gluUnProject (float winX, float winY, float winZ, float[] model, int modelOffset, float[] project,
		int projectOffset, int[] view, int viewOffset, float[] obj, int objOffset) {
		int result = android.opengl.GLU.gluUnProject(winX, winY, winZ, model, modelOffset, project, projectOffset, view, viewOffset, obj, objOffset);
		return result == GL10.GL_TRUE;
	}

}
