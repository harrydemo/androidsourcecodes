package org.loon.framework.android.game.action.sprite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import org.loon.framework.android.game.core.geom.RectBox;
import org.loon.framework.android.game.core.graphics.opengl.GL;
import org.loon.framework.android.game.core.graphics.opengl.GLColor;
import org.loon.framework.android.game.core.graphics.opengl.GLEx;

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
public class WaitAnimation {

	private final GLColor defaultBlackColor;

	private final GLColor defaultWhiteColor;

	private final double sx = 1.0D, sy = 1.0D;

	private final int ANGLE_STEP = 15;

	private final int ARCRADIUS = 120;

	private GLColor color;

	private double r;

	private ArrayList<Object> list;

	private boolean isRunning = false;

	private int width, height;

	private int angle;

	private int style;

	private int paintX, paintY, paintWidth, paintHeight;

	private GLColor fill;

	public WaitAnimation(int s, int width, int height) {
		this.style = s;
		this.width = width;
		this.height = height;
		this.defaultBlackColor = new GLColor(0.0f, 0.0f, 0.0f);
		this.defaultWhiteColor = new GLColor(0.9f, 0.9f, 0.9f);
		this.color = defaultBlackColor;
		switch (style) {
		case 0:
			int r1 = width / 8,
			r2 = height / 8;
			this.r = (r1 < r2 ? r1 : r2) / 2;
			this.list = new ArrayList<Object>(Arrays.asList(new Object[] {
					new RectBox(sx + 3 * r, sy + 0 * r, 2 * r, 2 * r),
					new RectBox(sx + 5 * r, sy + 1 * r, 2 * r, 2 * r),
					new RectBox(sx + 6 * r, sy + 3 * r, 2 * r, 2 * r),
					new RectBox(sx + 5 * r, sy + 5 * r, 2 * r, 2 * r),
					new RectBox(sx + 3 * r, sy + 6 * r, 2 * r, 2 * r),
					new RectBox(sx + 1 * r, sy + 5 * r, 2 * r, 2 * r),
					new RectBox(sx + 0 * r, sy + 3 * r, 2 * r, 2 * r),
					new RectBox(sx + 1 * r, sy + 1 * r, 2 * r, 2 * r) }));
			break;
		case 1:
			this.fill = new GLColor(165, 0, 0, 255);
			this.paintX = (width - ARCRADIUS);
			this.paintY = (height - ARCRADIUS);
			this.paintWidth = paintX + ARCRADIUS;
			this.paintHeight = paintY + ARCRADIUS;
			break;
		}
	}

	public void setColor(GLColor color) {
		this.color = color;
	}

	public void black() {
		this.color = defaultBlackColor;
	}

	public void white() {
		this.color = defaultWhiteColor;
	}

	public void next() {
		if (isRunning) {
			switch (style) {
			case 0:
				list.add(list.remove(0));
				break;
			case 1:
				angle += ANGLE_STEP;
				break;
			}
		}
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	public void draw(GLEx g, int x, int y) {
		draw(g, x, y, width, height);
	}

	public void draw(GLEx g, int x, int y, int w, int h) {
		switch (style) {
		case 0:
			GLColor oldColor = g.getColor();
			g.setColor(color);
			float alpha = 0.0f;
			int nx = x + w / 2 - (int) r * 4,
			ny = y + h / 2 - (int) r * 4;
			g.translate(nx, ny);
			for (Iterator<Object> it = list.iterator(); it.hasNext();) {
				RectBox s = (RectBox) it.next();
				alpha = isRunning ? alpha + 0.1f : 0.5f;
				g.setAlpha(alpha);
				g.fillOval(s.x, s.y, s.width, s.height);
			}
			g.setAlpha(1.0F);
			g.translate(-nx, -ny);
			g.setColor(oldColor);
			break;
		case 1:
			g.beginBlend(GL.MODE_SPEED);
			g.setLineWidth(10);
			g.translate(x, y);
			g.setColor(fill);
			g.drawOval(0, 0, width, height);
			int sa = angle % 360;
			g.fillArc(x + (width - paintWidth) / 2, y + (height - paintHeight)
					/ 2, paintWidth, paintHeight, sa, sa + ANGLE_STEP);
			g.translate(-x, -y);
			g.resetLineWidth();
			g.endBlend();
			break;
		}
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}
