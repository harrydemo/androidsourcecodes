package org.loon.framework.javase.game.core.graphics.opengl;

import java.nio.Buffer;

import org.loon.framework.javase.game.core.LRelease;
import org.loon.framework.javase.game.core.graphics.LPixmap;

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
public abstract class LTextureData implements LRelease {

	int width, height;

	int texWidth, texHeight;

	boolean hasAlpha;

	LPixmap pixmap;

	Buffer source;

	int[] pixels;

	String fileName;

	public abstract LTextureData copy();

	public int getHeight() {
		return height;
	}

	public Buffer getSource() {
		return source;
	}

	public int getTexHeight() {
		return texHeight;
	}

	public int getTexWidth() {
		return texWidth;
	}

	public boolean hasAlpha() {
		return hasAlpha;
	}

	public int getWidth() {
		return width;
	}

	public String getFileName() {
		return fileName;
	}

	public void dispose() {
		if (pixels != null) {
			pixels = null;
		}
		if (source != null) {
			source.clear();
			source = null;
		}
	}

}
