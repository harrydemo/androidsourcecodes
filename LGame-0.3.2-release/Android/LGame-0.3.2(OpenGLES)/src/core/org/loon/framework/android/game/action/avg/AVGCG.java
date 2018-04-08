package org.loon.framework.android.game.action.avg;

import org.loon.framework.android.game.core.graphics.opengl.LTexture;
import org.loon.framework.android.game.core.graphics.opengl.LTexture.Format;
import org.loon.framework.android.game.utils.collection.ArrayMap;

/**
 * Copyright 2008 - 2010
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
public class AVGCG {

	private LTexture background;

	private ArrayMap charas;

	public AVGCG() {
		charas = new ArrayMap(100);
	}

	public LTexture getBackgroundCG() {
		return background;
	}

	public void noneBackgroundCG() {
		if (background != null) {
			background.destroy();
			background = null;
		}
	}

	public void setBackgroundCG(LTexture backgroundCG) {
		if (backgroundCG == this.background) {
			return;
		}
		if (background != null) {
			background.destroy();
			background = null;
		}
		this.background = backgroundCG;
	}

	public void setBackgroundCG(String resName) {
		this.setBackgroundCG(new LTexture(resName,Format.STATIC));
	}

	public void addChara(String file, AVGChara role) {
		charas.put(file.replaceAll(" ", "").toLowerCase(), role);
	}

	public void addImage(String name, int x, int y, int w) {
		String keyName = name.replaceAll(" ", "").toLowerCase();
		AVGChara chara = (AVGChara) charas.get(keyName);
		if (chara == null) {
			charas.put(keyName, new AVGChara(name, x, y, w));
		} else {
			chara.setX(x);
			chara.setY(y);
		}
	}

	public AVGChara removeImage(String file) {
		return (AVGChara) charas.remove(file.replaceAll(" ", "").toLowerCase());
	}

	public void dispose() {
		for (int i = 0; i < charas.size(); i++) {
			AVGChara ch = (AVGChara) charas.get(i);
			if (ch != null) {
				ch.dispose();
				ch = null;
			}
		}
		charas.clear();
	}

	public void clear() {
		charas.clear();
	}

	public ArrayMap getCharas() {
		return charas;
	}

}
