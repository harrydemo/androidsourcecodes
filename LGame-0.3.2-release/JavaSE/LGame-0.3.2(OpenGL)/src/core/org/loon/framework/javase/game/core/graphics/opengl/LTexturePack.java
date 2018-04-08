package org.loon.framework.javase.game.core.graphics.opengl;

import java.util.ArrayList;

import org.loon.framework.javase.game.core.LRelease;

import org.loon.framework.javase.game.core.geom.RectBox;
import org.loon.framework.javase.game.core.geom.Point.Point2i;
import org.loon.framework.javase.game.core.geom.RectBox.Rect2i;
import org.loon.framework.javase.game.core.graphics.LImage;
import org.loon.framework.javase.game.core.graphics.device.LGraphics;
import org.loon.framework.javase.game.core.graphics.opengl.LTexture.Format;
import org.loon.framework.javase.game.utils.collection.ArrayMap;
import org.loon.framework.javase.game.utils.xml.XMLDocument;
import org.loon.framework.javase.game.utils.xml.XMLElement;
import org.loon.framework.javase.game.utils.xml.XMLParser;

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
public class LTexturePack implements LRelease {

	private final Point2i blittedSize = new Point2i();

	private final ArrayMap temps = new ArrayMap();

	private LTexture texture = null;

	private int count;

	GLColor colorMask;

	boolean useAlpha, packed, packing;

	private String fileName;

	public LTexturePack() {
		this(true);
	}

	public LTexturePack(boolean hasAlpha) {
		this.useAlpha = hasAlpha;
	}

	public LTexturePack(String path) {
		if (path == null || "".equals(path)) {
			throw new RuntimeException(path + " not found !");
		}
		XMLDocument doc = XMLParser.parse(path);
		XMLElement pack = doc.getRoot();
		this.fileName = pack.getAttribute("file", null);
		if (fileName != null) {
			ArrayList<XMLElement> blocks = pack.list("block");
			for (XMLElement e : blocks) {
				Entry entry = new Entry(null);
				final int id = e.getIntAttribute("id", count);
				entry.fileName = e.getAttribute("name", null);
				entry.bounds.left = e.getIntAttribute("left", 0);
				entry.bounds.top = e.getIntAttribute("top", 0);
				entry.bounds.right = e.getIntAttribute("right", 0);
				entry.bounds.bottom = e.getIntAttribute("bottom", 0);
				if (entry.fileName != null) {
					temps.put(entry.fileName, entry);
				} else {
					temps.put(String.valueOf(id), entry);
				}
				count++;
			}
			this.packing = false;
			this.packed = true;
		}
		this.useAlpha = true;
	}

	public synchronized LImage getImage(int id) {
		Entry entry = getEntry(id);
		if (entry != null) {
			if (entry.image != null && !entry.image.isClose()) {
				return entry.image;
			} else if (entry.fileName != null) {
				return LImage.createImage(entry.fileName);
			}
		}
		return null;
	}

	public synchronized LImage getImage(String name) {
		Entry entry = getEntry(name);
		if (entry != null) {
			if (entry.image != null && !entry.image.isClose()) {
				return entry.image;
			} else if (entry.fileName != null) {
				return LImage.createImage(entry.fileName);
			}
		}
		return null;
	}

	public synchronized int putImage(String res) {
		return putImage(res, LImage.createImage(res));
	}

	public synchronized int putImage(LImage image) {
		return putImage(System.currentTimeMillis() + "|"
				+ String.valueOf((count + 1)), image);
	}

	public synchronized int putImage(String name, LImage image) {
		checkPacked();
		if (image == null) {
			throw new NullPointerException();
		}
		if (image.getWidth() <= 0 || image.getHeight() <= 0) {
			throw new IllegalArgumentException(
					"width and height must be positive");
		}
		this.temps.put(name, new Entry(image));
		this.packing = true;
		this.count++;
		return temps.size() - 1;
	}

	public synchronized int putImage(String name, LTexture tex2d) {
		if (tex2d != null) {
			return putImage(name, tex2d.getImage());
		}
		return count;
	}

	public synchronized int putImage(LTexture tex2d) {
		if (tex2d != null) {
			return putImage(tex2d.getImage());
		}
		return count;
	}

	public synchronized int removeImage(String name) {
		if (name != null) {
			Entry e = getEntry(name);
			if (e != null) {
				if (e.image != null) {
					e.image.dispose();
					e.image = null;
					this.count--;
					this.packing = true;
					return temps.size() - 1;
				}
			}
		}
		return count;
	}

	public synchronized int removeImage(int id) {
		if (id > -1) {
			Entry e = getEntry(id);
			if (e != null) {
				if (e.image != null) {
					e.image.dispose();
					e.image = null;
					this.count--;
					this.packing = true;
					return temps.size() - 1;
				}
			}
		}
		return count;
	}

	public int size() {
		return count;
	}

	private synchronized LImage packImage() {
		checkPacked();
		if (packing) {
			if (temps.isEmpty()) {
				throw new IllegalStateException("Nothing to Pack !");
			}
			int maxWidth = 0;
			int maxHeight = 0;
			int totalArea = 0;
			for (int i = 0; i < temps.size(); i++) {
				Entry entry = (Entry) temps.get(i);
				int width = entry.image.getWidth();
				int height = entry.image.getHeight();
				if (width > maxWidth) {
					maxWidth = width;
				}
				if (height > maxHeight) {
					maxHeight = height;
				}
				totalArea += width * height;
			}
			Point2i size = new Point2i(closeTwoPower(maxWidth),
					closeTwoPower(maxHeight));
			boolean fitAll = false;
			loop: while (!fitAll) {
				int area = size.x * size.y;
				if (area < totalArea) {
					nextSize(size);
					continue;
				}
				Node root = new Node(size.x, size.y);
				for (int i = 0; i < temps.size(); i++) {
					Entry entry = (Entry) temps.get(i);
					Node inserted = root.insert(entry);
					if (inserted == null) {
						nextSize(size);
						continue loop;
					}
				}
				fitAll = true;
			}
			LImage image = new LImage(size.x, size.y, useAlpha);
			LGraphics g = image.getLGraphics();
			for (int i = 0; i < temps.size(); i++) {
				Entry entry = (Entry) temps.get(i);
				g.drawImage(entry.image, entry.bounds.left, entry.bounds.top);
			}
			g.dispose();
			g = null;
			packing = false;
			return image;
		}
		return null;
	}

	public synchronized LTexture pack() {
		return pack(Format.DEFAULT);
	}

	public synchronized LTexture pack(Format format) {
		if (texture != null && !packing) {
			return texture;
		}
		if (fileName != null) {
			texture = new LTexture(GLLoader.getTextureData(fileName), format);
		} else {
			LImage image = packImage();
			if (image == null) {
				return null;
			}
			if (texture != null) {
				texture.destroy();
				texture = null;
			}
			if (colorMask != null) {
				int[] pixels = image.getPixels();
				int size = pixels.length;
				int color = colorMask.getRGB();
				for (int i = 0; i < size; i++) {
					if (pixels[i] == color) {
						pixels[i] = 0xffffff;
					}
				}
				image.setPixels(pixels, image.getWidth(), image.getHeight());
			}
			texture = new LTexture(GLLoader.getTextureData(image), format);
			if (image != null) {
				image.dispose();
				image = null;
			}
		}
		return texture;
	}

	public synchronized LTexture getTexture() {
		return texture;
	}

	private synchronized Entry getEntry(int id) {
		return (Entry) temps.get(id);
	}

	private synchronized Entry getEntry(Object id) {
		return (Entry) temps.get(id);
	}

	public void glBegin() {
		if (count > 0) {
			if (GLEx.self != null) {
				GLEx.self.glBegin(GL.GL_TRIANGLE_STRIP, false);
			}
		}
	}

	public void glEnd() {
		if (count > 0) {
			if (GLEx.self != null) {
				GLEx.self.glEnd();
			}
		}
	}

	public Point2i draw(int id, float x, float y) {
		return draw(id, x, y, 0, null);
	}

	public Point2i draw(int id, float x, float y, float rotation, GLColor color) {
		this.pack();
		if (GLEx.self != null) {
			Entry entry = getEntry(id);
			if (entry == null) {
				return null;
			}
			if (GLEx.self.useGLBegin() && rotation == 0 && color == null) {
				GLEx.self.draw(texture, x, y, entry.bounds.width(),
						entry.bounds.height(), entry.bounds.left,
						entry.bounds.top, entry.bounds.right,
						entry.bounds.bottom, false);
			} else {
				GLEx.self.drawTexture(texture, x, y, entry.bounds.width(),
						entry.bounds.height(), entry.bounds.left,
						entry.bounds.top, entry.bounds.right,
						entry.bounds.bottom, rotation, color);
			}
			blittedSize.set(entry.bounds.width(), entry.bounds.height());
		}
		return blittedSize;
	}

	public Point2i draw(int id, float x, float y, float w, float h) {
		return draw(id, x, y, w, h, 0, null);
	}

	public Point2i draw(int id, float x, float y, float w, float h,
			float rotation, GLColor color) {
		this.pack();
		if (GLEx.self != null) {
			Entry entry = getEntry(id);
			if (entry == null) {
				return null;
			}
			if (GLEx.self.useGLBegin() && rotation == 0 && color == null) {
				GLEx.self.draw(texture, x, y, w, h, entry.bounds.left,
						entry.bounds.top, entry.bounds.right,
						entry.bounds.bottom, false);
			} else {
				GLEx.self.drawTexture(texture, x, y, w, h, entry.bounds.left,
						entry.bounds.top, entry.bounds.right,
						entry.bounds.bottom, rotation, color);
			}
			blittedSize.set(entry.bounds.width(), entry.bounds.height());
		}
		return blittedSize;
	}

	public Point2i draw(int id, float dx1, float dy1, float dx2, float dy2,
			float sx1, float sy1, float sx2, float sy2) {
		return draw(id, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, 0);
	}

	public Point2i draw(int id, float dx1, float dy1, float dx2, float dy2,
			float sx1, float sy1, float sx2, float sy2, float rotation) {
		return draw(id, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, rotation, null);
	}

	public Point2i draw(int id, float dx1, float dy1, float dx2, float dy2,
			float sx1, float sy1, float sx2, float sy2, float rotation,
			GLColor color) {
		this.pack();
		if (GLEx.self != null) {
			Entry entry = getEntry(id);
			if (entry == null) {
				return null;
			}
			if (GLEx.self.useGLBegin() && rotation == 0 && color == null) {
				GLEx.self.draw(texture, dx1, dy1, dx2, dy2, sx1
						+ entry.bounds.left, sy1 + entry.bounds.top, sx2
						+ entry.bounds.left, sy2 + entry.bounds.top, false);
			} else {
				GLEx.self.drawTexture(texture, dx1, dy1, dx2, dy2, sx1
						+ entry.bounds.left, sy1 + entry.bounds.top, sx2
						+ entry.bounds.left, sy2 + entry.bounds.top, rotation,
						color);
			}
			blittedSize.set(entry.bounds.width(), entry.bounds.height());
		}
		return blittedSize;
	}

	public Point2i draw(String name, float x, float y) {
		return draw(name, x, y, 0, null);
	}

	public Point2i draw(String name, float x, float y, float rotation,
			GLColor color) {
		this.pack();
		if (GLEx.self != null) {
			Entry entry = getEntry(name);
			if (entry == null) {
				return null;
			}
			if (GLEx.self.useGLBegin() && rotation == 0 && color == null) {
				GLEx.self.draw(texture, x, y, entry.bounds.width(),
						entry.bounds.height(), entry.bounds.left,
						entry.bounds.top, entry.bounds.right,
						entry.bounds.bottom, false);
			} else {
				GLEx.self.drawTexture(texture, x, y, entry.bounds.width(),
						entry.bounds.height(), entry.bounds.left,
						entry.bounds.top, entry.bounds.right,
						entry.bounds.bottom, rotation, color);
			}
			blittedSize.set(entry.bounds.width(), entry.bounds.height());
		}
		return blittedSize;
	}

	public Point2i draw(String name, float x, float y, float w, float h) {
		return draw(name, x, y, w, h, 0, null);
	}

	public Point2i draw(String name, float x, float y, float w, float h,
			float rotation, GLColor color) {
		this.pack();
		if (GLEx.self != null) {
			Entry entry = getEntry(name);
			if (entry == null) {
				return null;
			}
			if (GLEx.self.useGLBegin() && rotation == 0 && color == null) {
				GLEx.self.draw(texture, x, y, w, h, entry.bounds.left,
						entry.bounds.top, entry.bounds.right,
						entry.bounds.bottom, false);
			} else {
				GLEx.self.drawTexture(texture, x, y, w, h, entry.bounds.left,
						entry.bounds.top, entry.bounds.right,
						entry.bounds.bottom, rotation, color);
			}
			blittedSize.set(entry.bounds.width(), entry.bounds.height());
		}
		return blittedSize;
	}

	public Point2i draw(String name, float dx1, float dy1, float dx2,
			float dy2, float sx1, float sy1, float sx2, float sy2) {
		return draw(name, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, 0);
	}

	public Point2i draw(String name, float dx1, float dy1, float dx2,
			float dy2, float sx1, float sy1, float sx2, float sy2,
			float rotation) {
		return draw(name, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, 0, null);
	}

	public Point2i draw(String name, float dx1, float dy1, float dx2,
			float dy2, float sx1, float sy1, float sx2, float sy2,
			float rotation, GLColor color) {
		this.pack();
		if (GLEx.self != null) {
			Entry entry = getEntry(name);
			if (entry == null) {
				return null;
			}
			if (GLEx.self.useGLBegin() && rotation == 0 && color == null) {
				GLEx.self.draw(texture, dx1, dy1, dx2, dy2, sx1
						+ entry.bounds.left, sy1 + entry.bounds.top, sx2
						+ entry.bounds.left, sy2 + entry.bounds.top, false);
			} else {
				GLEx.self.drawTexture(texture, dx1, dy1, dx2, dy2, sx1
						+ entry.bounds.left, sy1 + entry.bounds.top, sx2
						+ entry.bounds.left, sy2 + entry.bounds.top, rotation,
						color);
			}
			blittedSize.set(entry.bounds.width(), entry.bounds.height());
		}
		return blittedSize;
	}

	public RectBox getImageRect(int id) {
		Entry entry = getEntry(id);
		if (entry == null) {
			return new RectBox(0, 0, 0, 0);
		}
		return new RectBox(0, 0, entry.width, entry.height);
	}

	public int[] getImageRectArray(int id) {
		Entry entry = getEntry(id);
		if (entry == null) {
			return new int[] { 0, 0 };
		}
		return new int[] { entry.width, entry.height };
	}

	public Rect2i getImageSize(int id) {
		Entry entry = getEntry(id);
		if (entry == null) {
			return null;
		}
		return entry.bounds;
	}

	public Rect2i getImageSize(String name) {
		Entry entry = getEntry(name);
		if (entry == null) {
			return null;
		}
		return entry.bounds;
	}

	private void nextSize(Point2i size) {
		if (size.x > size.y) {
			size.y <<= 1;
		} else {
			size.x <<= 1;
		}
	}

	public String getFileName() {
		return fileName;
	}

	private int closeTwoPower(int i) {
		int power = 1;
		while (power < i) {
			power <<= 1;
		}
		return power;
	}

	private void checkPacked() {
		if (packed) {
			throw new IllegalStateException("the packed !");
		}
	}

	public void packed() {
		this.packed(Format.DEFAULT);
	}

	public synchronized void packed(Format format) {
		this.pack(format);
		this.packed = true;
		this.free();
	}

	private class Entry {

		private final Rect2i bounds = new Rect2i();

		private LImage image;

		private String fileName;

		private int width, height;

		private Entry(LImage image) {
			this.image = image;
			if (image != null) {
				this.fileName = image.getPath();
				this.width = image.getWidth();
				this.height = image.getHeight();
			}
		}

	}

	private class Node {

		private final Node[] child = new Node[2];

		private final Rect2i bounds = new Rect2i();

		private Entry entry;

		private Node() {
		}

		private Node(int width, int height) {
			bounds.set(0, 0, width, height);
		}

		private boolean isLeaf() {
			return (child[0] == null) && (child[1] == null);
		}

		private Node insert(Entry entry) {
			if (isLeaf()) {
				if (this.entry != null) {
					return null;
				}
				int width = entry.image.getWidth();
				int height = entry.image.getHeight();

				if ((width > bounds.width()) || (height > bounds.height())) {
					return null;
				}

				if ((width == bounds.width()) && (height == bounds.height())) {
					this.entry = entry;
					this.entry.bounds.set(this.bounds);
					return this;
				}

				child[0] = new Node();
				child[1] = new Node();

				int dw = bounds.width() - width;
				int dh = bounds.height() - height;

				if (dw > dh) {
					child[0].bounds.set(bounds.left, bounds.top, bounds.left
							+ width, bounds.bottom);
					child[1].bounds.set(bounds.left + width, bounds.top,
							bounds.right, bounds.bottom);
				} else {
					child[0].bounds.set(bounds.left, bounds.top, bounds.right,
							bounds.top + height);
					child[1].bounds.set(bounds.left, bounds.top + height,
							bounds.right, bounds.bottom);
				}
				return child[0].insert(entry);
			} else {
				Node newNode = child[0].insert(entry);
				if (newNode != null) {
					return newNode;
				}
				return child[1].insert(entry);
			}
		}

	}

	public GLColor getColorMask() {
		return colorMask;
	}

	public void setColorMask(GLColor colorMask) {
		this.colorMask = colorMask;
	}

	private void free() {
		if (temps != null) {
			for (int i = 0; i < temps.size(); i++) {
				Entry e = (Entry) temps.get(i);
				if (e != null) {
					if (e.image != null) {
						e.image.dispose();
						e.image = null;
					}
				}
			}
		}
	}

	public String toString() {
		StringBuffer sbr = new StringBuffer(1000);
		sbr.append("<?xml version=\"1.0\" standalone=\"yes\" ?>\n");
		if (colorMask != null) {
			sbr.append("<pack file=\"" + fileName + "\" mask=\""
					+ colorMask.getRed() + "," + colorMask.getGreen() + ","
					+ colorMask.getBlue() + "\">\n");
		} else {
			sbr.append("<pack file=\"" + fileName + "\">\n");
		}
		for (int i = 0; i < temps.size(); i++) {
			Entry e = (Entry) temps.get(i);
			if (e != null && e.bounds != null) {
				sbr.append("<block id=\"" + i + "\" name=\"" + e.fileName
						+ "\" left=\"" + e.bounds.left + "\" top=\""
						+ e.bounds.top + "\" right=\"" + e.bounds.right
						+ "\" bottom=\"" + e.bounds.bottom + "\"/>\n");
			}
		}
		sbr.append("</pack>");
		return sbr.toString();
	}

	public synchronized void dispose() {
		free();
		if (texture != null) {
			texture.destroy();
			texture = null;
		}
	}
}
