package org.loon.framework.android.game.core.resource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.loon.framework.android.game.core.LSystem;
import org.loon.framework.android.game.utils.CollectionUtils;

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
 * @version 0.1.1
 */
public class ZIPResource {

	private static HashMap<String, byte[]> zipLazy = new HashMap<String, byte[]>(
			LSystem.DEFAULT_MAX_CACHE_SIZE);

	private static ArrayList<ZipFile> zipFiles = new ArrayList<ZipFile>(
			CollectionUtils.INITIAL_CAPACITY);

	public ZIPResource() {
	}

	/**
	 * 加载内部zip文件
	 * 
	 * @param fileName
	 * @throws ZipException
	 * @throws IOException
	 */
	public void addArchive(String fileName) throws ZipException, IOException {
		addArchive(new ZipInputStream(Resources.openResource(fileName)));
	}

	/**
	 * 加载zip流
	 * 
	 * @param in
	 * @throws ZipException
	 * @throws IOException
	 */
	public void addArchive(ZipInputStream in) throws ZipException, IOException {
		ZipEntry ze = null;
		int read;
		byte buffer[] = new byte[8192];
		do {
			ze = in.getNextEntry();
			if (ze != null) {
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				while ((read = in.read(buffer)) >= 0) {
					os.write(buffer, 0, read);
				}
				zipLazy.put(ze.getName(), os.toByteArray());
			}
		} while (ze != null);
	}

	/**
	 * 加载网络文件
	 * 
	 * @param url
	 * @throws ZipException
	 * @throws IOException
	 */
	public void addArchive(URL url) throws ZipException, IOException {
		addArchive(new ZipInputStream(url.openStream()));
	}

	/**
	 * 加载外部zip文件
	 * 
	 * @param archiveName
	 * @throws Exception
	 */
	public void addZipFile(String archiveName) throws Exception {
		ZipFile file = new ZipFile(archiveName);
		zipFiles.add(file);
	}

	public int count() {
		return zipLazy.size();
	}

	private byte[] loadData(ZipFile zipFile, String name) {
		InputStream is = null;
		ByteArrayOutputStream os = null;
		try {
			ZipEntry entry = zipFile.getEntry(name);
			if (entry == null) {
				return null;
			}
			is = zipFile.getInputStream(entry);
			os = new ByteArrayOutputStream();
			byte[] buffer = new byte[8192];
			int countBytes = 0;
			while (countBytes < entry.getSize()) {
				int len = is.read(buffer);
				if (len > 0) {
					countBytes += len;
					os.write(buffer, 0, len);
				}
			}
			return os.toByteArray();
		} catch (Exception e) {
			return null;
		} finally {
			try {
				if (os != null) {
					os.flush();
					os.close();
					os = null;
				}
				if (is != null) {
					is.close();
					is = null;
				}
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 读取指定名称的zip资源
	 * 
	 * @param name
	 * @return
	 */
	public byte[] loadData(String name) {
		if (zipLazy.size() > LSystem.DEFAULT_MAX_CACHE_SIZE / 3) {
			zipLazy.size();
			System.gc();
		}
		byte data[] = null;
		try {
			data = (byte[]) zipLazy.get(name);
			if (data == null) {
				for (Iterator<ZipFile> it = zipFiles.iterator(); it.hasNext()
						&& data == null;) {
					data = loadData((ZipFile) it.next(), name);
					zipLazy.put(name, data);
				}
			}
		} catch (Throwable t) {
			return null;
		}
		return data;
	}

	/**
	 * 清空资源
	 * 
	 */
	public static void destroy() {
		zipLazy.clear();
		zipFiles.clear();
	}

}
