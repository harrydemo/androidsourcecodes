package org.loon.framework.android.game.core.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

import org.loon.framework.android.game.core.LSystem;
import org.loon.framework.android.game.utils.StringUtils;

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
// 在JavaSE版中没有SDCard，操作模式与FileRes一致，而在Android版中则为SDCard操作(如果未安装SD卡，则尝试读取本地缓存中文件)
public class SDRes extends DataRes implements Resource {

	public SDRes(String path) {
		if (isMoutedSD()) {
			File f = android.os.Environment.getExternalStorageDirectory();
			String tmp = f.getPath();
			if (path.startsWith("/")) {
				path = path.substring(1);
			}
			if (!tmp.endsWith("/")) {
				path = tmp + "/" + path;
			} else {
				path = tmp + path;
			}
		} else {
			path = LSystem.getCacheFileName();
			path = StringUtils.replaceIgnoreCase(path, "\\", "/");
			if (path.startsWith("/") || path.startsWith("\\")) {
				path = path.substring(1, path.length());
			}
		}
		this.path = path;
		this.name = "sdcard://" + path;
	}

	public final static boolean isMoutedSD() {
		String sdState = android.os.Environment.getExternalStorageState();
		return sdState.equals(android.os.Environment.MEDIA_MOUNTED);
	}

	public InputStream getInputStream() {
		try {
			if (in != null) {
				return in;
			}
			return (in = new FileInputStream(new File(path)));
		} catch (FileNotFoundException e) {
			throw new RuntimeException("file " + name + " not found !", e);
		}
	}

	public String getResourceName() {
		return name;
	}

	public URI getURI() {
		try {
			if (uri != null) {
				return uri;
			}
			return (uri = new URL(path).toURI());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		SDRes other = (SDRes) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}

}
