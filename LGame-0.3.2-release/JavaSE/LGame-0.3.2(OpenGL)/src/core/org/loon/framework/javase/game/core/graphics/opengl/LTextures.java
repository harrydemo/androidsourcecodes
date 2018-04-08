package org.loon.framework.javase.game.core.graphics.opengl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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

public class LTextures {

	private static Map<String, Ref> lazyTextures = Collections
			.synchronizedMap(new HashMap<String, Ref>(20));

	final static ArrayList<Integer> copyToTextures = new ArrayList<Integer>(10);

	public static class Ref {

		public String name;

		private int refCount;

		private LTexture texture;

		public Ref(String name, LTexture texture) {
			this.name = name;
			this.texture = texture;
			this.refCount = 1;
		}

		public void deleteRefCount() {
			refCount--;
		}

		public void addRefCount() {
			refCount++;
		}

		public int unload() {
			if (--refCount == 0) {
				if (texture != null) {
					texture.dispose();
					texture = null;
				}
			}
			return refCount;
		}

		void dispose(boolean remove) {
			if (texture != null) {
				texture.dispose(remove);
			}
		}

		void destroy(boolean remove) {
			if (texture != null) {
				texture.dispose(remove);
			}
		}

		public void dispose() {
			dispose(true);
		}

		void destroy() {
			destroy(true);
		}

		public void bind() {
			if (texture != null) {
				texture.bind();
			}
		}

		public LTexture get() {
			return texture;
		}
	}

	public static Ref loadTexture(String path) {
		return loadTexture(path, Format.DEFAULT);
	}

	public static boolean containsValue(LTexture texture) {
		return lazyTextures.containsValue(texture);
	}

	public static Ref loadTexture(String fileName, Format format) {
		if (fileName == null) {
			return null;
		}
		fileName = fileName.trim().toLowerCase();
		if (lazyTextures.containsKey(fileName)) {
			Ref ref = lazyTextures.get(fileName);
			ref.addRefCount();
			return ref;
		}
		LTexture texture = new LTexture(fileName, format);
		texture.lazyName = fileName;
		Ref ref = new Ref(fileName, texture);
		lazyTextures.put(fileName, ref);
		return ref;
	}

	public static Ref loadTexture(LTexture texture) {
		return loadTexture(System.currentTimeMillis(), texture);
	}

	public static Ref loadTexture(long id, LTexture texture) {
		if (texture == null) {
			return null;
		}
		String key = texture.lazyName == null ? String.valueOf(id)
				: texture.lazyName;
		if (lazyTextures.containsKey(key)) {
			Ref ref = lazyTextures.get(key);
			ref.addRefCount();
			return ref;
		}
		texture.lazyName = key;
		Ref ref = new Ref(key, texture);
		lazyTextures.put(key, ref);
		return ref;
	}

	public static void removeTexture(String name) {
		lazyTextures.remove(name);
	}

	public static void removeTexture(LTexture texture) {
		removeTexture(texture.lazyName);
	}

	public static void reload() {
		for (Ref tex : lazyTextures.values()) {
			LTexture tex2d = tex.get();
			if (tex2d != null) {
				tex2d.reload();
			}
		}
		LSTRTexture.reload();
		copyToTextures.clear();
	}

	public static void disposeAll() {
		for (Ref tex : lazyTextures.values()) {
			tex.dispose(false);
			tex = null;
		}
		lazyTextures.clear();
		LSTRTexture.dispose();
		copyToTextures.clear();
	}

	public static void destroyAll() {
		for (Ref tex : lazyTextures.values()) {
			tex.destroy(false);
			tex = null;
		}
		lazyTextures.clear();
		LSTRTexture.dispose();
		copyToTextures.clear();
	}
}
