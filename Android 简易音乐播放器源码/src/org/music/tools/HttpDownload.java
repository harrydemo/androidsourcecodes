package org.music.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
/**
 * 文件下载类,涉及到文件输入/输出这部分，还有下载的音乐文件如果带有中文乱码
 */
public class HttpDownload {
	public static  String downloadStringFromInternetFile(String urlStr) {
		StringBuffer sb = null;
		String line = null;
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader buffer = null;
		try {
			inputStream = getInputStreamFromUrl(urlStr);
			inputStreamReader = new InputStreamReader(inputStream, "GB2312");//服务器显示UTF-8，我这边写GB2312,如写GB2312就乱码了。哎哟，这个真鬼畜
			// 使用IO流读取数据
			buffer = new BufferedReader(inputStreamReader);
			sb = new StringBuffer();
			while ((line = buffer.readLine()) != null) {
				sb.append(line.trim());
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (buffer != null) {
					buffer.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (inputStreamReader != null) {
						inputStreamReader.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						if (inputStream != null) {
							inputStream.close();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return null;
	}

	/**
	 * 该函数返回整形 -1：代表下载文件出错 0：代表下载文件成功 1：代表文件已经存在
	 */
	public int downLoadFile(String urlStr, String path, String finalFileName) {
		InputStream inputStream = null;
		try {
			if (FileUtil.isFileExist(finalFileName+".lrc", path)) {
				FileUtil.deleteFile(path, finalFileName+".lrc");
			}
			inputStream = getInputStreamFromUrl(urlStr);
			File resultFile = FileUtil.write2SDFromInput(path, finalFileName+".lrc",
					inputStream);
			if (resultFile == null) {
				return -1;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

	/**
	 * 根据URL得到输入流
	 */
	public static InputStream getInputStreamFromUrl(String urlStr)
			throws IOException {
		URL url = null;
		HttpURLConnection urlConn = null;
		url = new URL(urlStr);
		urlConn = (HttpURLConnection) url.openConnection();
		urlConn.setConnectTimeout(6000);
		urlConn.setReadTimeout(6000);
		urlConn.setRequestMethod("GET");
		return urlConn.getInputStream();
	}

}
