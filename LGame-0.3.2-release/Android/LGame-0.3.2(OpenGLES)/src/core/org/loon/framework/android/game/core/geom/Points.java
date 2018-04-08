package org.loon.framework.android.game.core.geom;

import java.nio.FloatBuffer;
import java.util.List;

import org.loon.framework.android.game.core.graphics.opengl.GL10;
import org.loon.framework.android.game.core.graphics.opengl.GLColor;
import org.loon.framework.android.game.core.graphics.opengl.GLEx;
import org.loon.framework.android.game.utils.BufferUtils;

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
public class Points {

	private GLColor color = new GLColor(0, 0, 0, 0);

	private final FloatBuffer buffer;

	private int size;

	private float lineWidth = 1;

	/**
	 * 构建顶点坐标
	 * 
	 * @param points
	 */
	public Points(List<Point> points) {
		size = points.size();
		buffer = BufferUtils.createFloatBuffer(points.size() * 3);
		for (Point point : points) {
			buffer.put(point.x);
			buffer.put(point.y);
			buffer.put(0);
		}
		buffer.position(0);
	}

	public void setColor(GLColor color) {
		this.color = color;
	}

	public GLColor getColor() {
		return color;
	}

	public void setLineWidth(float lineWidth) {
		this.lineWidth = lineWidth;
	}

	public float getLineWidth() {
		return lineWidth;
	}

	public final void draw(GLEx gl) {
		if (gl.isClose()) {
			return;
		}
		gl.setLineWidth(lineWidth);
		gl.glTex2DDisable();
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, 0, buffer);
		gl.glColor4f(color.r, color.g, color.b, color.a);
		gl.glDrawArrays(GL10.GL_LINE_STRIP, 0, size);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.resetLineWidth();
		gl.resetColor();
	}

}
