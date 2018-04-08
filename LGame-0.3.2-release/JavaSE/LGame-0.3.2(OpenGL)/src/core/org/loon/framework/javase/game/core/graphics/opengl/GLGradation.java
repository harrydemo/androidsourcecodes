package org.loon.framework.javase.game.core.graphics.opengl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

import org.loon.framework.javase.game.core.LRelease;
import org.loon.framework.javase.game.core.LSystem;
import org.loon.framework.javase.game.core.graphics.LGradation;
import org.loon.framework.javase.game.core.graphics.LImage;
import org.loon.framework.javase.game.core.graphics.device.LGraphics;
import org.loon.framework.javase.game.core.graphics.opengl.LTexture.Format;

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
public class GLGradation implements LRelease {

	private GLColor start;

	private GLColor end;

	private int width, height, alpha;

	private LTexture drawWidth, drawHeight;

	private static HashMap<String, GLGradation> lazyGradation;

	public static GLGradation getInstance(GLColor s, GLColor e, int w, int h) {
		return getInstance(s, e, w, h, 125);
	}

	public static GLGradation getInstance(GLColor s, GLColor e, int w, int h,
			int alpha) {
		if (lazyGradation == null) {
			lazyGradation = new HashMap<String, GLGradation>(10);
		}
		int hashCode = 1;
		hashCode = LSystem.unite(hashCode, s.getRGB());
		hashCode = LSystem.unite(hashCode, e.getRGB());
		hashCode = LSystem.unite(hashCode, w);
		hashCode = LSystem.unite(hashCode, h);
		hashCode = LSystem.unite(hashCode, alpha);
		String key = String.valueOf(hashCode);
		GLGradation o = (GLGradation) lazyGradation.get(key);
		if (o == null) {
			lazyGradation.put(key, o = new GLGradation(s, e, w, h, alpha));
		}
		return o;
	}

	private GLGradation() {

	}

	private GLGradation(GLColor s, GLColor e, int w, int h, int alpha) {
		this.start = s;
		this.end = e;
		this.width = w;
		this.height = h;
		this.alpha = alpha;
	}

	public synchronized void drawWidth(GLEx g, int x, int y) {
		try {
			if (drawWidth == null) {
				LImage img = new LImage(width, height, true);
				LGraphics gl = img.getLGraphics();
				for (int i = 0; i < width; i++) {
					gl.setColor((start.getRed() * (width - i))
							/ width + (end.getRed() * i) / width, (start
							.getGreen() * (width - i))
							/ width + (end.getGreen() * i) / width, (start
							.getBlue() * (width - i))
							/ width + (end.getBlue() * i) / width, alpha);
					gl.drawLine(i, 0, i, height);
				}
				drawWidth = new LTexture(GLLoader.getTextureData(img),
						Format.SPEED);
				gl.dispose();
				gl = null;
			}
			g.drawTexture(drawWidth, x, y);
		} catch (Exception ex) {
			ex.printStackTrace();
			for (int i = 0; i < width; i++) {
				g.setColorValue((start.getRed() * (width - i)) / width
						+ (end.getRed() * i) / width,
						(start.getGreen() * (width - i)) / width
								+ (end.getGreen() * i) / width, (start
								.getBlue() * (width - i))
								/ width + (end.getBlue() * i) / width, alpha);
				g.drawLine(i + x, y, i + x, y + height);
			}
		}
	}

	public synchronized void drawHeight(GLEx g, int x, int y) {
		try {
			if (drawHeight == null) {
				LImage img = new LImage(width, height, true);
				LGraphics gl = img.getLGraphics();
				for (int i = 0; i < height; i++) {
					gl.setColor((start.getRed() * (height - i))
							/ height + (end.getRed() * i) / height, (start
							.getGreen() * (height - i))
							/ height + (end.getGreen() * i) / height, (start
							.getBlue() * (height - i))
							/ height + (end.getBlue() * i) / height, alpha);
					gl.drawLine(0, i, width, i);
				}
				drawHeight = new LTexture(GLLoader.getTextureData(img),
						Format.SPEED);
				gl.dispose();
				gl = null;
			}
			g.drawTexture(drawHeight, x, y);
		} catch (Exception ex) {
			ex.printStackTrace();
			for (int i = 0; i < height; i++) {
				g.setColorValue((start.getRed() * (height - i)) / height
						+ (end.getRed() * i) / height,
						(start.getGreen() * (height - i)) / height
								+ (end.getGreen() * i) / height, (start
								.getBlue() * (height - i))
								/ height + (end.getBlue() * i) / height, alpha);
				g.drawLine(x, i + y, x + width, i + y);
			}
		}
	}

	public static void close() {
		if (lazyGradation == null) {
			return;
		}
		Set entrys = lazyGradation.entrySet();
		for (Iterator it = entrys.iterator(); it.hasNext();) {
			Entry e = (Entry) it.next();
			GLGradation g = (GLGradation) e.getValue();
			if (g != null) {
				g.dispose();
				g = null;
			}
		}
		lazyGradation.clear();
	}

	public void dispose() {
		if (drawWidth != null) {
			drawWidth.destroy();
		}
		if (drawHeight != null) {
			drawHeight.destroy();
		}
		LGradation.close();
	}

}
